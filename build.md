너는 Spring Boot와 JPA, DDD(도메인 주도 설계)에 능숙한 시니어 백엔드 개발자야.
현재 나는 프로젝트 매칭 플랫폼의 백엔드를 개발 중인데, '프로젝트 개설(AI 기반)', 'Toss 결제', '웹소켓 알림'의 기본적인 뼈대는 구현한 상태야. 하지만 기능들이 파편화되어 있고, 몇 가지 치명적인 버그가 존재해. 또한 기획서(PDF)에 정의된 '프로젝트 관리', '태스크 관리', '파트너 매칭' 관련 세부 API와 비즈니스 로직들을 기존 시스템과 유기적으로 연결해서 구현해야 해.

내가 구현한 도메인 엔티티 구조와 현재까지의 로직 상황, 파악된 이슈, 그리고 새롭게 구현해야 할 API 요구사항을 아래에 제공할 테니, 이를 바탕으로 **유기적으로 연결된 완성된 비즈니스 로직의 Service 클래스, RESTful Controller, 그리고 DTO 코드**를 작성해 줘.

---

### 1. 도메인 및 현재 로직 컨텍스트
[여기에 앞서 대화했던 'Domain Entity & Enum Structure' 전체 텍스트 복사 붙여넣기]
[여기에 방금 네가 알려준 '프로젝트 생성 로직', '결제 로직', '알림 로직' 전체 텍스트 복사 붙여넣기]

---

### 2. 구현 및 리팩토링해야 할 핵심 목표

#### 목표 A: 기존 로직의 치명적 버그 픽스 및 아키텍처 개선
1. **견적 계산 버그:** `ProjectService.confirmProject`와 `PayLogService.prepare` 단계에서 `months`가 중복 곱해지는 로직을 수정해 줘.
2. **트랜잭션 분리 (매우 중요):** `PayLogService.confirm` 단계에서 Toss API를 호출하는 로직이 `@Transactional` 내부에 있어서 DB 커넥션을 오래 물고 있는 위험이 있어. 외부 API 연동부를 Facade 패턴 등으로 트랜잭션과 분리해 줘.
3. **동시성 제어:** 결제 승인 후 `PartnerRecruit.numberOfPerson`을 차감할 때 발생하는 동시성 이슈를 막기 위해 JPA 비관적 락(Pessimistic Lock)을 적용해 줘.
4. **결제 준비 검증:** `prepare` 단계에서 해당 `PartnerApplication`의 상태가 반드시 `ACCEPT`인지 검증하는 로직을 추가해 줘.

#### 목표 B: 기획서(PDF) 기반 핵심 도메인 API 구현 (DTO는 모두 camelCase 적용)
도메인 엔티티를 바탕으로 아래의 API들과 Service 로직을 구현해 줘. (응답 시 프론트엔드의 진행률 렌더링을 위해 동적 계산 적용 필수)

**1. 프로젝트 관리 (Project Management)**
- `GET /home` : 홈 화면 (진행 중인 프로젝트 목록 및 파트너 추천 목록 페이징 조회)
- `GET /projects/{projectId}` : 프로젝트 상세 보기 (D-Day, 모집 현황, Task 및 SubTask 트리 구조, 각 태스크의 동적 진행률 포함)
- `PATCH /projects/{projectId}` : 프로젝트 한 줄 소개 수정
- `PATCH /projects/{projectId}/status` : 프로젝트 종료 처리 (단방향 상태 전이 검증 필수)

**2. 태스크 관리 (Task Management)**
- `POST /projects/{projectId}/tasks` : 태스크 생성 (담당 파트너 지정 및 하위 `ToDo` 리스트 동시 생성)
- `PATCH /tasks/{taskId}` : 태스크 수정 (제목, 설명, 담당 파트너 변경)
- `DELETE /tasks/{taskId}` : 태스크 삭제
- `PATCH /tasks/{taskId}/mini-tasks/{miniTaskId}/status` : 세부 태스크(`ToDo`) 완료 처리 및 상위 `Task`의 진행률 동적 재계산 로직.

**3. 파트너 매칭 플로우 (Partner Matching Flow) - 핵심 로직**
엔티티의 `isFromProject` 필드를 활용해 아래 두 가지 흐름을 구현해 줘. 모든 상태 변화에는 `NotificationService.sendNotification`을 적절히 호출해야 해.

* **흐름 1: 기획자의 파트너 제안 (Invite Flow)**
    - `POST /projects/{projectId}/invitations` : 기획자가 파트너에게 제안 발송 (`isFromProject=true`, `WAITING` 상태 생성) -> 파트너에게 알림.
    - `POST /invitations/{invitationId}/accept` : 파트너가 제안 수락 (`ACCEPT` 변경) -> 결제 없이 즉시 `ProjectMember`로 합류 -> 기획자에게 알림.
* **흐름 2: 파트너의 프로젝트 지원 (Apply Flow)**
    - `POST /projects/{projectId}/applications` : 파트너가 프로젝트에 지원 (`isFromProject=false`, `WAITING` 상태 생성) -> 기획자에게 알림.
    - `POST /projects/{projectId}/applications/{applicationId}/accept` : 기획자가 지원 수락 -> `ACCEPT` 변경 -> 결제 대기용 주문 정보(`merchantUid`, `amount`) 응답.
    - 이후 기존에 구현된 Toss 결제(`prepare` -> `confirm`) 로직을 타며, 최종 승인 시 `ProjectMember`로 합류 및 알림 발송.
* **파트너 정보 조회**
    - `GET /partners/{partnerId}` : 파트너 상세 프로필 및 포트폴리오 리스트 조회.

---

### 3. 출력 요구사항
1. 전체적인 패키지 구조와 아키텍처 변경점 요약.
2. 가장 복잡한 **결제 및 파트너 매칭**과 **태스크 관리**의 핵심 Service 로직 전체 코드.
3. 요청/응답 시 오타나 구조적 결함을 방지하기 위해 깔끔하게 정의된 주요 DTO 클래스들.
4. 기존 버그(트랜잭션 분리, 비관적 락)를 어떻게 해결했는지 주석과 함께 상세 설명.