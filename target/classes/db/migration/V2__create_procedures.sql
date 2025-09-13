-- V2__create_procedures.sql
-- Criação de objetos PL/SQL

-- Function para calcular tarifa por cliente e período
CREATE OR REPLACE FUNCTION calcular_tarifa
(
    p_cliente_id NUMBER,
    p_data_inicio DATE,
    p_data_fim DATE
)
RETURN NUMBER
IS
    v_quantidade_movimentacoes NUMBER;
    v_tarifa NUMBER := 0;
BEGIN
    -- Contar movimentações do cliente no período
    SELECT COUNT(*)
    INTO v_quantidade_movimentacoes
    FROM movimentacao m
        JOIN conta c ON m.conta_id = c.id
    WHERE c.cliente_id = p_cliente_id
        AND m.data_movimentacao BETWEEN p_data_inicio AND p_data_fim;

    -- Calcular tarifa baseada na quantidade
    IF v_quantidade_movimentacoes <= 10 THEN
        v_tarifa := v_quantidade_movimentacoes * 1.00;
ELSIF v_quantidade_movimentacoes <= 20 THEN
        v_tarifa :=
(10 * 1.00) +
((v_quantidade_movimentacoes - 10) * 0.75);
    ELSE
        v_tarifa :=
(10 * 1.00) +
(10 * 0.75) +
((v_quantidade_movimentacoes - 20) * 0.50);
END
IF;
    
    RETURN v_tarifa;
EXCEPTION
    WHEN OTHERS THEN
RETURN 0;
END calcular_tarifa;
/

-- Procedure para processar movimentação
CREATE OR REPLACE PROCEDURE processar_movimentacao
(
    p_conta_id NUMBER,
    p_tipo VARCHAR2,
    p_valor NUMBER,
    p_descricao VARCHAR2
) IS
    v_saldo_atual NUMBER;
    v_saldo_anterior NUMBER;
    v_saldo_posterior NUMBER;
    v_movimentacao_id NUMBER;
BEGIN
    -- Buscar saldo atual da conta
    SELECT saldo_atual
    INTO v_saldo_anterior
    FROM conta
    WHERE id = p_conta_id;

    -- Calcular novo saldo
    IF p_tipo = 'CREDITO' THEN
        v_saldo_posterior := v_saldo_anterior + p_valor;
ELSIF p_tipo = 'DEBITO' THEN
IF v_saldo_anterior < p_valor THEN
            RAISE_APPLICATION_ERROR
(-20001, 'Saldo insuficiente para a operação');
END
IF;
        v_saldo_posterior := v_saldo_anterior - p_valor;
    ELSE
        RAISE_APPLICATION_ERROR
(-20002, 'Tipo de movimentação inválido');
END
IF;
    
    -- Inserir movimentação
    INSERT INTO movimentacao
    (
    id, tipo, valor, descricao, saldo_anterior,
    saldo_posterior, conta_id, processado
    )
VALUES
    (
        movimentacao_seq.NEXTVAL, p_tipo, p_valor, p_descricao,
        v_saldo_anterior, v_saldo_posterior, p_conta_id, 1
    );

-- Atualizar saldo da conta
UPDATE conta 
    SET saldo_atual = v_saldo_posterior,
        updated_at = CURRENT_TIMESTAMP
    WHERE id = p_conta_id;

COMMIT;
EXCEPTION
    WHEN OTHERS THEN
ROLLBACK;
RAISE;
END processar_movimentacao;
/

-- Trigger para auditoria de cliente
CREATE OR REPLACE TRIGGER trg_audit_cliente
    AFTER
UPDATE ON cliente
    FOR EACH ROW
BEGIN
    -- Atualizar data de modificação
    :NEW.data_atualizacao := CURRENT_TIMESTAMP;

-- Log de auditoria (pode ser expandido para uma tabela de auditoria)
NULL;
-- Placeholder para futuras implementações
END;
/

-- Function para obter saldo atual do cliente
CREATE OR REPLACE FUNCTION obter_saldo_cliente
(
    p_cliente_id NUMBER
)
RETURN NUMBER
IS
    v_saldo_total NUMBER := 0;
BEGIN
    -- Somar saldos de todas as contas ativas do cliente
    SELECT NVL(SUM(saldo_atual), 0)
    INTO v_saldo_total
    FROM conta
    WHERE cliente_id = p_cliente_id
        AND ativo = 1;

    RETURN v_saldo_total;
    EXCEPTION
    WHEN OTHERS THEN
    RETURN 0;
END
obter_saldo_cliente;
/

-- Procedure para relatório de receita da empresa
CREATE OR REPLACE PROCEDURE gerar_relatorio_receita
(
    p_data_inicio DATE,
    p_data_fim DATE,
    p_cursor OUT SYS_REFCURSOR
) IS
BEGIN
    OPEN p_cursor
    FOR
    SELECT
        c.id as cliente_id,
        c.nome as cliente_nome,
        COUNT(m.id) as quantidade_movimentacoes,
        calcular_tarifa(c.id, p_data_inicio, p_data_fim) as valor_tarifas
    FROM cliente c
        LEFT JOIN conta ct ON c.id = ct.cliente_id
        LEFT JOIN movimentacao m ON ct.id = m.conta_id
            AND m.data_movimentacao BETWEEN p_data_inicio AND p_data_fim
    WHERE c.ativo = 1
    GROUP BY c.id, c.nome
    ORDER BY c.nome;
END
gerar_relatorio_receita;
/

COMMIT;
