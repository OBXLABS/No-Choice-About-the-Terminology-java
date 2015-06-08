package ncatt.behaviour;

import java.util.Map;

import net.nexttext.TextObject;
import net.nexttext.behaviour.AbstractAction;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Property;

public class SetRenderOrder extends AbstractAction 
{
	protected int order;
	protected int siblingOrder;
	
	public SetRenderOrder(int order)
	{
    	this(order, -1);
    }
	
	public SetRenderOrder(int order, int siblingOrder) 
	{
    	this.order = order;
    	this.siblingOrder = siblingOrder;
    }
	
	/**
     * @return Map containing the properties
     */
    public Map<String, Property> getRequiredProperties() 
    {
        Map<String, Property> properties = super.getRequiredProperties();
        
        NumberProperty renderOrderProp = new NumberProperty(0);
        properties.put("Render Order", renderOrderProp);
        
        return properties;
    }
    
    public ActionResult behave(TextObject to) 
	{
    	if (siblingOrder > -1) {
    		// set the render order of the siblings
    		TextObject sibling = to.getParent().getLeftMostChild();
    		do {
    			NumberProperty renderOrderProp = (NumberProperty)sibling.getProperty("Render Order");
    	    	renderOrderProp.set(siblingOrder);
    			
    			sibling = sibling.getRightSibling();
    		} while (sibling != null);
    	}
    	
    	if (order != siblingOrder) {
    		// set the TextObject render order
        	NumberProperty renderOrderProp = (NumberProperty)to.getProperty("Render Order");
        	renderOrderProp.set(order);
    	}

    	return new ActionResult(true, true, false);
	}
}
