
# 자료형

Java의 자료형(data type)은 크게 **기본형(Primitive types)**과 **참조형(Reference types)**으로 나눌 수 있습니다.

### 1. 기본형 (Primitive types)

| 자료형         | 크기 (비트)    | 기본 값     | 범위                                                     | 설명               |
| ----------- | ---------- | -------- | ------------------------------------------- | ---------------- |
| **byte**    | 8          | 0        | -128 ~ 127                                             | 작은 정수 값을 다룰 때 사용 |
| **short**   | 16         | 0        | -32,768 ~ 32,767                                       | 작은 범위의 정수 값을 다룸  |
| **int**     | 32         | 0        | -2,147,483,648 ~ 2,147,483,647                         | 일반적인 정수 값에 사용    |
| **long**    | 64         | 0L       | -9,223,372,036,854,775,808 ~ 9,223,372,036,854,775,807 | 매우 큰 정수 값에 사용    |
| **float**   | 32         | 0.0f     | 1.4E-45 ~ 3.4E+38                                      | 단정밀도 부동소수점 수     |
| **double**  | 64         | 0.0d     | 4.9E-324 ~ 1.7E+308                                    | 배정밀도 부동소수점 수     |
| **char**    | 16         | '\u0000' | 0 ~ 65,535                                             | 유니코드 문자          |
| **boolean** | 1 (논리적 크기) | false    | true 또는 false                                          | 논리 값 (참 또는 거짓)   |

### 2. 참조형 (Reference types)

참조형은 실제 데이터가 저장된 메모리 주소를 참조하는 형태입니다. 참조형에는 배열, 클래스, 인터페이스 등이 포함됩니다.

#### 대표적인 참조형
- **String**: 문자열을 저장하는 참조형 클래스. `new` 키워드 없이 문자열을 생성할 수 있는 특수한 클래스입니다.
- **배열 (Array)**: 같은 타입의 데이터를 순차적으로 저장하는 자료구조.
- **클래스 (Class)**: 사용자가 정의한 데이터 타입이며, 객체 지향 프로그래밍의 기본 단위입니다.

### 기본형 vs 참조형의 차이점
- 기본형은 값 자체를 저장하지만, 참조형은 값이 저장된 메모리의 주소를 저장합니다.
- 기본형은 고정된 크기의 메모리를 사용하지만, 참조형은 동적으로 메모리를 할당받을 수 있습니다.


# Wrapper Class

Java의 **Wrapper Class**는 기본 자료형(Primitive types)을 객체(Object)로 감싸기 위해 제공되는 클래스입니다. 

### **Wrapper Class의 사용 이유**

1. **컬렉션 사용**: 기본 자료형을 사용할 수 없는 Java 컬렉션(List, Set, Map 등)에 데이터를 저장할 때 필요합니다.
2. **유틸리티 메서드**: Wrapper Class는 숫자 변환, 문자열 변환 등 유용한 메서드를 제공합니다.
3. **객체와의 통합**: 객체지향적인 프로그래밍에서 기본 자료형을 객체로 다룰 수 있어야 할 때 사용합니다.
4. **상수 제공**: `Integer.MAX_VALUE`, `Double.NaN` 등 Wrapper Class는 유용한 상수를 제공합니다.


### **기본 자료형과 대응되는 Wrapper Class**

| 기본 자료형    | Wrapper Class |
| --------- | ------------- |
| `byte`    | `Byte`        |
| `short`   | `Short`       |
| `int`     | `Integer`     |
| `long`    | `Long`        |
| `float`   | `Float`       |
| `double`  | `Double`      |
| `char`    | `Character`   |
| `boolean` | `Boolean`     |

### **주요 기능**

1. **기본형을 객체로 감싸기 (Boxing)**: 기본 자료형을 Wrapper Class로 변환하여 객체로 사용할 수 있습니다.
    ```java
    int num = 10;
    Integer wrappedNum = Integer.valueOf(num);  // 기본형을 객체로 변환
    ```

2. **객체를 기본형으로 변환 (Unboxing)**: Wrapper Class 객체를 다시 기본 자료형으로 변환할 수 있습니다.
    ```java
    Integer wrappedNum = 10;
    int num = wrappedNum.intValue();  // 객체를 기본형으로 변환
    ```

3. **자동 Boxing과 Unboxing**: Java 5부터는 **자동 Boxing과 Unboxing** 기능이 추가되어, 기본형과 Wrapper Class 간의 변환이 자동으로 이루어집니다.
    ```java
    int num = 10;
    Integer wrappedNum = num;  // 자동 Boxing
    int anotherNum = wrappedNum;  // 자동 Unboxing
    ```
