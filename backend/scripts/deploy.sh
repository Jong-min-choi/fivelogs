#!/bin/bash

# 필요한 파일에 실행 권한 부여
chmod +x scripts/zero_downtime_deploy.py

# Python 스크립트 실행
echo "Starting zero downtime deployment..."
python3 scripts/zero_downtime_deploy.py

# 배포 후 완료 메시지
echo "Deployment completed successfully!"