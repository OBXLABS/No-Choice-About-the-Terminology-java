/*
  This file is part of the NextText project.
  http://www.nexttext.net/

  Copyright (c) 2004-08 Obx Labs / Jason Lewis

  NextText is free software: you can redistribute it and/or modify it under
  the terms of the GNU General Public License as published by the Free Software 
  Foundation, either version 2 of the License, or (at your option) any later 
  version.

  NextText is distributed in the hope that it will be useful, but WITHOUT ANY
  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR 
  A PARTICULAR PURPOSE.  See the GNU General Public License for more details.

  You should have received a copy of the GNU General Public License along with 
  NextText.  If not, see <http://www.gnu.org/licenses/>.
*/

package ncatt.behaviour.dform;

import java.awt.Rectangle;
import ncatt.NCAtTGlyph;
import ncatt.property.TessDataProperty;
import ncatt.tesselator.TessData;
import processing.core.PVector;

/**
 * A DForm which scales the size of a TextObject.
 *
 */
public class Scale extends DForm {
    
    private float scale;
    
    /**
     * @param scale is amount the object's size will increase, as a multiplier. 
     */
    public Scale(float scale) {
        this.scale = scale;        
    }

    public ActionResult behave(NCAtTGlyph glyph) {
    	// Determine the center of to, in the same coordinates as the verts will be.
    	PVector toAbsPos = glyph.getPositionAbsolute();
        Rectangle bb = glyph.getBoundingPolygon().getBounds();
        PVector center = new PVector((float)bb.getCenterX(), (float)bb.getCenterY());
        center.sub(toAbsPos);
        
        // Traverse the verts of the glyph, applying the multiplication factor to each one, 
        // but offset from the center, not the position.
        TessDataProperty tessDataProp = getTessData(glyph);
		TessData tessData = tessDataProp.get();
        for (int i = 0; i < tessData.vertices.length; i++) {
        	tessData.vertices[i][0] = ((tessData.vertices[i][0] - center.x) * scale) + center.x;
        	tessData.vertices[i][1] = ((tessData.vertices[i][1] - center.y) * scale) + center.y;
		}
        
        tessDataProp.set(tessData);
		glyph.setDeformed(true);
        
        return new ActionResult(true, true, false);       
    }
    
    public void set(float scale) {
        this.scale = scale;
    }
}
