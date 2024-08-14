#!/bin/bash
# 애플리케이션 시작 스크립트

cd /home/ubuntu/deployments/Weatherwear
nohup ./gradlew bootrun > /home/ubuntu/deployments/Weatherwear/logs/application.log 2>&1 &
