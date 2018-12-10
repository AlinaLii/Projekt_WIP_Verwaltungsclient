package de.fhdw.javafx.adminclient;

import java.io.IOException;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class AccountViewController {

	ServerAccess serverAccess = new ServerAccess();
	ArrayList<Account> accountList;

	@FXML
	private TableView<Account> tabAccount;

	@FXML
	private TableColumn<Account, String> tabAccNumber;

	@FXML
	private TableColumn<Account, String> tabAccOwner;

	@FXML
	private TableColumn<Account, BigDecimal> tabAccBalance;

	@FXML
	private TextField txtInputIPAddress;

	@FXML
	private ImageView imgLogo;

	@FXML
	private Button btnRefresh;

	@FXML
	private Button btnNewAccount;

	@FXML
	private Text txtHeader;

	@FXML
	private Button btnTransactionView;

	@FXML
	private Text txtError;

	@FXML
	private void initialize() {
		txtInputIPAddress.setText(ServerAccess.getIpAddress());
		refreshAccount();
		accountList = ServerAccess.getAccountList();
		fillTable();
	}
//aktualisieren button
	@FXML
	void refreshBtnAction(ActionEvent event) {
		ServerAccess.setIpAddress(txtInputIPAddress.getText().toString());
		refreshFnkt();
	}

	@FXML
	void transactionViewBtnAction(ActionEvent event) {
		ServerAccess.setIpAddress(txtInputIPAddress.getText().toString());
		try {
			Stage stage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("TransactionView.fxml"));
			Parent root = null;
			root = loader.<Parent>load();
			TransactionViewController controller = loader.<TransactionViewController>getController();
			Scene scene = new Scene(root);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
	void newAccountBtnAction(ActionEvent event) {
		ServerAccess.setIpAddress(txtInputIPAddress.getText().toString());
		try {
			Stage stage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("NewAccount.fxml"));
			Parent root = null;
			root = loader.<Parent>load();
			NewAccountController controller = loader.<NewAccountController>getController();
			Scene scene = new Scene(root);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void refreshFnkt() {
		accountList = refreshAccount();
		fillTable();
	}

	protected void fillTable() {
		tabAccNumber.setCellValueFactory(new PropertyValueFactory<Account, String>("number"));
		tabAccOwner.setCellValueFactory(new PropertyValueFactory<Account, String>("owner"));
		tabAccBalance.setCellValueFactory(new PropertyValueFactory<Account, BigDecimal>("balance"));

		for (Account item : accountList) {
			List<Transaction> transactions = item.getTransactions();
			BigDecimal accountBalance = new BigDecimal(0);
			for (Transaction transaction : transactions) {
				if (transaction.getSender().getNumber().equals(item.getNumber())) {
					accountBalance = accountBalance.subtract(transaction.getAmount());
				} else {
					accountBalance = accountBalance.add(transaction.getAmount());
				}
				item.setBalance(accountBalance);

			}
		}
		ObservableList<Account> data = FXCollections.observableList(accountList);
		tabAccount.setItems(data);

	}

	protected ArrayList<Account> refreshAccount() {
		try {
			HttpResponse response = serverAccess.getAllAccountResponse();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String accountJson = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().create();
				Account[] accountArray = gson.fromJson(accountJson, Account[].class);
				ArrayList<Account> accountList = new ArrayList<Account>(Arrays.asList(accountArray));
				ServerAccess.setAccountList(accountList);
				txtError.setText("");
				return accountList;
			} else {
				txtError.setText(EntityUtils.toString(response.getEntity())
				+ " (Fehler: " + response.getStatusLine().getStatusCode() +
				")");
			}
		} catch (IOException e) {
			e.printStackTrace();
			txtError.setText("Server nicht verfügbar");
		}
		return null;
	}

	@FXML
	void clickItem(MouseEvent mouseEvent) {
		ServerAccess.setIpAddress(txtInputIPAddress.getText().toString());
		if (mouseEvent.getClickCount() == 2) // Checking double click
		{
			try {
				Stage stage;
				FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountDetailView.fxml"));
				Parent root = null;
				root = loader.<Parent>load();
				AccountDetailViewController controller = loader.<AccountDetailViewController>getController();
				controller.initData(tabAccount.getSelectionModel().getSelectedItem());
				Scene scene = new Scene(root);
				stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

}
