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
package fr.lgi2a.similar2logo.examples.ants;

import java.awt.geom.Point2D;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar2logo.examples.ants.model.AntCategory;
import fr.lgi2a.similar2logo.examples.ants.model.AntDecisionModel;
import fr.lgi2a.similar2logo.examples.ants.model.AntSimulationParameters;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.lib.model.TurtlePerceptionModel;
import fr.lgi2a.similar2logo.lib.tools.RandomValueFactory;

/**
 * The simulation model of the Ants simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 *
 */
public class AntSimulationModel extends LogoSimulationModel {

	/**
	 * Constructor of the model simulation
	 * @param parameters
	 */
	
	public AntSimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
	}
	
	double x = 0, y = 0;

	/**
	 * Generates the agents of the simulation
	 */
	@Override
	protected AgentInitializationData generateAgents(ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		AntSimulationParameters castedParameters = (AntSimulationParameters) simulationParameters;
		AgentInitializationData result = new AgentInitializationData();	
		//Generate ants in the base
		for(int i = 0; i < castedParameters.initialNumberAnts; i++) {
			result.getAgents().add(generateAnts(castedParameters, this.x, this.y));
		}
		return result;
		
	}
	
	/**
	 * {@inheritDoc}
	 */
	protected EnvironmentInitializationData generateEnvironment( 
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels 
	) {
		AntSimulationParameters param = (AntSimulationParameters) simulationParameters;
		
		EnvironmentInitializationData environmentInitializationData = super.generateEnvironment(simulationParameters, levels);
		LogoEnvPLS environment = (LogoEnvPLS) environmentInitializationData.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
		for(int i=0; i< param.initialNumberFoods;i++) {
			this.x = (int) (RandomValueFactory.getStrategy().randomDouble() * param.gridWidth);
			this.y = (int) (RandomValueFactory.getStrategy().randomDouble() * param.gridHeight);
			environment.getMarksAt((int)x, (int)y).add(
			   new Mark<Double>(new Point2D.Double(this.x,this.y),(double) param.initialQuantityOfFood, "Food")
			);
		}
		this.x = (int) (RandomValueFactory.getStrategy().randomDouble() * param.gridWidth);
		this.y = (int) (RandomValueFactory.getStrategy().randomDouble() * param.gridHeight);
		environment.getMarksAt((int)this.x, (int)this.y).add(
			new Mark<Double>(new Point2D.Double(this.x,this.y),(double) 0, "Base")
		);
		
		return environmentInitializationData;
	}
	
	/**
	 * Generate a ants to the simulation
	 * @param param is a parameters of this simulation 
	 * @param x is a base on the x axe
	 * @param y is a base on the y axe
	 * @return the agents
	 */
	private static IAgent4Engine generateAnts(AntSimulationParameters param, double x, double y){
		return TurtleFactory.generate(
			new TurtlePerceptionModel(
				param.perceptionDistance,param.perceptionAngle,true,true,true
			),
			new AntDecisionModel(param, x, y, 0),
			AntCategory.CATEGORY,
			Math.PI-RandomValueFactory.getStrategy().randomDouble()*2*Math.PI,
			param.initialSpeed ,
			0,
			x,
			y
		);
	}
	
}
