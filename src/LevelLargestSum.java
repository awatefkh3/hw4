import java.util.ArrayDeque;
import java.util.Collection;

/***
 * Represents a binary tree method - level largest sum
 */
public class LevelLargestSum {
    /***
     * this method gets the level with the largest su
     * @param root the root of the binary tree
     * @return the number of level with the largest sm
     */
    public static int getLevelWithLargestSum(BinNode<Integer> root) {
        if(root == null){
            return -1;
        }

        ArrayDeque<BinNode<Integer>> deque = new ArrayDeque<>();
        deque.add(root);
        int level = 0;
        int maxSum = Integer.MIN_VALUE;
        int maxSumLevel = Integer.MIN_VALUE;

        while(!deque.isEmpty()){
            int sum = 0;
            for(int size = deque.size(); size > 0; size--){
                BinNode<Integer> curr = deque.removeFirst();
                sum += curr.getData();
                if(curr.getLeft() != null){
                    deque.add(curr.getLeft());
                }
                if(curr.getRight() != null){
                    deque.add(curr.getRight());
                }
            }
            if(sum > maxSum){
                maxSum = sum;
                maxSumLevel = level;
            }
            level++;
        }

        return maxSumLevel;
    }

}

