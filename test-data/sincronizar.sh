#!/bin/bash
# Uso: ./sincronizar.sh [TOKEN]
TOKEN=${1:-"TU_JWT_TOKEN_AQUI"}

curl -s -X POST http://localhost:8081/API/v1/compras/sincronizar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d @compras_100.json | python3 -m json.tool
