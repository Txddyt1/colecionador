const form = document.querySelector('#formulario');
const limpar = document.querySelector('#limpar');
const exibir = document.querySelector('#exibir');
const principal = document.querySelector('.principal');
const inputFile = document.getElementById('arquivo');
const fileName = document.getElementById('nome-arquivo');

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
    const response = await fetch('http://localhost:8080/colecionador/rest/moeda/cadastrar', options);
    // verifica se o status é 204 (sem conteudo) == (no content)
    if (response.status === 204) {
        alert("Nenhuma moeda encontrada.");
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

async function preencherTabela(listaMoedas) {
    let tbody = document.getElementById('tbody');
    tbody.innerText = '';

    for (let moeda of listaMoedas) {
        let tr = tbody.insertRow();
        let td_imagem = tr.insertCell();
        let td_idMoeda = tr.insertCell();
        let td_nome = tr.insertCell();
        let td_pais = tr.insertCell();
        let td_ano = tr.insertCell();
        let td_valor = tr.insertCell();
        let td_detalhes = tr.insertCell();
        let td_acoes = tr.insertCell();

        if (moeda.imagem) {
            const imgUrl = URL.createObjectURL(moeda.imagem);
            const img = document.createElement('img');
            img.src = imgUrl;
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
        editar.setAttribute('onclick', 'editarMoeda(' + JSON.stringify(moeda) + ')');
        td_acoes.appendChild(editar);

        let excluir = document.createElement('button');
        excluir.textContent = 'excluir';
        excluir.style.height = '30px';
        excluir.style.width = '100px';
        excluir.style.margin = '5px';
        excluir.style.padding = '2px';
        excluir.style.background = '#9e9e9e';
        excluir.style.border = 'none';
        excluir.style.borderRadius = '5px';
        excluir.style.cursor = 'pointer';
        excluir.setAttribute('onclick', 'excluirMoeda(' + JSON.stringify(moeda) + ')');
        td_acoes.appendChild(excluir);
    }

}

async function excluirMoeda(dados) {
    let options = {
        method: "DELETE",
        headers: {"Content-type": "application/json"},
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
    if (resultado.ok == true){
        alert("Exclusão realizada com sucesso.");
        moeda = {};
        buscarMoedas();
    } else {
        alert("Houve um problema na exclusão da moeda.");
    }
    //-------------------------
    
    form.addEventListener('submit', (evento) => {
        evento.preventDefault();

        const fileInput = document.getElementById('arquivo');
        const idMoeda = moeda.idMoeda;
        const nome = document.getElementById('nome').value;
        const pais = document.getElementById('pais').value;
        const ano = document.getElementById('ano').value;
        const valor = document.getElementById('valor').value;
        const detalhes = document.getElementById('detalhes').value;

        const MAX_FILE_SIZE = 1 * 1024 * 1024;
        const file = fileInput.files[0];
        if (file != undefined && file.size > MAX_FILE_SIZE) {
            alert('O arquivo é muito. O tamanho máximo permitido é 1 MB.');
            return;
        }

        const moedaVO = {
            idMoeda: idMoeda,
            nome: nome,
            pais: pais,
            ano: ano,
            valor: valor,
            detalhes: detalhes
        }
        const moedaJsonBlob = new Blob([JSON.stringify(moedaVO)], { type: 'application/json' });

        const formData = new FormData();
        if(file != undefined) {
            formData.append('file', file);
        } else {
            formData.append('file', []);
        }
        formData.append('moedaVO', moedaJsonBlob);

        if(idMoeda != undefined) {
            atualizarMoeda(formData);
        } else {
            cadastrarMoeda(formData);
        }
    })

    async function cadastrarMoeda(formData) {
        let options = {
            method: "POST",
            body: formData
        };
        const moedaJson = await fetch('http://localhost:8080/colecionador/rest/moeda/cadastrar', options);
        const moedaVO = await moedaJson.json();
        if(moedaVO.idMoeda != 0) {
            alert("Cadastro realizado com sucesso.");
            moeda = {};
            form.reset();
            fileName.textContent = 'Nenhum arquivo selecionado.';
            buscarMoedas();
        } else {
            alert("Houve um problema no cadastro da moeda.");
        }
        form.reset();
    }

    async function atualizarMoeda(formData) {
        let options = {
            method: "PUT",
            body: formData
        };
        const resultado = await fetch('http://localhost:8080/colecionador/rest/moeda/atualizar', options);
        if(resultado.ok == true) {
            alert("Atualização realizada com sucesso.");
            moeda = {};
            form.reset();
            fileName.textContent = 'Nenhum arquivo selecionado.';
            buscarMoedas();
        } else {
            alert("Houve um problema na atualização da pessoa.");
        }
        form.reset();
    }

    async function editarMoeda(dados) {
        moeda.idMoeda = dados.idMoeda;
        document.querySelector('#nome').value = dados.nome;
        document.querySelector('#pais').value = dados.pais;
        document.querySelector('#ano').value = dados.ano;
        document.querySelector('#valor').value = dados.valor;
        document.querySelector('#detalhes').value = dados.detalhes;
    }

}
