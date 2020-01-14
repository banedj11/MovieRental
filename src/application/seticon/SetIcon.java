package application.seticon;

import javafx.scene.image.Image;
import javafx.stage.Stage;

public class SetIcon {

	public static final String IMAGE_LOCATION = "/icon/icon.png";
	
	public static void setStageIcon(Stage stage) {
		
	     stage.getIcons().add(new Image(IMAGE_LOCATION));
	}
}
