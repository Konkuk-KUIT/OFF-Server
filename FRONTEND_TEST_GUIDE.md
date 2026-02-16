# 🧪 OFF 프로젝트 - 프론트엔드 테스트 가이드

## 🌐 서버 정보

```
Base URL: http://offf.kro.kr:8080
Swagger UI: http://offf.kro.kr:8080/swagger-ui/index.html
```

---

## 👥 테스트 계정 정보

### 📌 **공통 비밀번호**
```
password123
```
⚠️ 모든 테스트 계정의 비밀번호는 동일합니다.

---

## 🎭 역할별 테스트 계정

### 1. 기획자 (PM) 계정

#### 🟢 김기획 (프로젝트 진행 중)
```json
{
  "email": "planner@example.com",
  "password": "password123",
  "role": "PM",
  "nickname": "기획왕",
  "프로젝트 경험": "3회 (THREE_TIMES)",
  "현재 상태": "작업 중 (isWorking: true)"
}
```
**보유 프로젝트:**
- ✅ **펫케어 플랫폼** (진행 중) - 반려동물 건강 관리 앱
- ✅ **헬스케어 앱** (완료) - 운동/식단 관리 앱

**테스트 가능 기능:**
- 프로젝트 생성 (견적 미리보기, 확정)
- 프로젝트 상세 조회
- 파트너 모집 공고 생성
- 파트너 지원 승인/거절
- 프로젝트 완료 처리
- 태스크 생성/수정/삭제

#### 🟡 이기획 (프로젝트 없음)
```json
{
  "email": "planner2@example.com",
  "password": "password123",
  "role": "PM",
  "nickname": "서비스설계자",
  "프로젝트 경험": "1회 (ONCE)",
  "현재 상태": "대기 중 (isWorking: false)"
}
```
**보유 프로젝트:**
- ✅ **스터디 매칭 서비스** (진행 중) - 개발자/디자이너 스터디 그룹 매칭

**테스트 가능 기능:**
- 새 프로젝트 생성
- 홈 화면 조회

---

### 2. 개발자 (DEV) 계정

#### 💻 박개발
```json
{
  "email": "dev@example.com",
  "password": "password123",
  "role": "DEV",
  "nickname": "풀스택개발자",
  "자기소개": "Spring Boot + React 풀스택 개발자입니다.",
  "프로젝트 경험": "2회 (TWICE)"
}
```
**포트폴리오:**
- Spring Boot 쇼핑몰 프로젝트
- WebSocket 실시간 채팅 앱

**테스트 가능 기능:**
- 파트너 프로필 조회
- 프로젝트 지원
- 프로젝트 초대 수락

#### 💻 최개발 (경력 많음)
```json
{
  "email": "dev2@example.com",
  "password": "password123",
  "role": "DEV",
  "nickname": "백엔드마스터",
  "자기소개": "Java/Kotlin 백엔드 전문. MSA 경험 다수.",
  "프로젝트 경험": "5회 이상 (PLUS_FIVE)",
  "현재 상태": "작업 중"
}
```
**포트폴리오:**
- MSA 기반 결제 시스템

#### 💻 강개발
```json
{
  "email": "dev3@example.com",
  "password": "password123",
  "role": "DEV",
  "nickname": "프론트엔드전문",
  "자기소개": "React/Vue 프론트엔드 개발자.",
  "프로젝트 경험": "1회 (ONCE)"
}
```

---

### 3. 디자이너 (DES) 계정

#### 🎨 정디자인
```json
{
  "email": "des@example.com",
  "password": "password123",
  "role": "DES",
  "nickname": "UI천재",
  "자기소개": "Figma/Sketch 기반 UI/UX 디자이너. 앱 디자인 전문.",
  "프로젝트 경험": "1회 (ONCE)"
}
```
**포트폴리오:**
- e-commerce 앱 UI/UX 리디자인

#### 🎨 윤디자인
```json
{
  "email": "des2@example.com",
  "password": "password123",
  "role": "DES",
  "nickname": "UX마스터",
  "자기소개": "사용자 경험 중심의 디자인 전문가.",
  "프로젝트 경험": "2회 (TWICE)"
}
```

---

### 4. 마케터 (MAR) 계정

#### 📢 한마케팅
```json
{
  "email": "mar@example.com",
  "password": "password123",
  "role": "MAR",
  "nickname": "그로스해커",
  "자기소개": "퍼포먼스 마케팅 + 브랜딩 전문가.",
  "프로젝트 경험": "0회 (ZERO)"
}
```

---

## 📊 테스트용 프로젝트 데이터

### 프로젝트 1: 펫케어 플랫폼 (진행 중)
```json
{
  "프로젝트명": "펫케어 플랫폼",
  "생성자": "김기획 (planner@example.com)",
  "상태": "IN_PROGRESS",
  "예산": "₩8,500,000",
  "기간": "2026-01-15 ~ 2026-04-15",
  "타입": "APP",
  "설명": "반려동물 건강 관리 및 수의사 상담 매칭 서비스",
  "모집 중인 역할": [
    "개발자 (DEV) 2명 - ₩3,000,000",
    "디자이너 (DES) 1명 - ₩2,500,000",
    "마케터 (MAR) 1명 - ₩2,000,000"
  ]
}
```

### 프로젝트 2: 스터디 매칭 서비스 (진행 중)
```json
{
  "프로젝트명": "스터디 매칭 서비스",
  "생성자": "이기획 (planner2@example.com)",
  "상태": "IN_PROGRESS",
  "예산": "₩5,000,000",
  "기간": "2026-02-01 ~ 2026-05-01",
  "타입": "SERVICE",
  "설명": "개발자/디자이너 스터디 그룹 매칭 웹 서비스"
}
```

### 프로젝트 3: 헬스케어 앱 (완료)
```json
{
  "프로젝트명": "헬스케어 앱",
  "생성자": "김기획 (planner@example.com)",
  "상태": "COMPLETED",
  "예산": "₩7,000,000",
  "기간": "2025-09-01 ~ 2025-12-31",
  "타입": "APP",
  "설명": "운동 기록 및 식단 관리 모바일 앱"
}
```

---

## 🧪 테스트 시나리오 예시

### 시나리오 1: 기획자 플로우
```javascript
// 1. 김기획으로 로그인
const loginRes = await axios.post('/auth/login', {
  email: 'planner@example.com',
  password: 'password123'
});
const token = loginRes.data.data.accessToken;

// 2. 내 프로젝트 조회
const myProjects = await axios.get('/members/me/projects', {
  headers: { Authorization: `Bearer ${token}` }
});

// 3. 펫케어 플랫폼 상세 조회 (projectId 확인 후)
const projectDetail = await axios.get('/projects/1', {
  headers: { Authorization: `Bearer ${token}` }
});

// 4. 새 태스크 생성
const newTask = await axios.post('/projects/1/tasks', {
  title: '백엔드 API 설계',
  description: 'REST API 명세서 작성',
  assigneeId: null,
  miniTaskList: [
    { content: 'ERD 설계', isCompleted: false },
    { content: 'API 문서 작성', isCompleted: false }
  ]
}, {
  headers: { Authorization: `Bearer ${token}` }
});
```

### 시나리오 2: 개발자 플로우 (지원하기)
```javascript
// 1. 박개발로 로그인
const loginRes = await axios.post('/auth/login', {
  email: 'dev@example.com',
  password: 'password123'
});
const token = loginRes.data.data.accessToken;

// 2. 홈 화면 조회 (추천 프로젝트 확인)
const home = await axios.get('/home?page=0&size=10', {
  headers: { Authorization: `Bearer ${token}` }
});

// 3. 펫케어 플랫폼에 지원
const application = await axios.post('/projects/1/applications', {
  message: '백엔드 개발 지원합니다. 포트폴리오 확인 부탁드립니다.',
  roleId: 'DEV'
}, {
  headers: { Authorization: `Bearer ${token}` }
});
```

### 시나리오 3: 채팅 테스트
```javascript
// 1. 로그인 후 채팅방 목록 조회
const chatRooms = await axios.get('/chat/rooms?type=INDIVIDUAL', {
  headers: { Authorization: `Bearer ${token}` }
});

// 2. 새로운 채팅 시작
const newChat = await axios.post('/chat/rooms/first', {
  opponentId: 2,  // 받는 사람 ID
  content: '안녕하세요! 프로젝트 관련해서 문의드립니다.'
}, {
  headers: { Authorization: `Bearer ${token}` }
});
```

---

## 📋 빠른 테스트 체크리스트

### ✅ 인증 기능
- [ ] 회원가입 (신규 계정 생성)
- [ ] 로그인 (위 테스트 계정들)
- [ ] 내 프로필 조회
- [ ] 프로필 수정

### ✅ 프로젝트 기능
- [ ] 홈 화면 조회
- [ ] 프로젝트 견적 미리보기
- [ ] 프로젝트 생성
- [ ] 프로젝트 상세 조회
- [ ] 프로젝트 소개 수정
- [ ] 프로젝트 완료 처리

### ✅ 파트너 매칭 기능
- [ ] 파트너 프로필 조회
- [ ] 프로젝트 지원
- [ ] 파트너 제안 보내기
- [ ] 제안 수락하기

### ✅ 태스크 기능
- [ ] 태스크 생성
- [ ] 태스크 수정
- [ ] 태스크 삭제
- [ ] 할일 완료 토글

### ✅ 채팅 기능
- [ ] 채팅방 목록 조회
- [ ] 채팅방 메시지 조회
- [ ] 메시지 전송
- [ ] 첫 메시지 및 방 생성

### ✅ 알림 기능
- [ ] 알림 목록 조회
- [ ] 알림 읽음 처리

### ✅ 결제 기능
- [ ] 결제 준비
- [ ] 결제 확인

---

## 🚨 참고사항

1. **JWT 토큰 만료 시간**: 1시간 (3600초)
2. **CORS 설정**: `localhost:5173`, `localhost:3000`, `offf.kro.kr` 허용됨
3. **Authorization 헤더**: `Bearer {token}` 형식으로 전송
4. **날짜 형식**: `yyyy-MM-dd` (예: `2026-01-15`)
5. **enum 값**: 대문자 사용 (`DEV`, `ZERO`, `PM` 등)

---

## 💡 문제 해결

### CORS 에러가 발생하는 경우
- 배포 서버가 최신 버전인지 확인
- `Origin` 헤더가 올바른지 확인

### 401 Unauthorized 에러
- JWT 토큰이 만료되었을 수 있음 → 재로그인
- Authorization 헤더 형식 확인: `Bearer {token}`

### 500 Internal Server Error
- 요청 데이터 형식 확인 (특히 날짜, enum 값)
- 필수 필드 누락 확인

---

## 📞 문의

API 관련 문의사항은 백엔드 팀에게 연락 주세요.

**Happy Testing! 🚀**
