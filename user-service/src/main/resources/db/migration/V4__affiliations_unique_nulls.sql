DO $$
DECLARE c text;
BEGIN
SELECT conname INTO c FROM pg_constraint
WHERE conrelid = 'affiliations'::regclass AND contype = 'u';
IF c IS NOT NULL THEN
        EXECUTE format('ALTER TABLE affiliations DROP CONSTRAINT %I', c);
END IF;
END $$;

ALTER TABLE affiliations ADD CONSTRAINT uq_affiliations_profile_university
    UNIQUE NULLS NOT DISTINCT (user_profile_id, university_id, faculty_id, program_id);
