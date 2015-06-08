package ncatt.behaviour;

import net.nexttext.TextObject;
import net.nexttext.property.NumberProperty;

public class Init extends NCAtTAction 
{
	protected float speed;
	
	public Init(float speed) 
	{
    	this.speed = speed;
    }
	
	public ActionResult behave(TextObject to) 
	{
		NumberProperty velProp = getVelocity(to);
		velProp.set(Math.abs(speed));
		velProp.setOriginal(Math.abs(speed));
		
		NumberProperty dirProp = getDirection(to);
		if (speed < 0) {
			dirProp.set(DIR_LEFT);
			dirProp.setOriginal(DIR_LEFT);
		} else if (speed > 0) {
			dirProp.set(DIR_RIGHT);
			dirProp.setOriginal(DIR_RIGHT);
		}
		
		return new ActionResult(true, true, false);
	}
}
