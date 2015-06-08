package ncatt.property;

import ncatt.tesselator.TessData;
import net.nexttext.property.Property;

public class TessDataProperty extends Property {
	
	private TessData original;
	private TessData value;
	
	public TessDataProperty() {
		original = new TessData();
		value = new TessData();	
	}
	
	public TessDataProperty(TessData data) {
		original = data;
		value = data.clone();	
	}
	
	public TessDataProperty(TessData original, TessData value) {
		this.original = original;
		this.value = value;	
	}
	
	/**
	 * @return the original value of this property .
	 */
	public TessData getOriginal() { 
        return original.clone();
	}
	
	/**
	 * Set the original value of the property.
	 */
	public void setOriginal(TessData original) {
		this.original = original;	
	    firePropertyChangeEvent();
	}
	
	/**
	 * @return the value of this property.
	 */
	public TessData get() {
	    return value.clone();
	}
	
	/**
	 * Sets the value of this property. The object passed as a newValue will 
	 * be copied before it is assigned to the property's value. 
	 */
	public void set(TessData data) {
		value = data;
	    firePropertyChangeEvent();
	}
	
	@Override
	public void reset() {
		value = original.clone();
		firePropertyChangeEvent();
	}

}
