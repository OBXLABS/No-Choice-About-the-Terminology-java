package ncatt.input;

import processing.core.PVector;
import net.nexttext.Locatable;
import net.nexttext.TextObject;
import net.nexttext.input.InputSource;

//--------------------------------------------------------------
//--------------------------------------------------------------
public class Touch extends InputSource implements Locatable 
{
	private int id;
	private float x, px;
	private float y, py;
	private float xSpeedIn, xSpeedOut;  // buffer these cause the last value is always 0 for some reason
	private float ySpeedIn, ySpeedOut;
	private boolean dead;
	private TextObject to;
	
	/** Build a new Touch. */
	public Touch(int id, float x, float y) 
	{
		this.id = id;
		this.x = this.px = x;
		this.y = this.py = y;
		dead = false;
		to = null;
	}
	
	/** Set the x-position of the Touch. */
    public void setX(float x) {
    	if (this.x == x) return;
    	px = this.x;
    	this.x = x;
    }
    /** Set the y-position of the Touch. */
    public void setY(float y) {
    	if (this.y == y) return;
    	py = this.y;
    	this.y = y; 
    }
    
    /** Set the x-speed of the Touch. */
    public void setXSpeed(float x) { 
    	xSpeedOut = xSpeedIn;
    	xSpeedIn = x; 
    }
    /** Set the y-speed of the Touch. */
    public void setYSpeed(float y) {
    	ySpeedOut = ySpeedIn;
    	ySpeedIn = y; 
    }
    
    /** Set the position and speed of the Touch. */
    public void set(float x, float y, float xSpeed, float ySpeed) { setX(x); setY(y); setXSpeed(xSpeed); setYSpeed(ySpeed); }
	
    /** Get the current x-position of the Touch. */
    public float getX() { return x; }
    /** Get the current y-position of the Touch. */
    public float getY() { return y; }
    
    /** Get the previous x-position of the Touch. */
    public float getPX() { return px; }
    /** Get the previous y-position of the Touch. */
    public float getPY() { return py; }
    
	/** Get the current x-speed of the Touch. */
    public float getXSpeed() { return xSpeedOut; }
    /** Get the current y-speed of the Touch. */
    public float getYSpeed() { return ySpeedOut; }
    
    /** Get the id of the Touch. */
    public int getID() { return id; }
    
    /** Mark the Touch as dead. **/
    public void die() { dead = true; }
    
    /** Get whether or not the Touch is dead. */
    public boolean isDead() { return dead; }
    /** Get whether or not the Touch is alive. */
    public boolean isAlive() { return !dead; }
    
    /** Sets the TextObject of the Touch. */
    public void setTextObject(TextObject to) { this.to = to; }
    
    /** Get the TextObject of the Touch, or null if none is set. */
    public TextObject getTextObject() { return to; }
    /** Get whether or not the Touch has a TextObject. */
    public boolean hasTextObject() { return to != null; }
    
	/** Get the current vector position of the Touch. */
	@Override
	public PVector getLocation() { return new PVector(x, y); }
}
