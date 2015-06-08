package ncatt.behaviour.soft;

import ncatt.NCAtTGlyph;
import ncatt.input.Touch;
import ncatt.property.TessDataProperty;
import ncatt.tesselator.TessData;
import net.nexttext.CoordinateSystem;
import processing.core.PVector;

public class Inflate extends SoftBase 
{
	protected float strength;
	protected float attenuation;
	protected float friction;
	protected float randomness;
	
	public Inflate() 
	{
		this(1.0f, 0.0f, 0.1f, 0.0f);
	}
	
  	public Inflate(float strength, float attenuation, float friction, float randomness) 
	{
		super(ANY_TOUCH);
		
		this.strength = strength;
		this.attenuation = attenuation;
		this.friction = friction;
		this.randomness = randomness;
	}
	
	@Override
	public ActionResult behave(NCAtTGlyph glyph) 
	{
		Touch touch = touches.get((int)getTouch(glyph).get());
		if (touch != null) {
			PVector touchPos = new PVector(touch.getX(), touch.getY());
			
			CoordinateSystem ac = glyph.getAbsoluteCoordinateSystem();
			
			// temp variables
			PVector ptPos = new PVector();
			PVector ptPosAbs, touchDelta;
			float touchDist;
			float dx, dy;
			
			TessDataProperty tessDataProp = getTessData(glyph);
			TessData tessData = tessDataProp.get();
			for (int i = 0; i < tessData.vertices.length; i++) {
				ptPos.x = tessData.vertices[i][0];
	        	ptPos.y = tessData.vertices[i][1];
				ptPosAbs = ac.transform(ptPos);
				PVector ptVel = getPointVelocity(i);
				
				// offset the Touch position from this point
				touchDelta = PVector.sub(touchPos, ptPosAbs);
				touchDist = touchDelta.mag();
				
				if (attenuation == 0 || touchDist < attenuation) {
					dx = touchDelta.x / touchDist * strength;
					dy = touchDelta.y / touchDist * strength;
					
					dx += randomness * dx * Math.random();
					dy += randomness * dx * Math.random();
					
					// transform the velocity
					ptVel.add(-dx, -dy, 0);
				}
				
				// transform the delta with velocity
				ptPos.add(ptVel);
				
				tessData.vertices[i][0] = ptPos.x;
				tessData.vertices[i][1] = ptPos.y;
				
				// apply friction to velocity
				ptVel.mult(1.0f - friction);
			}
			
			tessDataProp.set(tessData);
			glyph.setDeformed(true);
		}
		
		return new ActionResult(false, false, false);
	}

}
