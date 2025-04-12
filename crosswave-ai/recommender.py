import torch
import torch.nn as nn
import torch.optim as optim
import numpy as np
from collections import defaultdict
from sklearn.model_selection import train_test_split


class BinaryRecommender(nn.Module):
    def __init__(self, num_users, num_tracks, embedding_dim=32):
        super(BinaryRecommender, self).__init__()
        self.num_users = num_users
        self.num_tracks = num_tracks

        # Эмбеддинги
        self.user_emb = nn.Embedding(num_users, embedding_dim)
        self.track_emb = nn.Embedding(num_tracks, embedding_dim)

        # Модель для предсказания взаимодействий
        self.interaction_model = nn.Sequential(
            nn.Linear(embedding_dim * 2, 64),
            nn.ReLU(),
            nn.Linear(64, 1),
            nn.Sigmoid()
        )

        # Хранение данных
        self.user_track_pairs = set()  # Множество пар (user, track)
        self.user_user_connections = defaultdict(set)  # Социальные связи

    def forward(self, user, track):
        # Получаем эмбеддинги
        u = self.user_emb(user)
        t = self.track_emb(track)

        # Объединяем и предсказываем вероятность взаимодействия
        x = torch.cat([u, t], dim=1)
        return self.interaction_model(x).squeeze()

    def add_interaction(self, user_id, track_id):
        """Добавляет факт взаимодействия пользователя с треком"""
        self.user_track_pairs.add((user_id, track_id))

    def add_connection(self, user_id, followed_user_id):
        """Добавляет социальную связь"""
        self.user_user_connections[user_id].add(followed_user_id)

    def prepare_data(self, neg_samples=1):
        """Подготавливает данные с негативными примерами"""
        positives = list(self.user_track_pairs)

        # Создаем негативные примеры (треки, с которыми пользователь не взаимодействовал)
        negatives = []
        all_tracks = set(range(self.num_tracks))

        for user, track in positives:
            # Треки, с которыми пользователь уже взаимодействовал
            user_tracks = {t for (u, t) in self.user_track_pairs if u == user}

            # Доступные для негативных примеров треки
            available_tracks = list(all_tracks - user_tracks)

            # Добавляем негативные примеры
            if available_tracks:
                for _ in range(neg_samples):
                    neg_track = np.random.choice(available_tracks)
                    negatives.append((user, neg_track))

        # Объединяем положительные и отрицательные примеры
        all_pairs = positives + negatives
        labels = [1] * len(positives) + [0] * len(negatives)

        return torch.LongTensor(all_pairs), torch.FloatTensor(labels)

    def train_model(self, epochs=10, lr=0.01, batch_size=64):
        """Обучает модель"""
        pairs, labels = self.prepare_data()

        # Разделяем на обучающую и тестовую выборки
        X_train, X_test, y_train, y_test = train_test_split(
            pairs.numpy(), labels.numpy(), test_size=0.2, random_state=42
        )

        X_train = torch.LongTensor(X_train)
        X_test = torch.LongTensor(X_test)
        y_train = torch.FloatTensor(y_train)
        y_test = torch.FloatTensor(y_test)

        criterion = nn.BCELoss()
        optimizer = optim.Adam(self.parameters(), lr=lr)

        for epoch in range(epochs):
            # Перемешиваем данные
            indices = torch.randperm(X_train.size(0))

            for i in range(0, X_train.size(0), batch_size):
                batch_indices = indices[i:i + batch_size]
                batch_X = X_train[batch_indices]
                batch_y = y_train[batch_indices]

                users = batch_X[:, 0]
                tracks = batch_X[:, 1]

                optimizer.zero_grad()
                outputs = self(users, tracks)
                loss = criterion(outputs, batch_y)
                loss.backward()
                optimizer.step()

            # Оценка на тестовой выборке
            with torch.no_grad():
                test_users = X_test[:, 0]
                test_tracks = X_test[:, 1]
                test_outputs = self(test_users, test_tracks)
                test_loss = criterion(test_outputs, y_test)
                test_accuracy = ((test_outputs > 0.5).float() == y_test).float().mean()

            print(
                f'Epoch {epoch + 1}, Loss: {loss.item():.4f}, Test Loss: {test_loss.item():.4f}, Test Acc: {test_accuracy.item():.4f}')

    def recommend_tracks(self, user_id, k=5):
        """Рекомендует треки для пользователя"""
        self.eval()
        user = torch.LongTensor([user_id])
        all_tracks = torch.arange(self.num_tracks)

        # Предсказываем вероятность взаимодействия для всех треков
        with torch.no_grad():
            scores = self(user.repeat(self.num_tracks), all_tracks)

        # Сортируем по убыванию вероятности
        _, rec_tracks = torch.topk(scores, k + len([t for (u, t) in self.user_track_pairs if u == user_id]))

        # Исключаем уже прослушанные треки
        listened = {t for (u, t) in self.user_track_pairs if u == user_id}
        recommendations = [t.item() for t in rec_tracks if t.item() not in listened]

        return recommendations[:k]

    def similar_users(self, user_id, k=5):
        """Находит пользователей с похожими вкусами"""
        self.eval()
        with torch.no_grad():
            # Получаем эмбеддинг пользователя
            user_vec = self.user_emb(torch.LongTensor([user_id]))

            # Получаем эмбеддинги всех пользователей
            all_users = torch.arange(self.num_users)
            all_vecs = self.user_emb(all_users)

            # Вычисляем косинусную схожесть
            sim = torch.cosine_similarity(user_vec, all_vecs, dim=1)

            # Сортируем по убыванию схожести
            _, similar = torch.topk(sim, k + 1)  # +1 чтобы исключить самого пользователя

        # Исключаем самого пользователя и уже подписанных
        followed = self.user_user_connections.get(user_id, set())
        recommendations = [u.item() for u in similar
                           if u.item() != user_id and u.item() not in followed]

        return recommendations[:k]

    def recommend_users(self, user_id, k=5):
        """Рекомендует пользователей для подписки"""
        # Находим пользователей с похожими вкусами
        similar = self.similar_users(user_id, k * 2)

        # Добавляем пользователей, на которых подписаны похожие
        recommended = set()
        for sim_user in similar:
            for followed in self.user_user_connections.get(sim_user.item(), set()):
                if followed != user_id and followed not in self.user_user_connections.get(user_id, set()):
                    recommended.add(followed)
                    if len(recommended) >= k:
                        return list(recommended)[:k]

        # Если не набрали достаточно через соц. связи, добавляем просто похожих
        return [u.item() for u in similar[:k]]