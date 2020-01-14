package application.movielist;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;


public class Movie {

	private final IntegerProperty id = new SimpleIntegerProperty(this, "id", 0);
	private final StringProperty title = new SimpleStringProperty(this, "title", "");
	private final StringProperty actor = new SimpleStringProperty(this, "actor", "");
	private final StringProperty director = new SimpleStringProperty(this, "director", "");
	private final StringProperty language = new SimpleStringProperty(this, "language", "");
	private final StringProperty length = new SimpleStringProperty(this, "length", "");
	private final StringProperty genre = new SimpleStringProperty(this, "genre", "");
	private final StringProperty description = new SimpleStringProperty(this, "description", "");
	private final StringProperty availability = new SimpleStringProperty(this, "availability", "");
	
	public Movie() {
		
	}
	public Movie(int id, String title, String actor, String director,
			String language, String length, String genre, String description) {
		
		this.id.set(id);
		this.title.set(title);
		this.actor.set(actor);
		this.director.set(director);
		this.language.set(language);
		this.length.set(length);
		this.genre.set(genre);
		this.description.set(description);
		
	}
	
	public Movie(int id, String title, String actor, String director,
			String language, String length, String genre, String description, String availability) {
		
		this.id.set(id);
		this.title.set(title);
		this.actor.set(actor);
		this.director.set(director);
		this.language.set(language);
		this.length.set(length);
		this.genre.set(genre);
		this.description.set(description);
		this.availability.set(availability);
	}
	

	
	public int getId() {
		return id.get();
	}

	public String getTitle() {
		return title.get();
	}

	public String getActor() {
		return actor.get();
	}

	public String getDirector() {
		return director.get();
	}

	public String getLanguage() {
		return language.get();
	}

	public String getLength() {
		return length.get();
	}

	public String getGenre() {
		return genre.get();
	}

	public String getDescription() {
		return description.get();
	}
	
	public String getAvailability() {
		return availability.get();
	}
	
}
