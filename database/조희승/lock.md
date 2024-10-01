# 락
- 정의
    - 데이터베이스에서 동시성 제어를 위한 방법론
- 종류
    - 공유락 Shared Lock(S-lock), 읽기락 Read Lock 
        - 락을 소유한 데이터를 다른 트랜잭션이 읽기는 가능하나 쓰기는 불가능 하다
    - 배타 락 Exclusive Lock(X-lock), 쓰기 락 Write Lock
        - 락을 소유한 데이터를 다른 트랜잭션이 읽거나 쓸 수 없다

# Dead Lock
- 두 개이상의 트랜잭션이 각자 락을 잡고 있으며 서로 상대방의 락잡힌 데이터의 락을 획득하고 싶어한다
- 데드락이 발생하기 위한 네가지 충분 조건
    - 상호 배제
        - 하나의 프로세스가 자원을 사용하면 다른 프로세스는 접근 불가
    - 점유와 대기
        - 하나의 프로세스가 자원을 사용한 상태에서 다른 자원 사용을 요청
    - 비선점
        - 프로세스가 사용중인 자원을 쓰지 못하게 막을 수 없다
    - 순환 대기
        - 프로세스가 각자 사용중인 자원을 서로가 사용하려고 요청한 상황
- 해결책
    - 개발자가 데드락 상황이 생기지 않도록 쿼리를 설계해야 한다
    - 데이터베이스 자체 해결책
        - 타임 아웃
            - 트랜잭션 시작후 일정 시간 지나면 강제 종료

-----------------------------------------------------------------------


# MySQL 엔진 락
- 모든 스토리지 엔진에 영향을 준다
## 글로벌 락
- 명령어
    - flush tables with read lock
- 세션에서 글로벌 락 획득시, 다른 세션에서 DDL, DML 명령어 실행시 대기
    -  MySQL 엔진 수준 락

## 백업 락
- 명령어
    - lock instance for backup
    - unlock instance
- MySQL 8.0에 도입, 글로벌 락보다 경량화해 백업 작업용으로 설계
    - DDL 작업을 대기 시킨다

## 테이블 락
- 명령어
    - lock tables [table_name] read
    - lock tables [table_name] write
    - unlock tables [table_name] 
- 테이블에 DDL 연산을 잠근다

## 네임드 락
- 명령어
    - select get_lock([string], [time])
        - string에 락이 걸릴 시 접근자는 time 초 동안 대기하는 락을 건다
    - select release_lock([string])
    - select IS_FREE_LOCK([string])
- 목적
    - MySQL을 이용해 분산락을 구현하고 싶을 때 사용

# 메타데이터 락
- 테이블, 뷰 객체의 이름/구조 변경시 사용
- alter table 명령어 호출시 메타데이터 락을 획득하고 테이블 락을 획득합니다

# InnoDB 스토리지 엔진 락
## next key lock
- record lock과 gap lock의 조합
- repeatable read에서 일관된 읽기를 보장
- 쿼리에 해당하는 record가 10, 20, 30인 경우 next key lock은 
    - 10, 20, 30 레코드에 대한 수정을 막곤
    - (-infite, 10], (10, 20], (20, 30], (30, +infinite) 영역에 데이터 추가를 막습니다


## record lock
- innodb는 쿼리에 해당하는 record만을 잠그지 않고 record가 포함된 인덱스 데이터 페이지를 잠근다
- 만약에 update 문장에서 조건이 인덱스가 없다면 기본키에 대한 클러스터 인데스의 데이터 페이지를 잠급니다
- 목적
    - 수정 방지

## gap lock
- 쿼리에 해당하는 레코드 뿐 아니라 레코드 사이의 간격 / 첫번째 레코드의 이전 / 마지막 레코드의 이후도 같이 거는 락
- 물리적으로 잠그는게 아니라 논리적으로 막는다
    - LockManager가 gap lock이 잠그고 있는 영역을 관리, 쿼리 처리 전 gap lock이 잡힌 공간인지 확인해 대기를 건다
- 목적
    - 데이터 추가 방지
    - Phantom Read 방지
- 다른 트랜잭션의 갭 락이 잡힌 영역에 대해 읽기를 허락하고 쓰기를 막습니다


## auto increment lock
- 명령어
    - 별도의 명령어 없이 auto increment 설정을 한 컬럼이 있는 테이블에 insert 쿼리 실행시 동작
- 테이블 수준의 락
- MySQL 5.1 이상일 경우 관련 설정
    - innodb_autoinc_lock_mode=1
        - auto increment lock을 사용하지 않고 뮤텍스를 이용 
    - innodb_autoinc_lock_mode=2
        - MySQL 8.0 부터 기본값

## 개발자가 의도해서 락을 걸때 사용 방법
- 공유 락
    - select ... for share
- 배타 락
    - select ... for update

## Lock Option
- select for share, select for update에 옵션으로 사용
- nowait
    - 트랜잭션이 행 잠금을 획득할 수 없을 때 얻을 수 있을 때까지 기다리지 않고 실패로 처리합니다 
- Skip Locked
    - 트랜잭션이 행 잠금을 획득할 수 없을 때 해당 행을 조회하는 걸 건너 뛰어 결과물에서 제외됩니다
- 예시

```
# session 1 - trasaction 
start transaction
select * from t where i = 2 for update;


# session 2 - transaction
start transaction
select * from t where i = 2 for update nowait; // Error return

# sessopm 3 - transactions
start transaction
select * from t for update skip locked; // i=2를 제외한 결과 return
```