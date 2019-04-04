package application;

import java.io.IOException;
import java.net.URL;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class EditmoveController implements Initializable {
	@FXML
	private TextField id;
	@FXML
	private TextField movetype;
	@FXML
	private TextField currentroom;
	@FXML
	private TextField newroom;
	@FXML
	private TextArea search;
	@FXML
	private AnchorPane rootpane1;
	@FXML
	private DatePicker date;
	@FXML
	private ComboBox cmbmovetype;
	@FXML
	private Button btndelete;
	@FXML
	private Button btnedit;

	ObservableList<String> combomovetype = FXCollections.observableArrayList("office", "lab", "library", "teachingroom",
			"meetingroom");

	@FXML
	public void search(ActionEvent event) {
		try {
			// create mongo client
			MongoClient mongo = new MongoClient("localhost", 27017);

			// select database
			MongoDatabase database = mongo.getDatabase("FMIS");

			// select collection
			MongoCollection<Document> collection = database.getCollection("Requestmove");
			// filter document using id and select that document
			Document myDoc = collection.find(Filters.eq("movenumber", id.getText())).first();
			System.out.println("data collection selected");

			// Assign variables for needed fields
			int setdate = myDoc.getInteger("movetime");
			int setMonth = myDoc.getInteger("movemonth");
			int setYear = myDoc.getInteger("moveyear");
			
			// get the date from system
			Calendar c = Calendar.getInstance();
			int mDay = c.get(Calendar.DAY_OF_MONTH);
			int mMonth = 1 + c.get(Calendar.MONTH);
			int mYear = c.get(Calendar.YEAR);
			// calculate time period
			int dateval = mDay - (setdate);
			int monthval = mMonth - (setMonth);
			int yearval = mYear - (setYear);
			
			// check weather move can be edited or not
			if (yearval < 0) {
				movetype.setText(myDoc.getString("movetype"));
				currentroom.setText(myDoc.getString("currentroomnum"));
				newroom.setText(myDoc.getString("Newroomnum"));

			} else if (yearval == 0) {
				if (monthval < 0) {
					movetype.setText(myDoc.getString("movetype"));
					currentroom.setText(myDoc.getString("currentroomnum"));
					newroom.setText(myDoc.getString("Newroomnum"));
				}

				else if (monthval == 0) {
					if (dateval <= 0) {
						movetype.setText(myDoc.getString("movetype"));
						currentroom.setText(myDoc.getString("currentroomnum"));
						newroom.setText(myDoc.getString("Newroomnum"));
					}

				}

			}
			if (!(movetype == null)) {
				btndelete.setDisable(false);
				btnedit.setDisable(false);
			}

		} catch (final NullPointerException ex) {
			Alert message = new Alert(Alert.AlertType.INFORMATION, "Oops! something Wrong Rechcek filled data",
					ButtonType.CLOSE);
			message.showAndWait();
		}
	}

	// Method to update move dedails after edit
	@FXML
	public void update(ActionEvent event) {

		try {
			int day = date.getValue().getDayOfMonth();
			int month = date.getValue().getMonthValue();
			int year = date.getValue().getYear();
			String movetypeVal = (String) cmbmovetype.getValue();

			// Create a mongoclient
			MongoClient mongo = new MongoClient("localhost", 27017);
			// Get database
			MongoDatabase database = mongo.getDatabase("FMIS");
			// Get collection
			MongoCollection<Document> collection = database.getCollection("Requestmove");
			System.out.println("Collection myCollection selected successfully");
			// update each field using update one method
			collection.updateOne(Filters.eq("movenumber", id.getText()), Updates.set("movetime", day));
			collection.updateOne(Filters.eq("movenumber", id.getText()), Updates.set("movemonth", month));
			collection.updateOne(Filters.eq("movenumber", id.getText()), Updates.set("moveyear", year));
			collection.updateOne(Filters.eq("movenumber", id.getText()), Updates.set("movetype", movetypeVal));
			Alert message = new Alert(Alert.AlertType.INFORMATION, "Update success",
					ButtonType.CLOSE);
			message.showAndWait();
		
			//clear texts of needed fields
			id.clear();
			movetype.clear();
			currentroom.clear();
			newroom.clear();
		} catch (final NullPointerException ex) {
			Alert message = new Alert(Alert.AlertType.INFORMATION, "Oops! something Wrong Rechcek filled data",
					ButtonType.CLOSE);
			message.showAndWait();
		}
	}

	@FXML
	public void delete(ActionEvent event) {

		try {
			// create mongo client
			MongoClient mongo = new MongoClient("localhost", 27017);
			// select database
			MongoDatabase database = mongo.getDatabase("FMIS");
			// select collection to delete request
			MongoCollection<Document> collection = database.getCollection("Requestmove");
			System.out.println("Collection myCollection selected successfully");
			// select collection to update rooms occupant field from pending to vacant
			MongoCollection<Document> collection1 = database.getCollection("Rooms");
			System.out.println("Collection myCollection selected successfully");
			// alert box to confirm the process
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirm Delete");
			alert.setContentText("Are you Sure?");
            
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				collection1.updateOne(Filters.eq("roomnum", newroom.getText()), Updates.set("occupant", "vacant"));
				collection.deleteOne(Filters.eq("movenumber", id.getText()));
				System.out.println("Document deleted successfully...");

				movetype.clear();
				currentroom.clear();
				newroom.clear();
			}
		} catch (final NullPointerException ex) {
			Alert message = new Alert(Alert.AlertType.INFORMATION, "Oops! something Wrong Rechcek filled data",
					ButtonType.CLOSE);
			message.showAndWait();
		}

	}

	// method to switch the anchor pane to menu form
	@FXML
	public void menu(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		rootpane1.getChildren().setAll(pane);

	}

	@FXML
	public void clear(ActionEvent event) {
		btndelete.setDisable(true);
		

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// initialize combobox items
		cmbmovetype.setItems(combomovetype);

	}

}
