package ncatt.behaviour.soft;

import ncatt.NCAtTGlyph;
import ncatt.input.Touch;
import ncatt.property.TessDataProperty;
import ncatt.tesselator.TessData;
import net.nexttext.CoordinateSystem;
import processing.core.PVector;

public class Drift extends SoftBase 
{
	protected float strength;
	protected float attenuation;
	protected float inertia;
	protected float friction;
	
	public Drift() 
	{
		this(1.0f, 10.0f, 0.15f, 0.02f);
	}
	
  	public Drift(float strength, float attenuation, float inertia, float friction) 
	{
		super(ANY_TOUCH);
		
		this.strength = strength;
		this.attenuation = attenuation;
		this.inertia = inertia;
		this.friction = friction;
	}
    
  	@Override
  	public ActionResult behave(NCAtTGlyph glyph)
  	{
		Touch touch = touches.get((int)getTouch(glyph).get());
		if (touch != null) {
			PVector touchPos = new PVector(touch.getX(), touch.getY());
			float mdx = touch.getX() - touch.getPX();
			float mdy = touch.getY() - touch.getPY();
			
			CoordinateSystem ac = glyph.getAbsoluteCoordinateSystem();
			
			// temp variables
			PVector ptPos = new PVector();
			PVector ptPosOrig = new PVector();
			PVector ptPosAbs, ptPosOrigAbs, touchDelta, origDelta;
			float touchDist, origDist;
			float force, dx, dy;
			
			TessDataProperty tessDataProp = getTessData(glyph);
			TessData tessData = tessDataProp.get();
			TessData origTessData = tessDataProp.getOriginal();
			for (int i = 0; i < tessData.vertices.length; i++) {
	        	ptPos.x = tessData.vertices[i][0];
	        	ptPos.y = tessData.vertices[i][1];
	        	ptPosAbs = ac.transform(ptPos);
	        	
	        	ptPosOrig.x = origTessData.vertices[i][0];
	        	ptPosOrig.y = origTessData.vertices[i][1];
	        	ptPosOrigAbs = ac.transform(ptPosOrig);
	        	
	        	// point distance from Touch
	        	touchDelta = PVector.sub(touchPos, ptPosAbs);
	        	touchDist = touchDelta.mag();
	        	
	        	// point distance from original
	        	origDelta = PVector.sub(ptPosAbs, ptPosOrigAbs);
	        	origDist = origDelta.mag();
	        	
	        	// apply force
	        	force = strength / (touchDist + attenuation);
	        	dx = force * mdx;
	        	dy = force * mdy;
	        	
	        	// don't attract to origin when it's too close, it's more stable this way
	        	if (origDist > 0.25f) {
	        		dx -= inertia * (origDelta.x / origDist);
	        		dy -= inertia * (origDelta.y / origDist);
	        	}
	        	
	        	// transform velocity
	        	PVector ptVel = getPointVelocity(i);
				ptVel.add(dx, dy, 0);
	        	
				// translate last delta
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
