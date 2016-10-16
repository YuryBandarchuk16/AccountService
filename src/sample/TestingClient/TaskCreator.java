package sample.TestingClient;


import sample.Constants;

import java.util.ArrayList;
import java.util.Random;

public abstract class TaskCreator {

    public static ArrayList<Task>  createTasks(int rCount, int wCount) {
        // rCount - number of calls of getAmount
        // wCount - number of calls of AddAmount
        Random random = new Random();
        ArrayList<Task> result = new ArrayList<>();
        int summaryNumberOfProblems = rCount + wCount;
        for (int tasksAdded = 0; tasksAdded < summaryNumberOfProblems; tasksAdded++) {
            if (rCount == 0) {
                result.add(new Task(random.nextInt(Constants.RANGE_FOR_ID), Constants.RANGE_FOR_ADD_AMOUNT + random.nextInt(Constants.RANGE_FOR_RANDOM)));
                continue;
            }
            if (wCount == 0) {
                result.add(new Task(random.nextInt(Constants.RANGE_FOR_ID)));
                continue;
            }
            if (random.nextInt() % 2 == 0) {
                --rCount;
                result.add(new Task(random.nextInt(Constants.RANGE_FOR_ID)));
            } else {
                --wCount;
                result.add(new Task(random.nextInt(Constants.RANGE_FOR_ID), Constants.RANGE_FOR_ADD_AMOUNT + random.nextInt(Constants.RANGE_FOR_RANDOM)));
            }
        }
        return result;
    }

}
