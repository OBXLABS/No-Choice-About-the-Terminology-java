package ncatt.behaviour;

import java.util.Map;

import ncatt.NCAtTConstants;
import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Property;

public class NCAtTAction extends AbstractAction implements NCAtTConstants
{	
	/**
     * Gets the set of properties required by all TouchConditions
     * @return Map containing the properties
     */
    public Map<String, Property> getRequiredProperties() 
    {
        Map<String, Property> properties = super.getRequiredProperties();
        
        NumberProperty velProp = new NumberProperty(0);
        properties.put("Velocity", velProp);
        
        NumberProperty dirProp = new NumberProperty(DIR_NONE);
        properties.put("Direction", dirProp);
        
        return properties;
    }
    
    /** Returns a TextObject's Velocity property. */
    protected NumberProperty getVelocity(TextObject to) 
    {
        return (NumberProperty)to.getProperty("Velocity");
    }
    
    /** Returns a TextObject's Direction property. */
    protected NumberProperty getDirection(TextObject to) 
    {
        return (NumberProperty)to.getProperty("Direction");
    }
}
