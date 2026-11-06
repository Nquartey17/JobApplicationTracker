from io import BytesIO
from pypdf import PdfReader
from docx import Document

# Getting file contents as bytes since PDF and DOCX contain binary data, formatting, and other document structures
def extract_text(file_bytes: bytes, filename: str) -> str:

    # Lower case, split filename at period from left to right ex: ["myresume", "pdf"] once (maxsplit:1)
    # Then retrieve the last item in the list [-1]
    extension = filename.lower().rsplit(".", 1)[-1]

    if extension == "pdf":
        # BytesIo allows python to read file contents directly from memory
        pdf = PdfReader(BytesIO(file_bytes))
        text = ""

        for page in pdf.pages:
            text += page.extract_text() or ""
            # PDFs can sometimes return None if no text can be extracted. using "" in case of that

        return text

    elif extension == "docx":

        document = Document(BytesIO(file_bytes))

        paragraphs = [ paragraph.text for paragraph in document.paragraphs ]

        return "\n".join(paragraphs)

    else:
        raise ValueError("Unsupported file format")