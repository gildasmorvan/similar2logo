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
package fr.lgi2a.similar2logo.examples.multiturmite.model;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters;

/**
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class MultiTurmiteSimulationParameters extends LogoSimulationParameters {

	/**
	 * <code>true</code> if direction changes are not taken into account
	 * when two turtles want to modify the same patch.
	 */
	public boolean removeDirectionChange;
	
	/**
	 * <code>true</code> if the output of turtle actions is inversed
	 * when two turtles want to modify the same patch.
	 */
	public boolean inverseMarkUpdate;
	
	/**
	 * The number of turmites in the environment.
	 */
	public int nbOfTurmites;
	
	/**
	 * The initial locations of turmites. If it is empty,
	 * turmites are placed randomly.
	 */
	public List<Point2D> initialLocations;
	
	/**
	 * The initial directions of turmites. If it is empty,
	 * turmites head randomly.
	 */
	public List<Double> initialDirections;
	
	/**
	 * Builds a parameters set containing default values.
	 */
	public MultiTurmiteSimulationParameters() {
		super();
		this.removeDirectionChange = false;
		this.inverseMarkUpdate = false;
		this.nbOfTurmites = 2;
		initialLocations = new ArrayList<Point2D>();
		initialDirections = new ArrayList<Double>();
	}

}