package application.settings;

import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;


public class ChangePasswordController implements Initializable{

	@FXML
	private AnchorPane rootPane;
	
    @FXML
    private PasswordField retPassword;

    @FXML
    private PasswordField newPassword;

    Database db;
    ResultSet rs;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = new Database();
	}
    //changing password
    @FXML
    void handleSave(ActionEvent event) {

    	String newPass = newPassword.getText();
    	String retPass = retPassword.getText();
    	
    	String query = "UPDATE admin SET password = '"+newPass+"' where admin_id = 1";
    	if(newPass.isEmpty() || retPass.isEmpty()) {
    		db.getAlert(AlertType.ERROR, "Empty Field", "Field can't be empty!!!");
    		return;
    	}
    	if(!newPass.equals(retPass)) {
    		db.getAlert(AlertType.INFORMATION, "Failed", "Wrong retyped password!!!");
    	}else {
    		if(db.execAction(query)) {
    			db.getAlert(AlertType.INFORMATION, "Success", "Succesfully changed password!!!");
    		}else {
    			db.getAlert(AlertType.ERROR, "Failed", "Changing password failed!!!");
    		}
    	}
    }
    //get back to settings page
    @FXML
    void homePage(ActionEvent event) {
    	db.closeStage(rootPane);
    	db.setStage("/application/settings/Settings.fxml", "Settings", false);
    }
    //canceling pasword changes and getting back to settings page
    @FXML
    void handleCancel(ActionEvent event) {
        db.closeStage(rootPane);
        db.setStage("/application/settings/Settings.fxml", "Settings", false);
    }
}
