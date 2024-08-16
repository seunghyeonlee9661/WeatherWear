#!/bin/bash

# 로그 파일 및 디렉토리 정의
LOGDIR="/home/ubuntu/spring/WeatherWear/logs"
LOGFILE="$LOGDIR/startup.log"

# 로그 디렉토리 생성 (존재하지 않는 경우)
mkdir -p $LOGDIR
chown ubuntu:ubuntu $LOGDIR

# 현재 시간 출력
echo "Starting application at $(date)" >> $LOGFILE

# 환경 변수 로드
if [ -f /home/ubuntu/spring/WeatherWear/env-vars.env ]; then
    # 환경 변수 파일의 내용을 로그에 출력
    echo "Loading environment variables from /home/ubuntu/spring/WeatherWear/env-vars.env" >> $LOGFILE
    while IFS= read -r line; do
        echo "Loading: $line" >> $LOGFILE
    done < /home/ubuntu/spring/WeatherWear/env-vars.env

    # 환경 변수 파일의 내용 로드
    source /home/ubuntu/spring/WeatherWear/env-vars.env

    # 환경 변수 적용 확인
    echo "Environment variables loaded from env-vars.env" >> $LOGFILE
else
    echo "Environment variables file not found!" >> $LOGFILE
    exit 1
fi


# 애플리케이션 디렉토리로 이동
echo "Changing directory to /home/ubuntu/spring/WeatherWear" >> $LOGFILE
cd /home/ubuntu/spring/WeatherWear >> $LOGFILE 2>&1

# 애플리케이션 실행
echo "Starting application with gradlew bootrun" >> $LOGFILE
nohup ./gradlew bootrun >> /home/ubuntu/spring/WeatherWear/logs/application.log 2>&1 &
echo "Application started at $(date)" >> $LOGFILE
