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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class ServerAccess {

	static Account account;
	static ArrayList<Account> accountList;
	static BigDecimal accountBalance;
	private static String ipAddress = "127.0.0.1:9998";
	private int timeout = 2000;

	public static ArrayList<Account> getAccountList() {
		return accountList;
	}

	public static void setAccountList(ArrayList<Account> currentAccountList) {
		accountList = currentAccountList;
	}

	public static String getIpAddress() {
		return ipAddress;
	}

	public static void setIpAddress(String ipAddress) {
		ServerAccess.ipAddress = ipAddress;
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

	public HttpResponse getAccountResponse(String accountNumber) throws ClientProtocolException, IOException {

		HttpResponse response;
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpGet get = new HttpGet(String.format("http://%s/rest/account/%s", ipAddress, accountNumber));
		return response = client.execute(get);

	}

	public HttpResponse getAllAccountResponse() throws ClientProtocolException, IOException {
		HttpResponse response;
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpGet get = new HttpGet(String.format("http://%s/rest/allAccounts", ipAddress));
		return response = client.execute(get);
	}

	public HttpResponse getAllTransactionsResponse() throws ClientProtocolException, IOException {

		HttpResponse response;
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpGet get = new HttpGet(String.format("http://%s/rest/allTransactions", ipAddress));
		return response = client.execute(get);

	}

	public HttpResponse getFreeAccountNumberResponse() throws ClientProtocolException, IOException {

		HttpResponse response;
		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpGet get = new HttpGet(String.format("http://%s/rest/freeNumber", ipAddress));
		return response = client.execute(get);

	}

	public HttpResponse postTransaction(String senderNumber, String receiverNumber, String amount, String reference)
			throws ClientProtocolException, IOException {

		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(String.format("http://%s/rest/transaction", ipAddress));
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

		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(String.format("http://%s/rest/recallNumberReservation", ipAddress));
		List<NameValuePair> parameterList = new ArrayList<>();
		parameterList.add(new BasicNameValuePair("number", number));
		UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameterList, "UTF-8");

		post.setEntity(form);
		HttpResponse httpResponse = client.execute(post);
		return httpResponse;
	}

	public HttpResponse addAccount(String owner, String startBalance) throws ClientProtocolException, IOException {

		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(String.format("http://%s/rest/addAccount", ipAddress));
		List<NameValuePair> parameterList = new ArrayList<>();
		parameterList.add(new BasicNameValuePair("owner", owner));
		parameterList.add(new BasicNameValuePair("startBalance", startBalance));
		UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameterList, "UTF-8");

		post.setEntity(form);
		HttpResponse httpResponse = client.execute(post);
		return httpResponse;
	}



	public HttpResponse updateOwner(String number, String owner) throws ClientProtocolException, IOException {

		final HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, timeout);
	    DefaultHttpClient client = new DefaultHttpClient(httpParams);
		HttpPost post = new HttpPost(String.format("http://%s/rest/updateOwner", ipAddress));
		List<NameValuePair> parameterList = new ArrayList<>();
		parameterList.add(new BasicNameValuePair("number", number));
		parameterList.add(new BasicNameValuePair("owner", owner));
		UrlEncodedFormEntity form = new UrlEncodedFormEntity(parameterList, "UTF-8");

		post.setEntity(form);
		HttpResponse httpResponse = client.execute(post);
		return httpResponse;
	}

}
