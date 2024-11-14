package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.vo.MoedaVO;
import model.vo.UsuarioVO;

public class MoedaDAO {

    // Método para cadastrar uma nova moeda na coleção
    public boolean cadastrarMoeda(MoedaVO moeda) {
        String sql = "INSERT INTO moeda (idUsuario, nome, pais, ano, valor, detalhes, dataCadastro, imagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);

        try {
            stmt.setInt(1, moeda.getUsuario().getIdUsuario());
            stmt.setString(2, moeda.getNome());
            stmt.setString(3, moeda.getPais());
            stmt.setInt(4, moeda.getAno());
            stmt.setBigDecimal(5, moeda.getValor());
            stmt.setString(6, moeda.getDetalhes());
            stmt.setObject(7, moeda.getDataCadastro());
            stmt.setBytes(8, moeda.getImagem());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar moeda: " + e.getMessage());
            return false;
        } finally {
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }


    // Método para verificar se uma moeda com um nome específico já existe para um usuário
    public boolean consultarMoedaPorNome(String nome, int idUsuario) {
        String sql = "SELECT idMoeda FROM moeda WHERE nome = ? AND idUsuario = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);
        ResultSet rs = null;

        try {
            stmt.setString(1, nome);
            stmt.setInt(2, idUsuario);
            rs = stmt.executeQuery();
            return rs.next(); // Retorna true se encontrar algum registro
        } catch (SQLException e) {
            System.out.println("Erro ao consultar moeda por nome: " + e.getMessage());
            return false;
        } finally {
            Banco.closeResultSet(rs);
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }

    // Método para listar todas as moedas de um usuário específico
    public List<MoedaVO> listarMoedas(int idUsuario) {
        String sql = "SELECT * FROM moeda WHERE idUsuario = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);
        ResultSet rs = null;
        List<MoedaVO> moedas = new ArrayList<>();

        try {
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();

            while (rs.next()) {
                MoedaVO moeda = new MoedaVO();
                moeda.setIdMoeda(rs.getInt("idMoeda"));
                moeda.setNome(rs.getString("nome"));
                moeda.setPais(rs.getString("pais"));
                moeda.setAno(rs.getInt("ano"));
                moeda.setValor(rs.getBigDecimal("valor"));
                moeda.setDetalhes(rs.getString("detalhes"));
                moeda.setDataCadastro(rs.getObject("dataCadastro", java.time.LocalDate.class));
                moeda.setImagem(rs.getBytes("imagem"));
                
                // Associar o usuário à moeda (opcional, se necessário)
                UsuarioVO usuario = new UsuarioVO();
                usuario.setIdUsuario(idUsuario);
                moeda.setUsuario(usuario);

                moedas.add(moeda);
            }
        } catch (SQLException e) {
            System.out.println("Erro ao listar moedas: " + e.getMessage());
        } finally {
            Banco.closeResultSet(rs);
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }

        return moedas;
    }

    // Método para atualizar uma moeda existente
    public boolean atualizarMoeda(MoedaVO moeda) {
        String sql = "UPDATE moeda SET nome = ?, pais = ?, ano = ?, valor = ?, detalhes = ?, imagem = ? WHERE idMoeda = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);

        try {
            stmt.setString(1, moeda.getNome());
            stmt.setString(2, moeda.getPais());
            stmt.setInt(3, moeda.getAno());
            stmt.setBigDecimal(4, moeda.getValor());
            stmt.setString(5, moeda.getDetalhes());
            stmt.setBytes(6, moeda.getImagem());
            stmt.setInt(7, moeda.getIdMoeda());

            return stmt.executeUpdate() > 0; // Retorna true se a atualização foi bem-sucedida
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar moeda: " + e.getMessage());
            return false;
        } finally {
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }

    // Método para excluir uma moeda pelo ID
    public boolean excluirMoeda(int idMoeda) {
        String sql = "DELETE FROM moeda WHERE idMoeda = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);

        try {
            stmt.setInt(1, idMoeda);
            return stmt.executeUpdate() > 0; // Retorna true se a exclusão foi bem-sucedida
        } catch (SQLException e) {
            System.out.println("Erro ao excluir moeda: " + e.getMessage());
            return false;
        } finally {
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }


    // Outros métodos podem seguir o mesmo padrão, utilizando Banco para conexão e fechamento.
}