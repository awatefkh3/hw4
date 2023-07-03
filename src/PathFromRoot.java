/***
 * Represents a binary tree method - path from root
 */
public class PathFromRoot {
    /***
     * this method checks if the word given as parameter exists in the binary tree
     * @param root the root of the binary tree
     * @param str the string we are searching in the tree
     * @return true if the word exists in the tree, false otherwise
     */
    public static boolean doesPathExist(BinNode<Character> root, String str) {
        if(str.length() == 0){
            return true;
        }
        if(str.charAt(0) != root.getData()){
            return false;
        }
        if(doesPathExist(root.getRight(), str.substring(1))){
            return true;
        }
        if(doesPathExist(root.getLeft(), str.substring(1))){
            return true;
        }
        return false;
    }

}
