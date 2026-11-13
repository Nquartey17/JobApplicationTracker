import re

# IMPORTANT: starting with a dictionary-based skill extraction since it's beginner-friendly.
# I can use NLP techniques to identify skills that aren't in dictionary
# Skills  will be comp sci related for now, will expand upon later if needed
SKILLS = [
    "Java", "Python", "JavaScript", "C++", "PHP", "SQL", "Spring Boot", "React", "Flask", "Node.js", "FastAPI",
    "PostgreSQL", "MySQL", "MongoDB", "AWS", "Microsoft Azure", "Docker", "Terraform"
]

def extract_skills(text: str) -> list[str]:

    matches = []

    for skill in SKILLS:

        # re.escape(skill) protects special characters in skill names
        pattern = r"(?<!\w)" + re.escape(skill) + r"(?!\w)"

        if re.search(pattern, text, re.IGNORECASE):
            matches.append(skill)

    return matches