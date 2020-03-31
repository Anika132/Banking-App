package CreateAccount;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Main;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CreateAccountController implements Initializable {
	
	private FileChooser filechooser = new FileChooser();
	private File file;
	private FileInputStream fis;
	@FXML
	private ImageView pic;
	@FXML
	private TextField name;
	@FXML
	private TextField idcardno;
	@FXML
	private TextField mobileno;
	@FXML
	private TextField city;
	@FXML
	private TextField address;
	@FXML
	private TextField accountno;
	@FXML
	private TextField answer;
	@FXML
	private TextField pin;
	@FXML
	private TextField balance;
	@FXML
	private DatePicker dob;
	@FXML
	private ComboBox<String> gender;
	@FXML
	private ComboBox<String> martialstatus;
	@FXML
	private ComboBox<String> religion;
	@FXML
	private ComboBox<String> questions;
	@FXML
	private ComboBox<String> accountype;

	ObservableList<String> genderlist = FXCollections.observableArrayList("Male","Female","Others");
	
	ObservableList<String> statuslist = FXCollections.observableArrayList("Single","Married");
	
	ObservableList<String> religionlist = FXCollections.observableArrayList("Hindu","Christianity","Jainism","Buddhisim","Islam","Sikhism","Other");
	
	ObservableList<String> accountlist = FXCollections.observableArrayList("Saving","Current");
	
	ObservableList<String> questionlist = FXCollections.observableArrayList("What is your pet name?","What is yournick name?","What is your chidhood town?");
	
	public void backToLogin(MouseEvent e) throws IOException {
		Main.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/application/Login.fxml")));
	}

	public void closeApp(MouseEvent e) {
		Platform.exit();
		System.exit(0);
	}
	public void setUpPic(MouseEvent event) {
		filechooser.getExtensionFilters().add(new ExtensionFilter("Images Files","*.png","*.jpg"));
		file = filechooser.showOpenDialog(null);
		if(file!=null) {
			Image img = new Image(file.toURI().toString(),150,150,true,true);
			pic.setImage(img);;
			pic.setPreserveRatio(true);
		}
	}
	
	public boolean validateName() {
		Pattern p = Pattern.compile("^[a-zA-Z\\s]+");
		Matcher m = p.matcher((name.getText()));
		if(m.find() && m.group().equals(name.getText())) {
			return true;
		}else {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Wrong Name!!");
			a.setHeaderText("Your name is incorrect ");
			a.setContentText("Please enter charcter only in name. TRY AGAIN !!");
			a.showAndWait();
			return false;
		}
	}
	
	public boolean validateMobileNo() {
		Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
		Matcher m = p.matcher((mobileno.getText()));
		if(m.find() && m.group().equals(mobileno.getText())) {
			return true;
		}else {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Wrong Mobile Number!!");
			a.setHeaderText("Your Mobile Number is incorrect ");
			a.setContentText("Please enter numbers only in mobile number field and also should contain 10 digits. TRY AGAIN !!");
			a.showAndWait();
			return false;
		}
	}
	
	public boolean validateIDCardNo() {
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher((idcardno.getText()));
		if(m.find() && m.group().equals(idcardno.getText())) {
			return true;
		}else {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Wrong ID Card Number!!");
			a.setHeaderText("Your ID card number is incorrect ");
			a.setContentText("Please enter numbers only ID card number field. TRY AGAIN !!");
			a.showAndWait();
			return false;
		}
	}
	public boolean validateBalance() {
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher((balance.getText()));
		if(m.find() && m.group().equals(balance.getText())) {
			return true;
		}else {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Wrong Value of Balance!!");
			a.setHeaderText("Your Balance is incorrect ");
			a.setContentText("Please enter numbers only Balance field. TRY AGAIN !!");
			a.showAndWait();
			return false;
		}
	}
	public void clearAllFields() {
		name.clear();
		idcardno.clear();
		mobileno.clear();
		gender.getSelectionModel().clearSelection();
		religion.getSelectionModel().clearSelection();
		martialstatus.getSelectionModel().clearSelection();
		accountype.getSelectionModel().clearSelection();
		questions.getSelectionModel().clearSelection();
		answer.clear();
		city.clear();
		dob.getEditor().clear();
		address.clear();
		pin.clear();
		balance.clear();
		Image img = new Image("/images/profile.png");
		pic.setImage(img);
		generateAccountNo();
	}
	
	public void generateAccountNo() {
		Random rand = new Random();
		int num = rand.nextInt(899999)+100000;
		accountno.setText(String.valueOf(num));
	}
	public void newAccount(MouseEvent event) {
		Connection con = null;
		PreparedStatement ps = null;
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			if(validateName()&&validateIDCardNo()&&validateMobileNo()) {
				String sql = "INSERT INTO userdata (Name, ICN, MobileNo, Gender, MartialStatus, Religion, DOB, City, Address, AccountNo, PIN, AccountType, Balance, SecurityQuestion, Answer, ProfilePic) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
				ps =con.prepareStatement(sql);
				ps.setString(1, name.getText());
				ps.setString(2, idcardno.getText());
				ps.setString(3, mobileno.getText());
				ps.setString(4, gender.getValue());	
				ps.setString(5, martialstatus.getValue());
				ps.setString(6, religion.getValue());
				ps.setString(7, ((TextField)dob.getEditor()).getText());
				ps.setString(8, city.getText());
				ps.setString(9, address.getText());
				ps.setString(10, accountno.getText());	
				ps.setString(11, pin.getText());
				ps.setString(13, balance.getText());
				ps.setString(12, accountype.getValue());
				ps.setString(14, questions.getValue());
				ps.setString(15, answer.getText());
				fis = new FileInputStream(file);
				ps.setBinaryStream(16, (InputStream)fis,(int)file.length());
				int i = ps.executeUpdate();
				if(i>0) {
					Alert a = new Alert(AlertType.INFORMATION);
					a.setTitle("Account Created");
					a.setHeaderText("Account Created Successfully");
					a.setContentText("Your account has been created.You can now login with your account");
					a.showAndWait();
					clearAllFields();
				}else {
					Alert a = new Alert(AlertType.ERROR);
					a.setTitle("Error");
					a.setHeaderText("Error in creating account");
					a.setContentText("Your account is not created. There is some technical issue TRY AGAIN !!!");
					a.showAndWait();
					clearAllFields();
				}
		   }
		}catch(Exception e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Error in creating account");
			a.setContentText("Your account is not created. There is some technical issue"+e.getMessage());
			a.showAndWait();
			clearAllFields();
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		gender.setItems(genderlist);
		martialstatus.setItems(statuslist);
	    accountype.setItems(accountlist);
		religion.setItems(religionlist);
		questions.setItems(questionlist);
		generateAccountNo();
	}

}
