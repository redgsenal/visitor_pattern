package com.designpattern.visitor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN
}

abstract class Tree {

    private int value;
    private Color color;
    private int depth;

    public Tree(int value, Color color, int depth) {
        this.value = value;
        this.color = color;
        this.depth = depth;
    }

    public int getValue() {
        return value;
    }

    public Color getColor() {
        return color;
    }

    public int getDepth() {
        return depth;
    }

    public abstract void accept(TreeVis visitor);
}

class TreeNode extends Tree {

    private ArrayList<Tree> children = new ArrayList<>();

    public TreeNode(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitNode(this);

        for (Tree child : children) {
            child.accept(visitor);
        }
    }

    public void addChild(Tree child) {
        children.add(child);
    }

    @Override
    public String toString() {
        String s = "node: { v: " + this.getValue() + ", c: " + this.getColor() + ", d: " + this.getDepth();
        if (!this.children.isEmpty()) {
            s = s + ", child: ";
            for (Tree node : this.children) {
                s = s + node.toString();
            }
        }
        s = s + "} ";
        return s;
    }
}

class TreeLeaf extends Tree {

    public TreeLeaf(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        visitor.visitLeaf(this);
    }
}

abstract class TreeVis {

    public abstract int getResult();

    public abstract void visitNode(TreeNode node);

    public abstract void visitLeaf(TreeLeaf leaf);

}

class TreeBasic extends Tree {

    public TreeBasic(int value, Color color, int depth) {
        super(value, color, depth);
    }

    public void accept(TreeVis visitor) {
        // not implemented here
    }

    @Override
    public String toString() {
        return " leaf: {v: " + this.getValue() + ", c: " + this.getColor() + ", d: " + this.getDepth() + "}";
    }
}

class RootTree extends Tree {

    private final List<Tree> children;

    public RootTree(int value, Color color, int depth) {
        super(value, color, depth);
        this.children = new ArrayList<>();
        children.add(this);
    }

    public void addChild(Tree child) {
        if (isRootTreeContainsChild(child)) {
            return;
        }
        children.add(child);
    }

    public List<Tree> getChildren() {
        return children;
    }

    public void addTreeNode(int treeValue, Color c, int depth) {
        TreeNode node = new TreeNode(treeValue, c, depth);
        if (isRootTreeContainsChild(node)) {
            children.add(node);
        }
    }

    public void addTreeLeaf(int treeValue, Color c, int depth) {
        TreeLeaf leaf = new TreeLeaf(treeValue, c, depth);
        if (isRootTreeContainsChild(leaf)) {
            children.add(leaf);
        }
    }

    private boolean isRootTreeContainsChild(Tree child) {
        if (child == null) {
            return false;
        }
        if (children != null && !children.isEmpty()) {
            for (Tree tree : children) {
                if (child.getValue() == tree.getValue() && child.getColor() == tree.getColor()
                        && child.getDepth() == tree.getDepth()) {
                    return true;
                }
            }
        }
        return false;
    }

    public TreeNode getChildNode(Tree child) {
        for (Tree tree : children) {
            if (child.getValue() == tree.getValue() && child.getColor() == tree.getColor()
                    && child.getDepth() == tree.getDepth()) {
                TreeNode node = new TreeNode(child.getValue(), child.getColor(), child.getDepth());
                children.remove(tree);
                children.add(node);
                return node;
            }
        }
        return null;
    }

    @Override
    public void accept(TreeVis visitor) {
        for (Tree tree : children) {
            if (tree instanceof TreeNode) {
                visitor.visitNode((TreeNode) tree);
            }

            if (tree instanceof TreeLeaf) {
                visitor.visitLeaf((TreeLeaf) tree);
            }
        }
    }

    @Override
    public String toString() {
        String s = "node: { v: " + this.getValue() + ", c: " + this.getColor() + ", d: " + this.getDepth();
        if (!this.children.isEmpty()) {
            s = s + ", child: ";
            for (Tree node : this.children) {
                s = s + node.toString();
            }
        }
        s = s + "} ";
        return s;
    }
}

class SumInLeavesVisitor extends TreeVis {

    private int result;
    private List<TreeNode> nodes = new ArrayList<>();
    private List<TreeLeaf> leaves = new ArrayList<>();

    public int getResult() {
        for (TreeLeaf leaf : leaves) {
            result = result + leaf.getValue();
        }
        return result;
    }

    public void visitNode(TreeNode node) {
        nodes.add(node);
    }

    public void visitLeaf(TreeLeaf leaf) {
        // add only the leaf
        leaves.add(leaf);
    }
}

class ProductOfRedNodesVisitor extends TreeVis {

    private int result = 1;
    private List<TreeNode> nodes = new ArrayList<>();
    private List<TreeLeaf> leaves = new ArrayList<>();

    public int getResult() {
        for (TreeNode node : nodes) {
            if (node.getColor() == Color.RED) {
                result = result * node.getValue();
            }
        }
        /*
		 * for (TreeLeaf leaf : leaves) { if (leaf.getColor() == Color.RED) { result =
		 * result * leaf.getValue(); } }
         */
        return result;
    }

    public void visitNode(TreeNode node) {
        nodes.add(node);
    }

    public void visitLeaf(TreeLeaf leaf) {
        leaves.add(leaf);
    }
}

class FancyVisitor extends TreeVis {

    public int getResult() {
        // implement this
        return 0;
    }

    public void visitNode(TreeNode node) {
        // implement this
    }

    public void visitLeaf(TreeLeaf leaf) {
        // implement this
    }
}

public class Solution {

    private static Color getColorValue(String n) {
        return "0".equalsIgnoreCase(n) ? Color.RED : Color.GREEN;
    }

    private static int getNodeValue(String v) {
        if (v == null || v.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(v.trim());
    }

    private static TreeNode createTreeNodeFromTreeListByIndexValue(List<Tree> treeLists, TreeNode treeRoot, int nodeIndex) {
        if (nodeIndex < 0) {
            return null;
        }
        if (nodeIndex == 0) {
            return treeRoot;
        }
        Tree tree = treeLists.get(nodeIndex);
        return new TreeNode(tree.getValue(), tree.getColor(), tree.getDepth());
    }

    private static TreeLeaf getLeafByIndex(List<Tree> treeList, int nodeIndex) {
        if (nodeIndex > 0) {
            for (Tree leaf : treeList) {
                if (leaf.getDepth() == nodeIndex) {
                    return new TreeLeaf(leaf.getValue(), leaf.getColor(), leaf.getDepth());
                }
            }
        }
        return null;
    }

    public static Tree solve() {
        // read the tree from STDIN and return its root as a return value of this
        // function

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        try {
            System.out.println("number of nodes: ");
            // int numberOfNodes = Integer.parseInt(br.readLine().trim());
            int numberOfNodes = 5;
            System.out.println("node values: ");
            System.out.println(numberOfNodes);
            // String inputNodeValues = scanner.next();
            // String[] nodeValues = inputNodeValues.split(" ");
            String[] nodeValues = {"4", "7", "2", "5", "12"};
            System.out.println("node colors: ");
            System.out.println(Arrays.toString(nodeValues));
            // String inputColorValues = scanner.next();
            // String[] colorValues = inputColorValues.split(" ");
            String[] colorValues = {"0", "1", "0", "0", "1"};
            System.out.println(Arrays.toString(colorValues));
            // initial top root tree node
            TreeNode rootTreeNode = new TreeNode(getNodeValue(nodeValues[0]), getColorValue(colorValues[0]), 0);
            // String inputColorValues = scanner.next();
            // String[] colorValues = inputColorValues.split(" ");
            // build the remaining nodes
            List<Tree> treeList = new ArrayList<>();
            // build treenodes with an initial 0 depth
            for (int nodeDepth = 0; nodeDepth < numberOfNodes; nodeDepth++) {
                // assign node depth 0 as root
                if (nodeDepth == 0) {
                    // add the root here
                    treeList.add(rootTreeNode);
                    continue;
                }
                treeList.add(new TreeBasic(getNodeValue(nodeValues[nodeDepth]), getColorValue(colorValues[nodeDepth]),
                        nodeDepth));
            }
            String[] edgeDepths = {"1 2", "1 3", "3 4", "3 5"};
            System.out.println(Arrays.toString(edgeDepths));
            // edges doesnt include the root
            // setup edge nodes
            for (String edge : edgeDepths) {
                String[] edges = edge.split(" ");
                if (edges.length == 2) {
                    String edgeParentIndex = edges[0].trim();
                    String edgeChildIndex = edges[1].trim(); // find the edge node fron the treenodes
                    int parentIndex = Integer.parseInt(edgeParentIndex) - 1;
                    int childIndex
                            = Integer.parseInt(edgeChildIndex) - 1;
                    TreeNode edgeNode
                            = createTreeNodeFromTreeListByIndexValue(treeList, rootTreeNode, parentIndex);
                    if (edgeNode
                            != null) {
                        TreeLeaf edgeLeafNode = getLeafByIndex(treeList, childIndex);
                        if (edgeLeafNode != null) {
                            edgeNode.addChild(edgeLeafNode);
                            if (parentIndex
                                    > 0) {
                                rootTreeNode.addChild(edgeNode);
                            }
                        }
                    }
                }
            }

            System.out.println(rootTreeNode.toString());
            return rootTreeNode;
        } catch (NumberFormatException e) {
            System.out.println("** error");
            e.printStackTrace();
        } finally {
            scanner.close();
        }
        return new RootTree(0, Color.RED, 0);
    }

    public static void main(String[] args) {
        Tree root = solve();
        SumInLeavesVisitor vis1 = new SumInLeavesVisitor();
        ProductOfRedNodesVisitor vis2 = new ProductOfRedNodesVisitor();
        FancyVisitor vis3 = new FancyVisitor();

        root.accept(vis1);
        root.accept(vis2);
        root.accept(vis3);

        int res1 = vis1.getResult();
        int res2 = vis2.getResult();
        int res3 = vis3.getResult();

        System.out.println(res1);
        System.out.println(res2);
        System.out.println(res3);
    }
}
