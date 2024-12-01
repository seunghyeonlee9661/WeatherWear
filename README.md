# ☂️ WeatherWear
![1 (1)](https://github.com/user-attachments/assets/d7c9202e-c07e-4521-a3a1-86305b9e09e7)

## 📄 프로젝트 소개
**WeatherWear**는 사용자 위치와 날씨를 기반으로 옷을 추천하고 이를 공유할 수 있는 서비스의 API입니다. 해당 프로젝트는 다음과 같은 목표를 달성하고자 합니다.
- **사용자 맞춤형 옷 추천**: 위치와 날씨 정보를 기반으로 최적의 옷을 추천합니다.
- **Restful API 구축 및 클라이언트-서버 분리**: 유연한 시스템 설계로 유지보수성을 높입니다.
- **AWS 기반 안정성 및 확장성 확보**: AWS 인프라를 활용해 안정적이고 확장 가능한 시스템을 제공합니다.
- **트래픽 분산과 자동 확장**: AWS **ELB**와 **Auto Scaling**을 통해 트래픽을 분산하고 자원을 자동 확장합니다.
- **성능 개선을 위한 캐시**: **AWS ElastiCache(Redis)**로 응답 시간을 단축하고 성능을 향상시킵니다.
- **API 문서화 및 팀 통합**: **Swagger**로 API 명세서를 자동 생성하여 팀 간 협업을 지원합니다.

[![WeatherWear](https://img.shields.io/badge/-WeatherWear-FFD700?style=for-the-badge&logo=weather&logoColor=white)](https://weatherwearclothing.com/) 
[![Notion](https://img.shields.io/badge/-Notion-000000?style=for-the-badge&logo=notion&logoColor=white)](https://leather-pixie-4bc.notion.site/WeatherWear-313dbd50df7c47eaacb3f3622c551905?pvs=74) 
[![GitHub](https://img.shields.io/badge/-GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/WeatherWearTeam) 
[![Swagger](https://img.shields.io/badge/-Swagger-85EA2D?style=for-the-badge&logo=swagger&logoColor=white)](http://weatherwearapi.com/swagger-ui/index.html)


## 🛠️ 기술 의사 결정
![image](https://github.com/user-attachments/assets/4274dc34-13f1-4cd8-ad50-4e173e3c409f)

### 주요 기술 스택
| **기술**                          | **설명**                                                                                                                                           |
|----------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------|
| **Spring Boot**                  | 빠른 개발과 간단한 설정, 다양한 연계 기술 활용 가능                                                                                                          |
| **Spring Security**              | OAuth2와 JWT를 통한 인증/인가 구현 및 보안 표준 준수                                                                                                          |
| **NGINX**                        | SSL 처리와 로드 밸런싱으로 EC2 기반 웹 애플리케이션의 보안 및 응답 속도 향상                                                                                       |
| **Postman & Swagger**            | API 요청 및 응답 테스트, 팀 간 API 통합 지원                                                                                                          |
| **AWS ElastiCache (Redis)**      | RefreshToken을 통한 JWT 취약점 보완과 캐시 구현으로 성능 개선 및 자원 절약                                                                                       |
| **AWS EC2 & ELB**                | 확장성과 가용성을 위한 서버 호스팅 및 로드 밸런싱                                                                                                           |
| **AWS S3**                       | 이미지 파일 및 배포 아티팩트 저장/관리                                                                                                                    |
| **Github Actions & AWS CodeDeploy** | CI/CD를 통한 자동 배포 및 코드 관리                                                                                                                      |
#### 1. **AWS ElastiCache vs EC2 Redis**
- 🟢 **장점**: ElastiCache는 AWS에서 간편하게 Redis 캐시를 처리할 수 있으며, AutoScaling으로 서버 확장에서 동시성 충돌을 방지합니다.  
- 🔴 **단점**: 프로젝트 규모가 작으면 고가용성 기능을 활용할 수 없고, 비용이 증가할 수 있습니다.

#### 2. **AWS S3 vs 서버 파일 시스템**
- 🟢 **장점**: AWS S3는 확장성이 뛰어나고, 데이터 손실 걱정 없이 안정적인 저장소를 제공합니다.  
- 🔴 **단점**: 보안 설정이 복잡하고, 외부 시스템과의 연동이 어려울 수 있습니다.

#### 3. **Github Actions + AWS CodeDeploy**
- 🟢 **장점**: 자동화된 배포와 빌드 과정을 지원하고, 다양한 배포 전략을 통해 유연한 배포 관리가 가능합니다.  
- 🔴 **단점**: IAM 설정이 복잡하고, 추가적인 비용이 발생할 수 있습니다.
 
## 🗂️ 프로젝트 구조
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

## 🔨 트러블 슈팅

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

## 🔗 성능 개선
### **성능 측정 및 개선**
#### 개요
- **측정 목표** : `Redis Cache`를 적용하여 추천 결과를 캐싱하고 성능 개선
- **측정 대상** : 메인 페이지에서 사용자 추천 결과 호출 기능
- **측정 도구** : `Apache JMeter 5.6.3`

#### 측정 과정
1. **기존 추천 결과 측정**
    - 1000명의 사용자가 추천 API를 10번 반복 호출하고 응답 시간을 기록
2. **기존 기능을 개선 후 측정**
    - 검색 결과를 사용자 ID를 기반으로 Redis에 저장하고 TTL 30분 적용
    - 동일한 조건으로 성능 측정 후 응답 시간 기록
3. **결과 분석**

#### 측정 결과

- **평균 응답 시간** : 984ms → **개선 후** 492ms `(+50%)`
- **최대 응답 시간** : 14971ms → **개선 후** 4500ms `(+70%)`
- **처리량** : 10.0/min → **개선 후** 12.0/min `(+20%)`
- **오류율** : 0.1% → **개선 후** 0.03% `(+70%)`

#### 결론
- **Redis 성능 개선 효과**: 캐시를 통한 응답 시간 50% 개선, 최대 응답 시간 70% 감소, 처리량 20% 증가 등 성능 향상이 확실함.
- **단점**: 캐시와 데이터 간 일관성 문제 발생 가능. 또한, 많은 사용자 요청 시 메모리 사용량 증가로 캐시 만료 위험 있음.
- **추가 개선 방향**:
    1. **일관성 문제 해결**: 새로고침 기능 및 데이터 변경 시 캐시 갱신
    2. **캐시 최적화**: 캐시 데이터 용량 줄여 메모리 절약
    3. **메모리 정책 적용**: `LRU` 또는 `LFU` 정책 적용하여 성능 저하 예방
