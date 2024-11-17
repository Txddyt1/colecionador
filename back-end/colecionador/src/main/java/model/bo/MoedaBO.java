package model.bo;

import model.dao.MoedaDAO;
import model.vo.MoedaVO;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MoedaBO {
    
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
    
     public MoedaVO cadastrarMoedaBO(MoedaVO moedaVO) {
        MoedaDAO moedaDAO = new MoedaDAO();
		if (moedaDAO.verificarCadastroMoedaDAO(moedaVO)) {
					System.out.println("\nPessoa já cadastrada no banco de dados.");
		} else {
            moedaVO = moedaDAO.cadastrarMoedaDAO(moedaVO);
		}
		return moedaVO;
	}

	public boolean atualizarMoedaBO(MoedaVO moedaVO) {
		boolean resultado = false;
		MoedaDAO moedaDAO = new MoedaDAO();
	
			if (moedaDAO.verificarCadastroMoedaDAO(moedaVO)) {
				resultado = moedaDAO.atualizarMoedaDAO(moedaVO);
			} else {
				System.out.println("Pessoa não consta na base de dados");
			} 
		return resultado;
	}

	public boolean excluirMoedaBO(MoedaVO moedaVO) {
		boolean resultado = false;
		MoedaDAO moedaDAO = new MoedaDAO();

		if (moedaDAO.verificarCadastroMoedaDAO(moedaVO)) {
			resultado = moedaDAO.excluirMoedaDAO(moedaVO);
			System.out.println("moeda excluída.");
		} else {
			System.out.println("Falha na exclusão da moeda.");
		}
		return resultado;
	}
}