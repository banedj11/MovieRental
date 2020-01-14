package application.memberlist;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;
import application.addingmember.MemberAddController;
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

public class MemberListController implements Initializable{

    @FXML
    public Button backBtn;

    @FXML
    public Button backBtn1;
    
    @FXML
    private AnchorPane rootPane;
    
    @FXML
    private TextField searchBar;

    @FXML
    private TableView<Member> tableView;

    @FXML
    private TableColumn<Member, String> idC;

    @FXML
    private TableColumn<Member, String> nameC;

    @FXML
    private TableColumn<Member, String> phoneC;

    @FXML
    private TableColumn<Member, String> emailC;

    @FXML
    private TableColumn<Member, String> genderC;

    @FXML
    private TableColumn<Member, String> dateC;

    @FXML
    private TableColumn<Member, String> addressC;

    Database db;
    Connection conn;
    ResultSet rs;
    ObservableList<Member> list = FXCollections.observableArrayList();
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		db = new Database();
		initTable();
		loadMemberList();
	}
	
    //load data into table
	private void loadMemberList() {
		list.clear();
		conn = db.connect();
		String query = "SELECT * FROM members";
		rs = db.execQuery(query);
		try {
			while(rs.next()) {
				int idM = rs.getInt("member_id");
				String nameM    = rs.getString("name");
				String phomeM   = rs.getString("mobile");
				String emailM   = rs.getString("email");
				String genderM  = rs.getString("gender");
				String dateM    = rs.getString("birthday");
				String addressM = rs.getString("address");
				list.add(new Member(idM, nameM, phomeM, emailM, genderM, dateM, addressM));
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		tableView.setItems(list);
	}
	//connecting table with model
	private void initTable() {
		idC.setCellValueFactory(new PropertyValueFactory<>("id"));
		nameC.setCellValueFactory(new PropertyValueFactory<>("name"));
		phoneC.setCellValueFactory(new PropertyValueFactory<>("phone"));
		emailC.setCellValueFactory(new PropertyValueFactory<>("email"));
		genderC.setCellValueFactory(new PropertyValueFactory<>("gender"));
		dateC.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
		addressC.setCellValueFactory(new PropertyValueFactory<>("address"));
	}
	
	//deleting member from database
    @FXML
    private void handleDelete(ActionEvent event) {
    	conn = db.connect();
        Member selectMember = tableView.getSelectionModel().getSelectedItem();
        if(selectMember == null) 
        {
        	db.getAlert(AlertType.ERROR, "No Selection", "Nothing is select to delete");
        	return;
        }
        boolean boo = db.hasMemberRentedMovie(selectMember);
        if(boo) 
        {
        	db.getAlert(AlertType.WARNING, "Alredy Rented", "Can't delete member " +selectMember.getName()+ ", the rented the movie");
        	return;
        }
        String query = "DELETE FROM members WHERE member_id='"+selectMember.getId()+"'";
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("Are you sure that you want to delete member " + selectMember.getName() + "?");
        Optional<ButtonType> answer = alert.showAndWait();
        if(answer.get() == ButtonType.OK) 
        {
        	if(db.execAction(query)) 
        	{
        		db.getAlert(AlertType.INFORMATION, "Deleted", "Member " + selectMember.getName() + " is deleted!!!");
        		list.removeAll(selectMember);
        	}
        	else 
        	{
        		db.getAlert(AlertType.WARNING, "Warning", "Member " + selectMember.getName() + " cant be deleted");
        	}
        }
        else 
        {
        	db.getAlert(AlertType.INFORMATION, "Canceled", "Delete option canceled!!!");
        }
    }

    @FXML
    private void handleEdit(ActionEvent event) {

        Member selected = tableView.getSelectionModel().getSelectedItem();
        if(selected == null) 
        {
        	db.getAlert(AlertType.ERROR, "No Selection", "Nothing is select to edit");
        	return;
        }

        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/addingmember/MemberAdd.fxml"));
        	Parent root = loader.load();
        	MemberAddController controller = (MemberAddController)loader.getController();
        	controller.setUI(selected);
        	Stage stage = new Stage(StageStyle.DECORATED);
        	stage.setScene(new Scene(root));
        	stage.setTitle("Edit Member");
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
    	loadMemberList();
    }

    @FXML
    private void searchMovie(KeyEvent event) {
    	
        conn = db.connect();
        if(searchBar.getText().equals("")) 
        {
        	loadMemberList();
        }
        else 
        {
        	list.clear();
            String query = "SELECT * FROM members WHERE member_id LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM members WHERE name LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM members WHERE mobile LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM members WHERE email LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM members WHERE gender LIKE '"+searchBar.getText()+"%'"
            	  + "UNION  SELECT * FROM members WHERE address LIKE '"+searchBar.getText()+"%'";
            rs = db.execQuery(query);
            try {
            	while(rs.next()) {
            		
    				int idM         = rs.getInt("member_id");
    				String nameM    = rs.getString("name");
    				String phomeM   = rs.getString("mobile");
    				String emailM   = rs.getString("email");
    				String genderM  = rs.getString("gender");
    				String dateM    = rs.getString("birthday");
    				String addressM = rs.getString("address");
    				list.add(new Member(idM, nameM, phomeM, emailM, genderM, dateM, addressM));
    				
                }
            	tableView.setItems(list);
            }
            catch(Exception ex) {
            	ex.printStackTrace();
            }
        }
    }
    @FXML
    void loadAddMember(ActionEvent event) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/addingmember/MemberAdd.fxml"));
        	Parent root = loader.load();
        	MemberAddController controller = (MemberAddController)loader.getController();
        	Stage stage = new Stage(StageStyle.DECORATED);
        	stage.setScene(new Scene(root));
        	stage.setResizable(false);
        	stage.setTitle("Add Member");
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
