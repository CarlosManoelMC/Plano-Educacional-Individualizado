package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ModelEntidade.Professor;

/**
 * Servlet implementation class CadastroProfessor
 */
@WebServlet("/CadastroProfessor")
public class CadastroProfessor extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CadastroProfessor() {
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
		// Define a codificação dos parâmetros recebidos
		request.setCharacterEncoding("UTF-8");

		// Obtém os parâmetros do formulário
		String nome = request.getParameter("nome_professor");
		String endereco = request.getParameter("endereco_professor");
		String telefone = request.getParameter("telefone_professor");
		String areaAtuacao = request.getParameter("area_professor");
		String nivelDeUsuario = request.getParameter("niveldeusuario_professor");
		String email = request.getParameter("email_professor");
		String senha = request.getParameter("senha_professor");

		// Verifica se todos os parâmetros estão presentes
		if (nome != null && endereco != null && telefone != null && areaAtuacao != null && nivelDeUsuario != null
				&& email != null && senha != null) {

			Professor professor = new Professor(nome, telefone, endereco, nivelDeUsuario, email, senha, areaAtuacao);

			// Insere os dados do professor
			boolean inserido = professor.inserirProfessor(professor);

			if (inserido) {

				response.sendRedirect("home.html");
			} else {

				response.sendRedirect("erro.html");
			}
		} else {

			response.sendRedirect("home.html?erro=3");
		}
	}

}
