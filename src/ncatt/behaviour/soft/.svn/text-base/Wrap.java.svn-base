package ncatt.behaviour.soft;

import processing.core.PConstants;
import processing.core.PVector;
import ncatt.NCAtTGlyph;
import ncatt.input.Touch;
import ncatt.property.TessDataProperty;
import ncatt.tesselator.TessData;
import net.nexttext.CoordinateSystem;

public class Wrap extends SoftBase 
{
	protected float backRadius;
	protected float backStrengthX;
	protected float backStrengthY;
	
	protected float awayRadius;
	protected float awayStrength;
	
	public Wrap() 
	{
		this(20.0f, 1.1f, 0.5f, 5.0f, 0.5f);
	}
	
	public Wrap(float backRadius, float backStrengthX, float backStrengthY, 
			float awayRadius, float awayStrength) 
	{
		super(ANY_TOUCH);
		
		this.backRadius = backRadius;
		this.backStrengthX = backStrengthX;
		this.backStrengthY = backStrengthY;
		
		this.awayRadius = awayRadius;
		this.awayStrength = awayStrength;
	}
	
	@Override
	public ActionResult behave(NCAtTGlyph glyph) {
		Touch touch = touches.get((int)getTouch(glyph).get());
		if (touch != null) {
			PVector touchPos = new PVector(touch.getX(), touch.getY());
			
			CoordinateSystem ac = glyph.getAbsoluteCoordinateSystem();
			
			// temp variables
			PVector ptPos = new PVector();
			PVector ptPosAbs, touchDelta;
			float touchDist;
			
			TessDataProperty tessDataProp = getTessData(glyph);
			TessData tessData = tessDataProp.get();
			for (int i = 0; i < tessData.vertices.length; i++) {
				ptPos.x = tessData.vertices[i][0];
	        	ptPos.y = tessData.vertices[i][1];
				ptPosAbs = ac.transform(ptPos);
				
				// offset the Touch position from this point
				touchDelta = PVector.sub(touchPos, ptPosAbs);
				touchDist = touchDelta.mag();

				int dir = (int)getDirection(glyph).get();
				float ang = (float)Math.atan2(ptPosAbs.y - touchPos.y, ptPosAbs.x - touchPos.x);
				boolean angCheck = (dir == DIR_LEFT && Math.abs(ang) < (0.25f * PConstants.PI)) ||
						(dir == DIR_RIGHT && Math.abs(ang) > (0.75f * PConstants.PI));
				
				if (angCheck) {
					PVector ptVel = new PVector();
					if (touchDist > backRadius) {
						// push the point back
						ptVel.set(touchDelta);
						ptVel.x *= backStrengthX;
						ptVel.y *= -1 * backStrengthY;
					} else if (touchDist < awayRadius) {
						// push the point away from the core
						ptVel.set(touchDelta);
						ptVel.y *= -1 * awayStrength;
					}
					
					// transform the delta with velocity
					ptPos.add(ptVel);
					
					tessData.vertices[i][0] = ptPos.x;
					tessData.vertices[i][1] = ptPos.y;
				}
			}
			
			tessDataProp.set(tessData);
			glyph.setDeformed(true);
		}
		
		return new ActionResult(false, false, false);
	}
}
