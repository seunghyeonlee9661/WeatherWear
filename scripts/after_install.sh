#!/bin/bash

# 애플리케이션 설치 후 추가 작업

# 로그 디렉토리 및 파일 정의
LOGDIR="/home/ubuntu/spring/WeatherWear/logs"
LOGFILE="$LOGDIR/startup.log"
APP_DIR="/home/ubuntu/spring/WeatherWear"
ENV_VARS_FILE="$APP_DIR/env-vars.env"

# 로그 디렉토리 생성 (존재하지 않는 경우)
mkdir -p $LOGDIR
chown ubuntu:ubuntu $LOGDIR

# 로그 파일 생성 및 권한 수정
touch $LOGFILE
chown ubuntu:ubuntu $LOGFILE

# 애플리케이션 디렉토리 및 파일 권한 수정
chown -R ubuntu:ubuntu $APP_DIR

# S3에서 환경 변수 파일 다운로드
aws s3 cp s3://${AWS_S3_BUCKET_NAME}/env-vars.env $ENV_VARS_FILE
# 환경 변수 파일의 권한 설정
chown ubuntu:ubuntu $ENV_VARS_FILE

# 로그 기록
echo "$(date): Environment variables set." >> $LOGFILE