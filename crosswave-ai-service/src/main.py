import os
from uuid import uuid4

import py_eureka_client.eureka_client as eureka_client
from fastapi import FastAPI, Header, UploadFile, File

from genre.predict import predict

your_rest_server_port = 9090
# The flowing code will register your server to eureka server and also start to send heartbeat every 30 seconds
eureka_client.init(eureka_server=os.environ["EUREKA_SERVER"],
                   app_name="your_app_name",
                   instance_port=your_rest_server_port)

app = FastAPI()

@app.get("/suggest/v1/tracks")
def recommend_tracks(user_id: Header("X-User-Id")):
    return {"greeting": f"Hello, {name}!"}

@app.get("/suggest/v1/friends")
def recommend_friends(user_id: Header("X-User-Id")):
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
