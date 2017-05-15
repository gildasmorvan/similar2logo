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
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also 
 * provides usage examples.
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
package fr.lgi2a.similar2logo.kernel.model.agents.turtle;

import java.awt.geom.Point2D;

import fr.lgi2a.similar.extendedkernel.agents.ExtendedAgent;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtPerceptionModel;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;
import fr.lgi2a.similar2logo.kernel.model.environment.SituatedEntity;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class TurtlePLSInLogo extends AbstractLocalStateOfAgent implements SituatedEntity, Cloneable {

	/**
	 * The location of the turtle in the grid environment.
	 */
	private Point2D location;
	
	/**
	 * The speed of the turtle in the grid environment.
	 */
	private double speed;
	
	/**
	 * The acceleration of the turtle in the grid environment.
	 */
	private double acceleration;
	
	/**
	 * The direction of the turtle's head in the grid environment
	 * expressed in radian. 0 represents the north of the grid.
	 *  
	 */
	private double direction;
	
	/**
	 * Builds an initialized instance of this public local state.
	 * @param owner The agent owning this public local state.
	 * @param initialX The initial x coordinate of the turtle.
	 * @param initialY The initial y coordinate of the turtle.
	 * @param initialSpeed The initial speed of the turtle.
	 * @param initialAcceleration The initial acceleration of the turtle.
	 * @param initialDirection The initial direction of the turtle.
	 * @throws IllegalArgumentException If intialX and initialY are lower than 0.
	 */
	public TurtlePLSInLogo(
		IAgent4Engine owner,
		double initialX,
		double initialY,
		double initialSpeed,
		double initialAcceleration,
		double initialDirection
	) {
		super(LogoSimulationLevelList.LOGO, owner);
		owner.getCategory();
		if( initialX < 0 || initialY < 0){
			throw new IllegalArgumentException( "The coordinates of a turtle in the grid cannot be negative." );
		} else {
			this.location = new Point2D.Double( initialX, initialY );
		}
		this.speed = initialSpeed;
		this.acceleration = initialAcceleration;
		this.setDirection(initialDirection);
	}

	/**
	 * @return the location
	 */
	public Point2D getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(Point2D location) {
		this.location = location;
	}

	/**
	 * @return the speed
	 */
	public double getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(double speed) {
		this.speed = speed;
	}

	/**
	 * @return the acceleration
	 */
	public double getAcceleration() {
		return acceleration;
	}

	/**
	 * @param acceleration the acceleration to set
	 */
	public void setAcceleration(double acceleration) {
		this.acceleration = acceleration;
	}

	/**
	 * @return the direction
	 */
	public double getDirection() {
		return direction;
	}

	/**
	 * @param direction the direction to set
	 */
	public void setDirection(double direction) {
		this.direction = direction % (2*Math.PI);
	}
	
	@Override
	public Object clone() {
		ExtendedAgent aa = (ExtendedAgent) this.getOwner();
		IAgent4Engine ia4e = TurtleFactory.generate(
				(AbstractAgtPerceptionModel) aa.getPerceptionModel(LogoSimulationLevelList.LOGO),
				(AbstractAgtDecisionModel) aa.getDecisionModel(LogoSimulationLevelList.LOGO),
				this.getCategoryOfAgent(),
				this.direction ,
				this.speed ,
				this.acceleration,
				this.location.getX(),
				this.location.getY()
			);
		return new TurtlePLSInLogo(
			ia4e,
			this.location.getX(),
			this.location.getY(),
			this.speed,
			this.acceleration,
			this.direction
		);
		/*try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			return null;
		}*/
	}
	
	public boolean equals (Object o) {
		if (!(o instanceof TurtlePLSInLogo)) {
			return false;
		} else {
			TurtlePLSInLogo tpil = (TurtlePLSInLogo) o;
			return (tpil.getLocation().equals(location) && (tpil.getAcceleration() == acceleration) 
					&& (tpil.getDirection() == direction) && (tpil.getSpeed() == speed) && tpil.getOwner().equals(this.getOwner()));
		}
	}
	
}
