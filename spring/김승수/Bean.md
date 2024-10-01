## Bean ?

스프링 Bean은 스프링 IOC 컨테이너가 관리하는 자바 객체입니다. 애플리케이션의 핵심을 이루는 객체이며, 스프링 컨테이너에 의해 생성되고, 라이프사이클이 관리됩니다.

## Bean 등록 방법

1. XML 설정 파일 사용
2. 자바 설정 클래스 사용 (@Configuration과 @Bean 어노테이션)
3. 컴포넌트 스캔과 어노테이션 사용 (@Component, @Service, @Repository, @Controller 등)

## Bean 생명주기

Bean의 생명주기는 크게 초기화, 사용, 소멸 단계로 나눌 수 있습니다.

1. 초기화 단계:
    - 인스턴스화
    - 프로퍼티 설정
    - BeanNameAware의 setBeanName() 메소드 호출
    - BeanFactoryAware의 setBeanFactory() 메소드 호출
    - ApplicationContextAware의 setApplicationContext() 메소드 호출
    - BeanPostProcessor의 postProcessBeforeInitialization() 메소드 호출
    - InitializingBean의 afterPropertiesSet() 메소드 호출
    - 커스텀 init-method 호출
    - BeanPostProcessor의 postProcessAfterInitialization() 메소드 호출
2. 사용 단계:
    - 애플리케이션에서 Bean 사용
3. 소멸 단계:
    - DisposableBean의 destroy() 메소드 호출
    - 커스텀 destroy-method 호출

Bean 생명주기 관리를 위한 인터페이스와 어노테이션:

```java
public class ExampleBean implements InitializingBean, DisposableBean {
    @Override
    public void afterPropertiesSet() throws Exception {
// 초기화 로직
    }

    @Override
    public void destroy() throws Exception {
// 소멸 로직
    }
}

// 또는 어노테이션 사용
public class AnotherExampleBean {
    @PostConstruct
    public void init() {
// 초기화 로직
    }

    @PreDestroy
    public void cleanup() {
// 소멸 로직
    }
}

```

## Bean의 Scope

Bean의 생명주기는 그 Scope에 따라 달라집니다. 주요 Scope는 다음과 같습니다:

1. singleton (기본값): 스프링 컨테이너당 하나의 인스턴스만 생성
2. prototype: 요청할 때마다 새로운 인스턴스 생성
3. request: HTTP 요청당 하나의 인스턴스 생성 (웹 애플리케이션)
4. session: HTTP 세션당 하나의 인스턴스 생성 (웹 애플리케이션)
5. application: ServletContext당 하나의 인스턴스 생성 (웹 애플리케이션)

## Bean 관리 방식

스프링은 IOC 컨테이너를 통해 Bean을 관리합니다.

1. Bean 생성: 설정에 따라 Bean 인스턴스를 생성합니다.
2. 의존성 주입: Bean 간의 의존 관계를 설정합니다.
3. 생명주기 관리: Bean의 초기화와 소멸 메소드를 호출합니다.
4. Scope 관리: Bean의 Scope에 따라 인스턴스를 관리합니다.
5. 자동 설정: @Autowired 등을 통한 자동 의존성 주입을 처리합니다.

예시 코드:

```java
@Configuration
public class AppConfig {
    @Bean(initMethod = "init", destroyMethod = "cleanup")
    @Scope("prototype")
    public ExampleService exampleService() {
        return new ExampleService();
    }
}

@Service
@Scope("singleton")
public class AnotherService implements InitializingBean, DisposableBean {
    @Override
    public void afterPropertiesSet() throws Exception {
// 초기화 로직
    }

    @Override
    public void destroy() throws Exception {
// 소멸 로직
    }
}

```