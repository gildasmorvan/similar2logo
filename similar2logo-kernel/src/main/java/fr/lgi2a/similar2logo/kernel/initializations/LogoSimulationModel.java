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
package fr.lgi2a.similar2logo.kernel.initializations;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.lgi2a.similar.extendedkernel.environment.ExtendedEnvironment;
import fr.lgi2a.similar.extendedkernel.levels.ExtendedLevel;
import fr.lgi2a.similar.extendedkernel.libs.endcriterion.TimeBasedEndCriterion;
import fr.lgi2a.similar.extendedkernel.libs.timemodel.PeriodicTimeModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.AbstractExtendedSimulationModel;
import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar.microkernel.LevelIdentifier;
import fr.lgi2a.similar.microkernel.levels.ILevel;
import fr.lgi2a.similar.microkernel.libs.generic.EmptyLocalStateOfEnvironment;
import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoNaturalModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * The abstract simulation model of a Logo simulation.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public abstract class LogoSimulationModel extends AbstractExtendedSimulationModel {

	/**
	 * The parameters being used in the simulation.
	 */
	private LogoSimulationParameters parameters;
	
	/**
	 * Builds a new model for a logo simulation.
	 * @param parameters The parameters of this simulation model.
	 */
	public LogoSimulationModel(LogoSimulationParameters parameters) {
		super(
				parameters,
				new TimeBasedEndCriterion( 
					parameters.finalTime
				)
			);
			this.parameters = parameters;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters) {
		ExtendedLevel logo = new ExtendedLevel(
				this.parameters.getInitialTime(), 
				LogoSimulationLevelList.LOGO, 
				new PeriodicTimeModel( 
					1, 
					0, 
					this.parameters.getInitialTime()
				),
				new LogoDefaultReactionModel()
			);
		List<ILevel> levelList = new LinkedList<ILevel>();
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
		// Create the environment.
		ExtendedEnvironment environment = new ExtendedEnvironment( );
		// Define the initial behavior of the environment for each level.
		environment.specifyBehaviorForLevel(
			LogoSimulationLevelList.LOGO, 
			new LogoNaturalModel( )
		);
		// Set the initial local state of the environment for each level.
		environment.includeNewLevel(
			LogoSimulationLevelList.LOGO,
			new LogoEnvPLS(
				this.parameters.gridWidth,
				this.parameters.gridHeight,
				this.parameters.xTorus,
				this.parameters.yTorus,
				this.parameters.pheromones
			),
			new EmptyLocalStateOfEnvironment( LogoSimulationLevelList.LOGO )
		);
		return new EnvironmentInitializationData( environment );
	}
	
	public void setParameters(LogoSimulationParameters parameters)
	{
		this.parameters = parameters;
	}

}
