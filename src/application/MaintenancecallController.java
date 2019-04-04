package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class MaintenancecallController implements Initializable{
	//to set items of combobox
	ObservableList<String>options=FXCollections.observableArrayList("Open office","Replace light","Carpet clean");
	
	@FXML
	private ComboBox combo;
	@FXML
	private AnchorPane rootpane1;
	@FXML
	private TextField txt1;
	@FXML
	private TextField txt2;
	@FXML
	private ImageView imgview; 
	
	
	
	
	
	@Override
	 public void initialize(URL location, ResourceBundle resources) {
		//initialize image used in imageview
		Image img=new Image("\\application\\icons\\bpo-project-available-here-500x500.jpg");
		imgview.setImage(img);
		//initialize combobox items
		 combo.setItems(options);
	   }
	
	//method to switch anchorpane to menuform
	 @FXML
	 public void nxtrequest(ActionEvent event) throws IOException {
		 AnchorPane pane=FXMLLoader.load(getClass().getResource("Menu.fxml"));
       rootpane1.getChildren().setAll(pane);
	      
	   }
	 //method to make the call
	 @FXML
	 public void makecall(ActionEvent event) {
		// Creating a Mongo client 
	     MongoClient mongo = new MongoClient( "localhost" , 27017 ); 
	    
	    
	     
	     // Accessing the database 
	     MongoDatabase database = mongo.getDatabase("FMIS"); 

	     // Retrieving a collection 
	     MongoCollection<Document> collection = database.getCollection("Maintenancecall");

	     String num=txt1.getText();
	     String num1=(String) combo.getValue();
	     String num2=txt2.getText();
	    
	     System.out.println(num1);
	     Document document = new Document("Staffid", num) 
	    		 .append("purposeofcall",num1).append("Location",num2);
	    		 
	    		 collection.insertOne(document); 
	    		 
	    		 txt1.clear();
	    		 txt2.clear();
	    		 combo.setValue(null);
	 }
	
   
}
