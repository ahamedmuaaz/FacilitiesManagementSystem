package application;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream.Filter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.QueryBuilder;
import com.mongodb.client.FindIterable;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;

public class RequestController implements Initializable {
	// create observablelists to set items to the combobox
	ObservableList<String> movetype = FXCollections.observableArrayList("office", "lab", "library", "teachingroom",
			"meetingroom");
	ObservableList<String> department = FXCollections.observableArrayList("science", "it");

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// initialize combobox items
		departments.setItems(department);
		movetyp.setItems(movetype);

	}

	@FXML
	private AnchorPane rootpane1;
	@FXML
	private Button menu;
	@FXML
	private Button add;
	@FXML
	private Button generate;

	@FXML
	private TextField txt1;
	@FXML
	private TextField txtnroom;
	@FXML
	private TextField txtoroom;

	@FXML
	private DatePicker date;
	@FXML
	private ComboBox movetyp;
	@FXML
	private ComboBox departments;
	@FXML
	private Button btn1;
	@FXML
	private TextArea txtarea;

	// method to switch anchorpane to menuform when clicked
	@FXML
	public void menu(ActionEvent event) throws IOException {

		AnchorPane pane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		rootpane1.getChildren().setAll(pane);

	}

	// Creating a Mongo client
	MongoClient mongo = new MongoClient("localhost", 27017);

	// Accessing the database
	MongoDatabase database = mongo.getDatabase("FMIS");

	// ADD function for databse

	@FXML
	public void add(ActionEvent event) {

		try {

			// Retrieving a collection
			MongoCollection<Document> collection = database.getCollection("Requestmove");
			System.out.println("Collection myCollection selected successfully");
			// getting the id and move type text from text fileds
			String num = txt1.getText();
			String num1 = (String) movetyp.getValue();
			// getting date from datepicker and saving it to mont year and date seperately
			int day = date.getValue().getDayOfMonth();
			int month = date.getValue().getMonthValue();
			int year = date.getValue().getYear();

			// insert document by getting all needed datas
			Document document = new Document("movenumber", num).append("movetype", num1).append("movetime", day)
					.append("movemonth", month).append("moveyear", year).append("currentroomnum", txtoroom.getText())
					.append("Newroomnum", txtnroom.getText());
			collection.insertOne(document);
			System.out.println("Document inserted successfully");
			// clear needed fields
			txt1.clear();
			txtarea.clear();

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Move Request Made Successfully");
			alert.showAndWait();

			// getting new room number to update status of newroom
			String roomnum = txtnroom.getText();

			// Retrieving a collection
			MongoCollection<Document> collection2 = database.getCollection("Rooms");
			System.out.println("Collection myCollection selected successfully");

			// update the status of newroom from vacant to pending
			collection2.updateOne(Filters.and(Filters.eq("roomnum", roomnum), Filters.eq("occupant", "vacant")),
					Updates.set("occupant", "pending"));
			Document x = collection2.find(Filters.eq("roomnum", txtnroom.getText())).first();

		} catch (final NullPointerException ex) {
			// pop up error message if anything goes wrong
			Alert message = new Alert(Alert.AlertType.INFORMATION, "Oops! something Wrong Rechcek filled data",
					ButtonType.CLOSE);
			message.showAndWait();
		}

	}

	// GENERATE ID function
	@FXML
	public void generate(ActionEvent event) throws IOException {

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("Requestmove");

		long count = collection.count();

		txt1.setText(String.valueOf((count + 1)));

	}

	// CHECK vacant room function
	@FXML
	public void chceck(ActionEvent event) {
		// getting details from comboboxes and saving to variables
		String spacetype = (String) movetyp.getValue();
		String occupant = (String) departments.getValue();

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("Rooms");

		// show all rooms that a vacant under conditions from comboboxes
		Iterator<Document> x = collection.find(Filters.and(Filters.eq("spacetype", spacetype),
				Filters.eq("occupant", "vacant"), Filters.eq("occupydept", occupant))).iterator();
		int i = 1;
		while (x.hasNext()) {

			Document y = x.next();
			String z = y.getString("roomnum");
			String h = y.getString("floor");
			// appending the datas into text area
			txtarea.appendText(" \n ");
			txtarea.appendText(i + ") " + "Room number:" + z);
			txtarea.appendText("   ");
			txtarea.appendText("floor Number:" + h);

			if (!(txtarea.getText() == null)) {
				add.setDisable(false);
			}
			i = i + 1;
		}

	}

	// function to clear text area
	@FXML
	public void clear(ActionEvent event) {
		txtarea.clear();
	}
}
