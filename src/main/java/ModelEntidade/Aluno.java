package ModelEntidade;

import java.util.List;

import Model.DAO;

public  class Aluno extends Pessoa {
	private int id;
	private String historicoEscolar;
	private String matricula;
	private String disciplina;
	private String tutor;
	private String meta;

	public Aluno(String nome, String telefone, String endereco, String niveldeusuario, int id, String matricula,
			String disciplina, String tutor) {
		super(nome, telefone, endereco, niveldeusuario);
		this.matricula = matricula;
		this.disciplina = disciplina;
		this.id = id;
		this.tutor = tutor;
	}

	public Aluno(String nome, String endereco, String telefone, String niveldeusuario, String matricula,
			String disciplina, String tutor) {
		super(nome, telefone, endereco, niveldeusuario);
		this.matricula = matricula;
		this.disciplina = disciplina;
		this.tutor = tutor;

	}

	public Aluno(String nome, String matricula, String endereco, String telefone, String disciplina) {
		super(nome, telefone, endereco);
		this.matricula = matricula;
		this.disciplina = disciplina;

	}
	public Aluno() {
		super();
		// TODO Auto-generated constructor stub
	}

	
	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	public String getTutor() {
		return tutor;
	}

	public void setTutor(String tutor) {
		this.tutor = tutor;
	}

	public boolean inserirDadosAluno() {
		if (this != null) {
			DAO dao = new DAO();
			return dao.inserirAluno(this);
		} else {
			return false;
		}
	}

	public boolean atualizarDadosAluno() {
		if (this != null) {
			DAO dao = new DAO();
			return dao.atualizarAluno(this);
		} else {
			return false;
		}
	}

	public boolean desativarAluno(int idAluno) {
		DAO dao = new DAO();
		return dao.desativarAluno(idAluno);
	}

	public static List<Aluno> buscarTodosAlunos() {
		DAO dao = new DAO();
		return dao.buscarTodosAlunos();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDisciplina() {
		return disciplina;
	}

	public void setDisciplina(String disciplina) {
		this.disciplina = disciplina;
	}

	public String getHistoricoEscolar() {
		return historicoEscolar;
	}

	public void setHistoricoEscolar(String historicoEscolar) {
		this.historicoEscolar = historicoEscolar;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@Override
	public String toString() {
		return "Aluno [nome=" + getNome() + ", matricula=" + matricula + ", endereco=" + getEndereco() + ", telefone="
				+ getTelefone() + ", disciplina=" + disciplina + "]";
	}

}
