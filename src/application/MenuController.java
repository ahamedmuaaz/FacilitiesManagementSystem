package application;

import java.io.IOException;

import java.net.URL;

import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;

import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MenuController implements Initializable {
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		Image img=new Image("\\application\\icons\\menu3.png");
		imgview.setImage(img);
		

	}

	@FXML
	private Button btnrequest;
	@FXML
	private Button btneditrequest;
	@FXML
	private Button maintenancecall;
	@FXML
	private AnchorPane rootpane;
	@FXML
	private Button logout;
	@FXML
	private ImageView imgview;

	@FXML
	public void nxtrequest(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("request move.fxml"));
		rootpane.getChildren().setAll(pane);

	}

	@FXML
	public void maintenancecall(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("Maintenancecall.fxml"));
		rootpane.getChildren().setAll(pane);

	}

	@FXML
	public void logout(ActionEvent event) throws IOException {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("");
		alert.setHeaderText(null);
		alert.setContentText("Are you Sure?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			AnchorPane pane = FXMLLoader.load(getClass().getResource("Login.fxml"));
			rootpane.getChildren().setAll(pane);
		} else {

		}

	}

	@FXML
	public void editrequest(ActionEvent event) throws IOException {
		AnchorPane pane = FXMLLoader.load(getClass().getResource("Editmove.fxml"));
		rootpane.getChildren().setAll(pane);

	}

	@FXML
	public void buildingplan(ActionEvent event) throws IOException {
		TextInputDialog login = new TextInputDialog("Authentication code");
		login.setTitle("Security");

		login.setContentText("Please provide Authentication code");
		login.showAndWait();
		String code = login.getResult();

		switch (code) {
		case "auth123":
			AnchorPane pane = FXMLLoader.load(getClass().getResource("Buildingplans.fxml"));
			rootpane.getChildren().setAll(pane);
			break;
		default:
			Alert message = new Alert(Alert.AlertType.INFORMATION, "Sorry you are not permitted", ButtonType.CLOSE);
			message.showAndWait();
		}

	}
	@FXML
	public void generatereport(ActionEvent event) throws IOException {
		TextInputDialog login = new TextInputDialog("Authentication code");
		login.setTitle("Security");

		login.setContentText("Please provide Authentication code");
		login.showAndWait();
		String code = login.getResult();

		switch (code) {
		case "spec123":
			AnchorPane pane = FXMLLoader.load(getClass().getResource("generatereport.fxml"));
			rootpane.getChildren().setAll(pane);
			break;
		default:
			Alert message = new Alert(Alert.AlertType.INFORMATION, "Sorry you are not permitted", ButtonType.CLOSE);
			message.showAndWait();
		}
		
	}

}
