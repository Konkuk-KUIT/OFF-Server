-- =====================================================
-- 시드 데이터: 모든 기능 테스트용 더미 데이터
-- =====================================================

-- 1. Member (기획자 2명 + 파트너 6명 = 총 8명)
INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at)
SELECT '김기획', 'planner@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '기획왕', '5년차 PM입니다. 앱/웹 서비스 기획 전문.', '1995-03-15', 'https://example.com/planner.png', 'PM', 'THREE_TIMES', true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'planner@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at)
SELECT '이기획', 'planner2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '서비스설계자', '스타트업 PM 경력 3년.', '1997-07-20', 'https://example.com/planner2.png', 'PM', 'ONCE', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'planner2@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at)
SELECT '박개발', 'dev@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '풀스택개발자', 'Spring Boot + React 풀스택 개발자입니다.', '1996-05-10', 'https://example.com/dev.png', 'DEV', 'TWICE', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'dev@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at)
SELECT '최개발', 'dev2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '백엔드마스터', 'Java/Kotlin 백엔드 전문. MSA 경험 다수.', '1994-11-25', 'https://example.com/dev2.png', 'DEV', 'PLUS_FIVE', true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'dev2@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at)
SELECT '강개발', 'dev3@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '프론트엔드전문', 'React/Vue 프론트엔드 개발자.', '1998-06-18', 'https://example.com/dev3.png', 'DEV', 'ONCE', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'dev3@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at)
SELECT '정디자인', 'des@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', 'UI천재', 'Figma/Sketch 기반 UI/UX 디자이너. 앱 디자인 전문.', '1998-02-14', 'https://example.com/des.png', 'DES', 'ONCE', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'des@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at)
SELECT '윤디자인', 'des2@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', 'UX마스터', '사용자 경험 중심의 디자인 전문가.', '1996-12-05', 'https://example.com/des2.png', 'DES', 'TWICE', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'des2@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count, is_working, created_at, updated_at)
SELECT '한마케팅', 'mar@example.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRdOox/8aOAlHZjQSKc7vT8LvqO', '그로스해커', '퍼포먼스 마케팅 + 브랜딩 전문가.', '1997-09-01', 'https://example.com/mar.png', 'MAR', 'ZERO', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'mar@example.com');

-- 2. Portfolio
INSERT INTO portfolio (link, description, member_id, created_at, updated_at)
SELECT 'https://github.com/parkdev', 'Spring Boot 쇼핑몰 프로젝트', m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'dev@example.com'
AND NOT EXISTS (SELECT 1 FROM portfolio p WHERE p.member_id = m.member_id AND p.link = 'https://github.com/parkdev');

INSERT INTO portfolio (link, description, member_id, created_at, updated_at)
SELECT 'https://github.com/parkdev/chat-app', 'WebSocket 실시간 채팅 앱', m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'dev@example.com'
AND NOT EXISTS (SELECT 1 FROM portfolio p WHERE p.member_id = m.member_id AND p.link = 'https://github.com/parkdev/chat-app');

INSERT INTO portfolio (link, description, member_id, created_at, updated_at)
SELECT 'https://behance.net/jungdesign', 'e-commerce 앱 UI/UX 리디자인', m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'des@example.com'
AND NOT EXISTS (SELECT 1 FROM portfolio p WHERE p.member_id = m.member_id AND p.link = 'https://behance.net/jungdesign');

INSERT INTO portfolio (link, description, member_id, created_at, updated_at)
SELECT 'https://github.com/choidev', 'MSA 기반 결제 시스템', m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'dev2@example.com'
AND NOT EXISTS (SELECT 1 FROM portfolio p WHERE p.member_id = m.member_id AND p.link = 'https://github.com/choidev');

-- 3. Project (진행 중 2개 + 완료 1개)
INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, introduction, creator_id, created_at, updated_at)
SELECT '펫케어 플랫폼',
       '반려동물 건강 관리 및 수의사 상담 매칭 서비스',
       '실시간 채팅 상담, 건강 기록 대시보드, 수의사 예약 시스템 필요',
       8500000, '2026-01-15', '2026-04-15', 'APP', 'IN_PROGRESS',
       '반려동물 보호자와 수의사를 연결하는 종합 펫케어 플랫폼입니다.',
       m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'planner@example.com'
AND NOT EXISTS (SELECT 1 FROM project WHERE name = '펫케어 플랫폼');

INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, creator_id, created_at, updated_at)
SELECT '스터디 매칭 서비스',
       '개발자/디자이너 스터디 그룹 매칭 웹 서비스',
       '관심 기술 스택 기반 매칭, 스터디룸 관리, 출석 체크 기능',
       5000000, '2026-02-01', '2026-05-01', 'SERVICE', 'IN_PROGRESS',
       m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'planner2@example.com'
AND NOT EXISTS (SELECT 1 FROM project WHERE name = '스터디 매칭 서비스');

-- 완료된 프로젝트
INSERT INTO project (name, description, requirement, estimation, start_date, end_date, project_type, status, introduction, creator_id, created_at, updated_at)
SELECT '헬스케어 앱',
       '운동 기록 및 식단 관리 모바일 앱',
       '운동 루틴 추천, 칼로리 계산기, 커뮤니티 기능',
       7000000, '2025-09-01', '2025-12-31', 'APP', 'COMPLETED',
       '성공적으로 완료된 헬스케어 프로젝트입니다.',
       m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'planner@example.com'
AND NOT EXISTS (SELECT 1 FROM project WHERE name = '헬스케어 앱');

-- 4. PartnerRecruit
-- 펫케어 플랫폼: DEV 2명, DES 1명, MAR 1명
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 2, 3000000, 'DEV', 'OPEN', p.project_id, NOW(), NOW()
FROM project p WHERE p.name = '펫케어 플랫폼'
AND NOT EXISTS (SELECT 1 FROM partner_recruit pr WHERE pr.project_id = p.project_id AND pr.role = 'DEV');

INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2500000, 'DES', 'OPEN', p.project_id, NOW(), NOW()
FROM project p WHERE p.name = '펫케어 플랫폼'
AND NOT EXISTS (SELECT 1 FROM partner_recruit pr WHERE pr.project_id = p.project_id AND pr.role = 'DES');

INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2000000, 'MAR', 'OPEN', p.project_id, NOW(), NOW()
FROM project p WHERE p.name = '펫케어 플랫폼'
AND NOT EXISTS (SELECT 1 FROM partner_recruit pr WHERE pr.project_id = p.project_id AND pr.role = 'MAR');

-- 스터디 매칭 서비스: DEV 1명, DES 1명
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2500000, 'DEV', 'OPEN', p.project_id, NOW(), NOW()
FROM project p WHERE p.name = '스터디 매칭 서비스'
AND NOT EXISTS (SELECT 1 FROM partner_recruit pr WHERE pr.project_id = p.project_id AND pr.role = 'DEV');

INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 1, 2000000, 'DES', 'OPEN', p.project_id, NOW(), NOW()
FROM project p WHERE p.name = '스터디 매칭 서비스'
AND NOT EXISTS (SELECT 1 FROM partner_recruit pr WHERE pr.project_id = p.project_id AND pr.role = 'DES');

-- 헬스케어 앱 (완료된 프로젝트 - CLOSED)
INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 0, 2500000, 'DEV', 'CLOSED', p.project_id, NOW(), NOW()
FROM project p WHERE p.name = '헬스케어 앱'
AND NOT EXISTS (SELECT 1 FROM partner_recruit pr WHERE pr.project_id = p.project_id AND pr.role = 'DEV');

INSERT INTO partner_recruit (number_of_person, cost, role, recruit_status, project_id, created_at, updated_at)
SELECT 0, 2000000, 'DES', 'CLOSED', p.project_id, NOW(), NOW()
FROM project p WHERE p.name = '헬스케어 앱'
AND NOT EXISTS (SELECT 1 FROM partner_recruit pr WHERE pr.project_id = p.project_id AND pr.role = 'DES');

-- 5. ProjectMember
-- 펫케어 플랫폼: 김기획(creator) + 박개발
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'planner@example.com' AND p.name = '펫케어 플랫폼'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DEV', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'dev@example.com' AND p.name = '펫케어 플랫폼'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

-- 스터디 매칭 서비스: 이기획(creator)
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'planner2@example.com' AND p.name = '스터디 매칭 서비스'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

-- 헬스케어 앱 (완료): 김기획(creator) + 최개발 + 정디자인
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'planner@example.com' AND p.name = '헬스케어 앱'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DEV', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'dev2@example.com' AND p.name = '헬스케어 앱'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DES', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'des@example.com' AND p.name = '헬스케어 앱'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

-- 6. PartnerApplication
-- 강개발이 펫케어 플랫폼 DEV에 지원 (WAITING)
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT false, 'WAITING', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr, project p
WHERE m.email = 'dev3@example.com'
  AND p.name = '펫케어 플랫폼'
  AND pr.project_id = p.project_id
  AND pr.role = 'DEV'
AND NOT EXISTS (SELECT 1 FROM partner_application pa WHERE pa.member_id = m.member_id AND pa.partner_recruit_id = pr.partner_recruit_id);

-- 윤디자인에게 펫케어 플랫폼 DES 초대 (WAITING)
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT true, 'WAITING', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr, project p
WHERE m.email = 'des2@example.com'
  AND p.name = '펫케어 플랫폼'
  AND pr.project_id = p.project_id
  AND pr.role = 'DES'
AND NOT EXISTS (SELECT 1 FROM partner_application pa WHERE pa.member_id = m.member_id AND pa.partner_recruit_id = pr.partner_recruit_id);

-- 한마케팅이 펫케어 플랫폼 MAR에 지원 후 수락됨 (ACCEPT)
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT false, 'ACCEPT', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr, project p
WHERE m.email = 'mar@example.com'
  AND p.name = '펫케어 플랫폼'
  AND pr.project_id = p.project_id
  AND pr.role = 'MAR'
AND NOT EXISTS (SELECT 1 FROM partner_application pa WHERE pa.member_id = m.member_id AND pa.partner_recruit_id = pr.partner_recruit_id);

-- 7. Task + ToDo (펫케어 플랫폼)
INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT 'API 설계', 'RESTful API 엔드포인트 설계 및 문서화', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p, project_member pm, member m
WHERE p.name = '펫케어 플랫폼'
  AND pm.project_id = p.project_id
  AND pm.member_id = m.member_id
  AND m.email = 'dev@example.com'
AND NOT EXISTS (SELECT 1 FROM task t WHERE t.name = 'API 설계' AND t.project_id = p.project_id);

INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT '화면 설계', '주요 화면 와이어프레임 및 프로토타입 제작', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p, project_member pm, member m
WHERE p.name = '펫케어 플랫폼'
  AND pm.project_id = p.project_id
  AND pm.member_id = m.member_id
  AND m.email = 'planner@example.com'
AND NOT EXISTS (SELECT 1 FROM task t WHERE t.name = '화면 설계' AND t.project_id = p.project_id);

INSERT INTO task (name, description, project_id, project_member_id, created_at, updated_at)
SELECT 'DB 설계', '데이터베이스 ERD 설계 및 테이블 정의', p.project_id, pm.project_member_id, NOW(), NOW()
FROM project p, project_member pm, member m
WHERE p.name = '펫케어 플랫폼'
  AND pm.project_id = p.project_id
  AND pm.member_id = m.member_id
  AND m.email = 'dev@example.com'
AND NOT EXISTS (SELECT 1 FROM task t WHERE t.name = 'DB 설계' AND t.project_id = p.project_id);

-- ToDo for 'API 설계'
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '회원 API 설계', true, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = 'API 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = '회원 API 설계' AND td.task_id = t.task_id);

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '상담 예약 API 설계', true, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = 'API 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = '상담 예약 API 설계' AND td.task_id = t.task_id);

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '결제 API 설계', false, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = 'API 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = '결제 API 설계' AND td.task_id = t.task_id);

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '채팅 API 설계', false, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = 'API 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = '채팅 API 설계' AND td.task_id = t.task_id);

-- ToDo for '화면 설계'
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '홈 화면 와이어프레임', true, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = '화면 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = '홈 화면 와이어프레임' AND td.task_id = t.task_id);

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '상담 예약 화면 와이어프레임', false, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = '화면 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = '상담 예약 화면 와이어프레임' AND td.task_id = t.task_id);

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '프로토타입 제작', false, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = '화면 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = '프로토타입 제작' AND td.task_id = t.task_id);

-- ToDo for 'DB 설계'
INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT 'ERD 초안 작성', true, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = 'DB 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = 'ERD 초안 작성' AND td.task_id = t.task_id);

INSERT INTO to_do (content, is_done, task_id, created_at, updated_at)
SELECT '인덱스 전략 수립', false, t.task_id, NOW(), NOW()
FROM task t WHERE t.name = 'DB 설계'
AND NOT EXISTS (SELECT 1 FROM to_do td WHERE td.content = '인덱스 전략 수립' AND td.task_id = t.task_id);

-- 8. ChatRoom + ChatRoomMember + Message
-- 펫케어 플랫폼 프로젝트 채팅방 (김기획 + 박개발)
INSERT INTO chat_room (chat_type, project_id, created_at, updated_at)
SELECT 'PROJECT', p.project_id, NOW(), NOW()
FROM project p WHERE p.name = '펫케어 플랫폼'
AND NOT EXISTS (SELECT 1 FROM chat_room cr WHERE cr.project_id = p.project_id AND cr.chat_type = 'PROJECT');

-- ChatRoomMember
INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id
  AND p.name = '펫케어 플랫폼'
  AND m.email = 'planner@example.com'
AND NOT EXISTS (SELECT 1 FROM chat_room_member crm WHERE crm.chat_room_id = cr.chat_room_id AND crm.member_id = m.member_id);

INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id
  AND p.name = '펫케어 플랫폼'
  AND m.email = 'dev@example.com'
AND NOT EXISTS (SELECT 1 FROM chat_room_member crm WHERE crm.chat_room_id = cr.chat_room_id AND crm.member_id = m.member_id);

-- Messages
INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT '안녕하세요! 박개발님 반갑습니다.', true, m.member_id, cr.chat_room_id, NOW() - INTERVAL '2 hours', NOW() - INTERVAL '2 hours'
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id
  AND p.name = '펫케어 플랫폼'
  AND m.email = 'planner@example.com'
AND NOT EXISTS (SELECT 1 FROM message msg WHERE msg.content = '안녕하세요! 박개발님 반갑습니다.' AND msg.chat_room_id = cr.chat_room_id);

INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT '네, 기획왕님! 잘 부탁드립니다.', true, m.member_id, cr.chat_room_id, NOW() - INTERVAL '1 hour', NOW() - INTERVAL '1 hour'
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id
  AND p.name = '펫케어 플랫폼'
  AND m.email = 'dev@example.com'
AND NOT EXISTS (SELECT 1 FROM message msg WHERE msg.content = '네, 기획왕님! 잘 부탁드립니다.' AND msg.chat_room_id = cr.chat_room_id);

INSERT INTO message (content, is_read, member_id, chat_room_id, created_at, updated_at)
SELECT 'API 설계 작업 진행 상황 어떠신가요?', false, m.member_id, cr.chat_room_id, NOW() - INTERVAL '30 minutes', NOW() - INTERVAL '30 minutes'
FROM chat_room cr, project p, member m
WHERE cr.project_id = p.project_id
  AND p.name = '펫케어 플랫폼'
  AND m.email = 'planner@example.com'
AND NOT EXISTS (SELECT 1 FROM message msg WHERE msg.content = 'API 설계 작업 진행 상황 어떠신가요?' AND msg.chat_room_id = cr.chat_room_id);

-- 개인 채팅방 (CONTACT): 김기획 + 윤디자인
INSERT INTO chat_room (chat_type, created_at, updated_at)
SELECT 'CONTACT', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM chat_room cr
  JOIN chat_room_member crm1 ON cr.chat_room_id = crm1.chat_room_id
  JOIN chat_room_member crm2 ON cr.chat_room_id = crm2.chat_room_id
  JOIN member m1 ON crm1.member_id = m1.member_id
  JOIN member m2 ON crm2.member_id = m2.member_id
  WHERE cr.chat_type = 'CONTACT' AND m1.email = 'planner@example.com' AND m2.email = 'des2@example.com');

INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, member m
WHERE cr.chat_type = 'CONTACT'
  AND cr.project_id IS NULL
  AND m.email = 'planner@example.com'
  AND NOT EXISTS (
    SELECT 1 FROM chat_room_member crm
    WHERE crm.chat_room_id = cr.chat_room_id
    AND crm.member_id = m.member_id
  )
LIMIT 1;

INSERT INTO chat_room_member (chat_room_id, member_id, last_read_at, created_at, updated_at)
SELECT cr.chat_room_id, m.member_id, NOW(), NOW(), NOW()
FROM chat_room cr, member m
WHERE cr.chat_type = 'CONTACT'
  AND cr.project_id IS NULL
  AND m.email = 'des2@example.com'
  AND NOT EXISTS (
    SELECT 1 FROM chat_room_member crm
    WHERE crm.chat_room_id = cr.chat_room_id
    AND crm.member_id = m.member_id
  )
LIMIT 1;

-- 9. Notification (다양한 타입)
INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '강개발님이 DEV 역할로 지원했어요! (예상 비용: 3,000,000원)', '/payments/prepare/1', false, 'APPLICATION', m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'planner@example.com'
AND NOT EXISTS (SELECT 1 FROM notification n WHERE n.content LIKE '%강개발님%' AND n.member_id = m.member_id);

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '펫케어 플랫폼 프로젝트에서 파트너 제안이 도착했어요!', '/invitations/2', false, 'INVITE', m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'des2@example.com'
AND NOT EXISTS (SELECT 1 FROM notification n WHERE n.content LIKE '%펫케어 플랫폼%파트너 제안%' AND n.member_id = m.member_id);

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '결제 성공! 파트너 매칭이 되었어요.', '/projects/1', true, 'PAY', m.member_id, NOW() - INTERVAL '1 day', NOW() - INTERVAL '1 day'
FROM member m WHERE m.email = 'dev@example.com'
AND NOT EXISTS (SELECT 1 FROM notification n WHERE n.notification_type = 'PAY' AND n.member_id = m.member_id);

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '헬스케어 앱 프로젝트가 완료되었습니다!', '/projects/3', true, 'PROJECT_COMPLETE', m.member_id, NOW() - INTERVAL '7 days', NOW() - INTERVAL '7 days'
FROM member m WHERE m.email = 'dev2@example.com'
AND NOT EXISTS (SELECT 1 FROM notification n WHERE n.notification_type = 'PROJECT_COMPLETE' AND n.member_id = m.member_id);

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '새로운 메시지가 도착했습니다.', '/chat/rooms/1', false, 'CHAT', m.member_id, NOW() - INTERVAL '10 minutes', NOW() - INTERVAL '10 minutes'
FROM member m WHERE m.email = 'dev@example.com'
AND NOT EXISTS (SELECT 1 FROM notification n WHERE n.notification_type = 'CHAT' AND n.member_id = m.member_id);

-- 10. PayLog (결제 완료 샘플)
INSERT INTO pay_log (order_id, amount, status, payment_key, member_id, application_id, project_member_id, created_at, updated_at)
SELECT
    'order_' || SUBSTR(MD5(RANDOM()::text), 1, 32),
    2500000,
    'PAID',
    'payment_key_' || SUBSTR(MD5(RANDOM()::text), 1, 20),
    m.member_id,
    pa.application_id,
    pm.project_member_id,
    NOW() - INTERVAL '1 day',
    NOW() - INTERVAL '1 day'
FROM member m, partner_application pa, project_member pm, member payee
WHERE m.email = 'planner@example.com'
  AND payee.email = 'dev@example.com'
  AND pa.member_id = payee.member_id
  AND pm.member_id = payee.member_id
  AND pm.role = 'DEV'
AND NOT EXISTS (SELECT 1 FROM pay_log pl WHERE pl.member_id = m.member_id AND pl.status = 'PAID')
LIMIT 1;