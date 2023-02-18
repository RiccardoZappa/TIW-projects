package it.polimi.tiw.project.dao;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import it.polimi.tiw.project.bean.Meeting;

public class PartecipantsDAO {
private Connection connection;
	
	public PartecipantsDAO(Connection connection) {
		this.connection = connection;
	}
	
	public List<Meeting> FindMeetingInvitation(String username) throws SQLException{
		List<Meeting> Mylist = new ArrayList<Meeting>();
		Meeting meeting = null;
		java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
		Time now = new Time(System.currentTimeMillis());
		String performedAction = " finding meeting invitations";
		String query = "SELECT * FROM meet.meeting JOIN meet.partecipants ON id_r = id_meeting  WHERE username_i = ? AND start_date >= ? AND end_date > ? ";
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
	
		
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,username);
			preparedStatement.setDate(2,today);
			preparedStatement.setTime(3, now);
			resultSet = preparedStatement.executeQuery();
			
			while(resultSet.next()) {
				meeting = new Meeting();
				meeting.setTitle(resultSet.getString("title"));
				meeting.setStart(resultSet.getDate("start_date"));
				meeting.setEnd(resultSet.getTime("end_date"));
				meeting.setUsCreator(resultSet.getString("us_creator"));
				Mylist.add(meeting);
			}
			
		}catch(SQLException e) {
			throw e;
		}finally {
			try {
				if(resultSet != null)
					resultSet.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the result set when" + performedAction);
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		return Mylist;
	}
		
		public void storePartecipants (String partecipants,int id ) throws SQLException{
			String performedAction = " storing partecipants";
			String queryAddUser = "INSERT INTO meet.partecipants (username_i,id_meeting) VALUES(?,?)";
			PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddUser);
			preparedStatementAddUser.setString(1, partecipants);
			preparedStatementAddUser.setInt(2, id);
			preparedStatementAddUser.executeUpdate();
			
		}catch(SQLException e) {
			throw e;
		}finally {
			try {
				preparedStatementAddUser.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		
		
	}
}
	
