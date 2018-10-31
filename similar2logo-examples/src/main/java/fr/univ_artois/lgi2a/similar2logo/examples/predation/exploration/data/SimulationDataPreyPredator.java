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
package fr.univ_artois.lgi2a.similar2logo.examples.predation.exploration.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.tools.SimulationData;

/**
 * Class for the management of the data of the PreyPredator simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class SimulationDataPreyPredator extends SimulationData {
	
	/**
	 * The number of preys in the simulation.
	 */
	private List<Integer> nbOfPreys;
	
	/**
	 * The number of predators in the simulation.
	 */
	private List<Integer> nbOfPredators;
	
	/**
	 * The quantity of grass in the simulation.
	 */
	private List<Double> nbOfGrass;
	
	/**
	 * Creates a new simulation data prey predation
	 * @param startTime the time at the beginning of the simulation
	 * @param id the id of the simulation
	 */
	public SimulationDataPreyPredator(SimulationTimeStamp startTime, int id) {
		super(startTime, id);
		this.nbOfPreys = new ArrayList<>();
		this.nbOfPredators = new ArrayList<>();
		this.nbOfGrass = new ArrayList<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone () {
		SimulationDataPreyPredator sdpp = new SimulationDataPreyPredator(new SimulationTimeStamp(this.currentTime.getIdentifier()), id);
		sdpp.agents = new HashSet<>();
		for (TurtlePLSInLogo turtle : agents) {
			sdpp.agents.add((TurtlePLSInLogo) turtle.clone());
		}
		sdpp.environment = (LogoEnvPLS) this.environment.clone();
		sdpp.currentTime = new SimulationTimeStamp(currentTime.getIdentifier());
		sdpp.endTime = new SimulationTimeStamp(endTime.getIdentifier());
		sdpp.nbOfGrass = new ArrayList<>(nbOfGrass);
		sdpp.nbOfPreys = new ArrayList<>(nbOfPreys);
		sdpp.nbOfPredators = new ArrayList<>(nbOfPredators);
		return sdpp;
	}

	public List<Integer> getNbOfPreys() {
		return nbOfPreys;
	}

	public List<Integer> getNbOfPredators() {
		return nbOfPredators;
	}

	public List<Double> getNbOfGrass() {
		return nbOfGrass;
	}

	public int getLastNbOfPreys() {
		return nbOfPreys.get(nbOfPreys.size() - 1);
	}

	public int getLastNbOfPredators() {
		return nbOfPredators.get(nbOfPredators.size() - 1);
	}

	public double getLastNbOfGrass() {
		return nbOfGrass.get(nbOfGrass.size() - 1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getData() {
		return id+" "+getLastNbOfPreys()+" "+getLastNbOfPredators()+" "+getLastNbOfGrass();

	}
	
	/**
	 * @param step the number of steps
	 * @return the difference between the number of preys at the current step and step steps ago.
	 */
	public int getDNbOfPreys(int step) {
		if (step == nbOfPredators.size()) {
			return getLastNbOfPreys() - nbOfPreys.get(0);
		}
		return getLastNbOfPreys() - nbOfPreys.get(nbOfPredators.size() - step - 1);
	}
	
	/**
	 * @param step the number of steps
	 * @return the difference between the number of predators at the current step and step steps ago.
	 */
	public int getDNbOfPredators(int step) {
		if (step == nbOfPredators.size()) {
			return getLastNbOfPredators() - nbOfPredators.get(0);
		}
		return getLastNbOfPredators() - nbOfPredators.get(nbOfPredators.size() - step - 1);
	}
	
	/**
	 * @param step the number of steps
	 * @return the difference between the quantity of grass at the current step and step steps ago.
	 */
	public double getDQuantityOfGrass(int step) {
		if (step == nbOfGrass.size()) {
			return getLastNbOfGrass() - nbOfGrass.get(0);
		}
		return getLastNbOfGrass() - nbOfGrass.get(nbOfGrass.size() - step - 1);
	}
}
