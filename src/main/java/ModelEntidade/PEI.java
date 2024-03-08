package ModelEntidade;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Model.DAO;

public class PEI {
	private int id;
	private String conteudoPragmatico;
	private String metodologia;
	private String avaliacao;
	private String objetivosEspecificos;
	private String habilidade;
	private String condicao;
	private String necessidadeEspecifica;
	private String dificuldade;
	private String conhecimento;
	private Date dataInicio;
	private Date dataFinal;
	
	

	public PEI(int id, String conteudoPragmatico, String metodologia, String avaliacao, String objetivosEspecificos,
			String habilidade, String condicao, String necessidadeEspecifica, String dificuldade, String conhecimento,
			Date dataInicio, Date dataFinal) {
		this.id = id;
		this.conteudoPragmatico = conteudoPragmatico;
		this.metodologia = metodologia;
		this.avaliacao = avaliacao;
		this.objetivosEspecificos = objetivosEspecificos;
		this.habilidade = habilidade;
		this.condicao = condicao;
		this.necessidadeEspecifica = necessidadeEspecifica;
		this.dificuldade = dificuldade;
		this.conhecimento = conhecimento;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;

	}
	public PEI(String conteudoPragmatico, String metodologia, String avaliacao, String objetivosEspecificos,
			String habilidade, String condicao, String necessidadeEspecifica, String dificuldade, String conhecimento,
			Date dataInicio, Date dataFinal) {
		this.conteudoPragmatico = conteudoPragmatico;
		this.metodologia = metodologia;
		this.avaliacao = avaliacao;
		this.objetivosEspecificos = objetivosEspecificos;
		this.habilidade = habilidade;
		this.condicao = condicao;
		this.necessidadeEspecifica = necessidadeEspecifica;
		this.dificuldade = dificuldade;
		this.conhecimento = conhecimento;
		this.dataInicio = dataInicio;
		this.dataFinal = dataFinal;

	}


	public PEI() {
		super();
		// TODO Auto-generated constructor stub
	}

	public boolean salvarPEI(int idAluno) {
		DAO dao = new DAO();
		return dao.inserirPEI(this, idAluno);
	}

	public static List<PEI> buscarPEI(int idAluno) {
		DAO dao = new DAO();
		return dao.buscarPEI(idAluno);
	}

	public boolean desativarPEIAluno(int idAluno, int idPEI) {
		DAO dao = new DAO();
		return dao.desativarPEIAluno(idAluno, idPEI);
	}
	
	 public boolean atualizarPEI(PEI peiData) {
	        DAO dao = new DAO(); 
	        return dao.atualizarPEI(peiData); }

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getConteudoPragmatico() {
		return conteudoPragmatico;
	}

	public void setConteudoPragmatico(String conteudoPragmatico) {
		this.conteudoPragmatico = conteudoPragmatico;
	}

	public String getMetodologia() {
		return metodologia;
	}

	public void setMetodologia(String metodologia) {
		this.metodologia = metodologia;
	}

	public String getAvaliacao() {
		return avaliacao;
	}

	public void setAvaliacao(String avaliacao) {
		this.avaliacao = avaliacao;
	}

	public String getObjetivosEspecificos() {
		return objetivosEspecificos;
	}

	public void setObjetivosEspecificos(String objetivosEspecificos) {
		this.objetivosEspecificos = objetivosEspecificos;
	}

	public String getHabilidade() {
		return habilidade;
	}

	public void setHabilidade(String habilidade) {
		this.habilidade = habilidade;
	}

	public String getCondicao() {
		return condicao;
	}

	public void setCondicao(String condicao) {
		this.condicao = condicao;
	}

	public String getNecessidadeEspecifica() {
		return necessidadeEspecifica;
	}

	public void setNecessidadeEspecifica(String necessidadeEspecifica) {
		this.necessidadeEspecifica = necessidadeEspecifica;
	}

	public String getDificuldade() {
		return dificuldade;
	}

	public void setDificuldade(String dificuldade) {
		this.dificuldade = dificuldade;
	}

	public String getConhecimento() {
		return conhecimento;
	}

	public void setConhecimento(String conhecimento) {
		this.conhecimento = conhecimento;
	}

}
