package sample.TestingClient;

import sample.DataBase.AccountService;
import sample.GUI.Main;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Tester extends Thread {

    private int rCount, wCount;
    private AccountService accountService;

    public Tester(AccountService accountService, int rCount, int wCount) {
        this.rCount = rCount;
        this.wCount = wCount;
        this.accountService = accountService;
    }

    @Override
    public void run() {
        System.out.println(rCount + " " + wCount);
        ArrayList<Task> tasks = TaskCreator.createTasks(rCount, wCount);
        long totalSpentTime = 0L;
        for (Task currentTask : tasks) {
            if (currentTask.isAdd()) {
                try {
                    accountService.addAmount(currentTask.getId(), currentTask.getAmount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                totalSpentTime += accountService.getRunTimeOfLastExecutedCommand();
            } else {
                try {
                    accountService.getAmount(currentTask.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                totalSpentTime += accountService.getRunTimeOfLastExecutedCommand();
            }
            try {
                sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        double timeInSeconds = ((double)(totalSpentTime) / 1000.0);
        String result = "---------------------------------\n" +
                "Total number of queries processed: " + tasks.size() + "\n" +
                "Number of GET queries: " + rCount + "\n" +
                "Number of ADD queries: " + wCount + "\n" +
                "Total time of processing all the queries: " + timeInSeconds + "\n" +
                "Average time per query: " + (timeInSeconds / (double)(tasks.size())) + "\n" +
                "---------------------------------\n";
        try {
            Main.writeLog(result);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
