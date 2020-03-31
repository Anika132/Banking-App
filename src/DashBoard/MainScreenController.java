package DashBoard;

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
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;

public class MainScreenController implements Initializable {
	
	@FXML
	private Label name;
	
	@FXML 
	private Label body;
	
	@FXML
	private Pane dashboard_main;
	
	@FXML
	public void openTransferMoneyScreen() throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("/Transfer/TransferMoney.fxml"));
		dashboard_main.getChildren().removeAll();
		dashboard_main.getChildren().addAll(fxml);
	}
	
	@FXML
	public void openTransactionHistoryScreen() throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("/Transaction/TransactionHistory.fxml"));
		dashboard_main.getChildren().removeAll();
		dashboard_main.getChildren().addAll(fxml);
	}
	
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
				name.setText(rs.getString("Name"));
				
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
	public void initialize(URL url, ResourceBundle rb) {
		setData();
		body.setText("Allied Bank is one of the largest public sector banks owned by the\nGovernment of India. It is headquartered in Bengaluru. It was \nestablished at Mangalore in 1906 by Ammembal Subba Rao Pai. \nIt is one of the oldest public sector banks in the country.\nThe government nationalized the bank in 1969.The Tagline of \nCanara Bank is \"Together we Can\". As of 31 March 2019, \nthe bank had a network of 6310 branches and more than 8851 \nATMs which are spread across 4467 centers.");
	}
}
