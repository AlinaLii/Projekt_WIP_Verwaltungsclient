package de.fhdw.javafx.example;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ResultData {
	double result;

	public double getResult() {
		return result;
	}

	public void setResult(double result) {
		this.result = result;
	}

}
