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
import javafx.scene.control.Tab;

public class SampleController {

	@FXML
	private Tab panelAccountOverview;

	@FXML
	private Tab panelTransactionOverview;

	public SampleController() {
		try {
			HttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet("http://localhost:9998/rest/account/" + "1000");
			HttpResponse response = client.execute(get);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String accountNumber = EntityUtils.toString(response.getEntity());
				Gson gson = new GsonBuilder().create();
				Account account = gson.fromJson(accountNumber, Account.class);
				System.out.println(account.getOwner());
				// return resultData.getResult();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
