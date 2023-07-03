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

    /***
     * gets the data from the current node.
     * @return data from this node
     */
    public E getData() {
        return data;
    }

    /***
     * gets the node to the left fo the current node.
     * @return the node left of this node.
     */
    public BinNode<E> getLeft() {
        return left;
    }

    /***
     * gets the node to the right fo the current node.
     * @return the node right of this node.
     */
    public BinNode<E> getRight() {
        return right;
    }

    /***
     * sets the data in the current node
     * @param data the data to be set in this node
     */
    public void setData(E data) {
        this.data = data;
    }

    /***
     * sets the node to the left fo the current node.
     * @param left the new node to the left of this node
     */
    public void setLeft(BinNode<E> left) {
        this.left = left;
    }

    /***
     * sets the node to the right fo the current node.
     * @param right the new node to the right of this node
     */
    public void setRight(BinNode<E> right) {
        this.right = right;
    }
}