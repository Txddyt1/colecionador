const form = document.querySelector('#formulario');
const limpar = document.querySelector('#limpar');
const exibir = document.querySelector('#exibir');
const principal = document.querySelector('.principal');
const inputFile = document.getElementById('arquivo');
const fileName = document.getElementById('nome-arquivo');
const usuarioLogadoStr = sessionStorage.getItem("usuarioLogado");
const usuarioLogado = JSON.parse(usuarioLogadoStr);
let moeda = {}; // Inicialização correta no escopo global


inputFile.addEventListener('change', function () {
    if (inputFile.files.length > 0) {
        fileName.textContent = inputFile.files[0].name;
    } else {
        fileName.textContent = 'Nenhum arquivo selecionado';
    }
});

exibir.addEventListener("click", function () {
    if (principal.style.display === 'flex') {
        principal.style.display = 'none';
        exibir.textContent = 'Mostrar';
    } else {
        principal.style.display = 'flex';
        exibir.textContent = 'Ocultar';
    }
});

function formatarDataPadraoBrasil(data) {
    let dataFormatada = new Date(data),
        dia = dataFormatada.getDate().toString().padStart(2, '0'),
        mes = (dataFormatada.getMonth() + 1).toString().padStart(2, '0'),
        ano = dataFormatada.getFullYear();
    return dia + "/" + mes + "/" + ano;
}

function formatarDataPadraoAmericano(data) {
    let dataFormatada = new Date(data),
        dia = dataFormatada.getDate().toString().padStart(2, '0'),
        mes = (dataFormatada.getMonth() + 1).toString().padStart(2, '0'),
        ano = dataFormatada.getFullYear();
    return ano + "-" + mes + "-" + dia;
}

limpar.addEventListener('click', () => {
    form.reset();
    arquivo.value = '';
    fileName.textContent = 'Nenhum arquivo selecionado';
});

async function buscarMoedas() {
    let listaMoedas = {}
    let options = {
        method: "GET"
    };
    const response = await fetch('http://localhost:8080/colecionador/rest/moeda/listar', options);
    // verifica se o status é 204 (sem conteudo) == (no content)
    if (response.status === 204) {
        alert("Nenhuma moeda encontrada.")
        return '';
    }
    const formData = await response.formData();
    const moedasMap = {};
    for (let [key, value] of formData.entries()) {
        const [moedaId, type] = value.name.split('-');
        if (!moedasMap[moedaId]) {
            moedasMap[moedaId] = { id: moedaId };
        }
        if (type === 'moeda.json') {
            moedasMap[moedaId].moedaVO = value;
        } else if (type === 'imagem.jpg') {
            moedasMap[moedaId].imagem = value;
        }
    }
    listaMoedas = Object.values(moedasMap);
    if (listaMoedas.length != 0) {
        preencherTabela(listaMoedas);
    } else {
        alert("Houve um problema na montagem do array de moedas.");
    }

}
buscarMoedas();
//--------------------------------------------------------
async function preencherTabela(listaMoedas) {
    let tbody = document.getElementById('tbody');
    tbody.innerText = ''; // Limpa o conteúdo da tabela

    for (let moeda of listaMoedas) {
        let tr = tbody.insertRow(); // Adiciona uma nova linha

        // Cria as células
        let td_imagem = tr.insertCell();
        let td_idMoeda = tr.insertCell();
        let td_nome = tr.insertCell();
        let td_pais = tr.insertCell();
        let td_ano = tr.insertCell();
        let td_valor = tr.insertCell();
        let td_detalhes = tr.insertCell();
        let td_acoes = tr.insertCell();

        // Adiciona a imagem, se existir
        if (moeda.imagem) {
            const img = document.createElement('img');
            img.src = typeof moeda.imagem === 'string' ? moeda.imagem : URL.createObjectURL(moeda.imagem);
            img.style.width = '80px';
            td_imagem.appendChild(img);
        }

        if (moeda.moedaVO) {
            const moedaJson = await moeda.moedaVO.text();
            moeda = JSON.parse(moedaJson);
            td_idMoeda.innerText = moeda.idMoeda;
            td_nome.innerText = moeda.nome;
            td_pais.innerText = moeda.pais;
            td_ano.innerText = moeda.ano;
            td_valor.innerText = moeda.valor;
            td_detalhes.innerText = moeda.detalhes;
        }

        // Preenche os dados da moeda
        td_idMoeda.innerText = moeda.idMoeda || '';
        td_nome.innerText = moeda.nome || '';
        td_pais.innerText = moeda.pais || '';
        td_ano.innerText = moeda.ano || '';
        td_valor.innerText = moeda.valor || '';
        td_detalhes.innerText = moeda.detalhes || '';

        // Cria o botão "Editar"
        let editar = document.createElement('button');
        editar.textContent = 'Editar';
        editar.style.height = '30px';
        editar.style.width = '100px';
        editar.style.margin = '5px';
        editar.style.padding = '2px';
        editar.style.background = '#9e9e9e';
        editar.style.border = 'none';
        editar.style.borderRadius = '5px';
        editar.style.cursor = 'pointer';

        // Adiciona evento ao botão "Editar"
        editar.addEventListener('click', () => {
            console.log('Moeda selecionada para edição:', moeda); // Log para depuração

            // Exibe o formulário
            principal.style.display = 'flex'; // Mostra o formulário
            exibir.textContent = 'Ocultar';

            // Preenche os campos do formulário
            document.getElementById('nome').value = moeda.nome || '';
            document.getElementById('pais').value = moeda.pais || '';
            document.getElementById('ano').value = moeda.ano || '';
            document.getElementById('valor').value = moeda.valor || '';
            document.getElementById('detalhes').value = moeda.detalhes || '';

            // Define o formulário para "atualizar"
            const form = document.getElementById('formulario');
            form.dataset.action = 'atualizar'; // Define o estado para "atualizar"
            form.dataset.moedaId = moeda.idMoeda; // Armazena o ID da moeda para a atualização
        });
        td_acoes.appendChild(editar);

        // Cria o botão "Excluir"
        let excluir = document.createElement('button');
        excluir.textContent = 'Excluir';
        excluir.style.height = '30px';
        excluir.style.width = '100px';
        excluir.style.margin = '5px';
        excluir.style.padding = '2px';
        excluir.style.background = '#9e9e9e';
        excluir.style.border = 'none';
        excluir.style.borderRadius = '5px';
        excluir.style.cursor = 'pointer';

        // Adiciona evento ao botão "Excluir"
        excluir.addEventListener('click', async () => {
            if (confirm(`Tem certeza que deseja excluir a moeda "${moeda.nome}"?`)) {
                console.log('Excluindo moeda:', moeda); // Log para depuração
                await excluirMoeda(moeda); // Função para excluir a moeda
            }
        });

        // Adiciona o botão ao DOM
        td_acoes.appendChild(excluir);
    }
}

form.addEventListener('submit', async (evento) => {
    evento.preventDefault(); // Impede o comportamento padrão de envio do formulário

    const action = form.dataset.action; // Verifica se a operação é "cadastrar" ou "atualizar"
    const idMoeda = form.dataset.moedaId || null; // Recupera o ID da moeda (para atualização)
    const fileInput = document.getElementById('arquivo'); // Campo de upload
    const file = fileInput.files[0]; // Arquivo selecionado
    const MAX_FILE_SIZE = 1 * 1024 * 1024; // Tamanho máximo: 1 MB

    // Verifica o tamanho do arquivo
    if (file && file.size > MAX_FILE_SIZE) {
        alert('O arquivo é muito grande. O tamanho máximo permitido é 1 MB.');
        return;
    }

    // Dados do formulário
    const nome = document.getElementById('nome').value;
    const pais = document.getElementById('pais').value;
    const ano = document.getElementById('ano').value;
    const valor = document.getElementById('valor').value;
    const detalhes = document.getElementById('detalhes').value;
    const idUsuario = usuarioLogado?.idUsuario || null; // ID do usuário logado

    // Monta o objeto da moeda
    const moedaVO = { idMoeda, nome, pais, ano, valor, detalhes, idUsuario };
    const moedaJsonBlob = new Blob([JSON.stringify(moedaVO)], { type: 'application/json' });

    // Prepara o FormData
    const formData = new FormData();
    if (file) formData.append('file', file); // Adiciona o arquivo se disponível
    formData.append('moedaVO', moedaJsonBlob);

    // Executa a operação correta (cadastro ou atualização)
    if (action === 'atualizar') {
        await atualizarMoeda(formData);
    } else {
        await cadastrarMoeda(formData);
    }
});


async function cadastrarMoeda(formData) {
    console.log("Iniciando cadastro de moeda...");
    console.log([...formData.entries()]); // Mostra todos os campos do FormData
    let options = {
        method: "POST",
        body: formData
    };
    try {
        const moedaJson = await fetch('http://localhost:8080/colecionador/rest/moeda/cadastrar', options);
        const moedaVO = await moedaJson.json();
        console.log("Resposta do servidor:", moedaVO);
        if (moedaVO.idMoeda != 0) {
            alert("Cadastro realizado com sucesso.");
            moeda = {};
            form.reset();
            principal.style.display = 'none';
            exibir.textContent = 'Mostrar';
            fileName.textContent = 'Nenhum arquivo selecionado.';
            buscarMoedas();
        } else {
            alert("Houve um problema no cadastro da moeda.");
        }
    } catch (error) {
        console.error("Erro ao cadastrar moeda:", error);
    }
}

async function atualizarMoeda(formData) {
    try {
        const options = {
            method: 'PUT',
            body: formData,
        };

        const response = await fetch('http://localhost:8080/colecionador/rest/moeda/atualizar', options);

        if (response.ok) {
            alert('Moeda atualizada com sucesso.');
            preencherTabela(await buscarMoedas()); // Atualiza a tabela
            principal.style.display = 'none'; // Oculta o formulário
            exibir.textContent = 'Mostrar';
        } else {
            alert('Erro ao atualizar a moeda.');
        }
    } catch (error) {
        console.error('Erro ao atualizar a moeda:', error);
    }
}


async function excluirMoeda(dados) {
    let options = {
        method: "DELETE",
        headers: { "Content-type": "application/json" },
        body: JSON.stringify({
            idMoeda: dados.idMoeda,
            nome: dados.nome,
            pais: dados.pais,
            ano: dados.ano,
            valor: dados.valor,
            detalhes: dados.detalhes
        })
    };

    const resultado = await fetch('http://localhost:8080/colecionador/rest/moeda/excluir', options);
    if (resultado.ok == true) {
        alert("Exclusão realizada com sucesso.");
        moeda = {};
        window.location.reload();
    } else {
        alert("Houve um problema na exclusão da moeda.");
    }
}