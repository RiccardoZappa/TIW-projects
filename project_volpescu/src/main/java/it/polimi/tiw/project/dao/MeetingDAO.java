package it.polimi.tiw.project.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;

import it.polimi.tiw.project.bean.Meeting;


public class MeetingDAO {
	private Connection connection;
	
	public MeetingDAO(Connection connection) {
		this.connection = connection;
	}
	public List<Meeting> FindMeetingListByCreator (String username) throws SQLException{ 
		List<Meeting> Mylist = new ArrayList<Meeting>();
		Meeting meeting = null;
		String performedAction = " finding a meeting by creator username";
		java.sql.Date today = new java.sql.Date(System.currentTimeMillis());
		Time now = new Time(System.currentTimeMillis());
		String query = "SELECT * FROM meet.meeting WHERE us_creator = ? AND start_date >= ? AND end_date > ? ";
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
				meeting.setId(resultSet.getInt("id_r"));
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
				throw e;
			}
			try {
				preparedStatement.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
		return Mylist;
		
	}
	public void createMeeting(int uniqueID,String username,String title,Date start_date,Time end_date, int n_max_p) throws SQLException {
		// TODO Auto-generated method stub
		String performedAction = " creating a new meeting";
		String queryAddUser = "INSERT INTO meet.meeting (id_r,us_creator,title,start_date,end_date,n_max_p) VALUES(?,?,?,?,?,?)";
		PreparedStatement preparedStatementAddUser = null;	
		java.sql.Date Start_date = new java.sql.Date(start_date.getTime());  
	try {
		
		preparedStatementAddUser = connection.prepareStatement(queryAddUser);
		preparedStatementAddUser.setInt(1, uniqueID);
		preparedStatementAddUser.setString(2, username);
		preparedStatementAddUser.setString(3, title);
		preparedStatementAddUser.setDate(4, Start_date);
		preparedStatementAddUser.setTime(5,end_date);
		preparedStatementAddUser.setInt(6,n_max_p);
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
