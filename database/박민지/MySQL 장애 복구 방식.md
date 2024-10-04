
# Backup and Recovery Types (백업 및 복구 유형)
- **Logical Backup**: 
	- **CREATE DATABASE**, **CREATE TABLE**, **INSERT** 명령어 등의 **데이터베이스 구조** 및 **내용**을 논리적으로 백업하는 방식입니다.
	- 속도는 물리적 백업보다 느리며, 출력이 더 큽니다. 주로 **텍스트 형식**으로 저장되기 때문입니다.
	- 이 방식은 **서버가 실행 중**일 때 백업할 수 있으며, 논리적 백업은 **플랫폼 독립적**이고 이식성이 뛰어납니다.
	- 도구: **mysqldump**, **SELECT ... INTO OUTFILE**
- **Physical Backup**:
	- 데이터베이스의 디렉토리 및 파일의 **원본 복사본**을 저장하는 백업 방식입니다.
	- 주로 대용량 데이터베이스에 적합하며, 빠른 복구가 필요한 경우 사용됩니다.
	- 파일 복사만 진행되므로, **속도가 빠르고** 출력 파일이 **더 compact**(압축된 형태)입니다.
	- 백업은 서버가 종료된 상태에서도 수행 가능하며, 서버가 실행 중일 경우 적절한 잠금 처리가 필요합니다.

- **Full Backup**: 
	- 특정 시점에서 **모든 데이터**를 백업하는 방식입니다.
- **Incremental Backup**: 
	- 특정 기간 동안 변경된 데이터만 백업하는 방식입니다. 이 방식은 **binary log**를 통해 수행됩니다.

- **Full Recovery**:
    - 전체 백업으로부터 **모든 데이터를 복원**하는 방식입니다. 이 방식은 백업이 수행된 시점까지의 상태로 복구합니다.
- **Point-in-Time Recovery**:
    - 특정 시점까지의 데이터를 복구하는 방식입니다. **binary log**를 기반으로 하며, 전체 백업 이후의 변경 사항을 복원하여 특정 시점까지 데이터를 재현합니다.


# 장애 복구 과정
### Full Recovery 과정
1. **전체 백업본 준비**
    - 복구하려는 데이터베이스의 **Full Backup**을 미리 준비해야 합니다. 전체 백업은 데이터베이스가 정상 상태일 때 수행된 백업본입니다.
    - Full Backup은 **MySQL Enterprise Backup**, **mysqldump** 또는 **파일 복사** 등의 방법으로 생성될 수 있습니다.
2. **복구 대상 데이터베이스 초기화**
    - 복구하려는 MySQL 데이터베이스가 손상된 상태라면, 현재 데이터를 삭제하거나 다른 방식으로 데이터를 초기화해야 합니다.
3. **Full Backup 복원**
    - **mysqldump**를 사용하여 데이터를 복원할 수 있습니다.
    - **MySQL Enterprise Backup**을 사용해 백업된 물리적 파일을 복구하는 경우, 백업 파일을 복사해 데이터 디렉토리로 복원하고 서버를 재시작할 수 있습니다.


### Point-in-Time Recovery 과정
**binary log**가 활성화되어 있어야 Point-in-Time Recovery를 수행할 수 있습니다. 다음과 같이 설정되어 있어야 합니다:
```bash
log_bin = /var/log/mysql/mysql-bin.log
```
- SHOW BINARY LOGS를 사용하여 현재 사용 가능한 **binary log** 파일을 확인할 수 있습니다
- 8.0에서는 **binary log**가 기본적으로 **비활성화**되어있으므로, 명시적으로 사용 설정해야 합니다.

1. **복구할 로그 파일 선택**
	- MySQL에서 제공하는 **mysqlbinlog** 도구를 사용하여 binary log 파일을 읽고 해당 로그를 실행하여 복구합니다. 로그 파일의 범위는 전체 백업이 수행된 이후부터 데이터 손실이 발생한 시점까지 선택해야 합니다.
2. **특정 시점까지의 로그 적용**
	- **mysqlbinlog** 명령어에서 특정 시간 또는 트랜잭션 ID를 기준으로 로그를 제한할 수 있습니다. 예를 들어, 특정 시간까지 데이터를 복구하려면 다음과 같이 명령어를 사용할 수 있습니다:
```bash
mysqlbinlog --start-datetime="YYYY-MM-DD HH:MM:SS" --stop-datetime="YYYY-MM-DD HH:MM:SS" /var/log/mysql/mysql-bin.000001 | mysql -u [username] -p
```
3. **복구 후 데이터 무결성 확인**
	- 복구 후에는 데이터베이스의 무결성을 반드시 확인해야 합니다. 데이터가 손상되었거나 누락되지 않았는지 점검하기 위해, **CHECK TABLE** 또는 **REPAIR TABLE** 명령을 사용할 수 있습니다:


# 참고: InnoDB 자동화된 장애 복구
- InnoDB는 mysql 서버가 시작될 때 완료되지 못한 트랜잭션이나 디스크에 일부만 기록된 데이터 페이지 등에 대한 복구 작업이 자동으로 진행
	- 디스크, 하드웨어 이슈로 InnoDB가 자동 복구를 하지 못하는 경우, mysql 서버가 종료됨
	- 이 경우, `innodb_force_recovery`  설정하여 mysql 서버 재시작 필요
		- `innodb_force_recovery` 값(1~6)에 따라 데이터 파일/로그 파일 손상 여부 검사 과정을 선별적으로 진행
			- https://dev.mysql.com/doc/refman/8.4/en/forcing-innodb-recovery.html 참고

# References
- https://dev.mysql.com/doc/mysql-backup-excerpt/8.0/en/backup-types.html
- Real MySQL 8.0