# Script para testar a API do Sistema Financeiro
$baseUrl = "http://localhost:8080"

Write-Host "===========================================" -ForegroundColor Green
Write-Host "     Testando API Sistema Financeiro" -ForegroundColor Green
Write-Host "===========================================" -ForegroundColor Green
Write-Host ""

# Função para testar se a API está rodando
function Test-ApiHealth {
    try {
        $response = Invoke-RestMethod -Uri "$baseUrl/actuator/health" -Method Get -TimeoutSec 5
        return $true
    }
    catch {
        return $false
    }
}

# Verificar se a API está rodando
Write-Host "1. Verificando se a API está rodando..." -ForegroundColor Yellow
if (Test-ApiHealth) {
    Write-Host "✅ API está rodando!" -ForegroundColor Green
}
else {
    Write-Host "❌ API não está rodando. Execute 'start-test.ps1' primeiro!" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "2. Testando endpoints..." -ForegroundColor Yellow

# Teste 1: Listar clientes (deve estar vazio)
Write-Host "   📋 Listando clientes..." -ForegroundColor Cyan
try {
    $clients = Invoke-RestMethod -Uri "$baseUrl/api/clients" -Method Get
    Write-Host "   ✅ GET /api/clients - OK ($($clients.Count) clientes)" -ForegroundColor Green
}
catch {
    Write-Host "   ❌ Erro ao listar clientes: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 2: Criar cliente PF
Write-Host "   👤 Criando cliente Pessoa Física..." -ForegroundColor Cyan
$clientPF = @{
    name       = "João Silva"
    cellPhone  = "11999999999"
    clientType = "PF"
    cpf        = "12345678901"
} | ConvertTo-Json

try {
    $newClientPF = Invoke-RestMethod -Uri "$baseUrl/api/clients" -Method Post -Body $clientPF -ContentType "application/json"
    Write-Host "   ✅ POST /api/clients (PF) - Criado! ID: $($newClientPF.id)" -ForegroundColor Green
}
catch {
    Write-Host "   ❌ Erro ao criar cliente PF: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 3: Criar cliente PJ
Write-Host "   🏢 Criando cliente Pessoa Jurídica..." -ForegroundColor Cyan
$clientPJ = @{
    name       = "XPTO Ltda"
    cellPhone  = "1133333333"
    clientType = "PJ"
    cnpj       = "12345678000199"
} | ConvertTo-Json

try {
    $newClientPJ = Invoke-RestMethod -Uri "$baseUrl/api/clients" -Method Post -Body $clientPJ -ContentType "application/json"
    Write-Host "   ✅ POST /api/clients (PJ) - Criado! ID: $($newClientPJ.id)" -ForegroundColor Green
}
catch {
    Write-Host "   ❌ Erro ao criar cliente PJ: $($_.Exception.Message)" -ForegroundColor Red
}

# Teste 4: Listar clientes novamente
Write-Host "   📋 Listando clientes novamente..." -ForegroundColor Cyan
try {
    $clients = Invoke-RestMethod -Uri "$baseUrl/api/clients" -Method Get
    Write-Host "   ✅ GET /api/clients - OK ($($clients.Count) clientes cadastrados)" -ForegroundColor Green
    
    foreach ($client in $clients) {
        Write-Host "      - $($client.name) ($($client.clientType))" -ForegroundColor White
    }
}
catch {
    Write-Host "   ❌ Erro ao listar clientes: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "===========================================" -ForegroundColor Green
Write-Host "     Teste Concluído!" -ForegroundColor Green
Write-Host "===========================================" -ForegroundColor Green
Write-Host ""
Write-Host "URLs úteis:" -ForegroundColor Yellow
Write-Host "- API: $baseUrl/api/clients" -ForegroundColor White
Write-Host "- Swagger: $baseUrl/swagger-ui.html" -ForegroundColor White
Write-Host "- H2 Console: $baseUrl/h2-console" -ForegroundColor White
