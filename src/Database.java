import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/***
 * Represents a database.
 */
public class Database {
    /** data map */
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
    /** set of current threads */
    private Set<Thread> currentThreads;


    /***
     * constructor that takes maximum number of readers as parameter
     * @param maxNumOfReaders the maximum number of readers
     */
    public Database(int maxNumOfReaders) {
        data = new HashMap<>();  // Note: You may add fields to the class and initialize them in here. Do not add parameters!
        this.currentThreads = new HashSet<>();
        this.maxNumOfReaders = maxNumOfReaders;
        this.readCondition = lock.newCondition();
        this.currentReadingNum = 0;
        this.isWriting = false;
        this.writeCondition = lock.newCondition();
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

    /***
     * this method checks if reading thread can run and if it can, runs the thread.
     * @return true if read thread can run, false otherwise
     */
    public boolean readTryAcquire() {
        lock.lock();
        try {
            if(isWriting || currentReadingNum >= maxNumOfReaders){
                return false;
            }
            readAcquire();
            return true;
        }
        finally {
            lock.unlock();
        }
    }

    /***
     * runs the thread. if the thread cannot run right now it will wait for its turn.
     */
    public void readAcquire() {
        lock.lock();
        try {
            while(isWriting || currentReadingNum >= maxNumOfReaders) {
                readCondition.await();

            }
            currentReadingNum++;
            currentThreads.add(Thread.currentThread());
        }

        catch (InterruptedException interruptedException) {
        } finally {
            lock.unlock();
        }
    }

    /***
     * releases the reading thread, and signals the waiting threads.
     * @throws IllegalMonitorStateException when the release attempt is illegal
     */
    public void readRelease() throws IllegalMonitorStateException{
        lock.lock();
        try {
            if ( currentReadingNum <= 0 || !currentThreads.contains(Thread.currentThread())) {
                throw new IllegalMonitorStateException("Illegal read release attempt");
            }
            else{
                currentReadingNum--;
                currentThreads.remove(Thread.currentThread());
            }
        }
        finally {
            readCondition.signalAll();
            writeCondition.signalAll();
            lock.unlock();
        }
    }

    /***
     * runs the thread. if the thread cannot run right now it will wait for its turn.
     */
    public void writeAcquire() {
        lock.lock();
        try{
            while(isWriting || currentReadingNum > 0){
                writeCondition.await();
            }
            isWriting = true;
            currentThreads.add(Thread.currentThread());
        }
        catch (InterruptedException interruptedException) {
        }
        finally {
            lock.unlock();
        }
    }

    /***
     * this method checks if writing thread can run and if it can, runs the thread.
     * @return true if write thread can run, false otherwise
     */
    public boolean writeTryAcquire() {
        lock.lock();
        try {
            if(isWriting || currentReadingNum > 0 ){
                return false;
            }
            writeAcquire();
            return true;
        }
        finally{
            lock.unlock();
        }
    }

    /***
     * releases the writing thread, and signals the waiting threads.
     * @throws IllegalMonitorStateException when the release attempt is illegal
     */
    public void writeRelease() throws IllegalMonitorStateException{
        lock.lock();
        try {
            if (!isWriting|| !currentThreads.contains(Thread.currentThread())) {
                throw new IllegalMonitorStateException("Illegal write release attempt");
            } else {
                    isWriting = false;
                    currentThreads.remove(Thread.currentThread());
            }
        }
        finally {
            readCondition.signalAll();
            writeCondition.signalAll();
            lock.unlock();
        }
    }
}