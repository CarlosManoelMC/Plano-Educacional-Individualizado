package Model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ModelEntidade.Aluno;
import ModelEntidade.PEI;
import ModelEntidade.Professor;

public class DAO {
	private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
	private static final String URL = "jdbc:mysql://localhost:3306/educacaoConectada?useUnicode=true&characterEncoding=UTF-8";
	private static final String USER = "root";
	private static final String PASSWORD = "130544"; // sua senha

	static {
		try {
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			System.out.println("Erro ao carregar o driver do banco de dados.");
			e.printStackTrace();
		}
	}

	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(URL, USER, PASSWORD);
	}

	public boolean checkEmailExists(String email) {
		String sql = "SELECT COUNT(*) FROM Usuario WHERE email = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, email);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					int count = rs.getInt(1);
					return count > 0;
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao verificar e-mail: " + e.getMessage());
		}
		return false;
	}

	public String autenticarUsuario(String email, String senha) {
		String sql = "SELECT u.id_pessoa, p.nivelDeUsuario FROM Usuario u "
				+ "INNER JOIN Pessoa p ON u.id_pessoa = p.id " + "WHERE u.email = ? AND u.senha = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			stmt.setString(2, senha);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {

					return rs.getString("nivelDeUsuario");
				} else {
					return null;
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao autenticar usuário: " + e.getMessage());
			return null;
		}
	}

	public boolean inserirMeta(Aluno aluno, int idAluno) {
		String sql = "	insert into metas (titulo, id_aluno) value (?, ?)";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, aluno.getMeta());
			stmt.setInt(2, idAluno);
			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println("Erro ao inserir metas no banco de dados: " + e.getMessage());
			return false;
		}
	}

	public boolean inserirAluno(Aluno aluno) {
		String sqlInserirPessoa = "INSERT INTO Pessoa (nome, telefone, endereco, nivelDeUsuario) VALUES (?, ?, ?, ?)";
		String sqlInserirAluno = "INSERT INTO Aluno (id_pessoa, historicoEscolar, matricula, disciplina, tutor) VALUES (?, ?, ?, ?, ?)";

		try (Connection conn = getConnection();
				PreparedStatement stmtPessoa = conn.prepareStatement(sqlInserirPessoa, Statement.RETURN_GENERATED_KEYS);
				PreparedStatement stmtAluno = conn.prepareStatement(sqlInserirAluno)) {

			// Inserir na tabela Pessoa
			stmtPessoa.setString(1, aluno.getNome());
			stmtPessoa.setString(2, aluno.getTelefone());
			stmtPessoa.setString(3, aluno.getEndereco());
			stmtPessoa.setString(4, aluno.getNiveldeusuario());

			int linhasAfetadasPessoa = stmtPessoa.executeUpdate();

			if (linhasAfetadasPessoa == 0) {
				throw new SQLException("A inserção na tabela Pessoa falhou, não houve linhas afetadas.");
			}

			try (ResultSet generatedKeys = stmtPessoa.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int idPessoa = generatedKeys.getInt(1);

					stmtAluno.setInt(1, idPessoa);
					stmtAluno.setString(2, aluno.getHistoricoEscolar());
					stmtAluno.setString(3, aluno.getMatricula());
					stmtAluno.setString(4, aluno.getDisciplina());
					stmtAluno.setString(5, aluno.getTutor());

					int linhasAfetadasAluno = stmtAluno.executeUpdate();
					return linhasAfetadasAluno > 0;
				} else {
					throw new SQLException("Falha ao obter o ID gerado para a tabela Pessoa.");
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao inserir aluno no banco de dados: " + e.getMessage());
			return false;
		}
	}

	public List<Aluno> buscarTodosAlunos() {
		List<Aluno> alunos = new ArrayList<>();
		String sql = "SELECT Aluno.*, Pessoa.* FROM Aluno INNER JOIN Pessoa ON Aluno.id_pessoa = Pessoa.id WHERE ativo = 1 ORDER BY Pessoa.nome";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String nome = rs.getString("Pessoa.nome");
				String telefone = rs.getString("Pessoa.telefone");
				String endereco = rs.getString("Pessoa.endereco");
				String nivelDeUsuario = rs.getString("Pessoa.nivelDeUsuario");
				int id = rs.getInt("Aluno.id");
				String matricula = rs.getString("Aluno.matricula");
				String disciplina = rs.getString("Aluno.disciplina");
				String tutor = rs.getString("Aluno.tutor");

				Aluno aluno = new Aluno(nome, telefone, endereco, nivelDeUsuario, id, matricula, disciplina, tutor);
				alunos.add(aluno);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar alunos no banco de dados: " + e.getMessage());
		}

		return alunos;
	}

	public boolean atualizarAluno(Aluno aluno) {
		String query = "UPDATE Aluno a JOIN Pessoa p ON a.id_pessoa = p.id SET p.nome=?, p.endereco=?, p.telefone=?, a.disciplina=?, a.matricula=?, a.tutor = ? WHERE a.id=?";
		try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(query)) {
			statement.setString(1, aluno.getNome());
			statement.setString(2, aluno.getEndereco());
			statement.setString(3, aluno.getTelefone());
			statement.setString(4, aluno.getDisciplina());
			statement.setString(5, aluno.getMatricula());
			statement.setString(6, aluno.getTutor());
			statement.setInt(7, aluno.getId());
			int rowsUpdated = statement.executeUpdate();
			return rowsUpdated > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean desativarAluno(int idAluno) {
		String sql = "UPDATE Aluno SET ativo = 0 WHERE id = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idAluno);
			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println("Erro ao desativar aluno no banco de dados: " + e.getMessage());
			return false;
		}
	}

	public boolean inserirPEI(PEI pei, int idAluno) {
		String sql = "INSERT INTO PEI (ConteudoPragmatico, Metodologia, Avaliacao, ObjetivosEspecificos, "
				+ "HabilidadeAluno, CondicaoAluno, NecessidadesEspecificas, ConhecimentosAluno, DificuldadesAluno, "
				+ "id_aluno, dataInicio, dataFinal) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, pei.getConteudoPragmatico());
			stmt.setString(2, pei.getMetodologia());
			stmt.setString(3, pei.getAvaliacao());
			stmt.setString(4, pei.getObjetivosEspecificos());
			stmt.setString(5, pei.getHabilidade());
			stmt.setString(6, pei.getCondicao());
			stmt.setString(7, pei.getNecessidadeEspecifica());
			stmt.setString(8, pei.getConhecimento());
			stmt.setString(9, pei.getDificuldade());
			stmt.setInt(10, idAluno);

			java.sql.Date dataInicioSql = new java.sql.Date(pei.getDataInicio().getTime());
			java.sql.Date dataFinalSql = new java.sql.Date(pei.getDataFinal().getTime());

			stmt.setDate(11, dataInicioSql);
			stmt.setDate(12, dataFinalSql);

			int rowsInserted = stmt.executeUpdate();
			return rowsInserted > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao inserir PEI no banco de dados: " + e.getMessage());
			return false;
		}
	}

	public List<PEI> buscarPEI(int idAluno) {
		List<PEI> listaPEIs = new ArrayList<>();
		String sql = "SELECT * FROM PEI WHERE id_aluno = ? AND ativo = 1";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idAluno);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {

					int idPEI = rs.getInt("id");
					System.out.println("Id do pei:" + idPEI);
					String conteudoPragmatico = rs.getString("ConteudoPragmatico");
					String metodologia = rs.getString("Metodologia");
					String avaliacao = rs.getString("Avaliacao");
					String objetivosEspecificos = rs.getString("ObjetivosEspecificos");
					String habilidade = rs.getString("HabilidadeAluno");
					String condicao = rs.getString("CondicaoAluno");
					String necessidadeEspecifica = rs.getString("NecessidadesEspecificas");
					String dificuldade = rs.getString("DificuldadesAluno");
					String conhecimento = rs.getString("ConhecimentosAluno");
					Date dataInicio = rs.getDate("dataInicio");
					Date dataFinal = rs.getDate("dataFinal");

					// Cria um objeto PEI e adiciona à lista
					PEI pei = new PEI(idPEI, conteudoPragmatico, metodologia, avaliacao, objetivosEspecificos,
							habilidade, condicao, necessidadeEspecifica, dificuldade, conhecimento, dataInicio,
							dataFinal);
					listaPEIs.add(pei);

				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar PEI do aluno: " + e.getMessage());
		}

		return listaPEIs;
	}

	public boolean desativarPEIAluno(int idAluno, int idPEI) {
		String sql = "UPDATE PEI SET ativo = ? WHERE id_aluno = ? AND id = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, 0);
			stmt.setInt(2, idAluno);
			stmt.setInt(3, idPEI);

			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao desativar PEIs do aluno: " + e.getMessage());
			return false;
		}
	}

	public boolean atualizarPEI(PEI pei) {
		String sql = "UPDATE PEI SET ConteudoPragmatico = ?, " + "Metodologia = ?, " + "Avaliacao = ?, "
				+ "ObjetivosEspecificos = ?, " + "HabilidadeAluno = ?, " + "CondicaoAluno = ?, "
				+ "NecessidadesEspecificas = ?, " + "ConhecimentosAluno = ?, " + "DificuldadesAluno = ?, "
				+ "dataInicio = ?, " + "dataFinal = ? " + "WHERE id = ?;";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, pei.getConteudoPragmatico());
			stmt.setString(2, pei.getMetodologia());
			stmt.setString(3, pei.getAvaliacao());
			stmt.setString(4, pei.getObjetivosEspecificos());
			stmt.setString(5, pei.getHabilidade());
			stmt.setString(6, pei.getCondicao());
			stmt.setString(7, pei.getNecessidadeEspecifica());
			stmt.setString(8, pei.getDificuldade());
			stmt.setString(9, pei.getConhecimento());
			stmt.setDate(10, new java.sql.Date(pei.getDataInicio().getTime()));
			stmt.setDate(11, new java.sql.Date(pei.getDataFinal().getTime()));
			stmt.setInt(12, pei.getId());

			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao atualizar PEI: " + e.getMessage());
			return false;
		}
	}

	public boolean inserirProfessor(Professor professor) {

		String sqlPessoa = "INSERT INTO Pessoa (nome, telefone, endereco, nivelDeUsuario) VALUES (?, ?, ?, ?)";

		String sqlUsuario = "INSERT INTO Usuario (email, senha, id_pessoa) VALUES (?, ?, ?)";

		String sqlProfissional = "INSERT INTO Profissional (area, id_pessoa, id_usuario) VALUES (?, ?, ?)";

		try (Connection conn = getConnection();
				PreparedStatement stmtPessoa = conn.prepareStatement(sqlPessoa,
						PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement stmtUsuario = conn.prepareStatement(sqlUsuario,
						PreparedStatement.RETURN_GENERATED_KEYS);
				PreparedStatement stmtProfissional = conn.prepareStatement(sqlProfissional)) {

			stmtPessoa.setString(1, professor.getNome());
			stmtPessoa.setString(2, professor.getTelefone());
			stmtPessoa.setString(3, professor.getEndereco());
			stmtPessoa.setString(4, professor.getNiveldeusuario());
			stmtPessoa.executeUpdate();

			int idPessoa;
			try (ResultSet generatedKeys = stmtPessoa.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					idPessoa = generatedKeys.getInt(1);
				} else {
					throw new SQLException("Falha ao obter o ID da pessoa.");
				}
			}

			stmtUsuario.setString(1, professor.getEmail());
			stmtUsuario.setString(2, professor.getSenha());
			stmtUsuario.setInt(3, idPessoa);
			stmtUsuario.executeUpdate();

			int idUsuario;
			try (ResultSet generatedKeys = stmtUsuario.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					idUsuario = generatedKeys.getInt(1);
				} else {
					throw new SQLException("Falha ao obter o ID do usuário.");
				}
			}

			stmtProfissional.setString(1, professor.getArea());
			stmtProfissional.setInt(2, idPessoa);
			stmtProfissional.setInt(3, idUsuario);
			int linhasAfetadas = stmtProfissional.executeUpdate();

			return linhasAfetadas > 0;
		} catch (SQLException e) {

			System.out.println("Erro ao inserir profissional: " + e.getMessage());
			return false;
		}
	}

	public List<Professor> buscarTodosProfessores() {
		List<Professor> professores = new ArrayList<>();
		String sql = "SELECT Profissional.*, Pessoa.*, Usuario.email, Usuario.senha " + "FROM Profissional "
				+ "INNER JOIN Usuario ON Profissional.id_usuario = Usuario.id "
				+ "INNER JOIN Pessoa ON Usuario.id_pessoa = Pessoa.id " + "WHERE ativo = 1 ORDER BY Pessoa.nome";

		try (Connection conn = getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql);
				ResultSet rs = stmt.executeQuery()) {

			while (rs.next()) {
				String nome = rs.getString("Pessoa.nome");
				String telefone = rs.getString("Pessoa.telefone");
				String endereco = rs.getString("Pessoa.endereco");
				String nivelDeUsuario = rs.getString("Pessoa.nivelDeUsuario");
				int id = rs.getInt("Profissional.id");
				String area = rs.getString("Profissional.area");
				String email = rs.getString("Usuario.email");
				String senha = rs.getString("Usuario.senha");

				Professor professor = new Professor(nome, telefone, endereco, nivelDeUsuario, id, area, email, senha);
				professores.add(professor);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar professores no banco de dados: " + e.getMessage());
		}

		return professores;
	}

	public boolean desativarProfessor(int idProfessor) {

		String sql = "UPDATE Profissional SET ativo = 0 WHERE id = ?";

		System.out.println("Entrou na dao");

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, idProfessor);

			int linhasAfetadas = stmt.executeUpdate();

			return linhasAfetadas > 0;
		} catch (SQLException e) {

			System.out.println("Erro ao desativar professor: " + e.getMessage());
			return false;
		}
	}

	public boolean atualizarProfessor(Professor professor) {
		String sqlUpdatePessoa = "UPDATE Pessoa p INNER JOIN Profissional pr ON pr.id_pessoa = p.id SET p.nome = ?, p.telefone = ?, p.endereco = ? WHERE pr.id = ?";
		String sqlUpdateUsuario = "UPDATE Usuario u INNER JOIN Profissional pr ON pr.id_usuario = u.id SET u.email = ?, u.senha = ? WHERE pr.id = ?";
		String sqlUpdateProfissional = "UPDATE Profissional SET area = ? WHERE id = ?";

		try (Connection conn = getConnection();
				PreparedStatement stmtUpdatePessoa = conn.prepareStatement(sqlUpdatePessoa);
				PreparedStatement stmtUpdateUsuario = conn.prepareStatement(sqlUpdateUsuario);
				PreparedStatement stmtUpdateProfissional = conn.prepareStatement(sqlUpdateProfissional)) {

			stmtUpdatePessoa.setString(1, professor.getNome());
			stmtUpdatePessoa.setString(2, professor.getTelefone());
			stmtUpdatePessoa.setString(3, professor.getEndereco());
			stmtUpdatePessoa.setInt(4, professor.getId());
			int linhasAfetadasPessoa = stmtUpdatePessoa.executeUpdate();

			stmtUpdateUsuario.setString(1, professor.getEmail());
			stmtUpdateUsuario.setString(2, professor.getSenha());
			stmtUpdateUsuario.setInt(3, professor.getId());
			int linhasAfetadasUsuario = stmtUpdateUsuario.executeUpdate();

			stmtUpdateProfissional.setString(1, professor.getArea());
			stmtUpdateProfissional.setInt(2, professor.getId());
			int linhasAfetadasProfissional = stmtUpdateProfissional.executeUpdate();

			return linhasAfetadasPessoa > 0 && linhasAfetadasUsuario > 0 && linhasAfetadasProfissional > 0;

		} catch (SQLException e) {

			System.out.println("Erro ao atualizar professor: " + e.getMessage());
			return false;
		}
	}

	public boolean inserirMeta(String titulo, int idAluno) {
		String sql = "INSERT INTO metas (titulo, id_aluno) VALUES (?, ?)";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, titulo);
			stmt.setInt(2, idAluno);
			int linhasAfetadas = stmt.executeUpdate();
			return linhasAfetadas > 0;
		} catch (SQLException e) {
			System.out.println("Erro ao inserir meta no banco de dados: " + e.getMessage());
			return false;
		}
	}

	public List<String> buscarMetasDoAluno(int idAluno) {
		List<String> metas = new ArrayList<>();
		String sql = "SELECT titulo FROM metas WHERE id_aluno = ?";

		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, idAluno);

			try (ResultSet rs = stmt.executeQuery()) {
				while (rs.next()) {
					String tituloMeta = rs.getString("titulo");
					metas.add(tituloMeta);
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao buscar metas do aluno: " + e.getMessage());
		}

		return metas;
	}

}
