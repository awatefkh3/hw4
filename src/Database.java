import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Database {
    private Map<String, String> data;
    private int maxNumOfReaders;
    private Lock lock = new ReentrantLock();
    private Condition max;
    private int currentReadingNum;
    private int currentWritingNum;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
        this.maxNumOfReaders = maxNumOfReaders;
        this.max = lock.newCondition();
        currentReadingNum = 0;
    }

    public void put(String key, String value) {
        data.put(key, value);
    }

    public String get(String key) {
        return data.get(key);
    }

    public boolean readTryAcquire() {
        lock.lock();
        try {
            if (Thread.currentThread() instanceof writeThread) {
                return false;
            }
            if (currentReadingNum >= maxNumOfReaders) {
//                System.out.println(Thread.activeCount());
                return false;
            }
            return true;
        }
        finally {
            lock.unlock();
        }
    }

    public void readAcquire() {
        lock.lock();
        try {
            while(currentReadingNum>= maxNumOfReaders) {
                max.await();
            }
            readThread t = new readThread();
            t.start();
            currentReadingNum++;
        }
        catch(InterruptedException interruptedException){
            System.out.println("interruptedException");
        }
        finally {
            lock.unlock();
        }


        // TODO: Add your code here...
    }

    public void readRelease() throws IllegalMonitorStateException{
        if(currentReadingNum == 0){
            throw new IllegalMonitorStateException("Illegal read release attempt");
        }
        currentReadingNum--;
        max.signalAll();
        // TODO: Add your code here...
    }

    public void writeAcquire() {
       // TODO: Add your code here...
    }

    public boolean writeTryAcquire() {
        // TODO: Add your code here...
        return true; // temp    DONT FORGET TO DELETE
    }

    public void writeRelease() {
        // TODO: Add your code here...
    }
}