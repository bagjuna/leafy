# 🌿 Leafy - 식물 관리 및 커뮤니티 서비스

Leafy는 식물을 키우는 사용자들을 위한 관리 및 소통 플랫폼입니다.
Spring Boot와 Vue.js를 기반으로 구축되었으며, Docker를 활용한 컨테이너 환경에서 동작합니다.

## 🛠 Tech Stack

### Backend
* **Java 17 / Spring Boot 3.x**
* **JPA / Hibernate**
* **Spring Security (JWT)**

### Frontend
* **Vue.js 3 / Vite**
* **TailwindCSS**

### Infrastructure & Database
* **PostgreSQL 15 (Bitnami)**: Non-root 권한 실행으로 보안 강화
* **Redis 7**: 캐싱 및 세션 관리
* **Docker & Docker Compose**: 로컬 개발 환경 표준화
* **AWS EC2 & GitHub Actions**: CI/CD 자동화 배포

---

## 🚀 Getting Started (Local Development)

이 프로젝트는 `docker-compose`를 통해 로컬에서 즉시 실행 가능한 환경을 제공합니다.

### 1. Prerequisites
* Docker Desktop (또는 Docker Engine)
* Docker Compose

### 2. Installation
프로젝트를 클론하고 프로젝트 디렉토리로 이동합니다.
```bash
git clone [https://github.com/YOUR_REPO/leafy.git](https://github.com/YOUR_REPO/leafy.git)
cd leafy
```


### 3. Environment Configuration
보안을 위해 .env 파일은 포함되어 있지 않습니다. 예제 파일(.env.example)을 복사하여 .env 파일을 생성하고, 필요한 값을 채워주세요.

```bash
cp .env.example .env
```

Note: 로컬 테스트 시 .env 파일 내의 값들을 그대로 사용해도 무방합니다. (비밀번호 등은 로컬용 기본값이 설정되어 있습니다.)

---
### 4. Run Application
Docker Compose를 사용하여 백엔드, 프론트엔드, DB, Redis를 한 번에 실행합니다.

```bash
docker-compose up -d
```

실행이 완료되면 아래 주소로 접속할 수 있습니다.

Frontend: http://localhost

Redis Commander: http://localhost:8082 (Redis GUI)

Vue.js Dev Server: http://localhost:5173 (leafy-frontend에서 실행 시키면 접속 가능)

---
### 5. Stop Application
```bash
docker-compose down
```

🔒 Security & Architecture Decisions
이 프로젝트는 `보안(Security)`과 `개발자 경험(DX)`을 최우선으로 고려하여 인프라를 설계했습니다.

####  1) Database Security (Bitnami & Localhost Binding)
   Bitnami Image 사용: 공식 이미지 대신 bitnami/postgresql 이미지를 사용하여 컨테이너가 Non-root 권한으로 실행되도록 설정했습니다. 이는 컨테이너가 해킹되더라도 호스트 시스템에 미치는 영향을 최소화합니다.

127.0.0.1 바인딩: 개발 환경(docker-compose)에서 DB 포트(5432)는 오직 127.0.0.1 (Localhost)에서만 접근 가능하도록 제한했습니다. 외부 네트워크에서의 무단 DB 접속 시도를 원천 차단합니다.

#### 2) Environment Variable Management
   Secrets 분리: 민감한 정보(DB 비밀번호, JWT 키 등)는 코드에 하드코딩하지 않고 .env 파일과 GitHub Actions Secrets를 통해 주입받도록 설계했습니다.

Spring Boot Override: initial-setup.yml을 통해 배포 시점에 환경변수를 주입하여, application.properties 설정보다 우선순위를 갖도록 구성했습니다.

#### 3) CI/CD Pipeline
   GitHub Actions를 사용하여 빌드 및 배포 과정을 자동화했습니다.

`initial-setup` : 워크플로우를 통해 EC2 서버의 초기 세팅(Docker 설치, 볼륨 설정 등)을 원격으로 제어합니다.

`leafy-backend-build-and-push.yml` : 워크플로우를 통해 백엔드 애플리케이션을 빌드하고, 도커 이미지를 생성하여 EC2 서버에 배포합니다.

`leafy-frontend-build-and-push.yml` : 워크플로우를 통해 프론트엔드 애플리케이션을 빌드하고, 도커 이미지를 생성하여 EC2 서버에 배포합니다.

`postgres-build.yml` : postgres의 설정을 변경될 때마다 이미지를 빌드하고 배포합니다. (자주 변경되지 않음)

`redis-build.yml` : redis 이미지를 빌드하고 배포합니다. (자주 변경되지 않음)


