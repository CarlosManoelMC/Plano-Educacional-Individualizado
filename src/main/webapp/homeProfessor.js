document.addEventListener("DOMContentLoaded", function() {

	document.getElementById('searchInput').addEventListener('input', filterElements);
	function filterElements() {
		const searchInput = document.getElementById('searchInput');
		const query = searchInput.value.trim().toLowerCase();
		const elementsToFilter = document.querySelectorAll('.aluno-info');

		elementsToFilter.forEach(function(element) {
			const queryParts = query.split(' ');
			const name = queryParts[0];
			const text = element.textContent.toLowerCase();

			if (text.includes(name.toLowerCase())) {
				element.style.display = 'block';
			} else {
				element.style.display = 'none';
			}
		});

		console.log("Valor do input:", query);
	}
	
	
	
	function visualizarAluno() {
		fetch('Card', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json; charset=utf-8'
			}
		})
			.then(response => response.json())
			.then(data => {
				const content = document.querySelector('.content');
				content.innerHTML = '';
				if (data.hasOwnProperty('locations') && Array.isArray(data.locations)) {
					data.locations.forEach(aluno => {
						const alunoInfo = document.createElement('div');
						alunoInfo.classList.add('aluno-info');

						alunoInfo.innerHTML = `
					    <p><strong>Nome:</strong> ${aluno.Nome}</p>
					    <p><strong>Dificuldade:</strong> ${aluno.Disciplina}</p>
					    <button class="visualizar-info-btn">Visualizar Informações</button>
					`;

						content.appendChild(alunoInfo);
						const visualizarInfoBtn = alunoInfo.querySelector('.visualizar-info-btn');
						visualizarInfoBtn.addEventListener('click', function() {
							abrirModal(aluno);
						});
						visualizarInfoBtn.addEventListener('click', function() {
							abrirModalPEIs(aluno);
						});
					});
				} else {
					console.error('Os dados retornados não contêm um array de alunos:', data);
				}
			})
			.catch(error => {
				console.error('Erro:', error);
			});
	}

	function abrirModal(aluno) {
		const modalContent = `
            <div id="modalEstudanteInformacoes" class="modal">
                <div class="modal-content">
                    <span class="close">&times;</span>
                    <h2>Informações Completas do Aluno</h2>
                    <div id="studentDetailsContainer">
                        <p><strong>Nome:</strong> ${aluno.Nome}</p>
                        <p><strong>Matrícula:</strong> ${aluno.Matricula}</p>
                        <p><strong>Endereço:</strong> ${aluno.Endereco}</p>
                        <p><strong>Telefone:</strong> ${aluno.Telefone}</p>
                        <p><strong>Dificuldade:</strong> ${aluno.Disciplina}</p>
                        <hr>
   						<div class="content_PEI">
					    <!-- Aqui serão renderizados os cards dos alunos -->
						</div>	
	
                        
                    </div>
                </div>
            </div>
        `;



		document.body.insertAdjacentHTML('beforeend', modalContent);
		console.log(aluno);
		const modal = document.getElementById('modalEstudanteInformacoes');
		const closeModalButton = modal.querySelector('.close');

		closeModalButton.addEventListener('click', () => {
			modal.remove();
		});

		modal.style.display = 'block';

	}



	function abrirModalPEIs(aluno) {
		fetch('CardPEI?id_aluno=' + aluno.id, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json; charset=utf-8'
			}
		})
			.then(response => response.json())
			.then(data => {
				const contentPEI = document.querySelector('.content_PEI');
				contentPEI.innerHTML = '';
				if (data.hasOwnProperty('peis') && Array.isArray(data.peis)) {
					data.peis.forEach(pei => {
						if (pei && pei.dataInicio) {
							const peiCard = document.createElement('div');
							peiCard.classList.add('pei-card');


							peiCard.innerHTML = `
                            <h2>PEI</h2>
                            <p><strong>Data Inicio:</strong> ${pei.dataInicio}</p>
                            <p><strong>Data Final:</strong> ${pei.dataFinal}</p>
                            <button class="visualizar-pei-btn">Visualizar Informacoes</button>
                        `;


							contentPEI.appendChild(peiCard);
							const visualizarPEIBtn = peiCard.querySelector('.visualizar-pei-btn');
							visualizarPEIBtn.addEventListener('click', function() {
								visualizarInformacoesPEI(pei, aluno);
							});
						} else {
							console.error('PEI inválido:', pei);
						}
					});
				} else {
					console.error('Os dados retornados não contêm um array de PEIs:', data);
				}
			})
			.catch(error => {
				console.error('Erro:', error);
			});

	}



	function visualizarInformacoesPEI(pei, aluno) {
		const modalContent = `
        <div id="modalPEIInfo" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Detalhes do PEI</h2>
                <p><strong>Conteúdo Pragmático:</strong> ${pei.conteudoPragmatico}</p>
                <p><strong>Metodologia:</strong> ${pei.metodologia}</p>
                <p><strong>Avaliação:</strong> ${pei.avaliacao}</p>
                <p><strong>Objetivos Específicos:</strong> ${pei.objetivosEspecificos}</p>
                <p><strong>Habilidade:</strong> ${pei.habilidade}</p>
                <p><strong>Condição:</strong> ${pei.condicao}</p>
                <p><strong>Necessidade Específica:</strong> ${pei.necessidadeEspecifica}</p>
                <p><strong>Dificuldade:</strong> ${pei.dificuldade}</p>
                <p><strong>Conhecimento:</strong> ${pei.conhecimento}</p>
                <p><strong>Data Início:</strong> ${pei.dataInicio}</p>
                <p><strong>Data Final:</strong> ${pei.dataFinal}</p>
                <hr>
                <button id="visualizarMetas">Visualizar Metas</button>
                
            </div>
        </div>
    `;
		const idPEI = pei.id;
		document.body.insertAdjacentHTML('beforeend', modalContent);

		const modal = document.getElementById('modalPEIInfo');
		const closeModalButton = modal.querySelector('.close');

		closeModalButton.addEventListener('click', () => {
			modal.remove();
		});

		modal.style.display = 'block';

	}
	
	visualizarAluno();

});
