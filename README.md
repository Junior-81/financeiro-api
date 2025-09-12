# 💰 Sistema Financeiro XPTO - API REST

Sistema de controle financeiro desenvolvido em **Java 8 + Spring Boot + Oracle Database** para o desafio da célula Financeiro e Controladoria.

## 🎯 Objetivo do Desafio

Desenvolver uma API REST para controle de receitas e despesas de clientes da empresa fictícia XPTO, com as seguintes funcionalidades:

- **CRUD** de clientes (PF/PJ)
- **CRUD** de endereços 
- **CRUD** de contas bancárias
- **Controle** de movimentações financeiras
- **Relatórios** de saldos e receita da empresa
- **Cálculo automático** de tarifas por movimentação

## 🛠️ Tecnologias Utilizadas

- **Java 8**
- **Spring Boot 2.7.15**
- **Spring Data JPA**
- **Oracle Database XE** *(obrigatório conforme desafio)*
- **Flyway** (migrações)
- **Maven**
- **Swagger/OpenAPI**
- **PL/SQL** (procedures, functions, triggers)

## 🗄️ Configuração do Oracle Database

### 1. **Criar Usuário no Oracle**

Execute como usuário **SYSTEM** no Oracle:

```sql
-- Conectar como SYSTEM
sqlplus system/oracle@localhost:1521/XE

-- Criar usuário específico do projeto
CREATE USER xpto_financeiro IDENTIFIED BY xpto123;
GRANT CONNECT, RESOURCE TO xpto_financeiro;
GRANT CREATE SESSION TO xpto_financeiro;
GRANT CREATE TABLE TO xpto_financeiro;
GRANT CREATE SEQUENCE TO xpto_financeiro;
GRANT CREATE PROCEDURE TO xpto_financeiro;
GRANT CREATE TRIGGER TO xpto_financeiro;
ALTER USER xpto_financeiro QUOTA UNLIMITED ON USERS;
COMMIT;

-- Testar conexão
CONNECT xpto_financeiro/xpto123;
```

### 2. **Script Automático**

```powershell
# Execute o script de configuração
.\setup-oracle.ps1

# OU execute manualmente o SQL
sqlplus system/oracle@localhost:1521/XE @create_user.sql
```

## 🚀 Como Executar

### 1. **Configurar Oracle** (obrigatório)
```bash
# Certificar que Oracle XE está rodando
# Criar usuário xpto_financeiro (ver seção acima)
```

### 2. **Executar Aplicação**
```bash
# Compilar projeto
mvn clean compile

# Executar aplicação
mvn spring-boot:run
```

## 🔗 Endpoints da API

### Base URL: `http://localhost:8080`

### 📋 **Clientes**
- **GET** `/api/clients` - Listar todos os clientes
- **GET** `/api/clients/{id}` - Buscar cliente por ID
- **POST** `/api/clients` - Criar novo cliente
- **PUT** `/api/clients/{id}` - Atualizar cliente
- **DELETE** `/api/clients/{id}` - Excluir cliente

### 🏦 **Contas**
- **GET** `/api/accounts` - Listar todas as contas
- **GET** `/api/accounts/{id}` - Buscar conta por ID
- **GET** `/api/accounts/client/{clientId}` - Contas de um cliente
- **POST** `/api/accounts` - Criar nova conta
- **DELETE** `/api/accounts/{id}` - Excluir conta

### 💸 **Transações**
- **GET** `/api/transactions/account/{accountId}` - Extrato da conta
- **POST** `/api/transactions/account/{accountId}` - Nova transação

### 📊 **Relatórios**
- **GET** `/api/reports/client/{clientId}` - Relatório de saldo do cliente
- **GET** `/api/reports/client/{clientId}?start=DATA&end=DATA` - Relatório por período
- **GET** `/api/reports/all-clients` - Saldo de todos os clientes
- **GET** `/api/reports/company-revenue?start=DATA&end=DATA` - Receita da empresa

### 📖 **Documentação**
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`

## 🧪 Testando a API

### 1. **Criar Cliente Pessoa Física**
```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "cellPhone": "11999999999",
    "clientType": "PF",
    "cpf": "12345678901"
  }'
```

### 2. **Criar Cliente Pessoa Jurídica**
```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "XPTO Ltda",
    "cellPhone": "1133333333",
    "clientType": "PJ",
    "cnpj": "12345678000199"
  }'
```

### 3. **Criar Conta Bancária**
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountNumber": "12345-6",
    "initialBalance": 1000.00,
    "clientId": "CLIENT_UUID_AQUI"
  }'
```

### 4. **Fazer Transação (Crédito)**
```bash
curl -X POST http://localhost:8080/api/transactions/account/ACCOUNT_UUID \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 500.00,
    "operationType": "C"
  }'
```

## 📊 Estrutura do Banco Oracle

### **Tabelas Principais:**
- `CLIENT` - Dados dos clientes (PF/PJ)
- `ADDRESS` - Endereços dos clientes
- `ACCOUNT` - Contas bancárias
- `TRANSACTIONS` - Movimentações financeiras

### **Objetos PL/SQL:**
- **Function** `calculate_client_fee()` - Calcula tarifa por período
- **Procedure** `get_client_balance_report()` - Relatório de saldo
- **Procedure** `get_all_clients_balance()` - Saldo de todos
- **Procedure** `get_company_revenue_report()` - Receita da empresa
- **Trigger** `tr_update_account_balance` - Atualiza saldo automaticamente

### **Regras de Tarifação:**
- Até 10 movimentações: **R$ 1,00** por movimentação
- De 10 a 20 movimentações: **R$ 0,75** por movimentação  
- Acima de 20 movimentações: **R$ 0,50** por movimentação

## 🎯 Funcionalidades Implementadas

✅ **CRUD Completo de Clientes**  
✅ **CRUD Completo de Endereços**  
✅ **CRUD Completo de Contas**  
✅ **Sistema de Transações Financeiras**  
✅ **Relatórios Conforme Especificação**  
✅ **Objetos PL/SQL (Functions, Procedures, Triggers)**  
✅ **Cálculo Automático de Tarifas**  
✅ **Validações de Negócio**  
✅ **Tratamento de Exceções**  
✅ **Documentação Swagger**  

## ❗ Requisitos do Sistema

- **Java 8+**
- **Maven 3.6+**
- **Oracle Database XE** (obrigatório)
- **Conexão**: `localhost:1521/XE`

## 🔍 Logs e Monitoramento

```bash
# Logs SQL habilitados para debug
# Métricas de performance via Actuator
# Health checks disponíveis em /actuator/health
```

---

## 🚀 **INÍCIO RÁPIDO**

```bash
# 1. Configure o Oracle (ver seção "Configuração do Oracle Database")
sqlplus system/oracle@localhost:1521/XE @create_user.sql

# 2. Execute a aplicação
mvn clean compile
mvn spring-boot:run

# 3. Acesse a documentação
# http://localhost:8080/swagger-ui.html
```

**🎯 Sistema funcional conforme especificação do desafio!**

## 🛠️ Tecnologias Utilizadas

- **Java 8**
- **Spring Boot 2.7.15**
- **Spring Data JPA**
- **Oracle Database** (produção)
- **H2 Database** (desenvolvimento/testes)
- **Flyway** (migrações)
- **Maven**
- **Swagger/OpenAPI**

## 🚀 Como Executar

### 1. **Executar com H2 (Banco em Memória) - RECOMENDADO PARA TESTES**

```bash
# Navegar até o diretório do projeto
cd financeiro-api

# Executar com perfil de teste
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

**OU**

```bash
# Compilar e executar
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"
```

### 2. **Executar com Oracle Database (Produção)**

Antes de executar, certifique-se de ter o Oracle configurado:

```bash
# Executar com perfil padrão
mvn spring-boot:run
```

## 🔗 Endpoints Disponíveis

### Base URL: `http://localhost:8080`

### 📋 Clientes
- **GET** `/api/clients` - Listar todos os clientes
- **GET** `/api/clients/{id}` - Buscar cliente por ID
- **POST** `/api/clients` - Criar novo cliente
- **PUT** `/api/clients/{id}` - Atualizar cliente
- **DELETE** `/api/clients/{id}` - Excluir cliente

### 📖 Documentação
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api-docs`

### 🗄️ Console H2 (modo teste)
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: *(vazio)*

## 🧪 Testando a API

### 1. **Criar um Cliente Pessoa Física**

```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "João Silva",
    "cellPhone": "11999999999",
    "clientType": "PF",
    "cpf": "12345678901"
  }'
```

### 2. **Criar um Cliente Pessoa Jurídica**

```bash
curl -X POST http://localhost:8080/api/clients \
  -H "Content-Type: application/json" \
  -d '{
    "name": "XPTO Ltda",
    "cellPhone": "1133333333",
    "clientType": "PJ",
    "cnpj": "12345678000199"
  }'
```

### 3. **Listar Todos os Clientes**

```bash
curl -X GET http://localhost:8080/api/clients
```

### 4. **Buscar Cliente por ID**

```bash
curl -X GET http://localhost:8080/api/clients/{id}
```

## 📊 Estrutura do Banco

### Tabelas Principais:
- `CLIENT` - Dados dos clientes (PF/PJ)
- `ADDRESS` - Endereços dos clientes
- `ACCOUNT` - Contas bancárias
- `TRANSACTION` - Movimentações financeiras

### Relacionamentos:
- `Client` 1:1 `Address`
- `Client` 1:N `Account`
- `Account` 1:N `Transaction`

## 🔍 Logs e Debug

Para ver os logs SQL e debug:

```bash
# Os logs já estão configurados para mostrar:
# - SQL queries (DEBUG)
# - Parâmetros (TRACE)
# - Aplicação (DEBUG)
```

## ❗ Problemas Comuns

### 1. **Porta já está em uso**
```bash
# Mudar a porta no application.yml ou usar:
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=8081"
```

### 2. **Erro de conexão Oracle**
```bash
# Use o perfil de teste com H2:
mvn spring-boot:run -Dspring-boot.run.profiles=test
```

### 3. **Erro de validação**
- Verifique se o JSON está correto
- CPF é obrigatório para clientType: "PF"
- CNPJ é obrigatório para clientType: "PJ"

## 🎯 Próximos Passos

1. ✅ **Clientes** - Implementado
2. 🔄 **Endereços** - Em desenvolvimento
3. 🔄 **Contas** - Em desenvolvimento
4. 🔄 **Transações** - Em desenvolvimento
5. 🔄 **Relatórios** - Em desenvolvimento

---

## 🚀 **INÍCIO RÁPIDO**

```bash
# Clone e execute em 3 comandos:
cd financeiro-api
mvn clean compile
mvn spring-boot:run -Dspring-boot.run.profiles=test

# Acesse: http://localhost:8080/swagger-ui.html
```

## Estrutura do Projeto

O projeto é organizado da seguinte forma:

- **src/main/java/com/xpto/financeiro**: Contém o código-fonte da aplicação.
  - **FinanceiroApplication.java**: Ponto de entrada da aplicação Spring Boot.
  - **controller**: Contém as classes responsáveis por gerenciar as requisições HTTP.
    - **ClienteController.java**: Gerencia operações CRUD para clientes.
    - **RelatorioController.java**: Gerencia a geração de relatórios financeiros.
  - **model**: Contém as classes que representam as entidades do sistema.
    - **Cliente.java**: Representa um cliente.
    - **Endereco.java**: Representa o endereço de um cliente.
    - **Conta.java**: Representa uma conta bancária de um cliente.
    - **Movimentacao.java**: Representa uma movimentação financeira.
  - **repository**: Contém as interfaces para acesso a dados.
    - **ClienteRepository.java**: Interface para operações de clientes.
    - **MovimentacaoRepository.java**: Interface para operações de movimentações.
  - **service**: Contém a lógica de negócios.
    - **ClienteService.java**: Gerencia a lógica relacionada a clientes.
    - **RelatorioService.java**: Gerencia a lógica para geração de relatórios.

- **src/main/resources**: Contém recursos da aplicação.
  - **application.properties**: Configurações da aplicação, incluindo conexão com o banco de dados Oracle.
  - **sql**: Scripts SQL para criação de tabelas e procedimentos armazenados.
    - **schema.sql**: Script para criar as tabelas necessárias.
    - **procedures.sql**: Definição de procedimentos armazenados.

- **src/test/java/com/xpto/financeiro**: Contém os testes unitários.
  - **ClienteServiceTest.java**: Testes para a classe `ClienteService`.

- **pom.xml**: Gerenciador de dependências do Maven.

## Funcionalidades

- CRUD de clientes, incluindo manutenção de dados.
- CRUD de endereços e contas bancárias.
- Geração de relatórios financeiros, incluindo saldo de clientes e receitas da empresa.
- Cálculo de tarifas baseadas na quantidade de movimentações.

## Instruções de Uso

1. Clone o repositório:
   ```
   git clone <URL_DO_REPOSITORIO>
   ```

2. Navegue até o diretório do projeto:
   ```
   cd xpto-financeiro-api
   ```

3. Compile e execute a aplicação:
   ```
   mvn spring-boot:run
   ```

4. Acesse a API através do endpoint:
   ```
   http://localhost:8080
   ```

## Boas Práticas

- O código segue as boas práticas de desenvolvimento em Java e Spring Boot.
- Utilização de padrões de projeto como MVC (Model-View-Controller) para organização do código.
- Testes unitários implementados para garantir a qualidade do código.

## Contribuições

Sinta-se à vontade para contribuir com melhorias ou correções. Para isso, crie um fork do repositório e envie um pull request com suas alterações.

## Licença

Este projeto é de uso livre.