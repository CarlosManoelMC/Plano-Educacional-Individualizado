package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ModelEntidade.PEI;

@WebServlet("/CardPEI")
public class CardPEI extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private String buildJsonResponse(String status, String message, String peisJson) {
		String jsonResponse = "{" + "\"status\":\"" + status + "\"," + "\"message\":\"" + message + "\"," + "\"peis\":"
				+ peisJson + "}";
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

		int idAluno = Integer.parseInt(request.getParameter("id_aluno"));

		PEI pei = new PEI();

		List<PEI> peis = pei.buscarPEI(idAluno);

		String jsonResponse = buildJsonResponse("success", "PEIs recuperados com sucesso.", convertPEIsToJson(peis));
		System.out.println(jsonResponse);
		out.println(jsonResponse);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		String conteudoPragmatico = request.getParameter("conteudoPragmatico");
		String metodologia = request.getParameter("metodologia");
		String avaliacao = request.getParameter("avaliacao");
		String objetivosEspecificos = request.getParameter("objetivosEspecificos");
		String habilidade = request.getParameter("habilidade");
		String condicao = request.getParameter("condicao");
		String necessidadeEspecifica = request.getParameter("necessidadeEspecifica");
		String dificuldade = request.getParameter("dificuldade");
		String conhecimento = request.getParameter("conhecimento");
		String dataInicioString = request.getParameter("dataInicio");
		String dataFinalString = request.getParameter("dataFinal");
		String idAlunoString = request.getParameter("idAluno");

		try {

			if (conteudoPragmatico == null || metodologia == null || avaliacao == null || objetivosEspecificos == null
					|| habilidade == null || condicao == null || necessidadeEspecifica == null || dificuldade == null
					|| conhecimento == null || dataInicioString == null || dataFinalString == null
					|| idAlunoString == null) {
				throw new IllegalArgumentException("Parâmetros inválidos.");
			}

			int idAluno = Integer.parseInt(idAlunoString);

			java.sql.Date dataInicio = java.sql.Date.valueOf(dataInicioString);
			java.sql.Date dataFinal = java.sql.Date.valueOf(dataFinalString);

			PEI pei = new PEI(conteudoPragmatico, metodologia, avaliacao, objetivosEspecificos, habilidade, condicao,
					necessidadeEspecifica, dificuldade, conhecimento, dataInicio, dataFinal);

			boolean inserido = pei.salvarPEI(idAluno);

			String status;
			String message;
			if (inserido) {
				status = "success";
				message = "PEI inserido com sucesso.";
				 response.sendRedirect("home.html");
			} else {
				status = "error";
				message = "Falha ao inserir PEI.";
			}

			String jsonResponse = buildJsonResponse(status, message, null);
			out.println(jsonResponse);
		} catch (NumberFormatException e) {

			String jsonResponse = buildJsonResponse("error", "Formato de número inválido.", null);
			out.println(jsonResponse);
		} catch (IllegalArgumentException e) {

			String jsonResponse = buildJsonResponse("error", "Parâmetros inválidos.", null);
			out.println(jsonResponse);
		}
	}

	public static String convertPEIsToJson(List<PEI> peis) {
		StringBuilder jsonBuilder = new StringBuilder("[");
		boolean first = true;

		for (PEI pei : peis) {
			if (!first) {
				jsonBuilder.append(",");
			} else {
				first = false;
			}

			jsonBuilder.append("{").append("\"id\":\"" + pei.getId() + "\",")
					.append("\"conteudoPragmatico\":\"" + escapeJsonString(pei.getConteudoPragmatico()) + "\",")
					.append("\"metodologia\":\"" + escapeJsonString(pei.getMetodologia()) + "\",")
					.append("\"avaliacao\":\"" + escapeJsonString(pei.getAvaliacao()) + "\",")
					.append("\"objetivosEspecificos\":\"" + escapeJsonString(pei.getObjetivosEspecificos()) + "\",")
					.append("\"habilidade\":\"" + escapeJsonString(pei.getHabilidade()) + "\",")
					.append("\"condicao\":\"" + escapeJsonString(pei.getCondicao()) + "\",")
					.append("\"necessidadeEspecifica\":\"" + escapeJsonString(pei.getNecessidadeEspecifica()) + "\",")
					.append("\"dificuldade\":\"" + escapeJsonString(pei.getDificuldade()) + "\",")
					.append("\"conhecimento\":\"" + escapeJsonString(pei.getConhecimento()) + "\",")
					.append("\"dataInicio\":\"" + pei.getDataInicio() + "\",")
					.append("\"dataFinal\":\"" + pei.getDataFinal() + "\"").append("}");
		}

		jsonBuilder.append("]");

		return jsonBuilder.toString();
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
}
