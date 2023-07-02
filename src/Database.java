import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Database {
    private Map<String, String> data;
    private int maxNumOfReaders;
    private Lock lock = new ReentrantLock();
    private Condition readCondition;
    private int currentReadingNum;
    private boolean isWriting;
    private Condition writeCondition;
    private int tryReading;
    private int tryWriting;

    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
        this.maxNumOfReaders = maxNumOfReaders;
        this.readCondition = lock.newCondition();
        this.currentReadingNum = 0;
        this.isWriting = false;
        this.writeCondition = lock.newCondition();
        this.tryWriting = 0;
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
            if (Thread.currentThread() instanceof WriteThread) {
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
            while(isWriting || currentReadingNum >= maxNumOfReaders) {
                readCondition.await();

            }
            ReadThread t = new ReadThread();
            t.start();
            currentReadingNum++;
        }

        catch (InterruptedException interruptedException) {
        } finally {
            lock.unlock();
        }
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
        lock.lock();
        try{
            while(isWriting || currentReadingNum > 0){
                writeCondition.await();
            }
            isWriting = true;
        }
        catch (InterruptedException interruptedException) {
        }
        finally {
            lock.unlock();
        }
    }

    public boolean writeTryAcquire() {
        lock.lock();
        try {
            if(isWriting || currentReadingNum > 0 || tryWriting > 0){
                return false;
            }
            tryWriting++;
            return true;
        }
        finally{
            lock.unlock();
        }
    }

    public void writeRelease() throws IllegalMonitorStateException{
        lock.lock();
        try {
            if (!isWriting && tryWriting <= 0) {
                throw new IllegalMonitorStateException("Illegal write release attempt");
            } else {
                if(tryWriting > 0){
                    tryWriting--;
                }
                if(isWriting){
                    isWriting = false;
                }
            }
        }
        finally {
            readCondition.signal();
            writeCondition.signal();
            lock.unlock();
        }
    }
}