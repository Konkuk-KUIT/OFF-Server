-- =====================================================
-- [PostgreSQL 전용] 초기화 및 더미 데이터 삽입 스크립트
-- 트랜잭션 시작 (중간에 실패하면 롤백됨)
-- =====================================================
BEGIN;

-- 1. 초기화 (기존 데이터 삭제)
-- TRUNCATE: 테이블을 비우고, RESTART IDENTITY: ID를 1번부터 다시 시작, CASCADE: 연관된 외래키 데이터도 자동 삭제
TRUNCATE TABLE 
    pay_log, 
    notification, 
    message, 
    chat_room_member, 
    chat_room, 
    to_do, 
    task, 
    partner_application, 
    partner_recruit, 
    project_member, 
    project, 
    portfolio, 
    member 
RESTART IDENTITY CASCADE;


-- =====================================================
-- 2. Member 데이터 삽입 (총 8명)
-- =====================================================
INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at) VALUES
('김기획', 'planner@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '기획왕', '5년차 PM입니다. 앱/웹 서비스 기획 전문.', '1995-03-15', 'https://example.com/planner.png', 'PM', 'THREE_TIMES', true, NOW(), NOW()),
('이기획', 'planner2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '서비스설계자', '스타트업 PM 경력 3년.', '1997-07-20', 'https://example.com/planner2.png', 'PM', 'ONCE', false, NOW(), NOW()),
('박개발', 'dev@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '풀스택개발자', 'Spring Boot + React 풀스택 개발자입니다.', '1996-05-10', 'https://example.com/dev.png', 'DEV', 'TWICE', false, NOW(), NOW()),
('최개발', 'dev2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '백엔드마스터', 'Java/Kotlin 백엔드 전문. MSA 경험 다수.', '1994-11-25', 'https://example.com/dev2.png', 'DEV', 'PLUS_FIVE', true, NOW(), NOW()),
('강개발', 'dev3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '프론트엔드전문', 'React/Vue 프론트엔드 개발자.', '1998-06-18', 'https://example.com/dev3.png', 'DEV', 'ONCE', false, NOW(), NOW()),
('정디자인', 'des@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', 'UI천재', 'Figma/Sketch 기반 UI/UX 디자이너. 앱 디자인 전문.', '1998-02-14', 'https://example.com/des.png', 'DES', 'ONCE', false, NOW(), NOW()),
('윤디자인', 'des2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', 'UX마스터', '사용자 경험 중심의 디자인 전문가.', '1996-12-05', 'https://example.com/des2.png', 'DES', 'TWICE', false, NOW(), NOW()),
('한마케팅', 'mar@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '그로스해커', '퍼포먼스 마케팅 + 브랜딩 전문가.', '1997-09-01', 'https://example.com/mar.png', 'MAR', 'ZERO', false, NOW(), NOW());


-- =====================================================
-- 3. Portfolio 삽입 (ID 자동 조회)
-- =====================================================
INSERT INTO portfolio (link, description, member_id, created_at, updated_at)
SELECT 'https://github.com/parkdev', 'Spring Boot 쇼핑몰 프로젝트', member_id, NOW(), NOW()
FROM member WHERE email = 'dev@example.com';

INSERT INTO portfolio (link, description, member_id, created_at, updated_at)
SELECT 'https://github.com/parkdev/chat-app', 'WebSocket 실시간 채팅 앱', member_id, NOW(), NOW()
FROM member WHERE email = 'dev@example.com';

INSERT INTO portfolio (link, description, member_id, created_at, updated_at)
SELECT 'https://behance.net/jungdesign', 'e-commerce 앱 UI/UX 리디자인', member_id, NOW(), NOW()
FROM member WHERE email = 'des@example.com';

INSERT INTO portfolio (link, description, member_id, created_at, updated_at)
SELECT 'https://github.com/choidev', 'MSA 기반 결제 시스템', member_id, NOW(), NOW()
FROM member WHERE email = 'dev2@example.com';


-- =====================================================
-- 4. Project 삽입
-- =====================================================
-- 펫케어 플랫폼 (IN_PROGRESS)
INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, introduction, creator_id, created_at, updated_at)
SELECT '펫케어 플랫폼',
       '반려동물 건강 관리 및 수의사 상담 매칭 서비스',
       '실시간 채팅 상담, 건강 기록 대시보드, 수의사 예약 시스템 필요',
       8500000, '2026-01-15', '2026-04-15', 'APP', 'IN_PROGRESS',
       '반려동물 보호자와 수의사를 연결하는 종합 펫케어 플랫폼입니다.',
       member_id, NOW(), NOW()
FROM member WHERE email = 'planner@example.com';

-- 스터디 매칭 서비스 (IN_PROGRESS)
INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, introduction, creator_id, created_at, updated_at)
SELECT '스터디 매칭 서비스',
       '개발자/디자이너 스터디 그룹 매칭 웹 서비스',
       '관심 기술 스택 기반 매칭, 스터디룸 관리, 출석 체크 기능',
       5000000, '2026-02-01', '2026-05-01', 'SERVICE', 'IN_PROGRESS',
       '함께 성장하는 개발자들을 위한 스터디 매칭 플랫폼.',
       member_id, NOW(), NOW()
FROM member WHERE email = 'planner2@example.com';

-- 헬스케어 앱 (COMPLETED)
INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, introduction, creator_id, created_at, updated_at)
SELECT '헬스케어 앱',
       '운동 기록 및 식단 관리 모바일 앱',
       '운동 루틴 추천, 칼로리 계산기, 커뮤니티 기능',
       7000000, '2025-09-01', '2025-12-31', 'APP', 'COMPLETED',
       '성공적으로 완료된 헬스케어 프로젝트입니다.',
       member_id, NOW(), NOW()
FROM member WHERE email = 'planner@example.com';


-- =====================================================
-- 5. PartnerRecruit 삽입
-- =====================================================
-- 펫케어 플랫폼
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 2, 3000000, 'DEV', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '펫케어 플랫폼';
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2500000, 'DES', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '펫케어 플랫폼';
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2000000, 'MAR', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '펫케어 플랫폼';

-- 스터디 매칭 서비스
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2500000, 'DEV', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '스터디 매칭 서비스';
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2000000, 'DES', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '스터디 매칭 서비스';

-- 헬스케어 앱
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 0, 2500000, 'DEV', 'CLOSED', project_id, NOW(), NOW() FROM project WHERE name = '헬스케어 앱';
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 0, 2000000, 'DES', 'CLOSED', project_id, NOW(), NOW() FROM project WHERE name = '헬스케어 앱';


-- =====================================================
-- 6. ProjectMember 삽입
-- =====================================================
-- 펫케어 플랫폼 멤버
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'planner@example.com' AND p.name = '펫케어 플랫폼';

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DEV', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'dev@example.com' AND p.name = '펫케어 플랫폼';

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'MAR', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'mar@example.com' AND p.name = '펫케어 플랫폼';

-- 스터디 매칭 서비스 멤버
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'planner2@example.com' AND p.name = '스터디 매칭 서비스';

-- 헬스케어 앱 멤버
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'planner@example.com' AND p.name = '헬스케어 앱';

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DEV', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'dev2@example.com' AND p.name = '헬스케어 앱';

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DES', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'des@example.com' AND p.name = '헬스케어 앱';


-- =====================================================
-- 7. PartnerApplication 삽입
-- =====================================================
-- 강개발 -> 펫케어(DEV) [WAITING]
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT false, 'WAITING', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr JOIN project p ON pr.project_id = p.project_id
WHERE m.email = 'dev3@example.com' AND p.name = '펫케어 플랫폼' AND pr.role = 'DEV';

-- 윤디자인 -> 펫케어(DES) [WAITING]
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT true, 'WAITING', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr JOIN project p ON pr.project_id = p.project_id
WHERE m.email = 'des2@example.com' AND p.name = '펫케어 플랫폼' AND pr.role = 'DES';

-- 한마케팅 -> 펫케어(MAR) [ACCEPT]
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT false, 'ACCEPT', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr JOIN project p ON pr.project_id = p.project_id
WHERE m.email = 'mar@example.com' AND p.name = '펫케어 플랫폼' AND pr.role = 'MAR';


-- =====================================================
-- 8. Task & ToDo 삽입
-- =====================================================
-- API 설계 Task (박개발)
INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT 'API 설계', 'RESTful API 엔드포인트 설계 및 문서화', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p JOIN project_member pm ON p.project_id = pm.project_id JOIN member m ON pm.member_id = m.member_id
WHERE p.name = '펫케어 플랫폼' AND m.email = 'dev@example.com';

-- 화면 설계 Task (김기획)
INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT '화면 설계', '주요 화면 와이어프레임 및 프로토타입 제작', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p JOIN project_member pm ON p.project_id = pm.project_id JOIN member m ON pm.member_id = m.member_id
WHERE p.name = '펫케어 플랫폼' AND m.email = 'planner@example.com';

-- DB 설계 Task (박개발)
INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT 'DB 설계', '데이터베이스 ERD 설계 및 테이블 정의', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p JOIN project_member pm ON p.project_id = pm.project_id JOIN member m ON pm.member_id = m.member_id
WHERE p.name = '펫케어 플랫폼' AND m.email = 'dev@example.com';


-- ToDo 삽입 (Task 이름으로 매칭)
-- API 설계 ToDo
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '회원 API 설계', true, task_id, NOW(), NOW() FROM task WHERE name = 'API 설계';
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '상담 예약 API 설계', true, task_id, NOW(), NOW() FROM task WHERE name = 'API 설계';
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '결제 API 설계', false, task_id, NOW(), NOW() FROM task WHERE name = 'API 설계';
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '채팅 API 설계', false, task_id, NOW(), NOW() FROM task WHERE name = 'API 설계';

-- 화면 설계 ToDo
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '홈 화면 와이어프레임', true, task_id, NOW(), NOW() FROM task WHERE name = '화면 설계';
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '상담 예약 화면 와이어프레임', false, task_id, NOW(), NOW() FROM task WHERE name = '화면 설계';
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '프로토타입 제작', false, task_id, NOW(), NOW() FROM task WHERE name = '화면 설계';

-- DB 설계 ToDo
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT 'ERD 초안 작성', true, task_id, NOW(), NOW() FROM task WHERE name = 'DB 설계';
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '인덱스 전략 수립', false, task_id, NOW(), NOW() FROM task WHERE name = 'DB 설계';


-- =====================================================
-- 9. ChatRoom & Message 삽입
-- =====================================================
-- 프로젝트 채팅방 (펫케어 플랫폼)
INSERT INTO chat_room (chat_type, project_id, created_at, updated_at)
SELECT 'PROJECT', project_id, NOW(), NOW() FROM project WHERE name = '펫케어 플랫폼';

-- 프로젝트 채팅방 멤버 (김기획, 박개발, 한마케팅)
INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr JOIN project p ON cr.project_id = p.project_id JOIN member m ON 1=1
WHERE p.name = '펫케어 플랫폼' AND m.email IN ('planner@example.com', 'dev@example.com', 'mar@example.com');

-- 프로젝트 채팅 메시지
INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT '안녕하세요! 박개발님 반갑습니다.', true, m.member_id, cr.chat_room_id, NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours'
FROM chat_room cr JOIN project p ON cr.project_id = p.project_id JOIN member m ON m.email = 'planner@example.com'
WHERE p.name = '펫케어 플랫폼';

INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT '네, 기획왕님! 잘 부탁드립니다.', true, m.member_id, cr.chat_room_id, NOW() - INTERVAL '1 hour', NOW() - INTERVAL '1 hour'
FROM chat_room cr JOIN project p ON cr.project_id = p.project_id JOIN member m ON m.email = 'dev@example.com'
WHERE p.name = '펫케어 플랫폼';

INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT 'API 설계 작업 진행 상황 어떠신가요?', false, m.member_id, cr.chat_room_id, NOW() - INTERVAL '30 minutes', NOW() - INTERVAL '30 minutes'
FROM chat_room cr JOIN project p ON cr.project_id = p.project_id JOIN member m ON m.email = 'planner@example.com'
WHERE p.name = '펫케어 플랫폼';


-- 개인 채팅방 (CONTACT): 김기획 + 윤디자인
-- 1. 방 생성
INSERT INTO chat_room (chat_type, created_at, updated_at) VALUES ('CONTACT', NOW(), NOW());

-- 2. 멤버 연결 (방금 만든 CONTACT 방에 연결 - ID 역순 정렬로 최신 방 찾기)
INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, member m
WHERE cr.chat_type = 'CONTACT' AND m.email = 'planner@example.com'
ORDER BY cr.chat_room_id DESC LIMIT 1;

INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, member m
WHERE cr.chat_type = 'CONTACT' AND m.email = 'des2@example.com'
ORDER BY cr.chat_room_id DESC LIMIT 1;


-- =====================================================
-- 10. Notification 삽입
-- =====================================================
INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '강개발님이 DEV 역할로 지원했어요! (예상 비용: 3,000,000원)', '/payments/prepare/1', false, 'APPLICATION', member_id, NOW(), NOW()
FROM member WHERE email = 'planner@example.com';

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '펫케어 플랫폼 프로젝트에서 파트너 제안이 도착했어요!', '/invitations/2', false, 'INVITE', member_id, NOW(), NOW()
FROM member WHERE email = 'des2@example.com';

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '결제 성공! 파트너 매칭이 되었어요.', '/projects/1', true, 'PAY', member_id, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'
FROM member WHERE email = 'dev@example.com';

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '헬스케어 앱 프로젝트가 완료되었습니다!', '/projects/3', true, 'PROJECT_COMPLETE', member_id, NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days'
FROM member WHERE email = 'dev2@example.com';

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '새로운 메시지가 도착했습니다.', '/chat/rooms/1', false, 'CHAT', member_id, NOW() - INTERVAL '10 minutes', NOW() - INTERVAL '10 minutes'
FROM member WHERE email = 'dev@example.com';


-- =====================================================
-- 11. PayLog 삽입
-- =====================================================
INSERT INTO pay_log (order_id, amount, status, payment_key, member_id, application_id, project_member_id, created_at, updated_at)
SELECT
    'order_' || SUBSTR(MD5(RANDOM()::text), 1, 10),
    2000000,
    'PAID',
    'payment_key_' || SUBSTR(MD5(RANDOM()::text), 1, 10),
    m.member_id,
    pa.application_id,
    pm.project_member_id,
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '1 day'
FROM member m, partner_application pa, project_member pm, member payee
WHERE m.email = 'planner@example.com'
  AND payee.email = 'mar@example.com'
  AND pa.member_id = payee.member_id
  AND pm.member_id = payee.member_id
  AND pm.role = 'MAR'
LIMIT 1;

-- 트랜잭션 커밋 (저장)
COMMIT;
