import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestOracleConnection {
    public static void main(String[] args) {
        String[] urls = {
            "jdbc:oracle:thin:@//localhost:1521/ORCL",
            "jdbc:oracle:thin:@//localhost:1521/ORCLPDB1", 
            "jdbc:oracle:thin:@//localhost:1521/XEPDB1",
            "jdbc:oracle:thin:@localhost:1521:ORCL"
        };
        
        String username = "system";
        String password = "system"; // Ajuste conforme necessário
        
        for (String url : urls) {
            System.out.println("Testando: " + url);
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                System.out.println("✅ SUCESSO: " + url);
                break;
            } catch (SQLException e) {
                System.out.println("❌ ERRO: " + e.getMessage());
            }
        }
    }
}