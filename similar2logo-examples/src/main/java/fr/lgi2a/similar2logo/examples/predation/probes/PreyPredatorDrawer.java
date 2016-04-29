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
package fr.lgi2a.similar2logo.examples.predation.probes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import fr.lgi2a.similar2logo.examples.predation.model.PredationSimulationParameters;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PredatorCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyCategory;
import fr.lgi2a.similar2logo.examples.predation.model.agents.PreyPredatorPLS;
import fr.lgi2a.similar2logo.kernel.model.SituatedEntity;
import fr.lgi2a.similar2logo.kernel.probes.ISituatedEntityDrawer;

/**
 * A drawer for the agents in the predation simulation.
 * Preys are displayed in blue and predtors in red.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class PreyPredatorDrawer implements ISituatedEntityDrawer {

	/**
	 * 
	 */
	private PredationSimulationParameters parameters;
	
	/**
	 * @param parameters
	 */
	public PreyPredatorDrawer(PredationSimulationParameters parameters) {
		this.parameters = parameters;
	}

	@Override
	public void draw(Graphics graphics, SituatedEntity situatedEntity) {
		Graphics2D workGraphics = (Graphics2D) graphics.create();
		PreyPredatorPLS agent = (PreyPredatorPLS) situatedEntity;
		if (agent.getCategoryOfAgent().isA(PreyCategory.CATEGORY)) {
			workGraphics.setColor(new Color(0,0,(int) Math.min(100+155*agent.getEnergy()/parameters.maximalPreyEnergy,255)));
		} else if (agent.getCategoryOfAgent().isA(PredatorCategory.CATEGORY)) {
			workGraphics.setColor(new Color((int) Math.min(100+ 155*agent.getEnergy()/parameters.maximalPreyEnergy,255),0,0));
		}
		
		Shape turtleShape = new Ellipse2D.Double(agent.getLocation().getX() - 0.5,
				agent.getLocation().getY() + 0.5, 1, 1);

		workGraphics.fill(turtleShape);

	}

}
