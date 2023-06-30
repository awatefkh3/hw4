import java.util.ArrayDeque;
import java.util.Collection;

public class LevelLargestSum {
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
            int size = deque.size();
            int sum = 0;
            while(size > 0){
                BinNode<Integer> curr = deque.removeFirst();
                sum += curr.getData();
                if(curr.getLeft() != null){
                    deque.add(curr.getLeft());
                }
                if(curr.getRight() != null){
                    deque.add(curr.getRight());
                }
                size--;
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

