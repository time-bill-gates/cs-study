**예외 처리(Exception Handling)** 는 프로그램 실행 중 발생할 수 있는 오류를 처리하여 프로그램이 비정상 종료되지 않도록 하는 중요한 메커니즘입니다. 

# 예외의 종류

자바의 예외는 크게 두 가지로 분류되며, 각 예외는 다양한 유형과 처리 방식이 있습니다. 예외의 두 가지 주요 카테고리는 **Checked Exception**과 **Unchecked Exception**입니다. 여기에 더해, 자바는 **Error**라는 특별한 유형의 예외도 제공합니다. 

### 1. Checked Exception (체크 예외)
체크 예외는 **컴파일 타임**에 반드시 처리되어야 하는 예외입니다. 예외가 발생할 가능성이 있는 메서드에서는 예외를 직접 처리하거나, 메서드 시그니처에 `throws` 키워드를 통해 호출자에게 예외 처리를 위임해야 합니다.

#### 주요 Checked Exception
- **IOException**: 입출력 작업 중 발생하는 예외입니다. 예를 들어 파일을 읽거나 쓸 때 파일이 존재하지 않거나 접근할 수 없는 경우 발생합니다.
- **SQLException**: 데이터베이스 관련 작업 중 발생하는 예외입니다. 잘못된 SQL 쿼리나 데이터베이스 연결 오류 등이 이 예외를 발생시킵니다.
- **ClassNotFoundException**: JVM이 특정 클래스 파일을 찾지 못할 때 발생하는 예외입니다. 주로 동적으로 클래스를 로드할 때 발생합니다.

#### 특징
- 예외 처리를 강제합니다: `try-catch` 블록으로 처리하거나, 메서드에 `throws`로 명시해야 컴파일이 가능합니다.
- 프로그램의 예측 가능한 상황에서 발생합니다: 예를 들어 파일을 열 때 파일이 없을 수 있다는 가능성을 컴파일러가 알기 때문에 이를 처리하도록 요구합니다.

### 2. Unchecked Exception (언체크 예외)

Unchecked Exception은 **런타임**에 발생하며, 컴파일러가 예외 처리를 강제하지 않는 예외입니다. 이러한 예외들은 `RuntimeException`을 상속받는 예외들이며, 주로 프로그래머의 실수나 논리적인 오류로 인해 발생합니다.

#### 주요 Unchecked Exception
- **NullPointerException**: 객체가 null인 상태에서 그 객체의 메서드나 필드에 접근할 때 발생합니다.
- **ArrayIndexOutOfBoundsException**: 배열의 인덱스 범위를 벗어날 때 발생합니다.
- **IllegalArgumentException**: 메서드가 부적절한 인수를 받았을 때 발생합니다.

#### 특징
- 예외 처리가 필수가 아닙니다: 이러한 예외들은 대부분 프로그래머의 실수로 발생하며, 예외 처리를 강제하지 않아도 됩니다.
- 프로그램의 실행 중 예기치 않게 발생할 수 있습니다: 주로 논리적 오류나 프로그래머가 미처 고려하지 못한 상태에서 발생합니다.

### 3. Error (에러)
**Error**는 예외와는 다른 특수한 유형으로, 주로 **JVM 레벨에서 발생하는 심각한 오류**입니다. Error는 보통 프로그램에서 복구할 수 없는 문제를 나타내며, 이들은 `java.lang.Error` 클래스를 상속받습니다. 개발자가 직접 처리하는 것이 아니라, 대부분의 경우 **프로그램이 비정상 종료**되게 만듭니다.

#### 주요 Error
- **StackOverflowError**: 메서드 호출이 너무 깊어져 스택 메모리가 다 차서 발생합니다. 주로 재귀 호출이 잘못된 경우 발생합니다.
- **OutOfMemoryError**: JVM 힙 메모리가 부족할 때 발생합니다. 객체를 너무 많이 생성하거나 메모리 누수가 있을 때 발생합니다.
- **NoClassDefFoundError**: 컴파일된 클래스가 실행 중에 JVM에서 찾을 수 없을 때 발생합니다. 런타임 시 클래스 파일이 누락된 경우 발생할 수 있습니다.

#### 특징
- 대부분 복구 불가능한 심각한 문제입니다: 이러한 문제는 자바 프로그램에서 처리하는 것이 어렵기 때문에 일반적으로 예외 처리 블록으로 다루지 않습니다.
- 프로그램의 **환경 문제**나 **JVM의 문제**로 인해 발생할 수 있습니다.

### 예외 계층 구조

```
java.lang.Throwable
    |-- java.lang.Exception (Checked Exception)
    |     |-- java.lang.RuntimeException (Unchecked Exception)
    |
    |-- java.lang.Error (Error)
```

- **Throwable**: 모든 예외와 에러의 최상위 클래스입니다.
- **Exception**: 프로그램에서 발생할 수 있는 예외를 나타내며, 대부분의 예외 처리가 이 클래스 또는 하위 클래스를 통해 이루어집니다.
- **RuntimeException**: 언체크 예외의 부모 클래스입니다.
- **Error**: 복구할 수 없는 심각한 문제를 나타내며, 대부분 JVM과 관련이 있습니다.


# 예외 처리 구조 

### try-catch-finally (Java 1)

```java
try {
    // 예외가 발생할 가능성이 있는 코드
} catch (ExceptionType e) {
    // 예외 처리 코드
} finally {
    // 예외 발생 여부와 관계없이 항상 실행되는 코드 (선택적)
}
```

### try-with-resources (Java 7)

자바 7 이전에는 파일이나 데이터베이스 연결과 같은 자원을 사용한 후 반드시 `finally` 블록에서 자원을 수동으로 닫아야 했습니다. 자바 7에서는 **try-with-resources** 문법이 도입되어, `AutoCloseable` 인터페이스를 구현한 객체는 try 블록이 끝나면 자동으로 닫히게 됩니다.

```java
try (BufferedReader br = new BufferedReader(new FileReader("file.txt"))) {
    String line = br.readLine();
} catch (IOException e) {
    e.printStackTrace();
}
```

### Multi-catch (Java 7)

자바 7에서는 하나의 `catch` 블록에서 여러 예외를 처리할 수 있는 **멀티 캐치** 기능이 추가되었습니다. 이를 통해 중복된 예외 처리 코드를 줄일 수 있습니다.

```java
try {
    // Some code
} catch (IOException | SQLException e) {
    e.printStackTrace();
}
```

### try-with-resources 개선 (Java 9)

자바 9에서는 **try-with-resources** 문법이 개선되었습니다. 자바 7에서는 `try-with-resources`에서 사용되는 객체를 반드시 선언해야 했지만, 자바 9부터는 **이미 선언된 자원**을 사용하여 `try-with-resources`를 사용할 수 있습니다.

```java
BufferedReader br = new BufferedReader(new FileReader("file.txt"));
try (br) {
     String line = br.readLine();
} catch (IOException e) {
    e.printStackTrace();
}
```

### `switch` 표현식의 `throw` (Java 14)

자바 14에서는 `switch` 문을 표현식으로 사용할 수 있게 되었으며, 이때 예외를 던질 수 있는 기능이 추가되었습니다. 예를 들어, `switch` 블록 내에서 특정 조건에 따라 예외를 던질 수 있습니다.

```java
int result = switch (value) {
    case 1 -> throw new IllegalArgumentException("잘못된 값입니다");
    default -> 0;
};
```

### 주요 키워드
1. **try**: 예외가 발생할 가능성이 있는 코드를 감쌉니다.
2. **catch**: try 블록에서 발생한 특정 예외를 처리합니다. 여러 개의 catch 블록을 사용해 다양한 예외를 처리할 수 있습니다.
3. **finally**: 예외 발생 여부와 관계없이 항상 실행되는 블록입니다. 자원 해제(예: 파일 닫기, 데이터베이스 연결 종료) 등에 주로 사용됩니다.
4. **throws**: 메서드가 체크 예외를 던질 수 있음을 선언합니다.
5. **throw**: 예외를 명시적으로 발생시킬 때 사용합니다.

### 예외 처리의 중요성
- **안정성**: 예외 처리를 통해 프로그램이 예기치 않게 종료되지 않고, 예외 상황에 적절히 대응할 수 있습니다.
- **코드 가독성**: 예외 처리를 사용하면 정상적인 로직과 오류 처리 로직을 분리하여 코드 가독성을 높일 수 있습니다.
- **자원 관리**: 파일, 네트워크 연결과 같은 자원을 사용할 때, 예외 발생 여부와 관계없이 자원을 적절히 해제할 수 있도록 `finally` 블록을 사용합니다.