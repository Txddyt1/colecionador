package model.bo;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

import model.dao.UsuarioDAO;
import model.vo.UsuarioVO;

public class UsuarioBO {

    private UsuarioDAO usuarioDAO;

    // Construtor para inicializar UsuarioDAO
    public UsuarioBO() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // Método para cadastrar usuário com verificação de duplicidade
    public boolean cadastrarUsuario(UsuarioVO usuario) {
        // Verifica se o objeto usuario ou o login estão nulos
        if (usuario == null || usuario.getLogin() == null || usuario.getLogin().isEmpty()) {
            System.out.println("Dados inválidos para cadastro de usuário.");
            return false;
        }

        if (usuarioDAO.consultarUsuarioPorLogin(usuario.getLogin())) {
            System.out.println("Usuário já está cadastrado com este login.");
            return false; // Retorna falso se já existe
        }
        return usuarioDAO.cadastrarUsuario(usuario);
    }

    // Método para cadastrar um usuário a partir de InputStreams de dados do usuário e arquivo
    public UsuarioVO cadastrarUsuarioBO(InputStream usuarioInputStream, InputStream fileInputStream, FormDataContentDisposition fileMetaData) {

        if (usuario == null) {
            System.out.println("Erro ao converter dados do usuário.");
            return null;
        }

        // Verificar se o usuário já existe no sistema
        if (usuarioDAO.consultarUsuarioPorLogin(usuario.getLogin())) {
            System.out.println("Usuário já está cadastrado com este login.");
            return null; // Retorna nulo se o usuário já existe
        }

        // Cadastrar o usuário no banco de dados
        boolean cadastroRealizado = usuarioDAO.cadastrarUsuario(usuario);
        if (cadastroRealizado) {
            return usuario; // Retorna o usuário cadastrado
        } else {
            System.out.println("Erro ao cadastrar o usuário.");
            return null;
        }
    }
    // Método para editar um usuário existente com verificação
    public boolean editarUsuario(UsuarioVO usuario) {
        if (usuario == null || usuario.getLogin() == null || usuario.getLogin().isEmpty()) {
            System.out.println("Dados inválidos para edição de usuário.");
            return false;
        }

        if (!usuarioDAO.consultarUsuarioPorLogin(usuario.getLogin())) {
            System.out.println("Usuário não encontrado para edição.");
            return false; // Retorna falso se o usuário não existe
        }
        return usuarioDAO.editarUsuario(usuario);
    }

    // Método para exclusão lógica de usuário
    public boolean excluirUsuario(int idUsuario) {
        if (idUsuario <= 0) {
            System.out.println("ID de usuário inválido.");
            return false;
        }

        UsuarioVO usuario = usuarioDAO.consultarUsuarioPorId(idUsuario);
        if (usuario == null) {
            System.out.println("Usuário não encontrado para exclusão.");
            return false;
        }
        return usuarioDAO.excluirUsuario(idUsuario);
    }

    // Método para login (apenas consulta de login e senha)
    public boolean login(String login, String senha) {
        if (login == null || login.isEmpty() || senha == null || senha.isEmpty()) {
            System.out.println("Login e/ou senha não podem ser vazios.");
            return false;
        }

        UsuarioVO usuario = usuarioDAO.consultarUsuarioPorLoginESenha(login, senha);
        if (usuario == null) {
            System.out.println("Login ou senha incorretos.");
            return false;
        }
        return true;
    }
}