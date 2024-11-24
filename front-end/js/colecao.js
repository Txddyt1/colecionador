const form = document.querySelector('#formulario');
const limpar = document.querySelector('#limpar');
const exibir = document.querySelector('#exibir');
const principal = document.querySelector('.principal');
const inputFile = document.getElementById('arquivo');
const fileName = document.getElementById('nome-arquivo');
const usuarioLogadoStr = sessionStorage.getItem("usuarioLogado");
const usuarioLogado = JSON.parse(usuarioLogadoStr);
const userId = usuarioLogado.idUsuario;
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

async function buscarMoedas() {
    let options = {
        method: "GET"
    };
    const response = await fetch(`http://localhost:8080/colecionador/rest/moeda/listar/${userId}`, options);

    // verifica se o status é 204 (sem conteudo) == (no content)
    if (response.status === 204) {
        alert("Nenhuma moeda encontrada.")
        return '';
    }

    const formData = await response.formData();
    const moedasMap = {};

    for (let [key, value] of formData.entries()) {
        console.log('Campo no formData:', key, value); // Log para depuração
        const [moedaId, type] = value.name.split('-');

        if (!moedasMap[moedaId]) {
            moedasMap[moedaId] = { id: moedaId };
        }

        if (type === 'moeda.json') {
            moedasMap[moedaId].moedaVO = value;
        } else if (value.name.endsWith('.jpg') || value.name.endsWith('.png') || value.name.endsWith('.jpeg')) {
            moedasMap[moedaId].imagem = value;
        }
    }

    console.log('Mapa de moedas:', moedasMap); // Verifica se a imagem está presente

    // Renderiza as moedas na tabela
    let tbody = document.querySelector('#tbody'); // Seleciona o tbody da tabela
    tbody.innerHTML = ''; // Limpa a tabela antes de popular    

    Object.values(moedasMap).forEach(async (moeda) => {
        const tableRow = document.createElement('tr');

        // Coluna da imagem
        const imageCell = document.createElement('td');
        if (moeda.imagem) {
            const imgElement = document.createElement('img');
            imgElement.src = URL.createObjectURL(moeda.imagem);
            imgElement.width = 100; // Ajuste conforme necessário
            imgElement.height = 100;
            imageCell.appendChild(imgElement);
        } else {
            imageCell.textContent = 'Imagem não disponível';
        }
        tableRow.appendChild(imageCell);

        // Coluna do ID
        const idCell = document.createElement('td');
        idCell.textContent = moeda.id || '';
        tableRow.appendChild(idCell);

        // Coluna do Nome
        const nomeCell = document.createElement('td');
        if (moeda.moedaVO) {
            const moedaJson = await moeda.moedaVO.text();
            const moedaData = JSON.parse(moedaJson);
            nomeCell.textContent = moedaData.nome || 'Nome não disponível';
        } else {
            nomeCell.textContent = 'Nome não disponível';
        }
        tableRow.appendChild(nomeCell);

        const paisCell = document.createElement('td');
        if (moeda.moedaVO) {
            const moedaJson = await moeda.moedaVO.text();
            const moedaData = JSON.parse(moedaJson);
            paisCell.textContent = moedaData.pais || 'País não disponível';
        } else {
            paisCell.textContent = 'País não disponível';
        }
        tableRow.appendChild(paisCell);

        const anoCell = document.createElement('td');
        if (moeda.moedaVO) {
            const moedaJson = await moeda.moedaVO.text();
            const moedaData = JSON.parse(moedaJson);
            anoCell.textContent = moedaData.ano || 'Ano não disponível';
        } else {
            anoCell.textContent = 'Ano não disponível';
        }
        tableRow.appendChild(anoCell);

        const valorCell = document.createElement('td');
        if (moeda.moedaVO) {
            const moedaJson = await moeda.moedaVO.text();
            const moedaData = JSON.parse(moedaJson);
            valorCell.textContent = moedaData.valor || 'Valor não disponível';
        } else {
            valorCell.textContent = 'Valor não disponível';
        }
        tableRow.appendChild(valorCell);

        const detalhesCell = document.createElement('td');
        if (moeda.moedaVO) {
            const moedaJson = await moeda.moedaVO.text();
            const moedaData = JSON.parse(moedaJson);
            detalhesCell.textContent = moedaData.detalhes || 'Detalhes não disponível';
        } else {
            detalhesCell.textContent = 'Detalhes não disponível';
        }
        tableRow.appendChild(detalhesCell);

// Coluna de Ações
const acoesCell = document.createElement('td');

// Criação do botão "Editar"
const editar = document.createElement('button');
editar.textContent = 'Editar';
editar.style.height = '30px';
editar.style.width = '100px';
editar.style.margin = '5px';
editar.style.padding = '2px';
editar.style.background = '#9e9e9e';
editar.style.border = 'none';
editar.style.borderRadius = '5px';
editar.style.cursor = 'pointer';

// Evento de clique no botão "Editar"
editar.addEventListener('click', () => {
    console.log('Moeda selecionada para edição:', moeda); // Log para depuração

    // Exibe o formulário
    const principal = document.querySelector('.principal'); // Certifique-se de que esta classe existe no HTML
    const exibir = document.querySelector('#exibir'); // Botão de exibição
    principal.style.display = 'flex'; // Mostra o formulário
    exibir.textContent = 'Ocultar';

    // Preenche os campos do formulário com os valores da moeda
    document.getElementById('nome').value = moeda.nome || '';
    document.getElementById('pais').value = moeda.pais || '';
    document.getElementById('ano').value = moeda.ano || '';
    document.getElementById('valor').value = moeda.valor || '';
    document.getElementById('detalhes').value = moeda.detalhes || '';

    // Define o formulário para o modo de "atualizar"
    const form = document.getElementById('formulario');
    form.dataset.action = 'atualizar'; // Define a ação para "atualizar"
    form.dataset.moedaId = moeda.idMoeda; // Armazena o ID da moeda para a atualização
});

// Adiciona o botão "Editar" à célula de ações
acoesCell.appendChild(editar);

// Criação do botão "Excluir"
const excluir = document.createElement('button');
excluir.textContent = 'Excluir';
excluir.style.height = '30px';
excluir.style.width = '100px';
excluir.style.margin = '5px';
excluir.style.padding = '2px';
excluir.style.background = '#e57373'; // Cor vermelha para destacar a exclusão
excluir.style.border = 'none';
excluir.style.borderRadius = '5px';
excluir.style.cursor = 'pointer';

// Evento de clique no botão "Excluir"
excluir.addEventListener('click', async () => {
    const moedaId = moeda.id; // Captura o ID da moeda

    if (!moedaId) {
        alert("Erro: O ID da moeda está ausente.");
        console.error("Erro: O objeto moeda não possui um ID válido.", moeda);
        return;
    }

    if (confirm(`Tem certeza que deseja excluir a moeda com ID "${moedaId}"?`)) {
        console.log('Tentando excluir a moeda com ID:', moedaId); // Log para depuração

        try {
            const options = {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ idMoeda: moedaId }), // Envia o ID no corpo da requisição
            };

            const resultado = await fetch('http://localhost:8080/colecionador/rest/moeda/excluir', options);

            // Log da resposta do servidor
            console.log('Resposta do servidor:', resultado);

            if (resultado.ok) {
                alert("Exclusão realizada com sucesso.");
                window.location.reload(); // Recarrega a página para atualizar a tabela
            } else {
                const erro = await resultado.text(); // Lê a mensagem de erro do backend
                alert(`Erro ao excluir a moeda: ${erro}`);
                console.error("Erro na exclusão:", erro);
            }
        } catch (error) {
            console.error('Erro ao excluir a moeda:', error);
            alert("Erro ao processar a exclusão. Tente novamente.");
        }
    }
});

acoesCell.appendChild(excluir);

tableRow.appendChild(acoesCell);

        // Adicione a linha à tabela
        tbody.appendChild(tableRow);
    });
}
buscarMoedas();
//--------------------------------------------------------

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