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
package fr.univ_artois.lgi2a.similar2logo.examples.projettut;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.univ_artois.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.univ_artois.lgi2a.similar.microkernel.levels.ILevel;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleFactory;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.lib.model.ConeBasedPerceptionModel;

/**
 * The simulation model of the "passive turtle" simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class SimulationModel extends AbstractLogoSimulationModel {

	
	/**
	 * Builds a new model for the passive turtle simulation.
	 * @param parameters The parameters of this simulation model.
	 */
	public SimulationModel(LogoSimulationParameters parameters) {
		super(parameters);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
		ISimulationParameters simulationParameters
	) {
		ExtendedLevel logo = new ExtendedLevel(
				simulationParameters.getInitialTime(), 
				LogoSimulationLevelList.LOGO, 
				new PeriodicTimeModel( 
					1, 
					0, 
					simulationParameters.getInitialTime()
				),
				new ReactionModel((SimulationParameters) simulationParameters)
			);
		List<ILevel> levelList = new LinkedList<>();
		levelList.add(logo);
		return levelList;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected EnvironmentInitializationData generateEnvironment(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		
		EnvironmentInitializationData environmentInitializationData =  super.generateEnvironment(simulationParameters, levels);
		LogoEnvPLS environment = (LogoEnvPLS) environmentInitializationData.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
		
		environment.getMarksAt(environment.getWidth()-10, environment.getHeight()/2).add(new Mark<>(new Point2D.Double(environment.getWidth()-10, environment.getHeight()/2)));
		
		return environmentInitializationData;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AgentInitializationData generateAgents(
			ISimulationParameters parameters, Map<LevelIdentifier, ILevel> levels) {
		SimulationParameters castedParameters = (SimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		IAgent4Engine turtle = TurtleFactory.generate(
			new ConeBasedPerceptionModel(
				castedParameters.gridWidth,2*Math.PI,false,true,true
			),
			new ProportionalDecisionModel(castedParameters),
			ControllerCategory.CATEGORY,
			castedParameters.initialDirection,
			castedParameters.initialSpeed,
			castedParameters.initialAcceleration,
			0,
			castedParameters.gridHeight/2
		);
		result.getAgents().add( turtle );
		return result;
	}

}
