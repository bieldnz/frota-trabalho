# Dockerização da aplicação Frota

Este guia mostra como construir e executar a aplicação Spring Boot com Docker e Docker Compose.

## Pré-requisitos
- Docker Desktop (Windows) ou Docker Engine (WSL/Linux)
- Opcional: Maven/Java localmente (não é necessário para rodar via Docker)

## Rodar com Docker Compose (recomendado)

Isto sobe MySQL e a aplicação juntos.

Windows PowerShell (na raiz do projeto):

```
# Construir e subir os serviços
docker compose up --build

# Parar (mantendo volumes/dados)
docker compose down

# Parar e remover volumes (apaga dados do MySQL)
docker compose down -v
```

WSL (Ubuntu):

```
docker compose up --build
```

Aplicação disponível em: http://localhost:8083/

## Construir apenas a imagem da aplicação

```
# Windows PowerShell
docker build -t frota-app:local .

# Executar a aplicação apontando para um MySQL já existente (ajuste as variáveis se necessário)
docker run --rm -p 8083:8083 ^
  -e SPRING_DATASOURCE_URL="jdbc:mysql://host.docker.internal:3306/frota?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true" ^
  -e SPRING_DATASOURCE_USERNAME=root ^
  -e SPRING_DATASOURCE_PASSWORD=cco123 ^
  frota-app:local
```

No WSL/Linux, troque `host.docker.internal` pelo IP/host adequado ou use o compose.

## Variáveis importantes
- SPRING_DATASOURCE_URL, SPRING_DATASOURCE_USERNAME, SPRING_DATASOURCE_PASSWORD
- JAVA_OPTS (opcional para tunning de memória/JVM)

## Problemas comuns
- Porta 3306/8083 em uso: altere as portas no `docker-compose.yml` ou desligue serviços conflitantes.
- Falha de conexão com MySQL: garanta que o serviço `db` está saudável (healthcheck) e que as credenciais conferem.
- Build lento: o `.dockerignore` já reduz o contexto; evite montar a pasta toda no container em produção.