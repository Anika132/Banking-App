package Transfer;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import application.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class TransferMoneyController implements Initializable{
	
	Connection con = null;
	PreparedStatement ps = null;
	ResultSet rs = null;
	
	@FXML
	private Label account_no;
	@FXML
	private Label balance;
	@FXML
	private TextField account_no_transfer;
	@FXML
	private TextField pin;
	@FXML
	private TextField money;
		
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int hour = cal.get(Calendar.HOUR);
	int min = cal.get(Calendar.MINUTE);
	int sec = cal.get(Calendar.SECOND);
	int daynight = cal.get(Calendar.AM_PM);
	
	DateFormat dateformat = new SimpleDateFormat("yyyy/mm/dd");
	Date d = new Date();
	String date = dateformat.format(d);
	
	LocalTime localtime = LocalTime.now();
	DateTimeFormatter dt = DateTimeFormatter.ofPattern("hh:mm:ss a");
	String time = localtime.format(dt);
	
	public void setData() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "SELECT * FROM userdata WHERE AccountNo=? ";
			ps =con.prepareStatement(sql);
			ps.setString(1,LoginController.acc);
			rs = ps.executeQuery();
			if(rs.next()) {
				balance.setText(rs.getString("Balance"));
				account_no.setText(rs.getString("AccountNo"));
			}else {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error");
				a.setHeaderText("Error in login to your account");
				a.setContentText("Your account details are not correct. TRY AGAIN !!!");
				a.showAndWait();
			}
		}catch(Exception e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Error in login to your account");
			a.setContentText("Your account details not correct. There is some technical issue"+ e.getMessage());
			e.printStackTrace();
			a.showAndWait();
		}
	}
	public void searchInfoUser() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "SELECT * FROM userdata WHERE AccountNo=? ";
			ps =con.prepareStatement(sql);
			ps.setString(1, account_no_transfer.getText());
			rs = ps.executeQuery();
			if(rs.next()) {
				Alert a = new Alert(AlertType.INFORMATION);
				a.setTitle("Successful Search");
				a.setHeaderText("Below are the details of the account");
				a.setContentText("Account Number = "+account_no_transfer.getText()+"\nAccount Holder Name = "+rs.getString("Name")+"\nContact Number = "+rs.getString("MobileNo"));
				a.showAndWait();
			}else {
				Alert a = new Alert(AlertType.ERROR);
				a.setTitle("Error");
				a.setHeaderText("Error in login to your account");
				a.setContentText("Your account details are not correct. TRY AGAIN !!!");
				a.showAndWait();
			}
		}catch(Exception e) {
			Alert a = new Alert(AlertType.ERROR);
			a.setTitle("Error");
			a.setHeaderText("Error in login to your account");
			a.setContentText("Your account details not correct. There is some technical issue"+ e.getMessage());
			e.printStackTrace();
			a.showAndWait();
		}
	}
	
	public void transferAmount() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "SELECT * FROM userdata WHERE AccountNo=? and PIN=?";
			ps =con.prepareStatement(sql);
			ps.setString(1, LoginController.acc);
			ps.setString(2, pin.getText());
			rs = ps.executeQuery();
			if(rs.next()) {
				int amount = Integer.parseInt(money.getText());
				int bal = Integer.parseInt(balance.getText());
				if(amount>bal) {
					Alert a = new Alert(AlertType.ERROR);
					a.setTitle("Error");
					a.setHeaderText("Error in transfering money");
					a.setContentText("You don't have enough money to transfer. ENTER AGAIN !!!");
					a.showAndWait();
				}else if(LoginController.acc.equals(account_no_transfer.getText())) {
					Alert a = new Alert(AlertType.ERROR);
					a.setTitle("Error");
					a.setHeaderText("Error in transfering money");
					a.setContentText("You can't transfer money to yourself. ENTER AGAIN !!!");
					a.showAndWait();
				}else{
					int total = bal-amount;
					String sql1 = "UPDATE userdata SET Balance='"+total+"' WHERE AccountNo='"+LoginController.acc+"'";
					ps = con.prepareStatement(sql1);
					ps.execute();
					String sql2 = "SELECT * FROM userdata WHERE AccountNo=? ";
					ps = con.prepareStatement(sql2);
					ps.setString(1, account_no_transfer.getText());
					rs = ps.executeQuery();
					if(rs.next()) {
						int transferedAmount = Integer.parseInt(money.getText());
						int curAmount = Integer.parseInt(rs.getString("Balance"));
						int finalAmount = curAmount+transferedAmount;
						String sql3 = "UPDATE userdata SET Balance='"+finalAmount+"' WHERE AccountNo='"+account_no_transfer.getText()+"'";
						ps = con.prepareStatement(sql3);
						ps.execute();
						String sql4 = "INSERT INTO transferamount (AccountNo, Amount,SendTo, Date, Time) VALUES(?,?,?,?,?) ";
						ps = con.prepareStatement(sql4);
						ps.setString(1, LoginController.acc);
						ps.setString(2, String.valueOf(money.getText()));
						ps.setString(3, String.valueOf(account_no_transfer.getText()));
						ps.setString(4, date);
						ps.setString(5, time);
						int i = ps.executeUpdate();
						if(i>0) {
							Alert a = new Alert(AlertType.CONFIRMATION);
							a.setTitle("Transfered Money");
							a.setHeaderText(" Money Transfered Successful");
							a.setContentText("Amount "+transferedAmount+" has been transfered from your account to Account No = "+account_no_transfer.getText());
							a.showAndWait();
							money.setText("");
							pin.setText("");
							account_no_transfer.setText("");
							balance.setText(String.valueOf(total));
						}
				}else {
					Alert a = new Alert(AlertType.ERROR);
					a.setTitle("Error");
					a.setHeaderText("Error in transfering money to account");
					a.setContentText("No such account exists. Please check your details. TRY AGAIN !!!");
					a.showAndWait();
				}
			}
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
		setData();
		
	}

}
