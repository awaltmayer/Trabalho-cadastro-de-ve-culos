package src;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CadVeiculos {

    // =============================================
    // Limpa o terminal usando sequência de escape ANSI
    // =============================================
    static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // =============================================
    // Lê um número inteiro do usuário com tratamento
    // de entrada inválida — nunca deixa o programa travar
    // =============================================
    static int lerInteiro(String mensagem) {
        while (true) {
            try {
                String entrada = IO.readln(mensagem);
                if (entrada.trim().isEmpty()) {
                    IO.println("Valor inválido! Digite um número.");
                    continue;
                }
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                IO.println("Valor inválido! Digite apenas números.");
            }
        }
    }

    // =============================================
    // Exibe o menu principal e retorna a opção escolhida
    // =============================================
    static int exibirMenu() {
        IO.println("\n========================================");
        IO.println("         Sistema CadVeículos            ");
        IO.println("========================================");
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

    // =============================================
    // Busca todos os veículos do banco e retorna como lista.
    // =============================================
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

    // =============================================
    // Verifica se o nome já existe no banco,
    // ignorando diferença entre maiúsculas e minúsculas.
    // =============================================
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

    // =============================================
    // Ordena a lista em ordem alfabética manualmente
    // usando Bubble Sort com for e if, sem métodos prontos.
    // =============================================
    static void ordenarLista(List<String> lista) {
        for (int i = 0; i < lista.size() - 1; i++) {
            for (int j = 0; j < lista.size() - 1 - i; j++) {
                if (lista.get(j).compareToIgnoreCase(lista.get(j + 1)) > 0) {
                    String aux = lista.get(j);
                    lista.set(j, lista.get(j + 1));
                    lista.set(j + 1, aux);
                }
            }
        }
    }

    // =============================================
    // Cadastra um novo veículo no banco.
    // Valida se o nome está vazio e se já existe.
    // =============================================
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

    // =============================================
    // Lista todos os veículos do banco em ordem alfabética.
    // Exibe o total e mensagem caso a lista esteja vazia.
    // =============================================
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

    // =============================================
    // Busca um veículo pelo nome no banco,
    // ignorando maiúsculas/minúsculas.
    // =============================================
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

    // =============================================
    // Edita o nome de um veículo existente pelo índice.
    // O novo nome passa pelas mesmas validações do cadastro.
    // =============================================
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

    // =============================================
    // Remove um veículo pelo índice da lista ordenada.
    // =============================================
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

    // =============================================
    // Remove um veículo pelo nome, ignorando maiúsculas/minúsculas.
    // =============================================
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

    // =============================================
    // Método principal — controla o fluxo do programa
    // com do-while e switch, conforme visto em aula.
    // =============================================
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