package de.fhdw.javafx.adminclient;

	import java.io.IOException;
	import java.math.BigDecimal;

	import org.apache.http.HttpResponse;
	import org.apache.http.HttpStatus;
	import org.apache.http.client.ClientProtocolException;
	import org.apache.http.client.HttpClient;
	import org.apache.http.client.methods.HttpGet;
	import org.apache.http.client.methods.HttpPost;
	import org.apache.http.impl.client.HttpClients;
	import org.apache.http.util.EntityUtils;

	import com.google.gson.Gson;
	import com.google.gson.GsonBuilder;

	public class ServerAccess {

		static Account account;

		static BigDecimal accountBalance;

		public static BigDecimal getAccountBalance() {
			return accountBalance;
		}

		public static void setAccountBalance(BigDecimal accountBalance) {
			ServerAccess.accountBalance = accountBalance;
		}

		public static Account getAccount() {
			return account;
		}

		public static void setAccount(Account loginAccount) {
			account = loginAccount;
		}

		public HttpResponse getAccountResponse(String accountNumber) throws ClientProtocolException, IOException{

			HttpResponse response;
			HttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet(String.format("http://localhost:9998/rest/account/" + 1000));
			return response = client.execute(get);

		}

		public HttpResponse getAllAccountResponse(String accountNumber) throws ClientProtocolException, IOException{

			HttpResponse response;
			HttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet(String.format("http://localhost:9998/rest/allAccounts"));
			return response = client.execute(get);

		}
		
		public void postTransaction() throws ClientProtocolException, IOException{

			HttpResponse response;
			HttpClient client = HttpClients.createDefault();
			HttpPost set = new HttpPost(String.format("http://localhost:9998/rest/account/"));
		}
	}
