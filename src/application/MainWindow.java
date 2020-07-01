/*************************************************************************************
 * Information and Computer Science (ICS) Department                                 *
 * College of Computer Sciences and Engineering (CCSE)                               *
 * King Fahd University of Petroleum and Minerals - KFUPM                            *
 * Dhahran, Saudi Arabia                                                             *
 *                                                                                   *
 * Course - ICS 201 / Term 191                         								 *
 * Project Name - MAIL MERGE ORGANIZER                                               *
 *                                                                                   *
 * @Authors Amaan Izhar (ID: 201781130) and AbdulJawad Mohammed (ID: 201744310)      *
 * @Instructors Prof. Andri Mirzal (LEC) and Mr. AbdulMajeed Alothman (LAB)          *
 *************************************************************************************/


/* General description of the class:
 * Loads the main view that contains all the components that are used in the application.
 * Handles window event (closing).
 */

package application;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MainWindow extends Application {
	
	private Stage primaryStage;
	private Scene primaryScene;
	public static MainWindowController mwc = new MainWindowController();
	
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			this.primaryStage.setTitle("Mail Merge Organizer");
			showMainWindow();
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}
	}
	
	public void showMainWindow() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("MainWindowView.fxml"));
			primaryScene = new Scene(loader.load());
			primaryStage.setScene(primaryScene);
			primaryStage.show();
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					event.consume();
					mwc.closeFunction();
				}
			});
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}
	}
	
	public Stage getStage() {
		return primaryStage;
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
