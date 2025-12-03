#PostgreSQL 15버전 비트나미 이미지 사용
FROM bitnami/postgresql:latest

# 2. init.sql 복사 (소유권 변경)
COPY --chown=1001:1001 ./init/init.sql /docker-entrypoint-initdb.d/

# 3. 설정 파일 복사
#COPY --chown=1001:1001 ./config/postgresql.conf /opt/bitnami/postgresql/conf/postgresql.conf

# 4. 포트 노출
EXPOSE 5432

