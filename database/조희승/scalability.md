# Scalability
- 문제
    - 데이터 베이스가 다루는 데이터 양이 많아질 때, 처리 속도/저장 공간 부족
    - 트래픽이 많아 질 때, 처리 속도

# Partitioning
- 정의
    - 단일 데이터 베이스에서 테이블를 논리적으로 하나지만 물리적으로는 여러개의 파일로 분리해 관리
- 목적
    - 단일 데이터베이스 안에서 읽기/쓰기 성능 향상
    - 단기간 대량으로 쌓인 뒤 기간이 지나면 쓸모 없어지는 데이터를 관리하는데 적합
    - 데이터가 많아질 경우 인덱스가 커지면서 물리 공간 용량을 넘어 갈 수 있다
    - 테이블을 쪼개 작은 인덱스 여러개를 사용


## 구축
- 대부분의 데이터베이스가 지원

### 테이블 생성
```sql
create table t(

) partition by range (YEAR(create_dt))(
    partition p2009 values less than (2010),
    partition p2010 values less than (2011),
    partition p9999 values less than MAXVALUE
);
```

### 파티션 생성 제약 사항
- 파티션 키가 테이블의 프라이머리 키에 포함해야 한다
- 파티션 키는 유니크 키여야 한다 
- 파티션 수의 최대 값은 8192개다
- 파티션 테이블에선 외래키를 쓸 수 없다
- 파티션 테이블은 전문 검색 컬럼을 사용할 수 없다
- 파티션 테이블은 공간 데이터 타입 컬럼을 사용할 수 없다

### 파티션 종류
- 레인지 파티션
    - partition by range(key), partition name values less then (value)
    - 사용 목적
        - 날짜기반으로 값의 비율이 균등
    - 마지막에 MAXVALUE를 정의하지 않고 범위에 없는 값을 insert하면 에러 응답
    - 파티션 추가시, MAXVALUE 부분을 reorganize를 이용해 새로 파티션닝 해야한다
    - reorganize를 이용해 파티션을 병합할 수도 분할할 수도 있따
- 리스트 파이션
    - partition by list(key), partition name values in (value)
    - 사용 목적
        - 파티션 키값이 가질 수 있는 값의 가지수가 적게 정해져 있을 때
        - 파티션 키 기준으로 값의 비율이 균등할 때 
- 해시 파티션
    - partition by hash(function), partitions number
    - 사용 목적
        - 어느 특정 컬럼 값에 의해 분배되기 보다는 id 기반으로 나누어야 할 때
        - 컬럼들의 값들을 기반으로 정수를 반환하는 function을 정의
- 키 파티션
    - partition by key(key), paritions number
    - 사용 목적
        - 목적은 해시 파티션과 유사, function 정의 없이 MySQL 내장 해싱 알고리즘 사용


### 테이블 변경
```sql
// 테이블에 락을 건다
alter table t
partition by range (YEAR(create_dt))(
    partition p2009 values less than (2010),
    partition p2010 values less than (2011),
    partition p9999 values less than MAXVALUE
);
```

### insert
    - 개발자는 보통의 insert 쿼리를 사용
    - 데이터베이스는 파티션 키를 이용해 어느 파티션에 저장해야하는지 판단
    - 파티션이 결정되면 보통의 insert 처리와 같다

### update
    - where 절에 파티션 키값이 빠지면 모든 파티션을 찾아야 한다
    - 파티션 키가 아닌 컬럼값 변경할 경우, 보통 update 처리와 같다
    - 파티션 키의 컬럼 값을 변경할 경우
        - 기존 레코드가 저장된 파티션에서 레코드를 삭제 후 변경 값이 갈 파티션에 새로 쓴다

### select
- 전체를 아우르는 인덱스는 없고 파티션별로 인덱스가 존재한다
- 쿼리 성능에 영향을 주는 요인
    - where 절에 파티션 키가 포함되는가
    - where 절 조건이 인덱스 레인지 스캔 가능한가

||where절에 파티션 키 포함|where절에 파티션 키 없음|
|---|---|---|
|인덱스 레인지 스캔 가능|필요한 파티션에서만 인덱스 레인지 스캔 동작|모든 파티션에서 인덱스 레인지 스캔 수행|
|인덱스 레인지 스캔 불가|필요한 파티션에서만 풀 테이블 스캔|모든 파티션에서 풀 스캔|

- 쿼리 작성시 인덱스 레인지 스캔을 할 수 없는 경우는 피할 것!
- 파티션 수가 많을 경우 where 절에 파티션 키 없는 경우 처리 속도 느려지는 것 염두!
- 파티션 프루닝 partition pruning
    - 쿼리 실행 계획을 새울 때 접근할 필요 없는 파티션은 처리에서 배제하는 행동
    - 실행 계회 조회시 paritions 컬럼에서 조회한 파티션들 이름을 볼 수 있다
- 각 파티션에서 조회 결과를 조합해 클라이언트에게 응답
    - 파티션 조회 결과를 우선순위 큐에 넣어 쿼리에서 지정한 정렬을 수행


# Sharding
- 정의
    - 하나의 테이블의 로우들을 여러 데이터베이스로 분할 
- 목적
    - RDB 역시 서버이기에 한대가 처리할 수 있는 처리량에 한계가 존재한다
    - 주로 쓰기 성능 높이기

## 구축 
- 데이터 베이스 설정만으로 못 하고 애플리케이션 로직 변경이 필요하다
- PK module
    - db 수 를 m 이라 하고 각 db에 0부터 번호 부여, row가 배정될 db 번호 = pk mod m 


## 주의 사항
- 특정 샤드에 데이터가 몰리는 상황이 생기면 성능 저하가 올 수 있다
- 여러 샤드를 접근해야하는 쿼리(크로스 샤드) 사용을 줄여야 합니다
- 스키마 변경시 모든 샤드에 적용해야 합니다
- 

# Replication
- 정의
    - 동일한 데이터의 복사본을 여러 데이터베이스에 저장
        - 한 서버에 적용된 데이터 변경 사항이 다른 서버에 동기화
- 목적
    - scale out, 주로 읽기 성능 높이기
    - backup
    - analytics
        - 분석/집계 용으로 데이터베이스 두기
    - data distance distribution
        - 애플리케이션 배포 서버와 지리적으로 가까운 곳에 복사본 데이터베이스 두기

## 구축
- 대부분의 데이터베이스가 지원
- 쓰기를 담당하는 하나의 데이터베이스(source)와 읽기를 담당하는 여러 데이터베이스(replica)

## 복제 방식
### binary log 기반 복제
- source가 처리한 쓰기 쿼리를 binary log 파일에 기록
- replica는 source의 binary log 파일을 주기적으로 자신의 relay log 파일에 복사
    - replica server는 source server와 tcp 통신을 한다
    - replica server는 binary log의 어디 까지 읽었는가 추적한다
- replica는 relay log에 내용을 자신의 데이터베이스에 적용

### GTID 기반 복제
- Global Transaction IDentifier
- MySQL 5.6 부터 도입
- 'source_id:transaction_id' 로 이루어진 식별자
    - 좀 더 효율적으로 어떤걸 복제해야 할 지 추적하기 쉽게 라벨을 단다
- source 서버에서 트랜잭션 시작시 gtid 할당, binary log에 트랜잭션 처리 사항 적을 당시 gtid와 함께 기록
- replica 서버는 자신이 처리한 gtid 목록을 추적, 아직 적용하지 않은 gtid를 source에게 요청해 relay log에 트랜잭션 처리 사항 복사

## 동기화 방식
### 비동기 방식 asynchronous
- source server와 replica server 사이 데이터 불일치가 발생할 수 있다

### 반동기 방식 semi synchronous
- source server가 트랜잭션 커밋 후, 최소 하나의 replica server가 binary log의 내용을 읽었다는 걸 확인한 뒤 클라이언트에게 응답한다


# AWS Aurora
- MySQL, PostgreSQL 아키텍처를 기반으로 AWS에서 새로 설계한 데이터베이스
- 확장과 복제 설정을 aws에게 맞김
- 컴퓨팅 계층과 스토리지 계층으로 나누어 source와 replica들이 공통의 스토리지를 공유
- 스토리지 계층은 6개의 노드로 구성(예시로 6개)
    - source instance가 쓰기 요청을 받으면 6개의 노드에 쓰기를 전달 4/6이 완료를 하면 클라이언트에게 응답
    - replica instance가 읽기 요청을 받으면 6개의 노드에 읽기를 전달 병렬로 결과물을 만들어 클라이언트에게 응답


# reference
- https://dev.mysql.com/doc/refman/8.4/en/replication.html
- real mysql 2권