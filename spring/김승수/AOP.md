## AOP란 ?

<aside>
💡

Aspect Oriented Programming 
OOP의 단점을 극복하기 위한 횡단관심사를 모듈화 한 프로그래밍 기법.
핵심 기능과 부가 기능을 완전히 분리하여 관리할 수 있다.
자바에서는 **AspectJ 라이브러리**를 활용하여 AOP를 사용한다.

</aside>

## AOP 동작 방식

- 컴파일 시점: 실제 대상 코드에 애스팩트를 통한 부가 기능 호출 코드가 포함된다. AspectJ를 직접 사용해야 한다.
    
    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/13818c71-a4f5-4af8-885a-0275a880e617/image.png)
    
- 클래스 로딩 시점: 실제 대상 코드에 애스팩트를 통한 부가 기능 호출 코드가 포함된다. AspectJ를 직접 사용해야 한다.
    
    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/4b18ea6e-f2f8-429b-8988-f1090d7db2da/image.png)
    
- 런타임 시점: 실제 대상 코드는 그대로 유지된다. 대신에 프록시를 통해 부가 기능이 적용된다. 따라서 항상 **프록시**를 통해야 부가 기능을 사용할 수 있다. 스프링 AOP는 이 방식을 사용한다.
    
    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/8c5fd1bd-8543-48d2-8439-6c4f8d4aa68b/image.png)
    
    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/aac294e6-6057-4114-94fb-cd2530ab781e/image.png)
    

## 용어

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/7abc0c8c-9d03-4ad7-ac92-3d69d80b65ae/image.png)

- 타켓(Target)
    - 어드바이스를 받는 객체, 포인트컷으로 결정
- 어드바이스(Advice)
    - 부가 기능
    - 특정 조인 포인트에서 Aspect에 의해 취해지는 조치
    - Around(주변), Before(전), After(후)와 같은 다양한 종류의 어드바이스가 있음
- 조인 포인트(Join point)
    - 어드바이스가 적용될 수 있는 위치, 메소드 실행, 생성자 호출, 필드 값 접근, static 메서드 접근 같은 프로그램 실행 중 지점
    - 조인 포인트는 추상적인 개념이다. AOP를 적용할 수 있는 모든 지점이라 생각하면 된다.
    - **스프링 AOP는 프록시 방식**을 사용하므로 조인 포인트는 항상 **메소드 실행 지점**으로 제한된다.
- 포인트컷(Pointcut)
    - 조인 포인트 중에서 어드바이스가 적용될 위치를 선별하는 기능
    - 주로 AspectJ 표현식을 사용해서 지정
    - 프록시를 사용하는 스프링 AOP는 메서드 실행 지점만 포인트컷으로 선별 가능
- 애스펙트(Aspect)
    - 어드바이스 + 포인트컷을 모듈화 한 것
    - @Aspect 를 생각하면 됨
    - 여러 어드바이스와 포인트 컷이 함께 존재
- 어드바이저(Advisor)
    - 하나의 어드바이스와 하나의 포인트 컷으로 구성
    - 스프링 AOP에서만 사용되는 특별한 용어
- 위빙(Weaving)
    - 포인트컷으로 결정한 타켓의 조인 포인트에 어드바이스를 적용하는 것
    - 위빙을 통해 핵심 기능 코드에 영향을 주지 않고 부가 기능을 추가 할 수 있음
    - AOP 적용을 위해 애스펙트를 객체에 연결한 상태
        - 컴파일 타임(AspectJ compiler)
        - 로드 타임
        - 런타임, 스프링 AOP는 런타임, 프록시 방식
- AOP 프록시
    - AOP 기능을 구현하기 위해 만든 프록시 객체, 스프링에서 AOP 프록시는 JDK 동적 프록시 또는 CGLIB 프록시이다.

## 프록시

### 프록시 패턴 vs 데코레이터 패턴

- 접근 제어
    - 권한에 따른 접근 차단
    - 캐싱
    - 지연 로딩
- 부가 기능 추가
    - 원래 서버가 제공하는 기능에 더해서 부가 기능을 수행한다.
    예) 요청 값이나, 응답 값을 중간에 변형한다.
    예) 실행 시간을 측정해서 추가 로그를 남긴다.
    
    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/edac9092-3ed3-4d8e-866c-ad86e9c5f6e8/image.png)
    
    ![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/70db3496-91e5-4cb2-8ecf-413b6baabd8d/image.png)
    

### CGLIB

사용 방법:

- 클래스를 상속받아 프록시를 생성합니다.
- 인터페이스가 없어도 동작 가능합니다.
- 런타임에 바이트코드를 생성하여 프록시 객체를 만듭니다.
- final 클래스나 메서드에는 적용할 수 없습니다.
1. CGLIB 라이브러리를 의존성에 추가합니다.
2. MethodInterceptor 인터페이스를 구현하여 프록시 동작을 정의합니다.
3. Enhancer 클래스를 사용하여 프록시 객체를 생성합니다.

### JDK동적프록시

사용 방법:

- 인터페이스를 기반으로 프록시를 생성합니다.
- 반드시 인터페이스가 필요합니다.
- Java의 리플렉션을 사용하여 프록시를 생성합니다.
- JDK에 내장되어 있어 추가 라이브러리가 필요 없습니다.
1. 대상 객체가 구현할 인터페이스를 정의합니다.
2. InvocationHandler 인터페이스를 구현하여 프록시 동작을 정의합니다.
3. Proxy.newProxyInstance() 메서드를 사용하여 프록시 객체를 생성합니다.

**차이점**

1. CGLIB는 클래스 기반, JDK 동적 프록시는 인터페이스 기반입니다.
2. CGLIB는 상속을 사용하므로 final 클래스에 적용할 수 없지만, JDK 동적 프록시는 인터페이스만 있으면 됩니다.
3. CGLIB는 별도의 라이브러리가 필요하지만, JDK 동적 프록시는 Java 표준 라이브러리만으로 구현 가능합니다.
4. 성능 면에서 CGLIB가 일반적으로 더 빠르다고 알려져 있지만, 최신 JDK 버전에서는 그 차이가 크지 않습니다.

### 프록시 팩토리

JDK 동적 프록시와 CGLIB를 함께 사용할 수 있는 구현체

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/1d7ae9d7-cd79-4709-874e-e038c0bf4602/image.png)

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/89b00dea-4af1-4b7a-8d05-2170cb84be40/image.png)

`Advice` 는 프록시에 적용하는 부가 기능 로직이다. 이것은 JDK 동적 프록시가 제공하는InvocationHandler 와 CGLIB가 제공하는 MethodInterceptor 의 개념과 유사한다. 둘을 개념적으로 추상화 한 것이다. 프록시 팩토리를 사용하면 둘 대신에 Advice 를 사용하면 된다.

## Spring AOP 어드바이스

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/3ebbae12-27a1-4383-9a57-f5e75ff49f14/image.png)

@Around : 메서드 호출 전후에 수행, 가장 강력한 어드바이스, 조인 포인트 실행 여부 선택, 반환 값 변환, 예외 변환 등이 가능
@Before : 조인 포인트 실행 이전에 실행
@AfterReturning : 조인 포인트가 정상 완료후 실행
@AfterThrowing : 메서드가 예외를 던지는 경우 실행
@After : 조인 포인트가 정상 또는 예외에 관계없이 실행(finally)

## Spring AOP 포인트 컷

포인트컷 지시자의 종류
execution : 메소드 실행 조인 포인트를 매칭한다. 스프링 AOP에서 가장 많이 사용하고, 기능도 복잡하
다.
within : 특정 타입 내의 조인 포인트를 매칭한다.
args : 인자가 주어진 타입의 인스턴스인 조인 포인트
this : 스프링 빈 객체(스프링 AOP 프록시)를 대상으로 하는 조인 포인트
target : Target 객체(스프링 AOP 프록시가 가리키는 실제 대상)를 대상으로 하는 조인 포인트
@target : 실행 객체의 클래스에 주어진 타입의 애노테이션이 있는 조인 포인트
@within : 주어진 애노테이션이 있는 타입 내 조인 포인트
@annotation : 메서드가 주어진 애노테이션을 가지고 있는 조인 포인트를 매칭
@args : 전달된 실제 인수의 런타임 타입이 주어진 타입의 애노테이션을 갖는 조인 포인트
bean : 스프링 전용 포인트컷 지시자, 빈의 이름으로 포인트컷을 지정한다.

## Spring AOP 문제점 !!!!

### 문제1

스프링은 프록시 방식의 AOP를 사용한다. 따라서 AOP를 적용하려면 항상 프록시를 통해서 대상 객체(Target)을 호출해야 한다. 이렇게 해야 프록시에서 먼저 어드바이스를 호출하고, 이후에 대상 객체를 호출한다. **`만약 프록시를 거치지 않고 대상 객체를 직접 호출하게 되면 AOP가 적용되지 않고, 어드바이스도 호출되지 않는다.`**

### 해결1 - 자기 자신 주입

### 해결2 - 지연 조회

### 해결3 - 구조 변경

### 문제2

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/310e23b6-f919-435b-9686-5cb3c7b2ae37/image.png)

JDK 동적 프록시 한계, 인터페이스 기반으로 프록시를 생성하는 JDK 동적 프록시는 구체 클래스로 타입 캐스팅이 불가능한 한계가 있다. 

### 문제3

스프링에서 CGLIB는 구체 클래스를 상속 받아서 AOP 프록시를 생성할 때 사용한다. CGLIB는 구체 클래스를 상속 받기 때문에 다음과 같은 문제가 있다.

- 대상 클래스에 기본 생성자 필수
- 생성자 2번 호출 문제
- final 키워드 클래스, 메서드 사용 불가

### 스프링에서 해결

- 스프링 3.2, CGLIB를 스프링 내부에 함께 패키징
    - CGLIB를 사용하려면 CGLIB 라이브러리가 별도로 필요했다. 스프링은 CGLIB 라이브러리를 스프링 내부에 함께 패키징해서 별도의 라이브러리 추가 없이 CGLIB를 사용할 수 있게 되었다. CGLIB spring-coreorg.springframework
- CGLIB 기본 생성자 필수 문제 해결
    - 스프링 4.0부터 CGLIB의 기본 생성자가 필수인 문제가 해결되었다. objenesis 라는 특별한 라이브러리를 사용해서 기본 생성자 없이 객체 생성이 가능하다.참고로 이 라이브러리는 생성자 호출 없이 객체를 생성할 수 있게 해준다.
- 생성자 2번 호출 문제
    - 스프링 4.0부터 CGLIB의 생성자 2번 호출 문제가 해결되었다. 이것도 역시 objenesis 라는 특별한 라이브러리 덕분에 가능해졌다. 이제 생성자가 1번만 호출된다.
- 스프링 부트 2.0 - CGLIB 기본 사용
    - 스프링 부트 2.0 버전부터 CGLIB를 기본으로 사용하도록 했다. 이렇게 해서 구체 클래스 타입으로 의존관계를 주입하는 문제를 해결했다. 스프링 부트는 별도의 설정이 없다면 AOP를 적용할 때 기본적으로 proxyTargetClass=true 로 설정해서 사용한다. 따라서 인터페이스가 있어도 JDK 동적 프록시를 사용하는 것이 아니라 항상 CGLIB를 사용해서 구체클래스를 기반으로 프록시를 생성한다.
    물론 스프링은 우리에게 선택권을 열어주기 때문에 다음과 깉이 설정하면 JDK 동적 프록시도 사용할 수 있다.
    application.properties

```
spring.aop.proxy-target-class=false
```