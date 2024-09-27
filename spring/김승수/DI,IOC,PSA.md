## DI (Dependency Injection, 의존성 주입)

DI는 객체 간의 의존 관계를 외부에서 설정하는 디자인 패턴입니다. 이를 통해 객체는 의존하는 객체를 직접 생성하지 않고, 외부로부터 주입받아 사용합니다.

```java
// 의존성 주입 없이
public class UserService {
    private UserRepository userRepository = new UserRepository();
}

// 생성자를 통한 의존성 주입
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

// Spring에서의 사용
@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
```

## IOC (Inversion of Control, 제어의 역전)

IOC는 프로그램의 제어 흐름을 개발자가 아닌 프레임워크가 관리하는 개념입니다. Spring에서는 IOC 컨테이너가 객체의 생명주기와 의존성을 관리합니다.

```java
@Configuration
public class AppConfig {
    @Bean
    public UserService userService() {
        return new UserService(userRepository());
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepository();
    }
}

```

### PSA (Portable Service Abstraction, 일관된 서비스 추상화)

PSA는 환경과 세부 기술의 변경과 관계없이 일관된 방식으로 기술에 접근할 수 있게 해주는 개념입니다.

```java
// JDBC를 직접 사용
Connection c = DriverManager.getConnection("jdbc:mysql://localhost/test", "root", "password");

// Spring의 JdbcTemplate 사용 (PSA)
@Autowired
private JdbcTemplate jdbcTemplate;

jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> new User(rs.getLong("id"), rs.getString("name")));

```