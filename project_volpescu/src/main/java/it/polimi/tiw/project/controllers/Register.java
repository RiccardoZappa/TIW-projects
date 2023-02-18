package it.polimi.tiw.project.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.project.bean.User;
import it.polimi.tiw.project.dao.UserDAO;
import it.polimi.tiw.project.utils.ConnectionHandler;

import it.polimi.tiw.project.utils.TemplateHandler;
import it.polimi.tiw.project.utils.pathUtils;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Register() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	ServletContext servletContext = getServletContext();
		this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
		this.connection = ConnectionHandler.getConnection(servletContext);
    }
    
   
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String password2 = request.getParameter("password2");
		if(username == null || email == null || password == null || password2 == null) {
			forwardToErrorPage(request, response, "Register module missing some data");
			return;
		}
		if(!password.equals(password2)) {
			forwardToErrorPage(request, response, "Passwords are not the same");
			return;
		}
		
		
		UserDAO userDAO = new UserDAO(connection);
		

		User user = null;
		
		try {
			user = userDAO.getUserByUsername(username);
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		} 
		
		if(user != null) {
			request.setAttribute("warning", "Chosen username already exists!");
			forward(request, response, pathUtils.pathToRegisterPage);
			return;
		}
		
		try {
			userDAO.registerUser(username, password, email);
			
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		try {
			user = userDAO.getUserByUsername(username);
		} catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		} 
		
		
	
		HttpSession session = request.getSession();
		session.setAttribute("currentUser", user);
		response.sendRedirect(getServletContext().getContextPath() + pathUtils.goToHomeServletPath);
		
	}
	
	private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
		
		request.setAttribute("error", error);
		forward(request, response, pathUtils.pathToErrorPage);
		return;
	}
	
	private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		
	}
	@Override
    public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}