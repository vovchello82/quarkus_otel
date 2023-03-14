PROFILE=all

docker:
	./mvnw package 
	docker build -f src/main/docker/Dockerfile.jvm -t vovchello/quarkus-otel .

deploy:
	docker-compose --project-directory ./src/deployment/local pull --ignore-pull-failures
	COMPOSE_PROFILES=${PROFILE} docker-compose --project-name quarkus-otelp --project-directory ./src/deployment/local up -d 
deploy-stop:
	docker-compose --project-name quarkus-otelp --project-directory ./src/deployment/local down	