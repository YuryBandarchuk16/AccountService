package sample.TestingClient;

import sample.Constants;
import sample.DataBase.AccountService;

import java.util.Random;

public abstract class TestRunner {

    public static void run(AccountService accountService, int numberOfThreads) {
        Random random = new Random();
        for (int index = 0; index < numberOfThreads; index++) {
            Tester tester = new Tester(accountService, random.nextInt(Constants.RANGE_FOR_R_COUNT) + 1, random.nextInt(Constants.RANGE_FOR_W_COUNT));
            tester.start();
            try {
                Thread.sleep(5000L);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
