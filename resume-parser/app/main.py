from fastapi import FastAPI, UploadFile, File, HTTPException, Request

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
    print("Filename:", file.filename)
    print("Content type:", file.content_type)

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



@app.post("/debug-multipart")
async def debug_multipart(request: Request):

    raw_body = await request.body()

    print("DEBUG All request headers:", dict(request.headers))

    print("DEBUG All request headers:",
          dict(request.headers))

    print("DEBUG Content-Type:",
          request.headers.get("content-type"))

    print("DEBUG Content-Length:",
          request.headers.get("content-length"))

    print("DEBUG Raw body size:",
          len(raw_body))

    print("DEBUG Raw body beginning:",
          repr(raw_body[:300]))

    form = await request.form()

    print("DEBUG Received form fields:",
          list(form.keys()))

    uploaded_file = form.get("file")

    print("DEBUG Filename:",
          getattr(uploaded_file, "filename", None))

    return {
        "received_fields": list(form.keys()),
        "filename": getattr(uploaded_file, "filename", None)
    }
