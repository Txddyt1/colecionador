package controller;

import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import model.bo.UsuarioBO;
import model.vo.UsuarioVO;

@Path("/usuario")
public class UsuarioController {
    
	@POST
	@Path("/cadastrar")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UsuarioVO cadastrarUsuarioController(UsuarioVO usuarioVO) {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.cadastrarUsuarioBO(usuarioVO);
	}

    @PUT
    @Path("/atualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean atualizarUsuarioController(@FormDataParam("usuarioVO") InputStream usuarioInputStream) throws Exception {
        UsuarioBO usuarioBO = new UsuarioBO();
        return usuarioBO.atualizarUsuarioBO(usuarioInputStream);
    }

    @DELETE
    @Path("/excluir")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean excluirUsuarioController (UsuarioVO usuarioVO) {
        UsuarioBO usuarioBO = new UsuarioBO();
        return usuarioBO.excluirUsuarioBO(usuarioVO);
    }
}
