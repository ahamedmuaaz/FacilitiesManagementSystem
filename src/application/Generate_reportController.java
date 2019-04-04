package application;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Scanner;

import javax.swing.DesktopManager;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

public class Generate_reportController implements Initializable {
	@FXML
	private ComboBox cmbBuild;
	@FXML
	private ComboBox cmbFloor;
	@FXML
	private TextArea txtarea;
	@FXML
	private AnchorPane rootpane1;

	// to set items of combobox

	ObservableList<String> build = FXCollections.observableArrayList("10", "12", "18");
	ObservableList<String> floornum10 = FXCollections.observableArrayList("1", "2");
	ObservableList<String> floornum12 = FXCollections.observableArrayList("0", "1");
	ObservableList<String> floornum18 = FXCollections.observableArrayList("0", "1");

	MongoClient mongo = new MongoClient("localhost", 27017);

	MongoDatabase database = mongo.getDatabase("FMIS");

	// method to initialize 2nd combobox items when first one is selected
	@FXML
	public void getfloornum(ActionEvent event) {

		String building_No = (String) cmbBuild.getValue();

		switch (building_No) {

		case "10": {
			cmbFloor.setItems(floornum10);
			break;
		}
		case "12": {
			cmbFloor.setItems(floornum12);
			break;
		}
		case "18": {
			cmbFloor.setItems(floornum18);
			break;
		}

		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// initialize 1st combobox
		cmbBuild.setItems(build);
	}

	// to goto menu
	@FXML
	public void menu(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		rootpane1.getChildren().setAll(pane);
	}

	// method to generate report
	@FXML
	public void reportgenerate(ActionEvent event) throws IOException {
		String buildno = (String) cmbBuild.getValue();
		String floorno = (String) cmbFloor.getValue();
		String room1 = null;
		String room2 = null;
		String room3 = null;
		int cost = 0;
		int cost1 = 0;
		int cost2 = 0;

		switch (buildno) {
		case "10":
			switch (floorno) {
			case "1":
				room1 = "10F";
				room2 = "11F";
				room3 = "12F";
				break;
			case "2":
				room1 = "13S";
				room2 = "14S";
				room3 = "15S";
				break;

			}
			break;
		case "12":
			switch (floorno) {
			case "0":
				room1 = "20G";
				room2 = "21G";
				room3 = "22G";
				break;
			case "1":
				room1 = "23F";
				room2 = "24F";
				room3 = "25F";
				break;

			}
			break;
		case "18":
			switch (floorno) {
			case "0":
				room1 = "30G";
				room2 = "31G";
				room3 = "32G";
				break;
			case "1":
				room1 = "33F";
				room2 = "34F";
				room3 = "35F";
				break;

			}
			break;

		}

		MongoCollection<Document> collection = database.getCollection("Rooms");

		PrintWriter outputStream = new PrintWriter(new FileOutputStream("FMO_report.txt"));

		Document x = collection.find(Filters.and(Filters.eq("building", buildno), Filters.eq("floor", floorno),
				Filters.eq("roomnum", room1))).first();

		outputStream.println("Campus     : Wellawatta");
		outputStream.println("Building No:" + buildno);
		outputStream.println("Floor No   :" + floorno);
		outputStream.println(" ");
		outputStream
				.println("Room      : " + x.getString("roomnum") + "\t\t" + "SpaceType: " + x.getString("spacetype"));
		outputStream
				.println("Department:" + x.getString("occupydept") + "\t\t" + "SpaceCode: " + x.getString("spacecode"));
		outputStream
				.println("Occupant  :" + x.getString("occupant") + "\t\t" + "UFA    :" + x.getInteger("UFA") + "sqm");

		MongoCollection<Document> collection1 = database.getCollection("Furniture");

		outputStream.println(" ");
		outputStream.println("Furniture Assets");
		outputStream.println("# Barcode        Description          AquisationDate     Cost");
		outputStream.println("_____________________________________________________________________");

		Iterator<Document> x1 = collection1.find(Filters.eq("roomnum", room1)).iterator();
		while (x1.hasNext()) {

			Document y = x1.next();
			outputStream.println(y.getString("Barcode") + "\t" + y.getString("Type") + "\t" + y.getString("date") + "\t"
					+ y.getInteger("PurchaseCost"));
			cost = cost + y.getInteger("PurchaseCost");

		}
		outputStream.println("\t\t\t\t\t      Roomtotal:" + cost);
		outputStream.println("\n\n\n");

		Document x2 = collection.find(Filters.and(Filters.eq("building", buildno), Filters.eq("floor", floorno),
				Filters.eq("roomnum", room2))).first();

		outputStream
				.println("Room      : " + x2.getString("roomnum") + "\t\t" + "SpaceType: " + x2.getString("spacetype"));
		outputStream.println(
				"Department:" + x2.getString("occupydept") + "\t\t" + "SpaceCode: " + x2.getString("spacecode"));
		outputStream
				.println("Occupant  :" + x2.getString("occupant") + "\t\t" + "UFA    :" + x2.getInteger("UFA") + "sqm");

		outputStream.println(" ");
		outputStream.println("Furniture Assets");
		outputStream.println("# Barcode        Description          AquisationDate     Cost");
		outputStream.println("_____________________________________________________________________");

		Iterator<Document> x3 = collection1.find(Filters.eq("roomnum", room2)).iterator();
		while (x3.hasNext()) {

			Document y1 = x3.next();
			outputStream.println(y1.getString("Barcode") + "\t" + y1.getString("Type") + "\t" + y1.getString("date")
					+ "\t" + y1.getInteger("PurchaseCost"));
			cost1 = cost1 + y1.getInteger("PurchaseCost");

		}
		outputStream.println("\t\t\t\t\t      Roomtotal:" + cost1);
		outputStream.println("\n\n\n");

		Document x4 = collection.find(Filters.and(Filters.eq("building", buildno), Filters.eq("floor", floorno),
				Filters.eq("roomnum", room3))).first();

		outputStream
				.println("Room      : " + x4.getString("roomnum") + "\t\t" + "SpaceType: " + x4.getString("spacetype"));
		outputStream.println(
				"Department:" + x4.getString("occupydept") + "\t\t" + "SpaceCode: " + x4.getString("spacecode"));
		outputStream
				.println("Occupant  :" + x4.getString("occupant") + "\t\t" + "UFA    :" + x4.getInteger("UFA") + "sqm");

		outputStream.println(" ");
		outputStream.println("Furniture Assets");
		outputStream.println("# Barcode        Description          AquisationDate     Cost");
		outputStream.println("_____________________________________________________________________");

		Iterator<Document> x5 = collection1.find(Filters.eq("roomnum", room3)).iterator();
		while (x5.hasNext()) {

			Document y2 = x5.next();
			outputStream.println(y2.getString("Barcode") + "\t" + y2.getString("Type") + "\t" + y2.getString("date")
					+ "\t" + y2.getInteger("PurchaseCost"));
			cost2 = cost2 + y2.getInteger("PurchaseCost");

		}
		outputStream.println("\t\t\t\t\t      Roomtotal:" + cost2);
		outputStream.println("\n\n\n");
		outputStream.println(
				"Total space occupied:" + (x.getInteger("UFA") + x2.getInteger("UFA") + x4.getInteger("UFA")) + "sqm");
		outputStream.println("Total Asset Value   :" + (cost + cost1 + cost2));
		outputStream.flush();
		outputStream.close();

	}

	@FXML
	public void read(ActionEvent event) throws IOException {
		Desktop desktop = Desktop.getDesktop();

		File f = new File("FMO_report.txt");
		desktop.open(f);
	}
}