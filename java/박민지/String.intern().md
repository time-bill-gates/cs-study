
JVM이 관리하는 String Constant Pool에서 문자열을 조회하여 존재하는 경우 반환, 아닌 경우 풀에 문자열을 등록하고 해당 문자열을 반환하는 메서드
- `"Hello"`와 같은 리터럴 문자열은 자바 컴파일러가 이를 **문자열 상수 풀**에 자동으로 저장합니다. 같은 문자열 리터럴이 여러 번 등장하면, 이미 상수 풀에 존재하는 객체를 재사용합니다.


```java
/**  
 * Returns a canonical representation for the string object. * <p>  
 * A pool of strings, initially empty, is maintained privately by the  
 * class {@code String}.  
 * <p>  
 * When the intern method is invoked, if the pool already contains a  
 * string equal to this {@code String} object as determined by  
 * the {@link #equals(Object)} method, then the string from the pool is  
 * returned. Otherwise, this {@code String} object is added to the  
 * pool and a reference to this {@code String} object is returned.  
 * <p>  
 * It follows that for any two strings {@code s} and {@code t},  
 * {@code s.intern() == t.intern()} is {@code true}  
 * if and only if {@code s.equals(t)} is {@code true}.  
 * <p>  
 * All literal strings and string-valued constant expressions are  
 * interned. String literals are defined in section {@jls 3.10.5} of the  
 * <cite>The Java Language Specification</cite>.  
 * * @return  a string that has the same contents as this string, but is  
 *          guaranteed to be from a pool of unique strings. */
public native String intern();
 ```

`StringTable` 클래스는 `RehashableHashTable` 클래스를 상속받는 클래스입니다.

이름과 코드에서 추측해보면, 해시 테이블의 균형이 맞지 않지 않을 때(특정 버킷에 데이터가 집중될 때) 해시 알고리즘을 변경해서 데이터가 테이블 전체에 고루 퍼지도록 할 수 있는 해시 테이블로 보입니다.

또한 `StringTable` 클래스는 내부에 정적 변수로 `StringTable`인 `_the_table`과 `CompactHashTable`인 `_shared_table` 필드를 가지고 있습니다.

`_the_table`을 정적 필드로 선언하고 `the_table()` 함수로 접근하는 것으로 미루어보아 `StringTable` 객체를 싱글턴으로 사용하기 위한 것으로 보입니다.

`CompactHashTable`은 Java의 CDS라는 기능을 위해 사용되는 해시 테이블입니다. CDS는 여러 JVM 프로세스가 공용으로 사용하는 메모리 공간에 로드된 클래스들을 모아놓고 공유하기 위한 목적으로 활용하는 공간입니다.

자세한 설명은 [Oracle Docs](https://docs.oracle.com/javase/9/vm/class-data-sharing.htm#JSJVM-GUID-7EAA3411-8CF0-4D19-BD05-DF5E1780AA91)와 [IBM Knowledge Center](https://www.ibm.com/support/knowledgecenter/en/SSYKE2_8.0.0/com.ibm.java.vm.80.doc/docs/shrc.html)에서 다루고 있습니다.

즉, 실질적으로 `shared_table`와 `the_table`이라는 두 개의 테
이블을 참조하고 있으며, `intern` 메서드가 호출되면 `shared_table`에서 먼저 검색해서 찾으면 반환합니다.

찾지 못한 경우에는 `RehashableHashTable`인 `the_table`에서 검색해보고 여기서도 찾지 못하면 `the_table`에 문자열을 추가한 뒤에 이를 반환하게 됩니다.

# References
---
- https://www.latera.kr/blog/2019-02-09-java-string-intern/