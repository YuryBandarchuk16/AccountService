package sample.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.DataBase.AccountService;
import sample.DataBase.DataBase;

public class Main extends Application {

    private DataBase dataBase;
    private static AccountService accountService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("accountService.fxml"));
        primaryStage.setTitle("Account Service");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        dataBase = new DataBase();
        dataBase.createTable();
        accountService = dataBase;
    }

    public static AccountService getAccountService() {
        return accountService;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
