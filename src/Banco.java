package src;

import java.sql.Connection;
import java.sql.Statement;

public class Banco {
    public Banco() {
    }

    public static void criarTabela() {
        String var0 = "CREATE TABLE IF NOT EXISTS veiculos (\n    id INTEGER PRIMARY KEY AUTOINCREMENT,\n    nome TEXT\n);\n";

        try {
            Connection var1 = Conexao.conectar();

            try {
                Statement var2 = var1.createStatement();

                try {
                    var2.execute(var0);
                    System.out.println("Tabela pronta!");
                } catch (Throwable var7) {
                    if (var2 != null) {
                        try {
                            var2.close();
                        } catch (Throwable var6) {
                            var7.addSuppressed(var6);
                        }
                    }

                    throw var7;
                }

                if (var2 != null) {
                    var2.close();
                }
            } catch (Throwable var8) {
                if (var1 != null) {
                    try {
                        var1.close();
                    } catch (Throwable var5) {
                        var8.addSuppressed(var5);
                    }
                }

                throw var8;
            }

            if (var1 != null) {
                var1.close();
            }
        } catch (Exception var9) {
            var9.printStackTrace();
        }

    }
}