package ncatt;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Polygon;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Iterator;
import ncatt.property.ContourIndexListProperty;
import ncatt.property.TessDataProperty;
import ncatt.tesselator.TessContourVertex;
import ncatt.tesselator.TessData;
import ncatt.tesselator.Tessellator;
import net.nexttext.Book;
import net.nexttext.TextObjectGlyph;
import net.nexttext.property.ColorProperty;
import net.nexttext.property.Property;
import net.nexttext.property.PropertyChangeListener;
import net.nexttext.property.StrokeProperty;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class NCAtTGlyph extends TextObjectGlyph {
	
    public static Tessellator tesselator;

	public NCAtTGlyph(String glyph, PFont pfont, float size, PVector pos,
			Color fillColor, Color strokeColor, float strokeWeight) {
		super(glyph, pfont, size, pos);
		
		setStrokeAndFill(fillColor, strokeColor, strokeWeight);
		
	 	// When the control points change, the renderer cache is no longer
        // valid, and the glyph has been deformed.
		getControlPoints().addChangeListener(new PropertyChangeListener() {
			public void propertyChanged(Property propertyThatChanged) {
				glyphDeformed();
			}
		});
		
		properties.init("Tess Data", new TessDataProperty());
        properties.init("Contour Indices", new ContourIndexListProperty());
        
        glyphChanged2();
        
        // When the tesselation data changes, the renderer cache is no longer
        // valid, and the glyph has been deformed.
        getTessData().addChangeListener(new PropertyChangeListener() {
        	public void propertyChanged(Property propertyThatChanged) {
        		glyphDeformed();
        	}
        });
	}
	
	public void setStrokeAndFill(Color fillColor, Color strokeColor, float strokeWeight) {
		// initialize the color
		ColorProperty colorProperty = getColor();
		colorProperty.set(fillColor);
		colorProperty.setOriginal(fillColor);
		
		// initialize the stroke color
		ColorProperty strokeColorProperty = getStrokeColor();
		strokeColorProperty.set(strokeColor);
		strokeColorProperty.setOriginal(strokeColor);

		// initialize the stroke weight
		StrokeProperty strokeProperty = getStroke();
		strokeProperty.setOriginal(new BasicStroke(strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
		strokeProperty.set(new BasicStroke(strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER));
	}
	
	@Override
	public void setGlyph(String glyph) {
		this.glyph = glyph;
        glyphChanged2();
	}
	
	@Override
	public void setFont(PFont pfont) {
        // Ideally setFont would attempt to preserve deformations.  However
        // that's a lot of work, so we have deferred it.
		this.pfont = pfont;
		font = Book.loadFontFromPFont(pfont);
        glyphChanged2();
	}
	
    /**
     * Convenience accessor for the tesselation data.
     */
    public TessDataProperty getTessData() {
        return (TessDataProperty) getProperty("Tess Data");
    }
    
    /**
     * Convenience accessor for the contour index list.
     */
    public ContourIndexListProperty getContourIndices() {
        return (ContourIndexListProperty) getProperty("Contour Indices");
    }
	
	/**
     * Reset any internally cached information that becomes invalid because the
     * glyph has changed.
     */
    protected void glyphChanged2() {
    	glyphChanged();
        buildTessData();
    }
    
    public void buildTessData() {
		if (tesselator == null) {
            PGraphics.showException("The TextObjectGlyph Tesselator has not been set.");
            return;
		}
		
		TessData tessData = tesselator.tesselateGlyph(this);
		getTessData().set(tessData);
		getTessData().setOriginal(tessData.clone());
		
        buildContourIndices(tesselator.getContourVertices());
	}
	
	protected void buildContourIndices(ArrayList<TessContourVertex> contourVerts) {
		Iterator<TessContourVertex> it = contourVerts.iterator();
		TessData tessData = getTessData().get();
		ContourIndexListProperty indexList = getContourIndices();
		PVector delta;
		
		while (it.hasNext()) {
			TessContourVertex contourVert = it.next();
			
            if (contourVert.segType == PathIterator.SEG_CLOSE) {
            	// no need to find a match to CLOSE the path
            	indexList.add(0, contourVert.segType);
            } else {
            	for (int i=0; i < tessData.vertices.length; i++) {
            		delta = new PVector(tessData.vertices[i][0] - contourVert.x, tessData.vertices[i][1] - contourVert.y);
            		if (delta.mag() == 0) {
            			// found a match!
            			indexList.add(i, contourVert.segType);
            			break;
            		}
            	}
            }
        }
	}

	@Override
	public synchronized Polygon getLocalBoundingPolygon() {

        if (localBoundingPolygonValidToFrame >= getFrameCount()) {
            return localBoundingPolygon;
        }
        localBoundingPolygonValidToFrame = Long.MAX_VALUE;

        // Find the smallest box enclosing all the object's tesselation vertices by 
        // computing the min/max
        
        float minX = Float.POSITIVE_INFINITY;
        float minY = Float.POSITIVE_INFINITY;
        float maxX = Float.NEGATIVE_INFINITY;
        float maxY = Float.NEGATIVE_INFINITY;

        // if font is not defined then we are using a bitmap font, so
        // calculate the bounds using the font metrics
    	if (font == null) {
    		minX = 0;
    		minY = -(size*pfont.ascent());
    		maxX = size*pfont.width(getGlyph().charAt(0));
    		maxY = size*pfont.descent();
    	}
    	// if not, we have tesselation data so calculate by checking verts
    	else {    	
	        // Spaces are calculated differently because they don't have control
	        // points in the same way as other glyphs.
	        if ( getGlyph().equals(" ") ) {        	
	            Rectangle2D sb = Book.loadFontFromPFont(pfont).getStringBounds(" ", frc);
	            minX = (float)sb.getMinX();
	            minY = (float)sb.getMinY();
	            maxX = (float)sb.getMaxX();
	            maxY = (float)sb.getMaxY();
	        	
	        } else {
	        	float[][] vertices = getTessData().get().vertices;
	        	for (int i=0; i < vertices.length; i++) {
	                minX = Math.min(vertices[i][0], minX);
	                minY = Math.min(vertices[i][1], minY);
	                maxX = Math.max(vertices[i][0], maxX);
	                maxY = Math.max(vertices[i][1], maxY);
	        	}
	        }
    	}
        
        // return the box as a polygon object
        
		int[] x = new int[] { (int)minX, (int)maxX, (int)maxX, (int)minX };
		int[] y = new int[] { (int)minY, (int)minY, (int)maxY, (int)maxY };

        localBoundingPolygon = new Polygon( x, y, 4 );
        return localBoundingPolygon;
    }
}
