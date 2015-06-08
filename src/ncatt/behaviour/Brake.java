package ncatt.behaviour;

import net.nexttext.TextObject;

//--------------------------------------------------------------
//--------------------------------------------------------------
public class Brake extends NCAtTAction
{
	protected float drag;
	protected int impact;
	
	//--------------------------------------------------------------
	public Brake(float drag, int impact)
	{
		this.drag = drag;
		this.impact = impact;
	}

	//--------------------------------------------------------------
	public ActionResult behave(TextObject to)
	{
		int dir = (int)(getDirection(to)).get();
		
		if (impact == IMPACT_SOLO) {
			getVelocity(to).set(0);
		}
		else if (impact == IMPACT_SIBLINGS) {
			// start at the first sibling
			TextObject sibling = to.getParent().getLeftMostChild();
			while (sibling != null) {
				getVelocity(sibling).set(0);

				sibling = sibling.getRightSibling();
			}
		} 
		else {  // impact == IMPACT_FOLLOWERS
			// start at the object itself
			TextObject sibling = to;
			while (sibling != null) {
				getVelocity(sibling).set(0);

				if (dir == DIR_LEFT) {
					sibling = sibling.getRightSibling();
				} else if (dir == DIR_RIGHT) {
					sibling = sibling.getLeftSibling();
				}
			}
		}
		
    	return new ActionResult(false, false, false);
	}
}
