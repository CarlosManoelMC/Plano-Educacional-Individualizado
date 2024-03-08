package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ModelEntidade.PEI;

/**
 * Servlet implementation class CardPEIExcluir
 */
@WebServlet("/CardPEIExcluir")
public class CardPEIExcluir extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CardPEIExcluir() {
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

		System.out.println("entrou");
		int idPEI = Integer.parseInt(request.getParameter("idPEI"));
		int idAluno = Integer.parseInt(request.getParameter("id_aluno"));

		System.out.println("Id pei:" + idPEI + "id aluno: " + idAluno);

		PEI pei = new PEI();

		boolean excluiu = pei.desativarPEIAluno(idAluno, idPEI);

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();

		if (excluiu) {
			out.write("PEI excluído com sucesso");
		} else {
			out.write("Erro ao excluir PEI");
		}
	}
}
