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
package fr.lgi2a.similar2logo.lib.probes;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.swing.JPanel;

import fr.lgi2a.similar.microkernel.AgentCategory;
import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.agents.ILocalStateOfAgent;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.lgi2a.similar.microkernel.libs.probes.AbstractProbeImageSwingJPanel;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtleAgentCategory;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.lgi2a.similar2logo.kernel.model.environment.Pheromone;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.kernel.probes.IPheromoneFieldDrawer;
import fr.lgi2a.similar2logo.kernel.probes.ISituatedEntityDrawer;

/**
 * This probe displays the grid of the logo simulation as a Swing {@link JPanel}.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class GridSwingView extends AbstractProbeImageSwingJPanel {

	/**
	 * The conversion ratio between patches and pixels.
	 */
	private static final int PATCH_SIZE_IN_PIXELS = 10;
	
	/**
	 *  Mapping between agent categories and situated entity drawers.
	 */
	private Map<AgentCategory, ISituatedEntityDrawer> turtleDrawers;
	
	/**
	 *  The default drawer for turtles.
	 */
	private ISituatedEntityDrawer defaultTurtleDrawer;
	
	/**
	 *  The default drawer for marks.
	 */
	private ISituatedEntityDrawer defaultMarkDrawer;
	
	/**
	 * The identifier of the pheromone field to be displayed. Set to <code>null</code> to not display the field.
	 */
	private String pheromoneIdentifier;

	private double[][] pheromoneField;

	/**
	 * The drawer of the pheromone field.
	 */
	private IPheromoneFieldDrawer pheromoneFieldDrawer;
	
	/**
	 * Creates the probe displaying the simulation as an image in a {@link JPanel}.
	 * @param backgroundColor The background color of the {@link JPanel}.
	 * <code>null</code> if transparent.
	 * @param defaultTurtleDrawer The default drawer for turtle.
	 * @param defaultTurtleDrawer The default drawer for marks.
	 * @param pheromoneIdentifier The identifier of the pheromone field to be displayed. Set to <code>null</code> to not display the field.
	 * @param pheromoneFieldDrawer The drawer of the pheromone field
	 */
	public GridSwingView(
		Color backgroundColor,
		ISituatedEntityDrawer defaultTurtleDrawer,
		ISituatedEntityDrawer defaultMarkDrawer,
		String pheromoneIdentifier,
		IPheromoneFieldDrawer pheromoneFieldDrawer
	) {
		super( backgroundColor );
		this.defaultTurtleDrawer = defaultTurtleDrawer;
		this.defaultMarkDrawer = defaultMarkDrawer;
		this.turtleDrawers = new LinkedHashMap<AgentCategory, ISituatedEntityDrawer>();
		this.pheromoneIdentifier = pheromoneIdentifier;
		this.pheromoneFieldDrawer = pheromoneFieldDrawer;
	}
	
	/**
	 * Creates the probe displaying the simulation as an image in a {@link JPanel}.
	 * @param backgroundColor The background color of the {@link JPanel}. 
	 * @param displayTurtles <code>true</code> to display turtles with the default drawer.
	 * .
	 */
	public GridSwingView(
		Color backgroundColor,
		boolean displayTurtles
	) {
		super( backgroundColor );
		if(displayTurtles) {
			this.defaultTurtleDrawer = new DefaultSituatedEntityDrawer();
		} else {
			this.defaultTurtleDrawer = null;
		}
		this.turtleDrawers = new LinkedHashMap<AgentCategory, ISituatedEntityDrawer>();
		this.defaultMarkDrawer = new DefaultSituatedEntityDrawer(Color.RED);
		this.pheromoneFieldDrawer = new DefaultPheromoneFieldDrawer();
	}
	
	/**
	 * Associates a drawer to a category of agents
	 * @param agentCategory The agent category. 
	 * @param turtleDrawer The turtle drawer.
	 * .
	 */
	public void addDrawer(AgentCategory agentCategory, ISituatedEntityDrawer turtleDrawer) {
		if(agentCategory == null || turtleDrawer == null) {
			throw new IllegalArgumentException( "The arguments cannot be null." );
		}
		this.turtleDrawers.put(agentCategory, turtleDrawer);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(
		SimulationTimeStamp initialTimestamp,
		ISimulationEngine simulationEngine
	) {
		super.observeAtInitialTimes(initialTimestamp, simulationEngine);
		if(this.pheromoneIdentifier != null) {
			LogoEnvPLS envPLS = (LogoEnvPLS) simulationEngine.getEnvironment().getPublicLocalState(LogoSimulationLevelList.LOGO);
			for(Map.Entry<Pheromone, double[][]> pheromone : envPLS.getPheromoneField().entrySet()) {
				if(pheromone.getKey().getIdentifier().equals(this.pheromoneIdentifier)) {
					this.pheromoneField = pheromone.getValue();
				}
			}
			if(this.pheromoneField == null) {
				throw new IllegalArgumentException(
					"the pheromone identifier " + this.pheromoneIdentifier + " is not recognized."
				);
			}
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateGraphics(IPublicDynamicStateMap dynamicState,
			SimulationTimeStamp currentTime, Graphics2D graphics, int imgWidth,
			int imgHeight) {
		graphics.scale(PATCH_SIZE_IN_PIXELS, PATCH_SIZE_IN_PIXELS);
		graphics.setBackground( null );
		graphics.clearRect(
			0, 
			0, 
			imgWidth, 
			imgHeight
		);
		
		// Get the public local state of the environment in the "Micro" level.
		Set<ILocalStateOfAgent>  rawAgentStates = dynamicState.get( LogoSimulationLevelList.LOGO ).getPublicLocalStateOfAgents();
		LogoEnvPLS envState = (LogoEnvPLS) dynamicState.get( LogoSimulationLevelList.LOGO ).getPublicLocalStateOfEnvironment();
		
		//Display pheromones
		if(this.pheromoneField != null) {
			for(int x = 0; x < envState.getWidth(); x++) {
				for(int y = 0; y < envState.getHeight(); y++) {
					pheromoneFieldDrawer.draw(graphics, x, y, this.pheromoneField[x][y]);
				}
			}
		}
		//Display marks
		for(int x = 0; x < envState.getWidth(); x++) {
			for(int y = 0; y < envState.getHeight(); y++) {
				for(Mark mark : envState.getMarks()[x][y]) {
					defaultMarkDrawer.draw(graphics, mark);
				}
			}
		}
		
		//Display agents
		for(ILocalStateOfAgent rawAgentState : rawAgentStates) {
			AgentCategory agentCategory = rawAgentState.getCategoryOfAgent();
			if(!agentCategory.isA(TurtleAgentCategory.CATEGORY)) {
				throw new IllegalArgumentException( "A logo level cannot host non turtle agents." );
			}
			TurtlePLSInLogo castedAgentState = (TurtlePLSInLogo) rawAgentState;
			ISituatedEntityDrawer agentDrawer = getAgentDrawer(agentCategory);
			if(agentDrawer != null) {
				agentDrawer.draw(graphics, castedAgentState);
			}
		}
	}
	
	/**
	 * @param turtleCategory The category of the turle.
	 * @return the drawer associated to the turtle category.
	 */
	private ISituatedEntityDrawer getAgentDrawer(AgentCategory turtleCategory) {
		ISituatedEntityDrawer drawer = turtleDrawers.get(turtleCategory);
		if(drawer == null) {
			return defaultTurtleDrawer;
		}
		return drawer;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Dimension computeSimulationImageDimensions(
		IPublicDynamicStateMap dynamicState, SimulationTimeStamp initialTime) {
		// Get the public local state of the environment in the "Micro" level.
		ILocalStateOfEnvironment rawEnPls = dynamicState.get( LogoSimulationLevelList.LOGO ).getPublicLocalStateOfEnvironment();
		LogoEnvPLS envPls = (LogoEnvPLS) rawEnPls;
		// Create the dimensions of the images.
		return new Dimension(
			envPls.getWidth() * PATCH_SIZE_IN_PIXELS,
			envPls.getHeight() * PATCH_SIZE_IN_PIXELS
		);
	}

	/**
	 * @return the pheromone field to be displayed.
	 */
	public String getDisplayPheromone() {
		return pheromoneIdentifier;
	}

	/**
	 * @param pheromoneIdentifier the pheromone field to be displayed.
	 * Set to <code>null</code> to not display the field.
	 */
	public void setDisplayPheromone(String displayPheromone) {
		this.pheromoneIdentifier = displayPheromone;
	}

}