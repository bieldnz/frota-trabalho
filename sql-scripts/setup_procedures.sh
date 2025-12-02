#!/bin/bash

# Script para executar todas as procedures e triggers após a inicialização
echo "Executando procedures e triggers..."

# Executar procedure de delete
echo "1. Criando procedure apagar_caminhao..."
docker exec frota-db mysql -u frota -pfrota frota < /sql-scripts/create_procedure_delcaminhao.sql

# Executar logs e triggers
echo "2. Criando tabela de logs e triggers..."
docker exec frota-db mysql -u frota -pfrota frota < /sql-scripts/create_logs_and_triggers.sql

echo "Procedures e triggers criados com sucesso!"