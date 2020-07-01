/* General description of the class:
 * If user wants to add a contact in the contact list, he can do so by clicking on addContact button.
 * This class serves as a controller class for adding a new contact. 
 * Displays the list view of all the contacts in csv form.
 * It also displays some text fields to add details for the new contact (if add contact button is clicked) and then adds it to the original excel file and updates the list view
 * for selecting either "all" or "selected items" only. 
 */

package application;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ContactListController extends Application implements Initializable{

	@FXML
	private ListView<String> list;
	
	@FXML
	private Button submitAll, submitSelectedItems, addContact;
	
	@FXML
	private TextField contactField;
	
	private MainWindow mw = new MainWindow();
	private ObservableList<String> contactData = FXCollections.observableArrayList();
	public static ObservableList<String> selectedContactData = FXCollections.observableArrayList();
	public static String msg = "";
	public static String filePath;
	private String newContactData = "";
	private ArrayList<Label> arrLabel = new ArrayList<>();
	private ArrayList<TextField> arrTf = new ArrayList<>();
	private VBox vb;
	private HBox hb;
	
	@FXML
	public void submitAllAction() {
		submitAllFunction();
	}
	
	@FXML
	public void submitSelectedItemsAction() {
		submitSelectedItemsFunction();
	}
	
	@FXML
	public void addContactAction() {
		addContactFunction();
	}
	
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("ContactListView.fxml"));
			Scene scene = new Scene(root);
			stage.setTitle("Contact List");
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(mw.getStage());
			stage.showAndWait();
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		for(int i=0; i<Contacts.ContactList.size(); i++) {
			String temp = Contacts.ContactList.get(i).toString();
			contactData.add((i+1) + ". " + temp);	
		}
		
		list.setItems(contactData);
		list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	}
	
	public void submitAllFunction() {
		msg = "all";
		Stage stage = (Stage) submitAll.getScene().getWindow();
	    stage.close();
	}
	
	public void submitSelectedItemsFunction() {
		msg = "not all";
		selectedContactData = list.getSelectionModel().getSelectedItems();
		Stage stage = (Stage) submitSelectedItems.getScene().getWindow();
	    stage.close();
	}
	
	public void addContactFunction() {
		try {
			Stage stage = new Stage();
			vb = new VBox();
			for(int i=0 ;i<Contacts.attributes.size(); i++) {
				arrLabel.add(new Label(Contacts.attributes.get(i)));
				arrTf.add(new TextField());
				hb = new HBox(arrLabel.get(i), arrTf.get(i));
				hb.setPadding(new Insets(15, 15, 15, 15));
				vb.getChildren().add(hb);
			}
			Button add = new Button("Add");
			vb.getChildren().add(add); 
			add.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					getAddedData();	
					stage.close();
				}
			});
		
			Scene scene = new Scene(vb);
			stage.setScene(scene);
			stage.show();
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}
	}
	
	public void getAddedData() {
		for(int i=0; i<arrLabel.size()-1; i++) {
			newContactData += arrTf.get(i).getText() + ",";
		}
		newContactData += arrTf.get(arrTf.size()-1).getText();
		addContactInCsvFile();
	}
	
	public void addContactInCsvFile() {
		try {
			Scanner fileScan = new Scanner(new FileInputStream(filePath));
			ArrayList<String> temp = new ArrayList<String>();
			while(fileScan.hasNextLine()) {
				temp.add(fileScan.nextLine());
			}
			fileScan.close();

			PrintWriter write = new PrintWriter(new FileOutputStream(filePath), true);
			for(int i=0; i<temp.size(); i++) {
				write.print(temp.get(i) + "\n");
			}
			write.print(newContactData);
			write.close();
			addInContacts(newContactData);
			int newIndex = contactData.size();
			contactData.add((newIndex+1) + ". " + Contacts.ContactList.get(Contacts.ContactList.size()-1).toString());
			list.setId(contactData.get(contactData.size()-1));
			
			Alert addSuccessful = new Alert(AlertType.INFORMATION);
		    addSuccessful.setContentText("Contact Created.");
			addSuccessful.showAndWait();
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}	
	}
	
	public void addInContacts(String newContactData) {
		String[] temp = newContactData.split(",");
		ArrayList<String> data = new ArrayList<>();
		for(String element : temp)
			data.add(element);
		Contacts.ContactList.add(data);
	}
	
	public void clearContactList() {
		Contacts.ContactList.clear();
		Contacts.attributes.clear();
		contactData.clear();
	}
	
}
