Spring에서 트랜잭션 관리는 매우 중요한 역할을 하며, 특히 데이터베이스와 관련된 작업에서 데이터 일관성과 무결성을 유지하는 데 사용됩니다. Spring은 주로 `@Transactional` 어노테이션을 통해 선언적 트랜잭션 관리를 제공합니다. 이를 통해 복잡한 트랜잭션 처리 로직을 코드에 직접 구현하지 않고도, 트랜잭션의 시작, 커밋, 롤백을 자동으로 처리할 수 있습니다.

## Spring 트랜잭션 관리의 주요 개념

### 1. **트랜잭션 전파(propagation)**

트랜잭션 전파는 메서드 호출 간 트랜잭션의 행동 방식을 정의합니다. Spring은 다양한 트랜잭션 전파 옵션을 제공합니다.

- `REQUIRED`: 기본값으로, 현재 트랜잭션이 없으면 새로운 트랜잭션을 시작하고, 이미 트랜잭션이 있으면 해당 트랜잭션에 참여합니다.
- `REQUIRES_NEW`: 항상 새로운 트랜잭션을 시작하며, 기존 트랜잭션이 있으면 이를 일시 중단합니다.
- `NESTED`: 기존 트랜잭션 내에서 중첩된 트랜잭션을 시작합니다.
- `SUPPORTS`: 트랜잭션이 있으면 트랜잭션 내에서 실행하고, 없으면 트랜잭션 없이 실행합니다.
- `NOT_SUPPORTED`: 트랜잭션을 중단하고, 트랜잭션 없이 실행합니다.
- `MANDATORY`: 반드시 트랜잭션 내에서 실행되어야 하며, 트랜잭션이 없으면 예외가 발생합니다.
- `NEVER`: 트랜잭션이 있으면 예외를 발생시키고, 트랜잭션 없이 실행됩니다.

### 2. **트랜잭션 격리 수준(isolation level)**

트랜잭션 격리 수준은 트랜잭션 간에 데이터가 어떻게 격리되는지를 정의합니다. 일반적으로 다음과 같은 격리 수준을 제공합니다:

- `DEFAULT`: 기본적으로 데이터베이스의 격리 수준을 따릅니다.
- `READ_UNCOMMITTED`: 다른 트랜잭션에서 커밋되지 않은 데이터를 읽을 수 있습니다 (Dirty Read).
- `READ_COMMITTED`: 다른 트랜잭션에서 커밋한 데이터만 읽을 수 있습니다.
- `REPEATABLE_READ`: 동일한 트랜잭션 내에서 동일한 데이터를 여러 번 읽을 때 항상 동일한 결과를 반환합니다.
- `SERIALIZABLE`: 가장 엄격한 격리 수준으로, 트랜잭션이 직렬화된 것처럼 동작합니다. 동시성 성능이 저하될 수 있습니다.

### 3. **트랜잭션 롤백 규칙**

Spring에서는 기본적으로 `RuntimeException`이나 `Error`가 발생하면 트랜잭션이 자동으로 롤백됩니다. 하지만, 다른 예외에 대해서도 롤백을 원할 경우, `@Transactional`의 `rollbackFor` 속성을 사용하여 처리할 수 있습니다.

```java
@Transactional(rollbackFor = Exception.class)
public void someMethod() throws Exception {
    // 이 메서드에서 Exception이 발생하면 트랜잭션 롤백
}
```

또한 특정 예외에 대해 롤백하지 않도록 설정할 수도 있습니다.

```java
@Transactional(noRollbackFor = MyCustomException.class)
public void anotherMethod() throws MyCustomException {
    // 이 메서드에서 MyCustomException이 발생하더라도 롤백하지 않음
}
```

## 트랜잭션 사용 방법

### 1. **`@Transactional` 어노테이션을 사용한 선언적 트랜잭션**

Spring에서는 `@Transactional` 어노테이션을 사용하여 간단하게 트랜잭션을 관리할 수 있습니다. 주로 서비스 계층에서 사용되며, 메서드나 클래스 레벨에서 설정할 수 있습니다.

```java
@Service
public class MyService {

    @Autowired
    private MyRepository myRepository;

    @Transactional
    public void executeBusinessLogic() {
        // 트랜잭션이 시작됨
        myRepository.save(new MyEntity());

        // 예외가 발생하면 트랜잭션 롤백
        if (someCondition) {
            throw new RuntimeException("Rollback this transaction");
        }

        // 정상적으로 끝나면 트랜잭션 커밋
    }
}
```

### 2. **프로그래밍 방식 트랜잭션 관리**

프로그래밍 방식으로 트랜잭션을 관리하려면 `PlatformTransactionManager`를 사용합니다. 이를 통해 더 세밀하게 트랜잭션을 제어할 수 있습니다.

```java
@Service
public class MyService {

    @Autowired
    private PlatformTransactionManager transactionManager;

    public void executeWithProgrammaticTransaction() {
        TransactionDefinition definition = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(definition);

        try {
            // 트랜잭션 시작
            myRepository.save(new MyEntity());

            // 예외 발생 시 롤백
            if (someCondition) {
                throw new RuntimeException("Rollback this transaction");
            }

            // 커밋
            transactionManager.commit(status);
        } catch (Exception e) {
            // 롤백
            transactionManager.rollback(status);
            throw e;
        }
    }
}
```

### 3. **트랜잭션 관리의 모범 사례**

- **서비스 계층에서 트랜잭션 관리**: 비즈니스 로직이 구현된 서비스 계층에서 트랜잭션을 관리하는 것이 일반적인 모범 사례입니다.
- **짧고 효율적인 트랜잭션**: 트랜잭션은 가능한 한 짧게 유지하는 것이 좋습니다. I/O 작업이나 긴 프로세싱 작업이 포함되지 않도록 해야 합니다.
- **예외 처리**: 트랜잭션 롤백은 적절한 예외 처리에 따라 수행되어야 하며, 롤백이 필요한 예외와 아닌 예외를 구분하는 것이 중요합니다.

이와 같은 트랜잭션 관리 전략을 통해 Spring에서 안정적이고 효율적인 트랜잭션 처리가 가능합니다.