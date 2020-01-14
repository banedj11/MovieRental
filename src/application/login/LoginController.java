package application.login;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoginController implements Initializable{

    @FXML
    private AnchorPane rootPane;
	
    @FXML
    private TextField usernameTf;

    @FXML
    private PasswordField passwordTf;

    @FXML
    private Label labelError;
    
    Database db;
    ResultSet rs;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = new Database();
	}

    @FXML
    void handleCancel(ActionEvent event) {
         ((Stage)rootPane.getScene().getWindow()).close();
    }

    @FXML
    void handleLogin(ActionEvent event) {

    	String username = usernameTf.getText();
    	String password = passwordTf.getText();
    	String query = "SELECT * FROM admin WHERE username = '"+username+"' and password = '"+password+"'";
    	rs = db.execQuery(query);
    	
    	try {
    		if(username.isEmpty()) {
    			labelError.setText("Please enter username!!!");
        		return;
        	}
        	if(password.isEmpty()) {
        		labelError.setText("Please enter password!!!");
        		return;
        	}
			if(!rs.next()) {
				labelError.setText("Wrong username/password!!!");
			}
			else {
				db.getAlert(AlertType.INFORMATION, "Succes", "Succesfully Loged in!!!");
				db.closeStage(rootPane);
				db.setStage("/application/Home.fxml", "Movie Rental", true);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
}
