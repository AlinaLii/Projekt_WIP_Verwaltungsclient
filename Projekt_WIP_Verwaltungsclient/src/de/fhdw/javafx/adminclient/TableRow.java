package de.fhdw.javafx.adminclient;

import java.math.BigDecimal;

public class TableRow {
	private String transactionDate;
	private String senderReceiver;
	private String  accountNumber;
	private BigDecimal amount;
	private String reference;

	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getSenderReceiver() {
		return senderReceiver;
	}
	public void setSenderReceiver(String senderReceiver) {
		this.senderReceiver = senderReceiver;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public BigDecimal getAmount() {
		return amount;
	}
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	public String getReference() {
		return reference;
	}
	public void setReferenceString(String reference) {
		this.reference = reference;
	}
}

