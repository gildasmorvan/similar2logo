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
package fr.univ_artois.lgi2a.similar2logo.examples.fire.model;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.annotations.Parameter;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;

/**
 * The main class of the "Fire" simulation.
 * 
 * @author <a xavier_szkudlarek@univ-artois.fr target="_blank">Szkudlarek Xavier</a>
 *       
 *
 */
public class FireForestParameters extends LogoSimulationParameters{
	
	/**
	 * Density of trees in the simulation
	 */
	@Parameter(
	name = "Density of Trees",
			description = "Initial Density of trees in the simulation"
	)
	public double initialTreeDensity; 
	
	/**
	 * Fire Propagation Probability
	 */
	@Parameter(
	name = "Fire Propagation ",
			description = "Fire Propagation "
	)
	public double firePropagationProba; 
	
	/**
	 * Speed of combustion to Trees
	 */
	@Parameter(
	name = "Combustion Speed",
			description = "Combustion Speed"
	)
	public double combustionSpeed ; 
	
	/**
	 * Constructor of the fire and tree parameters
	 */
	public FireForestParameters(){
		super();
		this.initialTreeDensity = 70;
		this.firePropagationProba  = 0.1;
		this.combustionSpeed  = 0.1;
		this.xTorus = false;
		this.yTorus = false;
	}
	
}
