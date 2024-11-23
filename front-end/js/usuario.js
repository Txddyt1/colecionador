document.addEventListener("DOMContentLoaded", async () => {
    try {
        const usuarioLogadoStr = sessionStorage.getItem("usuarioLogado");

        if (!usuarioLogadoStr) {
            throw new Error("Nenhum dado de usuário encontrado.");
        }
        const usuarioLogado = JSON.parse(usuarioLogadoStr);

        if (!usuarioLogado.idUsuario) {
            throw new Error("Dados de usuário incompletos.");
        }

        document.getElementById("idUsuario").value = usuarioLogado.idUsuario;
        document.getElementById("nome").value = usuarioLogado.nome;
        document.getElementById("email").value = usuarioLogado.email;
        document.getElementById("login").value = usuarioLogado.login;

        console.log("Dados do usuário carregados com sucesso:", usuarioLogado);
    
    } catch (error) {
        console.error("Erro ao buscar os dados do usuário:", error.message);
        alert("Erro ao carregar os dados do usuário. Faça login novamente.");
        window.location.href = "../modules/login.html";
    }
});


document.getElementById("EditForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const idUsuario = document.getElementById("idUsuario").value;
    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const login = document.getElementById("login").value;
    const senha = document.getElementById("senha").value;

    const dados = {
        idUsuario,
        nome,
        email,
        login,
        senha,
    };

    try {
        const response = await fetch("http://localhost:8080/colecionador/rest/usuario/atualizar", {
            method: "PUT",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(dados),
        });

        if (response.ok) {
            alert("Usuário atualizado com sucesso!");
        } else {
            const error = await response.json();
            alert(`Erro ao atualizar: ${error.mensagem || "Erro desconhecido."}`);
        }
    } catch (error) {
        console.error("Erro ao conectar ao servidor:", error);
        alert("Erro ao processar a solicitação. Tente novamente mais tarde.");
    }
});

document.getElementById("excluirUsuario").addEventListener("click", async (event) => {
    event.preventDefault();

    const usuarioLogadoStr = sessionStorage.getItem("usuarioLogado");
    if (!usuarioLogadoStr) {
        alert("Erro: Nenhum usuário está logado.");
        return;
    }

    const usuarioLogado = JSON.parse(usuarioLogadoStr);
    const idUsuario = usuarioLogado.idUsuario;

    const confirmacao = confirm("Tem certeza de que deseja excluir sua conta? Esta ação é irreversível.");
    if (!confirmacao) return;

    try {

        const response = await fetch(`http://localhost:8080/colecionador/rest/usuario/excluir`, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ idUsuario }),
        });

        if (response.ok) {
            alert("Usuário excluído com sucesso.");
            sessionStorage.removeItem("usuarioLogado");
            window.location.href = "../modules/login.html";
        } else {
            const error = await response.json();
            alert(`Erro ao excluir usuário: ${error.mensagem || "Erro desconhecido."}`);
        }
    } catch (error) {
        console.error("Erro ao conectar ao servidor:", error);
        alert("Erro ao processar a solicitação. Tente novamente mais tarde.");
    }
});
