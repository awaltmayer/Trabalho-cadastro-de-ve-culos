package src;

import java.sql.Connection;
import java.sql.Statement;

public class Banco {
    public Banco() {
    }

    public static void criarTabela() {
        String sql = "CREATE TABLE IF NOT EXISTS veiculos (\n" + //
                        "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" + //
                        "    nome TEXT\n" + //
                        ");";
        try {
            Connection conn = Conexao.conectar();
            Statement stmt = conn.createStatement();
            stmt.execute(sql);
            stmt.close();
            conn.close();
            System.out.println("Tabela pronta!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}