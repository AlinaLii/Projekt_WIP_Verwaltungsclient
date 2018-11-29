package de.fhdw.javafx.adminclient;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;

public class AccountDetailViewController {

	ServerAccess serverAccess = new ServerAccess();
	Account currentAccount;


    @FXML
    private Tab panelAccountOverview;

    @FXML
    private Tab panelTransactionOverview;

    @FXML
    private Text header;

    @FXML
	private TableView<TableRow> transactionTable;

	@FXML
	private TableColumn<TableRow, String> tabDate;

	@FXML
	private TableColumn<TableRow, String> tabSenderReceiver;

	@FXML
	private TableColumn tabAccNumber;

	@FXML
	private TableColumn<TableRow, BigDecimal> tabAmount;

	@FXML
	private TableColumn<TableRow, String> tabReference;

    @FXML
    private Button save;

    @FXML
    private TextField ownerTextInput;

    @FXML
    private Label balanceLabel;



	@FXML
	private void initialize() {

		currentAccount = ServerAccess.getAccount();


		tabDate.setCellValueFactory(new PropertyValueFactory<TableRow, String>("transactionDate"));
		tabSenderReceiver.setCellValueFactory(new PropertyValueFactory<TableRow, String>("senderReceiver"));
		tabAccNumber.setCellValueFactory(new PropertyValueFactory<TableRow, String>("accountNumber"));
		tabAmount.setCellValueFactory(new PropertyValueFactory<TableRow, BigDecimal>("amount"));
		tabReference.setCellValueFactory(new PropertyValueFactory<TableRow, String>("reference"));

		List<TableRow> tableRows = new ArrayList<TableRow>();
		List<Transaction> transactions = currentAccount.getTransactions();
		for (Transaction transaction : transactions) {
			TableRow tableRow = new TableRow();
			tableRow.setTransactionDate(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(transaction.getTransactionDate()));
			if (transaction.getSender().getNumber().equals(currentAccount.getNumber())) {
				tableRow.setSenderReceiver(transaction.getReceiver().getOwner());
				tableRow.setAccountNumber(transaction.getReceiver().getNumber());

			} else {
				tableRow.setSenderReceiver(transaction.getSender().getOwner());
				tableRow.setAccountNumber(transaction.getSender().getNumber());

			}
			tableRow.setAmount(transaction.getAmount());
			tableRow.setReferenceString(transaction.getReference());
			tableRows.add(tableRow);

		}



	}

	void initData(String x) {
		System.out.print(x);
	}


}