@echo off
echo ===========================================
echo     Sistema Financeiro - Teste com H2
echo ===========================================
echo.

echo 1. Compilando o projeto...
call mvn clean compile
if %ERRORLEVEL% neq 0 (
    echo ERRO: Falha na compilacao!
    pause
    exit /b 1
)

echo.
echo 2. Executando com perfil de teste (H2)...
echo.
echo URLs para teste:
echo - API: http://localhost:8080/api/clients
echo - Swagger: http://localhost:8080/swagger-ui.html
echo - H2 Console: http://localhost:8080/h2-console
echo.
echo Pressione Ctrl+C para parar o servidor
echo.

call mvn spring-boot:run -Dspring-boot.run.profiles=test

pause
