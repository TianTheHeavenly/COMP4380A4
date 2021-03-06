package bplustree;


public class BPlusTree<K extends Comparable<K>, V> {
	private BPlusTreeNode<K> root;

	public BPlusTree(int degree) {
		root = new BPlusTreeLeafNode<>(degree);
	}

	public BPlusTreeNode<K> getRoot() {
		return root;
	}

	public void insert(K key, V value) {
		BPlusTreeLeafNode<K, V> leaf = findLeafNode(key);
		leaf.insert(key, value);

		if (leaf.isOverflow()) {
			BPlusTreeNode<K> node = leaf.dealOverflow();
			if (node != null) {
				this.root = node;
			}
		}
	}

	private BPlusTreeLeafNode<K, V> findLeafNode(K key) {
		BPlusTreeNode<K> node = root;
		while (BPlusTreeInnerNode.class == node.getClass()) {
			node = ((BPlusTreeInnerNode<K>) node).getChildren().get(
					node.search(key));
		}
		return (BPlusTreeLeafNode<K, V>) node;
	}

	public V search(K key) {
		BPlusTreeLeafNode<K, V> leaf = this.findLeafNode(key);
		int index = leaf.search(key);
		
		return index >= 0 ? leaf.values.get(index): null;
	}

	public void delete(K key) {
		BPlusTreeLeafNode<K, V> leaf = this.findLeafNode(key);
		
		if (leaf.delete(key) && leaf.isUnderflow()) {
			BPlusTreeNode<K> n = leaf.dealUnderflow();
			if (n != null)
				this.root = n; 
		}
		
	}
	
	@Override
	public String toString() {
		StringBuilder treeString = new StringBuilder("Root ");
		
		BPlusTreeNode<K> node = root;
		while (node != null) {
			treeString.append(node.toString());
			BPlusTreeNode<K> sibling = node.rSibling;
			while(sibling != null) {
				treeString.append(sibling.toString());
				sibling = (BPlusTreeNode<K>) sibling.rSibling;
			}
			treeString.append("\n");
			
			if (BPlusTreeInnerNode.class == node.getClass()) {
				node = ((BPlusTreeInnerNode<K>) node).children.get(0);
			} else {
				break;
			}
		}
		return treeString.toString();
		
	}

}