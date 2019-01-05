package bertelsbank.AccountViewController;

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

import bertelsbank.NewAccountController.NewAccountController;
import bertelsbank.TransactionViewController.TransactionViewController;
import de.fhdw.javafx.adminclient.Account;
import bertelsbank.AccountDetailViewController.AccountDetailViewController;
import de.fhdw.javafx.adminclient.ServerAccess;
import de.fhdw.javafx.adminclient.Transaction;
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

	/**
	 * Connects to the server, updates the table
	 *
	 * @author Alina Liedtke
	 */
	@FXML
	private void initialize() {
		txtInputIPAddress.setText(ServerAccess.getIpAddress());
		refreshAccount();
		accountList = ServerAccess.getAccountList();
		if (accountList != null) {
			fillTable();
		}
	}

	/**
	 * Updates the table, uses the given IP-address
	 *
	 * @param event refresh-button is pressed
	 * @author Alina Liedtke
	 */
	@FXML
	void refreshBtnAction(ActionEvent event) {
		ServerAccess.setIpAddress(txtInputIPAddress.getText().toString());
		refreshFnkt();
	}

	/**
	 * When the transaction view-button is clicked the view changes to the transaction view, uses the given ip-address
	 *
	 * @param event transaction view-button is pressed
	 * @author Alina Liedtke
	 */
	@FXML
	void transactionViewBtnAction(ActionEvent event) {
		ServerAccess.setIpAddress(txtInputIPAddress.getText().toString());
		try {
			Stage stage;
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/bertelsbank/TransactionViewController/TransactionView.fxml"));
			Parent root = null;
			root = loader.<Parent>load();
			TransactionViewController controller = loader.<TransactionViewController>getController();
			Scene scene = new Scene(root);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			txtError.setText("Server nicht verfügbar.");
		}
	}

	/**
	 * When the new account-button is pressed the view changes to the new account-view using the given ip-address.
	 *
	 * @param event the new account-button is pressed
	 * @author Alina Liedtke
	 */
	@FXML
	void newAccountBtnAction(ActionEvent event) {
		ServerAccess.setIpAddress(txtInputIPAddress.getText().toString());
		try {
			HttpResponse response = serverAccess.getFreeAccountNumberResponse();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String freeNumber = EntityUtils.toString(response.getEntity());
				Stage stage;
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/bertelsbank/NewAccountController/NewAccount.fxml"));
				Parent root = null;
				root = loader.<Parent>load();
				NewAccountController controller = loader.<NewAccountController>getController();
				controller.setNumber(freeNumber);
				Scene scene = new Scene(root);
				stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();
			} else {
				txtError.setText("Server nicht verfügbar.");
			}

		} catch (IOException e) {
			txtError.setText("Server nicht verfügbar.");
			e.printStackTrace();
		}

	}

	/**
	 * Updates the table.
	 *
	 * @author Alina Liedtke
	 */
	public void refreshFnkt() {
		accountList = refreshAccount();
		fillTable();
	}

	/**
	 * Fills the table with the account number, account owner and account balance.
	 *
	 * @author Alina Liedtke
	 */
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

	/**
	 * Returns a list of all accounts that currently belong to the bank.
	 *
	 * @return accountList
	 * @author Alina Liedtke
	 */
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
				txtError.setText(EntityUtils.toString(response.getEntity()) + " (Fehler: "
						+ response.getStatusLine().getStatusCode() + ")");
			}
		} catch (IOException e) {
			e.printStackTrace();
			txtError.setText("Server nicht verfügbar");
		}
		return null;
	}

	/**
	 * When an item out of the table is clicked the view changes to account detail view.
	 *
	 * @param mouseEvent When an item out of the table is clicked
	 * @author Alina Liedtke
	 */
	@FXML
	void clickItem(MouseEvent mouseEvent) {
		ServerAccess.setIpAddress(txtInputIPAddress.getText().toString());
		if (mouseEvent.getClickCount() == 2) // Checking double click
		{
			try {
				Stage stage;
				FXMLLoader loader = new FXMLLoader(
						getClass().getResource("/bertelsbank/AccountDetailViewController/AccountDetailView.fxml"));
				Parent root = null;
				root = loader.<Parent>load();
				AccountDetailViewController controller = loader.<AccountDetailViewController>getController();
				controller.initData(tabAccount.getSelectionModel().getSelectedItem());
				Scene scene = new Scene(root);
				stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
				stage.setScene(scene);
				stage.show();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
