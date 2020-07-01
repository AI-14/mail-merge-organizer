/* General description of the class:
 * This class acts as the controller class. It has all the functions that handles the functionality of the components present.
 * Majority of the functionalities take place in this class like loading template, saving them, loading contacts, sending email and creating pdf etc.
 */

package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Scanner;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainWindowController implements Initializable {
	
	@FXML
	private MenuItem loadTemplate, saveTemplateAs, close, about;
	
	@FXML
	public TextArea textArea = new TextArea(); 
	
	@FXML
	private Button clearAll, loadContacts, createPDF, sendEmails;
	
	@FXML
	private ComboBox<String> tags = new ComboBox<>();
	
	//Initializing some frequently used global variables for easy access.
	MainWindow mainWindow = new MainWindow();
	private File file;
	private FileChooser fileChooser = new FileChooser();
	private Scanner fileScan;
	private ArrayList<String> tagsContent = new ArrayList<String>();
	public static ArrayList<String> emailState = new ArrayList<String>();
	private ObservableList<String> tagsItems = FXCollections.observableArrayList();
	private String tempTags, contactCsvPath;
	private String[] splitTags;
	private int[] serialNum;
	
	@FXML
	public void loadTemplateAction()  {
		loadTemplateFunction();
	}
	
	@FXML
	public void saveTemplateAsAction() {
		saveTemplateAsFunction();
	}
	
	@FXML
	public void closeAction() {
		closeFunction();
	}
	
	@FXML
	public void aboutAction() {
		aboutFunction();
	}
	
	@FXML
	public void clearAllAction() {
		clearFunction();
	}
	
	@FXML
	public void printTagsOnTextArea() {
		printTagsOnTextAreaFunction();
	}
	
	@FXML
	public void loadContactsAction() {
		loadContactsFunction();
	}
	
	@FXML
	public void createPDFAction() {
		createPDFFunction();
	}
	
	@FXML
	public void sendEmailAction() {
		if(emailExists()) {
			sendEmailFunction();
		}
		else {
			Alert message = new Alert(AlertType.INFORMATION);
			message.setContentText("Could not find any attribute of type [[Email Address]].");
			message.showAndWait();	
		}
	}
	
	
	//This method initially disables PDF, placeholder, and email functions until a contacts file is loaded.
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		createPDF.setDisable(true);
		sendEmails.setDisable(true);
		tags.setDisable(true);
	}
	
	//Methods for loading and saving user inputed work. Gives an alert if no file is chosen in either save or load case/other file related problems.
	public void loadTemplateFunction() {
		try {
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
			file = fileChooser.showOpenDialog(mainWindow.getStage());
			String templatePath = file.getAbsolutePath();
			fileScan = new Scanner(new FileInputStream(templatePath));
			String content = "";
			while(fileScan.hasNextLine()) {
				content += fileScan.nextLine();
				content += "\n";
			}
			fileScan.close();
			textArea.setText(content);
			Alert loadSuccessful = new Alert(AlertType.INFORMATION);
			loadSuccessful.setContentText("Template loaded into the text area successfully!");
			loadSuccessful.showAndWait();
		}
		catch(Exception e) {
			ErrorHandle.loadTemplateError();
		}
	}
	
	//This method lets the user save the template in user-defined directory.
	public void saveTemplateAsFunction() {
		try {
			String text = textArea.getText();
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (.txt)", "*.txt");
			fileChooser.getExtensionFilters().add(extFilter);
			file = fileChooser.showSaveDialog(mainWindow.getStage());
			PrintWriter write = new PrintWriter(new FileOutputStream(file));
			write.print(text);
			write.close();
			Alert saveSuccessful = new Alert(AlertType.INFORMATION);
			saveSuccessful.setContentText("Template saved successfully!");
			saveSuccessful.showAndWait();
		}
		catch(Exception e) {
			ErrorHandle.saveTemplateError();
		}
	}
	
	//Method that closes the main window. A confirmation box will always appear before closing so that the user can save, exit or continue their work.
	public void closeFunction() {
		Alert unsavedChanges = new Alert(AlertType.CONFIRMATION);
		unsavedChanges.setTitle("Confirmation");
	    unsavedChanges.setContentText("Unsaved Changes may exist within the program. Choose your option.");
	    ButtonType buttonTypeOne = new ButtonType("SAVE TEMPLATE");
        ButtonType buttonTypeTwo = new ButtonType("CANCEL");
        ButtonType buttonTypeThree = new ButtonType("EXIT");
        unsavedChanges.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo, buttonTypeThree);
	    Optional<ButtonType> result = unsavedChanges.showAndWait();
	    if (result.get() == buttonTypeOne) 
	 	    saveTemplateAsFunction();
	 	else if (result.get() == buttonTypeTwo)
	 	    unsavedChanges.close();
	 	else if(result.get() == buttonTypeThree) {
	 	    Platform.exit();
	 		System.exit(0);
	    }
	}
	
	//Method that loads a new stage that contains an image view containing the description of the mail merge application and developers information.
	public void aboutFunction() {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("AboutView.fxml"));
			Stage aboutStage = new Stage();
			Scene aboutScene = new Scene(root);
			aboutStage.setTitle("About");
			aboutStage.setScene(aboutScene);
			aboutStage.initModality(Modality.WINDOW_MODAL);
			aboutStage.show();
		}
		catch(Exception e) {
			ErrorHandle.aboutWindowError();
		}
	}
	
	//This method clears the text area. Linked to "clear all" button.
	public void clearFunction() {
		textArea.clear();
	}
	
	//This method loads a combobox based on the contacts file and can print any of the list's choices onto the text area at any position.
	public void printTagsOnTextAreaFunction() {
		try {
		String tag = (String) tags.getValue();
		textArea.insertText(textArea.getCaretPosition(), tag);
		}
		catch(NullPointerException ignore) {
			
		}
	}
	
	//Loads contacts file and loads the placeholders into the "tags" combobox.
	public void loadContactsFunction() {
		try {
			FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV files (.csv)", "*.csv");
			fileChooser.getExtensionFilters().add(extFilter);
			file = fileChooser.showOpenDialog(mainWindow.getStage());
			contactCsvPath = file.getAbsolutePath();
			fileScan = new Scanner(new FileInputStream(contactCsvPath));
			tempTags = fileScan.nextLine();
			splitTags = tempTags.split(",");
			for(String tag : splitTags) {
				if(tag.contains("[[") && tag.contains("]]"))
					tagsContent.add(tag.toUpperCase());
				else 
					tagsContent.add("[[" + tag.toUpperCase() + "]]");
			}
			for(int i=0; i<tagsContent.size(); i++) {
				tagsItems.add(tagsContent.get(i));
			}
			tags.setItems(tagsItems);
			Contacts.list(tagsContent, contactCsvPath);
			boolean yesLoaded = true;
			if(yesLoaded)
				enableButtons();
			else
				disableButtons();
			
			ContactListController clc = new ContactListController();
			ContactListController.filePath = contactCsvPath;
			clc.start(new Stage());
			loadContacts.setDisable(true);
		}
		catch(Exception e) {
			ErrorHandle.loadContactsError();
		}
	}
	
	//Enables previously disabled functions.
	public void enableButtons() {
		createPDF.setDisable(false);
		sendEmails.setDisable(false);
		tags.setDisable(false);
	}
	
	//Disables the specified functions.
	public void disableButtons() {
		createPDF.setDisable(true);
		sendEmails.setDisable(true);
		tags.setDisable(true);
	}
	
	//Creates a PDF file based on user-selected contacts and adjusts the number of pages according to the number of contacts. It contains the merged templates.
	public void createPDFFunction() {
		try {
			if(textArea.getText().length() != 0 && (ContactListController.msg.equals("all") || ContactListController.msg.equals("not all"))) {
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (.pdf)", ".pdf");
				fileChooser.getExtensionFilters().add(extFilter);
				file = fileChooser.showSaveDialog(mainWindow.getStage());
				String pdfPath = file.getAbsolutePath();
				OutputStream pdfFile = new FileOutputStream(new File(pdfPath));
				Document document = new Document();
				PdfWriter.getInstance(document, pdfFile);
				document.open();
				if(ContactListController.msg.equals("all")) {
					int numOfContacts = Contacts.ContactList.size();
					for(int i=0; i<numOfContacts; i++) {
						document.add(new Paragraph(getMergedTemplate(i)));
						document.newPage();
					}  
					document.close();
					pdfFile.close();
					Alert pdfSaveSuccessful = new Alert(AlertType.INFORMATION);
					pdfSaveSuccessful.setContentText("PDF created successfully!");
					pdfSaveSuccessful.showAndWait();
					ContactListController clc = new ContactListController();
					clc.clearContactList();
				}
				else if(ContactListController.msg.equals("not all")) {
					serialNum = new int[ContactListController.selectedContactData.size()]; //Keeping track of number of selected items from the list view.
					for(int i=0; i<serialNum.length; i++) {
						serialNum[i] = Integer.parseInt(ContactListController.selectedContactData.get(i).substring(0,1));
					} 
					int numOfContacts = serialNum.length;
					for(int i=0; i<numOfContacts; i++) {
						document.add(new Paragraph(getMergedTemplate(serialNum[i]-1))); 
						document.newPage();
					}  
					document.close();
					pdfFile.close();
		
					Alert pdfSaveSuccessful = new Alert(AlertType.INFORMATION);
					pdfSaveSuccessful.setContentText("PDF created successfully!");
					pdfSaveSuccessful.showAndWait();
					ContactListController clc = new ContactListController();
					clc.clearContactList();
				}
			}
			else {
				Alert emptyPDFAlert = new Alert(AlertType.INFORMATION);
				emptyPDFAlert.setContentText("Either text area is empty or no contacts have been selected.");
				emptyPDFAlert.showAndWait();
			}
		}
		catch(DocumentException e) {
			ErrorHandle.documentExceptionError();
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}
	}

	//First asks for user authentication; if successful, it sets the port to suit the specified email address of each user-selected contact and sends each merged template
	//to the specified contacts.
	public void sendEmailFunction() {
		try {
			if(textArea.getText().length() != 0 && (ContactListController.msg.equals("all") || ContactListController.msg.equals("not all"))) {
				EmailSubjectController eSubjectObj = new EmailSubjectController();
				eSubjectObj.start(new Stage());
			
				PasswordDialog passwordDialogObj = new PasswordDialog();
				passwordDialogObj.start(new Stage());
		
				String userName = PasswordDialog.login.getUserName(),
					   password = PasswordDialog.login.getPassword(),
				       toEmail = "",
				       subject = EmailSubjectController.content,
				       messageToBeSent = "";
				if(ContactListController.msg.equals("all")) {
					int numOfContacts = Contacts.ContactList.size();
					for(int i=0; i<numOfContacts; i++) {
						messageToBeSent = getMergedTemplate(i);
						toEmail = getEmailAddress(i);
						SendEmailOffice365 mailer =  new SendEmailOffice365(userName, password , toEmail, subject, messageToBeSent);
						if (mailer.sendEmail()) {
							emailState.add("Email was sent to " + toEmail + " successfully");
						}
						else {
							emailState.add("Failed to send Email to " + toEmail);
						}
					}
					ContactListController clc = new ContactListController();
					clc.clearContactList();
				}
				else if(ContactListController.msg.equals("not all")) {
					serialNum = new int[ContactListController.selectedContactData.size()]; //Keeping track of number of selected items from the list view.
					for(int i=0; i<serialNum.length; i++) {
						serialNum[i] = Integer.parseInt(ContactListController.selectedContactData.get(i).substring(0,1));
					} 
					int numOfContacts = serialNum.length;
					for(int i=0; i<numOfContacts; i++) {
						messageToBeSent = getMergedTemplate(serialNum[i]-1);
						toEmail = getEmailAddress(serialNum[i]-1);
						SendEmailOffice365 mailer =  new SendEmailOffice365(userName, password , toEmail, subject, messageToBeSent);
						if (mailer.sendEmail()) {
							emailState.add("Email was sent to " + toEmail + " successfully");
						}
						else {
							emailState.add("Failed to send Email to " + toEmail);
						}
					}
					ContactListController clc = new ContactListController();
					clc.clearContactList();
				}
				EmailStateController emailStateObj = new EmailStateController();
				emailStateObj.start(new Stage());
			}
			else {
				Alert emptyEmailAlert = new Alert(AlertType.INFORMATION);
				emptyEmailAlert.setContentText("Either text area is empty or no contacts have been selected.");
				emptyEmailAlert.showAndWait();
			}
		}
		catch(Exception e) {
			ErrorHandle.generalExceptionError();
		}
	}
	
	//Method that replaces the placeholders in the template with the attributes of each specified contact.
	public String getMergedTemplate(int i) {
		String textAreaContent = textArea.getText();
    	for(int j=0; j<Contacts.attributes.size(); j++) {
    		String attribute = Contacts.attributes.get(j);
    		int index = Contacts.attributes.indexOf(attribute);
    		String contactData = Contacts.ContactList.get(i).get(index);
    		textAreaContent = textAreaContent.replace(attribute, contactData);		
        }
    	return textAreaContent;
	}
	
	//Method that returns email address if it is within the attributes of the contact.
	public String getEmailAddress(int i) {
		for(int j=0; j<Contacts.attributes.size(); j++) {
			String attribute = Contacts.attributes.get(j);
			attribute = attribute.toLowerCase();
			if(attribute.contains("email")) {
				return Contacts.ContactList.get(i).get(j);
			}
		}
		return "";
	}
	
	//Method that checks if an attribute of email address exists in the contact file.
	public boolean emailExists() {
		boolean exists = false;
		for(int j=0; j<Contacts.attributes.size(); j++) {
			String attribute = Contacts.attributes.get(j);
			attribute = attribute.toLowerCase();
			if(attribute.contains("email")) {
				exists = true;
			}
		}
		return exists;
	}
	
}
