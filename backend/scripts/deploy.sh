#!/bin/bash

SCRIPT_DIR=$(cd "$(dirname "$0")" && pwd)

chmod +x "$SCRIPT_DIR/zero_downtime_deploy.py"

echo "Starting zero downtime deployment..."
python3 "$SCRIPT_DIR/zero_downtime_deploy.py"

echo "Deployment completed successfully!"