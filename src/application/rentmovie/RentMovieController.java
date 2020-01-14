package application.rentmovie;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import application.memberlist.MemberListController;
import application.movielist.MovieListController;
import database.Database;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class RentMovieController implements Initializable{

    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private Button backBtn;
    
    @FXML
    private Button rentBtn;
    
    @FXML
    private Button showMovieBtn;
    
    @FXML
    private Button showMemberBtn;
    
    @FXML
    private TextField movieIdTf;

    @FXML
    private Text titleTxt;

    @FXML
    private Text actorTxt;

    @FXML
    private Text directorTxt;

    @FXML
    private Text genreTxt;

    @FXML
    private Text statusTxt;

    @FXML
    private TextField memberIdTf;

    @FXML
    private Text memberNameTxt;

    @FXML
    private Text memberPhoneTxt;

    @FXML
    private Text memberEmail;
    
    @FXML
    private Text memberRentedMovies;
    
    @FXML
    private Text memberTotalCosts;
    
    @FXML
    private TextField returnMovieIdTf;

    @FXML
    private Text dateAndTimeTxt;

    @FXML
    private Text renewTxt;

    @FXML
    private Text retId;

    @FXML
    private Text retTitle;

    @FXML
    private Text retActor;

    @FXML
    private Text retDirector;

    @FXML
    private Text retGenre;

    @FXML
    private Text retMemberId;

    @FXML
    private Text retName;

    @FXML
    private Text retPhone;

    @FXML
    private Text retEmail;
    
    @FXML
    private Button renewBtn;

    @FXML
    private Button returnBtn;
    
    @FXML
    private Pane slidingPane;

    @FXML
    private Label lblRent;

    @FXML
    private Label lblReturnRenew;

    @FXML
    private Label lblStatus;

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab tabRent;
    
    @FXML
    private Tab tabReturnRenew;
    
	private Database db;
	private Connection conn;
	private ResultSet rs;
    boolean isRedyForReturn;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = new Database();
		
	}
	//moving tab from left to right
    @FXML
    void openRentTab(MouseEvent event) {
        TranslateTransition toLeftTransition = new TranslateTransition(new Duration(500), lblStatus);
        toLeftTransition.setToX(slidingPane.getTranslateX());
        toLeftTransition.play();
        toLeftTransition.setOnFinished((ActionEvent event2) ->{
        	lblStatus.setText("RENT MOVIE");
        });
        tabPane.getSelectionModel().select(tabRent);
    }
    //moving tab from right to left
    @FXML
    void openReturnRenewTab(MouseEvent event) {
        TranslateTransition toRightTransition = new TranslateTransition(new Duration(500), lblStatus);
        toRightTransition.setToX(slidingPane.getTranslateX() + (slidingPane.getPrefWidth() - lblStatus.getPrefWidth()));
        toRightTransition.play();
        toRightTransition.setOnFinished((ActionEvent event1) ->{
        	lblStatus.setText("RETURN/RENEW");
        	
        });
        tabPane.getSelectionModel().select(tabReturnRenew);
    }
    //renting movie to member
    @FXML
    private void rentMovieToMember(ActionEvent event) {
        
    	String movieId = movieIdTf.getText();
    	String memberId = memberIdTf.getText();
    	int maximumMoviesRented = getMaximumRentedMovies();
    	int rentedMovies = countMemberMovies(memberId);
    	if(rentedMovies == maximumMoviesRented) {
    		db.getAlert(AlertType.INFORMATION, "Can't Rent", "Member can rent maximum " + maximumMoviesRented + " movies");
    		return;
    	}
    	if(movieId.isEmpty()) {
    		db.getAlert(AlertType.INFORMATION, "Empty field", "Plese insert movie id number!");
    		return;
    	}
    	if(memberId.isEmpty()) {
    		db.getAlert(AlertType.INFORMATION, "Empty field", "Please insert member id number!");
    		return;
    	}
    	if(!doesMoviExist(movieId)) {
    		db.getAlert(AlertType.INFORMATION, "Info", "Movie with id: " + movieId + " doesn't exist in database!!!");
    		return;
    	}
    	if(!doesMemberExist(memberId)) {
    		db.getAlert(AlertType.INFORMATION, "Info", "Member with id: " + memberId + " doesn't exist in database!!!");
    		return;
    	}
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setTitle("Confirm issue operation");
        alert.setContentText("Are you sure that you want to rent movie " + titleTxt.getText() + " \nto " + memberNameTxt.getText() + "?");
        Optional<ButtonType> response = alert.showAndWait();
        if(response.get() == ButtonType.OK) 
        {
        	if(isMovieRented(movieId)) 
        	{
        		db.getAlert(AlertType.WARNING, "Warning", "Can't rent movie, it's alredy rented!!!");
        		return;
        	}
        	String que1 = "INSERT INTO rent(movieId, memberId) VALUES('"+movieId+"', '"+memberId+"')";
        	String que2 = "UPDATE movie SET status = false WHERE movie_id = '"+movieId+"'";
        	if(db.execAction(que1) && db.execAction(que2)) {
        		db.getAlert(AlertType.CONFIRMATION, "Succes", "Movie " + titleTxt.getText() + " succesfully rented to member " + memberNameTxt.getText());
        		showMovieDetails(null);
        		showMemberDetails(null);
        		rentBtn.setDisable(true);
        	}
        	else {
        		db.getAlert(AlertType.ERROR, "Error", "Failed to rent a movie!!!");
        	}
        }
        else {
        	db.getAlert(AlertType.INFORMATION, "Canceled", "Rent operation canceled!!!");
        }
    }

    //showing book details and availibility
    @FXML
    private void showMovieDetails(ActionEvent event) {
        
    	
    	conn = db.connect();
    	String id = movieIdTf.getText();
    	if(id.isEmpty()) {
    		db.getAlert(AlertType.INFORMATION, "Empty Field", "Please insert movie id!!!");
    		return;
    	}
        	String query = "SELECT * FROM movie WHERE movie_id = '"+id+"'";
        	boolean boo = false;
        	ResultSet rs = db.execQuery(query);
        	try {
    			while(rs.next()) 
    			{
    				String title = rs.getString("title");
    				String actor = rs.getString("actor");
    				String director = rs.getString("director");
    				String genre = rs.getString("genre");
    				Boolean status = rs.getBoolean("status");
    				String isAvailible = (status)? "Availible" : "Not Availible";
    				
    				titleTxt.setText(title);
    				actorTxt.setText("Actor: " + actor);
    				directorTxt.setText("Director: " + director);
    				genreTxt.setText(genre);
    				statusTxt.setText(isAvailible);
    				boo = true;
    				if(!isMovieRented(id)) {
    					rentBtn.setDisable(false);
    				}else {
    					rentBtn.setDisable(true);
    				}
    				
    				
    			}
    			if(!boo) 
    			{
    				titleTxt.setText("No such movie availible");
    				actorTxt.setText("");
    				directorTxt.setText("");
    				genreTxt.setText("");
    				statusTxt.setText("");
    				rentBtn.setDisable(true);
    			    
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	
	}

    //showing member details
    @FXML
    private void showMemberDetails(ActionEvent event) {

    	
    	conn = db.connect();
    	String id = memberIdTf.getText();
    	if(id.isEmpty()) {
    		db.getAlert(AlertType.INFORMATION, "Empty Field", "Please insert movie id!!!");
    		return;
    	}
    		String query = "SELECT * FROM members WHERE member_id = '"+id+"'";
        	ResultSet rs = db.execQuery(query);
        	boolean boo = false;
        	try {
    			while(rs.next()) 
    			{
    				String name = rs.getString("name");
    				String phone = rs.getString("mobile");
    				String email = rs.getString("email");
    				
    			    memberNameTxt.setText(name);
    			    memberPhoneTxt.setText(phone);
    			    memberEmail.setText(email);
    			    memberRentedMovies.setText("Rented Movies: " + Integer.toString(countMemberMovies(id)));
    			    memberTotalCosts.setText("Total costs: " + (getCostPerMovie() * countMemberMovies(id)) + " RSD");
    			    boo = true;			   
    			    
    			    
    			}
    			if(!boo) 
    			{
    			    memberNameTxt.setText("No such member in database");
    			    memberPhoneTxt.setText("");
    			    memberEmail.setText("");
    			    memberRentedMovies.setText("");
    			    memberTotalCosts.setText("");
    			    
    			    
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
    	
	}
    

    //member list table
    @FXML
    private void showMemberList(ActionEvent event) {
    	try {
    		db.closeStage(rootPane);
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/memberlist/MemberList.fxml"));
        	Parent root = loader.load();
        	MemberListController controller = (MemberListController)loader.getController();
        	controller.backBtn.setVisible(false);
        	controller.backBtn1.setVisible(true);
        	Stage stage = new Stage(StageStyle.DECORATED);
        	stage.setScene(new Scene(root));
        	stage.setTitle("Member List");
        	stage.show();
        	
        	
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
    }
    //movie list table
    @FXML
    void showMovieList(ActionEvent event) {
    	try {
    		db.closeStage(rootPane);
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/movielist/MovieList.fxml"));
        	Parent root = loader.load();
        	MovieListController controller = (MovieListController)loader.getController();
        	controller.backBtn.setVisible(false);
        	controller.backBtn1.setVisible(true);
        	Stage stage = new Stage(StageStyle.DECORATED);
        	stage.setTitle("Movie List");
        	stage.setScene(new Scene(root));
        	stage.show();
        	
        	
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
    }
    //checking if movie is rented
    private boolean isMovieRented(String id) {
		try {
			String checkStmt = "SELECT COUNT(*) FROM rent WHERE movieId = ?";
			PreparedStatement st = conn.prepareStatement(checkStmt);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			if(rs.next()) 
			{
				int count = rs.getInt(1);
				return (count > 0);
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
    }
    //checking if movie exists in database
    private boolean doesMoviExist(String id) {
		try {
			String checkStmt = "SELECT COUNT(*) FROM movie WHERE movie_id = ?";
			PreparedStatement st = conn.prepareStatement(checkStmt);
			st.setString(1, id);
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
    //checking if member exists in database
    private boolean doesMemberExist(String id) {
		try {
			String checkStmt = "SELECT COUNT(*) FROM members WHERE member_id = ?";
			PreparedStatement st = conn.prepareStatement(checkStmt);
			st.setString(1, id);
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
    //counting how many movies has member rented
    public int countMemberMovies(String id) {
    	int count = 0;
    	try {
			String query = "SELECT COUNT(*) from rent WHERE memberId = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, id);
			ResultSet rs = st.executeQuery();
			while(rs.next()) {
				count = rs.getInt(1);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return count;
    	
	}
    //get maximum rented movies
    private int getMaximumRentedMovies() {
    	int maxRented = 0;
    	String query = "SELECT maximum_movies FROM settings WHERE id_settings = 1";
    	rs = db.execQuery(query);
    	try {
			while(rs.next()) {
				String maxRented1 = rs.getString("maximum_movies");
				maxRented = Integer.parseInt(maxRented1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return maxRented;
    }
    //get cost per movie
    private double getCostPerMovie() {
    	double costPerMovie = 0;
    	String query = "SELECT cost FROM settings WHERE id_settings = 1";
    	rs = db.execQuery(query);
    	try {
			while(rs.next()) {
				costPerMovie = rs.getDouble("cost");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return costPerMovie;
    }
    //showing rent information
    @FXML
    private void showRentInfo(ActionEvent event) {
         clear();
         isRedyForReturn = false;
         if(returnMovieIdTf.getText().isEmpty()) {
      		db.getAlert(AlertType.INFORMATION, "Empty field", "Please insert movie id");
      		return;
         }
         try {
         String movieId = returnMovieIdTf.getText();
         String myQuery = "SELECT rent.movieId, rent.memberId, rent.rentTime, rent.renew_count,\r\n" + 
         		"members.name, members.mobile, members.email,\r\n" + 
         		"movie.title, movie.actor, movie.director, movie.genre \r\n" + 
         		"FROM rent\r\n" + 
         		"LEFT JOIN members\r\n" + 
         		"ON rent.memberId=member_id\r\n" + 
         		"LEFT JOIN movie\r\n" + 
         		"ON rent.movieId=movie_id\r\n" + 
         		"WHERE rent.movieId='"+movieId+"'";
         ResultSet res = db.execQuery(myQuery);
         
			if(res.next()) {
				 int memberId = res.getInt("memberId");
				 String memberName = res.getString("name");
				 String phoneNumber = res.getString("mobile");
				 String email = res.getString("email");
				 retMemberId.setText("ID: " + Integer.toString(memberId));
				 retName.setText(memberName);
				 retPhone.setText(phoneNumber);
				 retEmail.setText(email);
				 
				 String movieTitle = res.getString("title");
				 String actor = res.getString("actor");
				 String director = res.getString("director");
				 String genre = res.getString("genre");
				 retId.setText("ID: " + movieId);
				 retTitle.setText(movieTitle);
				 retActor.setText("Actor: " + actor);
				 retDirector.setText("Director: " + director);
				 retGenre.setText(genre);
				 
				 Timestamp rentTime = res.getTimestamp("rentTime");
				 int renewCount = res.getInt("renew_count");
				 dateAndTimeTxt.setText(formatDateTime(rentTime));
				 renewTxt.setText("Renew: " + Integer.toString(renewCount));
				 isRedyForReturn = true;
				 disableButtons(true);
			 }else {
				 db.getAlert(AlertType.INFORMATION, "Not Rented", "There is no rented movie with ID: " + movieId);
			 }
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         
         
    }
    private void clear() {
		
		 retMemberId.setText("");
		 retName.setText("");
		 retPhone.setText("");
		 retEmail.setText("");
         retId.setText("");
		 retTitle.setText("");
		 retActor.setText("");
		 retDirector.setText("");
		 retGenre.setText("");
	     dateAndTimeTxt.setText("");
		 renewTxt.setText("");
		 disableButtons(false);
	}
    private void disableButtons(boolean boo) {
    	if(boo) {
    		renewBtn.setDisable(false);
    		returnBtn.setDisable(false);
    	}else {
    		renewBtn.setDisable(true);
    		returnBtn.setDisable(true);
    	}
    }
	//renew book to member
    @FXML
    void handleRenew(ActionEvent event) {
    	
    	 String movieId = returnMovieIdTf.getText();
         Boolean status = false;
         if(returnMovieIdTf.getText().isEmpty()) {
      		db.getAlert(AlertType.INFORMATION, "Empty field", "Please insert movie id");
      		return;
         }
         String query = "SELECT status FROM movie WHERE movie_id = '"+movieId+"'";
         ResultSet rs = db.execQuery(query);
         try {
			while(rs.next()) {
				 status = rs.getBoolean("status");
				 if(status == true) {
					 db.getAlert(AlertType.INFORMATION, "Not Rented", "Movie with ID: " + movieId + "is not rented");
				 }
				 else if(status == false){
			    	 Alert alert = new Alert(AlertType.CONFIRMATION);
			   	     alert.setHeaderText(null);
			   	     alert.setTitle("Confirm renew operation");
			   	     alert.setContentText("Are you sure that you want to renew the movie");
			   	     Optional<ButtonType> response = alert.showAndWait();
			   	     if(response.get() == ButtonType.OK) {
			   	         
			   	         String que = "UPDATE rent SET rentTime = CURRENT_TIMESTAMP, renew_count = renew_count+1 WHERE movieId = '"+movieId+"'";
			   	    	
			   	         if(db.execAction(que)) {
			                  db.getAlert(AlertType.INFORMATION, "Succesfully Renew", "Movie is succesfully renewed");
			 				 showRentInfo(null);
			   	         }
			   	         else {
			   	        	 db.getAlert(AlertType.ERROR, "Error", "Can't renew movie");
			   	        	 return;
			   	         }
			   	     }else {
			   	    	 db.getAlert(AlertType.INFORMATION, "Canceled", "Renew operation is canceled!");
			   	     }
				 }
				 else {
					 db.getAlert(AlertType.INFORMATION, "Not Rented", "Movie with ID: " + movieId + "is not rented");
				 }
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}
         


    }
    //return book from member
    @FXML
    void handleReturn(ActionEvent event) {
         
   	     String movieId = returnMovieIdTf.getText();
         Boolean status = false;
         if(returnMovieIdTf.getText().isEmpty()) {
  		       db.getAlert(AlertType.INFORMATION, "Empty field", "Please insert movie id");
  		        return;
         }

         String query = "SELECT status FROM movie WHERE movie_id = '"+movieId+"'";
         ResultSet rs = db.execQuery(query);
         try {
			while(rs.next()) {
				 status = rs.getBoolean("status");
				 if(status == true) {
					 db.getAlert(AlertType.INFORMATION, "Not Rented", "Movie with ID: "  + movieId + " is not rented");
				 }
				 else {
			         Alert alert = new Alert(AlertType.CONFIRMATION);
			         alert.setHeaderText(null);
			         alert.setTitle("Confirm submission operation");
			         alert.setContentText("Are you sure that you want to return the movie?");
			         Optional<ButtonType> response = alert.showAndWait();
			         if(response.get() == ButtonType.OK) {
			        	 String que1 = "DELETE FROM rent WHERE movieId = '"+movieId+"'";
			        	 String que2 = "UPDATE movie SET status = true WHERE movie_id = '"+movieId+"'";
			        	 if(db.execAction(que1) && db.execAction(que2)) {
			        		 db.getAlert(AlertType.INFORMATION, "Succesfuly Returned", "Movie has been succesfully retunred");
			 				 showRentInfo(null);
			        	 }else {
			        		 db.getAlert(AlertType.ERROR, "Error", "Can't return movie");
			        	 }
			         }else {
			        	 db.getAlert(AlertType.INFORMATION, "Canceled", "Return operation is canceled!!!");
			         }
				 }
			 }
		} catch (SQLException e) {
			e.printStackTrace();
		}

    }
    //load home page
    @FXML
    void homePage(ActionEvent event) {
         db.closeStage(rootPane);
         String title = "Movie Rental";
         String url = "/application/Home.fxml";
         db.setStage(url, title, true);
         
    }
    private String formatDateTime(Timestamp timestamp) {
    	SimpleDateFormat sdt = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    	String formatedDateTime = sdt.format(timestamp);
    	return formatedDateTime;
    }
    
}
