# 1. 테스트용 네트워크 생성
docker network create postgres 2>/dev/null || true

# 2. 프라이머리 노드 실행 (일반 모드)
# 이미지를 bitnami/postgresql:15로 변경하고, 불필요한 REPMGR 변수 제거
docker run -d \
  --name postgres-primary-0 \
  --network postgres \
  -v postgres_primary_data:/bitnami/postgresql \
  -e POSTGRESQL_POSTGRES_PASSWORD=${DATASOURCE_ADMIN_PASSWORD} \
  -e POSTGRESQL_USERNAME=${DATASOURCE_USERNAME} \
  -e POSTGRESQL_PASSWORD=${DATASOURCE_PASSWORD} \
  -e POSTGRESQL_DATABASE=${DATASOURCE_DB_NAME} \
  bitnami/postgresql:15

# 3. 스탠바이 노드 (일반 이미지에서는 복제 설정 없이 단순 DB 2개로 뜹니다)
# 단순히 DB를 하나 더 띄우는 것이라면 아래 명령어도 동일하게 수정
docker run -d \
  --name postgres-standby-1 \
  --network postgres \
  -v postgres_standby_data:/bitnami/postgresql \
  -e POSTGRESQL_POSTGRES_PASSWORD=${DATASOURCE_ADMIN_PASSWORD} \
  -e POSTGRESQL_USERNAME=${DATASOURCE_USERNAME} \
  -e POSTGRESQL_PASSWORD=${DATASOURCE_PASSWORD} \
  -e POSTGRESQL_DATABASE=${DATASOURCE_DB_NAME} \
  bitnami/postgresql:15

# 4. 로그 확인
docker logs -f postgres-primary-0
