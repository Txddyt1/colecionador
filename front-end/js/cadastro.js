document.querySelector("form").addEventListener("submit", async (event) => {
    console.log("Formulário enviado com sucesso!");
    event.preventDefault();


    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email").value;
    const login = document.getElementById("login").value;
    const senha = document.getElementById("senha").value;

    if (nome.length < 3) {
        alert("O campo Nome deve ter pelo menos 3 caracteres.");
        document.getElementById("nome").focus();
        return;
    }

    if (email.length < 3) {
        alert("O campo E-mail deve ter pelo menos 3 caracteres.");
        document.getElementById("email").focus();
        return;
    }

    if (login.length < 3) {
        alert("O campo Login deve ter pelo menos 3 caracteres.");
        document.getElementById("login").focus();
        return;
    }

    if (senha.length < 3) {
        alert("O campo Senha deve ter pelo menos 3 caracteres.");
        document.getElementById("senha").focus();
        return;
    }

    const usuario = { nome, email, login, senha };

    try {

        const response = await fetch("http://localhost:8080/colecionador/rest/usuario/cadastrar", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify(usuario),
        });

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
