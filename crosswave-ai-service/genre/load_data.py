import os
import numpy as np
import torch
import torchaudio
from torch.utils.data import Dataset, DataLoader


class MusicGenreDataset(Dataset):
    def __init__(self, root_dir, transform=None, target_sample_rate=22050, num_samples=22050 * 30):
        self.root_dir = root_dir
        self.transform = transform
        self.target_sample_rate = target_sample_rate
        self.num_samples = num_samples
        self.classes = sorted(os.listdir(root_dir))
        self.filepaths = []

        for genre in self.classes:
            genre_dir = os.path.join(root_dir, genre)
            for filename in os.listdir(genre_dir):
                if filename.endswith('.mp3'):
                    self.filepaths.append((os.path.join(genre_dir, filename), self.classes.index(genre)))

    def __len__(self):
        return len(self.filepaths)

    def __getitem__(self, idx):
        filepath, label = self.filepaths[idx]
        waveform, sample_rate = torchaudio.load(filepath)

        if sample_rate != self.target_sample_rate:
            resampler = torchaudio.transforms.Resample(sample_rate, self.target_sample_rate)
            waveform = resampler(waveform)

        if waveform.shape[1] > self.num_samples:
            waveform = waveform[:, :self.num_samples]
        elif waveform.shape[1] < self.num_samples:
            pad_length = self.num_samples - waveform.shape[1]
            waveform = torch.nn.functional.pad(waveform, (0, pad_length))

        if self.transform:
            waveform = self.transform(waveform)

        return waveform, label


transform = torchaudio.transforms.MelSpectrogram(
    sample_rate=22050,
    n_fft=1024,
    hop_length=512,
    n_mels=64
)

dataset = MusicGenreDataset("mp3/mp3", transform=transform)
train_size = int(0.8 * len(dataset))
val_size = len(dataset) - train_size
train_dataset, val_dataset = torch.utils.data.random_split(dataset, [train_size, val_size])

train_loader = DataLoader(train_dataset, batch_size=32, shuffle=True)
val_loader = DataLoader(val_dataset, batch_size=32, shuffle=False)