import re

EXPERIENCE_HEADINGS = [
    "experience",
    "work experience",
    "professional experience",
    "employment history",
    "work history",
    "career history",
    "professional background"
]

NEXT_SECTION_HEADINGS = [
    "education",
    "academic background",
    "academic history",
    "skills",
    "technical skills",
    "projects",
    "certifications",
    "awards",
    "publications",
    "volunteer experience",
    "references"
]

def extract_work_experience(text: str) -> dict:

    # Normalize whitespace within the text
    normalized_text = re.sub(r'\s+', ' ', text)

    # Find the beginning of the experience section
    start_pattern = (
        r'\b(?:work\s+experience|professional\s+experience|'
        r'employment\s+history|work\s+history|experience)\b'
    )

    start_match = re.search(
        start_pattern,
        normalized_text,
        re.IGNORECASE
    )

    if not start_match:
        return {"experience_section": ""}

    # Find the beginning of the next section
    end_pattern = (
        r'\b(?:education|academic background|skills|technical skills|'
        r'projects|certifications|awards|publications|volunteer experience)\b'
    )

    remaining_text = normalized_text[start_match.end():]

    end_match = re.search(
        end_pattern,
        remaining_text,
        re.IGNORECASE
    )

    if end_match:
        experience_text = remaining_text[:end_match.start()]
    else:
        experience_text = remaining_text

    return {
        "experience_section": experience_text.strip()
    }