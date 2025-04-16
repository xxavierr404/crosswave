import asyncio
import os
from contextlib import asynccontextmanager

import py_eureka_client.eureka_client as eureka_client
from fastapi import FastAPI, Header, UploadFile, File

from model import retrain, recommend

EUREKA_SERVER_URL = os.environ["EUREKA_SERVER_URL"]
APP_NAME = "crosswave-ai"
INSTANCE_PORT = 8080
MAX_RETRIES = 5
RETRY_DELAY = 5

async def wait_for_eureka():
    for attempt in range(1, MAX_RETRIES + 1):
        try:
            await eureka_client.init_async(
                eureka_server=EUREKA_SERVER_URL,
                app_name=APP_NAME,
                instance_port=INSTANCE_PORT,
                renewal_interval_in_secs=30,
                duration_in_secs=90,
            )
            return True
        except Exception as e:
            if attempt < MAX_RETRIES:
                await asyncio.sleep(RETRY_DELAY)
    return False

async def retrain_cron():
    while True:
        retrain()
        await asyncio.sleep(10)

@asynccontextmanager
async def lifespan(app: FastAPI):
    if not await wait_for_eureka():
        raise RuntimeError("Eureka is unavailable")

    asyncio.create_task(retrain_cron())

    yield
    await eureka_client.stop_async()

app = FastAPI(lifespan=lifespan)

@app.get("/suggest/v1/tracks")
def recommend_tracks(x_user_id: str = Header()):
    return recommend(x_user_id, 5, 5)[1]

@app.get("/suggest/v1/friends")
def recommend_friends(x_user_id: str = Header()):
    return recommend(x_user_id, 5, 5)[0]

@app.post("/suggest/v1/predict-genre")
async def predict_genre(file: UploadFile = File("file")):
    return "unknown"
