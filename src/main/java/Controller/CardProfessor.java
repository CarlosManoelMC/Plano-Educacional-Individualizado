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
import ModelEntidade.Professor;

/**
 * Servlet implementation class CardProfessor
 */
@WebServlet("/CardProfessor")
public class CardProfessor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CardProfessor() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// Buscando todos os professores
		List<Professor> professores = Professor.buscarTodosProfessores();

		String jsonResponse = buildJsonResponse("success", "Professores recuperados com sucesso.",
				convertProfessoresToJson(professores));
		out.println(jsonResponse);

	}

	private String buildJsonResponse(String status, String message, String professoresJson) {
		String jsonResponse = "{" + "\"status\":\"" + status + "\"," + "\"message\":\"" + message + "\","
				+ "\"professores\":" + professoresJson + "}";
		return jsonResponse;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		int idProfessor = Integer.parseInt(request.getParameter("idProfessor"));

		System.out.println("Entrou id:" + idProfessor);

		Professor professor = new Professor();

		boolean desativado = professor.desativarProfessor(idProfessor);

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		if (desativado) {

			response.getWriter().write("Professor desativado com sucesso!");
		} else {

			response.getWriter().write("Erro ao desativar o professor. Verifique o registro.");
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

	public static String convertProfessoresToJson(List<Professor> professores) {
		StringBuilder jsonBuilder = new StringBuilder("[");
		boolean first = true;

		for (Professor professor : professores) {
			if (!first) {
				jsonBuilder.append(",");
			} else {
				first = false;
			}

			jsonBuilder.append("{").append("\"id\":\"" + professor.getId() + "\",")
					.append("\"Nome\":\"" + escapeJsonString(professor.getNome()) + "\",")
					.append("\"Telefone\":\"" + escapeJsonString(professor.getTelefone()) + "\",")
					.append("\"Endereco\":\"" + escapeJsonString(professor.getEndereco()) + "\",")
					.append("\"NivelDeUsuario\":\"" + escapeJsonString(professor.getNiveldeusuario()) + "\",")
					.append("\"Email\":\"" + escapeJsonString(professor.getEmail()) + "\",")
					.append("\"Senha\":\"" + escapeJsonString(professor.getSenha()) + "\",")
					.append("\"Area\":\"" + escapeJsonString(professor.getArea()) + "\"").append("}");
		}

		jsonBuilder.append("]");

		return jsonBuilder.toString();
	}

}
