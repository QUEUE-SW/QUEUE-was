## 🧭 소개
QUEUE-was는 [QUEUE-SW](https://github.com/QUEUE-SW) 프로젝트의 백엔드 서버로,
대규모 트래픽 환경에서도 안정적인 수강신청을 지원합니다.
JMeter 기반 성능 테스트를 통해 병목 구간을 개선하며, 6단계 아키텍처 고도화를 거쳐
Redis·SSE 기반 실시간 피드백과 확장성 있는 대기열 처리를 제공하는 것이 특징입니다.

> 본 프로젝트는 과학기술정보통신부 주최 한이음 드림업 공모전에 출품된 작품으로, 기업 전문가 멘토와 협업하여 기획·개발하였습니다.

<br>

## 🛠️ 기술 스택

|영역            |사용 기술              |
|----------------|-----------------------|
|**언어**        |Java 17                |
|**프레임워크**  |Spring Boot 3.2.3      |
|**DB/Cache**    |MySQL 8.0.41, Redis 7.1|
|**인프라**      |AWS EC2, Docker, Nginx, RDS, ElastiCache, GitHub Actions|
|**성능 테스트** |JMeter                 |
|**협업 도구**   |Jira, Confluence, Slack|

<br>

## 💡 주요 기능
- **대기열 기반 수강신청**  
  - Polling / SSE / Redis Pub/Sub 구조를 실험하며 최적화  
  - 순번별 입장 및 좌석 관리 기능 제공

- **실시간 피드백**  
  - 사용자 대기열 순번 정보를 실시간 제공  
  - 과목 여석 변화를 Redis와 SSE로 즉시 반영 후 제공

- **성능 최적화**  
  - JMeter로 1,000명 ~ 10,000명 동시 접속 시뮬레이션  
  - 병목 구간을 분석하여 단계별 아키텍처 고도화 수행
    
<br>

## ⚙ 시스템 아키텍처
본 시스템은 단순 MVC 구조에서 출발해 **JMeter 성능 테스트 결과**를 기반으로 병목 구간을 분석하고 개선하는 과정을 거쳤습니다.  
그 결과 *총 6단계 아키텍처 고도화*를 통해 현재의 안정적인 구조로 발전했습니다.

<img width="895" height="523" alt="image (3)" src="https://github.com/user-attachments/assets/516d0f5d-bdf9-4ed1-a05f-1a011b9d35a8" />

<br>
<br>

## 🧪 성능 테스트
본 프로젝트는 JMeter를 활용해 **1,000명 ~ 10,000명 동시 접속 환경**을 시뮬레이션하며 성능을 검증했습니다.  
단순 부하 테스트가 아닌 **실제 대학 수강신청 과정에서 발생할 수 있는 시나리오**를 기반으로 테스트 케이스를 설계했습니다.

<p align="left">
  <img src="https://github.com/user-attachments/assets/b94a5795-72f6-481b-9e30-10aae68377cc" width="450"/>
  <img src="https://github.com/user-attachments/assets/e51243d2-d8e0-4f3e-9c46-7726ae31471a" width="250"/>
</p>


## 🗄️ERD
<img width="947" height="437" alt="3a3421a0-b5f4-4a0d-8f78-799197810ae3" src="https://github.com/user-attachments/assets/c9b43279-51ed-4871-ac01-94c55b5c156f" />

<br>
<br>
