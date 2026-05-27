INSERT INTO registration_codes (code, role, is_used) 
VALUES 
('ADMIN-KEY-2024', 'ADMIN', false),
('EDITOR-KEY-2024', 'EDITOR', false),
('VIEWER-KEY-2024', 'VIEWER', false)
ON CONFLICT (code) DO NOTHING;
