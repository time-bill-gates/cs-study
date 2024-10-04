**테이블 간의 공통 열**을 기준으로 **가로 방향**으로 결합하여, **연관된 데이터**를 가져오는 방법입니다.

# Join 종류
### **INNER JOIN**
- **INNER JOIN**은 두 테이블에서 공통으로 매칭되는 데이터만 조회하는 방식입니다. 즉, 각 테이블의 조건이 일치하는 행만 결과에 포함됩니다.

**예시**:
- 테이블 A: 고객 정보
- 테이블 B: 주문 정보  
    이 두 테이블을 `INNER JOIN`으로 결합하면, 주문을 한 고객들의 정보만 조회할 수 있습니다.

### **LEFT JOIN, RIGHT JOIN (LEFT / RIGHT OUTER JOIN)****
- `LEFT JOIN` 또는 `LEFT OUTER JOIN`은 왼쪽 테이블의 모든 데이터와 오른쪽 테이블에서 일치하는 데이터를 결합하고, 오른쪽에 없는 데이터는 `NULL`로 표시됩니다.
	- **TIP**: `LEFT JOIN`은 테이블 순서가 중요합니다. 여러 번의 `JOIN`을 사용할 때, 시작을 `LEFT JOIN`으로 했다면 나머지도 `LEFT JOIN`으로 통일하는 것이 좋습니다.
- **Right Join**은 `LEFT JOIN`과 반대로 오른쪽 테이블의 모든 데이터와 왼쪽 테이블의 일치하는 데이터를 결합합니다. 왼쪽 테이블에 없는 데이터는 `NULL`로 표시됩니다.

### **FULL JOIN (FULL OUTER JOIN)**
- **FULL JOIN**은 두 테이블의 모든 데이터를 결합합니다. 매칭되는 데이터는 모두 결합되고, 일치하지 않는 행은 `NULL` 값이 들어갑니다. MySQL에서 기본적으로는 `FULL JOIN`이 지원되지 않지만, `UNION`을 이용하여 유사한 결과를 만들 수 있습니다.

```sql
(select * from topic LEFT JOIN autor on topic.auther_id = authoer.id)) 
UNION 
(select * from topic RIGHT JOIN autor on topic.auther_id = authoer.id))
```

### **CROSS JOIN**
- **CROSS JOIN**은 두 테이블 간의 **카르테시안 곱**을 생성합니다. 즉, 각 테이블의 모든 행이 결합되므로 매우 큰 결과가 나올 수 있습니다.
```sql
SELECT * FROM t1 CROSS JOIN t2;
```

### **NATURAL JOIN**
- **NATURAL JOIN**은 테이블 간에 동일한 이름을 가진 모든 열을 자동으로 매칭하는 JOIN 유형입니다. **USING** 절 없이 자동으로 일치하는 열을 찾아 결합합니다.

```sql
SELECT * FROM t1 NATURAL JOIN t2;
```

### **STRAIGHT_JOIN**

**STRAIGHT_JOIN**은 MySQL에서만 지원되는 특수한 JOIN 유형으로, 왼쪽 테이블을 항상 먼저 읽습니다. 옵티마이저가 잘못된 순서로 테이블을 처리할 때 이를 최적화하기 위해 사용될 수 있습니다.

```sql
SELECT * FROM t1 STRAIGHT_JOIN t2 ON t1.id = t2.id;
```


# Join 구현 방식
### **Nested Loop Join** (중첩 반복문 방식, O(NM))
**Nested-Loop Join**(NLJ)은 MySQL에서 테이블 간 **JOIN**을 수행하는 가장 기본적인 알고리즘입니다. NLJ는 여러 테이블을 결합할 때 외부 테이블의 각 행을 기준으로 내부 테이블의 행과 일치하는지를 확인하는 방식으로 동작합니다. 테이블이 많을수록 이러한 과정이 반복되며, 이 때문에 **NLJ**는 성능이 데이터 양에 따라 큰 차이를 보일 수 있습니다.

#### Nested-Loop Join 작동 방식
- **NLJ**는 첫 번째 테이블에서 한 행씩 데이터를 읽어오고, 그 행을 기준으로 내부 루프에서 다음 테이블의 데이터를 비교합니다. 모든 테이블이 **JOIN** 조건을 만족할 때 결과를 반환하는 방식입니다.\

- Table, Join Type이 각각 t1 range, t2 ref, t3 ALL인 테이블 세 개를 join할 때, 다음과 같습니다.
```pseudo
for each row in t1 matching range {
  for each row in t2 matching reference key {
    for each row in t3 {
      if row satisfies join conditions, send to client
    }
  }
}
```

- **Outer Loop**: 가장 바깥쪽 루프는 첫 번째 테이블에서 시작하여 한 행씩 가져옵니다. 외부 테이블의 각 행은 내부 테이블들과 **JOIN** 조건을 비교하기 위해 사용됩니다.
- **Inner Loop**: 내부 루프는 외부 루프에서 전달된 행과 내부 테이블에서 해당 조건을 만족하는 행을 찾아 비교합니다.
- **중첩 구조**: 내부 테이블이 많아질수록 루프가 중첩되며, 그만큼 처리 시간이 길어질 수 있습니다.

#### NLJ의 성능 이슈
- **NLJ**는 작은 데이터 세트나 적절한 인덱스가 있는 경우에는 비교적 효율적으로 동작할 수 있습니다. 그러나, 내부 테이블의 행을 반복적으로 읽기 때문에 테이블의 크기가 크거나 인덱스가 없을 경우 성능이 저하될 수 있습니다. 특히, 테이블을 반복적으로 읽어야 하므로 디스크 I/O 비용이 커집니다.
	- 예를 들어, **t1**에서 100개의 행이 반환되고, **t2**에서 1,000개의 행, **t3**에서 10,000개의 행이 있을 때, **t1**의 각 행마다 **t2**와 **t3**의 모든 행을 읽어야 한다면 총 **100 * 1,000 * 10,000 = 1억** 번의 비교가 필요할 수 있습니다.

#### 최적화 방안
- **인덱스 사용**: **Nested-Loop Join**의 성능을 높이기 위해서는 인덱스를 잘 설계하는 것이 중요합니다. 특히 **reference key**로 사용되는 열에 적절한 인덱스가 있을 경우, 내부 루프에서 테이블을 훨씬 빠르게 검색할 수 있습니다.
- **join_buffer_size** 조정: 메모리를 충분히 할당하여 내부 루프에서의 반복적인 I/O를 줄이는 것도 성능 향상에 도움이 됩니다.
- **JOIN 알고리즘 선택**: MySQL 8.0 이상에서는 **Hash Join** 등 다른 JOIN 알고리즘을 지원하므로, 상황에 맞는 알고리즘을 사용하는 것이 좋습니다.
- **조건 Pushdown**: 조건을 내부 루프나 깊은 단계로 전달하는 대신, 외부 루프나 조기 단계에서 필터링 조건을 평가하도록 하여 적은 데이터를 내부 루프에서 처리하게 만듭니다.


### **Block Nested Loop Join** (블록 중첩 반복문 방식, O(NM))
**Nested Loop Join**의 확장된 형태로, 한 테이블의 데이터를 메모리 버퍼인 **Join 버퍼**에 저장하여 Disk I/O를 줄이고, 그 데이터를 다른 테이블과 비교하여 조인을 수행하는 방식입니다.

#### Block Nested-Loop Join 작동 방식
- 첫 번째 테이블의 데이터를 **JOIN 버퍼**에 저장합니다.
- 두 번째 테이블의 각 행과 **JOIN 버퍼**에 있는 데이터를 비교하여 매칭되는 행을 찾습니다.
- 매칭되는 결과가 있으면 해당 결과를 출력합니다.

```pseudo
FOR each block of rows in table1 {       -- 첫 번째 테이블의 데이터를 블록 단위로 읽어온다.
    LOAD rows into join buffer;          -- 블록 단위 데이터를 JOIN 버퍼에 저장한다.
    FOR each row in table2 {             -- 두 번째 테이블에서 각 행을 반복한다.
        FOR each row in join buffer {    -- JOIN 버퍼 내의 각 행을 반복한다.
            IF join_condition_matches {  -- 두 테이블 간의 JOIN 조건이 일치하는지 확인한다.
                OUTPUT joined_row;       -- 매칭되는 행이 있으면 결과를 출력한다.
            }
        }
    }
}
```

#### 사용 시 주의 사항
- **메모리 사용량**:
    - **JOIN 버퍼** 크기에 따라 메모리 사용량이 달라집니다. 큰 데이터 세트를 처리할 때는 충분한 메모리를 확보해야 하며, 그렇지 않으면 성능이 오히려 저하될 수 있습니다.
    - **join_buffer_size** 설정을 통해 **JOIN 버퍼**의 크기를 조정할 수 있습니다. 이 값이 너무 작으면 여러 번의 디스크 I/O가 발생할 수 있습니다.
- **인덱스의 부재**:
    - **BNL Join**은 인덱스가 없는 테이블에 대해서만 사용됩니다. 인덱스가 있는 경우에는 더 효율적인 **Indexed Nested Loop Join**이나 **Batched Key Access (BKA)** 알고리즘을 사용하는 것이 좋습니다.
    - 인덱스를 적절히 활용할 수 있으면 **BNL Join**보다 더 효율적인 **JOIN** 방식을 선택하는 것이 좋습니다.
- **대규모 데이터 세트 처리**:
    - 대규모 데이터 세트를 처리할 때 **BNL Join**은 여전히 느릴 수 있습니다. 메모리가 충분히 있더라도, 처리할 데이터가 많아지면 성능이 저하될 수 있습니다.
- **BNL Join**은 인덱스가 없는 테이블에서만 사용됩니다. 만약 인덱스가 존재한다면, 다른 **JOIN** 알고리즘을 사용하는 것이 더 효율적입니다.
	- 따라서 BNL 방식으로 수행될 때 type은 **ALL (Table Full Scan), index (Index Full Scan) 또는 range**입니다.

#### 최적화 방안
- **join_buffer_size 조정**:
    - **JOIN 버퍼** 크기를 적절하게 설정하는 것이 중요합니다. **join_buffer_size**를 크게 설정하면, 더 많은 데이터를 메모리 상에서 처리할 수 있으므로 디스크 I/O를 줄일 수 있습니다.
    - 기본값은 256KB 정도로 설정되어 있지만, 시스템 메모리 상태에 맞춰 적절히 조정해야 합니다. 메모리가 충분할 경우 이를 크게 설정하면 성능이 향상될 수 있습니다.
- **인덱스 사용 고려**:
    - 가능하다면 테이블에 인덱스를 추가하여 **BNL Join** 대신 **Indexed Nested Loop Join**이나 **Batched Key Access (BKA)**와 같은 더 효율적인 알고리즘을 사용하도록 합니다.
- **필터 조건 활용**:
    - **WHERE** 절이나 **JOIN** 조건을 통해 처리할 데이터의 양을 줄이는 것도 중요한 최적화 방법입니다. 불필요한 데이터를 사전에 필터링하면 처리해야 할 데이터의 양이 줄어들어 **BNL Join**의 성능이 향상됩니다.
- **최적화 힌트 사용**:
    - MySQL의 **optimizer_switch** 변수를 통해 **BNL Join**의 사용 여부를 제어할 수 있습니다. 기본적으로 **block_nested_loop**가 활성화되어 있지만, 필요시 이를 비활성화하거나 다른 **JOIN** 방법을 사용할 수 있습니다.



### **Batch Key Access Join** (일괄 키 접근 조인, O(Nlog(M)))
**Batched Key Access (BKA)**는 **인덱스**를 사용할 수 있는 테이블에 대해 성능을 최적화한 **JOIN** 방식입니다. 첫 번째 테이블의 데이터를 **JOIN 버퍼**에 저장한 후, 그 데이터를 기반으로 두 번째 테이블에 대한 **인덱스 키**를 일괄적으로 생성하고 조회하여 **디스크 I/O**를 줄이고 성능을 향상시킵니다.

#### Batched Key Access Join 작동 방식
- 첫 번째 테이블의 행을 **JOIN 버퍼**에 저장합니다.
- **JOIN 버퍼**에 저장된 행을 기반으로 두 번째 테이블의 인덱스 키를 생성합니다.
- 생성된 인덱스 키를 **Multi-Range Read (MRR)** 인터페이스를 통해 일괄적으로 조회합니다.
	- **MRR**은 인덱스 조회를 최적화하여 디스크 읽기 순서를 최적화함으로써 성능을 향상시킵니다.
- 두 번째 테이블에서 매칭된 행을 가져와 **JOIN 버퍼** 내의 데이터와 결합한 후 결과를 출력합니다.

```pseudo
FOR each block of rows in table1 {                     
-- 첫 번째 테이블의 데이터를 블록 단위로 읽어옴
    LOAD rows into join buffer;                        
    -- JOIN 버퍼에 데이터를 저장
    BUILD keys for table2 based on join buffer;        
    -- JOIN 버퍼의 데이터를 기반으로 두 번째 테이블의 인덱스 키 생성
    SUBMIT keys to MRR interface for table2;           
    -- MRR 인터페이스를 통해 두 번째 테이블의 인덱스 키를 일괄적으로 제출
    FOR each row in table2 returned by MRR {           
    -- MRR로부터 반환된 두 번째 테이블의 행을 반복
        MATCH with corresponding row in join buffer;   
        -- JOIN 버퍼 내의 데이터와 매칭
        OUTPUT joined_row;                             
        -- 매칭된 결과를 출력
    }
}

```
#### 사용 시 주의 사항
- **인덱스 필수**:
    - **BKA Join**은 두 번째 테이블에 인덱스가 있을 때만 사용 가능합니다. 인덱스가 없으면 BKA를 사용할 수 없으며, 다른 **JOIN** 알고리즘이 대신 사용됩니다.
    - 따라서 두 번째 테이블의 **JOIN 조건**에 인덱스가 있는지 반드시 확인해야 합니다.
- **메모리 사용량**:
    - **JOIN 버퍼** 크기에 따라 메모리 사용량이 달라집니다. 큰 데이터 세트를 처리할 때는 충분한 메모리가 필요합니다. **join_buffer_size**가 작으면 성능이 저하될 수 있습니다.
    - 메모리가 부족하면 성능이 저하되므로 시스템 메모리 상태에 맞춰 적절한 버퍼 크기를 설정해야 합니다.
- **MRR 설정**:
    - **BKA Join**은 **Multi-Range Read (MRR)** 인터페이스를 사용하므로, **mrr** 플래그가 활성화되어야 합니다. **mrr_cost_based**를 **off**로 설정해야 **BKA Join**이 올바르게 작동할 수 있습니다.

#### 최적화 방안
- **join_buffer_size 조정**:
    - **JOIN 버퍼** 크기를 적절히 설정하면 성능을 크게 향상시킬 수 있습니다. 더 많은 데이터를 한 번에 메모리에서 처리할 수 있도록 **join_buffer_size**를 충분히 크게 설정해야 합니다. 예를 들어, 메모리가 충분하다면 버퍼 크기를 늘려 디스크 I/O를 줄일 수 있습니다.
- **mrr 및 mrr_cost_based 설정**:
    - **BKA Join**은 **MRR** 인터페이스와 함께 동작하므로, **mrr** 플래그를 **on**으로 설정하고 **mrr_cost_based**를 **off**로 설정하여 비용 추정 문제를 해결할 수 있습니다.
- **인덱스 활용**:
    - **BKA Join**은 인덱스가 필수적이므로, **JOIN 조건**에 인덱스를 적절히 추가하는 것이 중요합니다. 인덱스가 존재하면 **BKA**의 성능을 극대화할 수 있습니다.
- **필터 조건 활용**:
    - **WHERE** 조건이나 **JOIN** 조건을 사용해 불필요한 데이터를 필터링하면, 처리할 데이터 양이 줄어들어 **BKA Join**의 성능이 향상됩니다.
### **Hash Join** (해시 조인, O(N+M))

**Hash Join**은 두 개의 입력 테이블 간에 매칭되는 행을 찾기 위해 **Hash Table**을 사용하는 **JOIN** 알고리즘입니다. 이는 특히 한 입력 테이블이 메모리에 충분히 들어갈 수 있을 때 **Nested Loop Join**보다 효율적입니다. MySQL 8.0.18부터 지원되며, 성능 면에서 기존의 **Nested Loop Join**보다 더 효율적인 경우가 많습니다.

#### Hash Join의 작동 방식

```sql
SELECT
  given_name, country_name
FROM
  persons JOIN countries ON persons.country_id = countries.country_id;
```

Hash Join은 두 가지 주요 단계로 나뉩니다: **Build Phase**와 **Probe Phase**입니다.

**Build Phase (구축 단계)**
- **Build Phase**에서는 한 입력 테이블을 기반으로 **Hash Table**을 만듭니다. 이 입력 테이블을 **Build Input**이라고 부르며, 일반적으로 크기가 더 작은 테이블이 선택됩니다.
- 예를 들어, `countries` 테이블이 **Build Input**으로 선택되었다고 가정해 봅시다. `countries.country_id`가 JOIN 조건으로 사용되며, 이 값이 Hash Table의 키로 저장됩니다. 테이블의 모든 행이 Hash Table에 저장되면 **Build Phase**가 완료됩니다.

**Probe Phase (탐색 단계)**
- **Probe Phase**에서는 다른 입력 테이블, 즉 **Probe Input**에서 데이터를 읽고, 각 행의 값을 사용하여 **Hash Table**에서 일치하는 행을 찾습니다. 예시에서는 `persons` 테이블이 **Probe Input**입니다.
- 각 매칭된 행은 클라이언트에게 반환됩니다. 이때 서버는 모든 입력 테이블을 한 번만 스캔하고, 상수 시간 동안 일치하는 행을 찾아냅니다.


#### 메모리 부족 시 Disk로 Spill 
- 만약 **Build Input**이 메모리에 모두 적재되지 않을 만큼 크다면, 서버는 나머지 데이터를 디스크에 **chunk files**로 기록합니다. MySQL은 가능한 한 큰 chunk 파일이 메모리에 딱 맞도록 설정합니다. **Probe Phase**에서는 디스크에서 chunk 파일을 읽어들이면서 같은 방식으로 Hash Table을 탐색하고, 두 테이블 간의 매칭을 찾습니다.

#### Hash Join 사용 방법
- **Hash Join**은 기본적으로 활성화되어 있으며, 별도의 설정 없이 사용할 수 있습니다. **EXPLAIN FORMAT=tree** 명령을 사용하여 쿼리에서 Hash Join이 사용되는지 확인할 수 있습니다.
- Hash Join은 **equi-join** 조건(같은 값으로 결합하는 조건)을 사용하는 테이블들을 JOIN할 때 주로 사용됩니다. 그러나 만약 JOIN 조건에 대한 인덱스가 있는 경우, MySQL은 인덱스를 사용하는 **Nested Loop Join**을 선호할 수 있습니다.

#### 성능
- Hash Join은 각 입력 테이블을 한 번만 스캔하며, 상수 시간 동안 일치하는 행을 찾기 때문에 **block-nested loop**보다 훨씬 빠르게 작동할 수 있습니다. 특히 인덱스가 없고, 모든 데이터가 메모리에 있을 때 성능 차이가 극명하게 드러납니다.
- **성능 비교 결과**
	- [**Percona Live Europe 2019**](https://www.slideshare.net/slideshow/mysql-8018-latest-updates-hash-join-and-explain-analyze/178299554)에서 진행된 벤치마크 결과에 따르면, **Hash Join**은 **block-nested loop**를 사용하는 쿼리보다 현저히 빠른 성능을 보여주었습니다. 특히, **Hash Join**이 각 입력을 한 번만 스캔하고 매칭을 찾는 방식 덕분에 성능이 크게 향상되었습니다.
#### 제약 사항
- 현재 MySQL의 **Hash Join**은 **Inner Join**만 지원하며, **Anti Join**, **Semi Join**, **Outer Join**은 여전히 **block-nested loop** 알고리즘을 사용합니다. 또한, MySQL의 옵티마이저는 아직 **Hash Join**을 충분히 자주 사용하지 못할 수 있으며, 이는 추후 개선될 예정입니다.

### **Merge Join** (병합 조인, 이미 정렬된 경우 O(N+M))
**Merge Join**은 두 테이블을 각각 **정렬된 상태**로 만들어, 순차적으로 비교하면서 매칭되는 행을 결합하는 **JOIN** 방식입니다. 이 방식은 주로 **정렬된 데이터**에서 효과적으로 동작하며, **인덱스**가 있는 테이블 간의 조인에서 매우 빠른 성능을 발휘합니다.

#### Merge Join 작동 방식
- 두 테이블의 **JOIN 조건**에 따라, 조인할 열이 미리 **정렬**되어 있어야 합니다.
- 각 테이블에서 첫 번째 행을 읽고, 두 행을 비교하여 매칭되는지 확인합니다.
- 매칭되지 않는 경우, 작은 값의 행을 다음 행으로 이동시킵니다.
- 매칭되는 행을 찾으면 해당 결과를 출력한 후, 두 테이블에서 모두 다음 행으로 이동합니다.

```pseudo
READ first row from table1;
READ first row from table2;
WHILE NOT end_of_table1 AND NOT end_of_table2 {
    IF table1.row matches table2.row {     -- 두 테이블의 행이 조인 조건을 만족하는지 확인
        OUTPUT joined_row;                 -- 매칭된 행이 있으면 결과 출력
        READ next row from table1;         -- 두 테이블에서 다음 행을 읽음
        READ next row from table2;
    } ELSE IF table1.row < table2.row {     -- table1의 값이 더 작으면
        READ next row from table1;         -- table1의 다음 행을 읽음
    } ELSE {                               -- table2의 값이 더 작으면
        READ next row from table2;         -- table2의 다음 행을 읽음
    }
}
```

#### 사용 시 주의 사항
- **정렬된 데이터 필요**:
    - **Merge Join**은 두 테이블의 조인할 열이 **정렬**되어 있어야 효율적으로 동작합니다. 테이블이 정렬되지 않은 경우, **ORDER BY**나 **인덱스**를 사용해 데이터가 정렬된 상태로 있어야 합니다.
    - 정렬된 데이터가 없는 경우 **Merge Join**은 비효율적일 수 있으며, **Nested Loop Join**이나 **Hash Join**과 같은 다른 방식이 더 적합할 수 있습니다.
- **대규모 데이터 세트**:
    - **Merge Join**은 데이터가 정렬되어 있고, 메모리 내에서 처리 가능한 크기의 데이터일 때 매우 효율적입니다. 하지만 대규모 데이터 세트를 처리할 때는 성능이 저하될 수 있으므로, 메모리 관리와 인덱스 활용이 중요합니다.




# References
- https://inpa.tistory.com/entry/MYSQL-%F0%9F%93%9A-JOIN-%EC%A1%B0%EC%9D%B8-%EA%B7%B8%EB%A6%BC%EC%9C%BC%EB%A1%9C-%EC%95%8C%EA%B8%B0%EC%89%BD%EA%B2%8C-%EC%A0%95%EB%A6%AC
- https://dev.mysql.com/doc/refman/9.0/en/join.html
- https://dev.mysql.com/doc/refman/8.4/en/nested-loop-joins.html
- https://blog.naver.com/parkjy76/221069454499
- https://dev.mysql.com/blog-archive/hash-join-in-mysql-8/
- https://dev.mysql.com/doc/refman/8.4/en/nested-join-optimization.html
- https://dev.mysql.com/doc/refman/8.4/en/bnl-bka-optimization.html