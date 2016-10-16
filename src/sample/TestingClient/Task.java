package sample.TestingClient;

public class Task {


    private Type type;
    private int id;
    private long amount;


    public Task(int id, long amount) {
        this.id = id;
        this.amount = amount;
        this.type = Type.ADD;
    }

    public Task(int id) {
        this.id = id;
        this.type = Type.GET;
    }

    private enum Type {
        ADD,
        GET
    }

    public int getId() {
        return id;
    }

    public long getAmount() {
        return amount;
    }

    public boolean isAdd() {
        return this.type == Type.ADD;
    }

    public boolean isGet() {
        return this.type == Type.GET;
    }

}
