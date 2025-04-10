import librosa.feature
import pandas
import torchaudio
from sklearn import preprocessing
from sklearn.preprocessing import StandardScaler
from torch import nn
import torch
from torch.utils.data import DataLoader, Dataset
from torchaudio.transforms import MFCC

device = torch.accelerator.current_accelerator().type if torch.accelerator.is_available() else "cpu"
global mapping


class FeaturesDataset(Dataset):
    def __init__(self):
        global mapping

        # Загрузка данных из CSV
        self.df = pandas.read_csv("features_30_sec.csv")
        self.df.drop(labels=['filename', 'length', 'chroma_stft_var', 'rms_var', 'rms_mean', 'spectral_centroid_var',
                             'spectral_bandwidth_var', 'spectral_bandwidth_mean', 'chroma_stft_mean',
                             'rolloff_mean', 'rolloff_var', 'zero_crossing_rate_mean', 'zero_crossing_rate_var',
                             'harmony_mean', 'harmony_var', 'perceptr_var', 'perceptr_mean'], axis=1, inplace=True)
        # Кодируем метки
        le = preprocessing.LabelEncoder()
        le.fit(self.df['label'])
        mapping = dict(zip(le.classes_, range(len(le.classes_))))
        self.df['label'] = le.transform(self.df['label'])

        # Стандартизируем признаки
        scaler = StandardScaler()
        self.X = scaler.fit_transform(self.df.drop(columns=['label']).values).astype('float32')
        self.y = self.df['label'].values.astype('int64')

    def __len__(self):
        return len(self.df)

    def __getitem__(self, idx):
        return torch.tensor(self.X[idx]), torch.tensor(self.y[idx])


# Модель для классификации
class NeuralNetwork(nn.Module):
    def __init__(self, input_size, num_classes):
        super().__init__()
        self.linear_relu_stack = nn.Sequential(
            nn.Linear(input_size, 1024),
            nn.ReLU(),
            nn.Dropout(0.3),
            nn.Linear(1024, 512),
            nn.ReLU(),
            nn.Dropout(0.3),
            nn.Linear(512, num_classes)
        )

    def forward(self, x):
        return self.linear_relu_stack(x)


# Подготовка данных для тренировки и теста
input_size = FeaturesDataset()[0][0].shape[0]
num_classes = len(set(FeaturesDataset().y))

dataset = FeaturesDataset()
train_size = int(0.8 * len(dataset))
test_size = len(dataset) - train_size
train_dataset, test_dataset = torch.utils.data.random_split(dataset, [train_size, test_size])
train_dataloader = DataLoader(train_dataset, batch_size=64, shuffle=True)
test_dataloader = DataLoader(test_dataset, batch_size=64)

# Инициализация модели
model = NeuralNetwork(input_size, num_classes).to(device)
loss_fn = nn.CrossEntropyLoss()
optimizer = torch.optim.Adam(model.parameters(), lr=1e-3)


# Функция для тренировки
def train(dataloader, model, loss_fn, optimizer):
    model.train()
    for batch, (X, y) in enumerate(dataloader):
        X, y = X.to(device), y.to(device)

        pred = model(X)
        loss = loss_fn(pred, y)

        optimizer.zero_grad()
        loss.backward()
        optimizer.step()

        if batch % 100 == 0:
            print(f"loss: {loss.item():>7f}  [{batch * len(X):>5d}/{len(dataloader.dataset):>5d}]")


# Функция для тестирования
def test(dataloader, model, loss_fn):
    model.eval()
    test_loss, correct = 0, 0
    with torch.no_grad():
        for X, y in dataloader:
            X, y = X.to(device), y.to(device)
            pred = model(X)
            test_loss += loss_fn(pred, y).item()
            correct += (pred.argmax(1) == y).sum().item()

    test_loss /= len(dataloader)
    accuracy = correct / len(dataloader.dataset)
    print(f"Test Accuracy: {100 * accuracy:.1f}%, Avg loss: {test_loss:.4f}")


# Тренировка модели
epochs = 50
for t in range(epochs):
    print(f"Epoch {t + 1}\n-------------------------------")
    train(train_dataloader, model, loss_fn, optimizer)
    test(test_dataloader, model, loss_fn)
print("Done!")


# Функция извлечения признаков из аудио
def extract_features(file_path):
    waveform, sample_rate = torchaudio.load(file_path)
    rosa, sr = librosa.load(file_path)

    if waveform.size(0) == 1:
        waveform = waveform.repeat(2, 1)

    features = {}

    # Извлекаем спектральный центроид
    spectral_centroid = torchaudio.transforms.SpectralCentroid(sample_rate)(waveform)
    features['spectral_centroid_mean'], _ = calc_stat_features(spectral_centroid)

    # Извлекаем MFCC
    mfcc_transform = MFCC(sample_rate=sample_rate, n_mfcc=20)
    mfcc = mfcc_transform(waveform)

    for i in range(1, 21):
        mfcc_mean, mfcc_var = calc_stat_features(mfcc[0][0][i - 1])
        features[f'mfcc{i}_mean'] = mfcc_mean
        features[f'mfcc{i}_var'] = mfcc_var

    # Извлекаем темп
    tempo = librosa.feature.tempo(y=rosa, sr=sr)
    features['tempo'] = tempo.mean().item()

    return features


def calc_stat_features(data):
    mean = data.mean().item()
    var = data.var().item()
    return mean, var

features_order = ["spectral_centroid_mean","tempo","mfcc1_mean","mfcc1_var","mfcc2_mean","mfcc2_var","mfcc3_mean","mfcc3_var","mfcc4_mean","mfcc4_var","mfcc5_mean","mfcc5_var","mfcc6_mean","mfcc6_var","mfcc7_mean","mfcc7_var","mfcc8_mean","mfcc8_var","mfcc9_mean","mfcc9_var","mfcc10_mean","mfcc10_var","mfcc11_mean","mfcc11_var","mfcc12_mean","mfcc12_var","mfcc13_mean","mfcc13_var","mfcc14_mean","mfcc14_var","mfcc15_mean","mfcc15_var","mfcc16_mean","mfcc16_var","mfcc17_mean","mfcc17_var","mfcc18_mean","mfcc18_var","mfcc19_mean","mfcc19_var","mfcc20_mean","mfcc20_var"]

# Подготовка входных данных для предсказания
def prepare_input(features):
    feature_values = [features[key] for key in features_order]
    input_tensor = nn.functional.normalize(torch.tensor(feature_values, dtype=torch.float32).unsqueeze(0))
    return input_tensor


# Пример предсказания для нового аудио
file = "./test2.mp3"
input_tensor = prepare_input(extract_features(file))

# Предсказание метки
model.eval()
with torch.no_grad():
    output = model(input_tensor.to(device))
    predicted_label = torch.argmax(output, dim=1)
    print(mapping)
    print(predicted_label.item())
