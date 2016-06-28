package fr.lgi2a.similar2logo.lib.mecsyco;

import fr.lgi2a.similar.microkernel.IProbe;

/**
 * A probe thats returns "X", "Y" and "Z" data.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public interface IMecsycoProbe extends IProbe {

	/**
	 * @return "X" variable
	 */
	double getX();
	
	/**
	 * @return "Y" variable
	 */
	double getY();
	
	
	/**
	 * @return "Z" variable
	 */
	double getZ();
	
}
