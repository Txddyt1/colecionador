package model.bo;

import model.dao.UsuarioDAO;
import model.vo.UsuarioVO;

public class UsuarioBO {

    // Método para cadastrar usuário com verificação de duplicidade
    public UsuarioVO cadastrarUsuarioBO(UsuarioVO usuarioVO) {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
		if (usuarioDAO.verificarCadastroUsuarioDAO(usuarioVO)) {
					System.out.println("\nPessoa já cadastrada no banco de dados.");
		} else {
		usuarioVO = usuarioDAO.cadastrarUsuarioDAO(usuarioVO);
		}
		return usuarioVO;
	}

	// Método para efetuar o login do usuário no sistema
    public UsuarioVO logarUsuarioBO(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		return usuarioDAO.logarNoSistemaDAO(usuarioVO);
	}

    // Método para atualizar um usuário existente com verificação
	public boolean atualizarUsuarioBO(UsuarioVO usuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
	
			if (usuarioDAO.verificarCadastroUsuarioDAO(usuarioVO)) {
				resultado = usuarioDAO.atualizarUsuarioDAO(usuarioVO);
			} else {
				System.out.println("Pessoa não consta na base de dados");
			} 
		return resultado;
	}

    // Método para exclusão de usuário
	public boolean excluirUsuarioBO(UsuarioVO UsuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		// tenta excluir a Usuario e informa o resultado
		if (usuarioDAO.verificarCadastroUsuarioDAO(UsuarioVO)) {
			resultado = usuarioDAO.excluirUsuarioDAO(UsuarioVO);
			System.out.println("Usuario excluída.");
		} else {
			System.out.println("Falha na exclusão da Usuario.");
		}
		return resultado;
	}
}