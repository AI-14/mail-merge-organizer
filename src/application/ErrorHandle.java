/* General description of the class:
 * A class made for handling all possible exceptions/user made errors.
 */

package application;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class ErrorHandle {
	
	public static void generalExceptionError() {
		Alert generalException = new Alert(AlertType.ERROR);
		generalException.setContentText("Error! Something went wrong.");
		generalException.showAndWait();
	}
	
	public static void loadTemplateError() {
		Alert fileNotLoaded = new Alert(AlertType.INFORMATION);
		fileNotLoaded.setContentText("No file has been loaded into the text area");
		fileNotLoaded.showAndWait();
	}
	
	public static void saveTemplateError() {
		Alert saveUnsuccessful = new Alert(AlertType.ERROR);
		saveUnsuccessful.setContentText("Template not saved!");
		saveUnsuccessful.showAndWait();
	}
	
	public static void aboutWindowError() {
		Alert aboutWindowCouldNotLoad = new Alert(AlertType.ERROR);
		aboutWindowCouldNotLoad.setContentText("Something went wrong :( Window could not load!");
		aboutWindowCouldNotLoad.showAndWait();
	}
	
	public static void loadContactsError() {
		Alert contactsNotLoaded = new Alert(AlertType.ERROR);
		contactsNotLoaded.setContentText("Something went wrong with loading the contact file!");
		contactsNotLoaded.showAndWait();
	}
	
	public static void documentExceptionError() {
		Alert documentError = new Alert(AlertType.ERROR);
		documentError.setContentText("Document could not be created!");
		documentError.showAndWait();
	}
	
}
