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
package fr.univ_artois.lgi2a.similar2logo.examples.randomwalk.exploration;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.tools.SimulationData;

/**
 * Class for the management of the data of the RandomWalk simulation
 * @author <a href="mailto:ylin.huang@univ-artois.fr">Yu-Lin HUANG</a>
 */
public class SimulationDataRandomWalk extends SimulationData {
	
	/**
	 * The position point of the agent in the simulation.
	 */
	private List<Point2D> positions;
	
	/**
	 * Creates a new simulation data prey predation
	 * @param startTime the time at the beginning of the simulation
	 * @param id the id of the simulation
	 */
	public SimulationDataRandomWalk(SimulationTimeStamp startTime, int id) {
		super(startTime, id);
		this.positions = new ArrayList<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone () {
		SimulationDataRandomWalk sdpp = new SimulationDataRandomWalk(new SimulationTimeStamp(this.currentTime.getIdentifier()), id);
		sdpp.agents = new HashSet<>();
		for (TurtlePLSInLogo turtle : agents) {
			sdpp.agents.add((TurtlePLSInLogo) turtle.clone());
		}
		sdpp.environment = (LogoEnvPLS) this.environment.clone();
		sdpp.currentTime = new SimulationTimeStamp(currentTime.getIdentifier());
		sdpp.endTime = new SimulationTimeStamp(endTime.getIdentifier());
		sdpp.getPositions().addAll(positions);
		return sdpp;
	}

	public List<Point2D> getPositions() {
		return positions;
	}

	public List<Double> getPositionsOfX() {
		List<Double> result = new ArrayList<>();
		for (Point2D position: this.positions) {
			result.add(position.getX());
		}
		return result;
 	}
	
	public String getStringOfPositionX() {
		StringBuilder result = new StringBuilder();
		for (Point2D position: this.positions) {
			result.append(position.getX());
			result.append(" ");	
		}
		return result.toString();
	}
	
	public String getStringOfPositionY() {
		StringBuilder result = new StringBuilder();
		for (Point2D position: this.positions) {
			result.append(position.getY());
			result.append(" ");	
		}
		return result.toString();
	}
	
	public Point2D getCurrentPosition() {
		if (!positions.isEmpty()) {
			return positions.get(positions.size() - 1);
		}
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getData() {
		return "";
	}
}