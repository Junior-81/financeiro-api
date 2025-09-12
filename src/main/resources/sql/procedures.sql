CREATE OR REPLACE PROCEDURE cadastrar_cliente (
    p_nome IN VARCHAR2,
    p_cpf IN VARCHAR2,
    p_cnpj IN VARCHAR2,
    p_endereco IN VARCHAR2,
    p_tipo_pessoa IN VARCHAR2,
    p_telefone IN VARCHAR2,
    p_saldo_inicial IN NUMBER
) AS
BEGIN
    INSERT INTO clientes (nome, cpf, cnpj, endereco, tipo_pessoa, telefone, saldo)
    VALUES (p_nome, p_cpf, p_cnpj, p_endereco, p_tipo_pessoa, p_telefone, p_saldo_inicial);
    COMMIT;
END cadastrar_cliente;

CREATE OR REPLACE PROCEDURE registrar_movimentacao (
    p_cliente_id IN NUMBER,
    p_tipo IN VARCHAR2,
    p_valor IN NUMBER,
    p_data IN DATE,
    p_descricao IN VARCHAR2
) AS
BEGIN
    INSERT INTO movimentacoes (cliente_id, tipo, valor, data, descricao)
    VALUES (p_cliente_id, p_tipo, p_valor, p_data, p_descricao);
    COMMIT;
END registrar_movimentacao;

CREATE OR REPLACE PROCEDURE calcular_receita_empresa (
    p_periodo_inicio IN DATE,
    p_periodo_fim IN DATE,
    p_total_receita OUT NUMBER
) AS
BEGIN
    SELECT SUM(CASE 
                WHEN tipo = 'CREDITO' THEN valor 
                ELSE 0 
               END) INTO p_total_receita
    FROM movimentacoes
    WHERE data BETWEEN p_periodo_inicio AND p_periodo_fim;
END calcular_receita_empresa;