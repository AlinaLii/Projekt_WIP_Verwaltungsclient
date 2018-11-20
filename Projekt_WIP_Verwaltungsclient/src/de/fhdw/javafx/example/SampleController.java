package de.fhdw.javafx.example;


import java.io.IOException;

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
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SampleController {

    @FXML
    private Tab panelAccountOverview;

    @FXML
    private Tab panelTransactionOverview;

    @FXML
    private Text header;

    @FXML
    private Text balanceTextView;

    @FXML
    private TableView<?> transactionTable;

    @FXML
    private Button save;

    @FXML
    private TextField ownerTextInput;


	public SampleController() {
		try {
			HttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet("http://localhost:9998/rest/account/" + "1000");
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String accountJson = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().create();
				Account account = gson.fromJson(accountJson, Account.class);
				System.out.println(account.getOwner());
			//	ownerTextInput.setText(account.getOwner());
				header.setText("Detailansicht Konto " + account.getNumber());
				// return resultData.getResult();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
