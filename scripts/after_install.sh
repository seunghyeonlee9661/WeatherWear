#!/bin/bash
# 애플리케이션 설치 후 추가 작업

# 로그 디렉토리 및 파일 정의
LOGDIR="/home/ubuntu/spring/WeatherWear/logs"
LOGFILE="$LOGDIR/startup.log"
APP_DIR="/home/ubuntu/spring/WeatherWear"

# 로그 디렉토리 생성 (존재하지 않는 경우)
mkdir -p $LOGDIR
chown ubuntu:ubuntu $LOGDIR

# 로그 파일 생성 및 권한 수정
touch $LOGFILE
chown ubuntu:ubuntu $LOGFILE

# 애플리케이션 디렉토리 및 파일 권한 수정
chown -R ubuntu:ubuntu $APP_DIR

# 환경 변수 설정 (활성화)
echo "export DB_URL=${DB_URL}" | sudo tee -a /etc/profile
echo "export DB_USERNAME=${DB_USERNAME}" | sudo tee -a /etc/profile
echo "export DB_PASSWORD=${DB_PASSWORD}" | sudo tee -a /etc/profile
echo "export JWT_SECRET_KEY=${JWT_SECRET_KEY}" | sudo tee -a /etc/profile
echo "export NAVER_CLIENT_ID=${NAVER_CLIENT_ID}" | sudo tee -a /etc/profile
echo "export NAVER_CLIENT_SECRET=${NAVER_CLIENT_SECRET}" | sudo tee -a /etc/profile
echo "export KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID}" | sudo tee -a /etc/profile
echo "export KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI}" | sudo tee -a /etc/profile
echo "export WEATHER_API_KEY=${WEATHER_API_KEY}" | sudo tee -a /etc/profile
echo "export REDIS_HOST=${REDIS_HOST}" | sudo tee -a /etc/profile
echo "export REDIS_PORT=${REDIS_PORT}" | sudo tee -a /etc/profile
echo "export MAIL_USERNAME=${MAIL_USERNAME}" | sudo tee -a /etc/profile
echo "export MAIL_PASSWORD=${MAIL_PASSWORD}" | sudo tee -a /etc/profil

# 로그 기록
echo "$(date): Environment variables set." >> $LOGFILE