#!/bin/bash
# 이전 애플리케이션 중지 및 디렉토리 정리 작업

# 애플리케이션이 실행 중인 경우 종료
pkill -f 'gradlew bootrun' || true
