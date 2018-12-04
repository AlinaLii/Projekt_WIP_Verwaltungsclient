package de.fhdw.javafx.adminclient;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TransactionViewController {

    @FXML
    private Tab tbAccountView;
    
    @FXML
    private Tab tbTransactionView;
    
    @FXML
    private TableView<TableRow> tabTransaction;

    @FXML
    private Button btnRefresh;

    @FXML
    private ImageView imgLogo;

    @FXML
    private Text txtHeader;

	@FXML
	void newAccount(ActionEvent event) {
		try {
			Stage stage;
			FXMLLoader loader = new FXMLLoader(getClass().getResource("Kontodetailansicht.fxml"));
			Parent root = null;
			root = loader.<Parent>load();
			AccountDetailViewController controller = loader.<AccountDetailViewController>getController();
			Scene scene = new Scene(root);
			stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
    
    
}
