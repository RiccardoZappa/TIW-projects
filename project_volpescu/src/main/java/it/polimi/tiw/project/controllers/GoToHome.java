package it.polimi.tiw.project.controllers;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import it.polimi.tiw.project.bean.Meeting;
import it.polimi.tiw.project.bean.User;
import it.polimi.tiw.project.dao.MeetingDAO;
import it.polimi.tiw.project.dao.PartecipantsDAO;
import it.polimi.tiw.project.utils.ConnectionHandler;
import it.polimi.tiw.project.utils.TemplateHandler;
import it.polimi.tiw.project.utils.pathUtils;  

/**
 * Servlet implementation class goToHome
 */
@WebServlet("/GoToHome")
public class GoToHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TemplateEngine templateEngine;
	private Connection connection;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GoToHome() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    @Override
    public void init() throws ServletException {
    	ServletContext servletContext = getServletContext();
		this.templateEngine = TemplateHandler.getEngine(servletContext, ".html");
		this.connection = ConnectionHandler.getConnection(servletContext);
    }
    
    @Override
    public void destroy() {
		try {
			ConnectionHandler.closeConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		PartecipantsDAO partecipantsDAO = new PartecipantsDAO(connection);
		MeetingDAO meetingDAO = new MeetingDAO(connection);
		List<Meeting> hisMeeting,hisInvitation;
		try {
			hisMeeting = meetingDAO.FindMeetingListByCreator(currentUser.getUsername());
		}catch(SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		try {
			hisInvitation = partecipantsDAO.FindMeetingInvitation(currentUser.getUsername());
		}catch(SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;	
		}
		request.setAttribute("meetings", hisMeeting);
		request.setAttribute("invitations", hisInvitation);
		forward(request, response, pathUtils.pathToHomePage);
	}
	
	private void forwardToErrorPage(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
		
		request.setAttribute("error", error);
		forward(request, response,pathUtils.pathToErrorPage);
		return;
	}
	
	private void forward(HttpServletRequest request, HttpServletResponse response, String path) throws ServletException, IOException{
		
		ServletContext servletContext = getServletContext();
		final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
		templateEngine.process(path, ctx, response.getWriter());
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}