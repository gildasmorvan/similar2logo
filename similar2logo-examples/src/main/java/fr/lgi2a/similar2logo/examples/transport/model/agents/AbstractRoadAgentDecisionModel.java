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
package fr.lgi2a.similar2logo.examples.transport.model.agents;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.examples.transport.model.agents.rail.AbstractTransportAgentDecisionModel;
import fr.lgi2a.similar2logo.examples.transport.model.places.AbstractLeisure;
import fr.lgi2a.similar2logo.examples.transport.model.places.Station;
import fr.lgi2a.similar2logo.examples.transport.model.places.World;
import fr.lgi2a.similar2logo.examples.transport.parameters.DestinationGenerator;
import fr.lgi2a.similar2logo.examples.transport.time.TransportParametersPlanning;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePerceivedData.LocalPerceivedData;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.tools.MathUtil;

import static fr.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

/**
 * Abstract class for the road agents decision model
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public abstract class AbstractRoadAgentDecisionModel extends AbstractTransportAgentDecisionModel {
	
	/**
	 * The parameters planning
	 */
	protected TransportParametersPlanning planning;
	
	/**
	 * The way the person has to take for reaching his destination
	 */
	protected List<Point2D> way;
	
	/**
	 * The destination generator
	 */
	protected DestinationGenerator destinationGenerator;
	
	/**
	 * The moment when the agent has been created
	 */
	protected SimulationTimeStamp birthDate;

	public AbstractRoadAgentDecisionModel(
		Point2D destination,
		World world ,
		SimulationTimeStamp bd,
		TransportParametersPlanning tpp,
		List<Point2D> way,
		DestinationGenerator dg
	) {
		super(destination, world);
		this.planning = tpp;
		this.way = way;
		this.destinationGenerator = dg;
		this.birthDate = bd;
	}
	
	/**
	 * Indicates if the agent is somewhere where the passenger can take a transport.
	 * @param position where is the agent
	 * @return true if there is an access toward a station/stop, else false.
	 */
	protected boolean inStation (Point2D position) {
		for (Station s : this.world.getStations()) {
			if (s.getAccess().equals(position)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Indicates if the agent is in a leisure place
	 * @param position the current position of the agent
	 * @return true if the agent is in a leisure place, false else
	 */
	protected boolean inLeisure (Point2D position) {
		for (AbstractLeisure l : this.world.getLeisures()) {
			if (l.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Indicates if the car is in a dead end.
	 * @param position the position of the car
	 * @param data the data perceived by the car
	 * @return true if the car is in a dead end, false else
	 */
	@SuppressWarnings("rawtypes")
	protected boolean inDeadEnd (Point2D position, TurtlePerceivedData data) {
		for( LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if  (STREET.equals(perceivedMarks.getContent().getCategory())
			 && (perceivedMarks.getDistanceTo() > 0)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Gives a direction to the agent to take
	 * @param position the current position of the agent
	 * @param data the data perceived by the agent
	 * @return the direction where the agent has to go.
	 */
	@SuppressWarnings("rawtypes")
	protected double getDirection (Point2D position, TurtlePerceivedData data) {
		Point2D obj = destination;
		if (way.size() >1) {
			obj = way.get(0);
		}
		List<Double> directions = new ArrayList<>();
		for( LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if  (STREET.equals(perceivedMarks.getContent().getCategory())
			 && (perceivedMarks.getDistanceTo() > 0)) {
				directions.add(perceivedMarks.getDirectionTo());
			}
		}
		double dis = obj.distance(nextPosition(position, directions.get(0)));
		double direction = directions.get(0);
		for (Double d : directions) {
			double newDis = obj.distance(nextPosition(position, d));
			if (newDis < dis) {
				dis = newDis;
				direction = d;
			}
		}
		return direction;
	}

	/**
	 * Gives a new direction for when the agent reaches a step
	 * @param position the start position of the agents and the persons
	 * @param firstStep the first position where they have to go
	 * @return the direction they have to go
	 */
	protected double getDirectionForNextStep (Point2D position, Point2D firstStep) {
		double[] starts = {
			LogoEnvPLS.EAST,
			LogoEnvPLS.NORTH,
			LogoEnvPLS.NORTH_EAST,
			LogoEnvPLS.NORTH_WEST,
			LogoEnvPLS.SOUTH,
			LogoEnvPLS.SOUTH_EAST,
			LogoEnvPLS.SOUTH_WEST,
			LogoEnvPLS.WEST
		};
		int ind = 0;
		double dis = nextPosition(position, starts[0]).distance(firstStep);
		for (int i =1; i < starts.length; i++) {
			double newDis = nextPosition(position, starts[i]).distance(firstStep);
			if (newDis < dis) {
				dis = newDis;
				ind = i;
			}
		}
		return starts[ind];
	}
	
	/**
	 * Indicates if a point is on the border or not
	 * @param pt the point
	 * @return true if the point is on the border, false else
	 */
	protected boolean onTheBorder (Point2D pt) {
		return MathUtil.areEqual(pt.getX(), 0) 
			|| MathUtil.areEqual(pt.getY(), 0)
			|| MathUtil.areEqual(pt.getX(), world.getWidth()-1.0)
			|| MathUtil.areEqual(pt.getY(), world.getHeight()-1.0);
	}
	
	/**
	 * Gives the station where is the agent
	 * @param position the position of the agent
	 * @return the Station that has an access in position. Returns null if the car isn't in station.
	 */
	protected Station findStation (Point2D position) {
		for (Station s : this.world.getStations()) {
			if (s.getAccess().equals(position)) {
				return s;
			}
		}
		return null;
	}
	
	/**
	 * Gives the leisure place where is the agent
	 * @param position the position of the agent
	 * @return the Leisure place that is in this position
	 */
	protected AbstractLeisure findLeisure (Point2D position) {
		for (AbstractLeisure l : this.world.getLeisures()) {
			if (l.getPosition().equals(position)) {
				return l;
			}
		}
		return null;
	}
	
	/**
	 * Gives the factor to apply to each rode
	 * @param position the car position
	 * @param data the data perceived by the car
	 * @return the frequency of the road
	 */
	protected double getRoadFactor (Point2D position, TurtlePerceivedData data) {
		for (@SuppressWarnings("rawtypes") LocalPerceivedData<Mark> perceivedMarks : data.getMarks()) {
			if (STREET.equals(perceivedMarks.getContent().getCategory())
			  && perceivedMarks.getContent().getLocation().equals(position)) {
				Double d = (double) perceivedMarks.getContent().getContent();
				return d.doubleValue();
			}
		}
		return 1;
	}
	
	/**
	 * Gives the new frequency
	 * @param currentFrequency the current frequency
	 * @param factor the factor of the road
	 * @return the new frequency
	 */
	protected double getNewFrequency (double currentFrequency, double factor) {
		double res = Math.floor(currentFrequency*factor*10);
		return res/10;
	}

	/**
	 * @return the way the person has to take for reaching his destination
	 */
	public List<Point2D> getWay() {
		return way;
	}

	/**
	 * @param way the way the person has to take for reaching his destination
	 */
	public void setWay(List<Point2D> way) {
		this.way = way;
	}
	
}
