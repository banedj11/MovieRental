package application.addingmember;

import java.net.URL;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import application.memberlist.Member;
import database.Database;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;

public class MemberAddController implements Initializable{


    
    @FXML
    private AnchorPane root;
    
    @FXML
    private TextField idC;

    @FXML
    private TextField nameTf;

    @FXML
    private TextField phoneTf;

    @FXML
    private TextField emailTf;

    @FXML
    private RadioButton femaleRadio;

    @FXML
    private ToggleGroup gender;

    @FXML
    private RadioButton maleRadio;

    @FXML
    private TextArea addressTa;

    @FXML
    private DatePicker datePicker;
    
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private Database db;
 
    ResultSet rs;
    private boolean isEditable = false;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = new Database();
		setBirthDayFormat();
		
	}
    //cancel
    @FXML
    void handleCancel(ActionEvent event) {
         Stage stage = (Stage)root.getScene().getWindow();
         stage.close();
    }
    //saveing member to data
    @FXML
    void handleSave(ActionEvent event) {
        String name = nameTf.getText();
        String phone = phoneTf.getText();
        String email = emailTf.getText();
        String gender = setGender();
        String dateOfBirth = ((TextField)datePicker.getEditor()).getText();
        String address = addressTa.getText();
        
    	if(name.isEmpty() || phone.isEmpty() || email.isEmpty() || gender.isEmpty() || dateOfBirth.isEmpty() || address.isEmpty()) {
    		db.getAlert(AlertType.ERROR, "Empty field", "Field cant be empty!!!");
    		return;
    	}
    	if(isEditable) {
    		editMember();
    		return;
    	}
    	if(!validateNumber(phone)) {
    		return;
    	}
    	if(!validateEmail(email)) {
    		return;
    	}
    	String query = "INSERT INTO members VALUES(null, '"+name+"', '"+phone+"', '"+email+"', '"+gender+"', '"+dateOfBirth+"', '"+address+"')";

    	if(db.execAction(query)) {
    		db.getAlert(AlertType.INFORMATION, "Succesfully save", "Member " + name + " is succesfully added!!!");
    		nameTf.setText("");
    		phoneTf.setText("");
    		emailTf.setText("");
    		datePicker.setValue(null);
    		addressTa.setText("");
    	}else {
    		db.getAlert(AlertType.ERROR, "Saving Error", "Member adding failed!!!");
    	}
    }
    
    //seting gender
    private String setGender() {
    	if(femaleRadio.isSelected()) {
    		return "Female";
    	}else {
    		return "Male";
    	}
    }
    //seting birthday format
    private void setBirthDayFormat() {
          datePicker.setConverter(new StringConverter<LocalDate>() {
			
			@Override
			public String toString(LocalDate date) {
				if(date != null) {
				   return formatter.format(date);
			    }
			   return null;
			}
			@Override
			public LocalDate fromString(String string) {
				if(string != null && !string.trim().isEmpty()) {
					return LocalDate.parse(string, formatter);
				}
				return null;
			}
		});
    }
    //phone number validation
    private boolean validateNumber(String txt) {
    	Pattern p = Pattern.compile("[0-9]+");
    	Matcher m = p.matcher(txt);
    	if(m.find() && m.group().equals(txt)) {
    		return true;
    	}else {
    		db.getAlert(AlertType.WARNING, "Invalid number", "Please insert valid phone number!!!");
    		return false;
    	}
    }
    //email validation
    private boolean validateEmail(String txt) {
    	Pattern p = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
    	Matcher m = p.matcher(txt);
    	if(m.find() && m.group().equals(txt)) {
    		return true;
    	}else {
    		db.getAlert(AlertType.WARNING, "Invalid email", "Please insert valid email!!!");
    		return false;
    	}
    }
    //update member
    public void editMember() {
    	

    	
        int id             = Integer.parseInt(idC.getText());
    	String name        = nameTf.getText();
    	String phone       = phoneTf.getText();
    	String email       = emailTf.getText();
    	String gender      = setGender();
    	String dateOfBirth = ((TextField)datePicker.getEditor()).getText();
    	String address     = addressTa.getText();
    	
    	Member member = new Member(id, name, phone, email, gender, dateOfBirth, address);

        if(db.updateMember(member)) {
        	db.getAlert(AlertType.INFORMATION, "Success", "Member is succesfully edited!!!");
        }else {
        	db.getAlert(AlertType.ERROR, "Error", "Can't edit book!!!");
        }
		
    }
    //Seting edit UI
    public void setUI(Member member) {
    	
    	
    	idC.setText(Integer.toString(member.getId()));
    	nameTf.setText(member.getName());
    	phoneTf.setText(member.getPhone());
    	emailTf.setText(member.getEmail());
    	datePicker.getEditor().setText(member.getDateOfBirth());
    	addressTa.setText(member.getAddress());
    	if(member.getGender().equals("Male")) 
    	{
    		maleRadio.setSelected(true);
    	}else 
    	{
    		femaleRadio.setSelected(true);
    	}
    	isEditable = true;
    }


}
