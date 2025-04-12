import os
from uuid import uuid4

from fastapi import FastAPI, Header, UploadFile, File

from genre.predict import predict

app = FastAPI()

@app.get("/suggest/v1/tracks")
def greet(user_id: Header("X-User-Id")):
    return {"greeting": f"Hello, {name}!"}

@app.get("/suggest/v1/friends")
def greet(name: str):
    return {"greeting": f"Hello, {name}!"}

@app.post("/suggest/v1/predict-genre")
async def predict_genre(file: UploadFile = File(...)):
    fileData = await file.read()
    filename = f'{uuid4()}.mp3'
    with open(filename, 'wb') as f:
        f.write(fileData)
    result = {"genre": predict(filename)}
    os.remove(filename)
    return result
