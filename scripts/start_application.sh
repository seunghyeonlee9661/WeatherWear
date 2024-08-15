#!/bin/bash

# 로그 파일 정의
LOGFILE="/home/ubuntu/spring/WeatherWear/logs/startup.log"

# 현재 시간 출력
echo "Starting application at $(date)" >> $LOGFILE

# 애플리케이션 디렉토리로 이동
echo "Changing directory to /home/ubuntu/spring/WeatherWear" >> $LOGFILE
cd /home/ubuntu/spring/WeatherWear >> $LOGFILE 2>&1

# 애플리케이션 실행
echo "Starting application with gradlew bootrun" >> $LOGFILE
nohup ./gradlew bootrun >> /home/ubuntu/spring/WeatherWear/logs/application.log 2>&1 &
echo "Application started at $(date)" >> $LOGFILE
