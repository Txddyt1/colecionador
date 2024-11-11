package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import model.vo.UsuarioVO;
import util.DatabaseConnection;

public class UsuarioDAO {

    public boolean inserirUsuario(UsuarioVO usuario) {
        String sql = "INSERT INTO usuario (nome, email, login, senha, data_cadastro_usuario, data_expiracao) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getLogin());
            stmt.setString(4, usuario.getSenha());
            stmt.setObject(5, usuario.getDataCadastroUsuario());
            stmt.setObject(6, usuario.getDataExpiracao());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            System.out.println("Erro ao inserir usuário: " + e.getMessage());
            return false;
        }
    }
}
