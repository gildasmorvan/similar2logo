package fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail;

import fr.univ_artois.lgi2a.similar2logo.examples.transport.osm.OSMConstants;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;

/**
 * The wagon category for the transport simulation
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class WagonCategory {
	
	/**
	 * The category of the base.
	 */
	public static final AgentCategory CATEGORY = new AgentCategory(OSMConstants.WAGON, TurtleAgentCategory.CATEGORY);

}
