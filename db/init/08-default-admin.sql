-- Insert default admin user
-- Password: Admin@123! (hashed with bcrypt, cost factor 10)
-- Generated with: BCryptPasswordEncoder().encode("Admin@123!")
INSERT INTO identity (uid, email, role_id, verified, created_at, updated_at)
VALUES (
    'admin-default-uid-001',
    'admin@m1.local',
    (SELECT id FROM role WHERE name = 'admin'),
    true,
    now(),
    now()
);

INSERT INTO credential (identity_id, password_hash, created_at, updated_at)
VALUES (
    (SELECT id FROM identity WHERE email = 'admin@m1.local'),
    '$2a$12$9grk1NM4kHI65rWbpfTfveiz6JOus6RAlAR7dTUF1ZoOV9BZ0AL76',
    now(),
    now()
);
