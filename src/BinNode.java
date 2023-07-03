/***
 * Represents a BinNode of type E
 * @param <E> type parameter of the BinNode
 */
public class BinNode<E> {
    /** the type parameter */
    private E data;
    /** the BinNode on left */
    private BinNode<E> left;
    /** the BinNode on right */
    private BinNode<E> right;

    /***
     * constructor that takes all attributes values as parameters
     * @param data the type parameter of BinNode
     * @param left the BinNode on left
     * @param right the BinNode on right
     */
    public BinNode(E data, BinNode<E> left, BinNode<E> right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    /***
     * constructor that takes only the type parameter as parameter
     * @param data the type parameter of BinNode
     */
    public BinNode(E data) {
        this(data, null, null);
    }

    public E getData() {
        return data;
    }

    public BinNode<E> getLeft() {
        return left;
    }

    public BinNode<E> getRight() {
        return right;
    }

    public void setData(E data) {
        this.data = data;
    }

    public void setLeft(BinNode<E> left) {
        this.left = left;
    }

    public void setRight(BinNode<E> right) {
        this.right = right;
    }
}