package core;

/*
 * A standard generic Pair<X,Y>, with getters, hashCode, equals, and toString well implemented. 
 */

import java.io.Serializable;

public class Pair<X,Y> implements Serializable {

	private static final long serialVersionUID = -550872493742593086L;
	private final X x;
	private final Y y;
	
	public Pair(X x, Y y) {
		super();
		this.x = x;
		this.y = y;
	}

    public Pair(Pair<X, Y> selectedFieldCard) {
		this.x = selectedFieldCard.x;
		this.y = selectedFieldCard.y;
    }

    public X getX() {
		return this.x;
	}

	public Y getY() {
		return this.y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.x == null) ? 0 : this.x.hashCode());
		result = prime * result + ((this.y == null) ? 0 : this.y.hashCode());
		return result;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (this.x == null) {
			if (other.x != null)
				return false;
		} else if (!this.x.equals(other.x))
			return false;
		if (this.y == null) {
			if (other.y != null)
				return false;
		} else if (!this.y.equals(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "("+ this.x +"," + this.y +")";
	}
	
	

}
