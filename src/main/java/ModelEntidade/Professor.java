package ModelEntidade;

import java.util.List;

import Model.DAO;

public class Professor extends Usuario {
	private int id;
	private String area;

	public Professor(String nome, String telefone, String endereco, String niveldeusuario, String email, String senha,
			String area) {
		super(nome, telefone, endereco, niveldeusuario, email, senha);
		this.area = area;
	}

	public Professor(String nome, String telefone, String endereco, String niveldeusuario, String email, String senha,
			int id, String area) {
		super(nome, telefone, endereco, niveldeusuario, email, senha);
		this.id = id;
		this.area = area;
	}

	public Professor(String nome, String telefone, String endereco, String niveldeusuario, int id, String area) {
		super(nome, telefone, endereco, niveldeusuario);
		this.id = id;
		this.area = area;
	}

	public Professor(String nome, String telefone, String endereco, String niveldeusuario, int id, String area,
			String email, String senha) {
		super(nome, telefone, endereco, niveldeusuario, email, senha);
		this.id = id;
		this.area = area;
	}

	public Professor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean inserirProfessor(Professor professor) {
		if (this != null) {
			DAO dao = new DAO();
			return dao.inserirProfessor(this);
		} else {
			return false;
		}
	}

	public boolean atualizarDadosProfessor() {
		if (this != null) {
			DAO dao = new DAO();
			return dao.atualizarProfessor(this);
		} else {
			return false;
		}
	}

	public static List<Professor> buscarTodosProfessores() {
		DAO dao = new DAO();
		return dao.buscarTodosProfessores();
	}

	public boolean desativarProfessor(int idProfessor) {

		DAO dao = new DAO();
		return dao.desativarProfessor(idProfessor);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	 @Override
	    public String apresentarInformacoes() {
	        return "Professor: " + getNome() + ", Área: " + area;
	    }

}
