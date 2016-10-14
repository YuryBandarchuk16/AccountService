package sample.GUI;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.Constants;

public class Controller {

    @FXML
    private Button addAmountButton, getBalanceButton;

    @FXML
    private TextField idText, amountText, idTextQ, balanceText;

    @FXML
    private void addAmountButtonClicked() {
        if (!isValidId(idText.getText())) {
            createAlert("OK", "ID is incorrect!");
            return;
        }
        if (!isValidAmount(amountText.getText())) {
            createAlert("OK", "Amount is incorrect!");
            return;
        }
        Integer id = Integer.parseInt(idText.getText());
        Long amount = Long.parseLong(amountText.getText());
        try {
            Main.getAccountService().addAmount(id, amount); // trying to make a query to database
            createAlert("OK", "" + amount + "  added to balance with ID = " + id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getBalanceButtonClicked() {
        if (!isValidId(idTextQ.getText())) {
            createAlert("OK", "ID is incorrect!");
            return;
        }
        Integer id = Integer.parseInt(idTextQ.getText());
        String result = "";
        try {
            result = Main.getAccountService().getAmount(id).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        balanceText.setText(result);
    }

    private boolean isOnlyDigits(String s) {
        for (int index = 0; index < s.length(); index++) {
            int code = (int)s.charAt(index) - 48;
            if (code >= 0 && code <= 9) {
                continue;
            } else {
                return false;
            }

        }
        return true;
    }

    private boolean isValidId(String s) {
        if (s.length() == 0 || s.length() > Constants.MAX_INT.length()) {
            return false;
        }
        if (!isOnlyDigits(s)) {
            return false;
        }
        for (int index = 0; index < s.length(); index++) {
            if (s.charAt(index) < Constants.MAX_INT.charAt(index)) {
                return true;
            } else if (s.charAt(index) > Constants.MAX_INT.charAt(index)) {
                return false;
            }
        }
        return true;
    }

    private boolean isValidAmount(String s) {
        if (s.length() > 0 && s.charAt(0) == '-') {
            s = s.substring(1);
        }
        if (s.length() == 0 || s.length() > Constants.MAX_LONG.length()) {
            return false;
        }
        if (!isOnlyDigits(s)) {
            return false;
        }
        if (s.length() < Constants.MAX_LONG.length()) {
            return true;
        }
        for (int index = 0; index < s.length(); index++) {
            if (s.charAt(index) < Constants.MAX_LONG.charAt(index)) {
                return true;
            } else if (s.charAt(index) > Constants.MAX_LONG.charAt(index)) {
                return false;
            }
        }
        return true;
    }

    private void createAlert(String textButton, String text) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.setOnCloseRequest(e -> Platform.exit());
        Button b = new Button(textButton);
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialogStage.close();
            }
        });
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(new Text(text), b).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
        dialogStage.show();
        return;
    }

}
