package ncatt.behaviour.dform;

import ncatt.NCAtTGlyph;
import ncatt.behaviour.NCAtTAction;
import ncatt.property.TessDataProperty;
import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGlyphIterator;
import net.nexttext.TextObjectGroup;

public abstract class DForm extends NCAtTAction {
    /**
     * The tessellation data used to deform a glyph.
     */
    public TessDataProperty getTessData(NCAtTGlyph glyph) {
    	return glyph.getTessData();
    }

    public abstract ActionResult behave(NCAtTGlyph glyph);

    /**
     * Default implementation which recursively calls behave on all children.
     *
     * <p>The results of the called actions are combined using the method
     * outlined in ActionResult.  </p>
     */
    public ActionResult behave(TextObject to) {
        if (to instanceof NCAtTGlyph) {
            return behave((NCAtTGlyph) to);
        } else {
            ActionResult result = new ActionResult();
            TextObjectGlyphIterator i = ((TextObjectGroup) to).glyphIterator();
            while (i.hasNext()) {
            	TextObjectGlyph tog = i.next();
            	if (tog instanceof NCAtTGlyph) {
            		ActionResult tres = behave((NCAtTGlyph) tog);
            		result.combine(tres);
            	} else {
            		System.err.println("DForm skipping non-NCAtT TextObjectGlyph: " + tog);
            	}
            }
            return result.endCombine();
        }
    }
}