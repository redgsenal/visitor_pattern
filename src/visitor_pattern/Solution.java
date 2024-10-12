package visitor_pattern;

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

	@Override
	public String toString() {
		return " leaf: {v: " + this.getValue() + ", c: " + this.getColor() + ", d: " + this.getDepth() + "}";
	}
}

abstract class TreeVis {

	public abstract int getResult();

	public abstract void visitNode(TreeNode node);

	public abstract void visitLeaf(TreeLeaf leaf);

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
		for (TreeLeaf leaf : leaves) {
			if (leaf.getColor() == Color.RED) {
				result = result * leaf.getValue();
			}
		}
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

	private static TreeNode getNodeByIndex(List<TreeNode> treeNodes, String nodeSequence) {
		if (nodeSequence == null || nodeSequence.isEmpty()) {
			return null;
		}
		int v = Integer.parseInt(nodeSequence);
		if (v < 0) {
			return null;
		}
		int nodeIndex = v - 1;
		return treeNodes.get(nodeIndex);		
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
			String[] nodeValues = { "4", "7", "2", "5", "12" };
			System.out.println("node colors: ");
			System.out.println(Arrays.toString(nodeValues));
			// String inputColorValues = scanner.next();
			// String[] colorValues = inputColorValues.split(" ");
			String[] colorValues = { "0", "1", "0", "0", "1" };
			System.out.println(Arrays.toString(colorValues));
			// initial top tree node
			TreeNode treeNodeRoot = new TreeNode(getNodeValue(nodeValues[0]), getColorValue(colorValues[0]), 0);

			// String inputColorValues = scanner.next();
			// String[] colorValues = inputColorValues.split(" ");
			// build the remaining nodes
			List<TreeNode> treeNodes = new ArrayList<>();
			treeNodes.add(treeNodeRoot);
			// build treenodes with an initial 0 depth
			for (int nodeDepth = 1; nodeDepth < numberOfNodes; nodeDepth++) {
				treeNodes.add(
						new TreeNode(getNodeValue(nodeValues[nodeDepth]), getColorValue(colorValues[nodeDepth]), nodeDepth));
			}
			String[] edgeDepths = { "1 2", "1 3", "3 4", "3 5" };
			System.out.println(Arrays.toString(edgeDepths));
			// setup edge nodes
			for (String edge : edgeDepths) {
				String[] edges = edge.split(" ");
				if (edges.length == 2) {
					String edgeParentIndex = edges[0].trim();
					String edgeChildIndex = edges[1].trim();
					// find the edge node fron the treenodes
					TreeNode edgeNode = getNodeByIndex(treeNodes, edgeParentIndex);
					if (edgeNode != null) {
						TreeNode edgeLeafNode = getNodeByIndex(treeNodes, edgeChildIndex);
						if (edgeLeafNode != null) {
							edgeNode.addChild(edgeLeafNode);
							edgeNode.addChild(new TreeLeaf(edgeLeafNode.getValue(), edgeLeafNode.getColor(),
									edgeLeafNode.getDepth()));
						}
					}
				}
			}
			System.out.println(treeNodeRoot.toString());
			return treeNodeRoot;
		} catch (NumberFormatException e) {
			System.out.println("** error");
			e.printStackTrace();
		} finally {
			scanner.close();
		}
		return new TreeNode(1, Color.RED, 1);
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
