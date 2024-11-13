package controller;

import java.io.InputStream;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import model.bo.UsuarioBO;
import model.vo.UsuarioVO;

@Path("/pessoa")
public class UsuarioController {
    
    @POST
    @Path("/cadastrar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response cadastrarUsuarioController(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData,
            @FormDataParam("usuarioVO") InputStream usuarioInputStream) {
        try {
            UsuarioBO usuarioBO = new UsuarioBO();
            UsuarioVO usuarioCadastrado = usuarioBO.cadastrarUsuarioBO(usuarioInputStream, fileInputStream, fileMetaData);
            return Response.ok(usuarioCadastrado).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao cadastrar usuário: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/listar")
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarTodosUsuariosController() {
        UsuarioBO usuarioBO = new UsuarioBO();
        return Response.ok(usuarioBO.consultarTodosUsuariosBO()).build();
    }

    @GET
    @Path("/pesquisar/{idusuario}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response consultarUsuarioController(@PathParam("idusuario") int idUsuario) {
        UsuarioBO usuarioBO = new UsuarioBO();
        UsuarioVO usuario = usuarioBO.consultarUsuarioBO(idUsuario);
        if (usuario != null) {
            return Response.ok(usuario).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("Usuário não encontrado.").build();
        }
    }

    @PUT
    @Path("/atualizar")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response atualizarUsuarioController(
            @FormDataParam("file") InputStream fileInputStream,
            @FormDataParam("file") FormDataContentDisposition fileMetaData,
            @FormDataParam("usuarioVO") InputStream usuarioInputStream) {
        try {
            UsuarioBO usuarioBO = new UsuarioBO();
            Boolean atualizado = usuarioBO.atualizarUsuarioBO(usuarioInputStream, fileInputStream, fileMetaData);
            if (atualizado) {
                return Response.ok("Usuário atualizado com sucesso.").build();
            } else {
                return Response.status(Response.Status.NOT_MODIFIED).entity("Usuário não foi atualizado.").build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Erro ao atualizar usuário: " + e.getMessage()).build();
        }
    }
}
