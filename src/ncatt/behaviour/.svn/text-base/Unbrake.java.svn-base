package ncatt.behaviour;

import net.nexttext.TextObject;

public class Unbrake extends NCAtTAction
{
	int impact;

	public Unbrake(int impact) 
	{
		this.impact = impact;
	}
	
	/**
     * Resets the velocity and direction of the passed TextObject
     * 
     * @param to The TextObject to act upon
     * @return The outcome of the condition
     */
	public ActionResult behave(TextObject to)
	{
		// calculate the new velocity and direction
		float origVel = getVelocity(to).getOriginal();
		int origDir = (int)getDirection(to).getOriginal();

		if (impact == IMPACT_SOLO) {
			getVelocity(to).set(origVel);
			getDirection(to).set(origDir);
		} 
		else if (impact == IMPACT_SIBLINGS) {
			// set the new velocity and direction of the TextObject and all of its siblings
			TextObject sibling = to.getParent().getLeftMostChild();
			while (sibling != null) {
				getVelocity(sibling).set(origVel);
				getDirection(sibling).set(origDir);

				sibling = sibling.getRightSibling();
			}
		} 
		else {  // impact == IMPACT_FOLLOWERS
			// set the new velocity and direction of the TextObject and all of its followers
			int currDir = (int)getDirection(to).get();
			TextObject sibling = to;
			while (sibling != null) {
				getVelocity(sibling).set(origVel);
				getDirection(sibling).set(origDir);

				if (currDir == DIR_LEFT) sibling = sibling.getRightSibling();
				else if (currDir == DIR_RIGHT) sibling = sibling.getLeftSibling();
				else sibling = null;
			}
		}

		return new ActionResult(true, true, false);
	}
}
