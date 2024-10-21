Java에서의 Functional 프로그래밍(함수형 프로그래밍)은 함수형 패러다임을 적용하여 코드를 작성하는 방식으로, 특히 Java 8 이후부터 제공되는 람다식, 스트림 API, `java.util.function` 패키지의 다양한 기능을 통해 구현할 수 있습니다. 

Functional 프로그래밍을 사용하면 다음과 같은 장점이 있습니다:
- **코드 간결성:** 복잡한 로직을 단순하게 표현할 수 있습니다.
- **가독성 향상:** 불변성, 순수 함수를 사용함으로써 코드의 예측 가능성이 높아집니다.
- **병렬 처리:** 스트림 API를 통해 데이터 병렬 처리를 쉽게 구현할 수 있습니다.

# 주요 개념

### **람다 표현식(Lambda Expressions)**:
   - 익명 함수를 간결하게 표현할 수 있는 방식으로, 함수형 인터페이스(하나의 추상 메서드를 가지는 인터페이스)와 함께 사용됩니다.
   - 예: 
     ```java
     (int a, int b) -> a + b;
     ```

### **함수형 인터페이스(Functional Interfaces)**:
   - 하나의 추상 메서드만을 가지는 인터페이스입니다. 대표적으로 `Predicate`, `Function`, `Supplier`, `Consumer` 등이 있습니다.
   - 예: 
     ```java
     @FunctionalInterface
     public interface MyFunction {
         int apply(int a, int b);
     }
     ```

### **스트림 API(Stream API)**:
   - 컬렉션 데이터를 처리할 때 함수형 방식으로 연산을 수행하는 강력한 도구입니다. `map`, `filter`, `reduce`와 같은 메서드를 통해 데이터 변환 및 필터링을 쉽게 수행할 수 있습니다.
   - 예: 
     ```java
     List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
     names.stream()
          .filter(name -> name.startsWith("A"))
          .forEach(System.out::println);
     ```

### **고차 함수(Higher-Order Functions)**:
   - 함수를 인자로 받거나 함수를 반환하는 함수를 의미합니다.
   - 예:
     ```java
     public static Function<Integer, Integer> multiplyBy(int factor) {
         return x -> x * factor;
     }
     ```

### **불변성(Immutability)**:
   - 함수형 프로그래밍에서 상태 변화(변경 가능한 상태)를 피하고, 모든 데이터를 불변으로 다루는 것을 중요시합니다.
   - 예를 들어, Java에서 `Stream`은 데이터를 변경하지 않고, 새로운 값을 반환하는 방식을 사용합니다.

### **순수 함수(Pure Functions)**:
   - 같은 입력에 대해 항상 같은 출력을 반환하며, 외부 상태를 변경하지 않는 함수입니다. 이러한 함수는 예측 가능하고 테스트하기 쉽습니다.

### **메서드 참조(Method References)**:
   - 이미 존재하는 메서드를 람다 표현식처럼 사용할 수 있는 기능입니다.
   - 예: 
     ```java
     List<Integer> numbers = Arrays.asList(1, 2, 3);
     numbers.forEach(System.out::println);  // 메서드 참조 사용
     ```
