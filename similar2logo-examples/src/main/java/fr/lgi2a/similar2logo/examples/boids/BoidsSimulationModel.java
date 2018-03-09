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
package fr.lgi2a.similar2logo.examples.boids;

import java.util.Map;

import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.boids.model.BoidDecisionModel;
import fr.lgi2a.similar2logo.examples.boids.model.BoidsSimulationParameters;
import fr.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * The simulation model of the boids simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class BoidsSimulationModel extends AbstractLogoSimulationModel {

	/**
	 * Builds an instance of this simulation model.
	 * @param parameters The parameters of the simulation model.
	 */
	public BoidsSimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(
		ISimulationParameters parameters, Map<LevelIdentifier, ILevel> levels
	) {
		BoidsSimulationParameters castedParameters = (BoidsSimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		for(int i = 0; i < castedParameters.nbOfAgents; i++) {
			result.getAgents().add(generateBoid(castedParameters));
		}
		return result;
	}
	
	/**
	 * @param p The parameters of the simulation model.
	 * @return a new boid located at the center of the grid.
	 */
	private static IAgent4Engine generateBoid(BoidsSimulationParameters p) {
		return TurtleFactory.generate(
			new TurtlePerceptionModel(
				p.attractionDistance,p.perceptionAngle,true,false,false
			),
			new BoidDecisionModel(p),
			new AgentCategory("b", TurtleAgentCategory.CATEGORY),
			Math.PI-RandomValueFactory.getStrategy().randomDouble()*2*Math.PI,
			p.minInitialSpeed + RandomValueFactory.getStrategy().randomDouble()*(
				p.maxInitialSpeed-p.minInitialSpeed
			),
			0,
			p.gridWidth/2.0,
			p.gridHeight/2.0
		);
	}

}
