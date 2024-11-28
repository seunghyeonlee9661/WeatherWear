# WeatherWear
![1 (1)](https://github.com/user-attachments/assets/d7c9202e-c07e-4521-a3a1-86305b9e09e7)

## 목차
1. [📄 프로젝트 소개](#프로젝트-소개)
2. [🛠️ 기술 스택](#기술-스택)
3. [🗂️ 프로젝트 구조](#프로젝트-구조)
4. [🌟 주요 기능](#주요-기능)
5. [📑 API 문서](#api-문서)
6. [🧪 테스트](#테스트)
7. [🚀 배포](#배포)

## 📄 프로젝트 소개
**WeatherWear**에 오신 것을 환영합니다! 이 프로젝트는 2024년 스파르타 코딩클럽 이노베이션 캠프의 팀 프로젝트입니다.

**WeatherWear**는 사용자의 위치에 따라 날씨 데이터를 분석하고, 이를 바탕으로 적절한 옷차림을 추천하는 혁신적인 시스템입니다.

**주요 기능**:
- 🎯 **맞춤형 옷차림 추천**: 오늘의 날씨, 개인적인 옷장, 비슷한 과거의 옷차림, 다른 사용자들의 데이터를 바탕으로 일일 추천 옷차림 제공.
- 🗣️ **OOTD 공유**: 오늘의 옷차림(Outfit of the Day)을 등록하고 공유할 수 있습니다.
- 🔍 **검색 기능**: 키워드, 날씨 아이콘, 의류 종류, 색상 등을 사용하여 옷차림을 검색할 수 있습니다.
- 🚀 **향후 개선 사항**: 실시간 알림, 이벤트 기반 추천, 특정 날짜에 맞춘 추천 기능 등.
- ⚙️ **성능 최적화**: 로드 테스트를 통한 서버 성능 최적화.

[![WeatherWear](https://img.shields.io/badge/-WeatherWear-FFD700?style=for-the-badge&logo=weather&logoColor=white)](https://weatherwearclothing.com/) 
[![Notion](https://img.shields.io/badge/-Notion-000000?style=for-the-badge&logo=notion&logoColor=white)](https://www.notion.so/Weather-Wear-9e4122225f5d446489d14b9a028046f3) 
[![GitHub](https://img.shields.io/badge/-GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/WeatherWearTeam) 
[![Swagger](https://img.shields.io/badge/-Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white)](http://weatherwearapi.com/swagger-ui/index.html)

## 🛠️ 기술 스택
![image (2)](https://github.com/user-attachments/assets/f4e5e1f7-9609-4245-aa18-b82d43254f53)
| 기술                                                     | 설명                                               | 선택 이유                                                                                          |
|--------------------------------------------------------|--------------------------------------------------|--------------------------------------------------------------------------------------------------|
| ![Java & Spring Boot](https://img.shields.io/badge/-Java-ED8B00?style=for-the-badge&logo=java&logoColor=white) ![Spring Boot](https://img.shields.io/badge/-Spring%20Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white) | Java는 안정적이고 널리 사용되는 객체지향 프로그래밍 언어이며, Spring Boot는 Spring 프레임워크 기반의 애플리케이션 프레임워크입니다. | Java는 뛰어난 성능과 플랫폼 독립성을 제공하며, Spring Boot는 설정이 용이하고 개발 속도가 빨라 생산성을 향상시킵니다. |
| ![JPA](https://img.shields.io/badge/-JPA-6DB33F?style=for-the-badge&logo=hibernate&logoColor=white) | Java Persistence API는 객체와 데이터베이스 간의 매핑을 지원합니다. | SQL을 작성하지 않고 객체 지향 방식으로 데이터베이스 작업을 할 수 있어 코드 가독성과 유지보수가 용이합니다. |
| ![Gradle](https://img.shields.io/badge/-Gradle-02303A?style=for-the-badge&logo=gradle&logoColor=white) | 빌드 자동화 도구로 유연한 빌드 시스템과 종속성 관리를 지원합니다. | Maven보다 성능과 커스터마이징이 우수하며, 복잡한 빌드 구성을 위한 Groovy나 Kotlin DSL을 지원합니다. |
| ![Spring Security](https://img.shields.io/badge/-Spring%20Security-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white) | Spring 애플리케이션을 위한 보안 프레임워크입니다. | OAuth2와 JWT와 같은 최신 보안 표준을 지원하며, 유연한 인증 및 권한 부여 기능을 제공합니다. |
| ![NGINX](https://img.shields.io/badge/-NGINX-009639?style=for-the-badge&logo=nginx&logoColor=white) | HTTP와 HTTPS 간의 통신을 처리하는 웹 서버 및 리버스 프록시 서버입니다. | 높은 성능과 낮은 메모리 사용으로 웹 애플리케이션의 응답 시간을 개선하며, 로드 밸런싱 및 캐싱을 통해 성능을 향상시킵니다. |
| ![Github](https://img.shields.io/badge/-Github-181717?style=for-the-badge&logo=github&logoColor=white) | 코드 버전 관리 및 협업 도구입니다. | 코드 협업 및 버전 관리를 지원하며, Pull Request와 Issues 기능을 통해 효율적인 개발 프로세스를 제공합니다. |
| ![Github Actions](https://img.shields.io/badge/-Github%20Actions-2088FF?style=for-the-badge&logo=githubactions&logoColor=white) | CI/CD 파이프라인 자동화 도구입니다. | GitHub와의 통합이 용이하며, 코드 변경 사항을 자동으로 빌드하고 테스트하며 배포합니다. |
| ![Postman](https://img.shields.io/badge/-Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) | API 개발 및 테스트 도구입니다. | API 요청과 응답을 시각적으로 테스트할 수 있으며, 자동화 기능을 제공하여 개발 편의성을 높입니다. |
| ![Redis](https://img.shields.io/badge/-Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white) | 빠른 데이터 액세스 및 캐싱을 제공하는 인메모리 데이터베이스입니다. | 고성능 데이터 캐싱 및 세션 관리를 지원하며, 다른 데이터베이스에 비해 빠른 데이터 접근을 제공합니다. |
| ![MySQL](https://img.shields.io/badge/-MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white) | 오픈 소스 관계형 데이터베이스 관리 시스템입니다. | 안정적이고 성숙한 데이터베이스 시스템으로 관계형 데이터 모델을 지원하며 ACID 트랜잭션을 보장합니다. |
| ![Route 53](https://img.shields.io/badge/-AWS%20Route%2053-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS의 DNS 웹 서비스입니다. | 고가용성과 빠른 응답 시간을 제공하며, 전 세계적인 지역에서 일관된 성능을 유지합니다. |
| ![ELB](https://img.shields.io/badge/-AWS%20ELB-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS의 로드 밸런서 서비스입니다. | 트래픽을 분배하고 서버 부하를 줄여 애플리케이션의 가용성과 확장성을 높입니다. |
| ![EC2](https://img.shields.io/badge/-AWS%20EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS의 가상 서버 인스턴스 서비스입니다. | 서버 리소스를 유연하게 관리하고 고도의 확장성을 제공하며, 다양한 워크로드에 적합한 인스턴스를 선택할 수 있습니다. |
| ![S3](https://img.shields.io/badge/-AWS%20S3-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS의 객체 스토리지 서비스입니다. | 다양한 저장 클래스와 쉬운 데이터 접근을 통해 신뢰할 수 있고 확장성 있는 데이터를 저장합니다. |
| ![CodeDeploy](https://img.shields.io/badge/-AWS%20CodeDeploy-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS의 배포 자동화 서비스입니다. | 애플리케이션 배포를 자동화하여 일관된 배포 프로세스를 제공합니다. |
| ![RDS](https://img.shields.io/badge/-AWS%20RDS-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white) | AWS의 관계형 데이터베이스 서비스입니다. | 데이터베이스의 설정, 운영, 확장 등을 자동으로 관리하여 편리한 데이터베이스 운영을 지원합니다. |


## 🗂️ Project Structure
<details>
<summary>File Structure</summary>
<pre>
src
 ├── 📂main
 │    ├── 📂java
 │    └── 📂resources
 │         └── 📜application.properties
 ├── 📂board
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂repository
 │    └── 📂service
 ├── 📂clothes
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂enums
 │    ├── 📂repository
 │    └── 📂service
 ├── 📂global
 │    ├── 📂config
 │    ├── 📂dto
 │    ├── 📂filter
 │    ├── 📂handler
 │    ├── 📂security
 │    └── 📂service
 ├── 📂user
 │    ├── 📂controller
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂enums
 │    ├── 📂repository
 │    ├── 📂service
 │    └── 📂utils
 ├── 📂weather
 │    ├── 📂controller
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂repository
 │    └── 📂service
 ├── 📂wishlist
 │    ├── 📂dto
 │    ├── 📂entity
 │    ├── 📂repository
 │    └── 📂service
 └── 📜WeatherWearApplication.java
</pre>
</details>

### Domain-Specific Explanations
- **board**: Manages OOTD (Outfit of the Day) posts and related comments.
- **clothes**: Manages functionalities related to the list of clothes owned by the user.
- **global**: Handles basic functionalities such as Redis, Spring Security, JUnit, image file management, error handling, and AWS EC2 health checks.
- **user**: Manages user authorization and functionalities related to user accounts, including OAuth and external APIs.
- **weather**: Fetches weather information from the Korea Meteorological Administration API and stores it in the database.
- **wishlist**: Manages the retrieval of recommended items from the Naver Shopping API and stores them in the user's wishlist.

### 도메인 별 설명
- **board**: OOTD(Outfit of the Day) 게시물과 관련된 댓글을 관리합니다.
- **clothes**: 사용자가 소유한 옷 목록과 관련된 기능을 관리합니다.
- **global**: Redis, Spring Security, JUnit, 이미지 파일 관리, 오류 처리, AWS EC2 헬스 체크 등 기본 기능을 처리합니다.
- **user**: 사용자 인증과 사용자 계정과 관련된 기능을 관리합니다. OAuth 및 외부 API와 관련된 기능도 포함됩니다.
- **weather**: 기상청 API를 통해 날씨 정보를 가져와 데이터베이스에 저장합니다.
- **wishlist**: Naver Shopping API에서 추천된 아이템을 가져와 사용자 위시리스트에 저장합니다.

## 🌟 주요 기능

<details>
<summary>소셜 로그인</summary>
<ul>
    <li> 👉 소셜 로그인 기능을 제공하여 회원가입 시의 불편함을 덜어줍니다.</li>
</ul>
<img src="https://github.com/user-attachments/assets/4616e8be-3dd1-4e98-852c-8b15b9690300" alt="소셜 로그인 GIF">
</details>

<details>
<summary>날씨 정보</summary>
<ul>
    <li>👉 사용자의 위치를 바탕으로 날씨 정보를 확인할 수 있습니다. 카카오 맵을 통해 원하는 지역을 검색하거나 선택하여 해당 지역의 날씨 정보를 확인할 수 있습니다. 이 기능은 오늘의 날씨를 안내하고, 해당 온도에 적합한 옷을 추천해줍니다.</li>
</ul>
<img width="1424" alt="스크린샷 2024-08-17 오후 4 33 54" src="https://github.com/user-attachments/assets/f1a7d44d-0acd-49dc-abbc-a7b11d611aba">
<img width="1425" alt="스크린샷 2024-08-17 오후 4 32 28" src="https://github.com/user-attachments/assets/2b8ee837-1568-45d5-8dc5-83989e7bd566">
<img width="1428" alt="스크린샷 2024-08-17 오후 4 29 11" src="https://github.com/user-attachments/assets/bbd04119-00e6-40a6-a50e-daa7bb79aa1b">
</details>

<details>
<summary>개인화된 옷차림 추천</summary>
<ul>
    <li>👉 외출 전에 오늘의 날씨 데이터, 사용자 옷장에 등록된 옷, 비슷한 날씨에 착용한 옷, 다른 사용자의 데이터를 바탕으로 개인화된 옷차림을 추천합니다.</li>
</ul> 
<img src="https://github.com/user-attachments/assets/4fdd56ae-1c72-42d3-b8e8-74b61cdf0910">
</details>

<details>
<summary>위시리스트</summary>
<ul>
    <li>👉 Naver Shopping API를 기반으로 현재 날씨에 적합한 옷을 추천합니다. 좋아요 표시된 아이템은 위시리스트에 저장할 수 있으며, 위시리스트에서는 해당 아이템에 대한 정보와 구매 링크를 제공합니다.</li>
</ul>
<img src="https://github.com/user-attachments/assets/69808e08-26b5-4119-822d-775160f1b3c9">
</details>

<details>
<summary>게시판 & 댓글</summary>
<ul>
    <li>👉 사용자가 OOTD(Outfit of the Day)를 등록하고, 다른 사람들과 공유할 수 있습니다. 게시물에는 자유롭게 댓글을 추가할 수 있습니다.</li>
</ul>
<img src="https://github.com/user-attachments/assets/f07f940d-5523-4eab-89fb-da3aa715c71c" alt="게시판 & 댓글 GIF">
<img src="https://github.com/user-attachments/assets/1589a0d5-76b9-45d1-a504-27089fd86d36" alt="댓글 GIF">
</details>

<details>
<summary>검색 기능</summary>
<ul>
    <li>👉 키워드, 날씨 아이콘, 옷 종류/색상 등을 활용한 검색 기능으로 원하는 정보를 쉽게 찾을 수 있습니다.</li>
</ul>
<img src="https://github.com/user-attachments/assets/2cd8ae06-b5b3-4371-9660-db93f0669c9c" alt="게시판 & 댓글 GIF">
<img src="https://github.com/user-attachments/assets/5cbb9f41-6272-4ab0-94a1-ab4c3c57978a" alt="게시판 & 댓글 GIF">
</details>

## 🔨 문제 해결

<details>
  <summary><strong>WebP 변환을 통한 이미지 데이터 처리 효율성 향상</strong></summary>

  <blockquote>
    <strong>문제</strong> ❗: 대형 이미지를 불러오는 데 시간이 많이 소요되어 서비스 성능에 영향을 미침.<br>
    <strong>원인</strong> 🔍: 기존의 이미지 포맷(JPEG, PNG)은 파일 크기가 커서 이미지 로딩 시간이 느려짐.<br>
    <strong>해결책</strong> ✅: WebP 포맷을 선택하여 이미지 크기를 줄이면서 품질을 유지하고, 로딩 시간을 개선하여 서비스 성능을 향상시켰음.
  </blockquote>
</details>

<details>
  <summary><strong>RefreshToken 보안을 통한 토큰 도용 방지 및 사용자 편의성 향상</strong></summary>

  <blockquote>
    <strong>문제</strong> ❗: JWT 토큰이 쿠키에 저장되어 도난당할 경우 악용될 수 있으며, 서버에서 토큰 상태를 추적하지 않음.<br>
    <strong>원인</strong> 🔍: 토큰 만료 시간을 짧게 설정하면 로그인 빈도가 증가하고, 길게 설정하면 보안 위험이 증가함.<br>
    <strong>해결책</strong> ✅: 짧은 만료 시간을 가진 AccessToken과 Redis에 저장된 RefreshToken을 결합하여 토큰 도용 위험을 최소화하고 로그인 빈도를 줄임.
  </blockquote>
</details>

<details>
  <summary><strong>추천 아이템 캐싱을 통한 성능 개선</strong></summary>

  <blockquote>
    <strong>문제</strong> ❗: 서비스의 메인 화면에서 복잡한 추천 데이터를 반복적으로 보여주어 데이터베이스 부하와 응답 시간이 증가함.<br>
    <strong>원인</strong> 🔍: 대규모 데이터셋에 대한 복잡한 계산으로 추천 프로세스가 느려짐.<br>
    <strong>해결책</strong> ✅: Redis를 캐시로 사용하여 추천 항목을 1시간 동안 저장하고, 빠른 검색을 통해 서버 부하를 줄임.
  </blockquote>
</details>

<details>
  <summary><strong>프론트엔드 요청에서 발생한 net::ERR_CERT_COMMON_NAME_INVALID 오류</strong></summary>

  <blockquote>
    <strong>문제</strong> ❗: 프론트엔드 요청이 `net::ERR_CERT_COMMON_NAME_INVALID` 오류로 실패하여 서버에 요청이 도달하지 못함.<br>
    <strong>원인</strong> 🔍: 프론트엔드와 백엔드가 동일한 도메인을 사용하여 인증서 CN 및 경로 설정에서 문제가 발생함.<br>
    <strong>해결책</strong> ✅: 프론트엔드와 백엔드 도메인을 분리하고, 로드 밸런서와 Nginx를 설정하여 트래픽을 정확히 라우팅함으로써 요청을 정상 처리함.
  </blockquote>
</details>

<details>
  <summary><strong>프론트엔드 요청에서 발생한 CORS 문제</strong></summary>

  <blockquote>
    <strong>문제</strong> ❗: HTTPS 환경에서 백엔드에 요청을 보낼 때 CORS 오류가 발생함.<br>
    <strong>원인</strong> 🔍: 프론트엔드가 HTTPS 환경에서 HTTP 요청을 보내어 CORS 정책을 위반함.<br>
    <strong>해결책</strong> ✅: 로드 밸런서에 SSL 인증서를 설치하고, Nginx를 HTTPS 요청을 처리하도록 설정하여 CORS 문제를 해결함.
  </blockquote>
</details>

## 📑 API 문서
[Swagger UI](http://weatherwearapi.com/swagger-ui/index.html)에서 API 문서를 확인할 수 있습니다.

