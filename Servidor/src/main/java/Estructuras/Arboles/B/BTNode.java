package Estructuras.Arboles.B;



/**
 * Class BTNode
 * @author tnguyen
 */
public class BTNode<K extends Comparable, V>
{
    public final static int MIN_DEGREE          =   3;
    public final static int LOWER_BOUND_KEYNUM  =   MIN_DEGREE - 1;
    public final static int UPPER_BOUND_KEYNUM  =   (MIN_DEGREE * 2) - 1;

    protected boolean mIsLeaf;
    protected int mCurrentKeyNum;
    protected BTKeyValue<K, V> mKeys[];
    protected BTNode mChildren[];


    public BTNode() {
        mIsLeaf = true;
        mCurrentKeyNum = 0;
        mKeys = new BTKeyValue[UPPER_BOUND_KEYNUM];
        mChildren = new BTNode[UPPER_BOUND_KEYNUM + 1];
    }
    public String getName(){
        return "Nodo_"+this.hashCode();
    }
    public String dot(){
        String Dot ="";
        Dot+=getName()+"[label = \"<P0>";
        for(int i = 0; i<mCurrentKeyNum;i++){
            Dot+="|"+mKeys[i].mKey+"|<P"+(i+1)+">";
        }
        Dot+="\"];\n";
        for(int i = 0; i<=mCurrentKeyNum;i++){
            if(mChildren[i]!=null){
                Dot+=mChildren[i].dot();
                Dot+=getName()+":P"+i+" ->"+mChildren[i].getName()+";\n";
            }
        }
        return Dot;
    }
    protected static BTNode getChildNodeAtIndex(BTNode btNode, int keyIdx, int nDirection) {
        if (btNode.mIsLeaf) {
            return null;
        }

        keyIdx += nDirection;
        if ((keyIdx < 0) || (keyIdx > btNode.mCurrentKeyNum)) {
            return null;
        }

        return btNode.mChildren[keyIdx];
    }


    protected static BTNode getLeftChildAtIndex(BTNode btNode, int keyIdx) {
        return getChildNodeAtIndex(btNode, keyIdx, 0);
    }


    protected static BTNode getRightChildAtIndex(BTNode btNode, int keyIdx) {
        return getChildNodeAtIndex(btNode, keyIdx, 1);
    }


    protected static BTNode getLeftSiblingAtIndex(BTNode parentNode, int keyIdx) {
        return getChildNodeAtIndex(parentNode, keyIdx, -1);
    }


    protected static BTNode getRightSiblingAtIndex(BTNode parentNode, int keyIdx) {
        return getChildNodeAtIndex(parentNode, keyIdx, 1);
    }
}
