package application;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoginController implements Initializable {
	
	public static Stage stage = null;
	public static String acc;
	
	@FXML
	private Pane main_area;
	
	@FXML
	private TextField accountno;
	
	@FXML
	private TextField pin;
	
	@FXML
	private void closeApp(MouseEvent e) {
		Platform.exit();
		System.exit(0);
	}

	@FXML
	private void createAccount(MouseEvent e) throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("/CreateAccount/CreateAccount.fxml"));
		main_area.getChildren().removeAll();
		main_area.getChildren().addAll(fxml);
	}
	@FXML
	private void forgotPassword() throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("/ForgotPassword/ForgotPassword.fxml"));
		main_area.getChildren().removeAll();
		main_area.getChildren().addAll(fxml);
	}
	public void loginAccount(MouseEvent event) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "SELECT * FROM userdata WHERE AccountNo=? and PIN=? ";
			ps =con.prepareStatement(sql);
			ps.setString(1, accountno.getText());
			ps.setString(2, pin.getText());
			rs = ps.executeQuery();
			acc = accountno.getText();
			if(rs.next()) {
//				((Node)event.getSource()).getScene().getWindow().hide();
				Parent root = FXMLLoader.load(getClass().getResource("/DashBoard/Dashboard.fxml"));
				Scene scene  = new Scene(root);
				scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
				Stage stage = new Stage();
				stage.initStyle(StageStyle.UNDECORATED);
				stage.setScene(scene);
				stage.show();
				this.stage=stage;
				pin.setText("");
				accountno.setText("");
			}else {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error");
				a.setHeaderText("Error in login to account");
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
		
	}
	
}
