from fastapi import FastAPI

app = FastAPI(
    title="Resume Parser API",
    description="API for extracting information from resumes",
    version="1.0.0"
)


@app.get("/")
def home():
    return {"message": "Resume Parser API is running"}