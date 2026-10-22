ALTER TABLE affiliations ADD COLUMN enrollment_year INTEGER;

-- текущий учебный год начинается в сентябре
UPDATE affiliations
SET enrollment_year = (EXTRACT(YEAR FROM now())::int
                       - CASE WHEN EXTRACT(MONTH FROM now()) < 9 THEN 1 ELSE 0 END)
                          - course + 1
WHERE course IS NOT NULL;

ALTER TABLE affiliations DROP COLUMN course;
ALTER TABLE affiliations ADD CONSTRAINT affiliations_enrollment_year_check
    CHECK (enrollment_year BETWEEN 1950 AND 2100);
