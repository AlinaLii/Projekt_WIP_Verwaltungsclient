package de.fhdw.javafx.adminclient;

	import java.io.IOException;
	import java.math.BigDecimal;

	import org.apache.http.HttpResponse;
	import org.apache.http.client.ClientProtocolException;
	import org.apache.http.client.HttpClient;
	import org.apache.http.client.methods.HttpGet;
	import org.apache.http.client.methods.HttpPost;
	import org.apache.http.impl.client.HttpClients;
	import java.util.ArrayList;
import java.util.List;

	import org.apache.http.impl.client.DefaultHttpClient;
	import org.apache.http.NameValuePair;
	import org.apache.http.client.entity.UrlEncodedFormEntity;
	import org.apache.http.message.BasicNameValuePair;

	public class ServerAccess {

		static Account account;

		static ArrayList accountList;

		static BigDecimal accountBalance;

		public static ArrayList getAccountList() {
			return accountList;
		}

		public static void setAccountList(ArrayList accountListt) {
			accountList = accountListt;

		}


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
			HttpGet get = new HttpGet(String.format("http://localhost:9998/rest/account/" + accountNumber));
			return response = client.execute(get);

		}

		public HttpResponse getAllAccountResponse() throws ClientProtocolException, IOException{

			HttpResponse response;
			HttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet(String.format("http://localhost:9998/rest/allAccounts"));
			return response = client.execute(get);

		}

		public HttpResponse getFreeAccountNumberResponse() throws ClientProtocolException, IOException{

			HttpResponse response;
			HttpClient client = HttpClients.createDefault();
			HttpGet get = new HttpGet(String.format("http://localhost:9998/rest/freeNumber"));
			return response = client.execute(get);

		}

		public HttpResponse postTransaction(String senderNumber, String receiverNumber, String amount, String reference) throws ClientProtocolException, IOException {

			  HttpClient client = new DefaultHttpClient();
			  HttpPost post = new HttpPost("http://localhost:9998/rest/transaction");
			  List<NameValuePair> parameterList = new ArrayList<>();
			  parameterList.add(new BasicNameValuePair("senderNumber", senderNumber));
			  parameterList.add(new BasicNameValuePair("receiverNumber", receiverNumber));
			  parameterList.add(new BasicNameValuePair("amount", amount));
			  parameterList.add(new BasicNameValuePair("reference", reference));
			  UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameterList, "UTF-8");

			  post.setEntity(form);
			  HttpResponse httpResponse = client.execute(post);
			  return httpResponse;
		}

		public HttpResponse cancelReservation(String number) throws ClientProtocolException, IOException {

			  HttpClient client = new DefaultHttpClient();
			  HttpPost post = new HttpPost("http://localhost:9998/rest/recallNumberReservation");
			  List<NameValuePair> parameterList = new ArrayList<>();
			  parameterList.add(new BasicNameValuePair("number", number));
			  UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameterList, "UTF-8");

			  post.setEntity(form);
			  HttpResponse httpResponse = client.execute(post);
			  return httpResponse;
		}


		public HttpResponse addAccount(String owner, String startBalance) throws ClientProtocolException, IOException {

			  HttpClient client = new DefaultHttpClient();
			  HttpPost post = new HttpPost("http://localhost:9998/rest/addAccount");
			  List<NameValuePair> parameterList = new ArrayList<>();
			  parameterList.add(new BasicNameValuePair("owner", owner));
			  parameterList.add(new BasicNameValuePair("startBalance", startBalance));
			  UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameterList, "UTF-8");

			  post.setEntity(form);
			  HttpResponse httpResponse = client.execute(post);
			  return httpResponse;
		}

		public HttpResponse updateOwner(String number, String owner) throws ClientProtocolException, IOException {

			  HttpClient client = new DefaultHttpClient();
			  HttpPost post = new HttpPost("http://localhost:9998/rest/updateOwner");
			  List<NameValuePair> parameterList = new ArrayList<>();
			  parameterList.add(new BasicNameValuePair("number", number));
			  parameterList.add(new BasicNameValuePair("owner", owner));
			  UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameterList, "UTF-8");

			  post.setEntity(form);
			  HttpResponse httpResponse = client.execute(post);
			  return httpResponse;
		}


	}
