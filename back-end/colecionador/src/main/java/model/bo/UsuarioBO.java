package model.bo;

import model.dao.UsuarioDAO;
import model.vo.UsuarioVO;

public class UsuarioBO {

    // Método para cadastrar usuário com verificação de duplicidade
    public UsuarioVO cadastrarUsuario(UsuarioVO usuarioVO) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        // Verifica se o objeto usuario ou o login estão nulos
        if (usuarioDAO.verificarCadastroUsuario(usuarioVO)) {
			System.out.println("\nPessoa já cadastrada na base.");
		} else {
			// tenta cadastrar o usuário e atualiza o objeto com o ID gerado
			usuarioVO = usuarioDAO.cadastrarUsuarioDAO(usuarioVO);
		}
		return usuarioVO;
	}

    public UsuarioVO logarUsuario(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		return usuarioDAO.logarNoSistemaDAO(usuarioVO);
	}

    // Método para editar um usuário existente com verificação
	public boolean atualizarUsuarioBO(UsuarioVO usuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		// tenta atualizar a usuario e informa o resultado
		resultado = usuarioDAO.atualizarUsuarioDAO(usuarioVO);
		return resultado;
	}

    // Método para exclusão lógica de usuário
	public boolean excluirUsuarioBO(UsuarioVO UsuarioVO) {
		boolean resultado = false;
		UsuarioDAO UsuarioDAO = new UsuarioDAO();

		// tenta excluir a Usuario e informa o resultado
		if (UsuarioDAO.excluirUsuarioDAO(UsuarioVO)) {
			System.out.println("Usuario excluída.");
			resultado = true;
		} else {
			System.out.println("Falha na exclusão da Usuario.");
		}
		return resultado;
	}
}