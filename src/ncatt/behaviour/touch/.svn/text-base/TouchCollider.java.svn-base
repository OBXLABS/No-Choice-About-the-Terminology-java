package ncatt.behaviour.touch;

import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Map;
import ncatt.input.Touch;
import net.nexttext.TextObject;
import net.nexttext.behaviour.Action;
import net.nexttext.behaviour.control.Multiplexer;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.Property;
import processing.core.PVector;

public class TouchCollider extends TouchAction implements TouchConstants 
{
	public static final boolean DEBUG = false;
	
	protected Multiplexer addedActions;
	protected Multiplexer downActions;
    protected Multiplexer removedActions;
    protected Multiplexer upActions;
    
    protected boolean locks;
    protected Rectangle limits;
    
    /**
     * @param touchID The touch ID to check for
     * @param touches The array of current touches (updated externally)
     * @param addedAction The Action to perform when the touch is added
     * @param downAction The Action to perform when the touch is down
     * @param removedAction The Action to perform when the touch is removed
     * @param upAction The Action to perform when the touch is up (idle)
     */
    public TouchCollider(int touchID) 
	{
		super(touchID);
		
    	addedActions   = new Multiplexer();
    	downActions    = new Multiplexer();
    	removedActions = new Multiplexer();
    	upActions      = new Multiplexer();
        
        locks = false;
        limits = null;
	}
    
    public TouchCollider() 
	{
		this(ANY_TOUCH);
	}
    
    public void addAddedAction(Action newAction)
    {
    	addedActions.add(newAction);
    }
    
    public void addDownAction(Action newAction)
    {
    	downActions.add(newAction);
    }
    
    public void addRemovedAction(Action newAction)
    {
    	removedActions.add(newAction);
    }
    
    public void addUpAction(Action newAction)
    {
    	upActions.add(newAction);
    }
    
    public void shouldLockOn(boolean locks) 
	{
    	this.locks = locks;
	}
    
    public boolean locksOn() 
	{
    	return locks;
	}
    
    public void setLimits(int x, int y, int width, int height)
    {
    	limits = new Rectangle(x, y, width, height);
    }
    
    public Rectangle getLimits()
    {
    	return limits;
    }
    
    public Map<String, Property> getRequiredProperties() {
    	Map<String, Property> reqProps = super.getRequiredProperties();
    	
        reqProps.putAll(addedActions.getRequiredProperties());
        reqProps.putAll(downActions.getRequiredProperties());
        reqProps.putAll(removedActions.getRequiredProperties());
        reqProps.putAll(upActions.getRequiredProperties());

        return reqProps;
    }
    
    /**
     * Updates touch collisions and applies the appropriate Action.
     * 
     * @param to The TextObject to consider.
     * @return the result of the Action that was applied.
     */
    public ActionResult behave(TextObject to) 
    {
    	ActionResult result = new ActionResult(false, false, false);
    	
    	// Ignore white space.
        if (to.toString().compareTo(" ") == 0) {
        	return result;
        }
        
        NumberProperty touchProp = getTouch(to);
    	int touchDownID = (int)touchProp.get();
    	
    	// The TextObject already has a Touch down on it...
    	if (touchDownID != NO_TOUCH) {
    		// ... and it is an ID we are checking for...
    		if (touchID == ANY_TOUCH || touchID == touchDownID) {
    			// ...and the Touch was removed...
        		if (isTouchRemoved(touchDownID, to)) {
        			if (locks) {
        				// ...unset the TextObject on the Touch.
        				Touch touch = touches.get(touchDownID);
        				if (touch != null) touch.setTextObject(null);
        			}
        			// ...reset the down Actions and removed Actions.
        			downActions.reset(to);
        			removedActions.reset(to);
        			// ...apply the removed Actions.
        			result = removedActions.behave(to);
        			// ...clear the Touch property of the TextObject.
        			touchProp.set(NO_TOUCH);
        			if (DEBUG) System.out.println("Touch " + touchDownID + " was REMOVED from '" + to + "'");
        		}
        		// ...and the Touch is still down...
        		else {
        			// ...apply the down Actions.
        			result = downActions.behave(to);
        			//if (DEBUG) System.out.println("Touch " + touchDownID + " is DOWN on '" + to + "'");
        		}
    		} 
    		// ... ignore the ID we are not checking for.
    		else {
    			result = new ActionResult(false, false, false);
    		}
    	}
    	
    	// The TextObject has no Touch down on it...
    	else {
    		Touch addedTouch = isTouchAdded(to);
    		// ...and a Touch was added
    		if (addedTouch != null) {
    			if (locks) {
    				// ...set the TextObject on the Touch.
    				addedTouch.setTextObject(to);
    			}
    			// ...set the Touch property of the TextObject.
    			touchProp.set(addedTouch.getID());
    			// ...set the Touch Position property of the TextObject.
    			getTouchPosition(to).set(new PVector(addedTouch.getX(), addedTouch.getY()));
    			// ...set the Touch Offset property of the TextObject.
    			Rectangle bounds = to.getBounds();
    			getTouchOffset(to).set(new PVector((float)(bounds.getX() - addedTouch.getX()), (float)(bounds.getY() - addedTouch.getY())));
    			// ...reset the up Actions and added Actions.
    			upActions.reset(to);
    			addedActions.reset(to);
    			// ...apply the added Action.
    			result = addedActions.behave(to);
    			if (DEBUG) System.out.println("Touch " + addedTouch.getID() + " (" + (int)touchProp.get() + ") was ADDED to '" + to + "'");
    		} 
    		// ...and no Touch is added...
    		else {
    			// ...apply the up Actions.
    			result = upActions.behave(to);
    		}
    	}
    	
    	return result;
    }
    
    /**
     * @param id The ID of the Touch to check for. Even if we're checking for ANY_TOUCH, we can only remove a Touch that was previously down.
     * @param to The TextObject to consider.
     * @return whether or not the Touch was removed from the TextObject.
     */
    protected boolean isTouchRemoved(int id, TextObject to)
    {
		Touch touch = touches.get(id);
		if (touch == null || touch.isDead() || (limits != null && !limits.contains(touch.getX(), touch.getY())) || (!locks && !to.getBounds().contains(touch.getX(), touch.getY()))) {
			// the Touch was removed
			if (touch == null) System.out.println("Removing NULL touch!");
			return true;
		}
		
		return false;
    }
    
    /**
     * @param to The TextObject to consider.
     * @return the added Touch, or null if no Touch was added.
     */
    protected Touch isTouchAdded(TextObject to)
    {
    	Rectangle bounds = to.getBounds();
    	
    	if (touchID >= 0) {
    		// look for a specific touchID
    		Touch touch = touches.get(touchID);
    		if (touch != null && touch.isAlive() && (!locks || !touch.hasTextObject()) && (limits == null || limits.contains(touch.getX(), touch.getY())) && bounds.contains(touch.getX(), touch.getY())) {
    			// the Touch was added
    			return touch;
    		}
    	} 
    	else {
    		// look for any touch
        	synchronized (touches) {
        		Iterator<Integer> it = touches.keySet().iterator();
        		while (it.hasNext()) {
        			Touch touch = touches.get(it.next());
        			if (touch.isAlive() && (!locks || !touch.hasTextObject()) && (limits == null || limits.contains(touch.getX(), touch.getY())) && bounds.contains(touch.getX(), touch.getY())) {
        				// the Touch was added
            			return touch;
        			}
        		}
        	}
    	}
    	
    	return null;
    }
}
