#!/bin/bash
# 애플리케이션 중지 스크립트

# 'gradlew bootrun' 명령을 통해 시작된 애플리케이션 종료
pkill -f 'gradlew bootrun' || true