package ncatt.behaviour.touch;

import ncatt.input.Touch;
import net.nexttext.TextObject;
import net.nexttext.property.PVectorProperty;
import processing.core.PVector;

public class FollowTouch extends TouchAction
{
	public FollowTouch()
	{
		super(ANY_TOUCH);
	}
	
	/**
     * Follows the touch linked to the passed TextObject
     * 
     * @param to The TextObject to act upon
     * @return The outcome of the condition
     */
	public ActionResult behave(TextObject to)
	{
		Touch touch = touches.get((int)getTouch(to).get());
		PVectorProperty touchPosProp = getTouchPosition(to);
		
		if (touch != null) {
			// get the touch direction
			int touchDir;
			if (touch.getXSpeed() > 0) touchDir = DIR_RIGHT;
			else if (touch.getXSpeed() < 0) touchDir = DIR_LEFT;
			else touchDir = DIR_NONE;
			
			PVector touchOffset = new PVector(touch.getX() - touchPosProp.getX(), 0);
			touchPosProp.set(new PVector(touch.getX(), touch.getY()));
			
			// move the TextObject and all of its siblings
			TextObject sibling = to.getParent().getLeftMostChild();
			while (sibling != null) {
				getPosition(sibling).add(touchOffset);
				if (touchDir != DIR_NONE) 
					getDirection(sibling).set(touchDir);
				
				sibling = sibling.getRightSibling();
			}
		}
    	
    	return new ActionResult(false, false, false);
	}
}
