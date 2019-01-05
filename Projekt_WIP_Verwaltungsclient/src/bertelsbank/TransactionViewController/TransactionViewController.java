package bertelsbank.TransactionViewController;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.TableColumn;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import bertelsbank.AccountViewController.AccountViewController;
import de.fhdw.javafx.adminclient.ServerAccess;
import de.fhdw.javafx.adminclient.TableRowAllTransactions;
import de.fhdw.javafx.adminclient.Transaction;

public class TransactionViewController {

	ServerAccess serverAccess = new ServerAccess();
	ArrayList<Transaction> transactionList;

	@FXML
	private TableView<TableRowAllTransactions> tabTransaction;

	@FXML
	private TableColumn<TableRowAllTransactions, String> tabDate;

	@FXML
	private TableColumn<TableRowAllTransactions, String> tabSenderNumber;

	@FXML
	private TableColumn<TableRowAllTransactions, String> tabReceiverNumber;

	@FXML
	private TableColumn<TableRowAllTransactions, BigDecimal> tabAmount;

	@FXML
	private TableColumn<TableRowAllTransactions, String> tabReference;

	@FXML
	private Button btnRefresh;

	@FXML
	private ImageView imgLogo;

	@FXML
	private Text txtHeader;

	@FXML
	private Button btnAccountView;

	@FXML
	private Text txtError;

	/**
	 * Updates the list of transactions. If there isn't any content in the list it calls the function that fills the table.
	 *
	 * @author Alina Liedtke
	 */
	@FXML
	private void initialize() {
		transactionList = refreshTransactions();
		if (transactionList != null) {
			fillTable();
		}
	}

	/**
	 * When the account view-button is pressed the view changes to account view.
	 *
	 * @param event the account view-button is pressed
	 * @author Alina Liedtke
	 */
	@FXML
	void AccountViewBtnAction(ActionEvent event) {
		try {
			Stage stage;
			FXMLLoader loader = new FXMLLoader(
					getClass().getResource("/bertelsbank/AccountViewController/AccountView.fxml"));
			Parent root = null;
			root = loader.<Parent>load();
			AccountViewController controller = loader.<AccountViewController>getController();
			Scene scene = new Scene(root);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			txtError.setText("Der Server ist nicht verfügbar.");
		}
	}


	/**
	 * When the refresh-button is pressed the function refreshBtnAction is called.
	 *
	 * @param event refresh-button is pressed
	 * @author Alina Liedtke
	 */
	@FXML
	public void refreshBtnAction(ActionEvent event) {
		refreshFnkt();
	}

	/**
	 * Updates the transaction table.
	 *
	 * @author Alina Liedtke
	 */
	public void refreshFnkt() {
		transactionList = refreshTransactions();
		fillTable();
	}

	/**
	 * Updates the transaction table. If this is not successful there are error messages.
	 *
	 * @return a list of all transactions
	 * @author Alina Liedtke
	 */
	protected ArrayList<Transaction> refreshTransactions() {
		try {
			HttpResponse response = serverAccess.getAllTransactionsResponse();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String transactionsJson = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().create();
				Transaction[] transactionArray = gson.fromJson(transactionsJson, Transaction[].class);
				ArrayList<Transaction> transactionList = new ArrayList<Transaction>(Arrays.asList(transactionArray));
				txtError.setText("");
				return transactionList;
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
	 * Loads the data (date, sender number, receiver number, amount, reference) into the transaction table.
	 *
	 * @author Alina Liedtke
	 */
	protected void fillTable() {
		tabDate.setCellValueFactory(new PropertyValueFactory<TableRowAllTransactions, String>("transactionDate"));
		tabSenderNumber.setCellValueFactory(new PropertyValueFactory<TableRowAllTransactions, String>("senderNumber"));
		tabReceiverNumber
				.setCellValueFactory(new PropertyValueFactory<TableRowAllTransactions, String>("receiverNumber"));
		tabAmount.setCellValueFactory(new PropertyValueFactory<TableRowAllTransactions, BigDecimal>("amount"));
		tabReference.setCellValueFactory(new PropertyValueFactory<TableRowAllTransactions, String>("reference"));

		List<TableRowAllTransactions> tableRows = new ArrayList<TableRowAllTransactions>();

		for (Transaction transaction : transactionList) {
			TableRowAllTransactions tableRow = new TableRowAllTransactions();
			tableRow.setTransactionDate(
					new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(transaction.getTransactionDate()));
			tableRow.setSenderNumber(transaction.getSender().getNumber());
			tableRow.setReceiverNumber(transaction.getReceiver().getNumber());
			tableRow.setAmount(transaction.getAmount());
			tableRow.setReferenceString(transaction.getReference());
			tableRows.add(tableRow);

		}

		ObservableList<TableRowAllTransactions> data = FXCollections.observableList(tableRows);
		tabTransaction.setItems(data);
	}

}
