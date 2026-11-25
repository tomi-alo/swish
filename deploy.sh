#!/bin/bash
set -e
cd ~/swish
git pull
docker compose -f docker-compose.prod.yml pull
docker compose -f docker-compose.prod.yml up -d
docker image prune -f