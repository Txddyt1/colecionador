package model.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.vo.MoedaVO;
import model.vo.UsuarioVO;

public class MoedaDAO {

    // Método para cadastrar uma nova moeda na coleção
    public MoedaVO cadastrarMoedaDAO(MoedaVO moedaVO) {
        String sql = "INSERT INTO moeda (idMoeda, nome, pais, ano, valor, detalhes, dataCadastro, imagem) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);
        ResultSet resultado = null;

        try {
            stmt.setInt(1, moedaVO.getIdMoeda());
            stmt.setString(2, moedaVO.getNome());
            stmt.setString(3, moedaVO.getPais());
            stmt.setInt(4, moedaVO.getAno());
            stmt.setBigDecimal(5, moedaVO.getValor());
            stmt.setString(6, moedaVO.getDetalhes());
            stmt.setDate(7, Date.valueOf(java.time.LocalDate.now()));
            stmt.setBytes(8, moedaVO.getImagem());
            stmt.execute();
			resultado = stmt.getGeneratedKeys();
			if (resultado.next()) {
				moedaVO.setIdMoeda(resultado.getInt(1));
			}
		} catch (SQLException erro) {
			System.out.println("\nErro ao executar a query do método cadastrarMoedaDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(stmt);
			Banco.closeConnection(conn);
		}
		return moedaVO;
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
    public boolean atualizarMoedaDAO(MoedaVO moeda) {
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

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar moeda: " + e.getMessage());
            return false;
        } finally {
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }

    // Método para excluir uma moeda pelo ID
    public boolean excluirMoedaDAO(MoedaVO moedaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;

		String query = "DELETE FROM usuario WHERE idusuario = " + moedaVO.getIdMoeda();
		
		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método excluirusuarioDAO!");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
    }


    public boolean verificarCadastroMoedaDAO(MoedaVO moedaVO) {
		Connection conn = Banco.getConnection();
		Statement pstmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		
		String query = "SELECT idMoeda FROM moeda WHERE idMoeda = " + moedaVO.getIdMoeda();

		try {
			resultado = pstmt.executeQuery(query);
			if (resultado.next()) {
				return true; // retorna true se o usuário já existe
			}
		} catch (SQLException erro) {
			System.out.println("\nErro ao executar a query do método verificarCadastroUsuarioBaseDados");
			System.out.println("Erro: " + erro.getMessage());
		} finally { 
			Banco.closeResultSet(resultado);
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return false;
    }
}