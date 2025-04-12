FROM python:3.10-slim
WORKDIR /app

COPY requirements.txt .

RUN apt-get update && apt-get install -y --no-install-recommends \
    ffmpeg \
    libavcodec-extra \
    && rm -rf /var/lib/apt/lists/*

RUN pip install torch torchvision torchaudio --extra-index-url https://download.pytorch.org/whl/cpu
RUN pip install uvicorn[standard] python-multipart PySoundFile soundfile
RUN pip install --no-cache-dir -r requirements.txt

COPY ./src .

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8080"]