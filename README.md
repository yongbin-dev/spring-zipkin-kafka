# spring-zipkin-kafka

## 🏠 소개

- sse-service는 Kafka에서 메시지를 소비할 때마다 이를 Server-Sent Events(SSE)를 통해 client-service로 전송합니다.
- client-service는 수신한 SSE 데이터를 바탕으로 Spring Feign Client를 사용해 student-service에 데이터를 요청합니다.
- student-service는 요청받은 데이터를 조회한 후, 결과를 반환합니다.
- 이러한 모든 과정은 Zipkin을 통해 하나의 TraceId로 묶어 추적 및 관리됩니다.

![architecture](image/architecture.png)

## 📺 개발 환경

- <img src="https://img.shields.io/badge/Framework-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/springboot-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"><img src="https://img.shields.io/badge/3.3.0-515151?style=for-the-badge">
- <img src="https://img.shields.io/badge/Build-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/Gradle-02303A?style=for-the-badge&logo=Gradle&logoColor=white">
- <img src="https://img.shields.io/badge/Language-%23121011?style=for-the-badge">![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)<img src="https://img.shields.io/badge/2.0.0-515151?style=for-the-badge"><img src="https://img.shields.io/badge/java-%23ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"><img src="https://img.shields.io/badge/21-515151?style=for-the-badge">
- <img src="https://img.shields.io/badge/Message_Queue-%23121011?style=for-the-badge">![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)
- <img src="https://img.shields.io/badge/Tracing-%23121011?style=for-the-badge"><img src="https://img.shields.io/badge/Zipkin-FF1B2D?style=for-the-badge">

## 🗓 2024-11-12

- SpringBoot 3.0
    - Spring Cloud Sleuth를 Micrometer Tracing으로 이관

<p align="center">
  <img src="image/kibana-dashboard.png" style="width : 45%"/>
  <img src="image/kibana-discover.png" style="width : 45%"/>
</p>





