import pickle
from collections import defaultdict
import numpy as np
import pandas as pd
from sklearn.metrics.pairwise import cosine_similarity


class MusicRecommender:
    def __init__(self, data_path=None, model_path=None):
        self.user_track_interactions = defaultdict(dict)
        self.user_user_subscriptions = defaultdict(set)
        self.track_similarity = None
        self.user_similarity = None
        self.track_index = {}
        self.user_index = {}
        self.reverse_track_index = {}
        self.reverse_user_index = {}

        if model_path:
            self.load_model(model_path)
        elif data_path:
            self.load_data(data_path)
            self.build_models()

    def load_data(self, data_path):
        df = pd.read_csv(data_path)

        all_tracks = set(df['TRACK_ID'].unique())
        all_users = set(df['USER_ID'].unique())

        subscribe_events = df[df['EVENT_TYPE'].isin(['SUBSCRIBE', 'UNSUBSCRIBE'])]
        all_users.update(subscribe_events['TARGET_USER_ID'].dropna().unique())

        self.track_index = {track: idx for idx, track in enumerate(all_tracks)}
        self.reverse_track_index = {idx: track for track, idx in self.track_index.items()}

        self.user_index = {user: idx for idx, user in enumerate(all_users)}
        self.reverse_user_index = {idx: user for user, idx in self.user_index.items()}

        for _, row in df.iterrows():
            user = row['USER_ID']
            event_type = row['EVENT_TYPE']

            if event_type in ['SUBSCRIBE', 'UNSUBSCRIBE']:
                target_user = row['TARGET_USER_ID']
                if pd.notna(target_user):
                    if event_type == 'SUBSCRIBE':
                        self.user_user_subscriptions[user].add(target_user)
                    else:
                        self.user_user_subscriptions[user].discard(target_user)
                continue

            track = row['TRACK_ID']
            weight = 0
            if event_type == 'VIEW':
                weight = 1
            elif event_type == 'LISTEN':
                weight = 3
            elif event_type == 'LIKE':
                weight = 5

            if track in self.user_track_interactions[user]:
                self.user_track_interactions[user][track] += weight
            else:
                self.user_track_interactions[user][track] = weight

    def build_models(self):
        num_users = len(self.user_index)
        num_tracks = len(self.track_index)

        user_track_matrix = np.zeros((num_users, num_tracks))

        for user, tracks in self.user_track_interactions.items():
            user_idx = self.user_index[user]
            for track, weight in tracks.items():
                track_idx = self.track_index[track]
                user_track_matrix[user_idx][track_idx] = weight

        user_track_matrix += np.random.normal(0, 0.1, user_track_matrix.shape)

        track_matrix = user_track_matrix.T
        self.track_similarity = cosine_similarity(track_matrix)

        subscription_matrix = np.zeros((num_users, num_users))
        for user, subscriptions in self.user_user_subscriptions.items():
            user_idx = self.user_index[user]
            for target_user in subscriptions:
                if target_user in self.user_index:
                    target_idx = self.user_index[target_user]
                    subscription_matrix[user_idx][target_idx] = 1

        combined_user_matrix = np.concatenate([user_track_matrix, subscription_matrix], axis=1)
        self.user_similarity = cosine_similarity(combined_user_matrix)

    def recommend_tracks(self, user_id, top_n=10, include_subscriptions=False, min_similarity=0.1):
        if user_id not in self.user_index:
            return self.get_popular_tracks(top_n)

        user_idx = self.user_index[user_id]
        user_vector = np.zeros(len(self.track_index))

        for track, weight in self.user_track_interactions[user_id].items():
            track_idx = self.track_index[track]
            user_vector[track_idx] = weight

        scores = np.dot(self.track_similarity, user_vector)
        listened_tracks = set(self.user_track_interactions[user_id].keys())

        if include_subscriptions:
            for sub_user_id in self.user_user_subscriptions.get(user_id, set()):
                if sub_user_id in self.user_track_interactions:
                    for track in self.user_track_interactions[sub_user_id]:
                        if track in self.track_index:
                            track_idx = self.track_index[track]
                            scores[track_idx] += 0.5

        recommendations = []
        for track_idx in np.argsort(scores)[::-1]:
            track_id = self.reverse_track_index[track_idx]
            if track_id not in listened_tracks and scores[track_idx] >= min_similarity:
                recommendations.append((track_id, scores[track_idx]))
                if len(recommendations) >= top_n:
                    break

        if len(recommendations) < top_n:
            popular = self.get_popular_tracks(top_n - len(recommendations))
            recommendations.extend(popular)

        return recommendations[:top_n]

    def get_popular_tracks(self, top_n=10):
        track_counts = defaultdict(int)
        for user_tracks in self.user_track_interactions.values():
            for track, weight in user_tracks.items():
                track_counts[track] += weight

        popular = sorted(track_counts.items(), key=lambda x: x[1], reverse=True)
        return [(track, score) for track, score in popular[:top_n]]

    def recommend_users(self, user_id, top_n=5, include_subscriptions=True, min_similarity=0.1):
        if user_id not in self.user_index:
            return []

        user_idx = self.user_index[user_id]
        similar_users = []

        for other_user_idx in np.argsort(self.user_similarity[user_idx])[::-1]:
            other_user_id = self.reverse_user_index[other_user_idx]
            if other_user_idx == user_idx:
                continue

            similarity = self.user_similarity[user_idx][other_user_idx]
            if similarity >= min_similarity:
                is_subscribed = other_user_id in self.user_user_subscriptions.get(user_id, set())
                similar_users.append({
                    'user_id': other_user_id,
                    'similarity': similarity,
                    'is_subscribed': is_subscribed
                })
                if len(similar_users) >= top_n:
                    break

        if include_subscriptions:
            subscriptions = self.user_user_subscriptions.get(user_id, set())
            for sub_user_id in subscriptions:
                if sub_user_id in self.user_index and not any(u['user_id'] == sub_user_id for u in similar_users):
                    similar_users.append({
                        'user_id': sub_user_id,
                        'similarity': 1.0,
                        'is_subscribed': True
                    })

        similar_users.sort(key=lambda x: x['similarity'], reverse=True)
        return similar_users[:top_n]

    def save_model(self, path):
        with open(path, 'wb') as f:
            pickle.dump({
                'user_track_interactions': dict(self.user_track_interactions),
                'user_user_subscriptions': dict(self.user_user_subscriptions),
                'track_similarity': self.track_similarity,
                'user_similarity': self.user_similarity,
                'track_index': self.track_index,
                'user_index': self.user_index,
                'reverse_track_index': self.reverse_track_index,
                'reverse_user_index': self.reverse_user_index
            }, f)

    def load_model(self, path):
        with open(path, 'rb') as f:
            data = pickle.load(f)

        self.user_track_interactions = defaultdict(dict, data['user_track_interactions'])
        self.user_user_subscriptions = defaultdict(set, {k: set(v) for k, v in data['user_user_subscriptions'].items()})
        self.track_similarity = data['track_similarity']
        self.user_similarity = data['user_similarity']
        self.track_index = data['track_index']
        self.user_index = data['user_index']
        self.reverse_track_index = data['reverse_track_index']
        self.reverse_user_index = data['reverse_user_index']

    def get_user_history(self, user_id):
        if user_id not in self.user_track_interactions:
            return []

        tracks = sorted(self.user_track_interactions[user_id].items(),
                        key=lambda x: x[1], reverse=True)
        return tracks

    def get_user_subscriptions(self, user_id):
        return list(self.user_user_subscriptions.get(user_id, set()))