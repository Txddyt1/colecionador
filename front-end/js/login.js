document.getElementById("loginForm").addEventListener("submit", async (event) => {
    event.preventDefault(); // Previne o comportamento padrão de envio do formulário

    // Captura os valores dos campos
    const login = document.getElementById("Usuário").value; // Campo do login
    const senha = document.getElementById("senha").value;   // Campo da senha

    // Monta o objeto a ser enviado ao back-end
    const dados = { login, senha };

    try {
        // Faz a requisição ao back-end
        const response = await fetch("http://localhost:8080/colecionador/rest/usuario/logar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json", // Indica que o corpo da requisição é JSON
            },
            body: JSON.stringify(dados), // Converte os dados em JSON
        });

        // Verifica se a resposta foi bem-sucedida
        if (response.ok) {
            const data = await response.json();

            // Verifica se o idUsuario retornado é válido
            if (data.idUsuario > 0) {
                alert("Login realizado com sucesso!");
                window.location.href = "../telaprincipal.html";
                
            } else {
                alert("Usuário ou senha inválidos."); // idUsuario = 0
            }
        } else {
            // Trata respostas com erro do servidor (401, 403, etc.)
            const error = await response.json();
            alert(`Erro ao logar: ${error.mensagem || "Credenciais inválidas."}`);
        }
    } catch (error) {
        // Trata erros de conexão ou outras falhas
        console.error("Erro ao conectar ao servidor:", error);
        alert("Ocorreu um erro ao processar a solicitação. Tente novamente mais tarde.");
    }
});
