docker compose build backend-test
docker compose build k6
docker compose --profile load-test up