package ModelEntidade;

import java.util.List;

public abstract class Usuario extends Pessoa{
	private String email;
	private String senha;
	
	public Usuario(String nome, String telefone, String endereco, String niveldeusuario, String email, String senha) {
		super(nome, telefone, endereco, niveldeusuario);
		this.email = email;
		this.senha = senha;
	}
	public Usuario(String nome, String telefone, String endereco, String niveldeusuario) {
		super(nome, telefone, endereco, niveldeusuario);
		
	}
	
	public abstract String apresentarInformacoes();
	
	public Usuario() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	
	
	
}
