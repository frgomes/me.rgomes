package me.rgomes.vertex.tree;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

	private String name;
	private T data;

    //
    // protected constructors
    //

    /**
     * Default constructor
     */
    protected Node() {
    }


    //
    // public methods
    //

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name.trim();
    }

    public T getData() {
        return this.data;
    }

    public void setData(final T data) {
        this.data = data;
    }


    //
    // overrides Object
    //

    /**
	 * This method returns the hash code of its contained data
	 *
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		return result;
	}

	/**
	 * This method returns equality of its contained data
	 *
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Node)) {
			return false;
		}
		final Node other = (Node) obj;
		if (data == null) {
			if (other.data != null) {
				return false;
			}
		} else if (!data.equals(other.data)) {
			return false;
		}
		return true;
	}

	@Override
    public String toString() {
		return this.name;
    }


	//
	// implements Comparable
	//

	@Override
	public int compareTo(final Node<T> arg0) {
		return this.name.compareTo(arg0.name);
	}

}
