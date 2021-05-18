
package net.javaguide.usermanagement.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.javaguide.usermanagement.model.User;

//All the db operations in this class
//DAO - Data Access Object -A design pattern to seperate all db to a sepeerate cls


public class UserDAO {

	//variables to store username , pwd
	
	private String jdbcURL = "jdbc:mysql://localhost:3306/demo";
	private String jdbcUserName = "root";
	private String jdbcPassword = "";
	
	private static final String INSERT_USERS_SQL = "INSERT INTO users" + "(name, email, country) VALUES(?,?,?);";
	
	private static final String SELECT_USER_BY_ID = "select id,name,email,country from users where id=?";
	private static final String SELECT_ALL_USERS = "select * from users";
	private static final String DELETE_USERS_SQL = "delete from users where id=?;";
	private static final String UPDATE_USERS_SQL = "update users set name = ?, email =? , country=?  where id=?;";
	
	//Get connection method
	protected Connection getConnection() {
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(jdbcURL, jdbcUserName, jdbcPassword);
			
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	//create user / insertsql
	public void insertUser(User user) throws SQLException{
		try (Connection conn = getConnection();
			PreparedStatement ps = conn.prepareStatement(INSERT_USERS_SQL)){
				ps.setString(1, user.getName());
				ps.setString(2, user.getEmail());
				ps.setString(3, user.getCountry());
				
				
				ps.executeUpdate();
				
		}catch(Exception e) {
				e.printStackTrace();
		}
	}//~insert user
	
	//update
	public boolean updateUser(User user)throws SQLException{
		boolean rowUpdated = false;
		
		try (Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(UPDATE_USERS_SQL);){
				stmt.setString(1, user.getName());
				stmt.setString(2, user.getEmail());
				stmt.setString(3, user.getCountry());
				stmt.setInt(4, user.getId());
				
				rowUpdated = stmt.executeUpdate() > 0;
				
		}catch(Exception e) {
				e.printStackTrace();
		}
		return rowUpdated;
	}//~update user	
	
	
	//select user by id
	public User selectUser(int id ) { //select a user by id
		User user = null;
		
		try (Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SELECT_USER_BY_ID);){
				stmt.setInt(1, id);
			
				System.out.println(stmt);
				
				ResultSet rs = stmt.executeQuery();//as need a result set
				
				while(rs.next()) {
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");
					user = new User(id, name, email, country);
					
				}
				
				
		}catch(SQLException e) {
				e.printStackTrace();
		}
		return user;
	}//~ user	
	
	
	
	
	//select* user
	public List<User> selectAllUser() { //select a user by id
		List<User> users = new ArrayList<>();
		
		try (Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_USERS);){
			
				System.out.println(stmt);
				
				ResultSet rs = stmt.executeQuery();//as need a result set
				
				while(rs.next()) {
					int id = rs.getInt("id");
					String name = rs.getString("name");
					String email = rs.getString("email");
					String country = rs.getString("country");
					users.add(new User(id, name, email, country));
					
				}
				
				
		}catch(SQLException e) {
				e.printStackTrace();
		}
		return users;
	}//~ select * user
	
	//delete user
	public boolean deleteUser(int id) throws SQLException{ //select a user by id

		boolean rowDeleted;
		
		try (Connection conn = getConnection();
			PreparedStatement stmt = conn.prepareStatement(DELETE_USERS_SQL);){
			
			stmt.setInt(1, id);
			rowDeleted = stmt.executeUpdate() > 0 ;
				
		}
		
		return rowDeleted;
	}//~ delete user
}
