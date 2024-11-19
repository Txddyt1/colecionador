document.addEventListener("DOMContentLoaded", async () => {
    try {
        // Substitua o ID fixo pelo método para obter dinamicamente o idUsuario, se necessário
        const idUsuario = 1; // Exemplo: substitua pelo ID dinâmico do usuário
        const response = await fetch(`http://localhost:8080/colecionador/rest/usuario/obter/${idUsuario}`, {
            method: "GET",
        });

        if (response.ok) {
            const usuario = await response.json();

            // Preencha os campos do formulário com os dados do usuário
            sessionStorage.getItem("usuariologado")
            document.getElementById("nome").value = usuario.nome;
            document.getElementById("email").value = usuario.email;
            document.getElementById("login").value = usuario.login;
            document.getElementById("senha").value = usuario.senha || ""; // Apenas se a senha puder ser visível (não recomendado)
        } else {
            alert("Erro ao carregar os dados do usuário.");
        }
    } catch (error) {
        console.error("Erro ao buscar os dados do usuário:", error);
        alert("Erro ao carregar os dados. Tente novamente mais tarde.");
    }
});

// Adiciona o evento de submissão ao formulário
document.getElementById("EditForm").addEventListener("submit", async (event) => {
    event.preventDefault(); // Previne o comportamento padrão do formulário

    // Captura os valores dos campos
    const idUsuario = document.querySelector("input[name='idUsuario']").value;
    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const login = document.getElementById("login").value;
    const senha = document.getElementById("senha").value;

    // Monta o objeto com os dados atualizados
    const dados = {
        idUsuario,
        nome,
        email,
        login,
        senha,
    };

    try {
        // Envia os dados para o endpoint de atualização
        const response = await fetch("http://localhost:8080/colecionador/rest/usuario/atualizar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json", // Indica que o corpo da requisição é JSON
            },
            body: JSON.stringify(dados), // Converte os dados em JSON
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
