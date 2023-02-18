package it.polimi.tiw.project.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Arrays;
import java.util.Date;
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

import it.polimi.tiw.project.bean.Partecipants;
import it.polimi.tiw.project.bean.User;
import it.polimi.tiw.project.dao.MeetingDAO;
import it.polimi.tiw.project.dao.PartecipantsDAO;
import it.polimi.tiw.project.utils.ConnectionHandler;
import it.polimi.tiw.project.utils.TemplateHandler;
import it.polimi.tiw.project.utils.pathUtils;


    
/**
 * Servlet implementation class CreateMeeting
 */
@WebServlet("/CreateMeeting")
public class CreateMeeting extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection;
	private TemplateEngine templateEngine;     
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMeeting() {
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
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession(false);
		User currentUser = (User)session.getAttribute("currentUser");
		MeetingDAO meetingDAO = new MeetingDAO(connection);	
		PartecipantsDAO partecipantsDAO = new PartecipantsDAO(connection);
		Partecipants partecipants = new Partecipants();
		String[] invited = request.getParameterValues("partecipants[]");
		if (invited == null) {
			request.setAttribute("error", "no user selected");
			response.sendRedirect(getServletContext().getContextPath() + pathUtils.goToAnagraphicServletPath);
			return;
		}
		List<String> invitations = Arrays.asList(invited);
		partecipants.SetList(invitations);
		String title = request.getParameter("title");
		int n_max_p = 10;
		int id = (int) (Math.random() * 10000);
		int uniqueID = id;
        Date start_date = new Date(); 
        Time end_date = new Time(start_date.getTime() + 7200000);
        partecipants.SetIdMeeting(uniqueID);
        int toAdd = (int) session.getAttribute("counter");
        
        
        if(title == null) {
			forwardToAnagraphicPage(request, response, "Null meeting title",toAdd);
			return;
		}	 
        toAdd ++;
        
        if (partecipants.getUsername_i().size() > n_max_p) {
        	if (toAdd == 3) {
        		forwardToDeletePage(request,response,"three attempts to create a meeting exceeding max partecipants");
        		session.setAttribute("counter", 0);
        		getServletContext().setAttribute("error", null);
        		return;
        	}
        	else {
        		session.setAttribute("counter", toAdd);
        		forwardToDeletePage2(request, response, "partecipants exceeded, create a meeting with 10 partecipants max");
			return;
        	}
        }
		
		try {
			meetingDAO.createMeeting(uniqueID,currentUser.getUsername(), title, start_date,end_date,n_max_p);
		}catch (SQLException e) {
			forwardToErrorPage(request, response, e.getMessage());
			return;		
		}
		for (int i = 0; i < partecipants.getUsername_i().size(); i++) {
		    
			try {
				partecipantsDAO.storePartecipants(partecipants.getUsername_i().get(i),partecipants.getId_meeting());
			}catch (SQLException e) {
				forwardToErrorPage(request, response, e.getMessage());
			return;		
		}
		}
		getServletContext().setAttribute("error", null);
		response.sendRedirect(getServletContext().getContextPath() + pathUtils.goToHomeServletPath);
	
	}
	
	
	
	
private void forwardToAnagraphicPage(HttpServletRequest request, HttpServletResponse response, String error, int counter) throws ServletException, IOException{
	
	
	request.setAttribute("error", error);
	forward(request, response, pathUtils.pathToAnagraphicPage);
	return;
}

private void forwardToDeletePage(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
	
	request.setAttribute("error", error);
	forward(request, response, pathUtils.pathToDeletePage);
	return;
}
private void forwardToDeletePage2(HttpServletRequest request, HttpServletResponse response, String error) throws ServletException, IOException{
	
	getServletContext().setAttribute("error", error);
	response.sendRedirect(getServletContext().getContextPath() + pathUtils.goToAnagraphicServletPath);
	return;
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
}
