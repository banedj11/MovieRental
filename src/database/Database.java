package database;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.memberlist.Member;
import application.movielist.Movie;
import application.seticon.SetIcon;
import application.settings.Settings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Database {

	private static final String db_url   = "jdbc:mysql://localhost:3306/"; 
	private static final String name     = "dvd_assistent";
	private static final String user     = "root";
	private static final String password = "bane1101988";
	Connection conn = null;
	
	public Connection connect() {
		
		try 
		{
			 Class.forName("com.mysql.cj.jdbc.Driver");
			 conn = DriverManager.getConnection(db_url + name, user, password);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	//execute action
	public boolean execAction(String query) {
		try {
			conn = connect();
			Statement st = conn.createStatement();
			st.execute(query);
			st.close();
			conn.close();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	//execute query
	public ResultSet execQuery(String query) {
		ResultSet rs;
		try {
			conn = connect();
			Statement st = conn.createStatement();
			rs = st.executeQuery(query);
		}catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
		return rs;
		
	}
	//method for movie update
	public boolean updateMovie(Movie movie) {
		String update = "UPDATE movie SET title = ?, actor = ?, director = ?, language = ?, length = ?, genre = ?, description = ? WHERE movie_id = ?";
		try {
			conn = connect();
			PreparedStatement st = conn.prepareStatement(update);
			st.setString(1, movie.getTitle());
			st.setString(2, movie.getActor());
			st.setString(3, movie.getDirector());
			st.setString(4, movie.getLanguage());
			st.setString(5, movie.getLength());
			st.setString(6, movie.getGenre());
			st.setString(7, movie.getDescription());
			st.setInt(8, movie.getId());
			int res = st.executeUpdate();
			return (res > 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	//method for member update
	public boolean updateMember(Member member) {
		String update = "UPDATE members SET name = ?, mobile = ?, email = ?, gender = ?, birthday = ?, address = ? WHERE member_id = ?";
		try {
			conn = connect();
			PreparedStatement st = conn.prepareStatement(update);
			st.setString(1, member.getName());
			st.setString(2, member.getPhone());
			st.setString(3, member.getEmail());
			st.setString(4, member.getGender());
			st.setString(5, member.getDateOfBirth());
			st.setString(6, member.getAddress());
			st.setInt(7, member.getId());
			int res = st.executeUpdate();
			return (res > 0);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean updateSettings(Settings settings) {
		String update = "UPDATE settings SET cost = ?, maximum_movies = ? WHERE id_settings = 1"; 
		try {
			conn = connect();
			PreparedStatement st = conn.prepareStatement(update);
			st.setString(1, settings.getCostS());
			st.setString(2, settings.getMaximumMoviesS());
			int res = st.executeUpdate();
			return (res > 0);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return false;
	}
    //alert method
    public void getAlert(AlertType type, String title, String txt) {
    	Alert alert = new Alert(type);
    	alert.setHeaderText(null);
    	alert.setTitle(title);
    	alert.setContentText(txt);
    	alert.showAndWait();
    }
    //seting stage
    public void setStage(String url, String title, boolean resizable) {
    	try {
    		
    		Stage stage = new Stage();
			Parent root = FXMLLoader.load(getClass().getResource(url));
			stage.setScene(new Scene(root));
			stage.setTitle(title);
			SetIcon.setStageIcon(stage);
			stage.setResizable(resizable);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    //checking if movie is alredy rented, if it is, can't delete it
	public boolean isMovieAlredyRented(Movie movie) {
		
		try {
			String checkStmt = "SELECT COUNT(*) FROM rent WHERE movieId = ?";
			PreparedStatement st = conn.prepareStatement(checkStmt);
			st.setInt(1, movie.getId());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				
				int count = rs.getInt(1);
				return (count > 0);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	//checking if member has alredy rented the movie, if he has, can't delete him
	public boolean hasMemberRentedMovie(Member member) {
		
		try {
			String checkStmt = "SELECT COUNT(*) FROM rent WHERE memberId = ?";
			PreparedStatement st = conn.prepareStatement(checkStmt);
			st.setInt(1, member.getId());
			ResultSet rs = st.executeQuery();
			if(rs.next()) {
				
				int count = rs.getInt(1);
				return (count > 0);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void closeStage(AnchorPane pane) {
		Stage stage = (Stage)pane.getScene().getWindow();
		stage.close();
	}
}
