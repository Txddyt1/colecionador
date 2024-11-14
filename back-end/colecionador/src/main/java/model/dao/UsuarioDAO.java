package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import model.vo.UsuarioVO;

public class UsuarioDAO {

	// verifica se a pessoa já foi cadastrada
	public boolean verificarCadastroUsuario(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		
		// monta a query SQL
		String query = "SELECT idusuario FROM usuario WHERE idusuario = '" + usuarioVO.getIdUsuario() + "'";

		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				return true; // retorna true se o usuário já existe
			}
		} catch (SQLException erro) {
			System.out.println("\nErro ao executar a query do método verificarCadastroUsuarioBaseDados");
			System.out.println("Erro: " + erro.getMessage());
		} finally { // fecha os recursos JDBC
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return false; // retorna false se o usuário não existe
	}

	// insert usuário
	public UsuarioVO cadastrarUsuarioDAO(UsuarioVO usuarioVO) {
		
		String query = "INSERT INTO usuario (nome, email, login, senha, datacadastro, dataexpiracao) VALUES (?, ?, ?, ?, ?, ?)";
		
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		ResultSet resultado = null;

		try {
			pstmt.setString(1, usuarioVO.getNome());
			pstmt.setString(2, usuarioVO.getEmail());
			pstmt.setString(3, usuarioVO.getLogin());
			pstmt.setString(4, usuarioVO.getSenha());
            pstmt.setObject(5, usuarioVO.getDataCadastro());
			pstmt.setObject(6, usuarioVO.getDataExpiracao());
			pstmt.execute();
			resultado = pstmt.getGeneratedKeys();
			if (resultado.next()) {
				usuarioVO.setIdUsuario(resultado.getInt(1));
			}
		} catch (SQLException erro) {
			System.out.println("\nErro ao executar a query do método cadastrarUsuarioDAO");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closePreparedStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return usuarioVO;
	}
	
	// loga o usuário no sistema realizando uma consulta no banco de dados para verificar se o login e senha correspondem a um usuário registrado
	public UsuarioVO logarNoSistemaDAO(UsuarioVO usuarioVO) {
        Connection conn = Banco.getConnection();
        Statement stmt = Banco.getStatement(conn);
        
        ResultSet resultado = null;
        UsuarioVO usuario = new UsuarioVO();
        String query = "SELECT idusuario, nome, email, login, senha "
                + "FROM usuario "
                + "WHERE login = '" + usuarioVO.getLogin()
                + "' AND senha = '" + usuarioVO.getSenha() + "'";

        try{
            resultado = stmt.executeQuery(query);
            if(resultado.next()){
                usuario = new UsuarioVO();
                usuario.setIdUsuario(Integer.parseInt(resultado.getString(1)));
                usuario.setNome(resultado.getString(2));
                usuario.setEmail(resultado.getString(3));
                usuario.setLogin(resultado.getString(4));
                usuario.setSenha(resultado.getString(5));
            }
        } catch (SQLException erro){
            System.out.println("Erro ao executar a query do método logarNoSistemaDAO!");
            System.out.println("Erro: " + erro.getMessage());
        } finally {
            Banco.closeResultSet(resultado);
            Banco.closeStatement(stmt);
            Banco.closeConnection(conn);
        }
        return usuario;
    }

    // atualiza
    public boolean atualizarUsuarioDAO(UsuarioVO UsuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean sucesso = false;

		String query = "UPDATE Usuario SET idusuario = " + UsuarioVO.getIdUsuario() + ", nome = '"
				+ UsuarioVO.getNome() + "', email = '" + UsuarioVO.getEmail() + "', login = '"
				+ UsuarioVO.getLogin() + "' , senha = '" + UsuarioVO.getSenha() + "' WHERE idUsuario = " + UsuarioVO.getIdUsuario();

		try {
			int resultado = stmt.executeUpdate(query);
			sucesso = resultado > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método atualizarUsuarioDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return sucesso;
	}

    	// delete usuario
	public boolean excluirUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean sucesso = false;

		String query = "DELETE FROM usuario WHERE idusuario = " + usuarioVO.getIdUsuario();

		try {
			int resultado = stmt.executeUpdate(query);
			sucesso = resultado > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método excluirusuarioDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return sucesso;
	}


}


