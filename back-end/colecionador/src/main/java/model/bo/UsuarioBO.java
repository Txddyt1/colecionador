package model.bo;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;

import model.dao.UsuarioDAO;
import model.vo.UsuarioVO;

public class UsuarioBO {

	// Método para ler Imagens e JSON
	private byte[] converterByteParaArray (InputStream inputStream) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int read = 0;
		byte[] dados = new byte[1024];
		while ((read = inputStream.read(dados, 0, dados.length)) != -1) {
			buffer.write(dados, 0, read);
		}
		buffer.flush();
		return buffer.toByteArray();
	}

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
    public UsuarioVO logarUsuario(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();

		return usuarioDAO.logarNoSistemaDAO(usuarioVO);
	}

    // Método para atualizar um usuário existente com verificação
	public boolean atualizarUsuarioBO(InputStream usuarioInputStream) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		UsuarioVO usuarioVO = null;
		try {
			String usuarioJSON = new String(this.converterByteParaArray(usuarioInputStream), StandardCharsets.UTF_8);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.findAndRegisterModules();
			usuarioVO = objectMapper.readValue(usuarioJSON, UsuarioVO.class);

			if (usuarioDAO.verificarCadastroUsuarioDAO(usuarioVO)) {
				resultado = usuarioDAO.atualizarUsuarioDAO(usuarioVO);
			} else {
				System.out.println("Pessoa não consta na base de dados");
			} 

		} catch (FileNotFoundException erro) {
			System.out.println(erro);
		} catch (IOException e) {
			e.printStackTrace();
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