package com.xpto.financeiro;

// Arquivo removido: utilitário de teste de conexão, não faz parte da API principal
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnectionTest {

    public static void main(String[] args) {
        // URLs para testar
        String[] testUrls = {
                "jdbc:oracle:thin:@//localhost:1521/ORCL",
                "jdbc:oracle:thin:@//localhost:1521/ORCLPDB1",
                "jdbc:oracle:thin:@//localhost:1521/XEPDB1",
                "jdbc:oracle:thin:@//localhost:1521/XE",
                "jdbc:oracle:thin:@//localhost:1521/ORCLCDB",
                "jdbc:oracle:thin:@localhost:1521:ORCL",
                "jdbc:oracle:thin:@//localhost:1521/FREE",
                "jdbc:oracle:thin:@//localhost:1521/FREEPDB1"
        };

        String username = "system";
        String password = "system"; // Troque pela senha correta

        System.out.println("🔍 Testando conexões Oracle...\n");

        for (String url : testUrls) {
            System.out.print("Testando: " + url + " ... ");
            try {
                Class.forName("oracle.jdbc.OracleDriver");
                Connection conn = DriverManager.getConnection(url, username, password);

                // Testar uma query simples
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(
                        "SELECT 'Connected to ' || SYS_CONTEXT('USERENV', 'DB_NAME') || ' as ' || USER FROM DUAL");

                if (rs.next()) {
                    System.out.println("✅ SUCESSO: " + rs.getString(1));
                }

                rs.close();
                stmt.close();
                conn.close();
                break; // Para no primeiro sucesso

            } catch (Exception e) {
                System.out.println("❌ ERRO: " + e.getMessage().substring(0, Math.min(80, e.getMessage().length())));
            }
        }

        System.out.println("\n🏁 Teste finalizado.");
    }
}