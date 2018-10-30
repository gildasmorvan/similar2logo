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
package fr.lgi2a.similar2logo.examples.heatbugs.model.agents;

import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfAgent;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * The private local state of the "heat bug" agent in the "Logo" level.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class HeatBugHLS extends AbstractLocalStateOfAgent {

	/**
	 * The ideal temperature for this heat bug.
	 */
	private final double optimalTemperature;
	
	/**
	 * The heat that this heat bug generate each cycle.
	 */
	private final double outputHeat;
	
	/**
	 * The relative difference between real and optimal temperature that triggers moves.
	 */
	private final double unhappiness;
	
	/**
	 * The chance that a bug will make a random move even if it would prefer to
	 * stay where it is.
	 */
	private final double randomMoveProbability;
	
	/**
	 * Builds an initialized instance of this private local state.
	 * @param owner The agent owning this private local state.
	 * @param optimalTemperature The ideal temperatures for this heatbug.
	 * @param outputHeat The heat that heatbugs generate each cycle.
	 * @param unhappiness The relative difference between real and optimal
	 * temperature that triggers moves.
	 * @param randomMoveProbability The chance that a bug will make a random
	 * move even if it would prefer to stay where it is (because no more ideal
	 * patch is available).
	 */
	public HeatBugHLS(
		IAgent4Engine owner,
		double optimalTemperature,
		double outputHeat,
		double unhappiness,
		double randomMoveProbability
	) {
		super(
			LogoSimulationLevelList.LOGO,
			owner
		);
		this.optimalTemperature = optimalTemperature;
		this.outputHeat = outputHeat;
		this.unhappiness = unhappiness;
		this.randomMoveProbability = randomMoveProbability;
	}

	/**
	 * @return the heat that this heat bug generate each cycle.
	 */
	public double getOutputHeat() {
		return outputHeat;
	}

	/**
	 * @return the ideal temperature for this heat bug.
	 */
	public double getOptimalTemperature() {
		return optimalTemperature;
	}

	/**
	 * @return the relative difference between real and optimal temperature that triggers moves.
	 */
	public double getUnhappiness() {
		return unhappiness;
	}

	/**
	 * @return the chance that a bug will make a random move even if it would prefer to
	 * stay where it is.
	 */
	public double getRandomMoveProbability() {
		return randomMoveProbability;
	}
	
	

}
