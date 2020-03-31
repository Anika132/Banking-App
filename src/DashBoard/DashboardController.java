package DashBoard;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import application.LoginController;
import application.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class DashboardController implements Initializable {
	
	private double xOffset = 0;
	private double yOffset = 0;
	@FXML
	private Pane dashboard_main;
	@FXML
	private Text name;
	@FXML
	private Circle profilepic;
	@FXML
	private FontAwesomeIconView icon;
	@FXML
	private void closeApp(MouseEvent e) {
		Platform.exit();
		System.exit(0);
	}
	@FXML
	private void minimizeApp(MouseEvent e) {
		Stage stage = (Stage)icon.getScene().getWindow();
		stage.setIconified(true);	
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
				InputStream is = rs.getBinaryStream("ProfilePic");
				OutputStream os = new FileOutputStream(new File("pic.jpg"));
				byte[] content = new byte[1024];
				int size=0;
				while((size=is.read(content)) != -1) {
					os.write(content,0,size);
				}
				os.close();
				is.close();
				Image img = new Image("file:pic.jpg",false);
				profilepic.setFill(new ImagePattern(img));
				
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
	
	@FXML
	public void click(MouseEvent event) {
		xOffset = event.getSceneX();
		yOffset = event.getSceneY();
	}
	
	@FXML
	public void drag(MouseEvent event) {
		LoginController.stage.setX(event.getSceneX()-xOffset);
		LoginController.stage.setY(event.getSceneY()-yOffset);
	}
	
	@FXML
	public void openAccountInformation(MouseEvent event) throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("/AccountInfo/AccountInformation.fxml"));
		dashboard_main.getChildren().removeAll();
		dashboard_main.getChildren().addAll(fxml);
	}
	
	@FXML
	public void openMainScreen() throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("MainScreen.fxml"));
		dashboard_main.getChildren().removeAll();
		dashboard_main.getChildren().addAll(fxml);
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
	
	@FXML
	public void openChangePINScreen() throws IOException {
		Parent fxml  = FXMLLoader.load(getClass().getResource("/ChangePIN/ChangePIN.fxml"));
		dashboard_main.getChildren().removeAll();
		dashboard_main.getChildren().addAll(fxml);
	}
	
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
	
	public void logout(MouseEvent event) throws IOException {
		((Node)event.getSource()).getScene().getWindow().hide();
		Parent root = FXMLLoader.load(getClass().getResource("/application/Login.fxml"));
		Scene scene  = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
		Stage stage = new Stage();
		stage.initStyle(StageStyle.UNDECORATED);
		stage.setScene(scene);
//		stage.show();
		
		root.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				xOffset = event.getSceneX();
				yOffset = event.getSceneY();
			}
		});
		
		root.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				stage.setX(event.getSceneX()-xOffset);
				stage.setY(event.getSceneY()-yOffset);
			}
			
		});

		
	}
	
	@Override 
	public void initialize(URL url, ResourceBundle rb) {
		setData();
		try {
			openMainScreen();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		Main.stage.close();		
	}

	

}
