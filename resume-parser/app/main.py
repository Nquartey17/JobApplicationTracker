from fastapi import FastAPI, UploadFile, File, HTTPException

from app.parser import extract_text
from app.skill_extractor import extract_skills
from app.education_extractor import extract_education
from app.experience_extractor import extract_work_experience

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
        skills = extract_skills(text)
        education = extract_education(text)
        experience = extract_work_experience(text)

        return {
            "filename": file.filename,
            "extracted_data": {
                "skills": skills,
                "education": education,
                "experience": experience
            }
        }

    except ValueError as e:
        raise HTTPException( status_code=400, detail=str(e))