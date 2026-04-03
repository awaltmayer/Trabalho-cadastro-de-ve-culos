package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CadVeiculos {
    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    static int lerInteiro(String mensagem) {
    return Input.scanInt(mensagem);
    }

    static int exibirMenu() {
        IO.println("\n========================================");
        IO.println("         Sistema CadVeículos              ");
        IO.println("==========================================");
        IO.println("1 - Cadastrar veículo");
        IO.println("2 - Listar veículos");
        IO.println("3 - Buscar veículo por nome");
        IO.println("4 - Editar veículo");
        IO.println("5 - Remover veículo por índice");
        IO.println("6 - Remover veículo por nome");
        IO.println("0 - Sair");
        IO.println("========================================");
        return lerInteiro("Escolha uma opção: ");
    }

    static List<String> buscarTodos() {
        List<String> lista = new ArrayList<>();
        String sql = "SELECT nome FROM veiculos";

        try {
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                lista.add(rs.getString("nome"));
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    static boolean isDuplicado(String nome) {
        String sql = "SELECT COUNT(*) FROM veiculos WHERE LOWER(nome) = LOWER(?)";

        try {
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();

            int count = rs.getInt(1);

            rs.close();
            stmt.close();
            conn.close();

            return count > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    static void ordenarLista(List<String> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - 1 - i; j++) {
                    if (lista.get(j).toLowerCase().compareTo(lista.get(j + 1).toLowerCase()) > 0);
                    String aux = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, aux);
                }
            }
        }

    static void cadastrarVeiculo() {
        String nome = IO.readln("Informe o nome do veículo: ");

        if (nome.trim().isEmpty()) {
            IO.println("Nome inválido! O nome não pode ser vazio.");
            return;
        }

        if (isDuplicado(nome)) {
            IO.println("Veículo já cadastrado! Não é permitido duplicatas.");
            return;
        }

        String sql = "INSERT INTO veiculos (nome) VALUES (?)";

        try {
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.executeUpdate();

            stmt.close();
            conn.close();

            IO.println("Veículo '" + nome + "' cadastrado com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void listarVeiculos() {
        List<String> lista = buscarTodos();

        if (lista.isEmpty()) {
            IO.println("A lista está vazia! Nenhum veículo cadastrado.");
            return;
        }

        ordenarLista(lista);

        IO.println("\n========================================");
        IO.println("Total de veículos cadastrados: " + lista.size());
        IO.println("========================================");
        for (int i = 0; i < lista.size(); i++) {
            IO.println((i + 1) + " - " + lista.get(i));
        }
        IO.println("========================================");
    }

    static void buscarVeiculo() {
        List<String> lista = buscarTodos();

        if (lista.isEmpty()) {
            IO.println("A lista está vazia! Nenhum veículo cadastrado.");
            return;
        }

        String nome = IO.readln("Informe o nome do veículo a buscar: ");

        ordenarLista(lista);

        boolean encontrado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equalsIgnoreCase(nome)) {
                IO.println("Veículo encontrado: " + lista.get(i));
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            IO.println("Veículo '" + nome + "' não encontrado na lista.");
        }

        IO.println("Total de veículos cadastrados: " + lista.size());
    }

    static void editarVeiculo() {
        List<String> lista = buscarTodos();

        if (lista.isEmpty()) {
            IO.println("A lista está vazia! Nenhum veículo para editar.");
            return;
        }

        ordenarLista(lista);

        IO.println("\n========================================");
        IO.println("Total de veículos cadastrados: " + lista.size());
        IO.println("========================================");
        for (int i = 0; i < lista.size(); i++) {
            IO.println((i + 1) + " - " + lista.get(i));
        }
        IO.println("========================================");

        int indice = lerInteiro("Informe o número do veículo que deseja editar: ");

        if (indice < 1 || indice > lista.size()) {
            IO.println("Número inválido! Informe um número entre 1 e " + lista.size() + ".");
            return;
        }

        String novoNome = IO.readln("Informe o novo nome do veículo: ");

        if (novoNome.trim().isEmpty()) {
            IO.println("Nome inválido! O nome não pode ser vazio.");
            return;
        }

        if (isDuplicado(novoNome)) {
            IO.println("Já existe um veículo com esse nome! Não é permitido duplicatas.");
            return;
        }

        String nomeAntigo = lista.get(indice - 1);
        String sql = "UPDATE veiculos SET nome = ? WHERE LOWER(nome) = LOWER(?)";

        try {
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, novoNome);
            stmt.setString(2, nomeAntigo);
            stmt.executeUpdate();

            stmt.close();
            conn.close();

            IO.println("Veículo '" + nomeAntigo + "' alterado para '" + novoNome + "' com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void removerPorIndice() {
        List<String> lista = buscarTodos();

        if (lista.isEmpty()) {
            IO.println("A lista está vazia! Nenhum veículo para remover.");
            return;
        }

        ordenarLista(lista);

        IO.println("\n========================================");
        IO.println("Total de veículos cadastrados: " + lista.size());
        IO.println("========================================");
        for (int i = 0; i < lista.size(); i++) {
            IO.println((i + 1) + " - " + lista.get(i));
        }
        IO.println("========================================");

        int indice = lerInteiro("Informe o número do veículo que deseja remover: ");

        if (indice < 1 || indice > lista.size()) {
            IO.println("Número inválido! Informe um número entre 1 e " + lista.size() + ".");
            return;
        }

        String nome = lista.get(indice - 1);
        String sql = "DELETE FROM veiculos WHERE LOWER(nome) = LOWER(?)";

        try {
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.executeUpdate();

            stmt.close();
            conn.close();

            IO.println("Veículo '" + nome + "' removido com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void removerPorNome() {
        List<String> lista = buscarTodos();

        if (lista.isEmpty()) {
            IO.println("A lista está vazia! Nenhum veículo para remover.");
            return;
        }

        String nome = IO.readln("Informe o nome do veículo que deseja remover: ");

        boolean encontrado = false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).equalsIgnoreCase(nome)) {
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            IO.println("Veículo '" + nome + "' não encontrado na lista.");
            return;
        }

        String sql = "DELETE FROM veiculos WHERE LOWER(nome) = LOWER(?)";

        try {
            Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nome);
            stmt.executeUpdate();

            stmt.close();
            conn.close();

            IO.println("Veículo '" + nome + "' removido com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        Banco.criarTabela();

        int opcao;

        do {
            limparTela();
            opcao = exibirMenu();
            limparTela();

            switch (opcao) {
                case 1:
                    cadastrarVeiculo();
                    break;
                case 2:
                    listarVeiculos();
                    break;
                case 3:
                    buscarVeiculo();
                    break;
                case 4:
                    editarVeiculo();
                    break;
                case 5:
                    removerPorIndice();
                    break;
                case 6:
                    removerPorNome();
                    break;
                case 0:
                    limparTela();
                    IO.println("Encerrando o sistema. Até logo!");
                    break;
                default:
                    IO.println("Opção inválida! Escolha uma opção do menu.");
                    break;
            }

            if (opcao != 0) {
                IO.readln("\nPressione ENTER para continuar...");
            }

        } while (opcao != 0);
    }
}