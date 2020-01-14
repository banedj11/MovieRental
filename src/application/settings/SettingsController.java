package application.settings;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SettingsController implements Initializable{

    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private TextField cost;

    @FXML
    private TextField maxRented;

    @FXML
    private TextField password;

    @FXML
    private TextField retPassword;

    Database db;
    ResultSet rs;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = new Database();
		setUI();
	}
	
	private void setUI() {
		String costStr = "";
		String maxRentStr = "";
		String query = "SELECT * FROM settings";
		rs = db.execQuery(query);
		try {
			while(rs.next()) {
				costStr = rs.getString("cost");
				maxRentStr = rs.getString("maximum_movies");
				cost.setText(costStr);
				maxRented.setText(maxRentStr);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	private void refreshData() {
		setUI();
	}
	//load change password window
	@FXML
	private void loadChangePassword(ActionEvent event) {
		db.closeStage(rootPane);
		db.setStage("/application/settings/ChangePassword.fxml", "Change Password", false);
	}
	
    @FXML
    private void handleCancel(ActionEvent event) {
         ((Stage)rootPane.getScene().getWindow()).close();
         String url = "/application/Home.fxml";
         String title = "Movie Rental";
         db.setStage(url, title, true);
    }
    
    //saving settings
    @FXML
    private void handleSave(ActionEvent event) {
         
    	String costStr = cost.getText();
    	String maxRentedStr = maxRented.getText();
    	Settings settings = new Settings(costStr, maxRentedStr);
    	
    	if(!validateNumber(costStr)) {
    		return;
    	}
    	if(!validateNumber(maxRentedStr)) {
    		return;
    	}
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure that you want to change settings?");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK) 
        {
        	if(db.updateSettings(settings)) {
        		db.getAlert(AlertType.INFORMATION, "Saved", "Settings succesfully saved!!!");
        		refreshData();
        	}else {
        		db.getAlert(AlertType.ERROR, "Failed", "Can't save settings!!!");
        	}
        }
        else {
        	db.getAlert(AlertType.INFORMATION, "Canceled", "Save option canceled!!!");
        }
    	
    }
    
    //go one window back
    @FXML
    private void homePage(ActionEvent event) {
         ((Stage)rootPane.getScene().getWindow()).close();
         String url = "/application/Home.fxml";
         String title = "Movie Rental";
         db.setStage(url, title, true);
    }
    //validate number
    private boolean validateNumber(String txt) {
    	Pattern p = Pattern.compile("[0-9]+([.][0-9]{1,2})?");
    	Matcher m = p.matcher(txt);
    	if(m.find() && m.group().equals(txt)) {
    		return true;
    	}else {
    		db.getAlert(AlertType.WARNING, "Invalid number", "Please insert valid number!!!");
    		return false;
    	}
    }
}
