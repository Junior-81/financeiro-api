-- Script de configuração SQL Server para Sistema Financeiro XPTO
-- Execute no SQL Server Management Studio (SSMS) ou Azure Data Studio

-- 1. Criar database
CREATE DATABASE financeiro_xpto;
GO

-- 2. Usar o database
USE financeiro_xpto;
GO

-- 3. Criar login/usuário (opcional - pode usar sa)
CREATE LOGIN xpto_user WITH PASSWORD = 'XptoFinanceiro@123';
GO

CREATE USER xpto_user FOR LOGIN xpto_user;
GO

-- 4. Conceder permissões
ALTER ROLE db_owner ADD MEMBER xpto_user;
GO

-- 5. Verificar se está funcionando
SELECT @@VERSION;
SELECT DB_NAME();
GO

-- 6. Testar criação de tabela
CREATE TABLE teste_conexao (
    id INT IDENTITY(1,1) PRIMARY KEY,
    nome NVARCHAR(100),
    criado_em DATETIME2 DEFAULT GETDATE()
);
GO

-- 7. Inserir dados de teste
INSERT INTO teste_conexao (nome) VALUES ('Teste Conexao SQL Server');
GO

-- 8. Verificar
SELECT * FROM teste_conexao;
GO

-- 9. Limpar teste
DROP TABLE teste_conexao;
GO
