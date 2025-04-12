import os
from uuid import uuid4

import py_eureka_client.eureka_client as eureka_client
from fastapi import FastAPI, Header, UploadFile, File

from predict import predict

eureka_client.init(eureka_server=os.environ["EUREKA_SERVER"],
                   app_name="crosswave-ai",
                   instance_port=8080)

app = FastAPI()

@app.get("/suggest/v1/tracks")
def recommend_tracks(user_id: Header("X-User-Id")):
    return {"greeting": f"Hello, {user_id}!"}

@app.get("/suggest/v1/friends")
def recommend_friends(user_id: Header("X-User-Id")):
    return {"greeting": f"Hello, {user_id}!"}

@app.post("/suggest/v1/predict-genre")
async def predict_genre(file: UploadFile = File(...)):
    fileData = await file.read()
    filename = f'{uuid4()}.mp3'
    with open(filename, 'wb') as f:
        f.write(fileData)
    result = {"genre": predict(filename)}
    os.remove(filename)
    return result
