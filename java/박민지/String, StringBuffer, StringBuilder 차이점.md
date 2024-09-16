# **String, StringBuffer, StringBuilder의 차이점**

### **String (불변 객체)**
   - **특징**: `String` 객체는 **불변(immutable)** 입니다. 문자열을 수정하는 작업이 필요할 때는 새로운 `String` 객체가 생성됩니다.
   - **성능**: 문자열을 자주 변경하는 작업에서 `String`을 사용하면 불필요한 객체가 많이 생성되어 **성능이 떨어집니다**.
   - **스레드 안전성**: `String`은 불변 객체이므로 스레드로부터 안전합니다. 여러 스레드가 동시에 같은 문자열을 읽더라도 문제가 없습니다.

   ```java
   String str = "Hello";
   str = str + " World";  // 새로운 String 객체가 생성됩니다.
   ```

### **StringBuffer (가변 객체, 스레드 안전)**
   - **특징**: `StringBuffer`는 **가변(mutable)** 객체로, 문자열을 수정할 수 있습니다. 문자열이 자주 변경되는 경우 사용하면 성능이 향상됩니다.
   - **성능**: `String`과 달리, 기존 객체를 수정하므로 새로운 객체를 생성하지 않아 **성능이 좋습니다**.
   - **스레드 안전성**: `StringBuffer`는 내부적으로 **synchronized**가 적용되어 있어 **스레드 안전(thread-safe)**합니다. 여러 스레드가 동시에 같은 객체에 접근할 때도 문제가 발생하지 않습니다.

   ```java
   StringBuffer buffer = new StringBuffer("Hello");
   buffer.append(" World");  // 같은 객체에 문자열을 추가합니다.
   ```

### **StringBuilder (가변 객체, 스레드 안전하지 않음)**
   - **특징**: `StringBuilder`는 `StringBuffer`와 거의 동일한 기능을 제공하지만, **스레드 안전성을 제공하지 않습니다**.
   - **성능**: `StringBuilder`는 **synchronized**가 적용되지 않으므로, 스레드 동기화가 필요 없는 경우 `StringBuffer`보다 **더 빠릅니다**. 따라서 단일 스레드 환경에서 성능 최적화를 위해 사용됩니다.

   ```java
   StringBuilder builder = new StringBuilder("Hello");
   builder.append(" World");  // 같은 객체에 문자열을 추가합니다.
   ```

### 결론:
- **String**은 불변 객체로, 자주 변하지 않는 문자열을 처리할 때 사용.
- **StringBuffer**는 가변 객체로, 스레드 안전성이 필요할 때 사용.
- **StringBuilder**는 가변 객체로, 스레드 안전성이 필요 없을 때 사용하여 성능 최적화.