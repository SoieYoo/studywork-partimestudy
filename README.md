# StudyWork 백엔드 과제

## 개요

이 프로젝트는 Java, Spring Boot, JPA를 사용하여 StudyWork 서비스의 백엔드 API를 구현한 것입니다. 사용자 등록, 인증, 챌린지 관리, 사용자 챌린지 등록 및 일정 관리와 같은 주요 기능을 포함하고 있습니다.

## 프로젝트 구조
```
studywork-partimestudy/
├── src/
│ ├── main/
│ │ ├── java/com/partimestudy/
│ │ │ ├── config/ # 구성 클래스 (예: Swagger, Security)
│ │ │ ├── controller/ # HTTP 요청을 처리하는 REST 컨트롤러
│ │ │ ├── dto/ # 데이터 전송 객체 (DTO)
│ │ │ ├── entity/ # 데이터베이스 테이블을 나타내는 JPA 엔티티
│ │ │ ├── exception/ # 커스텀 예외 및 글로벌 예외 처리기
│ │ │ ├── repository/ # 데이터베이스 상호작용을 위한 JPA 리포지토리
│ │ │ ├── service/ # 비즈니스 로직이 포함된 서비스 레이어
│ │ │ ├── util/ # 유틸리티 클래스 (예: JWT 유틸리티)
│ │ │ └── StudyworkApplication.java # 메인 Spring Boot 애플리케이션 클래스
│ ├── test/ # 유닛 및 통합 테스트 파일
│ │ ├── java/com/partimestudy/
│ │ ├── resources/ # 테스트 리소스 및 애플리케이션 프로퍼티
├── build.gradle # Gradle 빌드 파일
├── README.md # 프로젝트 문서
└── ...
```

### ERD
![partimestudy (1)](https://github.com/user-attachments/assets/dafdf79a-5a40-4d51-a9e2-aa5aca347826)


### **구현 세부 사항**

#### **사용자 등록 (회원가입)**- `JoinService.java`에 구현되었습니다.
- 사용자는 `username`, `password`, `nickname`, `studyGoal`을 제공하여 등록할 수 있습니다.
- 비밀번호는 BCrypt를 사용하여 암호화된 후 데이터베이스에 저장됩니다.
- 사용자 이름의 중복 등록을 방지하기 위한 유효성 검사가 포함되어 있습니다.

#### **사용자 인증 (로그인)**- `LoginFilter.java`에 구현되었습니다.
- 사용자는 `username`과 `password`를 사용하여 로그인할 수 있습니다.
- 인증에 성공하면 JWT 토큰이 발급됩니다.
- 인증 실패 시 적절한 오류 메시지가 반환됩니다.

#### **챌린지 관리**- `ChallengeService.java`에 구현되었습니다.
- 관리자는 챌린지를 생성할 수 있으며, 챌린지 이름, 보증금 한도, 상태 등을 정의할 수 있습니다.
- 챌린지는 `ACTIVE`, `COMPLETED`와 같은 상태를 가질 수 있으며, 상태에 따라 사용자가 참여할 수 있습니다.

#### **사용자 챌린지 등록**- `UserChallengeService.java`에 구현되었습니다.
- 사용자는 한 번에 하나의 챌린지에만 등록할 수 있습니다.
- 등록 시 보증금과 선택한 챌린지에 대한 정보가 포함됩니다.
- 사용자가 동일한 챌린지에 중복 등록하거나 비활성 상태의 챌린지에 등록하는 것을 방지하는 유효성 검사가 포함되어 있습니다.

#### **챌린지 일정 관리**- `ChallengeSchedule.java`에 구현되었습니다.
- 사용자는 챌린지 등록 시 학습 일정을 정의하고 관리할 수 있습니다.
- 일정은 학습 날짜(`applyDate`)와 학습 시간(`hours`)을 포함하며, 데이터베이스에 저장됩니다.

### **설정 방법**

1. **저장소 클론:** git 클론 https://github.com/yourusername/studywork-parttimestudy.git
2. **프로젝트 빌드:** ./gradlew clean build
3. **애플리케이션 실행:** ./gradlew bootRun
4. **Swagger UI 접근:**- 애플리케이션을 시작한 후, Swagger UI에 다음 주소로 접근할 수 있습니다:
  ```
  http://localhost:8080/swagger-ui.html
  ```
### **API 문서**
API 문서는 Swagger UI 를 통해 제공됩니다.
Swagger UI: http://localhost:8080/swagger-ui.html에서 이용할 수 있습니다.

### **오류 처리**
커스텀 예외를 사용하여 USER_NOT_FOUND, ALREADY_EXIST_USERNAME, INVALID_DEPOSIT와 같은 다양한 오류 상황을 처리합니다.
이러한 예외는 GlobalExceptionHandler.java에 의해 글로벌하게 처리되어 API 클라이언트에게 의미 있는 오류 메시지를 반환합니다.

### **기술 스택**
- Java 17: 프로그래밍 언어.
- Spring Boot 3: 애플리케이션 구축을 위한 프레임워크.
- Spring Security: 인증 및 권한 관리를 위한 프레임워크.
- JPA/Hibernate: 데이터베이스 상호작용을 위한 ORM.
- Swagger/OpenAPI: API 문서화를 위한 도구.
- JUnit 5: 유닛 및 통합 테스트를 위한 프레임워크.
- Gradle: 빌드 도구.
  
### **추가 구현 예정 기능**
- 역할 기반 접근 제어: 현재는 기본 사용자 역할만 구현되어 있습니다. 향후에는 다양한 엔드포인트에 대한 복잡한 역할 관리 기능을 추가할 수 있습니다.
- 알림 시스템: 사용자의 챌린지 일정에 대한 알림 또는 리마인더를 보내는 기능을 추가할 수 있습니다.
