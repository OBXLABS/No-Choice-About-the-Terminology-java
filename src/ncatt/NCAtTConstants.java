package ncatt;

public interface NCAtTConstants 
{
	public static final int DIR_LEFT  = -1;
	public static final int DIR_NONE  =  0;
	public static final int DIR_RIGHT =  1;
	
	public static final int IMPACT_SOLO 	 = 0;
	public static final int IMPACT_PARENT	 = 1;
	public static final int IMPACT_CHILDREN  = 2;
	public static final int IMPACT_SIBLINGS  = 3;
	public static final int IMPACT_FOLLOWERS = 4;
	public static final int IMPACT_ALL	     = 5;
	
	public static final int RENDER_ANY    = -1;
	public static final int RENDER_TOP    =  0;
	public static final int RENDER_MIDDLE =  1;
	public static final int RENDER_BOTTOM =  2;
}
