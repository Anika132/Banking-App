package ChangePIN;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import DashBoard.DashboardController;
import application.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class ChangePINController implements Initializable{
	
	@FXML
	private TextField old_pin;
	@FXML
	private TextField new_pin;
	@FXML
	private TextField confirm_pin;
		
	DashboardController d = new DashboardController();
	
	public void changePIN(MouseEvent event) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "SELECT * FROM userdata WHERE AccountNo=? and PIN=?";
			ps =con.prepareStatement(sql);
			ps.setString(1, LoginController.acc);
			ps.setString(2, old_pin.getText());
			rs = ps.executeQuery();
			if(rs.next()) {
				if(new_pin.getText().equals(confirm_pin.getText())) {
					String sql1 = "UPDATE userdata SET PIN='"+new_pin.getText()+"' WHERE AccountNo='"+LoginController.acc+"'";
					ps = con.prepareStatement(sql1);
					ps.execute();
					Alert a = new Alert(AlertType.CONFIRMATION);
					a.setTitle("PIN Changed");
					a.setHeaderText("PIN changed successful");
					a.setContentText("Your PIN has been changed sucessfully from your account\nNow, you have to login again with your new PIN");
					a.showAndWait();
					old_pin.setText("");
					new_pin.setText("");
					confirm_pin.setText("");	
					d.logout(event);
				}
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
			a.setContentText("Your account is not created. There is some technical issue"+ e.getMessage());
			e.printStackTrace();
			a.showAndWait();
		}
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}
