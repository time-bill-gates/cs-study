# OSI 7계층

---

## **OSI 모델이란?**

개방형 시스템 상호 연결(OSI) 모델은 표준 프로토콜을 사용하여 다양한 통신 시스템이 통신할 수 있도록 국제표준화기구에서 만든 개념 모델입니다. 쉽게 표현하자면 OSI는 상이한 컴퓨터 시스템이 서로 통신할 수 있는 표준을 제공합니다.

OSI 모델은 컴퓨터 네트워킹의 범용 언어로 볼 수 있습니다. 이 모델은 통신 시스템을 7개의 추상적 계층으로 나누며 각 계층은 다음 계층 위에 스택됩니다.

![image](https://github.com/user-attachments/assets/40647626-e90e-491b-bd6c-d4cb99e02e7a)

OSI 모델의 각 계층은 특정 작업을 처리하고 그 위와 아래의 계층과 통신합니다. DDoS 공격은 다음과 같이 네트워크 연결의 특정 계층을 표적으로 합니다. 애플리케이션 계층 공격은 계층 7을 표적으로 하며 프로토콜 계층 공격은 계층 3과 4를 표적으로 합니다.

## **OSI 모델이 중요한 이유는 무엇입니까?**

현대 인터넷은 OSI 모델을 엄격하게 따르지 않음에도 불구하고(더 단순한 인터넷 프로토콜 제품군을 더 따름), OSI 모델은 여전히 네트워크 문제를 해결하는 데 아주 유용합니다. 한 사람이 노트북에서 인터넷을 이용할 수 없게 되었거나 수천 명의 사용자가 이용하는 웹 사이트가 다운되었더라도, OSI 모델은 문제를 분석하고 문제의 원인을 분리하는 데 도움이 될 수 있습니다. 모델의 어느 특정 계층으로 문제를 좁힐 수 있을 경우 불필요한 많은 작업을 피할 수 있습니다.

## **OSI 모델의 7가지 계층은 무엇인가요?**

OSI 모델의 7가지 추상 계층은 탑 다운 방식으로 다음과 같이 정의할 수 있습니다.

## **7. 애플리케이션 계층**

![image](https://github.com/user-attachments/assets/13bc041f-3380-49df-a3e6-72d1287098dc)

이 계층은 사용자의 데이터와 직접 상호 작용하는 유일한 계층입니다. 웹 브라우저 및 이메일 클라이언트와 같은 소프트웨어 애플리케이션은 통신을 개시하기 위해 애플리케이션 계층에 의지합니다. 그러나 클라이언트 소프트웨어 애플리케이션은 애플리케이션 계층의 일부가 아니라는 점은 분명해야 합니다. 더 정확히 말하면 애플리케이션 계층은 소프트웨어가 사용자에게 의미 있는 데이터를 제공하기 위해 의존하는 프로토콜과 데이터를 조작하는 역할을 합니다.

애플리케이션 계층 프로토콜에는 HTTP뿐만 아니라 SMTP도 포함됩니다.

## **6. 프레젠테이션 계층**

![image](https://github.com/user-attachments/assets/7c94b760-abf3-4cd4-87cf-bec4d58284b1)


이 계층은 주로 데이터를 준비하는 역할을 하여 애플리케이션 계층이 이를 사용할 수 있게 합니다. 즉, 계층 6은 애플리케이션이 소비할 수 있도록 데이터를 프레젠테이션합니다. 프레젠테이션 계층은 데이터의 변환, [암호화](https://www.cloudflare.com/learning/ssl/what-is-encryption/), 압축을 담당합니다.

서로 통신하는 두 개의 통신 장치는 서로 다른 인코딩 방법을 사용하고 있을 수 있으므로, 계층 6은 수신 장치의 애플리케이션 계층이 이해할 수 있는 구문으로 수신 데이터를 변환하는 일을 담당합니다.

장치가 암호화된 연결을 통해 통신하는 경우, 계층 6은 최종 송신자에게 암호화를 추가할 뿐만 아니라 최종 수신자에게 암호화를 디코딩하여 암호화되지 않은 읽기 쉬운 데이터로 애플리케이션 계층을 제시할 수 있도록 하는 역할을 합니다.

마지막으로, 프레젠테이션 계층은 애플리케이션 계층에서 수신한 데이터를 계층 5로 전송하기 전에 압축하는 일도 담당합니다. 전송할 데이터의 양을 최소화함으로써 통신의 속도와 효율을 높이는 데 도움이 됩니다.

## **5. 세션 계층**

![image](https://github.com/user-attachments/assets/36091b99-f211-4cc5-a8db-dc66083d9fa6)

두 기기 사이의 통신을 시작하고 종료하는 일을 담당하는 계층입니다. 통신이 시작될 때부터 종료될 때까지의 시간을 세션이라고 합니다. 세션 계층은 교환되고 있는 모든 데이터를 전송할 수 있도록 충분히 오랫동안 세션을 개방한 다음 리소스를 낭비하지 않기 위해 세션을 즉시 닫을 수 있도록 보장합니다.

또한 세션 계층은 데이터 전송을 체크포인트와 동기화합니다. 예를 들어, 100MB의 파일이 전송되는 경우 세션 계층이 5MB마다 체크포인트를 설정할 수 있습니다. 52MB가 전송 된 후 연결이 끊어 지거나 충돌이 발생하면 마지막 체크 포인트에서 세션을 재개하는 것이 가능합니다. 즉, 50MB의 데이터만 더 전송하면 됩니다. 체크 포인트가 없으면 전체 전송을 처음부터 다시 시작해야 합니다.


## **4. 전송 계층**

![image](https://github.com/user-attachments/assets/fc24b1d7-4a71-4be6-a8e2-0d3d3c7fba4a)

계층 4는 두 기기 간의 종단 간 통신을 담당합니다. 여기에는 세션 계층에서 데이터를 가져와서 계층 3으로 보내기 전에 세그먼트라고 하는 조각으로 분할하는 일이 포함됩니다. 수신 기기의 전송 계층은 세그먼트를 세션 계층이 이용할 수 있는 데이터로 재조립해야 합니다.

전송 계층은 또한 흐름 제어 및 오류 제어 기능의 역할도 합니다. 흐름 제어는 연결 속도가 빠른 송신자가 연결 속도가 느린 수신자를 압도하지 않도록 최적의 전송 속도를 결정합니다. 전송 계층은 수신된 데이터가 완료되었는지 확인하고 수신되지 않은 경우 재전송을 요청하여 최종 수신자에 대해 오류 제어를 수행합니다.

전송 계층 프로토콜에는 전송 제어 프로토콜(TCP) 및 사용자 데이터그램 프로토콜(UDP)이 있습니다.

## **3. 네트워크 계층**

![image](https://github.com/user-attachments/assets/eb0a613c-4247-438c-bba8-7e84fbf6d434)

네트워크 계층은 서로 다른 두 네트워크 간 데이터 전송을 용이하게 하는 역할을 합니다. 서로 통신하는 두 장치가 동일한 네트워크에 있는 경우에는 네트워크 계층이 필요하지 않습니다. 네트워크 계층은 전송 계층의 세그먼트를 송신자의 장치에서 패킷이라고 불리는 더 작은 단위로 세분화하여 수신 장치에서 이러한 패킷을 다시 조립합니다. 또한, 네트워크 계층은 데이터가 표적에 도달하기 위한 최상의 물리적 경로를 찾는데 이를 라우팅이라고 합니다.

네트워크 계층 프로토콜에는 IP, 인터넷 제어 메시지 프로토콜(ICMP), 인터넷 그룹 메시지 프로토콜(IGMP), IPsec제품군이 있습니다.

## **2. 데이터 연결 계층**

![image](https://github.com/user-attachments/assets/720b3769-3913-4ca1-9674-99e75c7e0605)

데이터 연결 계층은 네트워크 계층과 매우 비슷하지만, 데이터 연결 계층은 *동일한* 네트워크에 있는 두 개의 장치 간 데이터 전송을 용이하게 합니다. 데이터 연결 계층은 네트워크 계층에서 패킷을 가져와서 프레임이라고 불리는 더 작은 조각으로 세분화합니다. 네트워크 계층과 마찬가지로 데이터 연결 계층도 인트라 네트워크 통신에서 흐름 제어 및 오류 제어를 담당합니다(전송 계층은 네트워크 간 통신에 대해서만 흐름 제어 및 오류 제어만을 담당함).

## **1. 물리적 계층**

![image](https://github.com/user-attachments/assets/e3e84607-cc03-4deb-a633-aab24aa8848a)

이 계층에는 케이블, 스위치 등 데이터 전송과 관련된 물리적 장비가 포함됩니다. 이 계층은 또한 1과 0의 문자열인 비트 스트림으로 변환되는 계층입니다. 뿐만 아니라 두 장치의 물리적 계층은 신호 규칙에 동의해서 두 장치의 1이 0과 구별될 수 있어야 합니다.

## **OSI 모델을 통해 데이터가 전송되는 방법**

네트워크를 통해 사람이 읽을 수 있는 정보를 장치 간에 전송하려면 데이터가 송신 장치에서 OSI 모델의 7가지 계층 아래로 이동한 다음 최종 수신자에서 7가지 계층 위로 이동해야 합니다.

예: 쿠퍼 씨가 파머 씨에게 이메일을 보내려고 합니다. 쿠퍼 씨는 자신의 노트북에 있는 이메일 애플리케이션에서 메시지를 작성하고 '발송'을 누릅니다. 쿠퍼 씨의 이메일 애플리케이션이 이메일 메시지를 애플리케이션 계층으로 넘기면, 애플리케이션 레이어는 프로토콜(SMTP)을 선택하고, 데이터를 프레젠테이션 계층으로 전달합니다. 프리젠테이션 계층이 압축한 데이터는 세션 계층에 도달하고, 세션 계층은 세션을 시작합니다.

이제 데이터는 발신자의 전송 계층으로 넘어가 세그먼트로 나눠지게 되며, 이 세그먼트는 네트워크 계층에서 패킷으로 다시 나눠지고, 이는 데이터 연결 계층에서 프레임으로 나눠집니다. 데이터 링크 계층은 해당 프레임을 물리적 계층으로 전달하며, 물리적 계층은 데이터를 1과 0의 비트스트림으로 변환하고 물리적 매체(예: 케이블)를 통해 전송합니다.

파머 씨의 컴퓨터가 물리적 매체(예: 와이파이)를 통해 비트스트림을 수신하면, 데이터는 반대 순서로 계층을 지나게 됩니다. 먼저 물리적 계층은 비트 스트림을 1과 0에서 프레임으로 변환해 데이터 연결 계층으로 넘깁니다. 데이터 연결 계층은 프레임을 패킷으로 재조립해 네트워크 계층으로 넘깁니다. 네트워크 계층은 패킷으로 세그먼트를 만들어 전송 계층으로 넘기고, 전송 계층은 세그먼트를 재조립해 하나의 데이터를 만듭니다.

이제 데이터는 수신자의 세션 계층으로 흐르고, 세션 계층이 이 데이터를 프리젠테이션 계층으로 넘기면, 통신 세션이 종료됩니다. 이제 프레젠테이션 계층은 압축을 제거하고 원본 데이터를 애플리케이션 계층으로 넘깁니다. 애플리케이션 계층은 사람이 읽을 수 있는 데이터를 파머 씨의 이메일 소프트웨어에 제공하고, 파머 씨는 자기 노트북 화면에서 이메일 소프트웨어를 통해 쿠퍼 씨의 이메일을 읽을 수 있게 됩니다.

## 출처

- https://www.cloudflare.com/ko-kr/learning/ddos/glossary/open-systems-interconnection-model-osi/
