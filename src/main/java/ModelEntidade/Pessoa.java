package ModelEntidade;

public abstract class Pessoa {
	 	private String nome;
	    private String telefone;
	    private String endereco;
	    private String niveldeusuario;
	    
		public Pessoa(String nome, String telefone, String endereco, String niveldeusuario) {
			this.nome = nome;
			this.telefone = telefone;
			this.endereco = endereco;
			this.niveldeusuario = niveldeusuario;
		}
		
		public Pessoa(String nome, String telefone, String endereco) {
			this.nome = nome;
			this.telefone = telefone;
			this.endereco = endereco;
	
		}
		
		
		public Pessoa() {
			super();
			// TODO Auto-generated constructor stub
		}
		public String getNome() {
			return nome;
		}
		public void setNome(String nome) {
			this.nome = nome;
		}
		public String getTelefone() {
			return telefone;
		}
		public void setTelefone(String telefone) {
			this.telefone = telefone;
		}
		public String getEndereco() {
			return endereco;
		}
		public void setEndereco(String endereco) {
			this.endereco = endereco;
		}

		public String getNiveldeusuario() {
			return niveldeusuario;
		}
		public void setNiveldeusuario(String niveldeusuario) {
			this.niveldeusuario = niveldeusuario;
		}
	    
	    
}
