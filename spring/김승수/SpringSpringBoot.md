<aside>
💡

SpringBoot 등장배경

- Spring 생태계가 커짐에 따라 다양한 기능을 제공하지만 그 기능을 사용하기 위한 설정에 많은 시간이 걸림.
    
    ex) hibernate 관련 spring 설정
    

![image.png](https://prod-files-secure.s3.us-west-2.amazonaws.com/84c11b81-bda1-4e76-b244-ebadfc7850fd/d720a820-09de-442d-872c-d4c21ab4eecc/image.png)

- 스프링 부트는 단지 실행만 하면 되는 스프링 기반의 어플리케이션을 쉽게 만들 수 있다.
</aside>

## Spring Boot란?

Spring Boot는 Spring 프레임워크의 확장으로, 더 빠르고 효율적인 개발 환경을 제공합니다. 주요 특징은 다음과 같습니다:

- **자동 구성(Auto-configuration)**: 애플리케이션 설정을 자동화하여 개발자의 수고를 덜어줍니다.
- **독립 실행형 애플리케이션**: 내장 서버를 포함하여 별도의 서버 설정 없이 실행 가능합니다.
- **'Starter' 의존성**: 일반적인 사용 사례에 대한 의존성을 미리 구성해 제공합니다.
- **프로덕션 준비 기능**: 메트릭, 상태 확인, 외부 구성 등을 기본으로 제공합니다.

Spring Boot는 Spring의 철학을 유지하면서도, 개발자가 빠르게 애플리케이션을 설정하고 실행할 수 있도록 돕습니다.

## Maven 의존성

### Spring:

Spring 프레임워크를 사용할 때는 필요한 각 모듈을 개별적으로 추가해야 합니다. 예를 들어, 웹 애플리케이션을 만들기 위해서는 다음과 같은 의존성이 필요합니다:

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
    <version>5.3.5</version>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
    <version>5.3.5</version>
</dependency>

```

이외에도 필요한 기능에 따라 추가적인 의존성을 명시해야 합니다.

### Spring Boot:

반면 Spring Boot는 'Starter' 의존성을 통해 필요한 모듈을 한 번에 추가할 수 있습니다. 웹 애플리케이션을 위해서는 다음과 같은 단일 의존성만 필요합니다:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.4.4</version>
</dependency>

```

이 의존성은 웹 애플리케이션 개발에 필요한 모든 관련 라이브러리를 자동으로 포함시킵니다.

## 5. MVC 구성

### Spring:

Spring MVC를 사용하기 위해서는 여러 구성 단계가 필요합니다:

1. 디스패처 서블릿(DispatcherServlet) 정의
2. 매핑 설정
3. 뷰 리졸버(ViewResolver) 설정

예를 들어, Java 설정을 사용할 경우 다음과 같은 코드가 필요합니다:

```java
@EnableWebMvc
@Configuration
public class WebConfig implements WebMvcConfigurer {
   @Bean
   public ViewResolver viewResolver() {
      InternalResourceViewResolver resolver = new InternalResourceViewResolver();
      resolver.setPrefix("/WEB-INF/views/");
      resolver.setSuffix(".jsp");
      return resolver;
   }
}

```

### Spring Boot:

Spring Boot는 자동 구성을 통해 이러한 설정을 대부분 자동화합니다. 웹 스타터를 추가하고 나면, 다음과 같은 간단한 속성 설정만으로 JSP 뷰를 사용할 수 있습니다:

```
spring.mvc.view.prefix=/WEB-INF/jsp/
spring.mvc.view.suffix=.jsp

```

이러한 자동 구성은 개발자가 복잡한 설정 없이 빠르게 애플리케이션을 개발할 수 있게 해줍니다.

## 6. Spring Security 구성

### Spring:

Spring Security를 사용하기 위해서는 다음과 같은 의존성과 구성이 필요합니다:

1. spring-security-web과 spring-security-config 의존성 추가
2. 보안 구성 클래스 작성

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
            .formLogin()
                .and()
            .httpBasic();
    }
}

```

### Spring Boot:

Spring Boot에서는 spring-boot-starter-security 의존성만 추가하면 기본적인 보안 설정이 자동으로 적용됩니다. 추가적인 설정이 필요한 경우에만 구성 클래스를 작성하면 됩니다.

## 7. 애플리케이션 부트스트랩

### Spring:

전통적인 Spring 애플리케이션은 web.xml 파일이나 WebApplicationInitializer 구현을 통해 부트스트랩됩니다. 이 과정에서 ApplicationContext를 생성하고 DispatcherServlet을 설정합니다.

### Spring Boot:

Spring Boot는 main 메서드를 통해 애플리케이션을 시작합니다. @SpringBootApplication 어노테이션을 사용하여 자동 구성, 컴포넌트 스캔 등을 활성화합니다:

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

```

이 방식은 내장 웹 서버를 사용하여 애플리케이션을 실행하므로, 별도의 웹 서버 설정이 필요 없습니다.

## 8. 패키징 및 배포

### Spring:

전통적인 Spring 애플리케이션은 주로 WAR 파일로 패키징되어 외부 웹 서버(예: Tomcat)에 배포됩니다. 이 과정에서 서버 설정, 데이터소스 구성 등 추가적인 작업이 필요할 수 있습니다.

### Spring Boot:

Spring Boot는 다음과 같은 이점을 제공합니다:

1. **실행 가능한 JAR**: java -jar 명령으로 직접 실행 가능한 JAR 파일을 생성합니다.
2. **내장 서버**: Tomcat, Jetty 등의 서버가 JAR에 포함되어 별도의 웹 서버 설정이 필요 없습니다.
3. **외부 설정**: application.properties 또는 application.yml 파일을 통해 쉽게 설정을 관리할 수 있습니다.
4. **프로필 기반 구성**: 개발, 테스트, 프로덕션 환경별로 다른 설정을 쉽게 적용할 수 있습니다.

이러한 특징들은 애플리케이션의 배포와 관리를 크게 간소화합니다.

## 9. 결론

Spring Boot는 Spring 프레임워크의 강력한 기능을 유지하면서도, 개발자가 빠르게 애플리케이션을 구축하고 실행할 수 있도록 도와줍니다. 자동 구성, 내장 서버, 간편한 의존성 관리 등의 특징은 Spring Boot를 사용하는 주요 이유가 됩니다.

그러나 Spring의 세부적인 제어가 필요한 복잡한 엔터프라이즈 애플리케이션의 경우, 전통적인 Spring 프레임워크가 여전히 선호될 수 있습니다. 프로젝트의 요구사항과 복잡도에 따라 적절한 프레임워크를 선택하는 것이 중요합니다.