/* General description of the class:
 * This class adds the state of the email (sent successfully or not) in a list view and displays that list to the user.
 */
package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class EmailStateController extends Application implements Initializable {

	@FXML
	private ListView<String> emailSF;
	
	private ObservableList<String> list = FXCollections.observableArrayList();
	
	@Override
	public void start(Stage stage)  {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("EmailStateView.fxml"));
			Scene scene = new Scene(root);
			stage.setTitle("Email State");
			stage.setScene(scene);
			stage.show();
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}
	}
		
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		int len = MainWindowController.emailState.size();
		for(int i=0; i<len; i++) 
			list.add(MainWindowController.emailState.get(i));
		
		emailSF.setItems(list);
	}
	
}
