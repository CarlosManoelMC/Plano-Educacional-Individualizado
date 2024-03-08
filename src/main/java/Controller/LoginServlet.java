package Controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Model.DAO;
import ModelEntidade.Aluno;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String matricula = request.getParameter("matricula");
		String senha = request.getParameter("senha");

		if (matricula != null && senha != null) {
			DAO dao = new DAO();

			try {

				String tipoUsuario = dao.autenticarUsuario(matricula, senha);
				if (tipoUsuario != null) {

					if (tipoUsuario.equals("P")) {

						response.sendRedirect("homeProfessor.html");
					} else if (tipoUsuario.equals("N")) {

						response.sendRedirect("home.html");
					} else {

						response.sendRedirect("index.html?erro=3");
					}
				} else {

					response.sendRedirect("index.html?erro=1");
				}
			} catch (Exception e) {
				e.printStackTrace();

			}
		} else {

			response.sendRedirect("index.html?erro=2");
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("UTF-8");

		String nome = request.getParameter("nome");
		String endereco = request.getParameter("endereco");
		String telefone = request.getParameter("telefone");
		String matricula = request.getParameter("matricula");
		String historicoEscolar = request.getParameter("historicoEscolar");
		String nivelDeUsuario = request.getParameter("niveldeusuario");
		String disciplina = request.getParameter("disciplina");
		String tutor = request.getParameter("tutor");

		if (nome != null && endereco != null && telefone != null && matricula != null && nivelDeUsuario != null) {

			Aluno aluno = new Aluno(nome, endereco, telefone, nivelDeUsuario, matricula, disciplina, tutor);
			aluno.setHistoricoEscolar(historicoEscolar);
			aluno.setDisciplina(disciplina);
			aluno.setTutor(tutor);

			boolean inserido = aluno.inserirDadosAluno();

			if (inserido) {

				response.sendRedirect("home.html");
			} else {

				response.sendRedirect("");
			}
		} else {

			response.sendRedirect("index.html?erro=3");
		}
	}

}
