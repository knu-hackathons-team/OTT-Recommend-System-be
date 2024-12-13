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
<img width="554" alt="image" src="https://github.com/user-attachments/assets/5e1878a1-f09e-447e-b80b-6b745184d76d">

## Activity Diagram (비즈니스 관점)
<img width="400" alt="image" src="https://github.com/user-attachments/assets/5cfbe099-6404-4984-b21e-378d904aca88">
<img width="400" alt="image" src="https://github.com/user-attachments/assets/3bfb19aa-a0b4-4c16-8d85-f33d6610d5d5">

## Class Diagram (비즈니스 관점)
<img width="1000" alt="image" src="https://github.com/user-attachments/assets/774c98a6-3f7b-4326-bde9-14f515b11a08">

## Sequence Diagram (비즈니스 관점)

### 콘텐츠 검색
<img width="554" alt="image" src="https://github.com/user-attachments/assets/86f3005b-0bc6-45ba-a21b-9c5e413bd688">

### 콘텐츠 추천
<img width="700" alt="image" src="https://github.com/user-attachments/assets/c928c586-14ab-4629-b4ac-f49e1a52ff50">

### 콘텐츠 평가
<img width="554" alt="image" src="https://github.com/user-attachments/assets/3a8515c8-a8b6-4c11-bb35-ee87074c3670">

### 친구 등록
<img width="554" alt="image" src="https://github.com/user-attachments/assets/f0d9de81-d83f-4da0-8ed9-d05d4553be0e">

### 친구와 콘텐츠 공유
<img width="800" alt="image" src="https://github.com/user-attachments/assets/d16fb3a3-d237-4d6b-85ba-8193cd77d8de">

## ERD (코드 관점)
<img width="779" alt="image" src="https://github.com/user-attachments/assets/7e413630-c062-458c-92ff-178c684f97c3">

## API 명세서
### Swagger 링크
### [http://ott.knu-soft.site/swagger-ui/index.html](http://ott.knu-soft.site/swagger-ui/index.html#/)

## 웹 서버 아키텍쳐
![image](https://github.com/user-attachments/assets/cab60980-9e15-44f4-91b9-37c761688055)
