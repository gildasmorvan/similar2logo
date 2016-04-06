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
import java.awt.Graphics2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar.microkernel.dynamicstate.IPublicDynamicStateMap;
import fr.lgi2a.similar2logo.kernel.probes.IPheromoneFieldDrawer;
import fr.lgi2a.similar2logo.kernel.probes.ISituatedEntityDrawer;

/**
 * This probe displays the grid of the logo simulation as a Swing {@link JPanel}
 * .
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class GridPngView extends GridSwingView {

	File imageFile;
	File lastImageFile;

	/**
	 * Creates the probe displaying the simulation as an image in a
	 * {@link JPanel}.
	 * 
	 * @param backgroundColor
	 *            The background color of the {@link JPanel}. <code>null</code>
	 *            if transparent.
	 * @param defaultTurtleDrawer
	 *            The default drawer for turtle.
	 * @param defaultTurtleDrawer
	 *            The default drawer for marks.
	 * @param pheromoneIdentifier
	 *            The identifier of the pheromone field to be displayed. Set to
	 *            <code>null</code> to not display the field.
	 * @param pheromoneFieldDrawer
	 *            The drawer of the pheromone field
	 */
	public GridPngView(Color backgroundColor,
			ISituatedEntityDrawer defaultTurtleDrawer,
			ISituatedEntityDrawer defaultMarkDrawer,
			String pheromoneIdentifier,
			IPheromoneFieldDrawer pheromoneFieldDrawer,
			File imageFile,
			File lastImageFile) {
		super(backgroundColor, defaultTurtleDrawer, defaultMarkDrawer,
				pheromoneIdentifier, pheromoneFieldDrawer);
		this.imageFile = imageFile;
		this.lastImageFile = lastImageFile;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void observeAtInitialTimes(SimulationTimeStamp initialTimestamp,
			ISimulationEngine simulationEngine) {
		super.observeAtInitialTimes(initialTimestamp, simulationEngine);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void updateGraphics(IPublicDynamicStateMap dynamicState,
			SimulationTimeStamp currentTime, Graphics2D graphics, int imgWidth,
			int imgHeight) {
		super.updateGraphics(dynamicState, currentTime, graphics, imgWidth,
				imgHeight);
		CopyOption[] options = new CopyOption[]{
			      StandardCopyOption.REPLACE_EXISTING,
			      StandardCopyOption.COPY_ATTRIBUTES
			    }; 
		try {
			ImageIO.write(this.getDoubleBuffer()[0], "png", lastImageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			Files.copy(lastImageFile.toPath(), imageFile.toPath(),options);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}