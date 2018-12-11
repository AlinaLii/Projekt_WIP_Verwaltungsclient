package de.fhdw.javafx.adminclient;

import java.io.IOException;
import java.math.BigDecimal;


import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewAccountController {

	ServerAccess serverAccess = new ServerAccess();

	@FXML
	private Text txtHeader;

    @FXML
    private Text txtError;

	@FXML
	private TextField txtAccountOwner;

	@FXML
	private TextField txtStartBalance;
	@FXML
	private Text txtAccountNumber;


    @FXML
    private ImageView imgLogo;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnCancel;

	String freeNumber = "";

	@FXML
	void initialize() {
		try {

			HttpResponse response = serverAccess.getFreeAccountNumberResponse();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				freeNumber = EntityUtils.toString(response.getEntity());
				txtAccountNumber.setText(freeNumber);

			} else {

				// neues Textfeld in scenebuikder für fehlercode
				// Textfeld für Fehlermeldungen in jeder view wo auf den server
				// zugegriffen wird
			}
		} catch (IOException e) {
			// in das o.g. textfeld server nicht gefunden feeehler
		}

	}

	@FXML
	void cancelBtnAction(ActionEvent event) {
		try {
			if (freeNumber != "") {

				HttpResponse httpResponse = serverAccess.cancelReservation(freeNumber);

				int statusCode = httpResponse.getStatusLine().getStatusCode();
				String entityMsg = "";
				if (statusCode != HttpStatus.SC_NO_CONTENT) {
					entityMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
					String errorMsg = " (Fehler " + httpResponse.getStatusLine().getStatusCode() + ")";
					// errorText.setText(entityMsg + errorMsg); später
					// reinnehmen nach fehlerfeld
				}
			}

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
	void saveBtnAction(ActionEvent event) {
		//System.out.println(txtAccountOwner.getText() + "--" + txtStartBalance.getText());
			try {

			if (!freeNumber.equals("")) {
				System.out.println(freeNumber);
				//System.out.println(txtAccountOwner.getText());
				HttpResponse httpResponse = serverAccess.addAccount(freeNumber, txtAccountOwner.getText(), txtStartBalance.getText());

				int statusCode = httpResponse.getStatusLine().getStatusCode();
				String entityMsg = "";
				if (statusCode != HttpStatus.SC_OK) {
					entityMsg = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
					String errorMsg = entityMsg + " (Fehler " + httpResponse.getStatusLine().getStatusCode() + ")";
					txtError.setText(errorMsg);
				} else {
					Stage stage;
					FXMLLoader loader = new FXMLLoader(getClass().getResource("AccountView.fxml"));
					Parent root = null;
					root = loader.<Parent>load();
					AccountViewController controller = loader.<AccountViewController>getController();
					Scene scene = new Scene(root);
					stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
					stage.setScene(scene);
					stage.show();

				}
			 } else {
				txtError.setText("Konto konnte nicht angelegt werden.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			txtError.setText("Konto konnte nicht angelegt werden.");
		}

	}

}
