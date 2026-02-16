# OFF - 아웃소싱 프로젝트 매칭 플랫폼

## 로컬 개발 환경 설정

### 1. 필수 요구사항
- Java 21
- PostgreSQL (또는 제공된 RDS 사용)

### 2. 환경 변수 설정

프로젝트 루트에 `.env` 파일을 생성하고 다음 값들을 설정하세요:

```bash
# .env.example 파일을 복사하여 사용
cp .env.example .env
```

`.env.example` 파일에는 개발/테스트용 기본 값이 포함되어 있습니다.

**필요한 환경 변수:**
- `DB_ENDPOINT` - PostgreSQL 데이터베이스 엔드포인트
- `DB_USER` - 데이터베이스 사용자명
- `DB_PASSWORD` - 데이터베이스 비밀번호
- `GEMINI_API_KEY` - Google Gemini API 키 (프로젝트 견적 AI 기능)
- `JWT_SECRET` - JWT 토큰 서명용 시크릿 키 (256비트 이상)
- `PAYMENT_CLIENT_KEY` - Toss Payments 클라이언트 키
- `PAYMENT_SECRET_KEY` - Toss Payments 시크릿 키

### 3. 빌드 및 실행

```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew bootRun

# 테스트 실행
./gradlew test
```

### 4. API 문서

애플리케이션 실행 후 Swagger UI를 통해 API 문서를 확인할 수 있습니다:
- http://localhost:8080/swagger-ui.html

### 5. 주요 기능

- 회원 가입 및 JWT 기반 인증
- 프로젝트 등록 및 매칭
- AI 기반 프로젝트 견적 (Gemini API)
- 실시간 채팅 (WebSocket/STOMP)
- 결제 시스템 (Toss Payments)
- 알림 기능

### 6. 기술 스택

- **Backend**: Spring Boot 3.5.9, Java 21
- **Database**: PostgreSQL
- **Security**: Spring Security + JWT
- **Real-time**: WebSocket (STOMP)
- **AI**: Google Gemini API
- **Payment**: Toss Payments API
- **Documentation**: Swagger/OpenAPI

### 7. 프로젝트 구조

```
src/main/java/com/example/off/
├── domain/          # 도메인별 패키지 (member, project, chat, pay 등)
│   ├── member/
│   ├── project/
│   ├── chat/
│   └── pay/
├── common/          # 공통 기능 (예외처리, 응답, 외부 API 연동)
├── config/          # 설정 클래스
└── jwt/             # JWT 인증 필터
```

### 8. 문의

프로젝트 관련 문의사항은 이슈를 등록해주세요.