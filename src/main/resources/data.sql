-- =====================================================
-- 시드 데이터: H2 / PostgreSQL 호환
-- =====================================================

-- 1. Member (기획자 2명 + 파트너 4명)
INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count_type, is_working, created_at, updated_at)
SELECT '김기획', 'planner@example.com', 'password123', '기획왕', '5년차 PM입니다. 앱/웹 서비스 기획 전문.', '1995-03-15', 'https://example.com/planner.png', 'PM', 'THREE_TIMES', true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'planner@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count_type, is_working, created_at, updated_at)
SELECT '이기획', 'planner2@example.com', 'password123', '서비스설계자', '스타트업 PM 경력 3년.', '1997-07-20', 'https://example.com/planner2.png', 'PM', 'ONCE', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'planner2@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count_type, is_working, created_at, updated_at)
SELECT '박개발', 'dev@example.com', 'password123', '풀스택개발자', 'Spring Boot + React 풀스택 개발자입니다.', '1996-05-10', 'https://example.com/dev.png', 'DEV', 'TWICE', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'dev@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count_type, is_working, created_at, updated_at)
SELECT '최개발', 'dev2@example.com', 'password123', '백엔드마스터', 'Java/Kotlin 백엔드 전문. MSA 경험 다수.', '1994-11-25', 'https://example.com/dev2.png', 'DEV', 'PLUS_FIVE', true, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'dev2@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count_type, is_working, created_at, updated_at)
SELECT '정디자인', 'des@example.com', 'password123', 'UI천재', 'Figma/Sketch 기반 UI/UX 디자이너. 앱 디자인 전문.', '1998-02-14', 'https://example.com/des.png', 'DES', 'ONCE', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'des@example.com');

INSERT INTO member (name, email, password, nickname, self_introduction, birth, profile_image, role, project_count_type, is_working, created_at, updated_at)
SELECT '한마케팅', 'mar@example.com', 'password123', '그로스해커', '퍼포먼스 마케팅 + 브랜딩 전문가.', '1997-09-01', 'https://example.com/mar.png', 'MAR', 'ZERO', false, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1 FROM member WHERE email = 'mar@example.com');

-- 2. Portfolio (파트너들에게 포트폴리오 추가)
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

-- 3. Project (2개: 진행중 1개 + 진행중 1개)
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

-- 4. PartnerRecruit (프로젝트별 모집 공고)
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

-- 5. ProjectMember (기획자 = creator이므로 프로젝트 멤버에도 추가)
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'planner@example.com' AND p.name = '펫케어 플랫폼'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'PM', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'planner2@example.com' AND p.name = '스터디 매칭 서비스'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

-- 박개발을 펫케어 플랫폼에 참여시킴
INSERT INTO project_member (role, member_id, project_id, created_at, updated_at)
SELECT 'DEV', m.member_id, p.project_id, NOW(), NOW()
FROM member m, project p
WHERE m.email = 'dev@example.com' AND p.name = '펫케어 플랫폼'
AND NOT EXISTS (SELECT 1 FROM project_member pm WHERE pm.member_id = m.member_id AND pm.project_id = p.project_id);

-- 6. PartnerApplication 샘플
-- 최개발이 펫케어 플랫폼 DEV에 지원 (WAITING 상태)
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT false, 'WAITING', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr, project p
WHERE m.email = 'dev2@example.com'
  AND p.name = '펫케어 플랫폼'
  AND pr.project_id = p.project_id
  AND pr.role = 'DEV'
AND NOT EXISTS (SELECT 1 FROM partner_application pa WHERE pa.member_id = m.member_id AND pa.partner_recruit_id = pr.partner_recruit_id);

-- 정디자인에게 펫케어 플랫폼 DES 초대 (WAITING 상태)
INSERT INTO partner_application (is_from_project, application_status, member_id, partner_recruit_id, created_at, updated_at)
SELECT true, 'WAITING', m.member_id, pr.partner_recruit_id, NOW(), NOW()
FROM member m, partner_recruit pr, project p
WHERE m.email = 'des@example.com'
  AND p.name = '펫케어 플랫폼'
  AND pr.project_id = p.project_id
  AND pr.role = 'DES'
AND NOT EXISTS (SELECT 1 FROM partner_application pa WHERE pa.member_id = m.member_id AND pa.partner_recruit_id = pr.partner_recruit_id);

-- 7. Task + ToDo (펫케어 플랫폼에 태스크 추가)
-- 기획자(PM)의 ProjectMember ID 사용
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

-- 8. Notification 샘플
INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '최개발님이 DEV 역할로 지원했어요!', '/projects/1/applications/1', false, 'APPLICATION', m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'planner@example.com'
AND NOT EXISTS (SELECT 1 FROM notification n WHERE n.content = '최개발님이 DEV 역할로 지원했어요!' AND n.member_id = m.member_id);

INSERT INTO notification (content, url, is_read, notification_type, member_id, created_at, updated_at)
SELECT '펫케어 플랫폼 프로젝트에서 파트너 제안이 도착했어요!', '/invitations/2', false, 'INVITE', m.member_id, NOW(), NOW()
FROM member m WHERE m.email = 'des@example.com'
AND NOT EXISTS (SELECT 1 FROM notification n WHERE n.content LIKE '%펫케어 플랫폼%파트너 제안%' AND n.member_id = m.member_id);
