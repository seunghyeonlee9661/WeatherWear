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
### Weather API 성능 테스트 결과 및 개선 방안

##### 1. 테스트 개요

- **테스트 대상**: Weather API
- **테스트 툴**: Apache JMeter 5.6.3
- **테스트 목표**: 1000명의 동시 사용자가 접속할 때 시스템 성능 분석

##### 2. 주요 성능 지표

- **평균 응답 시간**: 984ms → **개선 후** 492ms
- **최대 응답 시간**: 14971ms → **개선 후** 4500ms
- **처리량**: 10.0/min → **개선 후** 12.0/min
- **오류율**: 0.1% → **개선 후** 0.03%

##### 3. 성능 개선 요약

- **Redis 캐싱 도입**: 자주 조회되는 데이터 캐싱으로 응답 시간 단축
- **쿼리 최적화**: 인덱싱 및 쿼리 성능 개선
- **비동기 처리 도입**: 긴 처리 작업을 비동기로 전환
- **예외 처리 로직 개선**: 오류율 감소를 위해 예외 처리 강화
- **모니터링 도구(AWS Control Tower) 도입**: 실시간 오류 대응 시스템 구축

[**JMeter 날씨 1000명 부하**](https://www.notion.so/weatherwer-f9f4ad791994422c8b5e24af9cfcae80?pvs=4) _(링크된 상세 문서로 이동)_








