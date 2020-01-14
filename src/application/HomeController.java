package application;

import java.net.URL;
import java.util.ResourceBundle;

import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;


public class HomeController implements Initializable{

    @FXML
    private AnchorPane rootPane;
	
	Database db;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = new Database();
	}

    @FXML
    void addMember(ActionEvent event) {


    	String url = "/application/addingmember/MemberAdd.fxml";
    	String title = "Add Member";
    	db.setStage(url, title, false);
    	
    }

    @FXML
    void addMovie(ActionEvent event) {
        
    	String url = "/application/addingmovie/MovieAdd.fxml";
    	String title = "Add Movie";
    	db.setStage(url, title, false);
    	
    }

    @FXML
    void rentMovie(ActionEvent event) {
        db.closeStage(rootPane);
    	String url = "/application/rentmovie/RentMovie.fxml";
    	String title = "Movie Rental";
    	db.setStage(url, title, true);
    	
    }

    @FXML
    void showMemberList(ActionEvent event) {
        db.closeStage(rootPane);
    	String url = "/application/memberlist/MemberList.fxml";
    	String title = "Members List";
    	db.setStage(url, title, true);
    	
    }

    @FXML
    void showMovieList(ActionEvent event) {
    	db.closeStage(rootPane);
    	String url = "/application/movielist/MovieList.fxml";
    	String title = "Movies List";
    	db.setStage(url, title, true);
    	
    	
    }
    
    @FXML
    void loadSetings(ActionEvent event) {
        db.closeStage(rootPane);
        String url = "/application/settings/Settings.fxml";
        String title = "Settings";
        db.setStage(url, title, false);
    }

}
