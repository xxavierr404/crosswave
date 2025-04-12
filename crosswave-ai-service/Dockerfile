FROM python:3.10 AS builder

WORKDIR /app
COPY ./requirements.txt .
RUN pip install --user -r requirements.txt

FROM python:3.10-slim
WORKDIR /app

COPY --from=builder /root/.local/bin /root/.local/bin
COPY ./src/ .

ENV PATH=/root/.local/bin:$PATH

CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]