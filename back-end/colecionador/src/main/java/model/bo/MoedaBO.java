package model.bo;

import model.dao.MoedaDAO;
import model.vo.MoedaVO;

import java.util.List;

public class MoedaBO {
    
    private MoedaDAO moedaDAO = new MoedaDAO();
    
    // Método para cadastrar moeda com verificação de duplicidade
    public boolean cadastrarMoeda(MoedaVO moeda) {
        // Verifica se a moeda já existe para o usuário
        if (moedaDAO.consultarMoedaPorNome(moeda.getNome(), moeda.getUsuario().getIdUsuario())) {
            System.out.println("Moeda já cadastrada na coleção do usuário.");
            return false; // Retorna falso se a moeda já existe
        }
        return moedaDAO.cadastrarMoeda(moeda);
    }

    // Método para listar moedas de um usuário específico
    public List<MoedaVO> listarMoedas(int idUsuario) {
        return moedaDAO.listarMoedas(idUsuario);
    }

    // Método para atualizar uma moeda com verificação de existência
    public boolean atualizarMoeda(MoedaVO moeda) {
        // Verifica se a moeda existe para o usuário
        if (!moedaDAO.consultarMoedaPorNome(moeda.getNome(), moeda.getUsuario().getIdUsuario())) {
            System.out.println("Moeda não encontrada para atualização.");
            return false; // Retorna falso se a moeda não existe
        }
        return moedaDAO.atualizarMoeda(moeda);
    }

    // Método para excluir uma moeda da coleção
    public boolean excluirMoeda(int idMoeda) {
        if (!moedaDAO.excluirMoeda(idMoeda)) {
            System.out.println("Erro ao excluir moeda.");
            return false;
        }
        return true;
    }
}