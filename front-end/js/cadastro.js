document.querySelector("form").addEventListener("submit", async (event) => {
    event.preventDefault(); // Impede o comportamento padrão do formulário

    // Captura os dados do formulário
    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const login = document.getElementById("login").value;
    const senha = document.getElementById("senha").value;

    // Monta o objeto a ser enviado
    const usuario = { nome, email, login, senha };

    try {
        // Faz a chamada ao backend
        const response = await fetch("http://localhost:8080/colecionador/rest/usuario/cadastrar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(usuario),
        });

        // Verifica se a requisição foi bem-sucedida
        if (response.ok) {
            const data = await response.json();
            alert("Cadastro realizado com sucesso!");
            window.location.href = "login.html";
        } else {
            const error = await response.json();
            alert(`Erro ao cadastrar: ${error.mensagem || "Verifique os dados e tente novamente."}`);
        }
    } catch (error) {
        console.error("Erro ao conectar ao servidor:", error);
        alert("Ocorreu um erro ao processar a solicitação. Tente novamente mais tarde.");
    }
});
