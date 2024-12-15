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
<img width="1000" alt="image" src="https://github.com/user-attachments/assets/0146ec81-8b3b-47f4-81bc-00bb28add50b">

## Sequence Diagram (비즈니스 관점)

### 콘텐츠 검색
<img width="554" alt="image" src="https://github.com/user-attachments/assets/538ea7f1-a5e8-43d1-932b-f9c14eb474a2">

### 콘텐츠 추천
<img width="700" alt="image" src="https://github.com/user-attachments/assets/958c6ed4-6ab9-4377-b07f-667758dcc133">

### 콘텐츠 평가
<img width="554" alt="image" src="https://github.com/user-attachments/assets/97251720-de68-4a58-9b3b-4205ce71744f">

### 친구 등록
<img width="554" alt="image" src="https://github.com/user-attachments/assets/c6aca36e-a0bb-4668-847b-4037dd2d14e3">

### 친구와 콘텐츠 공유
<img width="800" alt="image" src="https://github.com/user-attachments/assets/bea02b62-4bd6-4acb-98c1-07ecd85a4c2a">

## ERD (코드 관점)
<img width="1000" alt="image" src="https://github.com/user-attachments/assets/dea96a5c-458d-4b34-bd32-e3ffdec70a0b">

## API 명세서
### Swagger 링크
### [http://ott.knu-soft.site/swagger-ui/index.html](http://ott.knu-soft.site/swagger-ui/index.html#/)

## 웹 서버 아키텍쳐
![image](https://github.com/user-attachments/assets/3c2062b5-5589-4b3d-9f04-8521d6f59ce5)
