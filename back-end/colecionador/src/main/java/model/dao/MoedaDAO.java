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

	public MoedaVO cadastrarMoedaDAO(MoedaVO moedaVO) {
	    String sql = "INSERT INTO moeda (nome, pais, ano, valor, detalhes, dataCadastro, imagem, idusuario) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	    Connection conn = Banco.getConnection();
	    PreparedStatement stmt = Banco.getPreparedStatementWithPk(conn, sql); // Utilizando o método correto
	    ResultSet resultado = null;

	    try {
	        stmt.setString(1, moedaVO.getNome());
	        stmt.setString(2, moedaVO.getPais());
	        stmt.setInt(3, moedaVO.getAno());
	        stmt.setDouble(4, moedaVO.getValor());
	        stmt.setString(5, moedaVO.getDetalhes());
	        stmt.setDate(6, Date.valueOf(java.time.LocalDate.now()));
	        stmt.setBytes(7, moedaVO.getImagem());
	        stmt.setInt(8, moedaVO.getIdUsuario());

	        // Executar a query
	        int rowsAffected = stmt.executeUpdate();
	        System.out.println("Linhas afetadas: " + rowsAffected);

	        // Recuperar a chave gerada
	        resultado = stmt.getGeneratedKeys();
	        if (resultado.next()) {
	            int idGerado = resultado.getInt(1);
	            moedaVO.setIdMoeda(idGerado);
	            System.out.println("ID gerado: " + idGerado);
	        } else {
	            System.out.println("Nenhum ID foi gerado pelo banco de dados.");
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
                moeda.setValor(rs.getDouble("valor"));
                moeda.setDetalhes(rs.getString("detalhes"));
                moeda.setDataCadastro(rs.getObject("dataCadastro", java.time.LocalDate.class));
                moeda.setImagem(rs.getBytes("imagem"));
                
                // Associar o usuário à moeda (opcional, se necessário)
                UsuarioVO usuario = new UsuarioVO();
                usuario.setIdUsuario(idUsuario);
                moeda.setIdUsuario(idUsuario);

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

    public boolean atualizarMoedaDAO(MoedaVO moedaVO) {
        boolean retorno = false;
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = null;
        String query = "";
        if (moedaVO.getImagem() != null && moedaVO.getImagem().length > 0) {
            query = "UPDATE moeda SET nome = ?, pais = ?, ano = ?, valor = ?, detalhes = ?, imagem = ? WHERE idMoeda = ?";
            stmt = Banco.getPreparedStatement(conn, query);
        } else {
            query = "UPDATE moeda SET nome = ?, pais = ?, ano = ?, valor = ?, detalhes = ? WHERE idMoeda = ?";
            stmt = Banco.getPreparedStatement(conn, query);
        }

        try {
            stmt.setString(1, moedaVO.getNome());
            stmt.setString(2, moedaVO.getPais());
            stmt.setInt(3, moedaVO.getAno());
            stmt.setDouble(4, moedaVO.getValor());
            stmt.setString(5, moedaVO.getDetalhes());
            if (moedaVO.getImagem() != null && moedaVO.getImagem().length > 0) {
                stmt.setBytes(6, moedaVO.getImagem());
                stmt.setInt(7, moedaVO.getIdMoeda());
            } else {
                stmt.setInt(6, moedaVO.getIdMoeda());
            }

            if (stmt.executeUpdate() == 1) {
                retorno = true;
            }
        } catch (SQLException e) {
            System.out.println("Erro ao atualizar moeda: " + e.getMessage());
        } finally {
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
        return retorno;
    }

    // Método para excluir uma moeda pelo ID
    public boolean excluirMoedaDAO(MoedaVO moedaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;

		String query = "DELETE FROM moeda WHERE idMoeda = " + moedaVO.getIdMoeda();
		
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
		Connection conn = Banco.getConnection();//
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


    public ArrayList<MoedaVO> listarTodasMoedasDAO(int idUsuario) {
        Connection conn = Banco.getConnection();
        Statement stmt = Banco.getStatement(conn);

        ResultSet resultado = null;
        ArrayList<MoedaVO> listaMoedas = new ArrayList<>();
        String query = "SELECT idMoeda, nome, pais, ano, valor, detalhes, imagem FROM moeda WHERE idusuario = " + idUsuario;

        try{
            resultado = stmt.executeQuery(query);
            while (resultado.next()) {
                MoedaVO moeda = new MoedaVO();
                moeda.setIdMoeda(Integer.parseInt(resultado.getString(1)));
                moeda.setNome(resultado.getString(2));
                moeda.setPais(resultado.getString(3));
                moeda.setAno(resultado.getInt(4));
                moeda.setValor(resultado.getDouble(5));
                moeda.setDetalhes(resultado.getString(6));
                moeda.setImagem(resultado.getBytes(7));
                listaMoedas.add(moeda);
            }
        } catch (SQLException erro) {
            System.out.println("Erro ao executar a query do método listarTodasMoedasDAO");
            System.out.println("Erro:" + erro.getMessage());
        } finally {
            Banco.closeResultSet(resultado);
            Banco.closeStatement(stmt);
            Banco.closeConnection(conn);
        }
      return listaMoedas;
    }
}