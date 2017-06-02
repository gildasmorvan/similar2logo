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
import java.util.Map;
import java.util.Random;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.agents.IGlobalState;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.agents.IPerceivedData;
import fr.lgi2a.similar.microkernel.influences.InfluencesMap;
import fr.lgi2a.similar.microkernel.influences.system.SystemInfluenceAddAgent;
import fr.lgi2a.similar2logo.examples.transport.model.Station;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;

/**
 * Agent that creates new agents of every type.
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class CreatorDecisionModel extends AbstractAgtDecisionModel {
	
	private double probaCreateCar;
	
	private double probaCreateTram;
	
	private double probaCreateTrain;
	
	private int height;
	
	private int width;
	
	/**
	 * Limits of each type of way.
	 * The key is the type of way.
	 * The value is a list with all the points limits for this type of way
	 */
	private Map<String,List<Point2D>> limits;
	
	/**
	 * The list of stations/stops for each type of transport.
	 */
	private Map<String,List<Station>> stations;


	public CreatorDecisionModel(double probaCar, double probaTram, double probaTrain,
			int height, int width, Map<String,List<Point2D>> limits, Map<String,List<Station>> stations) {
		super(LogoSimulationLevelList.LOGO);
		this.probaCreateTram = probaTram;
		this.probaCreateCar = probaCar;
		this.probaCreateTrain = probaTrain;
		this.height = height;
		this.width = width;
		this.limits = limits;
		this.stations = stations;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void decide(SimulationTimeStamp timeLowerBound, SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState, ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		double proba = this.probaCreateCar;
		Random r = new Random();
		while (proba >= r.nextFloat()) {
			producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
					generateCarToAdd()));
			proba *= this.probaCreateCar;
		}
		proba = this.probaCreateTram;
		while (proba >= r.nextFloat()) {
			producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
					generateTramToAdd()));
			proba *= this.probaCreateTram;
		}
		proba = this.probaCreateTrain;
		while (proba >= r.nextFloat()) {
			producedInfluences.add(new SystemInfluenceAddAgent(getLevel(), timeLowerBound, timeUpperBound, 
					generateTrainToAdd()));
			proba *= this.probaCreateTrain;
		}
	}
	
	/**
	 * Generate a car to add in the simulation
	 * @return a car to insert in the simulation
	 */
	private IAgent4Engine generateCarToAdd () {
		// We unit the list of the station;
		List<Station> stop = new ArrayList<>();
		for (Station s : stations.get("Railway")) {
			stop.add(s);
		}
		for (Station s : stations.get("Tramway")) {
			stop.add(s);
		}
		Random r = new Random();
		Point2D np = startPosition(limits.get("Street").get(r.nextInt(limits.get("Street").size())));
		return CarFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new CarDecisionModel(0, stop, height, width),
					CarCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					1
				);
	}
	
	/**
	 * Generates a tram to add in the simulation
	 * @return a tram to insert in the simulation
	 */
	private IAgent4Engine generateTramToAdd () {
		Random r = new Random();
		Point2D np = startPosition(limits.get("Tramway").get(r.nextInt(limits.get("Tramway").size())));
			 return TransportFactory.generate(
					new TurtlePerceptionModel(
							Math.sqrt(2),Math.PI,true,true,true
						),
						new TransportDecisionModel("Tramway", limits.get("Tramway"), stations.get("Tramway"), height, width),
						TramCategory.CATEGORY,
						startAngle(np) ,
						0 ,
						0,
						np.getX(),
						np.getY(),
						1,
						1
					);
		}
	
	/**
	 * Generates a train to add in the simulation
	 * @return a train to insert in the simulation
	 */
	private IAgent4Engine generateTrainToAdd() {
		Random r = new Random ();
		Point2D np = startPosition(limits.get("Railway").get(r.nextInt(limits.get("Railway").size())));
		return TransportFactory.generate(
				new TurtlePerceptionModel(
						Math.sqrt(2),Math.PI,true,true,true
					),
					new TransportDecisionModel("Railway", limits.get("Railway"), stations.get("Railway"), height, width),
					TrainCategory.CATEGORY,
					startAngle(np) ,
					0 ,
					0,
					np.getX(),
					np.getY(),
					1,
					1
				);

	}
	
	/**
	 * Gives a position where put a new car
	 * @param position on the edge of the world
	 * @return the position where put the car
	 */
	private Point2D startPosition (Point2D position) {
		if (position.getX() == 0) return new Point2D.Double(position.getX()+1,position.getY());
		else if (position.getY() == 0) return new Point2D.Double(position.getX(),position.getY()+1);
		else if (position.getX() == (width -1)) return new Point2D.Double(position.getX()-1,position.getY());
		else return new Point2D.Double(position.getX(),position.getY()-1);
	}
	
	/**
	 * Gives the angle to give to the new car following its position
	 * @param position the next position of the new car
	 * @return the angle which the car starts
	 */
	private double startAngle (Point2D position) {
		if (position.getX() == 1) {
			return LogoEnvPLS.EAST;
		} else if (position.getY() == 1) {
			return LogoEnvPLS.NORTH;
		} else if (position.getX() == (width -1)) 
			return LogoEnvPLS.WEST;
		else
			return LogoEnvPLS.SOUTH;
	}

}
