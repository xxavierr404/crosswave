import torch
import torch.nn.functional as F
import torchaudio

from load_data import transform, dataset
from model_architecture import model, device


def predict_genre(model, audio_path, transform, device, class_names):
    # Загрузка аудио
    waveform, sample_rate = torchaudio.load(audio_path)

    # Ресемплирование если нужно
    if sample_rate != 22050:
        resampler = torchaudio.transforms.Resample(sample_rate, 22050)
        waveform = resampler(waveform)

    # Обрезка или дополнение
    target_length = 22050 * 30  # 30 секунд
    if waveform.shape[1] > target_length:
        waveform = waveform[:, :target_length]
    elif waveform.shape[1] < target_length:
        pad_length = target_length - waveform.shape[1]
        waveform = torch.nn.functional.pad(waveform, (0, pad_length))

    # Применение преобразований
    spectrogram = transform(waveform).unsqueeze(0).to(device)  # Добавляем batch dimension

    # Предсказание
    model.eval()
    with torch.no_grad():
        outputs = model(spectrogram)
        probabilities = F.softmax(outputs, dim=1)
        top_prob, top_class = torch.max(probabilities, 1)

    return class_names[top_class.item()], top_prob.item()


# Загрузка обученной модели
model.load_state_dict(torch.load("best_model_genre.pth"))
model.to(device)

# Пример использования
audio_path = "test2.mp3"
genre, confidence = predict_genre(model, audio_path, transform, device, dataset.classes)
print(f"Predicted genre: {genre} with confidence {confidence:.2f}")