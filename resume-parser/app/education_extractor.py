
import re

EDUCATION_HEADINGS = [
    "education",
    "academic background",
    "academic history",
    "educational background"
]

NEXT_SECTION_HEADINGS = [
    "experience",
    "work experience",
    "professional experience",
    "employment history",
    "skills",
    "technical skills",
    "projects",
    "certifications",
    "awards",
    "publications",
    "volunteer experience"
]

DEGREE_PATTERNS = [
    r"\bassociate(?:'s)?\b",
    r"\bbachelor(?:'s)?\b",
    r"\bmaster(?:'s)?\b",
    r"\bph\.?\s*d\.?\b",
    r"\bdoctorate\b",
    r"\bhigh school diploma\b",
    r"\bged\b"
]


def extract_education(text: str) -> dict:
    # Split string whenever a line breaks, remove whitespace from beginning and end, and remove empty lines from list
    lines = [line.strip() for line in text.splitlines() if line.strip()] # Split string whenever a line breaks

    education_lines = []
    inside_education = False

    for line in lines:
        normalized_line = line.lower().strip(" :|-")

        if normalized_line in EDUCATION_HEADINGS:
            inside_education = True
            continue # skip the rest of the loop if education section is found

        # Check if another section is reached
        if inside_education:
            if normalized_line in NEXT_SECTION_HEADINGS:
                break #stop reading lines

            education_lines.append(line) # Add current lines to list

    education_text = "\n".join(education_lines) # list to string

    degrees = []
    for pattern in DEGREE_PATTERNS:
        matches = re.findall(pattern, education_text, re.IGNORECASE)
        degrees.extend(matches)

    graduation_years = re.findall(
        r"\b(?:19|20)\d{2}\b",
        education_text
    )

    return {
        "education_section": education_text,
        "degrees": list(set(degrees)), # Using set to remove duplicate values
        "graduation_years": sorted(set(graduation_years))
    }
