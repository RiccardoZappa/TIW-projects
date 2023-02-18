package it.polimi.tiw.project.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.project.bean.User;

public class UserDAO {
	private Connection connection;
	
	public UserDAO(Connection connection) {
		this.connection = connection;
	}
public void registerUser(String username, String password, String email) throws SQLException {
		
		String performedAction = " registering a new user in the database";
		String queryAddUser = "INSERT INTO meet.user  VALUES(?,?,?)";
		PreparedStatement preparedStatementAddUser = null;	
		
		try {
			
			preparedStatementAddUser = connection.prepareStatement(queryAddUser);
			preparedStatementAddUser.setString(1, username);
			preparedStatementAddUser.setString(2, password);
			preparedStatementAddUser.setString(3, email);
			preparedStatementAddUser.executeUpdate();
			
		}catch(SQLException e) {
			throw new SQLException("Error accessing the DB when" + performedAction);
		}finally {
			try {
				preparedStatementAddUser.close();
			}catch (Exception e) {
				throw new SQLException("Error closing the statement when" + performedAction);
			}
		}
	}

public User getUserByUsername(String username) throws SQLException{
	User user = null;
	String performedAction = " finding a  user in the database";
	String query = "SELECT * FROM meet.user WHERE username = ?";
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	
	try {
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1,username);
		resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()) {
			user = new User();
			user.setUsername(resultSet.getString("username"));
			user.setEmail(resultSet.getString("e_mail"));
		}
		
	}catch(SQLException e) {
		throw new SQLException("Error accessing the DB when" + performedAction);
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
	return user;
}


public User findUser(String username, String password) throws SQLException{
	
	String performedAction = " finding a user by username and password";
	String query = "SELECT * FROM meet.user WHERE username = ? AND password = ?";
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	User user = null;
	try {
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1,username);
		preparedStatement.setString(2, password);
		resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()) {
			user  = new User();
			user.setUsername(resultSet.getString("username"));
			user.setEmail(resultSet.getString("e_mail"));
		}
		
	}catch(SQLException e) {
		throw new SQLException("Error accessing the DB when" + performedAction);
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
	return user;
}
public  List<User> FindAllUser(String username) throws SQLException{
	User user = null;
	List<User> UserList = new ArrayList<User>();
	String performedAction = " finding all the registered user";
	String query = "SELECT username, e_mail FROM meet.user WHERE username != ?";
	PreparedStatement preparedStatement = null;
	ResultSet resultSet = null;
	
	try {
		preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1,username);
		resultSet = preparedStatement.executeQuery();
		
		while(resultSet.next()) {
			user = new User();
			user.setUsername(resultSet.getString("username"));
			user.setEmail(resultSet.getString("e_mail"));
			UserList.add(user);
		}
		
	}catch(SQLException e) {
		throw new SQLException("Error accessing the DB when" + performedAction);
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
	return UserList;
}

}