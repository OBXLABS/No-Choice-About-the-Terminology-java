package ncatt.behaviour;

import java.awt.Rectangle;
import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.property.NumberProperty;
import net.nexttext.property.PVectorProperty;
import processing.core.PVector;

//--------------------------------------------------------------
//--------------------------------------------------------------
public class Scroll extends NCAtTAction 
{
	protected Rectangle frame;
	protected float lineWidth;
	protected float clamp = 30;
	
	//--------------------------------------------------------------
	public Scroll(Rectangle bounds)
	{
    	this(bounds, bounds.width);
    }
	
	//--------------------------------------------------------------
	public Scroll(Rectangle bounds, float lineWidth) 
	{
		frame = bounds;
		this.lineWidth = lineWidth;
	}
	
	//--------------------------------------------------------------
	public ActionResult behave(TextObject to) 
	{
		// scroll
		PVectorProperty posProp = getPosition(to);
		NumberProperty velProp = getVelocity(to);
		NumberProperty dirProp = getDirection(to);
    	posProp.add(new PVector(velProp.get() * dirProp.get(), 0));
    	
    	// slow down if necessary
    	if (velProp.get() > clamp) {
    		velProp.set(velProp.get() * 0.9f);
    	}
    	
    	// cycle across if necessary
    	int dir = (int)(getDirection(to)).get();
    	Rectangle bounds = to.getBounds();
    	
    	if (dir == DIR_LEFT && to.getLeftSibling() == null && frame.getMinX() > bounds.getMaxX()) {
    		// detach the TextObject and add it to the right-most sibling
    		TextObject rightMost = to.getParent().getRightMostChild();
    		if (to != rightMost) {
    			to.detach();
    			rightMost.attachToRight(to);
    		
    			// re-position the TextObject
    			float newX;
    			if (rightMost instanceof TextObjectGlyph) {
    				newX = (float)(rightMost.getPosition().getX() + ((TextObjectGlyph)rightMost).getLogicalBounds().getWidth());
    			} else {
    				newX = (float)rightMost.getBounds().getMaxX();
    			}
    			posProp.set(new PVector(newX, posProp.getY()));

    			// get the new sibling's velocity
    			velProp.set(getVelocity(rightMost).get());
    		}
    		
    	} else if (dir == DIR_RIGHT && to.getRightSibling() == null && frame.getMaxX() < bounds.getMinX()) {
    		// detach the TextObject and add it to the left-most sibling
    		TextObject leftMost = to.getParent().getLeftMostChild();
    		if (to != leftMost) {
    			to.detach();
    			leftMost.attachToLeft(to);
    		
    			// re-position the TextObject
    			float newX;
    			if (to instanceof TextObjectGlyph) {
    				newX = (float)(leftMost.getPosition().getX() - ((TextObjectGlyph)to).getLogicalBounds().getWidth());
    			} else {
    				newX = (float)(leftMost.getBounds().getX() - bounds.getWidth());
    			}
    			posProp.set(new PVector(newX, posProp.getY()));

    			// get the new sibling's velocity
    			velProp.set(getVelocity(leftMost).get());
    		}
    	}
       	
       	return new ActionResult(false, false, false);
    }
	
	//--------------------------------------------------------------
	public void setLineWidth(float width) 
	{
		lineWidth = width;
	}
	
	//--------------------------------------------------------------
	public float getLineWidth() 
	{
		return lineWidth;
	}
}
