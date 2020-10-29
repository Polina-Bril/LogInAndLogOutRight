package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/MyServlet")
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<String, List<String>> users = new ConcurrentHashMap<>();

	@Override
	public void init() throws ServletException {
		super.init();
		users.put("Vanya", List.of("ijfwhe", "Иван"));
		users.put("Katya", List.of("uhryop3n", "Екатерина"));
		users.put("Poli", List.of("laeuhgdfuyqg", "Полина"));
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		String login = request.getParameter("login");
		String passwSecured = null;
		try {
			passwSecured = users.get(login).get(0);
		} catch (NullPointerException e) {
			request.getRequestDispatcher("RegistrationNewUser.html").forward(request, response);
			return;
		}
		if (passwSecured != null) {
			String password = request.getParameter("password");
			if (passwSecured.equals(password)) {
				String name = users.get(login).get(1);
				session.setAttribute("name", name);
				session.setAttribute("password", password);
				session.setAttribute("login", login);
				out.println("<h3>Привет, " + session.getAttribute("name") + "!</h3>");
				out.print("<br>");
				out.print("<a href='http://localhost:8080/WebApp/LogOut'>Выйти</a>");
			} else {
				out.print("<h3>Пароль введен не верно! Попробуйте еще раз!</h3>");
				out.print("<a href='http://localhost:8080/LogInAndLogOut/StartPage.html'>На главную</a>");
			}
		}
		out.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		String login = request.getParameter("login");
		String password = request.getParameter("password");
		String name = request.getParameter("name");
		response.setContentType("text/html");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		if (!users.containsKey(login)) {
			users.put(login, List.of(password, name));
			out.println("<h3>Привет, " + users.get(login).get(1) + "!</h3>");
			out.print("<br>");
			out.print("<a href='http://localhost:8080/LogInAndLogOut/LogOut'>Выйти</a>");
		} else {
			out.print("<h3>Пользователь с login " + login + " уже существует. Выберите другой login</h3>");
			out.print("<a href='http://localhost:8080/LogInAndLogOut/RegistrationNewUser.html'>Назад</a>");
		}
		out.close();
	}
}
