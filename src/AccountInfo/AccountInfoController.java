package AccountInfo;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import application.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class AccountInfoController implements Initializable {
	
	@FXML 
	private Text account_no;
	@FXML 
	private Text gender;
	@FXML 
	private Text account_type;
	@FXML 
	private Text religion;
	@FXML
	private Label balance;
	@FXML
	private Pane dashboard_main;
	
	public void setData() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "SELECT * FROM userdata WHERE AccountNo=? ";
			ps =con.prepareStatement(sql);
			ps.setString(1, LoginController.acc);
			rs = ps.executeQuery();
			if(rs.next()) {
				balance.setText(rs.getString("Balance"));
				account_no.setText(rs.getString("AccountNo"));
				gender.setText(rs.getString("Gender"));
				account_type.setText(rs.getString("AccountType"));
				religion.setText(rs.getString("Religion"));
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
	
	@FXML
	public void openWithdrawScreen() throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("/Withdraw/WithdrawAmount.fxml"));
		dashboard_main.getChildren().removeAll();
		dashboard_main.getChildren().addAll(fxml);
	}
	
	@FXML
	public void openDepositScreen() throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("/Deposit/DepositAmount.fxml"));
		dashboard_main.getChildren().removeAll();
		dashboard_main.getChildren().addAll(fxml);
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setData();
	}
}
