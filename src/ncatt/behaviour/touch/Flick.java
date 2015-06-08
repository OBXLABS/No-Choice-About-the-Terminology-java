package ncatt.behaviour.touch;

import ncatt.input.Touch;
import net.nexttext.TextObject;
import net.nexttext.property.NumberProperty;

public class Flick extends TouchAction
{
	int impact;
	float scalar;
	float clamp;
	
	/**
     * @param impact The impact mode of the Action
     */
	public Flick(int impact) 
	{
		this(impact, 1, Float.MAX_VALUE);
	}
	
	/**
     * @param impact The impact mode of the Action
     * @param scalar The multiplier used to scale the flick velocity
	 */
	public Flick(int impact, float scalar) 
	{
		this(impact, scalar, Float.MAX_VALUE);
	}
	
	/**
     * @param impact The impact mode of the Action
     * @param scalar The multiplier used to scale the flick velocity
	 * @param clamp The maximum velocity value
     */
	public Flick(int impact, float scalar, float clamp) 
	{
		super(ANY_TOUCH);
		
		this.impact = impact;
		this.scalar = scalar;
		this.clamp = clamp;
	}

	/**
     * Sets the velocity of the passed TextObject using the speed of the Touch
     * 
     * @param to The TextObject to act upon
     * @return The outcome of the condition
     */
	public ActionResult behave(TextObject to)
	{
		Touch touch = touches.get((int)getTouch(to).get());
		if (touch != null) {
			// calculate the new velocity and direction
			float newVel = (touch.getX() - touch.getPX()) * scalar;
			int newDir;
			if (newVel < 0) {
				newDir = DIR_LEFT;
				newVel *= -1;
			} else {
				newDir = DIR_RIGHT;
			}
			
			// clamp the new velocity
			//newVel = Math.min(newVel, clamp);
			
			// if the new velocity is too small, revert to the original value
			float origVel = getVelocity(to).getOriginal();
			int currDir = (int)getDirection(to).get();
			if (currDir == DIR_NONE) {
				currDir = (int)getDirection(to).getOriginal();
			}
			if (Float.isNaN(newVel) || newVel < origVel) {
				newVel = origVel;
				newDir = currDir;
			}
			
			if (impact == IMPACT_SOLO) {
				getVelocity(to).set(newVel);
				getDirection(to).set(newDir);
			} 
			else if (impact == IMPACT_SIBLINGS) {
				// set the new velocity and direction of the TextObject and all of its siblings
				TextObject sibling = to.getParent().getLeftMostChild();
				while (sibling != null) {
					getVelocity(sibling).set(newVel);
					getDirection(sibling).set(newDir);
					
					sibling = sibling.getRightSibling();
				}
			} 
			else {  // impact == IMPACT_FOLLOWERS
				// set the new velocity and direction of the TextObject and all of its followers
				TextObject sibling = to;
				while (sibling != null) {
					getVelocity(sibling).set(newVel);
					getDirection(sibling).set(newDir);

					if (currDir == DIR_LEFT) sibling = sibling.getRightSibling();
					else if (currDir == DIR_RIGHT) sibling = sibling.getLeftSibling();
					else sibling = null;
				}

				// set the direction of all remaining TextObjects at the same level, and set the velocity if it is not fast enough
				NumberProperty velProp;
				if (currDir == DIR_LEFT) sibling = to.getLeftSibling();
				else if (currDir == DIR_RIGHT) sibling = to.getRightSibling();
				else sibling = null;
				while (sibling != null) {
					getDirection(sibling).set(newDir);
					velProp = getVelocity(sibling);
					if (velProp.get() < newVel) {
						velProp.set(newVel);
					}

					if (currDir == DIR_LEFT) sibling = sibling.getLeftSibling();
					else if (currDir == DIR_RIGHT) sibling = sibling.getRightSibling();
					else sibling = null;
				}
			}
 			
			return new ActionResult(true, true, false);
		}
    	
    	return new ActionResult(false, true, false);
	}
}
