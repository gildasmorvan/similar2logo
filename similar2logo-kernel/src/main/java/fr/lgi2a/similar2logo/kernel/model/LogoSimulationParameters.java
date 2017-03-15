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
package fr.lgi2a.similar2logo.kernel.model;

import java.util.LinkedHashSet;
import java.util.Set;

import fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractSimulationParameters;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class LogoSimulationParameters extends AbstractSimulationParameters {
	
	
	/**
	 * The final step of the simulation.
	 */
	@Parameter(
	   name = "final step", 
	   description = "the final step of the simulation"
	)
	public int finalStep = 100000;

	/**
	 * The final time stamp of the simulation.
	 */
	public SimulationTimeStamp finalTime = new SimulationTimeStamp(finalStep);
	
	/**
	 * Defines the width of the environment grid.
	 */
	@Parameter(
	   name = "grid width", 
	   description = "the width of the environment grid"
	)
	public int gridWidth;
	/**
	 * Defines the height of the environment grid.
	 */
	@Parameter(
	   name = "grid height", 
	   description = "the height of the environment grid"
	)
	public int gridHeight;
	
	/**
	 * Defines the topoogy of the environment grid (toroidal or bounded) along the x axis.
	 * 
	 */
	@Parameter(
	   name = "x torus", 
	   description = "the topoogy of the environment grid (toroidal or bounded) along the x axis"
	)
	public boolean xTorus;
	
	/**
	 * Defines the topoogy of the environment grid (toroidal or bounded) along the y axis.
	 */
	@Parameter(
	   name = "y torus", 
	   description = "the topoogy of the environment grid (toroidal or bounded) along the y axis"
	)
	public boolean yTorus;
	
	/**
	 * Defines the set of pheromones used in the simulation
	 */
	public Set<Pheromone> pheromones;
	
	/**
	 * Builds a parameters set containing default values.
	 */
	public LogoSimulationParameters() {
		super(new SimulationTimeStamp(0));
		this.gridWidth = 100;
		this.gridHeight = 100;
		this.xTorus = true;
		this.yTorus = true;
		this.pheromones = new LinkedHashSet<Pheromone>();
	}

}
