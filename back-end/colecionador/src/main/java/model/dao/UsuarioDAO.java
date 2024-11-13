package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import model.vo.UsuarioVO;

public class UsuarioDAO {

    // Método para cadastrar um novo usuário
    public boolean cadastrarUsuario(UsuarioVO usuario) {
        String sql = "INSERT INTO usuario (nome, email, login, senha, dataCadastro) VALUES (?, ?, ?, ?, ?)";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);

        try {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getSenha());
            stmt.setObject(5, usuario.dataCadastro());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao cadastrar usuário: " + e.getMessage());
            return false;
        } finally {
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }

    // Método para verificar se um usuário já existe pelo login
    public boolean consultarUsuarioPorLogin(String login) {
        String sql = "SELECT idUsuario FROM usuario WHERE login = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);
        ResultSet rs = null;

        try {
            stmt.setString(1, login);
            rs = stmt.executeQuery();
            return rs.next(); // Retorna true se encontrar algum registro
        } catch (SQLException e) {
            System.out.println("Erro ao consultar usuário: " + e.getMessage());
            return false;
        } finally {
            Banco.closeResultSet(rs);
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }
    
    // Método para consultar um usuário pelo login e senha
    public UsuarioVO consultarUsuarioPorLoginESenha(String login, String senha) {
        String sql = "SELECT * FROM usuario WHERE login = ? AND senha = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);
        ResultSet rs = null;
        UsuarioVO usuario = null;

        try {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new UsuarioVO();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setDataCadastro(rs.getObject("dataCadastro", java.time.LocalDate.class));
                usuario.setDataExpiracao(rs.getObject("dataExpiracao", java.time.LocalDate.class));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar usuário por login e senha: " + e.getMessage());
        } finally {
            Banco.closeResultSet(rs);
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }

        return usuario;
    }

    // Método para consultar um usuário pelo ID
    public UsuarioVO consultarUsuarioPorId(int idUsuario) {
        String sql = "SELECT * FROM usuario WHERE idUsuario = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);
        ResultSet rs = null;
        UsuarioVO usuario = null;

        try {
            stmt.setInt(1, idUsuario);
            rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new UsuarioVO();
                usuario.setIdUsuario(rs.getInt("idUsuario"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setDataCadastro(rs.getObject("dataCadastro", java.time.LocalDate.class));
                usuario.setDataExpiracao(rs.getObject("dataExpiracao", java.time.LocalDate.class));
            }
        } catch (SQLException e) {
            System.out.println("Erro ao consultar usuário por ID: " + e.getMessage());
        } finally {
            Banco.closeResultSet(rs);
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }

        return usuario;
    }

    // Método para editar informações do usuário
    public boolean editarUsuario(UsuarioVO usuario) {
        String sql = "UPDATE usuario SET nome = ?, email = ?, login = ?, senha = ? WHERE idUsuario = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);

        try {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getSenha());
            stmt.setInt(5, usuario.getIdUsuario());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao editar usuário: " + e.getMessage());
            return false;
        } finally {
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }

    // Método para exclusão lógica do usuário
    public boolean excluirUsuario(int idUsuario) {
        String sql = "UPDATE usuario SET dataExpiracao = ? WHERE idUsuario = ?";
        Connection conn = Banco.getConnection();
        PreparedStatement stmt = Banco.getPreparedStatement(conn, sql);

        try {
            stmt.setObject(1, LocalDate.now());
            stmt.setInt(2, idUsuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Erro ao excluir usuário: " + e.getMessage());
            return false;
        } finally {
            Banco.closePreparedStatement(stmt);
            Banco.closeConnection(conn);
        }
    }
}