package ncatt.behaviour.soft;

import ncatt.NCAtTGlyph;
import ncatt.input.Touch;
import ncatt.property.TessDataProperty;
import ncatt.tesselator.TessData;
import net.nexttext.CoordinateSystem;
import processing.core.PVector;

public class Rubber extends SoftBase 
{
	protected float strength;
	protected float elasticity;
	
	public Rubber() 
	{
		this(1.0f, 1.0f);
	}
	
	//--------------------------------------------------------------
  	public Rubber(float strength, float elasticity) 
	{
		super(ANY_TOUCH);
		
		this.strength = strength;
		this.elasticity = elasticity;
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
			int closestIndex = -1;
		    float closestDist = -1;
			
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
				
				// point distance from Touch
	        	touchDelta = PVector.sub(touchPos, ptPosAbs);
	        	touchDist = touchDelta.mag();
	        	
	        	// find the closest point
	            if ((touchDist < closestDist) || (closestIndex == -1)) {
	            	closestDist = touchDist;
	                closestIndex = i;
	            }
			}
			
			// Make sure we found a closest point (this might not happen if, for example, 
			// the outline is all ANCHOR points and the SoftType only targets CONTROL points.
			if (closestIndex != -1) {
				// find the center distance
				PVector center = glyph.getCenter();
				float centerDist = PVector.sub(touchPos, center).mag();
				
				// calculate the force
				float force = ((centerDist - closestDist) / centerDist) * strength;
				float dx = mdx * force;
				float dy = mdy * force;
				
				// move the closest point
				tessData.vertices[closestIndex][0] += dx;
				tessData.vertices[closestIndex][1] += dy;
				
				// get the next index (start at 0 if it was the last one of the contour)
				int numElements = tessData.vertices.length;
		        int lastNextIndex = closestIndex;
		        int nextIndex = closestIndex + 1;
		        if (nextIndex >= numElements) nextIndex = 0;

		        PVector lastPt = new PVector(tessData.vertices[lastNextIndex][0], tessData.vertices[lastNextIndex][1]);
		        PVector nextPt = new PVector(tessData.vertices[nextIndex][0], tessData.vertices[nextIndex][1]);
		        PVector stretchedDelta = PVector.sub(lastPt, nextPt);
		        float stretchedDist = stretchedDelta.mag();

				TessData origTessData = tessDataProp.getOriginal();
		        PVector lastOrigPt = new PVector(origTessData.vertices[lastNextIndex][0], origTessData.vertices[lastNextIndex][1]);
		        PVector nextOrigPt = new PVector(origTessData.vertices[nextIndex][0], origTessData.vertices[nextIndex][1]);
		        PVector originalDelta = PVector.sub(lastOrigPt, nextOrigPt);
		        float originalDist = originalDelta.mag();

		        float nextElasticRatio = stretchedDist / originalDist;
		        
		        // attract the next neighbours
		        while ((nextElasticRatio > elasticity) && (nextIndex != closestIndex)) {
		        	dx = (lastPt.x - nextPt.x) * 0.1f;
		        	dy = (lastPt.y - nextPt.y) * 0.1f;
		        	
		        	tessData.vertices[nextIndex][0] = nextPt.x + dx;
		        	tessData.vertices[nextIndex][1] = nextPt.y + dy;
		        	
		        	// augment indexes
		        	lastNextIndex = nextIndex;
		        	if (++nextIndex >= numElements) nextIndex = 0;
		        	
		        	// calculate the elastic ratio between the two points
		        	lastPt = new PVector(tessData.vertices[lastNextIndex][0], tessData.vertices[lastNextIndex][1]);
			        nextPt = new PVector(tessData.vertices[nextIndex][0], tessData.vertices[nextIndex][1]);
			        stretchedDelta = PVector.sub(lastPt, nextPt);
		            stretchedDist = stretchedDelta.mag();

		            lastOrigPt = new PVector(origTessData.vertices[lastNextIndex][0], origTessData.vertices[lastNextIndex][1]);
			        nextOrigPt = new PVector(origTessData.vertices[nextIndex][0], origTessData.vertices[nextIndex][1]);
			        originalDelta = PVector.sub(lastOrigPt, nextOrigPt);
		            originalDist = originalDelta.mag();

		            nextElasticRatio = stretchedDist / originalDist;
		        }
		        
		        // get the prev index (start at 0 if it was the last one of the contour)
		        int lastPrevIndex = closestIndex;
		        int prevIndex = closestIndex - 1;
		        if (prevIndex < 0) prevIndex = numElements-1;

		        lastPt = new PVector(tessData.vertices[lastPrevIndex][0], tessData.vertices[lastPrevIndex][1]);
		        PVector prevPt = new PVector(tessData.vertices[prevIndex][0], tessData.vertices[prevIndex][1]);
		        stretchedDelta = PVector.sub(lastPt, prevPt);
		        stretchedDist = stretchedDelta.mag();

		        lastOrigPt = new PVector(origTessData.vertices[lastPrevIndex][0], origTessData.vertices[lastPrevIndex][1]);
		        PVector prevOrigPt = new PVector(origTessData.vertices[prevIndex][0], origTessData.vertices[prevIndex][1]);
		        originalDelta = PVector.sub(lastOrigPt, prevOrigPt);
		        originalDist = originalDelta.mag();

		        float prevElasticRatio = stretchedDist / originalDist;

		        // attract the next neighbours
		        while ((prevElasticRatio > elasticity) && (prevIndex != closestIndex)) {
		            dx = (lastPt.x - prevPt.x) * 0.1f;
		            dy = (lastPt.y - prevPt.y) * 0.1f;
		            
		            tessData.vertices[prevIndex][0] = prevPt.x + dx;
		            tessData.vertices[prevIndex][1] = prevPt.y + dy;
		            
		            // augment indexes
		            lastPrevIndex = prevIndex;
		            if (--prevIndex < 0) prevIndex = numElements - 1;

		            // calculate the elastic ratio between the two points
		            lastPt = new PVector(tessData.vertices[lastPrevIndex][0], tessData.vertices[lastPrevIndex][1]);
			        prevPt = new PVector(tessData.vertices[prevIndex][0], tessData.vertices[prevIndex][1]);
			        stretchedDelta = PVector.sub(lastPt, prevPt);
		            stretchedDist = stretchedDelta.mag();

		            lastOrigPt = new PVector(origTessData.vertices[lastPrevIndex][0], origTessData.vertices[lastPrevIndex][1]);
			        prevOrigPt = new PVector(origTessData.vertices[prevIndex][0], origTessData.vertices[prevIndex][1]);
			        originalDelta = PVector.sub(lastOrigPt, prevOrigPt);
		            originalDist = originalDelta.mag();

		            prevElasticRatio = stretchedDist / originalDist;
		        }
			}
			
			tessDataProp.set(tessData);
			glyph.setDeformed(true);
		}
		
		return new ActionResult(false, false, false);
	}

}
