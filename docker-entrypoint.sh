#!/bin/sh

# Wait for MySQL to be ready
if [ -n "$DB_HOST" ]; then
  echo "Waiting for MySQL at $DB_HOST:$DB_PORT..."
  ./wait-for-it.sh -t 60 "$DB_HOST:$DB_PORT"
fi

# Run the application
exec java $JAVA_OPTS \
     -Djava.security.egd=file:/dev/./urandom \
     -Dspring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
     -Dspring.datasource.username=${DB_USER} \
     -Dspring.datasource.password=${DB_PASSWORD} \
     -Djwt.secret=${JWT_SECRET} \
     -jar app.jar