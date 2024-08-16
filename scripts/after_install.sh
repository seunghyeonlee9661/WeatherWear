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

# 환경 변수 설정
echo "DB_URL=${DB_URL}" | sudo tee -a /etc/environment
echo "DB_USERNAME=${DB_USERNAME}" | sudo tee -a /etc/environment
echo "DB_PASSWORD=${DB_PASSWORD}" | sudo tee -a /etc/environment
echo "JWT_SECRET_KEY=${JWT_SECRET_KEY}" | sudo tee -a /etc/environment
echo "NAVER_CLIENT_ID=${NAVER_CLIENT_ID}" | sudo tee -a /etc/environment
echo "NAVER_CLIENT_SECRET=${NAVER_CLIENT_SECRET}" | sudo tee -a /etc/environment
echo "KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID}" | sudo tee -a /etc/environment
echo "KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI}" | sudo tee -a /etc/environment
echo "WEATHER_API_KEY=${WEATHER_API_KEY}" | sudo tee -a /etc/environment
echo "REDIS_HOST=${REDIS_HOST}" | sudo tee -a /etc/environment
echo "REDIS_PORT=${REDIS_PORT}" | sudo tee -a /etc/environment
echo "MAIL_USERNAME=${MAIL_USERNAME}" | sudo tee -a /etc/environment
echo "MAIL_PASSWORD=${MAIL_PASSWORD}" | sudo tee -a /etc/environment

# 환경 변수 로그 기록
{
  echo "Environment Variables:"
  echo "DB_URL=${DB_URL}"
  echo "DB_USERNAME=${DB_USERNAME}"
  echo "DB_PASSWORD=${DB_PASSWORD}"
  echo "JWT_SECRET_KEY=${JWT_SECRET_KEY}"
  echo "NAVER_CLIENT_ID=${NAVER_CLIENT_ID}"
  echo "NAVER_CLIENT_SECRET=${NAVER_CLIENT_SECRET}"
  echo "KAKAO_CLIENT_ID=${KAKAO_CLIENT_ID}"
  echo "KAKAO_REDIRECT_URI=${KAKAO_REDIRECT_URI}"
  echo "WEATHER_API_KEY=${WEATHER_API_KEY}"
  echo "REDIS_HOST=${REDIS_HOST}"
  echo "REDIS_PORT=${REDIS_PORT}"
  echo "MAIL_USERNAME=${MAIL_USERNAME}"
  echo "MAIL_PASSWORD=${MAIL_PASSWORD}"
} >> $LOGFILE

# 로그 기록
echo "$(date): Environment variables set." >> $LOGFILE