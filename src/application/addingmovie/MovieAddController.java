package application.addingmovie;


import java.net.URL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import application.movielist.Movie;
import database.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MovieAddController implements Initializable{




    @FXML
    private TextField idC;
    
    @FXML
    private TextField titleTf;

    @FXML
    private TextField actorTf;

    @FXML
    private TextField directorTf;

    @FXML
    private TextField languageTf;

    @FXML
    private TextField lengthTf;

    @FXML
    private ComboBox<String> genreCb;

    @FXML
    private TextArea descriptionTa;

    @FXML
    private AnchorPane root;
    
    private String[] ganreArr = {"Action", "Adventure", "Biography", "Comedy", "Crime", "Drama", "Fantasy", "Horror", "Romance", "Saga", "Thriller", "Western"};
    Database db;
    Connection conn;
    boolean isEditable = false;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initGanre();
	    db = new Database();
	}
	
	//cancel action
    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage)root.getScene().getWindow();
        stage.close();        
    }
    //Save movie in database
    @FXML
    private void handleSave(ActionEvent event) {
    	String title = titleTf.getText();
    	String actor = actorTf.getText();
    	String director = directorTf.getText();
    	String language = languageTf.getText();
    	String length = lengthTf.getText();
    	String genre = genreCb.getSelectionModel().getSelectedItem();
    	String desc = descriptionTa.getText();
       
    	if(title.isEmpty() || actor.isEmpty() || director.isEmpty() || language.isEmpty() || length.isEmpty()  || desc.isEmpty()) {
    		db.getAlert(AlertType.ERROR, "Empty field", "Field cant be empty!!!");
    		return;
    	}
    	if(isEditable) {
    		editMovie();
    		return;
    	}
    	if(!validateNumber(length)) {
    		return;
    	}
    	String query = "INSERT INTO movie values(null, '"+title+"', '"+actor+"', '"+director+"', '"+language+"', '"+length+"','"+genre+"','"+desc+"', true)";
		conn = db.connect();
		if(db.execAction(query)) {
			db.getAlert(AlertType.INFORMATION, "Saving movie", "Movie succesfully saved!!!");
	        titleTf.setText("");
	    	actorTf.setText("");
	    	directorTf.setText("");
	    	languageTf.setText("");
	    	lengthTf.setText("");
	    	genreCb.getSelectionModel().clearAndSelect(0);
	        descriptionTa.setText("");
	     }else {
	    	db.getAlert(AlertType.ERROR, "Movie Adding Error", "Movie Adding Failed!!!");
	     }
	   }
    //update movie
    public void editMovie() {
    	conn = db.connect();
        int id = Integer.parseInt(idC.getText());
        
    	String title = titleTf.getText();
    	String actor = actorTf.getText();
    	String director = directorTf.getText();
    	String language = languageTf.getText();
    	String length = lengthTf.getText();
    	String genre = genreCb.getSelectionModel().getSelectedItem();
    	String desc = descriptionTa.getText();
    	
    	Movie movie = new Movie(id, title, actor, director, language, length, genre, desc);

        if(db.updateMovie(movie)) {
        	db.getAlert(AlertType.INFORMATION, "Success", "Movie is succesfully edited!!!");
        	
        }else {
        	db.getAlert(AlertType.ERROR, "Error", "Can't edit book!!!");
        }
		
    }
   
    private void initGanre() {
		
		List<String> ganreList = new ArrayList<String>();
		for (String content : ganreArr) {
			ganreList.add(content);
		}
		ObservableList<String> list = FXCollections.observableArrayList(ganreList);
		genreCb.setItems(list);
	}
    private boolean validateNumber(String txt) {
    	Pattern p = Pattern.compile("[0-9]+");
    	Matcher m = p.matcher(txt);
    	if(m.find() && m.group().equals(txt)) {
    		return true;
    	}else {
    		db.getAlert(AlertType.WARNING, "Invalid number", "Please insert valid number!!!");
    		return false;
    	}
    }
    //set UI for edit
    public void setUI(Movie movie) {
    	
    	genreCb.getSelectionModel().clearAndSelect(0);
    	idC.setText(Integer.toString(movie.getId()));
    	titleTf.setText(movie.getTitle());
    	actorTf.setText(movie.getActor());
    	directorTf.setText(movie.getDirector());
    	languageTf.setText(movie.getLanguage());
    	lengthTf.setText(movie.getLength());
    	descriptionTa.setText(movie.getDescription());
    	if(movie.getGenre().equals("Action")) {
    		genreCb.getSelectionModel().clearAndSelect(0);
    	}else if(movie.getGenre().equals("Adventure")) {
    		genreCb.getSelectionModel().clearAndSelect(1);
    	}else if(movie.getGenre().equals("Biography")) {
    		genreCb.getSelectionModel().clearAndSelect(2);
    	}else if(movie.getGenre().equals("Comedy")) {
    		genreCb.getSelectionModel().clearAndSelect(3);
    	}else if(movie.getGenre().equals("Crime")) {
    		genreCb.getSelectionModel().clearAndSelect(4);
    	}else if(movie.getGenre().equals("Drama")) {
    		genreCb.getSelectionModel().clearAndSelect(5);
    	}else if(movie.getGenre().equals("Fantasy")) {
    		genreCb.getSelectionModel().clearAndSelect(6);
    	}else if(movie.getGenre().equals("Horror")) {
    		genreCb.getSelectionModel().clearAndSelect(7);
    	}else if(movie.getGenre().equals("Romance")) {
    		genreCb.getSelectionModel().clearAndSelect(8);
    	}else if(movie.getGenre().equals("Saga")) {
    		genreCb.getSelectionModel().clearAndSelect(9);
    	}else if(movie.getGenre().equals("Thriller")) {
    		genreCb.getSelectionModel().clearAndSelect(10);
    	}else if(movie.getGenre().equals("Western")) {
    		genreCb.getSelectionModel().clearAndSelect(11);
    	}
    	isEditable = true;
    }
    
    
}
