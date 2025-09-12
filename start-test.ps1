# PowerShell script para executar o sistema financeiro
Write-Host "===========================================" -ForegroundColor Green
Write-Host "     Sistema Financeiro - Teste com H2" -ForegroundColor Green  
Write-Host "===========================================" -ForegroundColor Green
Write-Host ""

Write-Host "1. Compilando o projeto..." -ForegroundColor Yellow
mvn clean compile

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERRO: Falha na compilacao!" -ForegroundColor Red
    pause
    exit 1
}

Write-Host ""
Write-Host "2. Executando com perfil de teste (H2)..." -ForegroundColor Yellow
Write-Host ""
Write-Host "URLs para teste:" -ForegroundColor Cyan
Write-Host "- API: http://localhost:8080/api/clients" -ForegroundColor White
Write-Host "- Swagger: http://localhost:8080/swagger-ui.html" -ForegroundColor White
Write-Host "- H2 Console: http://localhost:8080/h2-console" -ForegroundColor White
Write-Host ""
Write-Host "Pressione Ctrl+C para parar o servidor" -ForegroundColor Yellow
Write-Host ""

mvn spring-boot:run -Dspring-boot.run.profiles=test
