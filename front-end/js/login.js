document.getElementById("loginForm").addEventListener("submit", async (event) => {
    event.preventDefault();

    const login = document.getElementById("Usuário").value;
    const senha = document.getElementById("senha").value;


    const dados = { login, senha };

    try {
        const response = await fetch("http://localhost:8080/colecionador/rest/usuario/logar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(dados),
        });

        if (response.ok) {
            const data = await response.json();

            if (data.idUsuario > 0) {
                alert("Login realizado com sucesso!");
                sessionStorage.setItem("usuarioLogado", JSON.stringify(data));
                window.location.href = "../telaprincipal.html";
                
            } else {
                alert("Usuário ou senha inválidos.");
            }
        } else {

            const error = await response.json();
            alert(`Erro ao logar: ${error.mensagem || "Credenciais inválidas."}`);
        }
    } catch (error) {

        console.error("Erro ao conectar ao servidor:", error);
        alert("Ocorreu um erro ao processar a solicitação. Tente novamente mais tarde.");
    }
});
