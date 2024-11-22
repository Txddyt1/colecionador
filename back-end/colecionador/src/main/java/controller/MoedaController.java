package controller;

import java.io.InputStream;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.bo.MoedaBO;
import model.vo.MoedaVO;

@Path("/moeda")
public class MoedaController {
        
	@POST
	@Path("/cadastrar")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public MoedaVO cadastraMoedaController(@FormDataParam("file") InputStream fileInputStream, 
    @FormDataParam("file") FormDataContentDisposition fileMetaData, 
    @FormDataParam("moedaVO") InputStream moedaInputStream) throws Exception {
        MoedaBO moedaBO = new MoedaBO();
        return moedaBO.cadastrarMoedaBO(moedaInputStream, fileInputStream, fileMetaData);
    }

    @PUT
    @Path("/atualizar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean atualizarMoedaController(@FormDataParam("file") InputStream fileInputStream, 
    @FormDataParam("file") FormDataContentDisposition fileMetaData, 
    @FormDataParam("moedaVO") InputStream moedaInputStream) throws Exception {
        MoedaBO moedaBO = new MoedaBO();
        return moedaBO.atualizarMoedaBO(moedaInputStream, fileMetaData, fileInputStream);
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.MULTIPART_FORM_DATA)
    public Response listarMoedasController() {
        MoedaBO moedaBO = new MoedaBO();
        return moedaBO.listarMoedasBO();
    }

    @DELETE
    @Path("/excluir")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean excluirUsuarioController (MoedaVO moedaVO) {
        MoedaBO MoedaBO = new MoedaBO();
        return MoedaBO.excluirMoedaBO(moedaVO);
    }
}
