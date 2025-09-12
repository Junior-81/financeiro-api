# Script para configurar SQL Server para Sistema Financeiro XPTO

Write-Host "============================================" -ForegroundColor Green
Write-Host "   Configuração SQL Server - Sistema Financeiro" -ForegroundColor Green  
Write-Host "============================================" -ForegroundColor Green
Write-Host ""

# Verificar se SQL Server está rodando
Write-Host "1. Verificando serviços SQL Server..." -ForegroundColor Yellow
$sqlServices = Get-Service -Name "*SQL*" | Where-Object { $_.Status -eq "Running" }

if ($sqlServices) {
    Write-Host "✅ Serviços SQL Server encontrados:" -ForegroundColor Green
    $sqlServices | ForEach-Object { Write-Host "   - $($_.Name): $($_.Status)" -ForegroundColor White }
}
else {
    Write-Host "❌ Nenhum serviço SQL Server rodando!" -ForegroundColor Red
    Write-Host "Tentando iniciar MSSQLSERVER..." -ForegroundColor Yellow
    
    try {
        Start-Service -Name "MSSQLSERVER" -ErrorAction Stop
        Write-Host "✅ MSSQLSERVER iniciado com sucesso!" -ForegroundColor Green
    }
    catch {
        Write-Host "❌ Erro ao iniciar MSSQLSERVER: $($_.Exception.Message)" -ForegroundColor Red
        Write-Host "Verificando SQL Server Express..." -ForegroundColor Yellow
        
        try {
            Start-Service -Name "MSSQL`$SQLEXPRESS" -ErrorAction Stop
            Write-Host "✅ SQL Server Express iniciado!" -ForegroundColor Green
        }
        catch {
            Write-Host "❌ SQL Server não encontrado. Instale SQL Server primeiro." -ForegroundColor Red
            exit 1
        }
    }
}

Write-Host ""

# Verificar porta 1433
Write-Host "2. Verificando porta 1433..." -ForegroundColor Yellow
$port1433 = netstat -an | findstr :1433
if ($port1433) {
    Write-Host "✅ Porta 1433 está aberta" -ForegroundColor Green
}
else {
    Write-Host "⚠️  Porta 1433 não encontrada. Verifique configuração TCP/IP" -ForegroundColor Yellow
}

Write-Host ""

# Testar conexão
Write-Host "3. Testando conexão com SQL Server..." -ForegroundColor Yellow
try {
    $testConnection = sqlcmd -S localhost -E -Q "SELECT @@VERSION" -h -1 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Conexão Windows Authentication funcionando!" -ForegroundColor Green
    }
    else {
        Write-Host "⚠️  Windows Authentication com problemas" -ForegroundColor Yellow
    }
}
catch {
    Write-Host "❌ Erro na conexão: $($_.Exception.Message)" -ForegroundColor Red
}

Write-Host ""
Write-Host "4. Próximos passos:" -ForegroundColor Cyan
Write-Host "   a) Configure SQL Server Authentication no SSMS" -ForegroundColor White
Write-Host "   b) Execute o script setup_sqlserver.sql" -ForegroundColor White
Write-Host "   c) Execute: mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver" -ForegroundColor White
Write-Host ""

$response = Read-Host "Deseja tentar executar a aplicação agora com SQL Server? (y/n)"
if ($response -eq "y" -or $response -eq "Y") {
    Write-Host "Executando aplicação com perfil SQL Server..." -ForegroundColor Green
    mvn spring-boot:run -Dspring-boot.run.profiles=sqlserver
}
else {
    Write-Host "Configure o SQL Server primeiro conforme o guia TROUBLESHOOTING_SQLSERVER.md" -ForegroundColor Yellow
}
