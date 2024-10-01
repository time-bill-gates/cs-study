## MVC

MVC(Model-View-Controller)는 애플리케이션을 세 가지 주요 컴포넌트로 분리하는 소프트웨어 디자인 패턴입니다.

- Model: 데이터와 비즈니스 로직을 담당합니다.
- View: 사용자 인터페이스를 담당합니다. 모델의 데이터를 표시합니다.
- Controller: 사용자 입력을 처리하고, 모델과 뷰 사이의 상호작용을 조정합니다.

Spring MVC에서의 구현:

```java
// Model
@Entity
public class User {
    @Id
    private Long id;
    private String name;
// getters and setters
}

// Controller
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        return "userList";
    }
}

// View (userList.jsp)
<c:forEach items="${users}" var="user">
    <p>${user.name}</p>
</c:forEach>

```

## 서블릿(Servlet)

서블릿은 Java EE의 사양 중 하나로, 웹 애플리케이션에서 동적 콘텐츠를 생성하는 서버 측 프로그램입니다.

```java
public class HelloServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>Hello, World!</h1>");
        out.println("</body></html>");
    }
}
```

## 필터(Filter)

필터는 서블릿 컨테이너와 서블릿 사이에서 요청과 응답을 가로채고 수정할 수 있는 Java 클래스입니다.

```java
public class LoggingFilter implements Filter {
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("Request received");
        chain.doFilter(request, response);
        System.out.println("Response sent");
    }
}

```

## Filter vs Interceptor

- Filter는 DispatcherServlet 이전에 동작하며, 서블릿 스펙에 정의되어 있습니다.
- Interceptor는 DispatcherServlet과 Controller 사이에서 동작하며, Spring MVC의 기능입니다.
- Filter는 웹 관련 요소(HttpServletRequest)를 처리하지만, Interceptor는 Spring의 요소(Handler, ModelAndView)를 처리할 수 있습니다.

## Spring MVC에서의 요청 처리 과정

1. 클라이언트가 요청을 보냅니다.
2. DispatcherServlet이 요청을 받습니다.
3. DispatcherServlet은 HandlerMapping을 통해 요청을 처리할 Controller를 찾습니다.
4. DispatcherServlet은 HandlerAdapter를 통해 Controller의 메소드를 호출합니다.
5. Controller는 비즈니스 로직을 처리하고 Model을 업데이트합니다.
6. Controller는 View 이름을 반환합니다.
7. ViewResolver가 View 이름을 실제 View로 해석합니다.
8. View가 Model 데이터를 사용하여 응답을 생성합니다.
9. DispatcherServlet이 응답을 클라이언트에게 보냅니다.

이 과정을 코드로 표현하면:

```java
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoggingInterceptor());
    }
}

public class LoggingInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Request intercepted");
        return true;
    }
}

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("message", "Welcome home!");
        return "home";
    }
}
```