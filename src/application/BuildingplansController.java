package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BuildingplansController implements Initializable {
	// to set combobox items
	ObservableList<String> build = FXCollections.observableArrayList("10", "12", "18");
	ObservableList<String> floornum10 = FXCollections.observableArrayList("1", "2");
	ObservableList<String> floornum12 = FXCollections.observableArrayList("G", "1");
	ObservableList<String> floornum18 = FXCollections.observableArrayList("G", "1");

	@Override
	public void initialize(URL loaction, ResourceBundle resourses) {
		// initialize combobox items
		cmbbuildno.setItems(build);

	}

	@FXML
	private ComboBox cmbbuildno;
	@FXML
	private ComboBox cmbfloorno;
	@FXML
	private ImageView imagex;

	@FXML
	private AnchorPane rootpane;

	// method to initialize 2nd combobox when first one is selected
	@FXML
	public void getfloornum(ActionEvent event) {

		String building_No = (String) cmbbuildno.getValue();

		switch (building_No) {

		case "10": {
			cmbfloorno.setItems(floornum10);
			break;
		}
		case "12": {
			cmbfloorno.setItems(floornum12);
			break;
		}
		case "18": {
			cmbfloorno.setItems(floornum18);
			break;
		}

		}

	}

	// to view building plan in image view
	@FXML
	public void generate(ActionEvent event) {
		String building_No = (String) cmbbuildno.getValue();
		String floor_No = (String) cmbfloorno.getValue();

		if (building_No == "10") {
			if (floor_No == "1") {
				Image img = new Image("/application/icons/1001.jpg");
				imagex.setImage(img);
			} else {
				Image img = new Image("/application/icons/1002.jpg");
				imagex.setImage(img);
			}

		}
		if (building_No == "12") {
			if (floor_No == "G") {
				Image img = new Image("/application/icons/1200.jpg");
				imagex.setImage(img);
			} else {
				Image img = new Image("/application/icons/1201.jpg");
				imagex.setImage(img);
			}

		}
		if (building_No == "18") {
			if (floor_No == "G") {
				Image img = new Image("/application/icons/1800.jpg");
				imagex.setImage(img);
			} else {
				Image img = new Image("/application/icons/1801.jpg");
				imagex.setImage(img);
			}
		}
	}
	// method to goto menu

	@FXML
	public void menu(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
		rootpane.getChildren().setAll(pane);

	}
}
