import java.util.HashMap;
import java.util.Map;


public class Database {
    private Map<String, String> data;
    private int maxNumOfReaders;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
        this.maxNumOfReaders = maxNumOfReaders;
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean readTryAcquire() {
        if (Thread.currentThread() instanceof writeThread){
            return false;
        }
        if(Thread.activeCount() >= maxNumOfReaders){
            return false;
        }
        else {
            return true;
        }
    }

    public void readAcquire() {
//        readThread t = new readThread();
//        t.start();
        // TODO: Add your code here...
    }

    public void readRelease() {
        // TODO: Add your code here...
    }

    public void writeAcquire() {
       // TODO: Add your code here...
    }

    public boolean writeTryAcquire() {
        // TODO: Add your code here...
    }

    public void writeRelease() {
        // TODO: Add your code here...
    }
}