package ncatt.behaviour;

import net.nexttext.TextObject;
import net.nexttext.TextObjectGlyphIterator;
import net.nexttext.TextObjectGroup;
import net.nexttext.behaviour.Action;

public class Impact extends NCAtTAction 
{
	private int impact;
	private Action action;
	
	public Impact(Action action, int impact)
	{
		this.action = action;
		this.impact = impact;
	}
	
	public ActionResult behave(TextObject to)
	{
		ActionResult result = new ActionResult();
		
		if (impact == IMPACT_SOLO) {
			result = action.behave(to);
		} 
		else if (impact == IMPACT_PARENT) {
			TextObject parent = to.getParent();
			if (parent != null) {
				// apply the Action on the TextObject's parent
				result = action.behave(parent);
			}
		}
		else if (impact == IMPACT_CHILDREN) {
			// apply the Action on the TextObject's children
			if (to instanceof TextObjectGroup) {
				TextObjectGroup tog = (TextObjectGroup) to;
				TextObject child = tog.getLeftMostChild();
				while (child != null) {
					result.combine(action.behave(child));
					
					child = child.getRightSibling();
				}
				
				result.endCombine();
			}
		}
		else if (impact == IMPACT_SIBLINGS) {
			// apply the Action on the TextObject and all of its siblings
			TextObject sibling = to.getParent().getLeftMostChild();
			while (sibling != null) {
				result.combine(action.behave(sibling));
				
				sibling = sibling.getRightSibling();
			}
			
			result.endCombine();
		} 
		else if (impact == IMPACT_FOLLOWERS) {
			// apply the Action on the TextObect and all of its followers
			int currDir = (int)getDirection(to).get();
			TextObject sibling = to;
			while (sibling != null) {
				action.behave(sibling);

				if (currDir == DIR_LEFT) sibling = sibling.getRightSibling();
				else if (currDir == DIR_RIGHT) sibling = sibling.getLeftSibling();
				else sibling = null;
			}
			
			result.endCombine();
		}
		else {  // IMPACT_ALL
			// get the root TextObject
			TextObject root = to;
			while (root.getParent() != null) {
				root = root.getParent();
			}
			
			// apply the Action on all the TextObjects in the tree
			TextObjectGlyphIterator i = ((TextObjectGroup) root).glyphIterator();
            while (i.hasNext()) {
            	result.combine(action.behave(i.next()));
            }
						
            result.endCombine();
		}
		
		return result;
	}
}
