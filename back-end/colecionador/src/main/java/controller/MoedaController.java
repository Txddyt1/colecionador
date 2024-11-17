package controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public MoedaVO cadastraMoedaController(MoedaVO moedaVO) {
		MoedaBO MoedaBO = new MoedaBO();
		return MoedaBO.cadastrarMoedaBO(moedaVO);
	}

    @PUT
    @Path("/atualizar")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Boolean atualizarUsuarioController(MoedaVO moedaVO) {
        MoedaBO MoedaBO = new MoedaBO();
        return MoedaBO.atualizarMoedaBO(moedaVO);
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
