Spring IoC (Inversion of Control) 컨테이너에서 관리되는 객체

### 특징
1. **생명주기 관리**: Spring Bean은 Spring IoC 컨테이너가 그 생성, 초기화, 소멸 등 생명주기를 관리합니다.
    - Bean이 생성되고, 필요한 의존성이 주입된 후, 애플리케이션이 종료되면 Spring이 Bean을 소멸시킵니다.
2. **싱글톤 기본 스코프**: 기본적으로 Spring Bean은 **싱글톤(Singleton) 스코프**로 관리됩니다. 즉, 같은 Bean 정의에 대해 애플리케이션 컨텍스트 내에서 하나의 인스턴스만 생성됩니다.
    - 필요에 따라 다른 스코프(`prototype`, `request`, `session` 등)도 사용할 수 있습니다.
3. **의존성 주입(Dependency Injection)**: Spring은 Bean 간의 의존 관계를 자동으로 관리하여, 개발자가 객체 간의 의존성을 수동으로 연결하는 수고를 덜어줍니다. 의존성 주입 방식에는 **생성자 주입**, **세터 주입**, **필드 주입** 등이 있습니다.

### 생명 주기

```java
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.DisposableBean;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.stereotype.Component;

@Component
public class MyLifecycleBean implements InitializingBean, DisposableBean {

    public MyLifecycleBean() {
        System.out.println("Bean is instantiated.");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("PostConstruct method is called.");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean's afterPropertiesSet method is called.");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("PreDestroy method is called.");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean's destroy method is called.");
    }
}

```

1. 객체 생성
2. 의존성 주입
3. 초기화 (우선순위별 나열)
	- **`@PostConstruct` 어노테이션**: 해당 메서드는 의존성 주입이 완료된 후 자동으로 호출됩니다.
	- **`InitializingBean` 인터페이스**: `afterPropertiesSet()` 메서드를 오버라이드하여 초기화 작업을 정의할 수 있습니다.
	- **`init-method` 속성**: XML 기반 설정에서 Bean 초기화 메서드를 지정할 수 있습니다.
4. 사용
5. 소멸 (우선순위별 나열)
	- **`@PreDestroy` 어노테이션**: 소멸 전에 호출할 메서드를 정의할 수 있습니다.
	- **`DisposableBean` 인터페이스**: `destroy()` 메서드를 오버라이드하여 리소스 해제 작업을 할 수 있습니다.
	- **`destroy-method` 속성**: XML 설정에서 소멸 메서드를 지정할 수 있습니다.

### 의존성 주입 방식

| 의존성 주입 방식  | 장점                                    | 단점                                         | 권장 여부     |
| ---------- | ------------------------------------- | ------------------------------------------ | --------- |
| **생성자 주입** | - 불변성 보장<br> - 의존성 주입 보장<br> - NPE 예방 | - 의존성이 많을 경우 생성자 코드가 길어질 수 있음              | **권장**    |
| **세터 주입**  | - 선택적 의존성 주입 가능<br> - 유연성             | - 의존성 주입이 보장되지 않음<br> - 객체 가변성 문제          | 상황에 따라 가능 |
| **필드 주입**  | - 코드가 간결함                             | - 테스트 어려움<br> - 리플렉션 사용<br> - 순환 의존성 문제 가능 | **지양**    |

### Bean Scope
- **Singleton (기본값)**:
    - 애플리케이션 컨텍스트당 하나의 Bean 인스턴스만 생성됩니다.
    - 애플리케이션 내에서 동일한 Bean을 공유하게 됩니다.
- **Prototype**:
    - Bean 요청 시마다 새로운 인스턴스가 생성됩니다.
    - Bean을 주입받는 각 컴포넌트마다 다른 인스턴스를 사용할 수 있습니다.
- **Request**:
    - 웹 애플리케이션에서 하나의 HTTP 요청 당 하나의 Bean 인스턴스가 생성됩니다.
- **Session**:
    - HTTP 세션 당 하나의 Bean 인스턴스가 생성됩니다.
- **Application**:
    - ServletContext와 동일한 범위를 갖습니다. 하나의 웹 애플리케이션에 하나의 인스턴스만 생성됩니다.


