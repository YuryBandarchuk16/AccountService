package sample.GUI;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Constants;
import sample.DataBase.AccountService;
import sample.DataBase.DataBase;
import sample.TestingClient.TestRunner;
import sample.TestingClient.Tester;

import java.io.*;

public class Main extends Application {

    private static String fileName;
    private DataBase dataBase;
    private static PrintWriter printWriter;
    private static AccountService accountService;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("accountService.fxml"));
        primaryStage.setTitle("Account Service");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.setResizable(false);
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        fileName = System.getProperty("user.dir") + "/logs/log.txt";
        dataBase = new DataBase();
        dataBase.createTable();
        accountService = dataBase;
    }

    public static AccountService getAccountService() {
        return accountService;
    }

    public static void runTest() {
        try {
            Thread.sleep(5000L);
            TestRunner.run(accountService, Constants.NUMBER_OF_TESTING_THREADS); // this method starts testing threads
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void writeLog(String textToWrite) throws FileNotFoundException {
        printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName, true)));
        printWriter.println(textToWrite);
        printWriter.close();
    }
}
