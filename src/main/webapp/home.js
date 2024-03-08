document.addEventListener("DOMContentLoaded", function() {
	document.getElementById('searchInput').addEventListener('input', filterElements);
	function filterElements() {
		const searchInput = document.getElementById('searchInput');
		const query = searchInput.value.trim().toLowerCase();


		const elementsToFilter = document.querySelectorAll('.aluno-info');

		elementsToFilter.forEach(function(element) {
			const text = element.textContent.toLowerCase();


			if (text.includes(name.toLowerCase())) {

				element.style.display = 'block';
			} else {

				element.style.display = 'none';
			}
		});

		console.log("Valor do input:", query);
	}




	document.getElementById('visualizarProfessor').addEventListener('click', function() {
		document.querySelector('.pesquisar_professores').style.display = 'block';
	});

	document.getElementById('visualizarProfessor').addEventListener('click', function() {
		document.querySelector('.pesquisar_aluno').style.display = 'none';
	});


	document.getElementById('visualizarAluno').addEventListener('click', function() {
		document.querySelector('.pesquisar_professores').style.display = 'none';
	});

	document.getElementById('visualizarAluno').addEventListener('click', function() {
		document.querySelector('.pesquisar_aluno').style.display = 'block';
	});


	visualizarAluno();
	const cadastrarAlunoLink = document.getElementById('cadastrarAluno');
	const visualizarAlunoLink = document.getElementById('visualizarAluno');

	const modalCadastroAluno = document.getElementById('modalCadastroAluno');
	const closeModalButton = modalCadastroAluno.querySelector('.close');

	cadastrarAlunoLink.addEventListener('click', function() {
		modalCadastroAluno.style.display = 'block';
	});

	visualizarAlunoLink.addEventListener('click', visualizarAluno);

	closeModalButton.addEventListener('click', function() {
		modalCadastroAluno.style.display = 'none';
	});

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
                        <p><strong>Tutor:</strong> ${aluno.Tutor}</p>
                        <div class="icons-container">
					        <i class="fas fa-pencil-alt edit-icon"></i>
					        <i class="fas fa-trash-alt delete-icon"></i>
					    </div>
                        <button id="PEI">Criar PEI</button>
                        <button id="Metas_criar">Criar Metas</button>
                        <button id="Metas_visualizar">Visualizar Metas</button>
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

		const criarMetas = document.getElementById('Metas_criar');
		criarMetas.addEventListener('click', function() {
			abrirModalMeta(aluno.id);
		});
		closeModalButton.addEventListener('click', () => {
			modal.remove();
		});
		modal.style.display = 'block';
		const visualizarMetasBtn = document.getElementById('Metas_visualizar');
		const modalMetas = document.getElementById('modalMetas');
		const closeModal = document.querySelector('.close');
		visualizarMetasBtn.addEventListener('click', function() {
			modalMetas.style.display = 'block';
			visualizarMetasAluno();
		});

		closeModal.addEventListener('click', function() {
			modalMetas.style.display = 'none';
		});

		window.addEventListener('click', function(event) {
			if (event.target == modalMetas) {
				modalMetas.style.display = 'none';
			}
		});



		const criarPEIButton = document.getElementById('PEI');
		criarPEIButton.addEventListener('click', () => {

			abrirModalPEI(aluno);
		});


		const editIcon = document.querySelector('.edit-icon');
		editIcon.addEventListener('click', function() {

			abrirModalEdicao(aluno);
		});

		const deleteIcon = document.querySelector('.delete-icon');
		deleteIcon.addEventListener('click', function() {
			const confirmDelete = confirm("Tem certeza que deseja excluir este aluno?");
			if (confirmDelete) {
				const idAluno = aluno.id;
				fetch('Card', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					},
					body: 'idAluno=' + encodeURIComponent(idAluno)
				})
					.then(response => response.text())
					.then(data => {

						console.log(data);

						window.location.reload();
					})
					.catch(error => {
						console.error('Erro:', error);
					});
			}
		});
	}

	// Função para buscar as metas do aluno
	function buscarMetasAluno() {
		return fetch('MetasServlet', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json; charset=utf-8'
			}
		})
			.then(response => {
				if (!response.ok) {
					throw new Error('Erro ao buscar as metas do aluno');
				}
				return response.json();
			})
			.then(data => data.metas)
			.catch(error => {
				console.error('Erro ao buscar as metas:', error);
				return []; // Retorna uma lista vazia em caso de erro
			});
	}

	// Função para renderizar as metas na interface do usuário
	function renderizarMetas(metas) {
		const content = document.querySelector('.content_Metas');
		content.innerHTML = '';

		if (metas.length === 0) {
			content.innerHTML = '<p>Nenhuma meta encontrada.</p>';
			return;
		}

		metas.forEach(meta => {
			const metaInfo = document.createElement('div');
			metaInfo.classList.add('meta-info');

			metaInfo.innerHTML = `
            <p><strong>Descrição:</strong> ${meta.titulo}</p>
            <p><strong>Descrição:</strong> ${meta.descricao}</p>
            <button class="visualizar-meta-btn">Visualizar Meta</button>
        `;

			content.appendChild(metaInfo);

			const visualizarMetaBtn = metaInfo.querySelector('.visualizar-meta-btn');
			visualizarMetaBtn.addEventListener('click', () => {
				abrirModalMeta(meta);
			});
		});
	}

	// Função principal para buscar e renderizar as metas do aluno
	function visualizarMetasAluno() {
		buscarMetasAluno()
			.then(metas => renderizarMetas(metas))
			.catch(error => console.error(error));
	}

	// Chamar a função principal para exibir as metas quando necessário
	visualizarMetasAluno();


	function visualizarMetasAluno() {
		fetch('MetasServlet', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json; charset=utf-8'
			}
		})
			.then(response => response.json())
			.then(data => {
				const content = document.querySelector('.content_Metas');
				content.innerHTML = '';

				if (data.hasOwnProperty('metas') && Array.isArray(data.metas)) {
					data.metas.forEach(meta => {
						const metaInfo = document.createElement('div');
						metaInfo.classList.add('meta-info');

						metaInfo.innerHTML = `
                        <p><strong>Descrição:</strong> ${meta.titulo}</p>
                        <p><strong>Descrição:</strong> ${meta.descricao}</p>
                        <button class="visualizar-meta-btn">Visualizar Meta</button>
                    `;

						content.appendChild(metaInfo);

						const visualizarMetaBtn = metaInfo.querySelector('.visualizar-meta-btn');
						visualizarMetaBtn.addEventListener('click', function() {
							abrirModalMeta(meta);
						});
					});
				} else {
					console.error('Os dados retornados não contêm um array de metas:', data);
				}
			})
			.catch(error => {
				console.error('Erro:', error);
			});
	}


	function abrirModalMeta(idAluno) {
		const modalMetaContent = `
        <div id="modalCriarMeta" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Criar Nova Meta</h2>
                <div id="metaForm">
                    <label for="metaNome">Nome da Meta:</label>
                    <input type="text" id="metaNome" name="metaNome" required>

                    <label for="metaDescricao">Descrição:</label>
                    <textarea id="metaDescricao" name="metaDescricao" required></textarea>
                    <button id="submitMeta">Criar Meta</button>
                </div>
            </div>
        </div>
    `;
		document.body.insertAdjacentHTML('beforeend', modalMetaContent);
		const modalMeta = document.getElementById('modalCriarMeta');
		const closeModalButtonMeta = modalMeta.querySelector('.close');

		closeModalButtonMeta.addEventListener('click', () => {
			modalMeta.remove();
		});

		modalMeta.style.display = 'block';

		const submitMetaButton = document.getElementById('submitMeta');
		submitMetaButton.addEventListener('click', () => {
			const metaNome = document.getElementById('metaNome').value;
			const metaDescricao = document.getElementById('metaDescricao').value;

			const data = new URLSearchParams();
			data.append('metaNome', metaNome);
			data.append('metaDescricao', metaDescricao);
			data.append('idAluno', idAluno);

			fetch('MetasServlet', {
				method: 'POST',
				body: data
			})
				.then(response => response.text())
				.then(data => {
					console.log('Resposta do servidor:', data);
					// Se necessário, você pode fazer algo com a resposta do servidor aqui
					// Por exemplo, atualizar a interface do usuário
				})
				.catch(error => {
					console.error('Erro ao enviar a requisição:', error);
				});

			// Após enviar os dados, você pode fechar a modal
			modalMeta.remove();
		});
	}



	function abrirModalEdicao(aluno) {

		const modalContent = `
    <div id="modalEditarAluno" class="modal">
        <div class="modal-content">
            <span class="close">&times;</span>
            <h2>Editar Dados do Aluno</h2>
            <form id="formEditarAluno">
                <label for="nome">Nome:</label>
                <input type="text" id="nome_novo" name="nome_novo" value="${aluno.Nome}">
                 	
                <label for="matricula">Matrícula:</label>
                <input type="text" id="matricula_novo" name="matricula_novo" value="${aluno.Matricula}" disabled>
                
                <label for="endereco">Endereço:</label>
                <input type="text" id="endereco_novo" name="endereco_novo" value="${aluno.Endereco}">
                
                <label for="telefone">Telefone:</label>
                <input type="text" id="telefone_novo" name="telefone_novo" value="${aluno.Telefone}">
                
                <label for="disciplina">Disciplina:</label>
                <input type="text" id="disciplina_novo" name="disciplina_novo" value="${aluno.Disciplina}">
                
                <label for="disciplina">Disciplina:</label>
                <input type="text" id="disciplina_novo" name="disciplina_novo" value="${aluno.Disciplina}">
                
                <label for="tutor">Tutor:</label>
                <input type="text" id="tutor_novo" name="tutor_novo" value="${aluno.Tutor}">
                
                <button type="submit">Salvar</button>
            </form>
        </div>
    </div>
    `;

		document.body.insertAdjacentHTML('beforeend', modalContent);

		const modalEditarAluno = document.getElementById('modalEditarAluno');
		const closeModalButton = modalEditarAluno.querySelector('.close');

		closeModalButton.addEventListener('click', () => {
			modalEditarAluno.remove();
		});

		modalEditarAluno.style.display = 'block';

		const formEditarAluno = document.getElementById('formEditarAluno');
		formEditarAluno.addEventListener('submit', function(event) {
			event.preventDefault();


			const nome = document.getElementById('nome_novo').value;
			const matricula = document.getElementById('matricula_novo').value;
			const endereco = document.getElementById('endereco_novo').value;
			const telefone = document.getElementById('telefone_novo').value;
			const disciplina = document.getElementById('disciplina_novo').value;
			const tutor = document.getElementById('tutor_novo').value;

			const alunoData = {
				id: aluno.id,
				nome: nome,
				matricula: matricula,
				endereco: endereco,
				telefone: telefone,
				disciplina: disciplina,
				tutor: tutor
			};

			console.log(alunoData);

			fetch('CardAtualizar', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json; charset=utf-8'
				},
				body: JSON.stringify(alunoData)
			})
				.then(response => response.text())
				.then(data => {
					console.log(data);
					modalEditarAluno.remove();
					window.location.reload();
				})
				.catch(error => {
					console.error('Erro:', error);
				});
		});
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
                <i class="fas fa-trash-alt delete-icon"></i>
                <i class="fas fa-pencil-alt edit-icon"></i>
                <hr>
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

		const editIcon = modal.querySelector('.edit-icon');
		editIcon.addEventListener('click', function() {

			abrirModalPEIEdicao(pei, aluno);
		});


		const deleteIcon = modal.querySelector('.delete-icon');
		deleteIcon.addEventListener('click', function() {
			const confirmDelete = confirm("Tem certeza que deseja excluir este PEI?");
			if (confirmDelete) {

				fetch('CardPEIExcluir?idPEI=' + encodeURIComponent(idPEI) + '&id_aluno=' + encodeURIComponent(aluno.id), {
					method: 'POST',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					}
				})
					.then(response => response.text())
					.then(data => {
						console.log(data);
						window.location.reload();
					})
					.catch(error => {
						console.error('Erro:', error);
					});
			}
		});

	}

	function abrirModalPEIEdicao(pei, aluno) {
		const modalContent = `
        <div id="modalEditarPEI" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Editar Detalhes do PEI</h2>
                <form id="formEditarPEI">
                    <label for="conteudoPragmatico">Conteúdo Pragmático:</label>
                    <input type="text" id="conteudoPragmatico_novo" name="conteudoPragmatico_novo" value="${pei.conteudoPragmatico}">
                    
                    <label for="metodologia">Metodologia:</label>
                    <input type="text" id="metodologia_novo" name="metodologia_novo" value="${pei.metodologia}">
                    
                    <label for="avaliacao">Avaliação:</label>
                    <input type="text" id="avaliacao_novo" name="avaliacao_novo" value="${pei.avaliacao}">
                    
                    <label for="objetivosEspecificos">Objetivos Específicos:</label>
                    <input type="text" id="objetivosEspecificos_novo" name="objetivosEspecificos_novo" value="${pei.objetivosEspecificos}">
                    
                    <label for="habilidade">Habilidade:</label>
                    <input type="text" id="habilidade_novo" name="habilidade_novo" value="${pei.habilidade}">
                    
                    <label for="condicao">Condição:</label>
                    <input type="text" id="condicao_novo" name="condicao_novo" value="${pei.condicao}">
                    
                    <label for="necessidadeEspecifica">Necessidade Específica:</label>
                    <input type="text" id="necessidadeEspecifica_novo" name="necessidadeEspecifica_novo" value="${pei.necessidadeEspecifica}">
                    
                    <label for="dificuldade">Dificuldade:</label>
                    <input type="text" id="dificuldade_novo" name="dificuldade_novo" value="${pei.dificuldade}">
                    
                    <label for="conhecimento">Conhecimento:</label>
                    <input type="text" id="conhecimento_novo" name="conhecimento_novo" value="${pei.conhecimento}">
                    
                    <label for="dataInicio">Data Início:</label>
                    <input type="date" id="dataInicio_novo" name="dataInicio_novo" value="${pei.dataInicio}">
                    
                    <label for="dataFinal">Data Final:</label>
                    <input type="date" id="dataFinal_novo" name="dataFinal_novo" value="${pei.dataFinal}">
                    
                    <button type="submit">Salvar</button>
                </form>
            </div>
        </div>
    `;

		document.body.insertAdjacentHTML('beforeend', modalContent);

		const modalEditarPEI = document.getElementById('modalEditarPEI');
		const closeModalButton = modalEditarPEI.querySelector('.close');

		closeModalButton.addEventListener('click', () => {
			modalEditarPEI.remove();
		});

		modalEditarPEI.style.display = 'block';

		const formEditarPEI = document.getElementById('formEditarPEI');
		formEditarPEI.addEventListener('submit', function(event) {
			event.preventDefault();

			const conteudoPragmatico = document.getElementById('conteudoPragmatico_novo').value;
			const metodologia = document.getElementById('metodologia_novo').value;
			const avaliacao = document.getElementById('avaliacao_novo').value;
			const objetivosEspecificos = document.getElementById('objetivosEspecificos_novo').value;
			const habilidade = document.getElementById('habilidade_novo').value;
			const condicao = document.getElementById('condicao_novo').value;
			const necessidadeEspecifica = document.getElementById('necessidadeEspecifica_novo').value;
			const dificuldade = document.getElementById('dificuldade_novo').value;
			const conhecimento = document.getElementById('conhecimento_novo').value;
			const dataInicio = document.getElementById('dataInicio_novo').value;
			const dataFinal = document.getElementById('dataFinal_novo').value;

			const peiData = {
				id: pei.id,
				conteudoPragmatico: conteudoPragmatico,
				metodologia: metodologia,
				avaliacao: avaliacao,
				objetivosEspecificos: objetivosEspecificos,
				habilidade: habilidade,
				condicao: condicao,
				necessidadeEspecifica: necessidadeEspecifica,
				dificuldade: dificuldade,
				conhecimento: conhecimento,
				dataInicio: dataInicio,
				dataFinal: dataFinal
			};

			fetch('SalvarEdicaoPEI', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json; charset=utf-8'
				},
				body: JSON.stringify(peiData)
			})
				.then(response => response.text())
				.then(data => {
					console.log(data);
					modalEditarPEI.remove();
					window.location.reload();
				})
				.catch(error => {
					console.error('Erro:', error);
				});
		});
	}




	function abrirModalPEI(aluno) {
		const idAluno = aluno.id;

		if (idAluno !== undefined) {
			document.getElementById("idAluno").value = idAluno;


			const modalPEI = document.getElementById('modalPEI');
			modalPEI.style.display = 'block';


			const closeModalButtonPEI = modalPEI.querySelector('.close');
			closeModalButtonPEI.addEventListener('click', () => {
				modalPEI.style.display = 'none';
			});

		} else {
			console.error('ID do aluno não definido.');
		}
	}


	const cadastrarProfessorLink = document.getElementById('cadastrarProfessor');
	const visualizarProfessorLink = document.getElementById('visualizarProfessor');
	const modalCadastroProfessor = document.getElementById('modalCadastroProfessor');
	const closeModalButton1 = modalCadastroProfessor.querySelector('.close');


	cadastrarProfessorLink.addEventListener('click', function() {
		modalCadastroProfessor.style.display = 'block';
	});


	visualizarProfessorLink.addEventListener('click', visualizarProfessor);


	closeModalButton1.addEventListener('click', function() {
		modalCadastroProfessor.style.display = 'none';
	});

	function visualizarProfessor() {
		fetch('CardProfessor', {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json; charset=utf-8'
			}
		})
			.then(response => response.json())
			.then(data => {
				const content = document.querySelector('.content');
				content.innerHTML = '';
				if (data.hasOwnProperty('professores') && Array.isArray(data.professores)) {
					data.professores.forEach(professor => {
						const professorInfo = document.createElement('div');
						professorInfo.classList.add('professor-info');

						professorInfo.innerHTML = `
                    <p><strong>Nome:</strong> ${professor.Nome}</p>
                    <p><strong>Área de Atuação:</strong> ${professor.Area}</p>
                    <button class="visualizarProfessor">Visualizar Informações</button>
                `;

						content.appendChild(professorInfo);

						const visualizarInfoBtn = professorInfo.querySelector('.visualizarProfessor');
						visualizarInfoBtn.addEventListener('click', function() {
							abrirModalProfessor(professor);
						});
					});
				} else {
					console.error('Os dados retornados não contêm um array de professores:', data);
				}
			})
			.catch(error => {
				console.error('Erro:', error);
			});
	}

	function abrirModalProfessor(professor) {
		const modalContent = `
        <div id="modalProfessorInformacoes" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Informações Completas do Professor</h2>
                <div id="professorDetailsContainer">
                    <p><strong>Nome:</strong> ${professor.Nome}</p>
                    <p><strong>Área de Atuação:</strong> ${professor.Area}</p>
                    <p><strong>Endereço:</strong> ${professor.Endereco}</p>
                    <p><strong>Telefone:</strong> ${professor.Telefone}</p>
                    <p><strong>Email:</strong> ${professor.Email}</p>
                    <div class="icons-container">
                        <i class="fas fa-pencil-alt edit-icon"></i>
                        <i class="fas fa-trash-alt delete-icon"></i>
                    </div>
                </div>
            </div>
        </div>
    `;

		document.body.insertAdjacentHTML('beforeend', modalContent);

		const modal = document.getElementById('modalProfessorInformacoes');
		const closeModalButton = modal.querySelector('.close');

		closeModalButton.addEventListener('click', () => {
			modal.remove();
		});

		modal.style.display = 'block';


		const editIcon = document.querySelector('.edit-icon');
		editIcon.addEventListener('click', function() {

			abrirModalEdicaoProfessor(professor);
		});


		const deleteIcon = document.querySelector('.delete-icon');
		deleteIcon.addEventListener('click', function() {
			const confirmDelete = confirm("Tem certeza que deseja excluir este professor?");
			if (confirmDelete) {
				const idProfessor = professor.id;

				fetch('CardProfessor', {
					method: 'POST',
					headers: {
						'Content-Type': 'application/x-www-form-urlencoded'
					},
					body: 'idProfessor=' + encodeURIComponent(idProfessor)
				})
					.then(response => response.text())
					.then(data => {

						console.log(data);
						window.location.reload();
					})
					.catch(error => {
						console.error('Erro:', error);
					});
			}
		});
	}

	function abrirModalEdicaoProfessor(professor) {
		const modalContent = `
        <div id="modalEditarProfessor" class="modal">
            <div class="modal-content">
                <span class="close">&times;</span>
                <h2>Editar Dados do Professor</h2>
                <form id="formEditarProfessor">
                    <label for="nome">Nome:</label>
                    <input type="text" id="nome_novo" name="nome_novo" value="${professor.Nome}">
                    
                    <label for="area">Área de Atuação:</label>
                    <input type="text" id="area_nova" name="area_nova" value="${professor.Area}">
                    
                    <label for="endereco">Endereço:</label>
                    <input type="text" id="endereco_novo" name="endereco_novo" value="${professor.Endereco}">
                    
                    <label for="telefone">Telefone:</label>
                    <input type="text" id="telefone_novo" name="telefone_novo" value="${professor.Telefone}">
                    
                    <label for="email">Email:</label>
                    <input type="email" id="email_novo" name="email_novo" value="${professor.Email}" disabled>
                    
                    <label for="senha">Senha:</label>
                    <input type="text" id="senha_nova" name="senha_nova" value="${professor.Senha}">


                    
                    
                    <button type="submit">Salvar</button>
                </form>
            </div>
        </div>
    `;

		document.body.insertAdjacentHTML('beforeend', modalContent);

		const modalEditarProfessor = document.getElementById('modalEditarProfessor');
		const closeModalButton = modalEditarProfessor.querySelector('.close');

		closeModalButton.addEventListener('click', () => {
			modalEditarProfessor.remove();
		});

		modalEditarProfessor.style.display = 'block';

		const formEditarProfessor = document.getElementById('formEditarProfessor');
		formEditarProfessor.addEventListener('submit', function(event) {
			event.preventDefault();
			const nome = document.getElementById('nome_novo').value;
			const area = document.getElementById('area_nova').value;
			const endereco = document.getElementById('endereco_novo').value;
			const telefone = document.getElementById('telefone_novo').value;
			const email = document.getElementById('email_novo').value;
			const senha = document.getElementById('senha_nova').value;

			const professorData = {
				id: professor.id,
				nome: nome,
				area: area,
				endereco: endereco,
				telefone: telefone,
				email: email,
				senha: senha

			};
			console.log(professorData);

			fetch('CardAtualizarProfessor', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json; charset=utf-8'
				},
				body: JSON.stringify(professorData)
			})
				.then(response => response.text())
				.then(data => {
					console.log(data);
					modalEditarProfessor.remove();
					window.location.reload();
				})
				.catch(error => {
					console.error('Erro:', error);
				});
		});
	}



});
