package Controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Model.DAO;
import ModelEntidade.Metas;

/**
 * Servlet implementation class MetasServlet
 */
@WebServlet("/MetasServlet")
public class MetasServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MetasServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    // Obtém o ID do aluno a partir dos parâmetros da requisição
	    int idAluno = Integer.parseInt(request.getParameter("idAluno"));

	    // Chama o método estático da classe Metas para buscar as metas do aluno com o ID fornecido
	    List<String> metasDoAluno = Metas.buscarTodasMetas(idAluno);

	    // Converte a lista de metas em JSON usando Gson
	    Gson gson = new Gson();
	    String jsonMetas = gson.toJson(metasDoAluno);

	    // Define o tipo de conteúdo da resposta como JSON
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    // Escreve a lista de metas como JSON na resposta
	    response.getWriter().write(jsonMetas);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");

		// Obtém os parâmetros enviados pela requisição AJAX
		String metaNome = request.getParameter("metaNome");
		String metaDescricao = request.getParameter("metaDescricao");
		String idAluno = request.getParameter("idAluno");
		System.out.println("id do aluno:" + idAluno);
		// Verifica se o ID do aluno não é nulo e não está vazio
		if (idAluno != null && !idAluno.isEmpty()) {
			// Converte o ID do aluno para um número inteiro
			int alunoId = Integer.parseInt(idAluno);

			// Chama o método para inserir a meta no banco de dados
			DAO dao = new DAO();
			boolean insercaoSucesso = dao.inserirMeta(metaNome, alunoId);

			// Retorna uma resposta para o cliente, indicando se a inserção foi bem-sucedida
			PrintWriter out = response.getWriter();
			if (insercaoSucesso) {
				out.println("Meta inserida com sucesso para o aluno com ID " + alunoId);
			} else {
				out.println("Erro ao inserir meta para o aluno com ID " + alunoId);
			}
		} else {
			// Se o ID do aluno estiver ausente ou vazio, retorna uma resposta de erro
			PrintWriter out = response.getWriter();
			out.println("ID do aluno não fornecido ou inválido.");
		}
	}

}
