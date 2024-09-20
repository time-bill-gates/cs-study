# 빈
- 스프링 컨테이너가 관리하는 객체

# 스프링 컨테이너
- 이름:객체 형태로 빈을 관리합니다.
- 빈 객체는 싱글톤 처럼 관리됩니다
- AnnotationConfigApplicationContext
    - ApplicationContext 구현체
    - 어노테이션을 이용해 빈을 찾고, 관리하는 컨테이너
- DefaultSingletonBeanRegistry
    - Map으로 싱글톤 인스턴스를 관리
- DefaultListableBeanFactory
    - BeanFactory 구현체
    - Map으로 BeanDefinition 을 저장하고 있는 클래스

# 빈 생성
- 어떤 객체를 빈으로 등록하는데는 어노테이션 혹은 XML 을 이용한 방식이 있다

# 어노테이션 방식의 빈 생성
## @Component 과 @ComponentScan
- @Component 어노테이션이 붙은 클래스를 빈으로 등록합니다
- @Controller, @Service, @Repository 에는 @Component가 선언되어 있다
- @ComponentScan 에서 찾기를 시작할 패키지를 지정할 수 있습니다.
- @SpringBootApplication 안에는 @ComponentScan이 선어되어 있다

## @Bean 과 @Configuration
- @Bean이 붙은 메서드의 반환 객체를 빈으로 등록합니다. 빈의 이름은 메서드의 이름입니다
- @Configuration 없이 @Bean만 사용하면 등록은 되지만 싱글톤이 보장 되지 않습니다

## @Lazy
- 빈 초기화를 지연합니다
- 보통은 ApplicationContext가 실행될 때 빈을 초기화하지만 @Lazy가 붙은 클래스는 실제로 사용할 때 빈을 초기화를 합니다
- @Configuration에 @Lazy를 붙이면 해당 클래스에 모든 @Bean이 붙은 메서드가 만든 빈들을 지연 초기화 합니다
- 애플리케이션 실행 시간을 단축하고 메모리 사용을 최적화할 수 있습니다
- 빈 생성과 관련한 오류 파악이 런타임 시점에 가능 합니다.

# 의존성 주입 방법
## @Autowired
- 생성자 주입
    - 생성자에 @Autowired
    - 생성자가 하나만 있으면 @Autowired 생략이 가능하다
    - 설정된 의존 클래스 불편, 필수 보장
- setter 주입
    - setter 메서드에 @Autowired
    - 설정된 의존 클래스 변경 가능
- 필드 주입
    - 필드에 @Autowired
- 일반 메서드 주입
    - 여러 파라미터를 쓰는 메서드에 @Autowired

## 순서 파악
- 빈의 의존 관계를 파악해 그래프를 만듭니다
- 순환 의존성이 파악되면 예외를 발생합니다
- 그래프에 위상정렬를 바탕으로 의존성 주입 순서를 결정합니다

# 하나의 인터페이스에 구현 클래스가 여러개일때 빈 생성 방법
## @Primary
- 해당 어노테이션이 붙은 클래스를 주입으로 사용합니다

## @Qualifier
- @Primary 보다 우선순위가 높으며 해당 어노테이션에서 지정한 빈을 주입으로 사용합니다


# 빈 생명주기
- 빈이 생성되고 소멸되는 과정

## 인스턴스 화
- 객체 인스턴스 생성

## 프로퍼티 설정
- 필드 값 할당, 의존성 주입

## 초기화
- 콜백
    - @PostConstruct 붙은 메서드
        - 빈으로 등록할 클래스의 void 반환, 파라미터 없는, public 접근제한 메서드에 붙여 사용
        - 권장 사항
    - 등록시 만든 커스텀 init 메서드
        - @Bean의 initMethod 속성에 메서드 이름을 값으로 지정, @Bean이 붙은 메서드의 반환 클래스에 해당 메서드가 있어야 한다
        - 코드를 수정할 수 없는 외부 라이브의 빈 클래스에 등록하고 싶을 때 사용
    - InitilizingBean를 구체화한 afterPropertiesSet 메서드

## 사용
- 컨테이너에서 빈을 꺼내 사용

## 소멸
- 빈을 컨테이너에서 제거합니다
- 콜백
    - @PreDestroy 을 활용한 콜백
        - 빈으로 등록할 클래스의 void 반환, 파라미터 없는, public 접근제한 메서드에 붙여 사용
    - 등록시 만든 커스텀 destroy 메서드
        - @Bean의 destroyMethod 속성에 메서드 이름을 값으로 지정, @Bean이 붙은 메서드의 반환 클래스에 해당 메서드가 있어야 한다
    - DisposableBean를 구체화한 destroy 메서드

# 빈 스코프
- 스프링의 빈은 언제 생성되어 언제 소멸되는지 스코프를 지정할 수 있다
- 기본으로 싱글톤 스코프다
- 싱글톤 스코프
    - 스프링 컨테이너 시작시 생성해 종료시 소멸
- 프로토타입
    - 스프링 컨테이너에서 빈 조회시 항상 새 인스턴스를 생성해 반환
    - 컨테이너가 빈 생성과 의존 관계 주입까지만 동작해 소멸 코드를 직접 작성해야 한다
    - 
- rquest scope
    - 요청이 들어올 때 생성되 응답시 소멸
- session scope
    - 세션 생성시 빈 생성되 소멸시 빈 소멸
- application scope
    - 서블릿 컨텍스와 생명주기 같음
- websocket scope
    - 웹 소켓과 생명주기 같음

# reference
- 인프런, 김영한, 스프링의 핵심 원리 기본편
- spring boot 3.3.1 source code
