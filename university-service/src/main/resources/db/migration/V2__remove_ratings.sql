DROP INDEX IF EXISTS idx_universities_rating_desc;

ALTER TABLE universities DROP COLUMN IF EXISTS rating;
ALTER TABLE faculties    DROP COLUMN IF EXISTS rating;
ALTER TABLE programs     DROP COLUMN IF EXISTS rating;

DROP TABLE IF EXISTS faculty_stats;
DROP TABLE IF EXISTS university_stats;

-- специалитет в Degree
ALTER TABLE programs DROP CONSTRAINT IF EXISTS programs_degree_check;
ALTER TABLE programs ADD CONSTRAINT programs_degree_check
    CHECK (degree IN ('BACHELOR', 'SPECIALIST', 'MASTER', 'PHD'));
