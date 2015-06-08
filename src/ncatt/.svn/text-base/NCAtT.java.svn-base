package ncatt;

import japplemenubar.JAppleMenuBar;
import java.awt.Color;
import java.awt.Rectangle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.media.opengl.glu.GLU;
import ncatt.behaviour.Brake;
import ncatt.behaviour.Impact;
import ncatt.behaviour.Init;
import ncatt.behaviour.Scroll;
import ncatt.behaviour.SetRenderOrder;
import ncatt.behaviour.Unbrake;
import ncatt.behaviour.dform.CounterScroll;
import ncatt.behaviour.dform.Reform;
import ncatt.behaviour.dform.Scale;
import ncatt.behaviour.soft.Drift;
import ncatt.behaviour.soft.Inflate;
import ncatt.behaviour.soft.Lens;
import ncatt.behaviour.soft.Rubber;
import ncatt.behaviour.soft.Wrap;
import ncatt.behaviour.touch.Flick;
import ncatt.behaviour.touch.FollowTouch;
import ncatt.behaviour.touch.TouchAction;
import ncatt.behaviour.touch.TouchCollider;
import ncatt.input.Touch;
import ncatt.renderer.NCAtTRenderer;
import ncatt.tesselator.Tessellator;
import net.nexttext.Book;
import net.nexttext.TextObjectGlyph;
import net.nexttext.TextObjectGlyphIterator;
import net.nexttext.TextObjectGroup;
import net.nexttext.behaviour.Behaviour;
import net.nexttext.behaviour.control.ApplyToGlyph;
import net.nexttext.behaviour.control.Chain;
import net.nexttext.behaviour.control.Multiplexer;
import net.nexttext.behaviour.control.Repeat;
import net.nexttext.behaviour.standard.Colorize;
import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;
import TUIO.TuioCursor;
import TUIO.TuioProcessing;

//--------------------------------------------------------------
//--------------------------------------------------------------
@SuppressWarnings("serial")
public class NCAtT extends PApplet implements NCAtTConstants
{
	/***************************************************************
	 *  application settings 
	 ***************************************************************/
	
	public static final boolean FULLSCREEN      = true;
	public static final boolean DEBUG_FRAMERATE = false;
	public static final boolean DEBUG_TOUCHES   = false;
	public static final boolean DEBUG_BOUNDS    = false;
	
	public static final int FPS = 30;
	
	// text settings
	public static final String TEXT_FILE  = "NCAtT.txt"; //located in bin/data directory
	public static final String FONT_FILE  = "pfbeausansproblack.ttf";
	public static final int FONT_SIZE     = 60;
	public static final int STROKE_WEIGHT = 2;
	
	// colour settings
	public static final Color BG_COLOR            = new Color(255, 0, 10, 255);
    public static final Color STROKE_COLOR_IDLE   = new Color(0, 0, 0, 0);
    public static final Color FILL_COLOR_IDLE     = new Color(0, 0, 0, 255);
    public static final Color STROKE_COLOR_TOUCH0 = new Color(0, 0, 0, 0);
    public static final Color FILL_COLOR_TOUCH0   = new Color(239, 239, 65, 255);
    public static final Color STROKE_COLOR_TOUCH1 = new Color(255, 0, 10, 150);
    public static final Color FILL_COLOR_TOUCH1   = new Color(107, 5, 147, 255);    
    
    // behaviour settings
	public static final int SCROLL_MIN_SPEED          = 3;
	public static final int SCROLL_MAX_SPEED          = 30;
	public static final int TOUCH0_ADDED_COLOR_SPEED  = 10;
	public static final int TOUCH1_ADDED_COLOR_SPEED  = 10;
	public static final int TOUCH_REMOVED_COLOR_SPEED = 10;
	
	public static final float FLICK_SCALAR  = 1.0f;
	public static final float SCALE_SPEED   = 1.05f;  // touch 0 DOWN, range > 1
	public static final float INFLATE_SPEED = 5.0f;   // touch 1 DOWN, range > 0
	public static final float REFORM_SPEED  = 0.3f;   // any touch UP, range > 0
	
	/***************************************************************
	 *  attributes 
	 ***************************************************************/
	
	// text properties
	private Rectangle bounds;
	private PFont font;
	private Book book;

	// I/O
	private TuioProcessing tuioClient;
	private Map<Integer, Touch> touches;
	private PFont debugFont;
	
	/***************************************************************
	 *  setup methods 
	 ***************************************************************/
	
	//--------------------------------------------------------------
	public static void main(String _args[]) 
	{
		if (FULLSCREEN) {
			PApplet.main(new String[] { "--present", "--bgcolor=#000000", "--hide-stop", ncatt.NCAtT.class.getName() });
			if (platform == MACOSX) {
				new JAppleMenuBar().setVisible(false);
			}
		} else {
			PApplet.main(new String[] { ncatt.NCAtT.class.getName() });
		}
	}

	//--------------------------------------------------------------
	public void setup() 
	{
		if (FULLSCREEN) {
			if (platform == WINDOWS) {
				// Windows shows all black when rendering in full screen PRESENT mode,
				// taking a pixel off each dimension seems to fix the issue
				size(1920 - 1, 1080 - 1, OPENGL);
			} else {
				size(1920, 1080, OPENGL);
			}
		} else {
			size(960, 540, OPENGL);
		}

		hint(DISABLE_OPENGL_2X_SMOOTH);
		hint(ENABLE_OPENGL_4X_SMOOTH);
		frameRate(FPS);
		noCursor();

		debugFont = createFont("Courier", 12);

		tuioClient  = new TuioProcessing(this);
		touches = Collections.synchronizedMap(new HashMap<Integer, Touch>());
		TouchAction.touches = touches;

		// init the text
		bounds = new Rectangle(0, 0, width, height);
		font = createFont(FONT_FILE, FONT_SIZE, true);
		textFont(font);
		textAlign(LEFT, BASELINE);
		book = new Book(this, new NCAtTRenderer(this));
		NCAtTGlyph.tesselator = new Tessellator(new GLU(), this.g, 3.0f);
		// The text file is located at bin/data/NCAtT.txt AND src/data/NCAtT.txt
		// Update both when updating the text!!!
		loadText(TEXT_FILE);
	}

	//--------------------------------------------------------------
	public void loadText(String textFile) 
	{
		String[] lines = loadStrings(textFile);
		float lineHeight = height / (float)lines.length;

		for (int i=0; i < lines.length; i++) {
			addLine(lines[i] , i, lineHeight);
		}
	}

	//--------------------------------------------------------------
	public void addLine(String text, int line, float lineHeight) 
	{
		println("Adding line '" + text + "'");
		
		float y = (line+0.75f) * lineHeight;
		
		TextObjectGroup lineGroup = new TextObjectGroup(new PVector(0, y));
		
		// build the TextObject hierarchy
		PVector glyphOffset = new PVector();
		for (int i=0; i < text.length(); i++) {
			// create a new glyph
			String glyphString = text.substring(i, i+1);
			NCAtTGlyph glyph = new NCAtTGlyph(glyphString, font, FONT_SIZE, glyphOffset, 
					FILL_COLOR_IDLE, STROKE_COLOR_IDLE, STROKE_WEIGHT);
			
		    // translate the glyph offset the its width and spacing
		    glyphOffset.add(new PVector((float)glyph.getLogicalBounds().getWidth(), 0, 0));
		    
		    // attach the glyph to the word
		    lineGroup.attachChild(glyph);
		}
		
		book.attachText(lineGroup);
		
		// add the behaviours
		Behaviour lineBehaviour = buildLineBehaviour(line, lineGroup.getBounds().width, lineHeight);
		book.addGroupBehaviour(lineBehaviour);
		Behaviour glyphBehaviour = buildGlyphBehaviour(line, lineGroup.getBounds().width, lineHeight);
		book.addGlyphBehaviour(glyphBehaviour);
		
		lineBehaviour.addObject(lineGroup);
		
		TextObjectGlyphIterator it = lineGroup.glyphIterator();
		while (it.hasNext()) {
			glyphBehaviour.addObject(it.next());
		}
		
		// clean up
		book.removeGroupBehaviour(lineBehaviour);
		book.removeGlyphBehaviour(glyphBehaviour);
	}
	
	//--------------------------------------------------------------
	public Behaviour buildGlyphBehaviour(int line, float lineWidth, float lineHeight) 
	{
		boolean scrollLeft = line%2 == 0;
		Init init = new Init((scrollLeft? -1:1) * SCROLL_MIN_SPEED);
		
		TouchCollider touchCollider0 = new TouchCollider(0);
		touchCollider0.shouldLockOn(true);
		touchCollider0.setLimits(0, (int)(line * lineHeight), width, (int)lineHeight);
		// Touch 0 ADDED Actions
		touchCollider0.addAddedAction(new Repeat(new SetRenderOrder(RENDER_MIDDLE, RENDER_BOTTOM)));
		touchCollider0.addAddedAction(new Brake(0.5f, IMPACT_SIBLINGS));
		// Touch 0 DOWN Actions
		touchCollider0.addDownAction(new Repeat(new Colorize(FILL_COLOR_TOUCH0, TOUCH0_ADDED_COLOR_SPEED, STROKE_COLOR_TOUCH0, TOUCH0_ADDED_COLOR_SPEED)));
		touchCollider0.addDownAction(new Repeat(new Impact(new Scale(SCALE_SPEED), IMPACT_SIBLINGS)));
		touchCollider0.addDownAction(new FollowTouch());
		// Touch 0 REMOVED Actions
		touchCollider0.addRemovedAction(new Repeat(new SetRenderOrder(RENDER_TOP, RENDER_TOP)));
		touchCollider0.addRemovedAction(new Repeat(new Flick(IMPACT_SIBLINGS, FLICK_SCALAR, SCROLL_MAX_SPEED)));
		
		TouchCollider touchCollider1 = new TouchCollider(1);
		touchCollider1.shouldLockOn(false);
		// Touch 1 ADDED Actions
		touchCollider1.addAddedAction(new Repeat(new SetRenderOrder(RENDER_MIDDLE)));
		// Touch 1 DOWN Actions
		touchCollider1.addDownAction(new Repeat(new Colorize(FILL_COLOR_TOUCH1, TOUCH1_ADDED_COLOR_SPEED, STROKE_COLOR_TOUCH1, TOUCH1_ADDED_COLOR_SPEED)));
		touchCollider1.addDownAction(new CounterScroll());
//		touchCollider1.addDownAction(new Drift(1.0f, 10.0f, 0.15f, 0.02f));
		touchCollider1.addDownAction(new Inflate(INFLATE_SPEED, 0.0f, 1.1f, 1.0f));
//		touchCollider1.addDownAction(new Lens(1.0f, 20.0f));
		touchCollider1.addDownAction(new Rubber(10.0f, 10.0f));
		touchCollider1.addDownAction(new Wrap(20.0f, 1.1f, 0.5f, 5.0f, 0.5f));
		// Touch 1 REMOVED Actions
		touchCollider1.addRemovedAction(new Repeat(new Unbrake(IMPACT_SIBLINGS)));
		touchCollider1.addRemovedAction(new Repeat(new SetRenderOrder(RENDER_TOP)));
		
		Multiplexer multiplexer = new Multiplexer();
		multiplexer.add(touchCollider0);
		multiplexer.add(touchCollider1);
		
		Chain chain = new Chain();
		chain.add(init);
		chain.add(multiplexer);
		
		return chain.makeBehaviour();
	}
	
	//--------------------------------------------------------------
	public Behaviour buildLineBehaviour(int line, float lineWidth, float lineHeight) 
	{
		ApplyToGlyph scroll = new ApplyToGlyph(new Scroll(bounds, lineWidth));

		TouchCollider touchColliderAny = new TouchCollider();
		touchColliderAny.setLimits(0, (int)(line * lineHeight), width, (int)lineHeight);
		touchColliderAny.addUpAction(new Repeat(new ApplyToGlyph(new Colorize(FILL_COLOR_IDLE, TOUCH_REMOVED_COLOR_SPEED, STROKE_COLOR_IDLE, TOUCH_REMOVED_COLOR_SPEED))));
		touchColliderAny.addUpAction(new Repeat(new Reform(REFORM_SPEED, Reform.STYLE_LINEAR)));

		Multiplexer multiplexer = new Multiplexer();
		multiplexer.add(scroll); 
		multiplexer.add(touchColliderAny);
		
		return multiplexer.makeBehaviour();
	}
	
	/***************************************************************
	 *  draw loop methods 
	 ***************************************************************/

	//--------------------------------------------------------------
	public void draw() 
	{
		// clear the background
		background(BG_COLOR.getRGB());

		if (DEBUG_FRAMERATE) drawFrameRate();

		book.stepAndDraw();
		
		if (DEBUG_BOUNDS) drawGlyphBounds();
		
		// clear any dead Touches
		synchronized (touches) {
			Iterator<Integer> it = touches.keySet().iterator();
    		while (it.hasNext()) {
    			Touch touch = touches.get(it.next());
//    			if (touch.isDead()) {
//    				if (it.hasNext()) it.next();
//    				System.out.println("Removing Touch for real id=" + touch.getID() + " speed=" + touch.getXSpeed() + " frame=" + frameCount);
//    				touches.remove(touch.getID());
//    			} else if (DEBUG) {
    			if (touch.isAlive() && DEBUG_TOUCHES) {
    				// draw the Touch
    				fill(255, 128);
        			stroke(255, 0, 0);
        			ellipse(touch.getX(), touch.getY(), 50, 50);
    			}
    		}
		}
	}
	
	//--------------------------------------------------------------
	public void drawGlyphBounds() 
	{
		// draw the glyph bounding boxes
		stroke(255);
		noFill();
		TextObjectGlyphIterator i = book.getTextRoot().glyphIterator();
		while (i.hasNext()) {
			TextObjectGlyph tog = i.next();
			Rectangle bounds = tog.getBounds();
			rect(bounds.x, bounds.y, bounds.width, bounds.height);
		}
	}

	//--------------------------------------------------------------
	public void drawFrameRate() 
	{
		noStroke();
		fill(255, 128, 0);
		textFont(debugFont);
		text(frameRate, 5, 15);
	}

	/***************************************************************
	 *  TUIO callback methods 
	 ***************************************************************/

	//--------------------------------------------------------------
	public void addTuioCursor(TuioCursor tcur) 
	{
		Integer id = new Integer(tcur.getCursorID());
		Touch touch = new Touch(tcur.getCursorID(), tcur.getScreenX(width), tcur.getScreenY(height));
		synchronized (touches) {
		    touches.put(id, touch);
		}
	}

	//--------------------------------------------------------------
	public void updateTuioCursor(TuioCursor tcur) 
	{
		Integer id = new Integer(tcur.getCursorID());
		synchronized (touches) {
			Touch touch = touches.get(id);
			if (touch != null) touch.set(tcur.getScreenX(width), tcur.getScreenY(height), tcur.getXSpeed(), tcur.getYSpeed());
		}
	}

	//--------------------------------------------------------------
	public void removeTuioCursor(TuioCursor tcur) 
	{
		Integer id = new Integer(tcur.getCursorID());
		synchronized (touches) {
			Touch touch = touches.get(id);
			if (touch != null) touch.die();
		}
	}
}
