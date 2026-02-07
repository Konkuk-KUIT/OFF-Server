-- 테스트용 시드 데이터: Member가 없으면 삽입
INSERT INTO member (name, email, password, phone_number, nickname, self_introduction, birth, profile_image, project_count_type, is_working, created_at, updated_at)
SELECT '테스트유저', 'test@example.com', 'password123', '010-1234-5678', '백엔드리드', 'RDS 연결 테스트용 계정입니다.', '2000-01-01', 'https://example.com/profile.png', 'ZERO', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'test@example.com');