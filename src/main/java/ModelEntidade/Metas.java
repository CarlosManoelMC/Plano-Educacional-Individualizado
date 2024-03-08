package ModelEntidade;

import java.util.List;

import Model.DAO;

public class Metas {

	private String titulo;
	private String descricao;

	public Metas(String titulo, String descricao) {
		super();
		this.titulo = titulo;
		this.descricao = descricao;
	}

	public Metas() {
		super();
		// TODO Auto-generated constructor stub
	}

	public static List<String> buscarTodasMetas(int idaluno) {
		DAO dao = new DAO();
		return dao.buscarMetasDoAluno(idaluno);
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
