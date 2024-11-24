package model.bo;

import model.dao.MoedaDAO;
import model.vo.MoedaVO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.file.StreamDataBodyPart;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.Response;

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
    
	public MoedaVO cadastrarMoedaBO(InputStream moedaInputStream, InputStream fileInputStream, FormDataContentDisposition fileMetaData){
        MoedaDAO moedaDAO = new MoedaDAO();
		MoedaVO moedaVO = null;
		try {
			byte[] arquivo = this.converterByteParaArray(fileInputStream);
			String moedaJSON = new String(this.converterByteParaArray(moedaInputStream), StandardCharsets.UTF_8);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.findAndRegisterModules();
			moedaVO = objectMapper.readValue(moedaJSON, MoedaVO.class);
			moedaVO.setImagem(arquivo);

			if (moedaDAO.verificarCadastroMoedaDAO(moedaVO)) {
				System.out.println("\nMoeda já cadastrada no banco de dados.");
			} else {
				moedaVO = moedaDAO.cadastrarMoedaDAO(moedaVO);
			} 
		} catch (FileNotFoundException erro) {
			System.out.println(erro);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return moedaVO;
	}

	public boolean atualizarMoedaBO(InputStream fileInputStream, FormDataContentDisposition fileMetaData, InputStream moedaInputStream) {
		boolean resultado = false;
		MoedaDAO moedaDAO = new MoedaDAO();
		MoedaVO  moedaVO = null;
		try{
			byte[] arquivo = null;
			if (fileInputStream != null) {
				arquivo = this.converterByteParaArray(fileInputStream);
			}
			String moedaJSON = new String(this.converterByteParaArray(moedaInputStream), StandardCharsets.UTF_8);

			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.findAndRegisterModules();
			moedaVO = objectMapper.readValue(moedaJSON, MoedaVO.class);
			if (arquivo.length > 0) {
				moedaVO.setImagem(arquivo);
			}

			if (moedaDAO.verificarCadastroMoedaDAO(moedaVO)) {
				resultado = moedaDAO.atualizarMoedaDAO(moedaVO);
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

	public Response listarMoedasBO(int idUsuario) {
		MoedaDAO moedaDAO = new MoedaDAO();
		ArrayList<MoedaVO> listaMoedasVO = moedaDAO.listarTodasMoedasDAO(idUsuario);
		if (listaMoedasVO.isEmpty()) {
			System.out.println("\nLista de moedas está vazia.");
			return Response.status(Response.Status.NO_CONTENT).entity("Nenhuma pessoa encontrada").build();
		}
		
		MultiPart multiPart = new FormDataMultiPart();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

		try {
			for (MoedaVO moedaVO : listaMoedasVO) {
				byte[] imagem = moedaVO.getImagem();
				moedaVO.setImagem(null);

				String moedaJSON = objectMapper.writeValueAsString(moedaVO);
				multiPart.bodyPart(new StreamDataBodyPart("moedaVO", new ByteArrayInputStream(moedaJSON.getBytes()),
				moedaVO.getIdMoeda() + "-moeda.json"));

				if (imagem != null) {
					multiPart.bodyPart(new StreamDataBodyPart("imagem", new ByteArrayInputStream(imagem),
					moedaVO.getIdMoeda() + "-imagem.jpeg"));
				}
			}
			return Response.ok(multiPart).build();
		} catch (Exception e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao processar resposta multipart.").build();
		}
		
	}
}