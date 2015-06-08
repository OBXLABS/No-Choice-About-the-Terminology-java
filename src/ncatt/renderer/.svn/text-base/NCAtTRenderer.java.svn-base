package ncatt.renderer;

import java.awt.BasicStroke;
import java.awt.geom.PathIterator;
import java.util.Iterator;
import ncatt.NCAtTConstants;
import ncatt.NCAtTGlyph;
import ncatt.property.ContourIndexProperty;
import ncatt.tesselator.TessData;
import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGroup;
import net.nexttext.property.NumberProperty;
import net.nexttext.renderer.G3DTextPageRenderer;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphicsJava2D;

public class NCAtTRenderer extends G3DTextPageRenderer implements NCAtTConstants
{
	public NCAtTRenderer(PApplet p) {
		super(p, 3.0f);
	}

	/**
     * Traverse the TextObject tree to render all of its glyphs.
     * 
     * <p>The tree is traversed using a variable to point at the current node
     * being processed. TextObjects specify their rotation and position
     * relative to their parent, which is handled by registering coordinate
     * system changes with the drawing surface as the tree is traversed.</p>
     * 
     * Currently, rendering is not synchronized with modifications to the
     * TextObjectTree. This is dodgy, but gives a performance boost, so will 
     * stay that way for the moment. However, it affects this method, because we
     * can't assume that the tree is always well structured.</p>
     * 
     * <p>Transformations are stored in a stack so that they can be undone as
     * needed. It is not appropriate to use the position of the TextObject to 
     * undo the transformation, because this may have changed due to the lack of
     * synchronization.</p>
     * 
     * @param root the TextObject node to traverse
     */
    protected void traverse(TextObject root) {
    	traverse(root, RENDER_BOTTOM);
    	traverse(root, RENDER_MIDDLE);
    	traverse(root, RENDER_TOP);
    	traverse(root, RENDER_ANY);
    }
    
    protected void traverse(TextObject root, int order) {
        TextObject current = root;
        do {
        	// Draw any glyphs
            if (current instanceof TextObjectGlyph) {
            	NumberProperty renderOrderProp = (NumberProperty)current.getProperty("Render Order");
            	if ((renderOrderProp != null && renderOrderProp.get() == order) || 
            	    (renderOrderProp == null && order == -1)) {
            		enterCoords(current);
            		renderGlyph((TextObjectGlyph) current);
            		exitCoords();
            	}
            }

            // Descend to process any children
            if (current instanceof TextObjectGroup) {
                TextObjectGroup tog = (TextObjectGroup) current;
                TextObject child = tog.getLeftMostChild();
                if (child != null) {
                    enterCoords(current);
                    current = child;
                    continue;
                }
            }
        
            // Processing of this node is complete, so move on to siblings.
            // Since a node may not have siblings, a search is made up the tree
            // for the first appropriate sibling. The search ends if a sibling
            // is found, or if it reaches the top of the tree.
            while (current != root) {
                TextObject sibling = current.getRightSibling();
                if (sibling != null) {
                    current = sibling;
                    break;
                } else {
                    current = current.getParent();
                    if (current == null) {
                        // Aaarghh, we were detached from the tree mid-render,
                        // so just abort the whole process.
                        return;
                    }
                    exitCoords();
                }
            }
        } while (current != root);
    }

	@Override
	protected void renderGlyph(TextObjectGlyph tog) {
		if (tog instanceof NCAtTGlyph) {
    		renderGlyph((NCAtTGlyph) tog);
    	} else {
    		System.err.println("DForm skipping non-NCAtT TextObjectGlyph: " + tog);
    	}
	}
	
	protected void renderGlyph(NCAtTGlyph glyph) {
		// save the current properties
        g.pushStyle();

        // check if the glyph's font has a vector font
        boolean hasVectorFont = glyph.getFont().getFont() != null;
        
        // optimize rendering based on the presence of DForms and of outlines
        if (glyph.isFilled()) {
            if (glyph.isDeformed()) {
        	    // fill the shape
                g.noStroke();
                g.fill(glyph.getColorAbsolute().getRGB());
                fillGlyph(glyph);
                
            } else {
                // set text properties
                if (hasVectorFont)
                	g.textFont(glyph.getFont(), glyph.getFont().getFont().getSize());
                else
                	g.textFont(glyph.getFont());
                g.textAlign(PConstants.LEFT, PConstants.BASELINE);
                
                // render glyph using Processing's native PFont drawing method
                g.fill(glyph.getColorAbsolute().getRGB());
                g.text(glyph.getGlyph(), 0, 0);
            }
        }

        if (glyph.isStroked()) {
            // draw the outline of the shape
            g.stroke(glyph.getStrokeColorAbsolute().getRGB());
            BasicStroke bs = glyph.getStrokeAbsolute();
            g.strokeWeight(bs.getLineWidth());
            if (g instanceof PGraphicsJava2D) {
                switch (bs.getEndCap()) {
                    case BasicStroke.CAP_ROUND:
                        g.strokeCap(PApplet.ROUND);
                        break;
                    case BasicStroke.CAP_SQUARE:
                        g.strokeCap(PApplet.PROJECT);
                        break;
                    default:
                        g.strokeCap(PApplet.SQUARE);
                    break;
                }
                switch (bs.getLineJoin()) {
                    case BasicStroke.JOIN_ROUND:
                        g.strokeJoin(PApplet.ROUND);
                        break;
                    case BasicStroke.JOIN_BEVEL:
                        g.strokeJoin(PApplet.BEVEL);
                        break;
                    default:
                        g.strokeJoin(PApplet.MITER);
                    break;
                }
            }
            g.noFill();
            strokeGlyph(glyph);
        }

        // restore saved properties
        g.popStyle();
	}
	
	/**
     * Fills the glyph using the tesselator.
     * 
     * @param glyph the glyph
     * @param gp the outline of the glyph
     */
    protected void fillGlyph(NCAtTGlyph glyph) {
        // save the current smooth property
        boolean smooth = g.smooth;
        // turn off smoothing so that we don't get gaps in between the triangles
        g.noSmooth();
                
        glyph.getTessData().get().draw(p);
        
        // restore saved smooth property
        if (smooth) g.smooth();
    }

    /**
     * Strokes the glyph using native Processing drawing functions.
     * 
     * @param gp the outline of the glyph
     */
    protected void strokeGlyph(NCAtTGlyph glyph) {
    	TessData tessData = glyph.getTessData().get();
    	Iterator<ContourIndexProperty> it = glyph.getContourIndices().iterator();
    	
    	while (it.hasNext()) {
    		ContourIndexProperty cip = it.next();
    		if (cip.getType() == PathIterator.SEG_MOVETO) {
    			g.beginShape();
    		} else if (cip.getType() == PathIterator.SEG_LINETO || cip.getType() == PathIterator.SEG_QUADTO) {
    			float[] vert = tessData.vertices[cip.getIndex()];
        		g.vertex(vert[0], vert[1]);
    		} else {
    			g.endShape(PConstants.CLOSE);
    		}
    	}
    }
}
