-- V5__fix_plsql_functions.sql
-- Correção das funções PL/SQL para usar VARCHAR2 em vez de RAW

-- Remover funções com erro
DROP FUNCTION IF EXISTS FNC_CLIENT_FEE;
DROP FUNCTION IF EXISTS FNC_CLIENT_BALANCE;
DROP PROCEDURE IF EXISTS PRC_COMPANY_REVENUE_REPORT;

-- Function para calcular tarifa do cliente (corrigida)
CREATE OR REPLACE FUNCTION FNC_CLIENT_FEE
(
    p_client_id IN VARCHAR2
)
RETURN NUMBER
IS
    v_transaction_count NUMBER;
    v_fee NUMBER := 0;
    v_client_since DATE;
    v_period_start DATE;
BEGIN
    -- Obter data de cadastro do cliente
    SELECT created_at
    INTO v_client_since
    FROM client
    WHERE id = p_client_id;
    
    -- Calcular período de 30 dias a partir do cadastro
    v_period_start := v_client_since;
    
    -- Contar transações do cliente nos últimos 30 dias
    SELECT COUNT(*)
    INTO v_transaction_count
    FROM transaction t
    WHERE t.client_id = p_client_id
        AND t.date >= v_period_start
        AND t.date <= v_period_start + 30;

    -- Calcular tarifa baseada nas regras:
    -- Até 10 movimentações: R$ 1,00 por movimentação
    -- De 10 a 20 movimentações: R$ 0,75 por movimentação
    -- Acima de 20 movimentações: R$ 0,50 por movimentação

    IF v_transaction_count <= 10 THEN
        v_fee := v_transaction_count * 1.00;
    ELSIF v_transaction_count <= 20 THEN
        v_fee := (10 * 1.00) + ((v_transaction_count - 10) * 0.75);
    ELSE
        v_fee := (10 * 1.00) + (10 * 0.75) + ((v_transaction_count - 20) * 0.50);
    END IF;
    
    RETURN v_fee;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END FNC_CLIENT_FEE;
/

-- Function para calcular saldo do cliente (corrigida)
CREATE OR REPLACE FUNCTION FNC_CLIENT_BALANCE
(
    p_client_id IN VARCHAR2
)
RETURN NUMBER
IS
    v_balance NUMBER := 0;
BEGIN
    SELECT NVL(SUM(a.balance), 0)
    INTO v_balance
    FROM account a
    WHERE a.client_id = p_client_id;
    
    RETURN v_balance;
EXCEPTION
    WHEN OTHERS THEN
        RETURN 0;
END FNC_CLIENT_BALANCE;
/

-- Procedure para relatório de receita da empresa (corrigida)
CREATE OR REPLACE PROCEDURE PRC_COMPANY_REVENUE_REPORT
(
    p_start_date IN DATE,
    p_end_date IN DATE,
    p_cursor OUT SYS_REFCURSOR
)
IS
BEGIN
    OPEN p_cursor FOR
    SELECT 
        c.id,
        c.name,
        COUNT(t.id) as transaction_count,
        FNC_CLIENT_FEE(c.id) as fee_amount
    FROM client c
    LEFT JOIN transaction t ON c.id = t.client_id
        AND t.date BETWEEN p_start_date AND p_end_date
    GROUP BY c.id, c.name
    ORDER BY c.name;
END PRC_COMPANY_REVENUE_REPORT;
/

-- Trigger para validação de transações (corrigida)
CREATE OR REPLACE TRIGGER TRG_TRANSACTION_VALIDATION
    BEFORE INSERT OR UPDATE ON transaction
    FOR EACH ROW
DECLARE
    v_account_balance NUMBER;
BEGIN
    -- Verificar se é débito e se há saldo suficiente
    IF :NEW.amount < 0 THEN
        SELECT balance
        INTO v_account_balance
        FROM account
        WHERE id = :NEW.account_id;
        
        IF v_account_balance + :NEW.amount < 0 THEN
            RAISE_APPLICATION_ERROR(-20001, 'Saldo insuficiente para a transação');
        END IF;
        
        -- Atualizar saldo da conta
        UPDATE account
        SET balance = balance + :NEW.amount
        WHERE id = :NEW.account_id;
    ELSE
        -- Para crédito, simplesmente adicionar ao saldo
        UPDATE account
        SET balance = balance + :NEW.amount
        WHERE id = :NEW.account_id;
    END IF;
END TRG_TRANSACTION_VALIDATION;
/

COMMIT;