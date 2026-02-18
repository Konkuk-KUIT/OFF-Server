-- =====================================================
-- 초기화: 모든 테이블 데이터 삭제 (PostgreSQL)
-- =====================================================
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
-- 시드 데이터: 모든 기능 테스트용 더미 데이터
-- =====================================================

-- 1. Member (기획자 5명 + 파트너 15명 = 총 20명)
-- 비밀번호: password123! ($2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO)

-- 기획자 (PM)
INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at) VALUES
('김기획', 'planner@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '기획왕', '5년차 PM입니다. 앱/웹 서비스 기획 전문.', '1995-03-15', 'https://avatar.iran.liara.run/public/1', 'PM', 'THREE_TIMES', true, NOW(), NOW()),
('이기획', 'planner2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '서비스설계자', '스타트업 PM 경력 3년.', '1997-07-20', 'https://avatar.iran.liara.run/public/2', 'PM', 'ONCE', false, NOW(), NOW()),
('박기획', 'planner3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '기획마스터', '다양한 도메인 경험 보유.', '1990-01-01', 'https://avatar.iran.liara.run/public/3', 'PM', 'PLUS_FIVE', false, NOW(), NOW()),
('최기획', 'planner4@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '신입PM', '열정 가득한 신입 PM입니다.', '2000-05-05', 'https://avatar.iran.liara.run/public/4', 'PM', 'ZERO', false, NOW(), NOW()),
('정기획', 'planner5@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '베테랑PM', '10년차 PM. 대규모 프로젝트 리딩 경험.', '1985-12-25', 'https://avatar.iran.liara.run/public/5', 'PM', 'PLUS_FIVE', true, NOW(), NOW());

-- 개발자 (DEV) - 5명
INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at) VALUES
('박개발', 'dev@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '풀스택개발자', 'Spring Boot + React 풀스택 개발자입니다.', '1996-05-10', 'https://avatar.iran.liara.run/public/11', 'DEV', 'TWICE', false, NOW(), NOW()),
('최개발', 'dev2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '백엔드마스터', 'Java/Kotlin 백엔드 전문. MSA 경험 다수.', '1994-11-25', 'https://avatar.iran.liara.run/public/12', 'DEV', 'PLUS_FIVE', true, NOW(), NOW()),
('강개발', 'dev3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '프론트엔드전문', 'React/Vue 프론트엔드 개발자.', '1998-06-18', 'https://avatar.iran.liara.run/public/13', 'DEV', 'ONCE', false, NOW(), NOW()),
('이개발', 'dev4@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '모바일개발자', 'iOS/Android native 개발 가능.', '1999-03-03', 'https://avatar.iran.liara.run/public/14', 'DEV', 'ZERO', false, NOW(), NOW()),
('김코딩', 'dev5@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '서버장인', '대용량 트래픽 처리 경험 보유.', '1993-08-15', 'https://avatar.iran.liara.run/public/15', 'DEV', 'THREE_TIMES', false, NOW(), NOW());

-- 디자이너 (DES) - 5명
INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at) VALUES
('정디자인', 'des@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', 'UI천재', 'Figma/Sketch 기반 UI/UX 디자이너. 앱 디자인 전문.', '1998-02-14', 'https://avatar.iran.liara.run/public/21', 'DES', 'ONCE', false, NOW(), NOW()),
('윤디자인', 'des2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', 'UX마스터', '사용자 경험 중심의 디자인 전문가.', '1996-12-05', 'https://avatar.iran.liara.run/public/22', 'DES', 'TWICE', false, NOW(), NOW()),
('오디자인', 'des3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '브랜딩전문', '브랜드 아이덴티티 구축 및 로고 디자인.', '1995-09-09', 'https://avatar.iran.liara.run/public/23', 'DES', 'PLUS_FIVE', false, NOW(), NOW()),
('박디자인', 'des4@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '웹디자이너', '반응형 웹 디자인 전문.', '1997-04-04', 'https://avatar.iran.liara.run/public/24', 'DES', 'ZERO', false, NOW(), NOW()),
('김아트', 'des5@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '그래픽장인', '일러스트레이터 활용 능숙. 캐릭터 디자인.', '1994-01-20', 'https://avatar.iran.liara.run/public/25', 'DES', 'THREE_TIMES', true, NOW(), NOW());

-- 마케터 (MAR) - 5명
INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at) VALUES
('한마케팅', 'mar@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '그로스해커', '퍼포먼스 마케팅 + 브랜딩 전문가.', '1997-09-01', 'https://avatar.iran.liara.run/public/31', 'MAR', 'ZERO', false, NOW(), NOW()),
('이마케팅', 'mar2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '콘텐츠마케터', 'SNS 콘텐츠 기획 및 제작.', '1998-11-11', 'https://avatar.iran.liara.run/public/32', 'MAR', 'ONCE', true, NOW(), NOW()),
('박홍보', 'mar3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', 'PR담당', '언론 홍보 및 보도자료 작성.', '1995-05-15', 'https://avatar.iran.liara.run/public/33', 'MAR', 'TWICE', false, NOW(), NOW()),
('최광고', 'mar4@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '광고기획자', '온오프라인 광고 캠페인 기획.', '1993-07-07', 'https://avatar.iran.liara.run/public/34', 'MAR', 'PLUS_FIVE', false, NOW(), NOW()),
('정분석', 'mar5@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '데이터분석가', '마케팅 데이터 분석 및 인사이트 도출.', '1996-02-28', 'https://avatar.iran.liara.run/public/35', 'MAR', 'THREE_TIMES', false, NOW(), NOW());


-- 2. Portfolio
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


-- 3. Project (진행 중 2개 + 완료 1개)
-- [IN_PROGRESS] 펫케어 플랫폼 (김기획)
INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, introduction, creator_id, created_at, updated_at)
SELECT '펫케어 플랫폼',
       '반려동물 건강 관리 및 수의사 상담 매칭 서비스',
       '실시간 채팅 상담, 건강 기록 대시보드, 수의사 예약 시스템 필요',
       8500000, '2026-01-15', '2026-04-15', 'APP', 'IN_PROGRESS',
       '반려동물 보호자와 수의사를 연결하는 종합 펫케어 플랫폼입니다.',
       member_id, NOW(), NOW()
FROM member WHERE email = 'planner@example.com';

-- [IN_PROGRESS] 스터디 매칭 서비스 (이기획)
INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, introduction, creator_id, created_at, updated_at)
SELECT '스터디 매칭 서비스',
       '개발자/디자이너 스터디 그룹 매칭 웹 서비스',
       '관심 기술 스택 기반 매칭, 스터디룸 관리, 출석 체크 기능',
       5000000, '2026-02-01', '2026-05-01', 'SERVICE', 'IN_PROGRESS',
       '함께 성장하는 개발자들을 위한 스터디 매칭 플랫폼.',
       member_id, NOW(), NOW()
FROM member WHERE email = 'planner2@example.com';

-- [COMPLETED] 헬스케어 앱 (김기획)
INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, introduction, creator_id, created_at, updated_at)
SELECT '헬스케어 앱',
       '운동 기록 및 식단 관리 모바일 앱',
       '운동 루틴 추천, 칼로리 계산기, 커뮤니티 기능',
       7000000, '2025-09-01', '2025-12-31', 'APP', 'COMPLETED',
       '성공적으로 완료된 헬스케어 프로젝트입니다.',
       member_id, NOW(), NOW()
FROM member WHERE email = 'planner@example.com';


-- 4. PartnerRecruit
-- 펫케어 플랫폼: DEV 2명, DES 1명, MAR 1명
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 2, 3000000, 'DEV', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '펫케어 플랫폼';

INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2500000, 'DES', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '펫케어 플랫폼';

INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2000000, 'MAR', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '펫케어 플랫폼';

-- 스터디 매칭 서비스: DEV 1명, DES 1명
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2500000, 'DEV', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '스터디 매칭 서비스';

INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2000000, 'DES', 'OPEN', project_id, NOW(), NOW() FROM project WHERE name = '스터디 매칭 서비스';

-- 헬스케어 앱 (완료된 프로젝트 - CLOSED)
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 0, 2500000, 'DEV', 'CLOSED', project_id, NOW(), NOW() FROM project WHERE name = '헬스케어 앱';

INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 0, 2000000, 'DES', 'CLOSED', project_id, NOW(), NOW() FROM project WHERE name = '헬스케어 앱';


-- 5. ProjectMember
-- 펫케어 플랫폼: 김기획(creator, PM) + 박개발(DEV) + 한마케팅(MAR)
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'planner@example.com' AND p.name = '펫케어 플랫폼';

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DEV', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'dev@example.com' AND p.name = '펫케어 플랫폼';

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'MAR', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'mar@example.com' AND p.name = '펫케어 플랫폼';

-- 스터디 매칭 서비스: 이기획(creator, PM)
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'planner2@example.com' AND p.name = '스터디 매칭 서비스';

-- 헬스케어 앱 (완료): 김기획(creator, PM) + 최개발(DEV) + 정디자인(DES)
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'planner@example.com' AND p.name = '헬스케어 앱';

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DEV', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'dev2@example.com' AND p.name = '헬스케어 앱';

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DES', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p WHERE m.email = 'des@example.com' AND p.name = '헬스케어 앱';


-- 6. PartnerApplication
-- 강개발이 펫케어 플랫폼 DEV에 지원 (WAITING)
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT false, 'WAITING', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr, project p
WHERE m.email = 'dev3@example.com' AND p.name = '펫케어 플랫폼' AND pr.project_id = p.project_id AND pr.role = 'DEV';

-- 윤디자인에게 펫케어 플랫폼 DES 초대 (WAITING)
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT true, 'WAITING', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr, project p
WHERE m.email = 'des2@example.com' AND p.name = '펫케어 플랫폼' AND pr.project_id = p.project_id AND pr.role = 'DES';

-- 한마케팅이 펫케어 플랫폼 MAR에 지원 후 수락됨 (ACCEPT) -> ProjectMember에 이미 추가됨
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT false, 'ACCEPT', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr, project p
WHERE m.email = 'mar@example.com' AND p.name = '펫케어 플랫폼' AND pr.project_id = p.project_id AND pr.role = 'MAR';


-- 7. Task + ToDo (펫케어 플랫폼)
-- API 설계 (박개발)
INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT 'API 설계', 'RESTful API 엔드포인트 설계 및 문서화', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p, project_member pm, member m
WHERE p.name = '펫케어 플랫폼' AND pm.project_id = p.project_id AND pm.member_id = m.member_id AND m.email = 'dev@example.com';

-- 화면 설계 (김기획)
INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT '화면 설계', '주요 화면 와이어프레임 및 프로토타입 제작', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p, project_member pm, member m
WHERE p.name = '펫케어 플랫폼' AND pm.project_id = p.project_id AND pm.member_id = m.member_id AND m.email = 'planner@example.com';

-- DB 설계 (박개발)
INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT 'DB 설계', '데이터베이스 ERD 설계 및 테이블 정의', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p, project_member pm, member m
WHERE p.name = '펫케어 플랫폼' AND pm.project_id = p.project_id AND pm.member_id = m.member_id AND m.email = 'dev@example.com';


-- ToDo for 'API 설계'
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '회원 API 설계', true, task_id, NOW(), NOW() FROM task WHERE name = 'API 설계';

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '상담 예약 API 설계', true, task_id, NOW(), NOW() FROM task WHERE name = 'API 설계';

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '결제 API 설계', false, task_id, NOW(), NOW() FROM task WHERE name = 'API 설계';

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '채팅 API 설계', false, task_id, NOW(), NOW() FROM task WHERE name = 'API 설계';

-- ToDo for '화면 설계'
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '홈 화면 와이어프레임', true, task_id, NOW(), NOW() FROM task WHERE name = '화면 설계';

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '상담 예약 화면 와이어프레임', false, task_id, NOW(), NOW() FROM task WHERE name = '화면 설계';

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '프로토타입 제작', false, task_id, NOW(), NOW() FROM task WHERE name = '화면 설계';

-- ToDo for 'DB 설계'
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT 'ERD 초안 작성', true, task_id, NOW(), NOW() FROM task WHERE name = 'DB 설계';

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '인덱스 전략 수립', false, task_id, NOW(), NOW() FROM task WHERE name = 'DB 설계';


-- 8. ChatRoom + ChatRoomMember + Message
-- 펫케어 플랫폼 프로젝트 채팅방 (김기획 + 박개발 + 한마케팅)
INSERT INTO chat_room (chat_type, project_id, created_at, updated_at)
SELECT 'PROJECT', project_id, NOW(), NOW() FROM project WHERE name = '펫케어 플랫폼';

-- ChatRoomMember
INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id AND p.name = '펫케어 플랫폼' AND m.email IN ('planner@example.com', 'dev@example.com', 'mar@example.com');

-- Messages
INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT '안녕하세요! 박개발님 반갑습니다.', true, m.member_id, cr.chat_room_id, NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours'
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id AND p.name = '펫케어 플랫폼' AND m.email = 'planner@example.com';

INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT '네, 기획왕님! 잘 부탁드립니다.', true, m.member_id, cr.chat_room_id, NOW() - INTERVAL '1 hour', NOW() - INTERVAL '1 hour'
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id AND p.name = '펫케어 플랫폼' AND m.email = 'dev@example.com';

INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT 'API 설계 작업 진행 상황 어떠신가요?', false, m.member_id, cr.chat_room_id, NOW() - INTERVAL '30 minutes', NOW() - INTERVAL '30 minutes'
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id AND p.name = '펫케어 플랫폼' AND m.email = 'planner@example.com';


-- 개인 채팅방 (CONTACT): 김기획 + 윤디자인
INSERT INTO chat_room (chat_type, created_at, updated_at) VALUES ('CONTACT', NOW(), NOW());

-- 최근 생성된 'CONTACT' 채팅방 ID 가져오기 (가장 최근 것)
-- MySQL/H2 호환 방식으로 간접적으로 ID 참조가 어렵지만, 시퀀스 가정하여 처리하거나 LIMIT 1로 최근 것 선택
-- 여기서는 일단 2번째 채팅방이라고 가정하지 않고, member 조인으로 처리하기엔 복잡하므로,
-- 위에서 생성한 'CONTACT' 채팅방을 특정할 수 있는 로직이 필요함.
-- data.sql은 순차 실행되므로, 가장 최근에 생성된 채팅방이 위 INSERT에 의한 것임.
-- 하지만 SQL문 내에서 변수 사용 불가.
-- 안전하게: 위 INSERT 후, PROJECT ID가 NULL인 가장 큰 ID의 채팅방을 타겟팅.

INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, member m
WHERE cr.chat_type = 'CONTACT' AND cr.project_id IS NULL AND m.email = 'planner@example.com'
ORDER BY cr.chat_room_id DESC LIMIT 1;

INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, member m
WHERE cr.chat_type = 'CONTACT' AND cr.project_id IS NULL AND m.email = 'des2@example.com'
ORDER BY cr.chat_room_id DESC LIMIT 1;


-- 9. Notification (다양한 타입)
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


-- 10. PayLog (결제 완료 샘플)
INSERT INTO pay_log (order_id, amount, status, payment_key, member_id, application_id, project_member_id, created_at, updated_at)
SELECT
    'order_sample_12345',
    2000000,
    'PAID',
    'payment_key_sample_12345',
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