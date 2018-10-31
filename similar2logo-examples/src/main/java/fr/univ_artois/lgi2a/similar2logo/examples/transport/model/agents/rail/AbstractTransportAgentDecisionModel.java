/**
 * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
package fr.univ_artois.lgi2a.similar2logo.examples.transport.model.agents.rail;

import java.awt.geom.Point2D;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil;

/**
 * Abstract class for the transport simulation agent
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public abstract class AbstractTransportAgentDecisionModel extends AbstractAgtDecisionModel {
	
	/**
	 * The destination of the agent
	 */
	protected Point2D destination;
	
	protected World world;

	public AbstractTransportAgentDecisionModel(Point2D destination, World world) {
		super(LogoSimulationLevelList.LOGO);
		this.destination = destination;
		this.world = world;
	}
	
	/**
	 * Gives the distance to do following the direction of the agent
	 * @param radius direction of the agent
	 * @return the distance to do
	 */
	protected double distanceToDo (double radius) {
		if (MathUtil.areEqual(radius % (Math.PI/2), 0)) {
			return 1;
		} else {
			return Math.sqrt(2);
		}
	}
	
	/**
	 * Indicates if the transport will leave the map
	 * @param currentPosition current position of the transport
	 * @param direction current direction of the transport
	 * @return true if the transport will leave the map, false else
	 */
	protected boolean willGoOut (Point2D currentPosition, double direction) {
		Point2D p = nextPosition(currentPosition, direction);
		return ((p.getX() < 0) || (p.getX() >= world.getWidth()) || (p.getY() <0) || (p.getY() >= world.getHeight()));
	}
	
	/**
	 * Gives the next position of a agent following its position and its direction
	 * @param position the current position of the agent
	 * @param direction the direction of the agent
	 * @return the next position of the agent
	 */
	protected Point2D nextPosition (Point2D position, double direction) {
		int x,y;
		if (direction < 0) {
			x = 1;
		} else if (MathUtil.areEqual(direction, LogoEnvPLS.NORTH) || MathUtil.areEqual(direction, LogoEnvPLS.SOUTH)) {
			x = 0;
		} else {
			x = -1;
		}
		if ((direction >= LogoEnvPLS.NORTH_EAST) && (direction <= LogoEnvPLS.NORTH_WEST)) {
			y = 1;
		} else if (MathUtil.areEqual(direction, LogoEnvPLS.WEST) || MathUtil.areEqual(direction, LogoEnvPLS.EAST)) {
			y = 0;
		} else {
			y = -1;
		}
		Point2D res = new Point2D.Double(position.getX()+x,position.getY()+y);
		return res;
	}

	/**
	 * @return the destination of the agent
	 */
	public Point2D getDestination() {
		return destination;
	}

}
