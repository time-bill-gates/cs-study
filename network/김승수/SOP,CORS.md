## SOP

**SOP**: 웹 브라우저에서 **보안**을 위해 도입된 정책. 웹 페이지가 자신과 **같은 출처**(같은 도메인, 프로토콜, 포트)를 가진 리소스에만 접근할 수 있도록 제한하는 규칙

**출처 3요소 모두 일치**해야 동일 출처

- **도메인** (예: `www.example.com`)
- **프로토콜** (예: `http`, `https`)
- **포트** (예: `80`, `443`)

## SOP의 목적

SOP는 해킹 공격 중 하나인 **XSS(Cross-Site Scripting)**나 **CSRF(Cross-Site Request Forgery)** 같은 공격을 막기 위해 만들었다. 공격자는 피해자의 브라우저에서 다른 출처의 민감한 데이터를 훔치려고 할 수 있기 때문에, SOP는 이런 부당한 데이터 접근을 차단

- XSS : XSS는 공격자가 사용자가 보는 웹 페이지에 악성 **스크립트**를 삽입해서, 사용자의 브라우저에서 이 스크립트가 실행되도록 하는 해킹 기법.
    - 네이버 글에 스크립트를 작성해서 해당 사용자의 **쿠키나 세션 정보** 같은 민감한 데이터를 탈취
- CSRF: 사용자가 **인증된 상태**를 악용해, 공격자가 의도한 행동을 사용자가 **모르게** 수행하게 만드는 공격
    - 공격자가 사용자를 속여서 **악성 링크**를 클릭하게 만들면, 이 링크는 네이버 서버로 요청을 보내는 **위조된 요청** 가능, 극한의 예시로 송금 요청도 가능,,

## SOP 문제

SOP에 의해 브라우저에서 다른 출처로 요청을 보내지 못하는 문제점이 있다. 

- ex) 브라우저에서 네이버 지도 api로 부터 정보를 받지 못하는 문제가 있을 수 있다.

우회 방법으로는 **CORS**, **JSONP**, **프록시 서버**, 그리고 **postMessage API** 등을 사용 가능. 하지만, 각 방법은 보안 문제를 고려해야 하며, 특히 **CORS**는 가장 안전하고 널리 사용되는 방법.

## CORS

**CORS**는 SOP의 제한을 완화하는 방법 중 하나. 브라우저가 기본적으로 SOP에 의해 교차 출처 요청을 차단하지만, **서버 측**에서 특정 출처에 대해 **허용해줄 수 있는 방법**이 바로 CORS

### **CORS의 동작 과정**

`https://www.example.com`에서 **네이버 API**인`https://api.naver.com/user/data`로 **교차 출처 요청**을 보내는 상황을 가정해 보자.

### **상황:**

- 클라이언트(브라우저)가 `https://www.example.com` 도메인에서 실행
- 네이버 API에 `POST` 요청을 보내서 JSON 데이터를 전송
- 요청에는 커스텀 헤더(`Authorization`)와 `Content-Type: application/json`을 사용

이때, **CORS 규칙**에 따르면 브라우저는 요청 전에 **Preflight Request**를 사용해 서버가 이 요청을 허용할지 확인해야 함

---

### **1. Preflight Request: 브라우저가 사전 검사 요청 보내기**

브라우저는 교차 출처 요청을 바로 보내는 대신, 먼저 **사전 검사 요청**을 서버로 보낸다. 이 요청은 **OPTIONS 메서드**를 사용해, 브라우저가 이 교차 출처 요청을 보낼 수 있는지 서버에 묻는다.

모든 요청이 사전 검사를 거치는 건 아님. 단순 요청(simple request)은 사전 요청을 거치지 않고 바로 교차 출처 요청을 보냄

- `GET`, `POST`, `HEAD` 중 하나의 HTTP 메서드를 사용.
- `Content-Type`이 `text/plain`, `multipart/form-data`, 또는 `application/x-www-form-urlencoded`일 때.

이 경우 브라우저는 사전 요청 없이 바로 요청을 보내고, 서버가 **CORS 헤더**를 응답에 포함하면 정상적으로 데이터를 받음

### 브라우저에서 보내는 Preflight Request 예시:

```
OPTIONS /user/data HTTP/1.1
Host: api.naver.com
Origin: https://www.example.com
Access-Control-Request-Method: POST
Access-Control-Request-Headers: Authorization, Content-Type
```

**요청의 주요 헤더:**

- **Origin**: 이 요청이 어디서 왔는지(즉, 출처)가 `https://www.example.com`임을 명시
- **Access-Control-Request-Method**: 브라우저가 나중에 보낼 요청의 메서드(`POST`)를 서버에게 알림
- **Access-Control-Request-Headers**: 브라우저가 사용할 커스텀 헤더들(`Authorization`, `Content-Type`)을 미리 서버에 알림

이 요청은 서버에 "내가 `POST` 메서드와 특정 헤더(`Authorization`, `Content-Type`)를 사용해 네이버 API로 데이터를 보내려고 하는데, 이 요청을 허용할 거야?"라고 묻는 과정

---

### **3. Preflight Request에 대한 서버 응답**

서버는 이 사전 검사 요청을 받고, 해당 교차 출처 요청을 허용할지 여부를 응답. 만약 서버가 이 요청을 허용하면, 응답에 **CORS 관련 헤더**를 포함해 브라우저에게 알려줌.

### 서버에서의 응답 예시:

```
HTTP/1.1 200 OK
Access-Control-Allow-Origin: https://www.example.com
Access-Control-Allow-Methods: POST
Access-Control-Allow-Headers: Authorization, Content-Type
Access-Control-Max-Age: 86400
```

**응답의 주요 헤더:**

- **Access-Control-Allow-Origin**: 서버가 교차 출처 요청을 허용하는 출처를 명시해. 여기서는 `https://www.example.com`을 허용.
- **Access-Control-Allow-Methods**: `POST`와 같은 특정 HTTP 메서드를 허용.
- **Access-Control-Allow-Headers**: `Authorization`, `Content-Type` 같은 특정 헤더들을 사용하도록 허용.
- **Access-Control-Max-Age**: 이 사전 검사 요청의 유효 기간(초 단위)을 지정해. 여기서는 86400초(24시간) 동안 같은 요청에 대해 사전 검사를 다시 하지 않도록 설정.

이 응답을 통해 서버는 "해당 요청을 허용할게"라는 메시지를 브라우저에 전달.

---

### **4. 실제 요청: 브라우저가 API 요청 보내기**

서버가 사전 검사 요청에 긍정적으로 응답했으니, 이제 브라우저는 실제로 네이버 API로 데이터를 전송할 가능

### 브라우저에서 보내는 실제 요청 예시:

```
POST /user/data HTTP/1.1
Host: api.naver.com
Origin: https://www.example.com
Authorization: Bearer someToken
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com"
}
```

이 요청은 브라우저에서 서버로 전송된 **실제 교차 출처 요청**. 이제 서버는 이 요청을 받아 처리. 중요한 점은 이 요청에 **Origin 헤더**가 포함되어 있어, 서버가 다시 한 번 출처를 확인할 수 있음.

---

### **5. 서버의 응답**

서버가 요청을 정상적으로 처리했다면, 클라이언트에게 응답을 반환. 이때 서버는 응답에 CORS 관련 헤더를 다시 포함해 브라우저가 응답을 받아들이도록 함.

### 서버의 응답 예시:

```
HTTP/1.1 200 OK
Access-Control-Allow-Origin: https://www.example.com
Content-Type: application/json

{
  "status": "success",
  "data": {
    "userId": 123,
    "name": "John Doe"
  }
}
```

- **Access-Control-Allow-Origin**: 이 응답도 `https://www.example.com` 출처에서 온 요청을 허용했음을 명시해. 브라우저는 이 헤더를 보고 응답을 처리할 수 있는지 결정해.

이 응답을 받은 브라우저는 이제 데이터를 화면에 표시하거나, 필요한 후속 작업을 진행할 수 있어.

---

### **6. Preflight Request 캐싱**

서버가 **Access-Control-Max-Age** 헤더를 설정했다면, 이 값 동안은 **같은 교차 출처 요청에 대해 사전 검사를 다시 보내지 않아도** 됨. 이로 인해 성능이 최적화. 예를 들어, `86400초(24시간)` 동안은 같은 요청을 할 때 Preflight Request를 생략하고 바로 요청을 보냄.

하지만 만약 서버에서 `Access-Control-Allow-Origin` 헤더를 설정하지 않으면, 브라우저는 요청을 **차단**