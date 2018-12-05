package de.fhdw.javafx.adminclient;

import java.math.BigDecimal;

public class TableRowAllTransactions {
	private String transactionDate;
	private String senderNumber;
	private String receiverNumber;
	private BigDecimal amount;
	private String reference;

	public String getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String getSenderNumber() {
		return senderNumber;
	}
	public void setSenderNumber(String senderNumber) {
		this.senderNumber = senderNumber;
	}
	public String getReceiverNumber() {
		return receiverNumber;
	}
	public void setReceiverNumber(String receiverNumber) {
		this.receiverNumber = receiverNumber;
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

