# 📚 Documentação da API - Sistema de Frota

## 🔧 Informações Técnicas

- **Framework**: Spring Boot 3.5.5
- **Java**: 21
- **Banco de Dados**: MySQL 8.4
- **Porta**: 8083 (local), 8084 (debug)
- **Base URL**: `http://localhost:8083`

## 🏗️ Arquitetura

O sistema segue o padrão **MVC** com camadas bem definidas:

```
Controller → Service → Repository → Entity
```

### Principais Entidades:
- **Cliente** - Clientes do sistema
- **Transportadora** - Empresas de transporte com preços customizados
- **Transporte** - Solicitações de transporte com sistema de double check
- **Caixa** - Tipos de embalagens disponíveis
- **Caminhão**, **Marca**, **Motorista**, **Viagem** - Gestão da frota

---

## 👥 Cliente

### Modelo
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "(11) 99999-9999",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "documento": "123.456.789-00",
  "cep": "01234-567",
  "ativo": true,
  "dataCriacao": "2024-01-15T10:30:00",
  "dataAtualizacao": "2024-01-15T10:30:00"
}
```

### Endpoints

#### `POST /clientes` - Criar Cliente
```json
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "telefone": "(11) 99999-9999",
  "endereco": "Rua das Flores, 123",
  "cidade": "São Paulo",
  "documento": "123.456.789-00",
  "cep": "01234-567"
}
```

#### `GET /clientes` - Listar Clientes
- **Parâmetros**: `page`, `size`, `sort`
- **Resposta**: Lista paginada de clientes

#### `GET /clientes/{id}` - Buscar Por ID
- **Parâmetros**: `id` (Long)
- **Resposta**: Cliente encontrado ou 404

#### `PUT /clientes/{id}` - Atualizar Cliente
```json
{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@email.com",
  "telefone": "(11) 88888-8888",
  "endereco": "Rua das Palmeiras, 456"
}
```

#### `DELETE /clientes/{id}` - Desativar Cliente
- Realiza desativação lógica (não remove do banco)

#### `GET /clientes/buscar/email?email={email}` - Buscar Por Email

### Validações
- **Nome**: 2-100 caracteres
- **Email**: Formato válido e único
- **Telefone**: Formato `(xx) xxxxx-xxxx`
- **Documento**: Máximo 20 caracteres
- **Endereço**: Máximo 255 caracteres

---

## 🚚 Transportadora

### Modelo
```json
{
  "id": 1,
  "nome": "Express Transportes",
  "cnpj": "12.345.678/0001-90",
  "email": "contato@express.com",
  "telefone": "(11) 3333-3333",
  "endereco": "Av. Principal, 1000",
  "observacoes": "Transportadora especializada",
  "precoKm": 5.50,
  "valorPorCaixa": 12.00,
  "valorPorKilo": 1.20,
  "avaliacao": 4.5,
  "ativo": true,
  "dataCriacao": "2024-01-15T10:30:00",
  "dataAtualizacao": "2024-01-15T10:30:00"
}
```

### Endpoints

#### `POST /transportadoras` - Criar Transportadora
```json
{
  "nome": "Express Transportes",
  "cnpj": "12.345.678/0001-90",
  "email": "contato@express.com",
  "telefone": "(11) 3333-3333",
  "endereco": "Av. Principal, 1000",
  "observacoes": "Transportadora especializada",
  "precoKm": 5.50,
  "valorPorCaixa": 12.00,
  "valorPorKilo": 1.20
}
```

#### `GET /transportadoras` - Listar Transportadoras
- **Parâmetros**: `ativo` (boolean), `page`, `size`

#### `GET /transportadoras/{id}` - Buscar Por ID

#### `PUT /transportadoras/{id}` - Atualizar Transportadora

#### `DELETE /transportadoras/{id}` - Desativar Transportadora

#### `PUT /transportadoras/{id}/avaliacao` - Atualizar Avaliação
```json
{
  "avaliacao": 4.5
}
```

#### `GET /transportadoras/buscar/nome?nome={nome}` - Buscar Por Nome

#### `GET /transportadoras/buscar/cnpj?cnpj={cnpj}` - Buscar Por CNPJ

### Validações
- **Nome**: 2-100 caracteres
- **CNPJ**: Formato `XX.XXX.XXX/XXXX-XX` e único
- **Email**: Formato válido e único
- **Preços**: Entre 0.0 e 1000.0
- **Avaliação**: Entre 0.0 e 5.0

---

## 📦 Transporte (Sistema de Double Check)

### Modelo Completo
```json
{
  "id": 1,
  "produto": "Eletrônicos",
  "caixaId": 1,
  "clienteId": 1,
  "nomeCliente": "João Silva",
  "transportadoraId": 1,
  "nomeTransportadora": "Express Transportes",
  "comprimento": 50.0,
  "largura": 30.0,
  "altura": 20.0,
  "peso": 10.5,
  "quantidade": 2,
  "origem": "São Paulo, SP",
  "destino": "Rio de Janeiro, RJ",
  "valorFrete": 145.50,
  "statusGeral": "EM_PROCESSAMENTO",
  "statusMotorista": "A_CAMINHO_DA_ENTREGA",
  "statusCliente": "SOLICITADO",
  "horarioRetirada": "2024-12-01T10:00:00",
  "statusPagamento": "PAGO"
}
```

### Status Disponíveis
```
SOLICITADO → COLETA → EM_PROCESSAMENTO → A_CAMINHO_DA_ENTREGA → ENTREGUE → FINALIZADO
```

### Endpoints

#### `POST /transporte` - Criar Transporte
```json
{
  "produto": "Eletrônicos",
  "comprimento": 50.0,
  "largura": 30.0,
  "altura": 20.0,
  "caixaId": 1,
  "clienteId": 1,
  "transportadoraId": 1,
  "peso": 10.5,
  "quantidade": 2,
  "origem": "São Paulo, SP",
  "destino": "Rio de Janeiro, RJ",
  "horarioRetirada": "2024-12-01T10:00:00",
  "statusPagamento": "PAGO"
}
```

#### `GET /transporte` - Listar Transportes

#### `GET /transporte/{id}` - Buscar Por ID

#### `PUT /transporte/{id}` - Atualizar Transporte

#### `DELETE /transporte/{id}` - Deletar Transporte

#### `GET /transporte/caixa/{caixaId}` - Buscar Por Caixa

### 🔄 Sistema de Double Check

#### `PUT /transporte/{id}/status/motorista?status={STATUS}` - Atualizar Status Motorista
- Apenas o motorista pode atualizar este status
- Usado para controle da transportadora

#### `PUT /transporte/{id}/status/cliente?status={STATUS}` - Atualizar Status Cliente  
- Apenas o cliente pode atualizar este status
- Usado para confirmação da entrega

### 🎯 Regra de Negócio - Finalização Automática
Quando **ambos** os status (motorista E cliente) são `ENTREGUE`, o sistema automaticamente altera o `statusGeral` para `FINALIZADO`.

### 💰 Cálculo Inteligente de Frete

#### `GET /transporte/disponiveis` - Buscar Transportadoras com Frete
**Parâmetros:**
- `peso` (double)
- `comprimento` (double) 
- `largura` (double)
- `altura` (double)
- `quantidade` (int)
- `origem` (String)
- `destino` (String)

**Exemplo:**
```
GET /transporte/disponiveis?peso=10.5&comprimento=50&largura=30&altura=20&quantidade=2&origem=São Paulo, SP&destino=Rio de Janeiro, RJ
```

**Resposta:**
```json
[
  {
    "id": 1,
    "nome": "Express Transportes",
    "cnpj": "12.345.678/0001-90",
    "email": "contato@express.com",
    "telefone": "(11) 3333-3333",
    "avaliacao": 4.5,
    "precoKm": 5.50,
    "valorPorCaixa": 12.00,
    "valorPorKilo": 1.20,
    "valorFrete": 145.50
  }
]
```

### Algoritmo de Cálculo
1. **Peso Considerado**: `Math.max(pesoReal, pesoCubado)`
2. **Peso Cubado**: `(comprimento × largura × altura) × 300`
3. **Frete por Peso**: `(peso × valorPorKg) + (km × valorPorKm) + pedágio`
4. **Frete por Caixa**: `(quantidade × valorPorCaixa) + (km × valorPorKm) + pedágio`
5. **Frete Final**: `Math.max(fretePeso, freteCaixa)`
6. **Ordenação**: Do menor para o maior valor

---

## 📋 Outros Recursos

### Caixa
- **GET** `/caixa` - Listar caixas disponíveis
- **POST** `/caixa` - Criar nova caixa
- **GET** `/caixa/{id}` - Buscar caixa por ID

### Caminhão  
- **GET** `/caminhao` - Listar caminhões
- **POST** `/caminhao` - Cadastrar caminhão
- **PUT** `/caminhao/{id}` - Atualizar caminhão

### Marca
- **GET** `/marca` - Listar marcas
- **POST** `/marca` - Cadastrar marca
- **DELETE** `/marca/{id}` - Deletar marca

### Motorista
- **GET** `/motorista` - Listar motoristas  
- **POST** `/motorista` - Cadastrar motorista
- **PUT** `/motorista/{id}/localizacao` - Atualizar localização

### Viagem
- **GET** `/viagem` - Listar viagens
- **POST** `/viagem` - Criar viagem
- **PUT** `/viagem/{id}/iniciar` - Iniciar viagem

### Avaliação
- **GET** `/avaliacao` - Listar avaliações
- **POST** `/avaliacao` - Registrar avaliação
- **GET** `/avaliacao/{id}` - Buscar avaliação

---

## 🚀 Como Usar

### 1. Configurar Ambiente
```bash
# Clonar repositório
git clone <repository-url>

# Configurar MySQL
# Criar database 'frota'
# Configurar application.properties

# Executar aplicação
./mvnw spring-boot:run
```

### 2. Fluxo Básico
1. **Cadastrar Cliente** (`POST /clientes`)
2. **Cadastrar Transportadora** (`POST /transportadoras`)  
3. **Buscar Transportadoras Disponíveis** (`GET /transporte/disponiveis`)
4. **Criar Transporte** (`POST /transporte`)
5. **Acompanhar Status** via double check
6. **Finalizar** quando ambos confirmarem entrega

### 3. Testar com Insomnia
Importe a collection `insomnia_collection.json` que contém todos os endpoints configurados.

---

## 🔐 Validações e Regras

### Cliente
- Email único no sistema
- Formato de telefone brasileiro
- Desativação lógica (não remove dados)

### Transportadora  
- CNPJ único e formato válido
- Email único no sistema
- Preços devem ser positivos
- Avaliação entre 0 e 5

### Transporte
- Cliente e Transportadora devem existir e estar ativos
- Produto deve caber na caixa selecionada
- Ambos os status devem ser `ENTREGUE` para finalizar
- Cálculo automático de frete baseado em múltiplos fatores

---

## 🎯 Recursos Avançados

### Sistema de Double Check
- **Segurança**: Evita entregas falsas
- **Rastreabilidade**: Histórico completo de status
- **Automação**: Finalização automática quando ambos confirmam

### Cálculo Inteligente de Frete
- **Múltiplos Fatores**: Peso real vs. cubado
- **Preços Personalizados**: Cada transportadora tem suas tarifas
- **Integração Google Maps**: Distância e pedágio reais
- **Comparação Automática**: Resultados ordenados por preço

### Gestão Completa de Frota
- **Motoristas**: Localização em tempo real
- **Caminhões**: Especificações e capacidades
- **Viagens**: Agrupamento de transportes
- **Avaliações**: Sistema de feedback

---

## 📞 Suporte

Para dúvidas ou problemas:
- Verifique os logs da aplicação
- Consulte a collection do Insomnia
- Validar configurações do banco de dados
- Verificar conectividade com Google Maps API

---

*Última atualização: 29 de Novembro de 2024*