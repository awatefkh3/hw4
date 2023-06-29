public class PathFromRoot {
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
