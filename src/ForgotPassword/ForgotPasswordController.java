package ForgotPassword;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ForgotPasswordController implements Initializable{
	
	@FXML
	private TextField accountno;
	@FXML
	private TextField answer;
	@FXML
	private ComboBox<String> question;
	
	ObservableList<String> questionlist = FXCollections.observableArrayList("What is your pet name?","What is yournick name?","What is your chidhood town?");
	
	public void backToLogin(MouseEvent e) throws IOException {
		Main.stage.getScene().setRoot(FXMLLoader.load(getClass().getResource("/application/Login.fxml")));
	}

	public void closeApp(MouseEvent e) {
		Platform.exit();
		System.exit(0);
	}
	
	public void recoverPassword(MouseEvent event) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "SELECT * FROM userdata WHERE AccountNo=? and SecurityQuestion=? and Answer=? ";
			ps =con.prepareStatement(sql);
			ps.setString(1, accountno.getText());
			ps.setString(2, question.getValue());
			ps.setString(3, answer.getText());
			rs = ps.executeQuery();
			if(rs.next()) {
				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("Password Recovery");
				a.setHeaderText("Below is your password");
				a.setContentText("Account No:- "+rs.getString("AccountNo")+"\nPIN:- "+rs.getString("PIN"));
				a.showAndWait();
			}else {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error");
				a.setHeaderText("Wrong Data !!");
				a.setContentText("Your account details  are not correct. TRY AGAIN !!!");
				a.showAndWait();
			}
			
			
		}catch(Exception e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Error in creating account");
			a.setContentText("Your account is not created. There is some technical issue"+e.getMessage());
			a.showAndWait();
		}
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		question.setItems(questionlist);
	}

}
