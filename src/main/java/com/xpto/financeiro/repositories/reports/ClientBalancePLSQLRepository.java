package com.xpto.financeiro.repositories.reports;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

@Repository
public class ClientBalancePLSQLRepository {

    @Autowired
    private DataSource dataSource;

    /**
     * Chama a função PL/SQL FNC_CLIENT_FEE para calcular a tarifa do cliente
     * 
     * @param clientId ID do cliente
     * @return Valor da tarifa calculada via PL/SQL
     */
    public Double getClientFee(UUID clientId) {
        try (Connection conn = dataSource.getConnection();
                CallableStatement stmt = conn.prepareCall("{ ? = call FNC_CLIENT_FEE(?) }")) {

            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.setString(2, clientId.toString());
            stmt.execute();

            double result = stmt.getDouble(1);
            return stmt.wasNull() ? null : result;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar função PL/SQL FNC_CLIENT_FEE", e);
        }
    }

    /**
     * Chama a função PL/SQL FNC_CLIENT_BALANCE para obter o saldo do cliente
     * 
     * @param clientId ID do cliente
     * @return Saldo do cliente calculado via PL/SQL
     */
    public Double getClientBalance(UUID clientId) {
        try (Connection conn = dataSource.getConnection();
                CallableStatement stmt = conn.prepareCall("{ ? = call FNC_CLIENT_BALANCE(?) }")) {

            stmt.registerOutParameter(1, Types.NUMERIC);
            stmt.setString(2, clientId.toString());
            stmt.execute();

            double result = stmt.getDouble(1);
            return stmt.wasNull() ? null : result;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao executar função PL/SQL FNC_CLIENT_BALANCE", e);
        }
    }
}