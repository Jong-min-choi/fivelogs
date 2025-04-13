#!/bin/bash
cd /home/ec2-user/app
CURRENT_PORT=$(docker ps --format '{{.Ports}}' | grep -o '809[0-1]' | head -1)

if [ -z "$CURRENT_PORT" ] || [ "$CURRENT_PORT" == "8090" ]; then
    # Blue 중지 및 제거
    docker compose stop blue || true
    docker compose rm -f blue || true
    # Green 실행
    docker compose up -d green
    sleep 10
    sudo sed -i 's/8090/8091/' /etc/nginx/sites-available/default
    sudo systemctl reload nginx
else
    # Green 중지 및 제거
    docker compose stop green || true
    docker compose rm -f green || true
    # Blue 실행
    docker compose up -d blue
    sleep 10
    sudo sed -i 's/8091/8090/' /etc/nginx/sites-available/default
    sudo systemctl reload nginx
fi