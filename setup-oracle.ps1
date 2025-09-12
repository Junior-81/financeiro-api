# Script para configurar Oracle Database para o Sistema Financeiro XPTO

Write-Host "============================================" -ForegroundColor Green
Write-Host "   Configuração Oracle - Sistema Financeiro" -ForegroundColor Green  
Write-Host "============================================" -ForegroundColor Green
Write-Host ""

Write-Host "PASSOS PARA CONFIGURAR O ORACLE:" -ForegroundColor Yellow
Write-Host ""
Write-Host "1. Conecte-se ao Oracle como SYSTEM:" -ForegroundColor Cyan
Write-Host "   sqlplus system/oracle@localhost:1521/XE" -ForegroundColor White
Write-Host ""
Write-Host "2. Execute os comandos para criar o usuário:" -ForegroundColor Cyan
Write-Host "   CREATE USER xpto_financeiro IDENTIFIED BY xpto123;" -ForegroundColor White
Write-Host "   GRANT CONNECT, RESOURCE TO xpto_financeiro;" -ForegroundColor White
Write-Host "   GRANT CREATE SESSION TO xpto_financeiro;" -ForegroundColor White
Write-Host "   GRANT CREATE TABLE TO xpto_financeiro;" -ForegroundColor White
Write-Host "   GRANT CREATE SEQUENCE TO xpto_financeiro;" -ForegroundColor White
Write-Host "   GRANT CREATE PROCEDURE TO xpto_financeiro;" -ForegroundColor White
Write-Host "   GRANT CREATE TRIGGER TO xpto_financeiro;" -ForegroundColor White
Write-Host "   ALTER USER xpto_financeiro QUOTA UNLIMITED ON USERS;" -ForegroundColor White
Write-Host "   COMMIT;" -ForegroundColor White
Write-Host ""
Write-Host "3. Teste a conexão:" -ForegroundColor Cyan
Write-Host "   CONNECT xpto_financeiro/xpto123;" -ForegroundColor White
Write-Host ""
Write-Host "4. Execute a aplicação Spring Boot:" -ForegroundColor Cyan
Write-Host "   mvn spring-boot:run" -ForegroundColor White
Write-Host ""

Write-Host "ALTERNATIVA - Execute o script SQL automaticamente:" -ForegroundColor Yellow
Write-Host ""
$scriptPath = Join-Path $PSScriptRoot "src\main\resources\sql\setup_oracle.sql"
Write-Host "sqlplus system/oracle@localhost:1521/XE @$scriptPath" -ForegroundColor White
Write-Host ""

Write-Host "URLs após iniciar a aplicação:" -ForegroundColor Cyan
Write-Host "- API: http://localhost:8080/api/clients" -ForegroundColor White
Write-Host "- Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host ""

$response = Read-Host "Deseja tentar executar a aplicação agora? (y/n)"
if ($response -eq "y" -or $response -eq "Y") {
    Write-Host "Executando aplicação..." -ForegroundColor Green
    mvn spring-boot:run
}
else {
    Write-Host "Configure o Oracle primeiro, depois execute: mvn spring-boot:run" -ForegroundColor Yellow
}
