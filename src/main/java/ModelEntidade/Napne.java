package ModelEntidade;

public class Napne extends Usuario {
	private String areaDeEspecializacao;
	

	public Napne(String nome, String telefone, String endereco, String niveldeusuario, String email, String senha,
			String areaDeEspecializacao) {
		super(nome, telefone, endereco, niveldeusuario, email, senha);
		this.areaDeEspecializacao = areaDeEspecializacao;
	}

	public String getAreaDeEspecializacao() {
		return areaDeEspecializacao;
	}

	public void setAreaDeEspecializacao(String areaDeEspecializacao) {
		this.areaDeEspecializacao = areaDeEspecializacao;
	}
	
	
	 @Override
	    public String apresentarInformacoes() {
	        return "Napne: " + getNome() + ", Especialização: " + areaDeEspecializacao;
	    }
	
}
