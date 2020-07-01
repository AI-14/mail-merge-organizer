/* General description of the class:
 * This class displays a view which contains a text area for the user to type in the subject of the email.
 * Then it sets that subject.
 */

package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EmailSubjectController extends Application implements Initializable {
	@FXML
	private Label label;
	
	@FXML
	private TextField subject;
	
	@FXML
	private Button OK;
	
	@FXML
	public void setSubject() {
		setSubjectFunction();
	}
	
	@FXML
	public void OKAction(ActionEvent event) {
		OKActionFunction();
	}
	
	public static String content = "";
	
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("EmailSubjectView.fxml"));
			Scene scene = new Scene(root);
			stage.setTitle("Email - Subject");
			stage.setScene(scene);
			stage.showAndWait();
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}
		
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		label.setText("Enter subject of the Email: ");
	}
	
	public void setSubjectFunction() {
		content = subject.getText();
	}
	
	public void OKActionFunction() {
		setSubjectFunction();
		Stage stage = (Stage) OK.getScene().getWindow();
	    stage.close();
	}
	
}
