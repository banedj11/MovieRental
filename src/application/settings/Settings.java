package application.settings;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Settings {

	private final StringProperty costS = new SimpleStringProperty(this, "costS", "");
	private final StringProperty maximumMoviesS = new SimpleStringProperty(this, "maximumMoviesS", "");
	
	public Settings() {
		
	}
	public Settings(String costS, String maximumMoviesS) {
		this.costS.set(costS);
		this.maximumMoviesS.set(maximumMoviesS);
	}
	//getters
	public String getCostS() {
		return costS.get();
	}
	public String getMaximumMoviesS() {
		return maximumMoviesS.get();
	}
	
}
