package ncatt.property;

import java.util.ArrayList;
import java.util.Iterator;

import net.nexttext.property.Property;
import net.nexttext.property.PropertyChangeListener;

public class ContourIndexListProperty extends Property implements PropertyChangeListener {

	private ArrayList<ContourIndexProperty> list;
	
	/**
	 * Default constructor.  Creates an empty IndexListProperty.
	 */
	public ContourIndexListProperty() {
        list = new ArrayList<ContourIndexProperty>();
	}
	
	/**
	 * Constructor with capacity. Creates an empty IndexListProperty with the given capacity.
	 */
	public ContourIndexListProperty(int capacity) {
        list = new ArrayList<ContourIndexProperty>(capacity);
	}
	
	/**
	 * Add a Contour Index to the end of the list.
	 */
	public void add(int index, int type) {
		add(list.size(), new ContourIndexProperty(index, type));
	}
	
	/**
	 * Adds a Contour Index at the specified position in the list.
	 */
	public void add(int position, int index, int type) {
		add(position, new ContourIndexProperty(index, type));
	}
	
	/**
	 * Adds a Contour Index object at the end of the list.
	 */
	public void add(ContourIndexProperty cip) {
		add(list.size(), cip);
	}
	
	/**
	 * Adds a Contour Index object at the specified position in the list.
	 */
	public void add(int position, ContourIndexProperty cip) {
		list.add(position, cip);
		firePropertyChangeEvent();
	}
	
	/**
	 * Returns the index at the specified position in the list.
	 */
	public ContourIndexProperty get(int position) {
        return list.get(position);
	}
	
	/**
     * Resets each ContourIndexProperty in the list.
     */
	@Override
	public void reset() {
		Iterator<ContourIndexProperty> i = list.iterator();
        while (i.hasNext()) {
            i.next().reset();
        }
        firePropertyChangeEvent();
	}
	
    /**
     * Clears out the content of the list.
     */
    public void clear() {
    	list.clear();
    	firePropertyChangeEvent();
    }
    
    public String toString() {
        StringBuffer ret = new StringBuffer();
        Iterator<ContourIndexProperty> i = list.iterator();
        while (i.hasNext()) {
            ret.append(i.next().toString());
        }
        return ret.toString();
	}

	/**
	 * Returns an iterator for the list of index objects.
     *
     * <p>Don't use this iterator to remove items from the list, since this
     * won't trigger the necessary PropertyChangeEvents.  If you need to remove
     * points, write a remove method for this class, and call that.</p>
	 */
	public Iterator<ContourIndexProperty> iterator() {
		return list.iterator();
	}
	   
	/**
	 * Returns the number of index objects contained in this list.
	 */
	public int size() {
		return list.size();
	}
	
	/**
     * For interface PropertyChangeListener, called when one of the properties
     * in the list changes.
     */
    public void propertyChanged(Property pc) {
        firePropertyChangeEvent();
    }

    public ContourIndexListProperty clone() {
    	ContourIndexListProperty that = (ContourIndexListProperty)super.clone();
        that.list = new ArrayList<ContourIndexProperty>(list.size());
        Iterator<ContourIndexProperty> i = list.iterator();
        while (i.hasNext()) {
            that.add(i.next().clone());
        }
        return that;
    }
}
