# 넷플릭스 추천 서비스
---
### 서비스 이용하기 : [ott.knu-soft.site](http://ott.knu-soft.site)
---
## 주제 설명
사용자가 시청한 영상 기록과 좋아요/싫어요 평가를 기반으로, 선호할 만한 넷플릭스 영상 목록을 추천하는 알고리즘을 구현하여 제공하는 시스템입니다.

## 서비스 특징
- 데이터셋 규모: 5,332개의 넷플릭스 영상 정보 데이터셋을 활용하여 추천 알고리즘을 구현합니다.
- 영상 시청 기능 제한: 법적 문제로 인해 실제 영상을 시청하는 기능은 제공하지 않으며, 시청 기록만 서버에 남깁니다. 대신, 넷플릭스 메인 홈페이지로 이동시킵니다.
- 추천 알고리즘의 목적: 넷플릭스의 영상 추천 방식을 참고하여, 유사한 추천 알고리즘을 구축하는 것이 목표입니다.

## Usecase Diagram (비즈니스 관점)
<img width="554" alt="image" src="https://github.com/user-attachments/assets/84a31664-cb2f-48ac-bb42-764e7eda80c7">

## Class Diagram (비즈니스 관점)
<img width="554" alt="image" src="https://github.com/user-attachments/assets/64bb3fb1-d59b-45ca-9f02-5c6a3154780d">

## Activity Diagram (비즈니스 관점)
<img width="839" alt="image" src="https://github.com/user-attachments/assets/f5bcc8c9-85b6-402a-b428-d883f0a207e4">

## Sequence Diagram (비즈니스 관점)

### 검색하여 보기
<img width="554" alt="image" src="https://github.com/user-attachments/assets/aaa021c8-e656-4f6e-8120-c90af8e99151">

### 추천받아 보기
<img width="554" alt="image" src="https://github.com/user-attachments/assets/ce33b456-3a46-46c3-b194-7b0ec990a461">

### 콘텐츠 평가하기
<img width="554" alt="image" src="https://github.com/user-attachments/assets/e9c2088d-c2c1-4d92-aed5-ddd3de905ba4">

### 친구 추가
<img width="554" alt="image" src="https://github.com/user-attachments/assets/d6d1e816-52eb-4134-abe2-5c507958b836">

### 친구 삭제
<img width="554" alt="image" src="https://github.com/user-attachments/assets/1fbb77cd-01c1-4889-9b0d-c2d3c1e980af">

### 친구와 컨텐츠 공유
<img width="554" alt="image" src="https://github.com/user-attachments/assets/9c907105-f163-4a33-94fc-3314eafa5ef8">

## ERD (코드 관점)
<img width="779" alt="image" src="https://github.com/user-attachments/assets/7e413630-c062-458c-92ff-178c684f97c3">

## API 명세서
### Swagger 링크
### [http://ott.knu-soft.site/swagger-ui/index.html](http://ott.knu-soft.site/swagger-ui/index.html#/)

## 웹 서버 아키텍쳐
![image](https://github.com/user-attachments/assets/cab60980-9e15-44f4-91b9-37c761688055)
