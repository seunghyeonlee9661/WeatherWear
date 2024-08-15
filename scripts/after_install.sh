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

# 필요에 따라 추가 설정 파일 등을 복사하는 명령어 추가
# 예: cp /source/path /home/ubuntu/spring/WeatherWear/destination/path
