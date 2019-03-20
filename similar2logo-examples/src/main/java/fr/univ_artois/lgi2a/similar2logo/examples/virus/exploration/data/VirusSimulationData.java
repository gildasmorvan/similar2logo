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
package fr.univ_artois.lgi2a.similar2logo.examples.virus.exploration.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.examples.virus.model.PersonPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.lib.exploration.tools.SimulationData;

/**
 * Class for the management of the data of the PreyPredator simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 */
public class VirusSimulationData extends SimulationData {
	
	/**
	 * The number of infected agents in the simulation.
	 */
	private List<Integer> nbOfInfectedAgents;
	
	/**
	 * The number of immune agents in the simulation.
	 */
	private List<Integer> nbOfImmuneAgents;
	
	/**
	 * The number of never infected agents in the simulation.
	 */
	private List<Integer> nbOfNeverInfectedAgents;
	
	/**
	 * The weight of current simulation.
	 * Set to float type to facilitate Python communication.
	 */
	private float weight;
	
	/**
	 * Creates a new virus simulation data
	 * @param startTime The initial time of the simulation.
	 * @param id The id of the simulation
	 */
	public VirusSimulationData(SimulationTimeStamp startTime, int id) {
		super(startTime, id);
		this.nbOfInfectedAgents = new ArrayList<>();
		this.nbOfImmuneAgents = new ArrayList<>();
		this.nbOfNeverInfectedAgents = new ArrayList<>();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object clone () {
		VirusSimulationData sdpp = new VirusSimulationData(new SimulationTimeStamp(this.currentTime.getIdentifier()), id);
		sdpp.agents = new HashSet<>();
		for (TurtlePLSInLogo turtle : agents) {
			sdpp.agents.add((PersonPLS) turtle.clone());
		}
		sdpp.environment = (LogoEnvPLS) this.environment.clone();
		sdpp.currentTime = new SimulationTimeStamp(currentTime.getIdentifier());
		sdpp.endTime = new SimulationTimeStamp(endTime.getIdentifier());
		sdpp.nbOfInfectedAgents = new ArrayList<>(nbOfInfectedAgents);
		sdpp.nbOfImmuneAgents = new ArrayList<>(nbOfImmuneAgents);
		sdpp.nbOfNeverInfectedAgents = new ArrayList<>(nbOfNeverInfectedAgents);
		sdpp.weight = weight;
		return sdpp;
	}

	/**
	 * @return the nbOfInfectedAgents
	 */
	public List<Integer> getNbOfInfectedAgents() {
		return nbOfInfectedAgents;
	}

	/**
	 * @return the nbOfImmuneAgents
	 */
	public List<Integer> getNbOfImmuneAgents() {
		return nbOfImmuneAgents;
	}

	/**
	 * @return the nbOfNeverInfectedAgents
	 */
	public List<Integer> getNbOfNeverInfectedAgents() {
		return nbOfNeverInfectedAgents;
	}

	/**
	 * @return the last nbOfInfectedAgents
	 */
	public int getLastNbOfInfectedAgents() {
		return nbOfInfectedAgents.get(nbOfInfectedAgents.size() - 1);
	}

	/**
	 * @return the last nbOfImmuneAgents
	 */
	public int getLastNbOfImmuneAgents() {
		return nbOfImmuneAgents.get(nbOfImmuneAgents.size() - 1);
	}

	/**
	 * @return the last nbOfNeverInfectedAgents
	 */
	public double getLastNbOfNeverInfectedAgents() {
		return nbOfNeverInfectedAgents.get(nbOfNeverInfectedAgents.size() - 1);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getData() {
		return id+" "+getLastNbOfInfectedAgents()+" "+getLastNbOfImmuneAgents()+" "+getLastNbOfNeverInfectedAgents()+" "+getWeight();

	}

	/**
	 * @return the weight of the simulation.
	 */
	public float getWeight() {
		return weight;
	}

	/**
	 * Set the weight of the simulation.
	 * @param weight The weight of the simulation.
	 */
	public void setWeight(float weight) {
		this.weight = weight;
	}
}
