# Script para testar a API Financeiro completa
# Encoding: UTF-8

Write-Host "===================================" -ForegroundColor Cyan
Write-Host "   TESTE COMPLETO DA API FINANCEIRO" -ForegroundColor Cyan  
Write-Host "===================================" -ForegroundColor Cyan
Write-Host ""

# Configuracao
$baseUrl = "http://localhost:8080"

# Verificar se o Spring Boot esta rodando
Write-Host "1. Verificando se a aplicacao esta rodando..." -ForegroundColor Yellow

try {
    $health = Invoke-RestMethod -Uri "$baseUrl/actuator/health" -Method GET
    Write-Host "   Status da aplicacao: $($health.status)" -ForegroundColor Green
}
catch {
    Write-Host "   ERRO: Aplicacao nao esta rodando ou endpoint health nao esta acessivel" -ForegroundColor Red
    Write-Host "   Execute: mvn spring-boot:run" -ForegroundColor Yellow
    exit 1
}

# Teste 1: Listar clientes (deve estar vazio inicialmente)
Write-Host ""
Write-Host "2. Testando GET /api/clients..." -ForegroundColor Yellow

try {
    $clients = Invoke-RestMethod -Uri "$baseUrl/api/clients" -Method GET
    Write-Host "   GET /api/clients - OK ($($clients.Count) clientes)" -ForegroundColor Green
}
catch {
    Write-Host "   Erro ao listar clientes: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 2: Criar cliente Pessoa Fisica
Write-Host ""
Write-Host "3. Criando cliente Pessoa Fisica..." -ForegroundColor Yellow

$clientPF = @{
    name     = "João Silva"
    type     = "PF"
    document = "12345678901"
    email    = "joao@email.com"
    phone    = "11999999999"
    address  = @{
        street        = "Rua das Flores"
        addressNumber = "123"
        neighborhood  = "Centro"
        city          = "São Paulo"
        state         = "SP" 
        zipCode       = "01234567"
    }
} | ConvertTo-Json -Depth 3

try {
    $newClientPF = Invoke-RestMethod -Uri "$baseUrl/api/clients" -Method POST -Body $clientPF -ContentType "application/json"
    Write-Host "   Cliente PF criado com ID: $($newClientPF.id)" -ForegroundColor Green
    $clientPFId = $newClientPF.id
}
catch {
    Write-Host "   Erro ao criar cliente PF: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 3: Criar cliente Pessoa Juridica
Write-Host ""
Write-Host "4. Criando cliente Pessoa Juridica..." -ForegroundColor Yellow

$clientPJ = @{
    name     = "XPTO Ltda"
    type     = "PJ"
    document = "12345678000195"
    email    = "contato@xpto.com"
    phone    = "1133334444"
    address  = @{
        street        = "Av. Paulista"
        addressNumber = "1000"
        neighborhood  = "Bela Vista"
        city          = "São Paulo"
        state         = "SP"
        zipCode       = "01310100"
    }
} | ConvertTo-Json -Depth 3

try {
    $newClientPJ = Invoke-RestMethod -Uri "$baseUrl/api/clients" -Method POST -Body $clientPJ -ContentType "application/json"
    Write-Host "   Cliente PJ criado com ID: $($newClientPJ.id)" -ForegroundColor Green
    $clientPJId = $newClientPJ.id
}
catch {
    Write-Host "   Erro ao criar cliente PJ: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 4: Listar clientes novamente
Write-Host ""
Write-Host "5. Listando todos os clientes..." -ForegroundColor Yellow

try {
    $allClients = Invoke-RestMethod -Uri "$baseUrl/api/clients" -Method GET
    Write-Host "   Total de clientes: $($allClients.Count)" -ForegroundColor Green
    foreach ($client in $allClients) {
        Write-Host "   - $($client.name) ($($client.type)) - $($client.document)" -ForegroundColor Cyan
    }
}
catch {
    Write-Host "   Erro ao listar clientes: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 5: Criar conta para cliente PF
Write-Host ""
Write-Host "6. Criando conta para cliente PF..." -ForegroundColor Yellow

if ($clientPFId) {
    $accountPF = @{
        clientId    = $clientPFId
        accountType = "CORRENTE"
    } | ConvertTo-Json

    try {
        $newAccountPF = Invoke-RestMethod -Uri "$baseUrl/api/accounts" -Method POST -Body $accountPF -ContentType "application/json"
        Write-Host "   Conta PF criada - Numero: $($newAccountPF.accountNumber)" -ForegroundColor Green
        $accountPFId = $newAccountPF.id
    }
    catch {
        Write-Host "   Erro ao criar conta PF: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Teste 6: Criar conta para cliente PJ
Write-Host ""
Write-Host "7. Criando conta para cliente PJ..." -ForegroundColor Yellow

if ($clientPJId) {
    $accountPJ = @{
        clientId    = $clientPJId
        accountType = "CORRENTE"
    } | ConvertTo-Json

    try {
        $newAccountPJ = Invoke-RestMethod -Uri "$baseUrl/api/accounts" -Method POST -Body $accountPJ -ContentType "application/json"
        Write-Host "   Conta PJ criada - Numero: $($newAccountPJ.accountNumber)" -ForegroundColor Green
        $accountPJId = $newAccountPJ.id
    }
    catch {
        Write-Host "   Erro ao criar conta PJ: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Teste 7: Listar todas as contas
Write-Host ""
Write-Host "8. Listando todas as contas..." -ForegroundColor Yellow

try {
    $accounts = Invoke-RestMethod -Uri "$baseUrl/api/accounts" -Method GET
    Write-Host "   Total de contas: $($accounts.Count)" -ForegroundColor Green
    foreach ($account in $accounts) {
        Write-Host "   - Conta: $($account.accountNumber) - Saldo: R$ $($account.balance)" -ForegroundColor Cyan
    }
}
catch {
    Write-Host "   Erro ao listar contas: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 8: Fazer deposito na conta PF
Write-Host ""
Write-Host "9. Fazendo deposito de R$ 1000 na conta PF..." -ForegroundColor Yellow

if ($accountPFId) {
    $deposit = @{
        accountId   = $accountPFId
        type        = "DEPOSITO"
        amount      = 1000.00
        description = "Deposito inicial"
    } | ConvertTo-Json

    try {
        $transaction1 = Invoke-RestMethod -Uri "$baseUrl/api/transactions" -Method POST -Body $deposit -ContentType "application/json"
        Write-Host "   Deposito realizado - ID: $($transaction1.id)" -ForegroundColor Green
    }
    catch {
        Write-Host "   Erro ao fazer deposito: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Teste 9: Fazer transferencia da conta PF para PJ
Write-Host ""
Write-Host "10. Fazendo transferencia de R$ 250 da conta PF para PJ..." -ForegroundColor Yellow

if ($accountPFId -and $accountPJId) {
    $transfer = @{
        fromAccountId = $accountPFId
        toAccountId   = $accountPJId
        type          = "TRANSFERENCIA"
        amount        = 250.00
        description   = "Transferencia entre contas"
    } | ConvertTo-Json

    try {
        $transaction2 = Invoke-RestMethod -Uri "$baseUrl/api/transactions" -Method POST -Body $transfer -ContentType "application/json"
        Write-Host "   Transferencia realizada - ID: $($transaction2.id)" -ForegroundColor Green
    }
    catch {
        Write-Host "   Erro ao fazer transferencia: $($_.Exception.Message)" -ForegroundColor Red
    }
}

# Teste 10: Listar movimentacoes
Write-Host ""
Write-Host "11. Listando todas as movimentacoes..." -ForegroundColor Yellow

try {
    $transactions = Invoke-RestMethod -Uri "$baseUrl/api/transactions" -Method GET
    Write-Host "   Total de movimentacoes: $($transactions.Count)" -ForegroundColor Green
    foreach ($transaction in $transactions) {
        Write-Host "   - $($transaction.type): R$ $($transaction.amount) - $($transaction.description)" -ForegroundColor Cyan
    }
}
catch {
    Write-Host "   Erro ao listar movimentacoes: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 11: Verificar saldos finais
Write-Host ""
Write-Host "12. Verificando saldos finais..." -ForegroundColor Yellow

try {
    $finalAccounts = Invoke-RestMethod -Uri "$baseUrl/api/accounts" -Method GET
    foreach ($account in $finalAccounts) {
        Write-Host "   - Conta $($account.accountNumber): R$ $($account.balance)" -ForegroundColor Green
    }
}
catch {
    Write-Host "   Erro ao verificar saldos: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 12: Testar relatorios
Write-Host ""
Write-Host "13. Testando relatorios..." -ForegroundColor Yellow

try {
    $reports = Invoke-RestMethod -Uri "$baseUrl/api/reports/clients/balance" -Method GET
    Write-Host "   Relatorio de saldos - $($reports.Count) registros" -ForegroundColor Green
}
catch {
    Write-Host "   Erro ao gerar relatorio: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "===================================" -ForegroundColor Cyan
Write-Host "      TESTE COMPLETO FINALIZADO    " -ForegroundColor Cyan
Write-Host "===================================" -ForegroundColor Cyan