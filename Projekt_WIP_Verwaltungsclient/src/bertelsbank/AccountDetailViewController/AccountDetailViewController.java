package bertelsbank.AccountDetailViewController;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bertelsbank.AccountViewController.AccountViewController;
import de.fhdw.javafx.adminclient.Account;
import de.fhdw.javafx.adminclient.ServerAccess;
import de.fhdw.javafx.adminclient.TableRowAccountTransactions;
import de.fhdw.javafx.adminclient.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AccountDetailViewController<currentAccount> {

	ServerAccess serverAccess = new ServerAccess();
	Account currentAccount;

	@FXML
	private Tab panelAccountOverview;

	@FXML
	private Tab panelTransactionOverview;

	@FXML
	private Text txtHeader;

	@FXML
	private ImageView imgLogo;

	@FXML
	private Button btnBack;

	@FXML
	private Button btnSave;

	@FXML
	private Text txtError;

	@FXML
	private Text txtError1;

	@FXML
	private TableView<TableRowAccountTransactions> tabTransaction;

	@FXML
	private TableColumn<TableRowAccountTransactions, String> tabDate;

	@FXML
	private TableColumn<TableRowAccountTransactions, String> tabSenderReceiver;

	@FXML
	private TableColumn<TableRowAccountTransactions, String> tabAccNumber;

	@FXML
	private TableColumn<TableRowAccountTransactions, BigDecimal> tabAmount;

	@FXML
	private TableColumn<TableRowAccountTransactions, String> tabReference;
	@FXML
	private TextField txtOwnerTextInput;

	@FXML
	private Text txtAccBalance;

	@FXML
	private Button btnRefresh;

	@FXML
	void refreshBtnAction(ActionEvent event) {
		initData(refreshAccount());
	}

	public Account refreshAccount() {
		try {
			HttpResponse response = serverAccess.getAccountResponse(currentAccount.getNumber());
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String accountNumber = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().create();
				Account account = gson.fromJson(accountNumber, Account.class);
				ServerAccess.setAccount(account);
				txtError1.setText("");
				return account;
			} else {
				txtError1.setText(EntityUtils.toString(response.getEntity()) + " (Fehler: "
						+ response.getStatusLine().getStatusCode() + ")");
			}
		} catch (IOException e) {
			e.printStackTrace();
			txtError1.setText("Server nicht verfügbar");
		}
		return null;
	}

	@FXML
	void backBtnAction(ActionEvent event) {
		try {
			Stage stage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/bertelsbank/AccountViewController/AccountView.fxml"));
			Parent root = null;
			root = loader.<Parent>load();
			AccountViewController controller = loader.<AccountViewController>getController();
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
	void saveBtnAction(ActionEvent event) {
		try {

			HttpResponse httpResponse = serverAccess.updateOwner(currentAccount.getNumber(),
					txtOwnerTextInput.getText());

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			String entityMsg = "";
			if (statusCode != HttpStatus.SC_OK) {
				entityMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
				String errorMsg = " (Fehler " + httpResponse.getStatusLine().getStatusCode() + ")";
				txtError.setText(entityMsg + errorMsg);
			} else {
				txtError.setText("Das Konto wurde erfolgreich umbenannt.");
			}

		} catch (IOException e) {
			txtError.setText("Dieses Feld darf nicht leer sein.");
		}
	}

	@FXML
	private void initialize() {
	}

	public void initData(Account account) {

		currentAccount = account;
		currentAccount = refreshAccount();
		tabDate.setCellValueFactory(new PropertyValueFactory<TableRowAccountTransactions, String>("transactionDate"));
		tabSenderReceiver
				.setCellValueFactory(new PropertyValueFactory<TableRowAccountTransactions, String>("senderReceiver"));
		tabAccNumber
				.setCellValueFactory(new PropertyValueFactory<TableRowAccountTransactions, String>("accountNumber"));
		tabAmount.setCellValueFactory(new PropertyValueFactory<TableRowAccountTransactions, BigDecimal>("amount"));
		tabReference.setCellValueFactory(new PropertyValueFactory<TableRowAccountTransactions, String>("reference"));

		BigDecimal accountBalance = new BigDecimal(0);

		List<TableRowAccountTransactions> tableRows = new ArrayList<TableRowAccountTransactions>();
		List<Transaction> transactions = currentAccount.getTransactions();
		for (Transaction transaction : transactions) {
			TableRowAccountTransactions tableRow = new TableRowAccountTransactions();
			tableRow.setTransactionDate(
					new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(transaction.getTransactionDate()));
			if (transaction.getSender().getNumber().equals(currentAccount.getNumber())) {
				tableRow.setSenderReceiver(transaction.getReceiver().getOwner());
				tableRow.setAccountNumber(transaction.getReceiver().getNumber());
				accountBalance = accountBalance.subtract(transaction.getAmount());
				BigDecimal negative = new BigDecimal(-1);
				tableRow.setAmount(transaction.getAmount().multiply(negative));
			} else {
				tableRow.setSenderReceiver(transaction.getSender().getOwner());
				tableRow.setAccountNumber(transaction.getSender().getNumber());
				accountBalance = accountBalance.add(transaction.getAmount());
				tableRow.setAmount(transaction.getAmount());
			}

			tableRow.setReferenceString(transaction.getReference());
			tableRows.add(tableRow);

			String accountBalanceAsString = accountBalance.toString();
			txtAccBalance.setText(accountBalanceAsString + " €");
			txtHeader.setText("Detailansicht Konto " + currentAccount.getNumber());
			txtOwnerTextInput.setText(currentAccount.getOwner());
			serverAccess.setAccountBalance(accountBalance);
		}

		ObservableList<TableRowAccountTransactions> data = FXCollections.observableList(tableRows);
		tabTransaction.setItems(data);

	}
}
