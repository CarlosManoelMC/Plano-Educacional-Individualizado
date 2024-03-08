package Controller;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Model.DAO;
import ModelEntidade.Aluno;

/**
 * Servlet implementation class CardAtualizar
 */
@WebServlet("/CardAtualizar")
public class CardAtualizar extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CardAtualizar() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");

		String jsonData = request.getReader().lines().collect(Collectors.joining());
		System.out.println(jsonData);

		
		Gson gson = new Gson();
		Aluno aluno = gson.fromJson(jsonData, Aluno.class);

		try {
			
			boolean sucesso = aluno.atualizarDadosAluno();

			if (sucesso) {
				response.getWriter().write("Atualização do aluno realizada com sucesso!");
			} else {
				response.getWriter().write("Falha ao atualizar o aluno.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().write("Erro ao processar a requisição: " + e.getMessage());
		}
	}

}
