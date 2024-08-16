# WeatherWear
<img src="https://file.notion.so/f/f/83c75a39-3aba-4ba4-a792-7aefe4b07895/8d75ca45-6c1b-438b-aeb1-d7ee14b6a3a0/1.jpg?table=block&id=bf1f7ee6-9206-4982-b7bf-60211dc05a98&spaceId=83c75a39-3aba-4ba4-a792-7aefe4b07895&expirationTimestamp=1723881600000&signature=Ij18XWOmws8w_h_mYDgxVgdk-mViF7Kz7sjuMYWTXow&downloadName=1.jpg" width="700px;" alt=""/>

## 🌈 팀원 소개 BACK-END
<table>

  <tbody>
    	<tr>
		  <td align="center">프로필</td>
	          <td align="center">이메일</td>
		<td align="center">담당 업무</td>
		  <td align="center" colspan="2">개발 기간</td>
	 </tr>	 
    <tr>
      <td align="center">
	      <a href="https://github.com/seunghyeonlee9661" target="_blank">
	      <img src="" width="200px;" alt=""/>
	      <br />
	      <sub><b>이승현(Team Leader)</b></sub>
	      </a>
	      <br />
       	      </td>
        <td align="left">
	   dltmdgus9661@gmail.com
     	    </td>
      <td align="left">
	    :purple_heart: 1 <br />
	    :purple_heart: 2 <br />
	    :purple_heart: 3 <br />
     	    </td>
      <td align="center" rowspan="5">
	    2024.07.19 ~ 2023.08.16 (약 4주)
      </td>
   <tr/>
   <tr>
      <td align="center">
	      <a href="https://github.com/HaJunyoung" target="_blank">
	      <img src="" alt=""/>
	      <br />
	      <sub><b>하준영(Team Member)</b></sub>
	      </a>
	      <br />
       	      </td>
      <td align="left">
	   arizona19973@naver.com
     	    </td>
      <td align="left">
	    :purple_heart: 게시물 & 댓글 & 좋아요 <br />
	    :purple_heart: AWS EC2 <br />
     	    </td>
   <tr/>
   <tr>
      <td align="center">
	      <a href="https://github.com/HanBeom98" target="_blank">
	      <img src="" alt=""/>
	      <br />
	      <sub><b>조한범(Team Member)</b></sub>
	      </a>
	      <br />
       	      </td>
      <td align="left">
	   carry9691@gmail.com
     	    </td>
      <td align="left">
	    :purple_heart: 1 <br />
	    :purple_heart: 2 <br />
     	    </td>
   <tr/>
  </tbody>
</table>


### 💚 개발 목표
<ul>
	<li>오늘의 날씨 데이터, 내 옷장에 등록된 옷, 비슷한 날씨에 내가 입었던 옷차림, 다른 사용자의 옷차림 데이터를 기반으로 나만의 맞춤 옷차림을 추천</li>
	<li>사용자의 OOTD(outfit of the day, 오늘의 옷 차림)를 등록 & 공유</li>
	<li>원하는 정보를 쉽게 찾고 검색 경험을 더욱 만족스럽게 느낄 수 있도록 각 페이지에 맞는 키워드로 검색, 날씨 아이콘으로 검색, 옷 종류-컬러로 검색하는 기능을 제공</li>
	<li>이후 실시간 알림 & 상황 별 ootd 추천 & 특정 날짜의 옷 추천 기능 추가 예정</li>
	<li>성능 개선 -> 부하 테스트로 안정적인 서버 동작</li>
</ul>
		
### 💚 기술 스택 

1. 스프링부트
2. JPA
3. Java
4. Gradle
5. Spring Security
6. NGNIX
7. Route 53
8. ELB
9. EC2
10. Github
11. Github Actions
12. S3
13. CodeDeploy
14. postman
15. Redis
16. MySQL
17. Amazon RDS

### 💚 주요 기능 (내용 작성 예정 & List로 변환 예정)
<ol>
  <li><strong>유저 기능</strong>
    <ol>
      <li>로그인 & 회원가입</li>
      <li>Token</li>
      <li>위시리스트</li>
      <li>사용자 맞춤 옷 추천</li>
      <li>비밀번호 찾기</li>
    </ol>
  </li>
  <li><strong>날씨 정보</strong>
    <ol>
      <li>기상청 Open API 호출</li>
    </ol>
  </li>
  <li><strong>게시물 기능</strong>
    <ol>
      <li>게시판 CRUD
        <ul>
          <li>로그인 여부에 따라 보여주는 정보 분기문 작성</li>
          <li>로그인 여부에 따라 보여주는 정보 분기문 작성</li>
          <li>로그인 여부에 따라 보여주는 정보 분기문 작성</li>
        </ul>
      </li>
      <li>검색 기능</li>
      <li>조회수 기능</li>
      <li>게시판 댓글</li>
      <li>게시판 좋아요 기능</li>
    </ol>
  </li>
</ol>
 



### 💚 ERD
<img src="https://file.notion.so/f/f/580978a3-c9a7-47f5-bafb-ccd33c1fd74a/4e80565c-8046-42a7-a0dd-56f709b75f05/Untitled.png?table=block&id=4f08046d-8ed4-45e1-969a-b1c9f2bc6820&spaceId=580978a3-c9a7-47f5-bafb-ccd33c1fd74a&expirationTimestamp=1723874400000&signature=6GBXjW6ONfIF7Zedq18daQZ1EwQKR63HHG8omMPbFCI&downloadName=Untitled.png" width="800px;" alt=""/>

### 💚 성능 측정
# Weather API 성능 테스트 결과 및 개선 방안

![Performance Comparison](images/output.png)

## 1. 테스트 개요

- **테스트 대상**: 웹 애플리케이션의 Weather API
- **테스트 툴**: Apache JMeter 5.6.3
- **테스트 목표**: 1000명의 동시 사용자가 접속할 때 시스템의 성능을 측정하여, 응답 시간, 처리량, 오류율 등을 분석하고 시스템의 성능 병목 및 한계점을 파악

## 2. 주요 성능 지표 분석

### 2.1 응답 시간 (Response Time)
- **평균 응답 시간**:
  - JSR223 Sampler: 0ms (대부분의 요청이 매우 짧은 시간 내에 처리됨)
  - HTTP Request: 984ms (상대적으로 긴 응답 시간)
- **최소/최대 응답 시간**:
  - JSR223 Sampler: 최소 0ms / 최대 35ms
  - HTTP Request: 최소 34ms / 최대 14971ms

**분석**: JSR223 Sampler는 매우 짧은 시간 내에 요청을 처리했지만, HTTP Request의 최대 응답 시간이 14971ms로 매우 길게 나타났습니다. 이는 특정 요청에서 성능 병목이 발생했을 가능성이 있습니다.

### 2.2 처리량 (Throughput)
- **처리량**:
  - JSR223 Sampler: 10.1/min
  - HTTP Request: 10.0/min
  - **총합**: 19.9/min

**분석**: 처리량은 각각의 샘플러에서 약 10건의 요청이 분당 처리되고 있음을 나타냅니다. 전체적으로 시스템은 동시 접속자가 많은 상황에서도 안정적으로 처리량을 유지하고 있으나, 특정 요청에서 처리량이 다소 낮아질 수 있습니다.

### 2.3 오류율 (Error Rate)
- **오류율**:
  - HTTP Request: 0.1%
  - **전체 오류율**: 0.05%

**분석**: HTTP Request에서 0.1%의 오류율이 기록되었습니다. 이는 1000개의 요청 중 1개가 실패했음을 의미합니다.

### 2.4 응답 시간의 변동성
- **응답 시간의 변화**: 그래프 결과에서 HTTP Request의 응답 시간 분포를 확인한 결과, 특정 구간에서 응답 시간이 급격히 증가한 패턴이 나타났습니다.
- **표준편차 (Deviation)**: HTTP Request의 표준편차는 1841.79ms로, 응답 시간의 일관성이 떨어지는 것으로 분석됩니다.

## 3. 성능 문제 분석

### 3.1 응답 시간
HTTP Request의 평균 응답 시간이 984ms로 상대적으로 긴 응답 시간을 기록했으며, 최대 응답 시간은 14971ms로 매우 길게 나타났습니다. 이는 시스템에서 특정 요청이 처리되는 동안 성능 병목이 발생하고 있다고 생각됩니다.

### 3.2 처리량
HTTP Request의 처리량은 10.0/min으로 동시 접속자가 많은 상황에서는 다소 낮을 수 있습니다. 이는 특정 구간에서의 성능 저하로 인해 처리량이 감소했을 가능성이 있습니다.

### 3.3 오류율
HTTP Request에서 0.1%의 오류율이 기록되었습니다. 1000개의 요청 중 1개가 실패했음을 의미하며, 이는 안정성 측면에서 개선이 필요한 부분입니다.

## 4. 개선 방안 제안

### 4.1 응답 시간 개선 방안

#### 1. 캐싱 도입 (Redis 사용)
- **개선 내용**: 자주 조회되는 데이터를 Redis를 사용하여 캐싱합니다. 이를 통해 데이터베이스에 대한 요청을 줄이고, 응답 시간을 단축할 수 있습니다.
- **예상 효과**: 평균 응답 시간이 약 50% 감소하여 492ms 정도로 개선될 수 있습니다. 최대 응답 시간은 70% 이상 줄어들어 4500ms 이하로 감소할 것으로 예상됩니다.

#### 2. 쿼리 최적화
- **개선 내용**: 데이터베이스 쿼리의 성능을 분석하고, 인덱싱 등을 통해 최적화 작업을 수행합니다.
- **예상 효과**: 요청 처리 속도가 개선되어 평균 응답 시간과 최대 응답 시간이 더 줄어들 수 있습니다.

#### 3. 비동기 처리 도입
- **개선 내용**: 비동기 처리를 통해 긴 시간이 소요되는 작업을 백그라운드에서 처리하여 사용자 응답 시간을 단축합니다.
- **예상 효과**: 응답 시간의 변동성이 줄어들고, 표준편차가 1000ms 이하로 감소하여 일관된 성능을 제공할 수 있습니다.

### 4.2 처리량 개선 방안

#### 1. 캐싱 도입 (Redis 사용)
- **개선 내용**: Redis 캐싱을 통해 데이터베이스 부하를 줄이고, 동일한 데이터에 대한 중복 요청을 최소화합니다.
- **예상 효과**: HTTP Request의 처리량이 약 20% 증가하여 12.0/min 정도로 개선될 수 있습니다.

### 4.3 오류율 개선 방안

#### 1. 예외 처리 로직 개선
- **개선 내용**: 서버 측의 예외 처리 로직을 강화하여 오류 발생 가능성을 줄입니다.
- **예상 효과**: 오류율이 0.1% 미만으로 감소할 수 있습니다.

#### 2. 서비스 모니터링 도입
- **개선 내용**: 실시간 모니터링 도구(AWS)를 도입하여 오류 발생 시 빠르게 대응할 수 있도록 시스템을 구축합니다.
- **예상 효과**: 전체 시스템의 안정성이 향상될 것입니다.

## 5. 개선 후 예상 성능

- **평균 응답 시간**: 492ms (기존 대비 50% 개선)
- **최대 응답 시간**: 4500ms 이하 (기존 대비 70% 이상 개선)
- **처리량**: 12.0/min (기존 대비 20% 개선)
- **오류율**: 0.03% 이하 (기존 대비 70% 개선)





