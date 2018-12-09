package de.fhdw.javafx.adminclient;

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
    void AccountViewBtnAction(ActionEvent event) {
		try {
			Stage stage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
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
	private void initialize() {
		transactionList = refreshTransactions();
		fillTable();
	}

	@FXML
	public void refreshBtnAction(ActionEvent event) {
		refreshFnkt();
	}

	public void refreshFnkt() {
		transactionList = refreshTransactions();
		fillTable();
	}

	protected ArrayList<Transaction> refreshTransactions() {
		try {
			HttpResponse response = serverAccess.getAllTransactionsResponse();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String transactionsJson = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().create();
				Transaction[] transactionArray = gson.fromJson(transactionsJson, Transaction[].class);
				ArrayList<Transaction> transactionList = new ArrayList<Transaction>(Arrays.asList(transactionArray));
				// ServerAccess.setAccountList(transactionList);
				// txtError.setText("");
				return transactionList;
			} else {
				// txtError.setText(EntityUtils.toString(response.getEntity()) +
				// " (Fehler: " + response.getStatusLine().getStatusCode() +
				// ")");
			}
		} catch (IOException e) {
			e.printStackTrace();
			// txtError.setText("Server nicht verfügbar");
		}
		return null;
	}

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
