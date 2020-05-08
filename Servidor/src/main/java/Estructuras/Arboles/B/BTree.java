package Estructuras.Arboles.B;

import Estructuras.Listas.SimplementeEnlazada.SimpleMenteEnlazada;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Stack;

public class BTree<K extends Comparable, V> {

    public final static int REBALANCE_FOR_LEAF_NODE = 1;
    public final static int REBALANCE_FOR_INTERNAL_NODE = 2;
    private String category = "";
    private String carpeta = "";

    public void setCarpeta(String carpeta) {
        this.carpeta = carpeta;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private BTNode<K, V> mRoot = null;
    private long mSize = 0L;
    private BTNode<K, V> mIntermediateInternalNode = null;
    private int mNodeIdx = 0;
    private final Stack<StackInfo> mStackTracer = new Stack<StackInfo>();

    public BTNode<K, V> getRootNode() {
        return mRoot;
    }

    public long size() {
        return mSize;
    }

    public void clear() {
        mSize = 0L;
        mRoot = null;
    }

    private BTNode<K, V> createNode() {
        BTNode<K, V> btNode;
        btNode = new BTNode();
        btNode.mIsLeaf = true;
        btNode.mCurrentKeyNum = 0;
        return btNode;
    }

    public V search(K key) {
        BTNode<K, V> currentNode = mRoot;
        BTKeyValue<K, V> currentKey;
        int i, numberOfKeys;

        while (currentNode != null) {
            numberOfKeys = currentNode.mCurrentKeyNum;
            i = 0;
            currentKey = currentNode.mKeys[i];
            while ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) > 0)) {
                ++i;
                if (i < numberOfKeys) {
                    currentKey = currentNode.mKeys[i];
                } else {
                    --i;
                    break;
                }
            }

            if ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) == 0)) {
                return currentKey.mValue;
            }
            if (key.compareTo(currentKey.mKey) > 0) {
                currentNode = BTNode.getRightChildAtIndex(currentNode, i);
            } else {
                currentNode = BTNode.getLeftChildAtIndex(currentNode, i);
            }
        }

        return null;
    }

    public BTree insert(K key, V value) {
        if (mRoot == null) {
            mRoot = createNode();
        }

        ++mSize;
        if (mRoot.mCurrentKeyNum == BTNode.UPPER_BOUND_KEYNUM) {
            // The root is full, split it
            BTNode<K, V> btNode = createNode();
            btNode.mIsLeaf = false;
            btNode.mChildren[0] = mRoot;
            mRoot = btNode;
            splitNode(mRoot, 0, btNode.mChildren[0]);
        }

        insertKeyAtNode(mRoot, key, value);
        return this;
    }

    private void insertKeyAtNode(BTNode rootNode, K key, V value) {
        int i;
        int currentKeyNum = rootNode.mCurrentKeyNum;

        if (rootNode.mIsLeaf) {
            if (rootNode.mCurrentKeyNum == 0) {
                // Empty root
                rootNode.mKeys[0] = new BTKeyValue<K, V>(key, value);
                ++(rootNode.mCurrentKeyNum);
                return;
            }
            for (i = 0; i < rootNode.mCurrentKeyNum; ++i) {
                if (key.compareTo(rootNode.mKeys[i].mKey) == 0) {
                    rootNode.mKeys[i].mValue = value;
                    --mSize;
                    return;
                }
            }
            i = currentKeyNum - 1;
            BTKeyValue<K, V> existingKeyVal = rootNode.mKeys[i];
            while ((i > -1) && (key.compareTo(existingKeyVal.mKey) < 0)) {
                rootNode.mKeys[i + 1] = existingKeyVal;
                --i;
                if (i > -1) {
                    existingKeyVal = rootNode.mKeys[i];
                }
            }

            i = i + 1;
            rootNode.mKeys[i] = new BTKeyValue<K, V>(key, value);

            ++(rootNode.mCurrentKeyNum);
            return;
        }
        i = 0;
        int numberOfKeys = rootNode.mCurrentKeyNum;
        BTKeyValue<K, V> currentKey = rootNode.mKeys[i];
        while ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) > 0)) {
            ++i;
            if (i < numberOfKeys) {
                currentKey = rootNode.mKeys[i];
            } else {
                --i;
                break;
            }
        }

        if ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) == 0)) {
            currentKey.mValue = value;
            --mSize;
            return;
        }

        BTNode<K, V> btNode;
        if (key.compareTo(currentKey.mKey) > 0) {
            btNode = BTNode.getRightChildAtIndex(rootNode, i);
            i = i + 1;
        } else {
            if ((i - 1 >= 0) && (key.compareTo(rootNode.mKeys[i - 1].mKey) > 0)) {
                btNode = BTNode.getRightChildAtIndex(rootNode, i - 1);
            } else {
                btNode = BTNode.getLeftChildAtIndex(rootNode, i);
            }
        }

        if (btNode.mCurrentKeyNum == BTNode.UPPER_BOUND_KEYNUM) {
            splitNode(rootNode, i, btNode);
            insertKeyAtNode(rootNode, key, value);
            return;
        }

        insertKeyAtNode(btNode, key, value);
    }

    private void splitNode(BTNode parentNode, int nodeIdx, BTNode btNode) {
        int i;

        BTNode<K, V> newNode = createNode();

        newNode.mIsLeaf = btNode.mIsLeaf;
        newNode.mCurrentKeyNum = BTNode.LOWER_BOUND_KEYNUM;

        for (i = 0; i < BTNode.LOWER_BOUND_KEYNUM; ++i) {
            newNode.mKeys[i] = btNode.mKeys[i + BTNode.MIN_DEGREE];
            btNode.mKeys[i + BTNode.MIN_DEGREE] = null;
        }

        if (!btNode.mIsLeaf) {
            for (i = 0; i < BTNode.MIN_DEGREE; ++i) {
                newNode.mChildren[i] = btNode.mChildren[i + BTNode.MIN_DEGREE];
                btNode.mChildren[i + BTNode.MIN_DEGREE] = null;
            }
        }

        btNode.mCurrentKeyNum = BTNode.LOWER_BOUND_KEYNUM;

        for (i = parentNode.mCurrentKeyNum; i > nodeIdx; --i) {
            parentNode.mChildren[i + 1] = parentNode.mChildren[i];
            parentNode.mChildren[i] = null;
        }
        parentNode.mChildren[nodeIdx + 1] = newNode;

        for (i = parentNode.mCurrentKeyNum - 1; i >= nodeIdx; --i) {
            parentNode.mKeys[i + 1] = parentNode.mKeys[i];
            parentNode.mKeys[i] = null;
        }
        parentNode.mKeys[nodeIdx] = btNode.mKeys[BTNode.LOWER_BOUND_KEYNUM];
        btNode.mKeys[BTNode.LOWER_BOUND_KEYNUM] = null;
        ++(parentNode.mCurrentKeyNum);
    }

    private BTNode<K, V> findPredecessor(BTNode<K, V> btNode, int nodeIdx) {
        if (btNode.mIsLeaf) {
            return btNode;
        }

        BTNode<K, V> predecessorNode;
        if (nodeIdx > -1) {
            predecessorNode = BTNode.getLeftChildAtIndex(btNode, nodeIdx);
            if (predecessorNode != null) {
                mIntermediateInternalNode = btNode;
                mNodeIdx = nodeIdx;
                btNode = findPredecessor(predecessorNode, -1);
            }

            return btNode;
        }

        predecessorNode = BTNode.getRightChildAtIndex(btNode, btNode.mCurrentKeyNum - 1);
        if (predecessorNode != null) {
            mIntermediateInternalNode = btNode;
            mNodeIdx = btNode.mCurrentKeyNum;
            btNode = findPredecessorForNode(predecessorNode, -1);
        }

        return btNode;
    }

    private BTNode<K, V> findPredecessorForNode(BTNode<K, V> btNode, int keyIdx) {
        BTNode<K, V> predecessorNode;
        BTNode<K, V> originalNode = btNode;
        if (keyIdx > -1) {
            predecessorNode = BTNode.getLeftChildAtIndex(btNode, keyIdx);
            if (predecessorNode != null) {
                btNode = findPredecessorForNode(predecessorNode, -1);
                rebalanceTreeAtNode(originalNode, predecessorNode, keyIdx, REBALANCE_FOR_LEAF_NODE);
            }

            return btNode;
        }

        predecessorNode = BTNode.getRightChildAtIndex(btNode, btNode.mCurrentKeyNum - 1);
        if (predecessorNode != null) {
            btNode = findPredecessorForNode(predecessorNode, -1);
            rebalanceTreeAtNode(originalNode, predecessorNode, keyIdx, REBALANCE_FOR_LEAF_NODE);
        }

        return btNode;
    }

    private void performLeftRotation(BTNode<K, V> btNode, int nodeIdx, BTNode<K, V> parentNode, BTNode<K, V> rightSiblingNode) {
        int parentKeyIdx = nodeIdx;

        btNode.mKeys[btNode.mCurrentKeyNum] = parentNode.mKeys[parentKeyIdx];
        btNode.mChildren[btNode.mCurrentKeyNum + 1] = rightSiblingNode.mChildren[0];
        ++(btNode.mCurrentKeyNum);

        parentNode.mKeys[parentKeyIdx] = rightSiblingNode.mKeys[0];
        --(rightSiblingNode.mCurrentKeyNum);

        for (int i = 0; i < rightSiblingNode.mCurrentKeyNum; ++i) {
            rightSiblingNode.mKeys[i] = rightSiblingNode.mKeys[i + 1];
            rightSiblingNode.mChildren[i] = rightSiblingNode.mChildren[i + 1];
        }
        rightSiblingNode.mChildren[rightSiblingNode.mCurrentKeyNum] = rightSiblingNode.mChildren[rightSiblingNode.mCurrentKeyNum + 1];
        rightSiblingNode.mChildren[rightSiblingNode.mCurrentKeyNum + 1] = null;
    }

    private void performRightRotation(BTNode<K, V> btNode, int nodeIdx, BTNode<K, V> parentNode, BTNode<K, V> leftSiblingNode) {
        int parentKeyIdx = nodeIdx;
        if (nodeIdx >= parentNode.mCurrentKeyNum) {
            parentKeyIdx = nodeIdx - 1;
        }

        btNode.mChildren[btNode.mCurrentKeyNum + 1] = btNode.mChildren[btNode.mCurrentKeyNum];
        for (int i = btNode.mCurrentKeyNum - 1; i >= 0; --i) {
            btNode.mKeys[i + 1] = btNode.mKeys[i];
            btNode.mChildren[i + 1] = btNode.mChildren[i];
        }

        btNode.mKeys[0] = parentNode.mKeys[parentKeyIdx];
        btNode.mChildren[0] = leftSiblingNode.mChildren[leftSiblingNode.mCurrentKeyNum];
        ++(btNode.mCurrentKeyNum);

        parentNode.mKeys[parentKeyIdx] = leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum - 1];
        leftSiblingNode.mChildren[leftSiblingNode.mCurrentKeyNum] = null;
        --(leftSiblingNode.mCurrentKeyNum);
    }

    private boolean performMergeWithLeftSibling(BTNode<K, V> btNode, int nodeIdx, BTNode<K, V> parentNode, BTNode<K, V> leftSiblingNode) {
        if (nodeIdx == parentNode.mCurrentKeyNum) {
            nodeIdx = nodeIdx - 1;
        }

        if (nodeIdx > 0) {
            if (leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum - 1].mKey.compareTo(parentNode.mKeys[nodeIdx - 1].mKey) < 0) {
                nodeIdx = nodeIdx - 1;
            }
        }

        leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum] = parentNode.mKeys[nodeIdx];
        ++(leftSiblingNode.mCurrentKeyNum);

        for (int i = 0; i < btNode.mCurrentKeyNum; ++i) {
            leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum + i] = btNode.mKeys[i];
            leftSiblingNode.mChildren[leftSiblingNode.mCurrentKeyNum + i] = btNode.mChildren[i];
            btNode.mKeys[i] = null;
        }
        leftSiblingNode.mCurrentKeyNum += btNode.mCurrentKeyNum;
        leftSiblingNode.mChildren[leftSiblingNode.mCurrentKeyNum] = btNode.mChildren[btNode.mCurrentKeyNum];
        btNode.mCurrentKeyNum = 0;  // Abandon the node

        int i;
        for (i = nodeIdx; i < parentNode.mCurrentKeyNum - 1; ++i) {
            parentNode.mKeys[i] = parentNode.mKeys[i + 1];
            parentNode.mChildren[i + 1] = parentNode.mChildren[i + 2];
        }
        parentNode.mKeys[i] = null;
        parentNode.mChildren[parentNode.mCurrentKeyNum] = null;
        --(parentNode.mCurrentKeyNum);

        parentNode.mChildren[nodeIdx] = leftSiblingNode;

        if ((parentNode == mRoot) && (parentNode.mCurrentKeyNum == 0)) {
            mRoot = leftSiblingNode;
            return false;
        }

        return true;
    }

    private boolean performMergeWithRightSibling(BTNode<K, V> btNode, int nodeIdx, BTNode<K, V> parentNode, BTNode<K, V> rightSiblingNode) {
        btNode.mKeys[btNode.mCurrentKeyNum] = parentNode.mKeys[nodeIdx];
        ++(btNode.mCurrentKeyNum);

        for (int i = 0; i < rightSiblingNode.mCurrentKeyNum; ++i) {
            btNode.mKeys[btNode.mCurrentKeyNum + i] = rightSiblingNode.mKeys[i];
            btNode.mChildren[btNode.mCurrentKeyNum + i] = rightSiblingNode.mChildren[i];
        }
        btNode.mCurrentKeyNum += rightSiblingNode.mCurrentKeyNum;
        btNode.mChildren[btNode.mCurrentKeyNum] = rightSiblingNode.mChildren[rightSiblingNode.mCurrentKeyNum];
        rightSiblingNode.mCurrentKeyNum = 0;  // Abandon the sibling node

        int i;
        for (i = nodeIdx; i < parentNode.mCurrentKeyNum - 1; ++i) {
            parentNode.mKeys[i] = parentNode.mKeys[i + 1];
            parentNode.mChildren[i + 1] = parentNode.mChildren[i + 2];
        }
        parentNode.mKeys[i] = null;
        parentNode.mChildren[parentNode.mCurrentKeyNum] = null;
        --(parentNode.mCurrentKeyNum);

        parentNode.mChildren[nodeIdx] = btNode;

        if ((parentNode == mRoot) && (parentNode.mCurrentKeyNum == 0)) {
            mRoot = btNode;
            return false;
        }

        return true;
    }

    private int searchKey(BTNode<K, V> btNode, K key) {
        for (int i = 0; i < btNode.mCurrentKeyNum; ++i) {
            if (key.compareTo(btNode.mKeys[i].mKey) == 0) {
                return i;
            } else if (key.compareTo(btNode.mKeys[i].mKey) < 0) {
                return -1;
            }
        }

        return -1;
    }

    public void list(BTIteratorIF<K, V> iterImpl) {
        if (mSize < 1) {
            return;
        }

        if (iterImpl == null) {
            return;
        }

        listEntriesInOrder(mRoot, iterImpl);
    }

    private boolean listEntriesInOrder(BTNode<K, V> treeNode, BTIteratorIF<K, V> iterImpl) {
        if ((treeNode == null)
                || (treeNode.mCurrentKeyNum == 0)) {
            return false;
        }

        boolean bStatus;
        BTKeyValue<K, V> keyVal;
        int currentKeyNum = treeNode.mCurrentKeyNum;
        for (int i = 0; i < currentKeyNum; ++i) {
            listEntriesInOrder(BTNode.getLeftChildAtIndex(treeNode, i), iterImpl);

            keyVal = treeNode.mKeys[i];
            bStatus = iterImpl.item(keyVal.mKey, keyVal.mValue);
            if (!bStatus) {
                return false;
            }

            if (i == currentKeyNum - 1) {
                listEntriesInOrder(BTNode.getRightChildAtIndex(treeNode, i), iterImpl);
            }
        }

        return true;
    }

    public V delete(K key) {
        mIntermediateInternalNode = null;
        BTKeyValue<K, V> keyVal = deleteKey(null, mRoot, key, 0);
        if (keyVal == null) {
            return null;
        }
        --mSize;
        return keyVal.mValue;
    }

    private BTKeyValue<K, V> deleteKey(BTNode<K, V> parentNode, BTNode<K, V> btNode, K key, int nodeIdx) {
        int i;
        int nIdx;
        BTKeyValue<K, V> retVal;

        if (btNode == null) {
            return null;
        }

        if (btNode.mIsLeaf) {
            nIdx = searchKey(btNode, key);
            if (nIdx < 0) {
                return null;
            }

            retVal = btNode.mKeys[nIdx];

            if ((btNode.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM) || (parentNode == null)) {
                for (i = nIdx; i < btNode.mCurrentKeyNum - 1; ++i) {
                    btNode.mKeys[i] = btNode.mKeys[i + 1];
                }
                btNode.mKeys[i] = null;
                --(btNode.mCurrentKeyNum);

                if (btNode.mCurrentKeyNum == 0) {
                    mRoot = null;
                }

                return retVal;
            }

            BTNode<K, V> rightSibling;
            BTNode<K, V> leftSibling = BTNode.getLeftSiblingAtIndex(parentNode, nodeIdx);
            if ((leftSibling != null) && (leftSibling.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM)) {
                moveLeftLeafSiblingKeyWithKeyRemoval(btNode, nodeIdx, nIdx, parentNode, leftSibling);
            } else {
                rightSibling = BTNode.getRightSiblingAtIndex(parentNode, nodeIdx);
                if ((rightSibling != null) && (rightSibling.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM)) {
                    moveRightLeafSiblingKeyWithKeyRemoval(btNode, nodeIdx, nIdx, parentNode, rightSibling);
                } else {
                    boolean isRebalanceNeeded = false;
                    boolean bStatus;
                    if (leftSibling != null) {
                        bStatus = doLeafSiblingMergeWithKeyRemoval(btNode, nodeIdx, nIdx, parentNode, leftSibling, false);
                        if (!bStatus) {
                            isRebalanceNeeded = false;
                        } else if (parentNode.mCurrentKeyNum < BTNode.LOWER_BOUND_KEYNUM) {
                            isRebalanceNeeded = true;
                        }
                    } else {
                        bStatus = doLeafSiblingMergeWithKeyRemoval(btNode, nodeIdx, nIdx, parentNode, rightSibling, true);
                        if (!bStatus) {
                            isRebalanceNeeded = false;
                        } else if (parentNode.mCurrentKeyNum < BTNode.LOWER_BOUND_KEYNUM) {
                            isRebalanceNeeded = true;
                        }
                    }

                    if (isRebalanceNeeded && (mRoot != null)) {
                        rebalanceTree(mRoot, parentNode, parentNode.mKeys[0].mKey);
                    }
                }
            }

            return retVal;
        }

        nIdx = searchKey(btNode, key);
        if (nIdx >= 0) {
            mIntermediateInternalNode = btNode;
            mNodeIdx = nIdx;
            BTNode<K, V> predecessorNode = findPredecessor(btNode, nIdx);
            BTKeyValue<K, V> predecessorKey = predecessorNode.mKeys[predecessorNode.mCurrentKeyNum - 1];

            BTKeyValue<K, V> deletedKey = btNode.mKeys[nIdx];
            btNode.mKeys[nIdx] = predecessorKey;
            predecessorNode.mKeys[predecessorNode.mCurrentKeyNum - 1] = deletedKey;
            return deleteKey(mIntermediateInternalNode, predecessorNode, deletedKey.mKey, mNodeIdx);
        }

        i = 0;
        BTKeyValue<K, V> currentKey = btNode.mKeys[0];
        while ((i < btNode.mCurrentKeyNum) && (key.compareTo(currentKey.mKey) > 0)) {
            ++i;
            if (i < btNode.mCurrentKeyNum) {
                currentKey = btNode.mKeys[i];
            } else {
                --i;
                break;
            }
        }

        BTNode<K, V> childNode;
        if (key.compareTo(currentKey.mKey) > 0) {
            childNode = BTNode.getRightChildAtIndex(btNode, i);
            if (childNode.mKeys[0].mKey.compareTo(btNode.mKeys[btNode.mCurrentKeyNum - 1].mKey) > 0) {
                i = i + 1;
            }
        } else {
            childNode = BTNode.getLeftChildAtIndex(btNode, i);
        }

        return deleteKey(btNode, childNode, key, i);
    }

    private void moveRightLeafSiblingKeyWithKeyRemoval(BTNode<K, V> btNode,
            int nodeIdx,
            int keyIdx,
            BTNode<K, V> parentNode,
            BTNode<K, V> rightSiblingNode) {
        for (int i = keyIdx; i < btNode.mCurrentKeyNum - 1; ++i) {
            btNode.mKeys[i] = btNode.mKeys[i + 1];
        }

        btNode.mKeys[btNode.mCurrentKeyNum - 1] = parentNode.mKeys[nodeIdx];
        parentNode.mKeys[nodeIdx] = rightSiblingNode.mKeys[0];

        for (int i = 0; i < rightSiblingNode.mCurrentKeyNum - 1; ++i) {
            rightSiblingNode.mKeys[i] = rightSiblingNode.mKeys[i + 1];
        }

        --(rightSiblingNode.mCurrentKeyNum);
    }

    private void moveLeftLeafSiblingKeyWithKeyRemoval(BTNode<K, V> btNode,
            int nodeIdx,
            int keyIdx,
            BTNode<K, V> parentNode,
            BTNode<K, V> leftSiblingNode) {
        nodeIdx = nodeIdx - 1;

        for (int i = keyIdx; i > 0; --i) {
            btNode.mKeys[i] = btNode.mKeys[i - 1];
        }

        btNode.mKeys[0] = parentNode.mKeys[nodeIdx];
        parentNode.mKeys[nodeIdx] = leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum - 1];
        --(leftSiblingNode.mCurrentKeyNum);
    }

    private boolean doLeafSiblingMergeWithKeyRemoval(BTNode<K, V> btNode,
            int nodeIdx,
            int keyIdx,
            BTNode<K, V> parentNode,
            BTNode<K, V> siblingNode,
            boolean isRightSibling) {
        int i;

        if (nodeIdx == parentNode.mCurrentKeyNum) {
            nodeIdx = nodeIdx - 1;
        }

        if (isRightSibling) {
            for (i = keyIdx; i < btNode.mCurrentKeyNum - 1; ++i) {
                btNode.mKeys[i] = btNode.mKeys[i + 1];
            }
            btNode.mKeys[i] = parentNode.mKeys[nodeIdx];
        } else {
            if (nodeIdx > 0) {
                if (siblingNode.mKeys[siblingNode.mCurrentKeyNum - 1].mKey.compareTo(parentNode.mKeys[nodeIdx - 1].mKey) < 0) {
                    nodeIdx = nodeIdx - 1;
                }
            }

            siblingNode.mKeys[siblingNode.mCurrentKeyNum] = parentNode.mKeys[nodeIdx];
            ++(siblingNode.mCurrentKeyNum);
            for (i = keyIdx; i < btNode.mCurrentKeyNum - 1; ++i) {
                btNode.mKeys[i] = btNode.mKeys[i + 1];
            }
            btNode.mKeys[i] = null;
            --(btNode.mCurrentKeyNum);
        }

        if (isRightSibling) {
            for (i = 0; i < siblingNode.mCurrentKeyNum; ++i) {
                btNode.mKeys[btNode.mCurrentKeyNum + i] = siblingNode.mKeys[i];
                siblingNode.mKeys[i] = null;
            }
            btNode.mCurrentKeyNum += siblingNode.mCurrentKeyNum;
        } else {
            for (i = 0; i < btNode.mCurrentKeyNum; ++i) {
                siblingNode.mKeys[siblingNode.mCurrentKeyNum + i] = btNode.mKeys[i];
                btNode.mKeys[i] = null;
            }
            siblingNode.mCurrentKeyNum += btNode.mCurrentKeyNum;
            btNode.mKeys[btNode.mCurrentKeyNum] = null;
        }
        for (i = nodeIdx; i < parentNode.mCurrentKeyNum - 1; ++i) {
            parentNode.mKeys[i] = parentNode.mKeys[i + 1];
            parentNode.mChildren[i + 1] = parentNode.mChildren[i + 2];
        }
        parentNode.mKeys[i] = null;
        parentNode.mChildren[parentNode.mCurrentKeyNum] = null;
        --(parentNode.mCurrentKeyNum);

        if (isRightSibling) {
            parentNode.mChildren[nodeIdx] = btNode;
        } else {
            parentNode.mChildren[nodeIdx] = siblingNode;
        }

        if ((mRoot == parentNode) && (mRoot.mCurrentKeyNum == 0)) {
            // Only root left
            mRoot = parentNode.mChildren[nodeIdx];
            mRoot.mIsLeaf = true;
            return false;
        }

        return true;
    }

    private boolean rebalanceTreeAtNode(BTNode<K, V> parentNode, BTNode<K, V> btNode, int nodeIdx, int balanceType) {
        if (balanceType == REBALANCE_FOR_LEAF_NODE) {
            if ((btNode == null) || (btNode == mRoot)) {
                return false;
            }
        } else if (balanceType == REBALANCE_FOR_INTERNAL_NODE) {
            if (parentNode == null) {
                return false;
            }
        }

        if (btNode.mCurrentKeyNum >= BTNode.LOWER_BOUND_KEYNUM) {
            return false;
        }

        BTNode<K, V> rightSiblingNode;
        BTNode<K, V> leftSiblingNode = BTNode.getLeftSiblingAtIndex(parentNode, nodeIdx);
        if ((leftSiblingNode != null) && (leftSiblingNode.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM)) {
            performRightRotation(btNode, nodeIdx, parentNode, leftSiblingNode);
        } else {
            rightSiblingNode = BTNode.getRightSiblingAtIndex(parentNode, nodeIdx);
            if ((rightSiblingNode != null) && (rightSiblingNode.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM)) {
                performLeftRotation(btNode, nodeIdx, parentNode, rightSiblingNode);
            } else {
                boolean bStatus;
                if (leftSiblingNode != null) {
                    bStatus = performMergeWithLeftSibling(btNode, nodeIdx, parentNode, leftSiblingNode);
                } else {
                    bStatus = performMergeWithRightSibling(btNode, nodeIdx, parentNode, rightSiblingNode);
                }

                if (!bStatus) {
                    return false;
                }
            }
        }

        return true;
    }

    private void rebalanceTree(BTNode<K, V> upperNode, BTNode<K, V> lowerNode, K key) {
        mStackTracer.clear();
        mStackTracer.add(new StackInfo(null, upperNode, 0));

        BTNode<K, V> parentNode, childNode;
        BTKeyValue<K, V> currentKey;
        int i;
        parentNode = upperNode;
        while ((parentNode != lowerNode) && !parentNode.mIsLeaf) {
            currentKey = parentNode.mKeys[0];
            i = 0;
            while ((i < parentNode.mCurrentKeyNum) && (key.compareTo(currentKey.mKey) > 0)) {
                ++i;
                if (i < parentNode.mCurrentKeyNum) {
                    currentKey = parentNode.mKeys[i];
                } else {
                    --i;
                    break;
                }
            }

            if (key.compareTo(currentKey.mKey) > 0) {
                childNode = BTNode.getRightChildAtIndex(parentNode, i);
                if (childNode.mKeys[0].mKey.compareTo(parentNode.mKeys[parentNode.mCurrentKeyNum - 1].mKey) > 0) {
                    i = i + 1;
                }
            } else {
                childNode = BTNode.getLeftChildAtIndex(parentNode, i);
            }

            if (childNode == null) {
                break;
            }

            if (key.compareTo(currentKey.mKey) == 0) {
                break;
            }

            mStackTracer.add(new StackInfo(parentNode, childNode, i));
            parentNode = childNode;
        }

        boolean bStatus;
        StackInfo stackInfo;
        while (!mStackTracer.isEmpty()) {
            stackInfo = mStackTracer.pop();
            if ((stackInfo != null) && !stackInfo.mNode.mIsLeaf) {
                bStatus = rebalanceTreeAtNode(stackInfo.mParent,
                        stackInfo.mNode,
                        stackInfo.mNodeIdx,
                        REBALANCE_FOR_INTERNAL_NODE);
                if (!bStatus) {
                    break;
                }
            }
        }
    }

    public class StackInfo {

        public BTNode<K, V> mParent = null;
        public BTNode<K, V> mNode = null;
        public int mNodeIdx = -1;

        public StackInfo(BTNode<K, V> parent, BTNode<K, V> node, int nodeIdx) {
            mParent = parent;
            mNode = node;
            mNodeIdx = nodeIdx;
        }
    }

    public void libros(SimpleMenteEnlazada arg) {
        mRoot.general(arg);
    }

    public void bibliotecaUsuario(SimpleMenteEnlazada arg, int Carnet) {
        mRoot.porUsuario(arg, Carnet);
    }

    public void dot() {
        if (mRoot != null) {
            String Dot = "digraph G{\nrankdir=TB\nnode[shape = record, style = filled, fillcolor = skyblue];\n";
            Dot += "label =  <<font point-size='20'>Arbol B: " + this.category + "</font>>;\nlabelloc = \"t \";\n";
            Dot += mRoot.dot();
            Dot += "}";
            FileWriter fichero = null;
            PrintWriter escritor;
            try {
                fichero = new FileWriter(carpeta + "/ArbolB_" + this.category + ".dot");
                escritor = new PrintWriter(fichero);
                escritor.print(Dot);
            } catch (Exception e) {
                System.err.println("Error al escribir el archivo ArbolB_" + this.category + ".dot");
            } finally {
                try {
                    if (null != fichero) {
                        fichero.close();
                    }
                } catch (Exception e2) {
                    System.err.println("Error al cerrar el archivo ArbolB_" + this.category + ".dot");
                }
            }
            try {
                Runtime rt = Runtime.getRuntime();
                rt.exec("dot -Tjpg -o " + carpeta + "/ArbolB_" + this.category + ".jpg" + " " + carpeta + "/ArbolB_" + this.category + ".dot");
                Thread.sleep(500);
            } catch (Exception ex) {
                System.err.println("Error al generar la imagen para el archivo ArbolB_" + this.category + ".dot");
            }
        }
    }
}
