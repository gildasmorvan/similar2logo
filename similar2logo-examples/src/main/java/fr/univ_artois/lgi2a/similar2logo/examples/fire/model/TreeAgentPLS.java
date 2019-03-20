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
package fr.univ_artois.lgi2a.similar2logo.examples.fire.model;

import java.awt.Point;

import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.random.PRNG;
import fr.univ_artois.lgi2a.similar.microkernel.agents.IAgent4Engine;

/**
 * The public local state of a MLE agent in the "Logo" level.
 * 
 * @author <a xavier_szkudlarek@univ-artois.fr target="_blank">Szkudlarek
 *         Xavier</a>
 * 
 */
/**
 * @author szkudlarek
 *
 */
public class TreeAgentPLS extends TurtlePLSInLogo {

	private double burned;

	private boolean burning;

	public TreeAgentPLS(IAgent4Engine owner, double initialX, double initialY,
			double initialSpeed, double initialAcceleration,
			double initialDirection, boolean burning) {
		super(owner, initialX, initialY, initialSpeed, initialAcceleration,
				initialDirection);
		this.burned = 0;
		this.burning = burning;
	}

	/**
	 * @return the levelOfEmergence
	 */
	public double getBurned() {
		return burned;
	}

	/**
	 * @return true if the tree is burned, false else.
	 */
	public boolean isBurned() {
		return burned >= 1;
	}

	/**
	 * Makes the tree burn.
	 * @param speed the burning speed
	 */
	public void burn(double speed) {
		burned += speed;
	}
	
	public void propagate(LogoEnvPLS env, double firePropagationProba) {
		for (Point neighbor: env.getNeighbors((int) this.getLocation().x, (int) this.getLocation().y, 1)){
			for(TurtlePLSInLogo otherAgent : env.getTurtlesAt(neighbor)) {
				TreeAgentPLS otherTree = (TreeAgentPLS) otherAgent;
				if(!otherTree.isBurning() && PRNG.randomDouble() < firePropagationProba) {
					otherTree.setBurning(true);
				}
			}
		}
	}

	/**
	 * @param levelOfEmergence
	 *            the levelOfEmergence to set
	 */
	public void setBurned(double burned) {
		if (burned >= 1) {
			this.burned = 1;
		} else {
			this.burned = burned;
		}

	}

	public boolean isBurning() {
		return burning;
	}

	public void setBurning(boolean burning) {
		this.burning = burning;
	}

}
