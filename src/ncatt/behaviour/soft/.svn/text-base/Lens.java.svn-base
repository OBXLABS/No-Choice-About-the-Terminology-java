package ncatt.behaviour.soft;

import java.util.Vector;
import ncatt.NCAtTGlyph;
import ncatt.input.Touch;
import ncatt.property.TessDataProperty;
import ncatt.tesselator.TessData;
import net.nexttext.CoordinateSystem;
import processing.core.PVector;

public class Lens extends SoftBase
{
	protected float magnification;
	protected float diameter;
	protected Vector<PVector> lastDeltas;
	
	public Lens() 
	{
		this(1.0f, 20.0f);
	}
	
	//--------------------------------------------------------------
  	public Lens(float magnification, float diameter) 
	{
		super(ANY_TOUCH);
		
		this.magnification = magnification;
		this.diameter = diameter;
		lastDeltas = new Vector<PVector>();
	}
  	
  	@Override
  	public ActionResult behave(NCAtTGlyph glyph)
  	{
  		Touch touch = touches.get((int)getTouch(glyph).get());
		if (touch != null) {
			PVector touchPos = new PVector(touch.getX(), touch.getY());
		
			CoordinateSystem ac = glyph.getAbsoluteCoordinateSystem();
			
			PVector ptPos = new PVector();
			PVector ptPosAbs, touchDelta, newDelta;
			float touchDist;
			float dx, dy;
				
			TessDataProperty tessDataProp = getTessData(glyph);
			TessData tessData = tessDataProp.get();
			for (int i = 0; i < tessData.vertices.length; i++) {
	        	ptPos.x = tessData.vertices[i][0];
	        	ptPos.y = tessData.vertices[i][1];
	        	ptPosAbs = ac.transform(ptPos);
				
				// offset the Touch position from this point
				touchDelta = PVector.sub(touchPos, ptPosAbs);
				touchDist = touchDelta.mag();
				
				float scaleFactor = (diameter / 2.0f) - (diameter / 2.0f - 1) / (touchDist + 1.0f);
				
				if (touchDist != 0) {
					dx = -touchDelta.x + (magnification * -touchDelta.x / touchDist) * scaleFactor;
					dy = -touchDelta.y + (magnification * -touchDelta.y / touchDist) * scaleFactor;
				} else {
					dx = -touchDelta.x;
					dy = -touchDelta.y;
				}
				
				newDelta = new PVector(dx + touchDelta.x, dy + touchDelta.y);
				
				// make sure we have enough deltas stored in the list
		  		while (i >= lastDeltas.size()) lastDeltas.add(new PVector());
		  		ptPos.add(PVector.sub(newDelta, lastDeltas.get(i)));

		  		tessData.vertices[i][0] = ptPos.x;
		  		tessData.vertices[i][1] = ptPos.y;
	        	
		  		lastDeltas.get(i).set(newDelta);
			}
			
			tessDataProp.set(tessData);
			glyph.setDeformed(true);
		}
		
		return new ActionResult(false, false, false);
  	}
}
