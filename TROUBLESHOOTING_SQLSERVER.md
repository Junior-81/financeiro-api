# 🔧 GUIA DE TROUBLESHOOTING - SQL SERVER

## ❌ **ERRO: "Could not open a connection to SQL Server"**

### 🔍 **DIAGNÓSTICO RÁPIDO**

Execute estes comandos para verificar:

```powershell
# Verificar se SQL Server está rodando
Get-Service -Name "*SQL*" | Where-Object {$_.Status -eq "Running"}

# Verificar se a porta 1433 está aberta
netstat -an | findstr :1433

# Testar conexão local
sqlcmd -S localhost -E -Q "SELECT @@VERSION"
```

### 🛠️ **SOLUÇÕES POR ETAPAS**

#### **1. VERIFICAR SERVIÇOS SQL SERVER**
```powershell
# Iniciar SQL Server se não estiver rodando
Start-Service -Name "MSSQLSERVER"
Start-Service -Name "SQLSERVERAGENT"
```

#### **2. HABILITAR TCP/IP**
1. Abra **SQL Server Configuration Manager**
2. Va em **SQL Server Network Configuration** > **Protocols for MSSQLSERVER**
3. Clique direito em **TCP/IP** > **Enable**
4. Reinicie o serviço SQL Server

#### **3. CONFIGURAR AUTHENTICATION MODE**
No **SSMS** execute:
```sql
-- Habilitar SQL Server Authentication
EXEC sp_configure 'show advanced options', 1;
RECONFIGURE;
EXEC sp_configure 'remote access', 1;
RECONFIGURE;
```

#### **4. CRIAR USUÁRIO SA (se necessário)**
```sql
-- No SSMS como administrador
ALTER LOGIN sa ENABLE;
ALTER LOGIN sa WITH PASSWORD = 'YourStrong@Passw0rd';
```

### 🎯 **CONFIGURAÇÕES ESPECÍFICAS**

#### **Para SQL Server Express:**
- Service Name: **SQLEXPRESS**
- Connection String: `localhost\\SQLEXPRESS`

#### **Para SQL Server Developer:**
- Service Name: **MSSQLSERVER**  
- Connection String: `localhost`

### ⚙️ **CONFIGURAÇÃO DA APLICAÇÃO**

Atualize a configuração conforme seu SQL Server:

```yaml
# application-sqlserver.yml
spring:
  datasource:
    # Para SQL Server padrão:
    url: jdbc:sqlserver://localhost:1433;databaseName=financeiro_xpto;encrypt=false
    
    # Para SQL Server Express:
    # url: jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=financeiro_xpto;encrypt=false
    
    # Para instância nomeada:
    # url: jdbc:sqlserver://localhost\\INSTANCE_NAME;databaseName=financeiro_xpto;encrypt=false
    
    username: sa
    password: YourStrong@Passw0rd
```

### 🚀 **EXECUTAR COM SQL SERVER**

```powershell
# Compilar projeto
mvn clean compile

# Executar com perfil SQL Server
mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver
```

### 🔧 **TROUBLESHOOTING AVANÇADO**

#### **Erro de Autenticação:**
```sql
-- Verificar modo de autenticação
SELECT SERVERPROPERTY('IsIntegratedSecurityOnly');
-- 0 = Mixed Mode, 1 = Windows Only
```

#### **Erro de Porta:**
```powershell
# Verificar porta do SQL Server
Get-ItemProperty -Path "HKLM:\SOFTWARE\Microsoft\Microsoft SQL Server\MSSQL*\MSSQLServer\SuperSocketNetLib\Tcp\IPAll" -Name TcpPort
```

#### **Erro de Firewall:**
```powershell
# Adicionar regra no firewall
New-NetFirewallRule -DisplayName "SQL Server" -Direction Inbound -Protocol TCP -LocalPort 1433 -Action Allow
```

### ✅ **TESTE DE CONEXÃO**

```powershell
# Teste manual de conexão
sqlcmd -S localhost -U sa -P "YourStrong@Passw0rd" -Q "SELECT @@VERSION"
```

### 📞 **COMANDOS ÚTEIS**

```powershell
# Status dos serviços SQL
Get-Service -Name "*SQL*"

# Reiniciar SQL Server
Restart-Service -Name "MSSQLSERVER" -Force

# Verificar portas abertas
netstat -an | findstr :1433
```

---

## 🎯 **RESUMO RÁPIDO:**

1. ✅ Verificar se SQL Server está **rodando**
2. ✅ Habilitar **TCP/IP** no Configuration Manager  
3. ✅ Configurar **Mixed Mode Authentication**
4. ✅ Criar/habilitar usuário **sa**
5. ✅ Executar: `mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver`

**🚀 Aplicação funcionando com SQL Server!**
