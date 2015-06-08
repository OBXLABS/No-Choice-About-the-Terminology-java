package ncatt.behaviour.touch;

import java.util.Map;

import processing.core.PVector;
import ncatt.behaviour.NCAtTAction;
import ncatt.input.Touch;
import net.nexttext.TextObject;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.PVectorProperty;
import net.nexttext.property.Property;

public abstract class TouchAction extends NCAtTAction implements TouchConstants 
{
	public static Map<Integer, Touch> touches;
	
	protected int touchID;
	
	/**
     * @param touchID The touch ID to check for
     */
	public TouchAction(int touchID) 
	{
		this.touchID = touchID;
	}
	
	/**
     * Gets the set of properties required by all TouchActions
     * @return Map containing the properties
     */
    public Map<String, Property> getRequiredProperties() 
    {
        Map<String, Property> properties = super.getRequiredProperties();

        NumberProperty touchProp = new NumberProperty(NO_TOUCH);
        properties.put("Touch", touchProp);
        
        PVectorProperty offsetProp = new PVectorProperty(new PVector());
        properties.put("TouchOffset", offsetProp);
        
        PVectorProperty posProp = new PVectorProperty(new PVector());
        properties.put("TouchPosition", posProp);
        
        return properties;
    }
    
    /** Returns a TextObject's Touch property. */
    protected NumberProperty getTouch(TextObject to) {
        return (NumberProperty)to.getProperty("Touch");
    }
    
    /** Returns a TextObject's TouchOffset property. */
    protected PVectorProperty getTouchOffset(TextObject to) {
        return (PVectorProperty)to.getProperty("TouchOffset");
    }
    
    /** Returns a TextObject's TouchPosition property. */
    protected PVectorProperty getTouchPosition(TextObject to) {
        return (PVectorProperty)to.getProperty("TouchPosition");
    }
}
