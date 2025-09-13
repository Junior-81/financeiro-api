# Sistema Financeiro XPTO

Este projeto é uma API backend para o gerenciamento de clientes, contas bancárias, transações e relatórios financeiros. Utiliza Java com Spring Boot, Hibernate e JPA para operações de banco de dados, incluindo procedures e triggers em Oracle Database para automação de cálculos e relatórios.

## Tecnologias Utilizadas

- Java 8
- Spring Boot 2.7.15
- Spring Data JPA
- Hibernate
- Oracle Database XE
- Flyway
- Swagger
- Maven

## Instalação

1. Clone o repositório:
```bash
git clone https://github.com/Junior-81/financeiro-api.git
cd financeiro-api
```

2. Configure o banco de dados Oracle:
```sql
-- Como usuário SYSTEM:
spring.datasource.url=jdbc:oracle:thin:@//<host>:<port>/<service>
spring.datasource.username=seu-usuario
spring.datasource.password=sua-senha
```

3. Execute o projeto:
```bash
mvn clean compile
mvn spring-boot:run
```

## Principais Endpoints

> **Dica**: Use o arquivo `test-api-fixed.ps1` na raiz do projeto para testar todos os endpoints automaticamente.

### Documentação
- Acesse `/swagger-ui.html` para documentação interativa da API

### Cliente
- `GET /api/clients`: Listar todos os clientes
- `GET /api/clients/{id}`: Obter detalhes de um cliente por ID
- `POST /api/clients`: Criação de um novo cliente
- `PUT /api/clients/{id}`: Atualizar informações de um cliente
- `DELETE /api/clients/{id}`: Excluir um cliente

### Conta
- `GET /api/accounts`: Listar todas as contas
- `GET /api/accounts/client/{id}`: Lista todas as contas de um cliente
- `POST /api/accounts`: Criação de uma nova conta
- `PUT /api/accounts/{id}`: Atualizar informações de uma conta
- `DELETE /api/accounts/{id}`: Excluir uma conta

### Transações
- `GET /api/transactions`: Lista todas as transações realizadas
- `GET /api/transactions/account/{id}`: Busca todas as transações de uma conta
- `POST /api/transactions`: Criar uma nova transação

### Endereços
- `GET /api/addresses`: Listar todos os endereços
- `GET /api/addresses/client/{id}`: Busca endereços de um cliente
- `POST /api/addresses`: Criar novo endereço

### Relatórios
- `GET /api/reports/clients/balance`: Retorna o relatório de saldo de todos os clientes
- `GET /api/reports/client/{id}/balance`: Retorna o relatório de saldo do cliente
- `GET /api/reports/client/{id}/fee`: Retorna a tarifa calculada do cliente
- `GET /api/reports/company/revenue`: Retorna o relatório de receita da empresa

## Procedures e Functions

### Functions
- `FNC_CLIENT_FEE`: Calcula tarifa do cliente baseada no número de movimentações

### Procedures
- `PRC_COMPANY_REVENUE_REPORT`: Retorna dados do relatório de receita da empresa

### Triggers
- Atualização automática de saldos nas contas após transações

## Boas Práticas Utilizadas

- Utilização do padrão REST
- Tratamento de Exceções centralizado
- Validação de Dados (CPF/CNPJ, email, etc.)
- Padrão Repository e Service
- DTOs para transferência de dados
- Transações declarativas

## Explicações

### Por que uma API?
Escolhi fazer uma API REST por ser um padrão amplamente usado no mercado e pela flexibilidade de integração com diferentes frontends.

### Separação de Componentes
Separei os componentes em Model, Repository, Service e Controller utilizando DTOs para transferência de dados. Optei por essa divisão pensando na separação de responsabilidades:

- **Controller**: Responsável por definir as rotas e parâmetros
- **Service**: Centraliza a lógica de negócio 
- **Repository**: Realiza interação com banco de dados
- **Model**: Define as entidades JPA e regras de domínio

### Uso de Procedures e Functions
Optei por usar procedures e functions PL/SQL para cálculos complexos visando melhor performance e centralização da lógica de negócio no banco de dados.