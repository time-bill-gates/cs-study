## @Transactional

@Transactional은 메서드나 클래스에 적용하여 해당 범위에서 트랜잭션을 관리합니다. 이를 통해 데이터베이스 작업의 원자성, 일관성, 격리성, 지속성(ACID)을 보장합니다.

```java
@Service
public class UserService {
    @Transactional
    public void createUser(User user) {
// 사용자 생성 로직
    }
}
```

## 주요 속성

- propagation: 트랜잭션 전파 방식을 설정
- isolation: 트랜잭션 격리 수준을 설정
- timeout: 트랜잭션 타임아웃을 설정
- readOnly: 읽기 전용 트랜잭션 여부를 설정
- rollbackFor: 특정 예외 발생 시 롤백을 수행
- noRollbackFor: 특정 예외 발생 시 롤백을 수행하지 않음

예시:

```java
@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, timeout = 30, readOnly = false, rollbackFor = Exception.class)
public void complexTransaction() {
// 복잡한 트랜잭션 로직
}

```

1. 트랜잭션 전파(Propagation)

트랜잭션 전파는 이미 진행 중인 트랜잭션이 있을 때 새로운 트랜잭션을 어떻게 처리할지를 정의합니다.

주요 전파 옵션:

- REQUIRED (기본값): 기존 트랜잭션이 있으면 참여하고, 없으면 새로 생성
- REQUIRES_NEW: 항상 새로운 트랜잭션을 생성
- SUPPORTS: 기존 트랜잭션이 있으면 참여하고, 없으면 트랜잭션 없이 실행
- MANDATORY: 기존 트랜잭션이 반드시 있어야 함
- NEVER: 트랜잭션 없이 실행되어야 함
1. 트랜잭션 격리 수준(Isolation)

데이터베이스의 트랜잭션 격리 수준을 설정합니다.

- DEFAULT: 데이터베이스의 기본 격리 수준
- READ_UNCOMMITTED: 커밋되지 않은 데이터 읽기 가능
- READ_COMMITTED: 커밋된 데이터만 읽기 가능
- REPEATABLE_READ: 동일 데이터를 여러 번 읽어도 일관성 유지
- SERIALIZABLE: 완벽한 격리, 성능 저하 가능성

## @Transactional의 동작 원리

@Transactional은 AOP(Aspect-Oriented Programming)를 기반으로 동작합니다. Spring은 프록시를 생성하여 트랜잭션 관리 로직을 주입합니다.

```java
// 실제 코드
@Transactional
public void someMethod() {
// 비즈니스 로직
}

// AOP에 의해 생성되는 프록시 코드 (의사 코드)
public void someMethod() {
    TransactionStatus status = beginTransaction();
    try {
// 실제 비즈니스 로직 호출
        commitTransaction(status);
    } catch (Exception e) {
        rollbackTransaction(status);
        throw e;
    }
}

```

주의사항

- private 메서드에는 @Transactional이 적용되지 않습니다.
- 같은 클래스 내의 메서드 호출 시 트랜잭션이 적용되지 않을 수 있습니다 (프록시 우회).
- RuntimeException과 Error는 기본적으로 롤백을 발생시킵니다.

실제 사용 예시

```java
@Service
public class TransferService {
    @Autowired
    private AccountRepository accountRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void transfer(String fromAccountId, String toAccountId, BigDecimal amount) {
        Account fromAccount = accountRepository.findById(fromAccountId).orElseThrow();
        Account toAccount = accountRepository.findById(toAccountId).orElseThrow();

        fromAccount.withdraw(amount);
        toAccount.deposit(amount);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);
    }
}
```