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
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.polimi.tiw.project.bean.Partecipants;
import it.polimi.tiw.project.bean.User;
import it.polimi.tiw.project.dao.MeetingDAO;
import it.polimi.tiw.project.dao.PartecipantsDAO;
import it.polimi.tiw.project.utils.ConnectionHandler;

/**
 * Servlet implementation class CreateMeeting
 */
@WebServlet("/CreateMeetings")
@MultipartConfig
public class CreateMeetings extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private Connection connection;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateMeetings() {
        super();
        // TODO Auto-generated constructor stub
    }
    @Override
    public void init() throws ServletException {
    	ServletContext servletContext = getServletContext();
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
		System.out.println(currentUser.getUsername());
		String[] username = request.getParameterValues("username[]");
		String title = request.getParameter("title");
		

		if (username == null) {
		    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		    response.getWriter().println("Invited list is null");
		    return;
		}
		if (title == null) {
		    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		    response.getWriter().println("MeetingTitle is null");
		    return;
		}                          
		MeetingDAO meetingDAO = new MeetingDAO(connection);	
		PartecipantsDAO partecipantsDAO = new PartecipantsDAO(connection);
		Partecipants partecipants = new Partecipants();
		int n_max_p = 10;
		int id = (int) (Math.random() * 10000);
		int uniqueID = id;
        Date start_date = new Date(); 
        Time end_date = new Time(start_date.getTime() + 7200000);
        partecipants.SetIdMeeting(uniqueID);
        List<String> invitations = Arrays.asList(username);
    	partecipants.SetList(invitations);

        
		try {
			meetingDAO.createMeeting(uniqueID,currentUser.getUsername(), title, start_date,end_date,n_max_p);
		}catch (SQLException e) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().println(e.getMessage());
			return;		
		}
		for (int i = 0; i < partecipants.getUsername_i().size(); i++) {
		    
			try {
				partecipantsDAO.storePartecipants(partecipants.getUsername_i().get(i),partecipants.getId_meeting());
			}catch (SQLException e) {
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				response.getWriter().println(e.getMessage());
				
			return;		
		}
		}
	}

}
