import json
import os
import random
import requests
import pandas as pd
from tqdm import tqdm

ANNOTATION_PATH = "data/moodtheme/moodtheme_annotations.json"
DOWNLOAD_DIR = "data/audio_subset"
OUTPUT_CSV = "data/track_labels.csv"
NUM_TRACKS = 250

# Маппинг тегов по категориям (можно сузить, если хочешь)
GENRE_TAGS = {"rock", "pop", "hip hop", "electronic", "jazz", "classical", "folk", "metal"}
MOOD_TAGS = {"happy", "sad", "angry", "relaxed", "romantic", "melancholic", "energetic", "calm"}

# Загружаем аннотации
with open(ANNOTATION_PATH, "r") as f:
    data = json.load(f)

filtered = []

# Фильтруем треки с тегами настроения и жанра
for track in data["annotations"]:
    tags = set(track["tags"])
    genres = tags & GENRE_TAGS
    moods = tags & MOOD_TAGS
    if genres and moods:
        genre = list(genres)[0]
        mood = list(moods)[0]
        filtered.append({
            "id": track["track_id"],
            "genre": genre,
            "mood": mood
        })

print(f"🟢 Найдено {len(filtered)} треков с нужными тегами")

# Случайный выбор 250 треков
subset = random.sample(filtered, NUM_TRACKS)

# Скачаем треки
os.makedirs(DOWNLOAD_DIR, exist_ok=True)

def download_mp3(track_id, dest_path):
    url = f"https://datasets.audiocommons.org/jamendo/audio/{track_id}.mp3"
    r = requests.get(url, stream=True)
    if r.status_code == 200:
        with open(dest_path, "wb") as f:
            for chunk in r.iter_content(1024):
                f.write(chunk)
        return True
    return False

print(f"⬇️ Скачиваем {NUM_TRACKS} треков...")
success = 0
rows = []

for track in tqdm(subset):
    path = os.path.join(DOWNLOAD_DIR, f"{track['id']}.mp3")
    if download_mp3(track["id"], path):
        success += 1
        rows.append({
            "file": path,
            "genre": track["genre"],
            "mood": track["mood"]
        })

# Сохраняем таблицу
df = pd.DataFrame(rows)
df.to_csv(OUTPUT_CSV, index=False)

print(f"✅ Скачано {success} треков. Данные сохранены в {OUTPUT_CSV}")
