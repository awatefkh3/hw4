import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class Database {
    private Map<String, String> data;
    /** max number of readers */
    private int maxNumOfReaders;
    /** the lock we use */
    private Lock lock = new ReentrantLock();
    /** read condition */
    private Condition readCondition;
    /** number of current reading threads */
    private int currentReadingNum;
    /** is a thread writing to the database */
    private boolean isWriting;
    /** write condition */
    private Condition writeCondition;
    /** numbers of threads trying to read */
    private int tryReading;
    /** numbers of threads trying to write */
    private int tryWriting;


    /***
     * constructor that takes maximum number of readers as parameter
     * @param maxNumOfReaders the maximum number of readers
     */
    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
        this.maxNumOfReaders = maxNumOfReaders;
        this.readCondition = lock.newCondition();
        this.currentReadingNum = 0;
        this.tryReading = 0;
        this.isWriting = false;
        this.writeCondition = lock.newCondition();
        this.tryWriting = 0;
    }

    /***
     * adds a pair of key value to the dataset
     * @param key the key we want to add
     * @param value the value we want to add
     */
    public void put(String key, String value) {
        data.put(key, value);
    }

    /***
     * ge the value of data in key
     * @param key the key we want to get its value
     * @return the value
     */
    public String get(String key) {
        return data.get(key);
    }

    public boolean readTryAcquire() {
        lock.lock();
        try {
            if (isWriting || currentReadingNum >= maxNumOfReaders || tryReading >= maxNumOfReaders) {
                return false;
            }
            tryReading++;
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
            currentReadingNum++;
        }

        catch (InterruptedException interruptedException) {
        } finally {
            lock.unlock();
        }
    }

    public void readRelease() throws IllegalMonitorStateException{
        lock.lock();
        try {
            if (currentReadingNum <= 0 && tryReading <= 0) {
                throw new IllegalMonitorStateException("Illegal read release attempt");
            } else{
                if(tryReading > 0){
                    tryReading--;
                }
                else if(currentReadingNum > 0){
                    currentReadingNum--;
                }
            }
        }
        finally {
            readCondition.signal();
            writeCondition.signal();
            lock.unlock();
        }
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