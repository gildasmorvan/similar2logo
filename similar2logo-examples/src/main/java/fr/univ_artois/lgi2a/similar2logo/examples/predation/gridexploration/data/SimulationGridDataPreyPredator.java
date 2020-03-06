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
package fr.univ_artois.lgi2a.similar2logo.examples.predation.gridexploration.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.tools.SimulationData;

/**
 * Class for the management of the data of the PreyPredator simulation as a grid
 *	@author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 */
public class SimulationGridDataPreyPredator extends SimulationData {
	
	/**
	 * The number of preys per patch.
	 */
	private List<Integer[][]> nbOfPreys;
	
	/**
	 * The mean energy of preys per patch.
	 */
	private List<Double[][]> preyEnergy;
	
	/**
	 * The mean age of preys per patch.
	 */
	private List<Double[][]> preyAge;
	
	/**
	 * The number of predators per patch.
	 */
	private List<Integer[][]> nbOfPredators;

	/**
	 * The mean age of predators per patch.
	 */
	private List<Double[][]> predatorAge;
	
	/**
	 * The mean energy of predators per patch.
	 */
	private List<Double[][]> predatorEnergy;
	
	/**
	 * The quantity of grass per patch.
	 */
	private List<Double[][]> nbOfGrass;

	
	
	/**
	 * The quantity of weight of current simulation.
	 * Set to float type to facilitate Python communication.
	 */
	private float weight;
	
	/**
	 * Creates a new simulation data prey predation
	 * @param startTime the time at the beginning of the simulation
	 * @param id the id of the simulation
	 */
	public SimulationGridDataPreyPredator(SimulationTimeStamp startTime, int id) {
		super(startTime, id);

		this.nbOfPreys = new ArrayList<>();
		this.preyEnergy = new ArrayList<>();
		this.preyAge = new ArrayList<>();
		this.nbOfPredators = new ArrayList<>();
		this.predatorEnergy = new ArrayList<>();
		this.predatorAge = new ArrayList<>();
		this.nbOfGrass = new ArrayList<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone () {
		SimulationGridDataPreyPredator sdpp = new SimulationGridDataPreyPredator(new SimulationTimeStamp(this.currentTime.getIdentifier()), id);
		sdpp.agents = new HashSet<>();
		for (TurtlePLSInLogo turtle : agents) {
			sdpp.agents.add((TurtlePLSInLogo) turtle.clone());
		}
		sdpp.environment = (LogoEnvPLS) this.environment.clone();
		sdpp.currentTime = new SimulationTimeStamp(currentTime.getIdentifier());
		sdpp.endTime = new SimulationTimeStamp(endTime.getIdentifier());
		sdpp.nbOfGrass = new ArrayList<>(nbOfGrass);
		sdpp.nbOfPreys = new ArrayList<>(nbOfPreys);
		sdpp.preyEnergy = new ArrayList<>(preyEnergy);
		sdpp.preyAge = new ArrayList<>(preyAge);
		sdpp.predatorEnergy = new ArrayList<>(predatorEnergy);
		sdpp.predatorAge = new ArrayList<>(predatorAge);
		sdpp.nbOfPredators = new ArrayList<>(nbOfPredators);
		
		sdpp.weight = weight;
		return sdpp;
	}

	public List<Integer[][]> getNbOfPreys() {
		return nbOfPreys;
	}

	public List<Integer[][]> getNbOfPredators() {
		return nbOfPredators;
	}

	public List<Double[][]> getNbOfGrass() {
		return nbOfGrass;
	}
	
	
	public List<Double[][]> getPreyEnergy() {
		return preyEnergy;
	}

	public List<Double[][]> getPreyAge() {
		return preyAge;
	}

	public List<Double[][]> getPredatorAge() {
		return predatorAge;
	}

	public List<Double[][]> getPredatorEnergy() {
		return predatorEnergy;
	}

	public String getLastNbOfPreys() {
		StringBuilder sb = new StringBuilder();
		Integer[][] data = nbOfPreys.get(nbOfPreys.size() - 1);
		for(int x = 0; x < data.length; x++) {
			for(int y = 0; y < data[x].length; y++) {
				sb.append( data[x][y]+ " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getLastPreyEnergy() {
		StringBuilder sb = new StringBuilder();
		Double[][] data = preyEnergy.get(preyEnergy.size() - 1);
		for(int x = 0; x < data.length; x++) {
			for(int y = 0; y < data[x].length; y++) {
				sb.append( data[x][y]+ " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getLastPredatorEnergy() {
		StringBuilder sb = new StringBuilder();
		Double[][] data = predatorEnergy.get(predatorEnergy.size() - 1);
		for(int x = 0; x < data.length; x++) {
			for(int y = 0; y < data[x].length; y++) {
				sb.append( data[x][y]+ " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getLastPreyAge() {
		StringBuilder sb = new StringBuilder();
		Double[][] data = preyAge.get(preyAge.size() - 1);
		for(int x = 0; x < data.length; x++) {
			for(int y = 0; y < data[x].length; y++) {
				sb.append( data[x][y]+ " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	public String getLastPredatorAge() {
		StringBuilder sb = new StringBuilder();
		Double[][] data = predatorAge.get(predatorAge.size() - 1);
		for(int x = 0; x < data.length; x++) {
			for(int y = 0; y < data[x].length; y++) {
				sb.append( data[x][y]+ " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	

	public String getLastNbOfPredators() {
		StringBuilder sb = new StringBuilder();
		Integer[][] data = nbOfPredators.get(nbOfPredators.size() - 1);
		for(int x = 0; x < data.length; x++) {
			for(int y = 0; y < data[x].length; y++) {
				sb.append( data[x][y]+ " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public String getLastNbOfGrass() {
		StringBuilder sb = new StringBuilder();
		Double[][] data = nbOfGrass.get(nbOfGrass.size() - 1);
		for(int x = 0; x < data.length; x++) {
			for(int y = 0; y < data[x].length; y++) {
				sb.append( data[x][y]+ " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public float getWeight() {
		return weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}
	
}
