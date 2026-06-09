#!/usr/bin/env bash
set -e
ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
COMPOSE_FILE="$ROOT_DIR/docker-compose.ec2.yml"
IMAGE="${DOCKERHUB_USERNAME:-youruser/fruit-shop-back}:latest"

# If Docker Hub repo is private, export DOCKERHUB_USERNAME and DOCKERHUB_PASSWORD before running this script.
if [ -n "${DOCKERHUB_USERNAME:-}" ] && [ -n "${DOCKERHUB_PASSWORD:-}" ]; then
  echo "$DOCKERHUB_PASSWORD" | docker login -u "$DOCKERHUB_USERNAME" --password-stdin
fi

# Pull latest image and restart service
docker-compose -f "$COMPOSE_FILE" pull
docker-compose -f "$COMPOSE_FILE" up -d --remove-orphans

echo "Deployed $IMAGE"
