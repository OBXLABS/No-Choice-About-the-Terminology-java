package ncatt.tesselator;

public class TessContourVertex 
{
	public int segType;
	public float x, y;
	
	public TessContourVertex(int segType) {
		this.segType = segType;
	}
	
	public TessContourVertex(int segType, double[] vertex) {
		this(segType);
		x = (float) vertex[0];
		y = (float) vertex[1];
	}
}
