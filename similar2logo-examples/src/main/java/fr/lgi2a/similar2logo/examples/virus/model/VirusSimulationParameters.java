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
package fr.lgi2a.similar2logo.examples.virus.model;

import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;

/**
 * The parameters of the virus simulation.
 * 
 * @author <a href="mailto:julienjnthn@gmail.com" target="_blank">Jonathan Julien</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class VirusSimulationParameters extends LogoSimulationParameters {
 
	/**
	 * The probability of an agent to be infected by an already infected agent
	 */
	public double probInfected;
	/**
	 * The rate of people infected at the launch of the simulation
	 */
	public double initialInfectionRate ; 
	
	/**
	 * The life time of an uninfected agent (in number of steps)
	 */
	public double lifeTime;
	
	/**
	 * The rate of births per step according to the population 
	 */
	public double birth ; 
	
    /**
	 * The degree of immunity of a person that has been infected and recovered.
	 */
	public double degreeOfImmunity;
	
	 /**
	  * The probability of dying when infected.
	  */
	public double deathProbability;
	
    /**
	 * The number of agents in the simulation.
	 */
	public int nbOfAgents;
	
	 /**
	   * The infection time (in number of steps)
	   */
	public int infectionTime;
	
	/**
	 * Builds an instance of this parameter class.
	 */
	public VirusSimulationParameters() {
		super();
		this.xTorus = true;
		this.yTorus = true;
		this.gridHeight = 20;
		this.gridWidth = 20;
		this.probInfected = 0.05;
		this.initialInfectionRate = 0.3;
		this.lifeTime = 150;
		this.birth = 0.01;
		this.deathProbability = 0.05;
		this.degreeOfImmunity = 0.2;
		this.nbOfAgents = 600 ;
		this.infectionTime = 20;
		
		
		
	}
}

