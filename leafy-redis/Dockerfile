# 베이스 이미지 (가벼운 alpine 버전 권장)
FROM redis:7-alpine

# 설정 파일을 이미지 내부로 복사
# (주의: COPY [호스트 경로] [컨테이너 경로])
# redis.conf가 Dockerfile과 같은 위치에 있어야 함
COPY redis.conf /usr/local/etc/redis/redis.conf

# 컨테이너 실행 시 사용할 기본 명령어
# 이 설정 파일로 Redis 서버를 실행
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]
