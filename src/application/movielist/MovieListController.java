package application.movielist;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import application.addingmovie.MovieAddController;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MovieListController implements Initializable{

    @FXML
    public Button backBtn;
   
    @FXML
    public Button backBtn1;
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private TextField searchBar;
	
    @FXML
    private TableView<Movie> tableView;

    @FXML
    public TableColumn<Movie, String> idC;

    @FXML
    private TableColumn<Movie, String> titleC;

    @FXML
    private TableColumn<Movie, String> actorC;

    @FXML
    private TableColumn<Movie, String> directorC;

    @FXML
    private TableColumn<Movie, String> languageC;

    @FXML
    private TableColumn<Movie, String> lengthC;

    @FXML
    private TableColumn<Movie, String> genreC;

    @FXML
    private TableColumn<Movie, String> descriptionC;
    
    @FXML
    private TableColumn<Movie, String> availabilityC;
    Database db;
    Connection conn;
    ObservableList<Movie> list = FXCollections.observableArrayList();
    ResultSet rs;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		db = new Database();
		initTable();
		loadMovieList();
	}
    //loading data in movie list
	private void loadMovieList() 
	{
		list.clear();
		conn = db.connect();
		String query = "SELECT * FROM movie";
		rs = db.execQuery(query);
		try {
			while(rs.next()) 
			{
				int idM             = rs.getInt("movie_id");
				String titleM       = rs.getString("title");
				String actorM       = rs.getString("actor");
				String directorM    = rs.getString("director");
				String languageM    = rs.getString("language");
				String lengthM      = rs.getString("length");
				String genreM       = rs.getString("genre");
				String descriptionM = rs.getString("description");
				String movieStatus = (rs.getBoolean("status"))? "Availible" : "Not Availible";
				
				
				list.add(new Movie(idM, titleM, actorM, directorM, languageM, lengthM, genreM, descriptionM, movieStatus));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		tableView.setItems(list);
	}
	//connecting table with model
	private void initTable() {
		idC.setCellValueFactory(new PropertyValueFactory<>("id"));
		titleC.setCellValueFactory(new PropertyValueFactory<>("title"));
		actorC.setCellValueFactory(new PropertyValueFactory<>("actor"));
		directorC.setCellValueFactory(new PropertyValueFactory<>("director"));
		languageC.setCellValueFactory(new PropertyValueFactory<>("language"));
		lengthC.setCellValueFactory(new PropertyValueFactory<>("length"));
		genreC.setCellValueFactory(new PropertyValueFactory<>("genre"));
		descriptionC.setCellValueFactory(new PropertyValueFactory<>("description"));
		availabilityC.setCellValueFactory(new PropertyValueFactory<>("availability"));
		
	}
	
	//delete movie from list
    @FXML
    private void handleDelete(ActionEvent event) {
    	
    	conn = db.connect();
        Movie selectedMovie = tableView.getSelectionModel().getSelectedItem();
        if(selectedMovie == null) 
        {
        	db.getAlert(AlertType.ERROR, "No Selection", "Nothing is select to delete");
        	return;
        }
        boolean boo = db.isMovieAlredyRented(selectedMovie);
        if(boo) 
        {
        	db.getAlert(AlertType.WARNING, "Alredy Rented", "Can't delete movie " +selectedMovie.getTitle()+ ", it's rented");
        	return;
        }
        String query = "DELETE FROM movie WHERE movie_id='"+selectedMovie.getId()+"'";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure that you want to delete movie " + selectedMovie.getTitle() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK) 
        {
        	if(db.execAction(query)) 
        	{
        		db.getAlert(AlertType.INFORMATION, "Deleted", "Movie " + selectedMovie.getTitle() + " is deleted!!!");
        		list.removeAll(selectedMovie);
        	}
        	else 
        	{
        		db.getAlert(AlertType.WARNING, "Warning", "Movie " + selectedMovie.getTitle() + " cant be deleted");
        	}
        }
        else 
        {
        	db.getAlert(AlertType.INFORMATION, "Canceled", "Delete option canceled!!!");
        }
        
        
    	
    }
    //edit movie in list
    @FXML
    private void handleEdit(ActionEvent event) {
        
        Movie selected = tableView.getSelectionModel().getSelectedItem();
        if(selected == null) 
        {
        	db.getAlert(AlertType.ERROR, "No Selection", "Nothing is select to edit");
        	return;
        }

        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/addingmovie/MovieAdd.fxml"));
        	Parent root = loader.load();
        	MovieAddController controller = (MovieAddController)loader.getController();
        	controller.setUI(selected);
        	
        	Stage stage = new Stage(StageStyle.DECORATED);
        	stage.setScene(new Scene(root));
        	stage.setTitle("Edit Movie");
        	stage.setResizable(false);
        	stage.show();
        	
        	stage.setOnCloseRequest((e) -> {
        		refreshData();
        	});
        	
        }catch(Exception ex) {
        	ex.printStackTrace();
        }

    }
    private void refreshData() {
    	loadMovieList();
    }
    //search movie
    @FXML
    void searchMovie(KeyEvent event) {
    	
        conn = db.connect();
        if(searchBar.getText().equals("")) 
        {
        	loadMovieList();
        }
        else 
        {
        	list.clear();
            String query = "SELECT * FROM movie WHERE movie_id LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM movie WHERE title LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM movie WHERE actor LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM movie WHERE director LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM movie WHERE genre LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM movie WHERE language LIKE '"+searchBar.getText()+"%'";
            rs = db.execQuery(query);
            try {
            	while(rs.next()) {
            		int idM             = rs.getInt("movie_id");
    				String titleM       = rs.getString("title");
    				String actorM       = rs.getString("actor");
    				String directorM    = rs.getString("director");
    				String languageM    = rs.getString("language");
    				String lengthM      = rs.getString("length");
    				String genreM       = rs.getString("genre");
    				String descriptionM = rs.getString("description");
    				
    				list.add(new Movie(idM, titleM, actorM, directorM, languageM, lengthM, genreM, descriptionM));
                }
            	tableView.setItems(list);
            }
            catch(Exception ex) {
            	ex.printStackTrace();
            }
        }

    }



    @FXML
    void loadAddMovie(ActionEvent event) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/addingmovie/MovieAdd.fxml"));
        	Parent root = loader.load();
        	MovieAddController controller = (MovieAddController)loader.getController();
        	Stage stage = new Stage(StageStyle.DECORATED);
        	stage.setScene(new Scene(root));
        	stage.setResizable(false);
        	stage.setTitle("Add Movie");
        	stage.show();
        	stage.setOnCloseRequest((e) -> {
        		refreshData();
        	});
        	
        }catch(Exception ex) {
        	ex.printStackTrace();
        }
    }

    @FXML
    void backPage(ActionEvent event) {
        db.closeStage(rootPane);
        String url = "/application/Home.fxml";
        String title = "Movie Rental";
        db.setStage(url, title, true);
        
    }
    @FXML
    void backPage1(ActionEvent event) {
        db.closeStage(rootPane);
        String url = "/application/rentmovie/RentMovie.fxml";
        String title = "Movie Rental";
        db.setStage(url, title, true);
        
    }

}
