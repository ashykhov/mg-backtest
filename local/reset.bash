# Остановка и удаление контейнеров
docker-compose down

# Удаление тома, содержащего данные базы данных
docker volume rm mg_backtest-database

# Пересоздание и запуск всей системы
docker-compose up -d