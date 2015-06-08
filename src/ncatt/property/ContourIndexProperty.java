package ncatt.property;

import java.awt.geom.PathIterator;

import net.nexttext.property.Property;

public class ContourIndexProperty extends Property {
	
	int index;
	int type;
	
	public ContourIndexProperty(int index, int type) {
		this.index = index;
		this.type = type;
	}
	
	public int getIndex() {
		return index;
	}
	
	public int getType() {
		return type;
	}
	
	public void set(int index, int type) {
		this.index = index;
		this.type = type;
		firePropertyChangeEvent();
	}
	
	public void setIndex(int index) {
		this.index = index;
		firePropertyChangeEvent();
	}
	
	public void setType(int type) {
		this.type = type;
		firePropertyChangeEvent();
	}
	
	@Override
	public void reset() {
		// does nothing
	}
	
	public String toString() {
		String s = "";
		switch (type) {
			case PathIterator.SEG_MOVETO:
				s += "MOVE_TO ";
				break;
			case PathIterator.SEG_LINETO:
				s += "LINE_TO ";
				break;
			case PathIterator.SEG_QUADTO:
				s += "QUAD_TO ";
				break;
			case PathIterator.SEG_CLOSE:
				s += "CLOSE ";
				break;
		}
		
		return s + "(" + index + ")";
	}

    public ContourIndexProperty clone() {
    	ContourIndexProperty that = (ContourIndexProperty)super.clone();
    	that.index = index;
    	that.type = type;
    	
        return that;
    }
}
