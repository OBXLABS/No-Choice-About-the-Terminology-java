package ncatt.behaviour.dform;

import ncatt.NCAtTGlyph;
import ncatt.property.TessDataProperty;
import ncatt.tesselator.TessData;

public class CounterScroll extends DForm {

	@Override
	public ActionResult behave(NCAtTGlyph glyph) {
		float vel = getVelocity(glyph).get();
		int dir = (int)getDirection(glyph).get();
		TessDataProperty tessDataProp = getTessData(glyph);
		TessData tessData = tessDataProp.get();
		
		for (int i = 0; i < tessData.vertices.length; i++) {
			tessData.vertices[i][0] += vel * dir * -1;
		}
		
		tessDataProp.set(tessData);
		glyph.setDeformed(true);
		
		return new ActionResult(false, false, false);
	}

}
