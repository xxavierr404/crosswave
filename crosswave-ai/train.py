from os.path import exists

import librosa
import numpy as np
import torch
from pandas import DataFrame
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import MultiLabelBinarizer
from torch import nn
from torch.utils.data import Dataset, DataLoader

from jamendo.scripts.commons import read_file

# === Config ===
SAMPLE_RATE = 22050
N_MELS = 128
DURATION = 30
AUDIO_LENGTH = SAMPLE_RATE * DURATION
DEVICE = torch.device("cuda" if torch.cuda.is_available() else "cpu")

# === Feature extraction ===
def extract_features(file_path, max_length=AUDIO_LENGTH):
    try:
        y, sr = librosa.load(file_path, sr=SAMPLE_RATE)
        if len(y) < max_length:
            y = np.pad(y, (0, max_length - len(y)))
        else:
            y = y[:max_length]
        mel = librosa.feature.melspectrogram(y=y, sr=sr, n_mels=N_MELS)
        mel_db = librosa.power_to_db(mel, ref=np.max)
        return mel_db  # (128, time)
    except:
        return np.array([])

# === Dataset ===
class JamendoDataset(Dataset):
    def __init__(self, dataframe, genre_binarizer, mood_binarizer):
        self.data = dataframe.reset_index(drop=True)
        self.genre_binarizer = genre_binarizer
        self.mood_binarizer = mood_binarizer

    def __len__(self):
        return len(self.data)

    def __getitem__(self, idx):
        row = self.data.iloc[idx]
        mel_spec = extract_features(row["path"])
        mel_tensor = torch.tensor(mel_spec, dtype=torch.float32).unsqueeze(0)  # (1, 128, time)

        genre_vec = self.genre_binarizer.transform([row["genre"]])[0]
        mood_vec = self.mood_binarizer.transform([row["mood/theme"]])[0]

        return mel_tensor, torch.tensor(genre_vec, dtype=torch.float32), torch.tensor(mood_vec, dtype=torch.float32)

# === CNN + BiLSTM model ===
class CNNBiLSTMMusicClassifier(nn.Module):
    def __init__(self, num_genres, num_moods):
        super().__init__()
        self.cnn = nn.Sequential(
            nn.Conv2d(1, 32, kernel_size=3, padding=1),
            nn.ReLU(),
            nn.MaxPool2d(kernel_size=(2, 2)),
            nn.Conv2d(32, 64, kernel_size=3, padding=1),
            nn.ReLU(),
            nn.MaxPool2d(kernel_size=(2, 2)),
        )
        self.lstm_input_size = 64 * (N_MELS // 4)
        self.lstm = nn.LSTM(
            input_size=self.lstm_input_size,
            hidden_size=128,
            num_layers=1,
            bidirectional=True,
            batch_first=True
        )
        self.genre_head = nn.Linear(128 * 2, num_genres)
        self.mood_head = nn.Linear(128 * 2, num_moods)

    def forward(self, x):
        x = self.cnn(x)  # (B, 64, H, T)
        x = x.permute(0, 3, 1, 2)  # (B, T, C, H)
        B, T, C, H = x.shape
        x = x.reshape(B, T, C * H)
        lstm_out, _ = self.lstm(x)
        x = lstm_out[:, -1, :]  # последний временной шаг
        return self.genre_head(x), self.mood_head(x)

# === Load annotations ===
df = DataFrame([track for track in read_file("jamendo/data/raw_30s.tsv")[0].values()])
existing_tracks = []

for row in df.iterrows():
    if exists(row[1]["path"]):
        existing_tracks.append(row[1])

df = DataFrame(existing_tracks)

# === Label binarization ===
genre_binarizer = MultiLabelBinarizer()
mood_binarizer = MultiLabelBinarizer()
genre_binarizer.fit(df["genre"])
mood_binarizer.fit(df["mood/theme"])

# === Train/test split ===
train_df, test_df = train_test_split(df, test_size=0.2, random_state=42)
train_dataset = JamendoDataset(train_df, genre_binarizer, mood_binarizer)
test_dataset = JamendoDataset(test_df, genre_binarizer, mood_binarizer)
train_loader = DataLoader(train_dataset, batch_size=8, shuffle=True)
test_loader = DataLoader(test_dataset, batch_size=8)

# === Model init ===
model = CNNBiLSTMMusicClassifier(
    num_genres=len(genre_binarizer.classes_),
    num_moods=len(mood_binarizer.classes_)
).to(DEVICE)

criterion = nn.BCEWithLogitsLoss()
optimizer = torch.optim.Adam(model.parameters(), lr=1e-4)

# === Training ===
for epoch in range(7):
    model.train()
    total_loss = 0
    for x, y_genre, y_mood in train_loader:
        x, y_genre, y_mood = x.to(DEVICE), y_genre.to(DEVICE), y_mood.to(DEVICE)
        optimizer.zero_grad()
        genre_pred, mood_pred = model(x)
        loss = criterion(genre_pred, y_genre) + criterion(mood_pred, y_mood)
        loss.backward()
        optimizer.step()
        total_loss += loss.item()
        print(f'processed, loss: {loss.item()}')
    print(f"Epoch {epoch+1} | Loss: {total_loss:.4f}")

MODEL_SAVE_PATH = "genre_recognizer.pt"

# === Save model ===
torch.save(model.state_dict(), MODEL_SAVE_PATH)
print(f"Model saved to {MODEL_SAVE_PATH}")
