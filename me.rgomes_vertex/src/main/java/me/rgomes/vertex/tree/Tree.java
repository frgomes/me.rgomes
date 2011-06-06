package me.rgomes.vertex.tree;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import me.rgomes.vertex.lang.Iterables;

/**
 * Represents a Tree of Objects of generic type T.
 * <p>
 * The Tree is represented as a single rootElement which points to a List<Node<T>> of children.
 * There is no restriction on the number of children that a particular node may have.
 * This Tree provides a method to serialize the Tree into a List by doing a
 * pre-order traversal.
 *
 * @author Richard Gomes<rgomes1997@yahoo.co.uk>
 */
//TODO: Employ Visitor pattern instead of method walk
public class Tree<T extends Comparable<T>> {

    private final NavigableNode<T> root;

    /**
     * Default constructor
     */
    public Tree() {
        this.root = new NavigableNode<T>();
    }

    /**
     * Return the root Node of the tree.
     *
     * @return the root element.
     */
    public NavigableNode<T> getRoot() {
        return this.root;
    }

//    /**
//     * Set the root Element for the tree.
//     *
//     * @param rootElement the root element to set.
//     */
//    public void setRootElement(final NavigableNode<T> root) {
//        root = root;
//    }

    /**
     * Returns the Tree<T> as a List of NavigableNode<T> objects. The elements of the
     * List are generated from a pre-order traversal of the tree.
     *
     * @return a List<NavigableNode<T>>.
     */
    public List<NavigableNode<T>> toList() {
        final List<NavigableNode<T>> list = new ArrayList<NavigableNode<T>>();
        walk(root, list);
        return list;
    }

    /**
     * Returns a String representation of the Tree. The elements are generated
     * from a pre-order traversal of the Tree.
     *
     * @return the String representation of the Tree.
     */
    @Override
    public String toString() {
        return toList().toString();
    }

    /**
     * Walks the Tree in pre-order style. This is a recursive method, and is
     * called from the toList() method with the root element as the first
     * argument. It appends to the second argument, which is passed by reference
     * as it recurses down the tree.
     *
     * @param element the starting element.
     * @param list the output of the walk.
     */
    private void walk(final NavigableNode<T> element, final List<NavigableNode<T>> list) {
        list.add(element);
        for (final NavigableNode<T> data : element.getChildren()) {
            walk(data, list);
        }
    }


    //
    // public inner classes
    //

    public static class NavigableNode<T extends Comparable<T>> extends Node<T> implements NavigableSet<NavigableNode<T>> {

        private Node<T> parent;
        private final NavigableSet<NavigableNode<T>> children;

        //
        // protected constructors
        //

        /**
         * Default constructor
         */
    	protected NavigableNode() {
    		super();
            this.children = new TreeSet<NavigableNode<T>>();
    	}

        /**
         * Convenience constructor to create a Node<T> with an instance of T
         * and pointing to its parent.
         *
         * @param data an instance of T.
         * @param parent is the parent of this Node.
         */
        protected NavigableNode(final String name, final T data, final Node<T> parent) {
        	this();
            setParent(parent);
            setName(name);
            setData(data);
        }


        //
        // protected methods
        //

        protected void setParent(final Node<T> parent) {
            this.parent = parent;
        }


        //
        // public methods
        //

        public String getPath() {
        	final String path = getEncodedPath();
        	return (path.length()==0) ? "/" : path;
        }

        private String getEncodedPath() {
        	if (parent==null) {
        		return "";
        	} else {
            	final StringBuilder sb = new StringBuilder();
            	sb.append(parent.getName()).append('/').append(encode(getName()));
            	return sb.toString();
        	}
        }

        private String encode(final String source) {
            try {
                return URLEncoder.encode(source, "UTF-8");
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * @return the parent node of this node
         */
        public Node<T> getParent() {
            return this.parent;
        }

        /**
         * Return the children of Node<T>.
         * <p>
         * The Tree<T> is represented by a single root Node<T> whose children are represented by a List<Node<T>>.
         * Each of these Node<T> elements in the List can have children. The getChildren() method will return the children of a Node<T>.
         * <p>
         * This class guarantees that this method never returns null.
         *
         * @return children of this node
         */
        public NavigableSet<NavigableNode<T>> getChildren() {
            return this.children;
        }

       /**
        * Disconnect <code>this</code> node from its parent, also making sure that
        * children nodes become orphans.
        * <p>
        * Once this operation on <code>this</code> node potentially makes the entire
        * branch unreachable, this operation on only <code>this</code> node is potentially
        * enough for making the entire branch eligible for being garbage collected.
        */
    	public void disconnect() {
    		// disconnect from parent
    		final Node<T> p = this.parent;
    		if (p==null) throw new RuntimeException("the root node cannot be disconnected");
    		// disconnect from parent
    		this.children.remove(this);
    		// disconnect all children
    		for (final NavigableNode<T> q : Iterables.unmodifiableIterable(this.children.iterator())) {
    			q.parent = null;
    		}
    		this.getChildren().clear();
    	}


    	//
        // overrides Object
        //

    	@Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{").append(super.toString()).append(",[");
            int i = 0;
            for (final Node<T> e : getChildren()) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append(e.toString());
                i++;
            }
            sb.append("]").append("}");
            return sb.toString();
        }


        /**
    	 * This method returns the hash code of its contained data
    	 *
    	 * @see java.lang.Object#hashCode()
    	 */
    	@Override
    	public int hashCode() {
    		return super.hashCode();
    	}

    	/**
    	 * This method returns equality of its contained data
    	 *
    	 * @see java.lang.Object#equals(java.lang.Object)
    	 */
    	@Override
    	public boolean equals(final Object obj) {
    		return super.equals(obj);
    	}


    	//
    	// implements NavigableSet<Node<T> via delegate pattern
    	//

    	@Override
    	public boolean add(final NavigableNode<T> arg0) {
    		final boolean node = children.add(arg0);
    		arg0.parent = this;
    		return node;
    	}

    	@Override
    	public boolean addAll(final Collection<? extends NavigableNode<T>> arg0) {
    		for (final NavigableNode<T> node : arg0) {
    			if (!children.add(node)) return false;
    		}
    		return true;
    	}

    	@Override
    	public NavigableNode<T> ceiling(final NavigableNode<T> arg0) {
    		return children.ceiling(arg0);
    	}

    	@Override
    	public void clear() {
    		final NavigableSet<NavigableNode<T>> nodes = children;
    		for (final NavigableNode<T> node : nodes) node.parent = null;
    		children.clear();
    	}

    	@Override
    	public Comparator<? super NavigableNode<T>> comparator() {
    		return children.comparator();
    	}

    	@Override
    	public boolean contains(final Object arg0) {
    		return children.contains(arg0);
    	}

    	@Override
    	public boolean containsAll(final Collection<?> arg0) {
    		return children.containsAll(arg0);
    	}

    	@Override
    	public Iterator<NavigableNode<T>> descendingIterator() {
    		return children.descendingIterator();
    	}

    	@Override
    	public NavigableSet<NavigableNode<T>> descendingSet() {
    		return children.descendingSet();
    	}

    	@Override
    	public NavigableNode<T> first() {
    		return children.first();
    	}

    	@Override
    	public NavigableNode<T> floor(final NavigableNode<T> arg0) {
    		return children.floor(arg0);
    	}

    	@Override
    	public NavigableSet<NavigableNode<T>> headSet(final NavigableNode<T> arg0, final boolean arg1) {
    		return children.headSet(arg0, arg1);
    	}

    	@Override
    	public SortedSet<NavigableNode<T>> headSet(final NavigableNode<T> arg0) {
    		return children.headSet(arg0);
    	}

    	@Override
    	public NavigableNode<T> higher(final NavigableNode<T> arg0) {
    		return children.higher(arg0);
    	}

    	@Override
    	public boolean isEmpty() {
    		return children.isEmpty();
    	}

    	@Override
    	public Iterator<NavigableNode<T>> iterator() {
    		return children.iterator();
    	}

    	@Override
    	public NavigableNode<T> last() {
    		return children.last();
    	}

    	@Override
    	public NavigableNode<T> lower(final NavigableNode<T> arg0) {
    		return children.lower(arg0);
    	}

    	@Override
    	public NavigableNode<T> pollFirst() {
    		final NavigableNode<T> node = children.pollFirst();
    		if (node!=null) node.parent = null;
    		return node;
    	}

    	@Override
    	public NavigableNode<T> pollLast() {
    		final NavigableNode<T> node = children.pollLast();
    		if (node!=null) node.parent = null;
    		return node;
    	}

    	@SuppressWarnings("unchecked")
    	@Override
    	public boolean remove(final Object arg0) {
    		final SortedSet<NavigableNode<T>> subSet = children.tailSet((NavigableNode<T>) arg0);
    		if (subSet.size()==0) return false;
    		final NavigableNode<T> node = subSet.first();
    		node.parent = null;
    		return children.remove(node);
    	}

    	@Override
    	public boolean removeAll(final Collection<?> arg0) {
    		for (final Object o : arg0) {
    			if (!remove(o)) return false;
    		}
    		return true;
    	}

    	@Override
    	public boolean retainAll(final Collection<?> arg0) {
    		final Set<NavigableNode<T>> complement = new TreeSet<NavigableNode<T>>();
    		for (final NavigableNode<T> child : children) {
    			if (!arg0.contains(child)) complement.add(child);
    		}
    		return removeAll(complement);
    	}

    	@Override
    	public int size() {
    		return children.size();
    	}

    	@Override
    	public NavigableSet<NavigableNode<T>> subSet(final NavigableNode<T> arg0, final boolean arg1,
    			final NavigableNode<T> arg2, final boolean arg3) {
    		return children.subSet(arg0, arg1, arg2, arg3);
    	}

    	@Override
    	public SortedSet<NavigableNode<T>> subSet(final NavigableNode<T> arg0, final NavigableNode<T> arg1) {
    		return children.subSet(arg0, arg1);
    	}

    	@Override
    	public NavigableSet<NavigableNode<T>> tailSet(final NavigableNode<T> arg0, final boolean arg1) {
    		return children.tailSet(arg0, arg1);
    	}

    	@Override
    	public SortedSet<NavigableNode<T>> tailSet(final NavigableNode<T> arg0) {
    		return children.tailSet(arg0);
    	}

    	@Override
    	public Object[] toArray() {
    		return children.toArray();
    	}

    	@SuppressWarnings("hiding")
    	@Override
    	public <T> T[] toArray(final T[] arg0) {
    		return children.toArray(arg0);
    	}
    }

}
