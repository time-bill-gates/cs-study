## JPA (Java Persistence API)

JPA는 자바 애플리케이션에서 관계형 데이터베이스를 사용하는 방식을 정의한 인터페이스입니다. Hibernate는 JPA의 구현체 중 하나입니다.

## 영속성 컨텍스트 (Persistence Context)

영속성 컨텍스트는 엔티티를 영구 저장하는 환경을 말합니다. 이는 애플리케이션과 데이터베이스 사이에서 객체를 보관하는 가상의 데이터베이스 역할을 합니다.

주요 특징:

- 1차 캐시
- 동일성 보장
- 트랜잭션을 지원하는 쓰기 지연
- 변경 감지 (Dirty Checking)
- 지연 로딩 (Lazy Loading)

엔티티의 생명주기:

1. 비영속 (New/Transient): 영속성 컨텍스트와 관계가 없는 상태
2. 영속 (Managed): 영속성 컨텍스트에 저장된 상태
3. 준영속 (Detached): 영속성 컨텍스트에 저장되었다가 분리된 상태
4. 삭제 (Removed): 삭제된 상태

예시 코드:

```java
EntityManager em = emf.createEntityManager();
EntityTransaction tx = em.getTransaction();

try {
    tx.begin();

// 비영속 상태
    User user = new User();
    user.setName("John");

// 영속 상태
    em.persist(user);

// 준영속 상태
    em.detach(user);

// 삭제 상태
    em.remove(user);

    tx.commit();
} catch (Exception e) {
    tx.rollback();
} finally {
    em.close();
}
```

## JPA 동작 원리

JPA의 주요 동작 원리는 다음과 같습니다:

a. 엔티티 매핑:
Java 객체와 데이터베이스 테이블을 매핑합니다. 이는 주로 어노테이션을 통해 이루어집니다.

```java
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

// getters and setters
}
```

b. 영속성 컨텍스트 관리:
EntityManager를 통해 영속성 컨텍스트를 관리합니다.

c. 트랜잭션 관리:
JPA는 트랜잭션 내에서 동작하며, 트랜잭션 커밋 시점에 변경사항을 데이터베이스에 반영합니다.

d. JPQL (Java Persistence Query Language):
객체 지향 쿼리 언어를 사용하여 엔티티 객체를 대상으로 쿼리를 수행합니다.

```java
List<User> users = em.createQuery("SELECT u FROM User u WHERE u.name LIKE :name", User.class)
                     .setParameter("name", "John%")
                     .getResultList();
```

## N+1 문제

N+1 문제는 연관 관계에서 발생하는 성능 문제로, 하나의 쿼리로 N개의 결과를 가져온 후, 각 결과에 대해 추가적인 쿼리를 N번 실행하는 상황을 말합니다.

예시:

```java
@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<Comment> comments;
}

@Entity
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
}
```

문제 상황:

```java
List<Post> posts = em.createQuery("SELECT p FROM Post p", Post.class).getResultList();
for (Post post : posts) {
    System.out.println(post.getComments().size());// 이 부분에서 N번의 추가 쿼리 발생
}
```

해결 방법:

1. Fetch Join 사용:

```java
List<Post> posts = em.createQuery("SELECT p FROM Post p JOIN FETCH p.comments", Post.class).getResultList();

```

1. EntityGraph 사용:

```java
@EntityGraph(attributePaths = {"comments"})
@Query("SELECT p FROM Post p")
List<Post> findAllWithComments();
```

1. BatchSize 설정:

```java
@Entity
public class Post {
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @BatchSize(size = 100)
    private List<Comment> comments;
}
```

1. @Fetch(FetchMode.SUBSELECT) 사용:

```java
@Entity
public class Post {
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private List<Comment> comments;
}

```