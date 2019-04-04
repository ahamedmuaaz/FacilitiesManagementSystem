package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class Logincontroller implements Initializable {
	@FXML
	public TextField txt1;
	@FXML
	private PasswordField pass;
	@FXML
	private Button btn;
	@FXML
	private AnchorPane rootpane1;
	@FXML
	private ImageView imagex;

	@FXML
	public void login(ActionEvent event) throws IOException {
		//creating strings to get username and password from text fields
		String user = txt1.getText();
        String pasword = pass.getText();

		// Creating a Mongo client
		MongoClient mongo = new MongoClient("localhost", 27017);

		// Accessing the database
		MongoDatabase fmis = mongo.getDatabase("FMIS");

		// Retrieving a collection
		MongoCollection<Document> staffdetail= fmis.getCollection("Staffdetails");
		System.out.println("Collection myCollection selected successfully");
        //Check for username and password
		try {
			Document myDoc = staffdetail.find(Filters.and(Filters.eq("Name", user), Filters.eq("password", pasword)))
					.first();
			String usern = myDoc.getString("Name");
			String passw = myDoc.getString("password");

			if (usern == myDoc.getString("Name") && passw == myDoc.getString("password")) {

				AnchorPane pane = FXMLLoader.load(getClass().getResource("Menu.fxml"));
				rootpane1.getChildren().setAll(pane);
			}

		} catch (final NullPointerException ex) {
            //Error message if password & username invalid
			Alert message = new Alert(Alert.AlertType.INFORMATION, "Check username and password", ButtonType.CLOSE);
			message.showAndWait();
		}

	}

	@Override
	public void initialize(URL loaction, ResourceBundle resourses) {
		//create an image type to set path for image used in login form
		Image img = new Image("\\application\\icons\\line_background_dark_stripes_obliquely_67110.jpg");
		//initialize image used in login page 
		imagex.setImage(img);
	

	}


	
}
