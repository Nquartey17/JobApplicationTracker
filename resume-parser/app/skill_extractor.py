import re

# IMPORTANT: starting with a dictionary-based skill extraction since it's beginner-friendly.
# I can use NLP techniques to identify skills that aren't in dictionary
# Skills categories will be comp sci related for now, will expand upon later if needed
SKILL_CATEGORIES = {
    "programming_languages": [
        "Java", "Python", "JavaScript", "C++", "PHP", "SQL"
    ],

    "frameworks": [
        "Spring Boot", "React", "Flask", "Node.js", "FastAPI"
    ],

    "databases": [
        "PostgreSQL", "MySQL", "MongoDB"
    ],

    "cloud": [
        "AWS", "Microsoft Azure", "Docker", "Terraform"
    ]
}

def extract_skills(text: str) -> dict:

    extracted_skills = {}

    for category, skills in SKILL_CATEGORIES.items():

        matches = []

        for skill in skills:

            # re.escape(skill) protects special characters in skill names
            pattern = r"(?<!\w)" + re.escape(skill) + r"(?!\w)"

            if re.search(pattern, text, re.IGNORECASE):
                matches.append(skill)

        extracted_skills[category] = matches

    return extracted_skills