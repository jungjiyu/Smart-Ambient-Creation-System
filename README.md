# 🔥 스마트 환경 생성 시스템 (Smart Ambient Creation System) 🔥
- 라즈베리파이와 센서 데이터, Spring Boot, React 기반의 프론트엔드 및 백엔드를 활용하여 스마트 환경을 조성하는 IoT 시스템으로, 라즈베리파이로부터 수집한 센서 데이터를 기반으로 AI API 를 호출하여 현재 환경에 어울리는 조명 색상, YouTube 음악 영상 추천 및 시 생성 등의 기능을 경험할 수 있도록 구현되었다.


# 🧩 아키텍처
![image](https://github.com/user-attachments/assets/c45d166e-df67-496b-ae56-76e9e3799fc6)


# ✅ Project Info
- 개발 도구 : `SpringBoot`, `MySQL`, `okhttp`, `OpenAI API` , `YouTube Data API v3`

# 🥶주요 기능
### 1. 라즈베리파이(클라이언트) 와 통신 
- 라즈베리파이로부터 센서 데이터를 받아 db 에 저장
- db 에 저장된 데이터를 기반으로 액추에이터 및 프론트트 단에서 활용할 데이터(ex: 추천 음악 영상 URL )를 생성 및 제공(라즈베리파이와 프론트단에게)

### 2. AI API 호출
1. 프롬프트 생성: 환경 데이터(온도, 습도 등)를 기반으로 적절한 프롬프트를 작성. (ex: "현재 온도는 26°C이며 습도는 50%입니다. 이 조건에 맞는 시를 작성해주세요.")
2. OpenAI API 호출: 백엔드에서 OpenAI API를 호출하여 결과를 수신. ( 사용 모델: gpt-4o-mini)
    - Youtube URL 의 경우 OpenAI 자체적으로 반환할 수 없게 되어있어, 일차적으로 OpenAI 를 통해 노래 제목까지만 추천 받고, 이를 YouTube API 를 사용하여 해당 키워드와 관련된 동영상의 URL 을 구하도록 추가 구현.
3. 결과 반환: OpenAI API 응답 결과를 프론트엔드/라즈베리파이로 반환.

# 📝 Commit Convention
- add : 새로운 기능 추가
- fix : 버그 수정
- docs : 문서 수정
- style : 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우
- refactor : 코드 리펙토링
- test : 테스트 코드, 리펙토링 테스트 코드 추가
- chore : 빌드 업무 수정, 패키지 매니저 수정

