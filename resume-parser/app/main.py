from fastapi import FastAPI, UploadFile, File, HTTPException

from app.parser import extract_text

#Create REST API app
app = FastAPI(
    title="Resume Parser API",
    version="1.0.0"
)


@app.get("/")
def home():
    return {"message": "Resume Parser API is running"}


# UploadFile -> file uploaded through HTTP request.
# File (...) tells FastAPI that this is a required parameter
@app.post("/parse")
def parse_resume(file: UploadFile = File(...)):

    try:
        file_bytes = file.file.read()

        text = extract_text(file_bytes, file.filename)

        return {
            "filename": file.filename,
            "extracted_text": text
        }

    except ValueError as e:
        raise HTTPException(
            status_code=400,
            detail=str(e)
        )