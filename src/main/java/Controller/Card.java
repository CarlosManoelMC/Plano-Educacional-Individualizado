package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ModelEntidade.Aluno;

/**
 * Servlet implementation class Card
 */
@WebServlet("/Card")
public class Card extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Card() {
		super();
		// TODO Auto-generated constructor stub
	}

	private String buildJsonResponse(String status, String message, String locationsJson) {
		String jsonResponse = "{" + "\"status\":\"" + status + "\"," + "\"message\":\"" + message + "\","
				+ "\"locations\":" + locationsJson + "}";
		return jsonResponse;
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();

		
		List<Aluno> alunos = Aluno.buscarTodosAlunos();

		String jsonResponse = buildJsonResponse("success", "Alunos recuperados com sucesso.",
				convertAlunosToJson(alunos));
		System.out.println(jsonResponse);
		out.println(jsonResponse);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int idAluno = Integer.parseInt(request.getParameter("idAluno"));

		Aluno aluno = new Aluno();
		boolean excluiu = aluno.desativarAluno(idAluno);

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		if (excluiu) {
			response.getWriter().write("Aluno excluído com sucesso");
		} else {
			response.getWriter().write("Erro ao excluir aluno");
		}
	}

	private static String escapeJsonString(String input) {
		if (input == null) {
			return "";
		}

		StringBuilder result = new StringBuilder();
		for (char c : input.toCharArray()) {
			switch (c) {
			case '"':
				result.append("\\\"");
				break;
			case '\\':
				result.append("\\\\");
				break;
			case '\b':
				result.append("\\b");
				break;
			case '\f':
				result.append("\\f");
				break;
			case '\n':
				result.append("\\n");
				break;
			case '\r':
				result.append("\\r");
				break;
			case '\t':
				result.append("\\t");
				break;
			default:
				result.append(c);
			}
		}
		return result.toString();
	}

	public static String convertAlunosToJson(List<Aluno> alunos) {
		StringBuilder jsonBuilder = new StringBuilder("[");
		boolean first = true;

		for (Aluno aluno : alunos) {
			if (!first) {
				jsonBuilder.append(",");
			} else {
				first = false;
			}

			jsonBuilder.append("{").append("\"id\":\"" + aluno.getId() + "\",")
					.append("\"Nome\":\"" + escapeJsonString(aluno.getNome()) + "\",")
					.append("\"Telefone\":\"" + escapeJsonString(aluno.getTelefone()) + "\",")
					.append("\"Endereco\":\"" + escapeJsonString(aluno.getEndereco()) + "\",")
					.append("\"NivelDeUsuario\":\"" + escapeJsonString(aluno.getNiveldeusuario()) + "\",")
					.append("\"Matricula\":\"" + escapeJsonString(aluno.getMatricula()) + "\",")
					.append("\"HistoricoEscolar\":\"" + escapeJsonString(aluno.getHistoricoEscolar()) + "\",")
					.append("\"Tutor\":\"" + escapeJsonString(aluno.getTutor()) + "\",")
					.append("\"Disciplina\":\"" + escapeJsonString(aluno.getDisciplina()) + "\"").append("}");
		}

		jsonBuilder.append("]");

		return jsonBuilder.toString();
	}

}
