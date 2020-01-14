package application.memberlist;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Member {

	private final IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
	private final StringProperty name = new SimpleStringProperty(this, "name", "");
	private final StringProperty phone = new SimpleStringProperty(this, "phone", "");
	private final StringProperty email = new SimpleStringProperty(this, "email", "");
	private final StringProperty gender = new SimpleStringProperty(this, "gender", "");
	private final StringProperty dateOfBirth = new SimpleStringProperty(this, "dateOfBirth", "");
	private final StringProperty address = new SimpleStringProperty(this, "address", "");
	
	public Member() {
		
	}
	public Member(int id, String name, String phone, String email, String gender, String dateOfBirth, String address) {
		this.id.set(id);
		this.name.set(name);
		this.phone.set(phone);
		this.email.set(email);
		this.gender.set(gender);
		this.dateOfBirth.set(dateOfBirth);
		this.address.set(address);
	}
	//getters
	public int getId() {
		return id.get();
	}
	public String getName() {
		return name.get();
	}
	public String getPhone() {
		return phone.get();
	}
	public String getEmail() {
		return email.get();
	}
	public String getGender() {
		return gender.get();
	}
	public String getDateOfBirth() {
		return dateOfBirth.get();
	}
	public String getAddress() {
		return address.get();
	}
	
}
