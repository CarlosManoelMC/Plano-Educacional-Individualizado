package Controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import Model.DAO;
import ModelEntidade.PEI;

/**
 * Servlet implementation class SalvarEdicaoPEI
 */
@WebServlet("/SalvarEdicaoPEI")
public class SalvarEdicaoPEI extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SalvarEdicaoPEI() {
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
		PrintWriter out = response.getWriter();

		String peiDataJson = request.getReader().readLine();
		Gson gson = new Gson();
		PEI peiData = gson.fromJson(peiDataJson, PEI.class);

		PEI pei = new PEI();
		boolean atualizado = pei.atualizarPEI(peiData);

		if (atualizado) {
			out.print("PEI atualizado com sucesso!");
		} else {
			out.print("Erro ao atualizar PEI.");
		}
	}

}
