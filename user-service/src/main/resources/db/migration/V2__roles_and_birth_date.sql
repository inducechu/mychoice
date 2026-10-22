-- 1. Роль: добавляем MODERATOR
ALTER TABLE user_profiles DROP CONSTRAINT IF EXISTS user_profiles_role_check;
ALTER TABLE user_profiles ADD CONSTRAINT user_profiles_role_check
    CHECK (role IN ('ABITURIENT', 'STUDENT', 'TEACHER', 'MODERATOR', 'ADMIN'));
CREATE INDEX IF NOT EXISTS idx_user_profiles_role ON user_profiles (role);
