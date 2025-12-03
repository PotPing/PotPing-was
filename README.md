# 🕳️ PotPing Backend

> **실시간 포트홀 탐지 및 자동 신고 시스템 - 백엔드 서버**
>
> 차량 단말기(Edge/YOLO)로부터 탐지된 포트홀 데이터를 수신하고, 행정 구역별 신고 접수 및 관리를 수행하는 REST API 서버입니다.

---

## 🛠️ 기술 스택 (Tech Stack)

| 구분 | 기술 | 비고 |
| :--- | :--- | :--- |
| **Language** | Java 17 | |
| **Framework** | Spring Boot 3.0+ | |
| **Database** | MySQL 8.0 | |
| **ORM** | Spring Data JPA | |
| **Docs** | Swagger (SpringDoc) | API 명세서 자동화 |
| **Build** | Gradle | |

---

## 📋 사전 요구 사항 (Prerequisites)

이 프로젝트를 로컬 환경에서 실행하려면 아래 소프트웨어가 설치되어 있어야 합니다.

1.  **JDK 17** 이상
2.  **MySQL Server 8.0** 이상
3.  **IntelliJ IDEA** (권장 IDE)

---

## 🚀 설치 및 실행 가이드 (Getting Started)

### 1. 프로젝트 클론 (Clone)
터미널에서 아래 명령어를 입력하여 프로젝트를 내려받습니다.
```bash
git clone <레포지토리_URL>
cd PotPing-was
```

### 2. 데이터베이스 생성 (MySQL Setup)
프로젝트 실행 전, 로컬 MySQL에 접속하여 사용할 데이터베이스(Schema)를 생성해야 합니다.
MySQL Workbench나 터미널에서 아래 SQL을 실행하세요.

```sql
-- 1. 데이터베이스 생성 (이름: pothole_db)
CREATE DATABASE pothole_db CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

-- 2. 생성 확인
SHOW DATABASES;
```

### 3. 환경변수 설정 (Environment Variables)
이 프로젝트는 보안을 위해 **`application.yml`에 DB 정보를 직접 적지 않고 환경변수를 사용**합니다.  
실행 전, 아래 환경변수들을 IDE(IntelliJ) 설정에 등록해야 합니다.

| 변수명 (Key) | 예시 값 (Value) | 설명 |
| :--- | :--- | :--- |
| **`DB_URL`** | `jdbc:mysql://localhost:3306/pothole_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8` | MySQL 접속 주소 (옵션 포함) |
| **`DB_USERNAME`** | `root` | 본인의 MySQL 아이디 |
| **`DB_PASSWORD`** | `1234` | 본인의 MySQL 비밀번호 |

#### 🛠️ IntelliJ에서 환경변수 설정하는 법
1.  우측 상단 실행 버튼 옆의 **`PotpingApplication`** 클릭 → **`Edit Configurations...`** 선택
2.  **Build and run** 섹션에서 **`Modify options`** 클릭 → **`Environment variables`** 체크
3.  입력창에 아래 내용을 **세미콜론(;)**으로 구분하여 한 줄로 입력 (본인 비번에 맞게 수정)
    ```text
    DB_URL=jdbc:mysql://localhost:3306/pothole_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Asia/Seoul&characterEncoding=UTF-8;DB_USERNAME=root;DB_PASSWORD=1234;UPLOAD_DIR=C:/pothole_project/images/
    ```
4.  **[OK]** 클릭 후 저장

### 4. 서버 실행 (Run)
설정이 완료되었다면 서버를 실행합니다.

* **IntelliJ:** `src/main/java/com/potping/PotpingApplication.java` 파일 열기 → `Run` 버튼(▶) 클릭
* **터미널:** `./gradlew bootRun` (Windows는 `gradlew.bat bootRun`)

## 📚 API 명세서 (Swagger)

서버가 정상적으로 실행되면 브라우저에서 아래 주소로 접속하여 API를 테스트할 수 있습니다.

👉 **[Swagger UI 바로가기](http://localhost:8080/swagger-ui/index.html)**
* 주소: `http://localhost:8080/swagger-ui/index.html`

---
