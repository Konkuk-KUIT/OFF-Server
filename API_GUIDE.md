# OFF API 가이드 (프론트엔드용)

## 🌐 서버 정보

### 개발 서버
```
Base URL: http://YOUR_EC2_IP:8080
Swagger UI: http://YOUR_EC2_IP:8080/swagger-ui/index.html
```

## 🔐 인증 (JWT)

### 1. 회원가입
```http
POST /members/signup
Content-Type: application/json

{
  "name": "홍길동",
  "email": "test@example.com",
  "password": "password123",
  "nickname": "테스터",
  "selfIntroduction": "안녕하세요",
  "birth": "1995-01-01",
  "role": "DEV",
  "projectCount": "ZERO"
}
```

### 2. 로그인
```http
POST /members/login
Content-Type: application/json

{
  "email": "test@example.com",
  "password": "password123"
}

Response:
{
  "success": true,
  "code": 200,
  "message": "성공",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### 3. API 호출 시 인증
모든 API 호출 시 헤더에 토큰 포함:
```http
Authorization: Bearer {accessToken}
```

## 🧪 테스트 계정

더미 데이터로 로그인 가능한 계정들:

| 이메일 | 역할 | 비밀번호 (원본) |
|--------|------|----------------|
| planner@example.com | PM | password123* |
| dev@example.com | DEV | password123* |
| des@example.com | DES | password123* |
| mar@example.com | MAR | password123* |

*주의: 실제 DB에는 bcrypt 해시값으로 저장되어 있습니다. 회원가입 API로 새 계정을 만드는 것을 권장합니다.

## 💬 WebSocket (채팅)

### 연결
```javascript
const socket = new SockJS('http://YOUR_EC2_IP:8080/ws');
const stompClient = Stomp.over(socket);

// 연결 시 JWT 토큰 전달
const headers = {
  'Authorization': `Bearer ${token}`
};

stompClient.connect(headers, (frame) => {
  console.log('Connected: ' + frame);
});
```

### 구독 (메시지 수신)
```javascript
// 프로젝트 채팅방
stompClient.subscribe('/sub/chat/room/' + roomId, (message) => {
  const received = JSON.parse(message.body);
  console.log(received);
});

// 개인 메시지
stompClient.subscribe('/user/queue/messages', (message) => {
  const received = JSON.parse(message.body);
  console.log(received);
});
```

### 메시지 전송
```javascript
const message = {
  content: "안녕하세요!"
};

stompClient.send(
  '/pub/chat/message/' + roomId,
  {},
  JSON.stringify(message)
);
```

## 📌 주요 비즈니스 로직

### 프로젝트 생성 플로우
1. `/projects` POST - 프로젝트 생성
2. `/projects/{id}/recruits` POST - 파트너 모집 공고 생성
3. 다른 사용자가 `/applications` POST - 지원
4. 프로젝트 생성자가 `/applications/{id}/accept` POST - 승인
5. `/payments/ready` POST - 결제 준비
6. 토스 페이먼츠 결제 완료 후 `/payments/confirm` POST
7. 결제 완료 시 자동으로 프로젝트 멤버 추가

### 권한 체크
- **프로젝트 멤버만** 가능:
  - 채팅
  - 태스크 생성/수정
  - 프로젝트 상세 조회

- **프로젝트 생성자만** 가능:
  - 파트너 지원 승인/거절
  - 프로젝트 완료 처리

## 🚨 에러 처리

모든 API 응답은 다음 형식:
```json
{
  "success": true/false,
  "code": 200/400/403/404/500,
  "message": "에러 메시지",
  "data": null
}
```

주요 에러 코드:
- `400`: 잘못된 요청
- `403`: 권한 없음
- `404`: 리소스 없음
- `500`: 서버 에러

## 📚 참고 자료

- Swagger UI: http://YOUR_EC2_IP:8080/swagger-ui/index.html
- API Docs JSON: `/v3/api-docs`

## 💡 개발 팁

1. **JWT 토큰 만료**: 1시간 (3600000ms)
2. **파일 업로드**: 현재 미지원 (URL 문자열로 저장)
3. **날짜 형식**: `yyyy-MM-dd` (LocalDate)
4. **타임스탬프**: ISO 8601 형식 자동 생성

## 🤝 문의

API 관련 문의사항은 백엔드 팀에게 연락 주세요.