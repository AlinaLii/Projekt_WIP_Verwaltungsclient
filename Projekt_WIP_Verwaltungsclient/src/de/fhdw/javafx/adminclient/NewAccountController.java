package de.fhdw.javafx.adminclient;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class NewAccountController {

	ServerAccess serverAccess = new ServerAccess();

    @FXML
    private Text txtHeader;

    @FXML
    private TextField txtAccountOwner;

    @FXML
    private TextField txtAccBalance;

    @FXML
    private TextField txtStartBalance;
    @FXML
    private Text txtAccountNumber;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    void initialize(){
    	try {

			HttpResponse response = serverAccess.getFreeAccountNumberResponse();
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				String freeNumber = EntityUtils.toString(response.getEntity());
				txtAccountNumber.setText(freeNumber);
			} else {
				//neues Textfeld in scenebuikder für fehlercode
				// Textfeld für Fehlermeldungen in jeder view wo auf den server
				// zugegriffen wird
			}
		} catch (IOException e) {
			// in das o.g. textfeld server nicht gefunden
		}

	    }

    @FXML
    void cancelBtnAction(ActionEvent event) {
    	try {
			Stage stage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Kontenuebersicht.fxml"));
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

    }


}
