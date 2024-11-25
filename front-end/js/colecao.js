const form = document.querySelector('#formulario');
const limpar = document.querySelector('#limpar');
const exibir = document.querySelector('#exibir');
const principal = document.querySelector('.principal');
const inputFile = document.getElementById('arquivo');
const fileName = document.getElementById('nome-arquivo');
const usuarioLogadoStr = sessionStorage.getItem("usuarioLogado");
const usuarioLogado = JSON.parse(usuarioLogadoStr);
const userId = usuarioLogado.idUsuario;
let moeda = {};

//---Troca nome do arquivo---
inputFile.addEventListener('change', function () {
    if (inputFile.files.length > 0) {
        fileName.textContent = inputFile.files[0].name;
    } else {
        fileName.textContent = 'Nenhum arquivo selecionado';
    }
});
//---Botão de exibição---
exibir.addEventListener("click", function () {
    if (principal.style.display === 'flex') {
        principal.style.display = 'none';
        exibir.textContent = 'Mostrar';
    } else {
        principal.style.display = 'flex';
        exibir.textContent = 'Ocultar';
    }
});
//---Formatador de data BR---
function formatarDataPadraoBrasil(data) {
    let dataFormatada = new Date(data),
        dia = dataFormatada.getDate().toString().padStart(2, '0'),
        mes = (dataFormatada.getMonth() + 1).toString().padStart(2, '0'),
        ano = dataFormatada.getFullYear();
    return dia + "/" + mes + "/" + ano;
}
//---Formatador de data Ame---
function formatarDataPadraoAmericano(data) {
    let dataFormatada = new Date(data),
        dia = dataFormatada.getDate().toString().padStart(2, '0'),
        mes = (dataFormatada.getMonth() + 1).toString().padStart(2, '0'),
        ano = dataFormatada.getFullYear();
    return ano + "-" + mes + "-" + dia;
}
//---Limpa form---
limpar.addEventListener('click', () => {
    form.reset();
    arquivo.value = '';
    fileName.textContent = 'Nenhum arquivo selecionado';
});
//---Cadastra moeda---
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
//---Atualiza moeda--- (not function)
async function atualizarMoeda(formData) {
    try {
        const options = {
            method: 'PUT',
            body: formData,
        };

        const response = await fetch(`http://localhost:8080/colecionador/rest/moeda/atualizar`, options);

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

//---Excluir moeda---
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
//---Listar moedas---
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
        console.log('Campo no formData:', key, value);
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

    console.log('Mapa de moedas:', moedasMap);

    // Renderiza as moedas na tabela
    let tbody = document.querySelector('#tbody'); 
    tbody.innerHTML = '';

    Object.values(moedasMap).forEach(async (moeda) => {
        const tableRow = document.createElement('tr');

        const imageCell = document.createElement('td');
        if (moeda.imagem) {
            const imgElement = document.createElement('img');
            imgElement.src = URL.createObjectURL(moeda.imagem);
            imgElement.width = 80;
            imgElement.height = 80;
            imageCell.appendChild(imgElement);
        } else {
            imageCell.textContent = 'Imagem não disponível';
        }
        tableRow.appendChild(imageCell);

        const idCell = document.createElement('td');
        idCell.textContent = moeda.id || '';
        tableRow.appendChild(idCell);

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

const acoesCell = document.createElement('td');

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

editar.addEventListener('click', () => {
    console.log('Moeda selecionada para edição:', moeda); 

    const principal = document.querySelector('.principal');
    const exibir = document.querySelector('#exibir');
    principal.style.display = 'flex';
    exibir.textContent = 'Ocultar';

    document.getElementById('nome').value = moeda.nome || '';
    document.getElementById('pais').value = moeda.pais || '';
    document.getElementById('ano').value = moeda.ano || '';
    document.getElementById('valor').value = moeda.valor || '';
    document.getElementById('detalhes').value = moeda.detalhes || '';

    const form = document.getElementById('formulario');
    form.dataset.action = 'atualizar';
    form.dataset.moedaId = moeda.idMoeda;
});

acoesCell.appendChild(editar);

// Criação do botão "Excluir"
const excluir = document.createElement('button');
excluir.textContent = 'Excluir';
excluir.style.height = '30px';
excluir.style.width = '100px';
excluir.style.margin = '5px';
excluir.style.padding = '2px';
excluir.style.background = '#9e9e9e'; // Cor vermelha para destacar a exclusão
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

//---Tratamento do botão de submit---
form.addEventListener('submit', async (evento) => {
    evento.preventDefault();

    const action = form.dataset.action;
    const idMoeda = form.dataset.moedaId || null;
    const fileInput = document.getElementById('arquivo');
    const file = fileInput.files[0];
    const MAX_FILE_SIZE = 1 * 1024 * 1024;

    if (file && file.size > MAX_FILE_SIZE) {
        alert('O arquivo é muito grande. O tamanho máximo permitido é 1 MB.');
        return;
    }

    const nome = document.getElementById('nome').value;
    const pais = document.getElementById('pais').value;
    const ano = document.getElementById('ano').value;
    const valor = document.getElementById('valor').value;
    const detalhes = document.getElementById('detalhes').value;
    const idUsuario = usuarioLogado?.idUsuario || null;

    const moedaVO = { idMoeda, nome, pais, ano, valor, detalhes, idUsuario };
    const moedaJsonBlob = new Blob([JSON.stringify(moedaVO)], { type: 'application/json' });

    const formData = new FormData();
    if (file) formData.append('file', file);
    formData.append('moedaVO', moedaJsonBlob);

    if (action === 'atualizar') {
        await atualizarMoeda(formData);
    } else {
        await cadastrarMoeda(formData);
    }
});