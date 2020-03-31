package Withdraw;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class WithdrawAmountController implements Initializable{
	
	@FXML
	private Label account_no;
	@FXML
	private Label balance;
	@FXML
	private TextField amt_withdraw;
	@FXML
	private TextField pin;
	
	
	Calendar cal = Calendar.getInstance();
	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int hour = cal.get(Calendar.HOUR);
	int min = cal.get(Calendar.MINUTE);
	int sec = cal.get(Calendar.SECOND);
	int daynight = cal.get(Calendar.AM_PM);
	
	DateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
	Date d = new Date();
	String date = dateformat.format(d);
	
	LocalTime localtime = LocalTime.now();
	DateTimeFormatter dt = DateTimeFormatter.ofPattern("hh:mm:ss a");
	String time = localtime.format(dt);
	
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
	public void withdrawAmount() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","");
			String sql = "SELECT * FROM userdata WHERE AccountNo=? and PIN=?";
			ps =con.prepareStatement(sql);
			ps.setString(1, LoginController.acc);
			ps.setString(2, pin.getText());
			rs = ps.executeQuery();
			if(rs.next()) {
				int amount = Integer.parseInt(amt_withdraw.getText());
				int bal = Integer.parseInt(balance.getText());
				if(amount>bal) {
					Alert a = new Alert(AlertType.ERROR);
					a.setTitle("Error");
					a.setHeaderText("Error in withdrawing money");
					a.setContentText("Your don't have enough money. ENTER AGAIN !!!");
					a.showAndWait();
				}else {
					int total = bal-amount;
					String sql1 = "UPDATE userdata SET Balance='"+total+"' WHERE AccountNo='"+LoginController.acc+"'";
					ps = con.prepareStatement(sql1);
					ps.execute();
					String sql2 = "INSERT INTO withdraw (AccountNo, WithdrawAmount, RemainingAmount, Date, Time) VALUES(?,?,?,?,?) ";
					ps = con.prepareStatement(sql2);
					ps.setString(1,LoginController.acc);
					ps.setString(2, String.valueOf(amount));
					ps.setString(3, String.valueOf(total));
					ps.setString(4, date);
					ps.setString(5, time);
					
					int i = ps.executeUpdate();
					if(i>0) {
						Alert a = new Alert(AlertType.CONFIRMATION);
						a.setTitle("Withdraw Successful");
						a.setHeaderText("Amount Withdraw successful");
						a.setContentText("Amount "+amount+" has been debited from your account\nCurrent Balance  = "+total);
						a.showAndWait();
						amt_withdraw.setText("");
						pin.setText("");
						balance.setText(String.valueOf(total));
					}
					
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
			a.setContentText("Your account is not created. There is some technical issue\n"+ e.getMessage());
			e.printStackTrace();
			a.showAndWait();
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		setData();
	}

}
