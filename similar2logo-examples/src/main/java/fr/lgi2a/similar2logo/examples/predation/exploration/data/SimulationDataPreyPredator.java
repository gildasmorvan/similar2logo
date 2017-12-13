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
package fr.lgi2a.similar2logo.examples.predation.exploration.data;

import java.util.HashSet;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.lib.exploration.tools.SimulationData;

/**
 * Class for the management of the data of the PreyPredator simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class SimulationDataPreyPredator extends SimulationData implements Cloneable {
	
	/**
	 * The number of preys in the simulation.
	 */
	private int nbOfPreys;
	
	/**
	 * The number of predators in the simulation.
	 */
	private int nbOfPredators;
	
	/**
	 * The quantity of grass in the simulation.
	 */
	private double nbOfGrass;
	
	/**
	 * The previous number of preys in the simulation.
	 */
	private int lastNbOfPreys;
	
	/**
	 * The previous number of predators in the simulation.
	 */
	private int lastNbOfPredators;
	
	/**
	 * The previous quantity of grass in the simulation.
	 */
	private double lastNbOfGrass;
	
	/**
	 * Creates a new simulation data prey predation
	 * @param startTime the time at the beginning of the simulation
	 */
	public SimulationDataPreyPredator(SimulationTimeStamp startTime) {
		super(startTime);
		this.nbOfPreys =0;
		this.nbOfPredators =0;
		this.nbOfGrass =0;
		this.lastNbOfPreys =0;
		this.lastNbOfPredators =0;
		this.lastNbOfGrass =0;
	}
	
	public int getNbOfPreys() {
		return nbOfPreys;
	}

	public void setNbOfPreys(int nbOfPreys) {
		this.nbOfPreys = nbOfPreys;
	}

	public int getNbOfPredators() {
		return nbOfPredators;
	}

	public void setNbOfPredators(int nbOfPredators) {
		this.nbOfPredators = nbOfPredators;
	}

	public double getNbOfGrass() {
		return nbOfGrass;
	}

	public void setNbOfGrass(double nbOfGrass) {
		this.nbOfGrass = nbOfGrass;
	}

	public int getLastNbOfPreys() {
		return lastNbOfPreys;
	}

	public void setLastNbOfPreys(int lastNbOfPreys) {
		this.lastNbOfPreys = lastNbOfPreys;
	}

	public int getLastNbOfPredators() {
		return lastNbOfPredators;
	}

	public void setLastNbOfPredators(int lastNbOfPredators) {
		this.lastNbOfPredators = lastNbOfPredators;
	}

	public double getLastNbOfGrass() {
		return lastNbOfGrass;
	}

	public void setLastNbOfGrass(double lastNbOfGrass) {
		this.lastNbOfGrass = lastNbOfGrass;
	}

	public String getData () {
		return nbOfPreys+" "+nbOfPredators+" "+nbOfGrass+" "+(nbOfPreys-lastNbOfPreys)+" "+(nbOfPredators-lastNbOfPredators);
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object clone () {
		SimulationDataPreyPredator sdpp = new SimulationDataPreyPredator(new SimulationTimeStamp(this.currentTime.getIdentifier()));
		sdpp.agents = new HashSet<>();
		for (TurtlePLSInLogo turtle : agents) {
			sdpp.agents.add((TurtlePLSInLogo) turtle.clone());
		}
		sdpp.environment = (LogoEnvPLS) this.environment.clone();
		sdpp.currentTime = new SimulationTimeStamp(currentTime.getIdentifier());
		sdpp.endTime = new SimulationTimeStamp(endTime.getIdentifier());
		sdpp.nbOfGrass = nbOfGrass;
		sdpp.nbOfPreys = nbOfPreys;
		sdpp.nbOfPredators = nbOfPredators;
		sdpp.lastNbOfGrass = lastNbOfGrass;
		sdpp.lastNbOfPreys = lastNbOfPreys;
		sdpp.lastNbOfPredators = lastNbOfPredators;
		return sdpp;
	}

}
