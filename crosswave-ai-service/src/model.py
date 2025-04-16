import os

import pandas as pd
import torch
from numpy import ndarray
from torch_geometric.data import Data
from torch_geometric.nn.conv.gcn_conv import GCNConv
from torch_geometric.utils import negative_sampling

MODEL_DIR = "model_save"
MODEL_PATH = os.path.join(MODEL_DIR, "gcn_model.pt")
MODEL_STATE_PATH = os.path.join(MODEL_DIR, "gcn_state.pt")

os.makedirs(MODEL_DIR, exist_ok=True)

def load_and_preprocess_data(filepath):
    data = pd.read_csv(filepath)

    unique_users = pd.unique(data[['USER_ID', 'TARGET_USER_ID']].stack().dropna())
    unique_tracks = pd.unique(data['TRACK_ID'])

    user_to_idx = {user_id: idx for idx, user_id in enumerate(unique_users)}
    track_to_idx = {track_id: idx for idx, track_id in enumerate(unique_tracks)}

    num_users = len(unique_users)
    num_tracks = len(unique_tracks)

    edge_index = []
    edge_attr = []

    event_weights = {
        'VIEW': 1,
        'LISTEN': 2,
        'LIKE': 3,
        'SUBSCRIBE': 4,
        'UNSUBSCRIBE': -2
    }

    for _, row in data.iterrows():
        weight = event_weights.get(row['EVENT_TYPE'], 1)

        if row['EVENT_TYPE'] in ['SUBSCRIBE', 'UNSUBSCRIBE'] and pd.notna(row['TARGET_USER_ID']):
            user_idx = user_to_idx[row['USER_ID']]
            target_user_idx = user_to_idx[row['TARGET_USER_ID']]

            edge_index.append([user_idx, target_user_idx])
            edge_attr.append(weight)
            edge_index.append([target_user_idx, user_idx])
            edge_attr.append(weight * 0.5)
        elif pd.notna(row['USER_ID']):
            user_idx = user_to_idx[row['USER_ID']]
            track_idx = track_to_idx[row['TRACK_ID']] + num_users

            edge_index.append([user_idx, track_idx])
            edge_attr.append(weight)
            edge_index.append([track_idx, user_idx])
            edge_attr.append(weight * 0.7)

    edge_index = torch.tensor(edge_index, dtype=torch.long).t().contiguous()
    edge_attr = torch.tensor(edge_attr, dtype=torch.float)

    x = torch.ones((num_users + num_tracks, 1))
    graph_data = Data(x=x, edge_index=edge_index, edge_attr=edge_attr)

    return graph_data, user_to_idx, track_to_idx, num_users, num_tracks


class GCN(torch.nn.Module):
    def __init__(self, hidden_channels):
        super().__init__()
        self.conv1 = GCNConv(1, hidden_channels)
        self.conv2 = GCNConv(hidden_channels, hidden_channels)

    def forward(self, x, edge_index, edge_weight):
        x = self.conv1(x, edge_index, edge_weight).relu()
        x = self.conv2(x, edge_index, edge_weight)
        return x

def save_model(model, user_to_idx, track_to_idx, num_users, num_tracks):
    torch.save(model, MODEL_PATH)

    torch.save({
        'model_state_dict': model.state_dict(),
        'user_to_idx': user_to_idx,
        'track_to_idx': track_to_idx,
        'num_users': num_users,
        'num_tracks': num_tracks
    }, MODEL_STATE_PATH)

    print(f"Model saved to {MODEL_PATH} and state to {MODEL_STATE_PATH}")


def load_model(load_full_model=False):
    if load_full_model and os.path.exists(MODEL_PATH):
        model = torch.load(MODEL_PATH)
        print(f"Full model loaded from {MODEL_PATH}")
        return model
    elif os.path.exists(MODEL_STATE_PATH):
        checkpoint = torch.load(MODEL_STATE_PATH)

        model = GCN(hidden_channels=16)
        model.load_state_dict(checkpoint['model_state_dict'])

        print(f"Model state loaded from {MODEL_STATE_PATH}")
        return model, checkpoint
    else:
        raise FileNotFoundError("No model files found")


def train_model(data, epochs=100):
    model = GCN(hidden_channels=16)
    optimizer = torch.optim.Adam(model.parameters(), lr=0.01)

    for epoch in range(epochs):
        model.train()
        optimizer.zero_grad()

        z = model(data.x, data.edge_index, data.edge_attr)

        pos_mask = data.edge_attr > 0
        pos_edge_index = data.edge_index[:, pos_mask]

        neg_mask = data.edge_attr < 0
        neg_edge_index_existing = data.edge_index[:, neg_mask]

        neg_edge_index_random = negative_sampling(
            edge_index=data.edge_index,
            num_nodes=data.num_nodes,
            num_neg_samples=data.edge_index.size(1) // 2)

        neg_edge_index = torch.cat([neg_edge_index_existing, neg_edge_index_random], dim=1)

        pos_loss = -torch.log(
            torch.sigmoid((z[pos_edge_index[0]] * z[pos_edge_index[1]]).sum(dim=1))).mean()

        neg_loss = -torch.log(
            1 - torch.sigmoid((z[neg_edge_index[0]] * z[neg_edge_index[1]]).sum(dim=1))).mean()

        loss = pos_loss + neg_loss
        loss.backward()
        optimizer.step()

        if epoch % 10 == 0:
            print(f'Epoch: {epoch}, Loss: {loss.item():.4f}')

    return model

def retrain():
    print("Training new model...")
    data, user_to_idx, track_to_idx, num_users, num_tracks = load_and_preprocess_data('recommendations/data_input/analytics.csv')
    model = train_model(data)
    save_model(model, user_to_idx, track_to_idx, num_users, num_tracks)

def recommend(user_id, k_users=5, k_tracks=5):
    data, user_to_idx, track_to_idx, num_users, num_tracks = load_and_preprocess_data('recommendations/data_input/analytics.csv')

    try:
        model, checkpoint = load_model()
        user_to_idx = checkpoint['user_to_idx']
        track_to_idx = checkpoint['track_to_idx']
        num_users = checkpoint['num_users']
        num_tracks = checkpoint['num_tracks']
        print("Loaded existing model")
    except FileNotFoundError:
        return [], []

    model.eval()
    with torch.no_grad():
        z = model(data.x, data.edge_index, data.edge_attr)

        user_idx = user_to_idx[user_id]
        user_embedding = z[user_idx]

        other_users = [i for i in range(num_users) if i != user_idx]
        similarities = torch.matmul(z[other_users], user_embedding)

        subscribed_mask = (data.edge_index[0] == user_idx) & \
                          (data.edge_index[1] < num_users) & \
                          (data.edge_attr == 4)

        subscribed_users = data.edge_index[1, subscribed_mask]
        if len(subscribed_users) > 0:
            similarities[subscribed_users] *= 1.5

        top_k_users = torch.topk(similarities, k=min(k_users, len(similarities)))

        track_embeddings = z[num_users:]
        track_scores = torch.matmul(track_embeddings, user_embedding)
        top_k_tracks = torch.topk(track_scores, k=min(k_users, len(track_scores)))

        recommended_users = [list(user_to_idx.keys())[list(user_to_idx.values()).index(other_users[i])]
                             for i in top_k_users.indices]
        recommended_tracks = [list(track_to_idx.keys())[list(track_to_idx.values()).index(i)]
                              for i in top_k_tracks.indices]

        return recommended_users, recommended_tracks
