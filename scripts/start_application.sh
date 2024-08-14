#!/bin/bash
# 애플리케이션 시작 스크립트

cd /home/ubuntu/spring/WeatherWear
nohup ./gradlew bootrun > /home/ubuntu/spring/WeatherWear/logs/application.log 2>&1 &
