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
package fr.lgi2a.similar2logo.examples.transport.osm.roadsgraph;

import java.awt.geom.Point2D;

/**
 * The class for showing points extract from Open Street Map
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class RoadNode {
	
	/**
	 * The position of the point
	 */
	private Point2D position;
	
	private String type;
	
	public RoadNode (Point2D p, String type) {
		this.position = p;
		this.type = type;
	}
	
	/**
	 * Returns the position of the point
	 * @return the position of the point
	 */
	public Point2D getPosition () {
		return this.position;
	}
	
	/**
	 * Return the type of the point
	 * @return the type of the point
	 */
	public String getType () {
		return this.type;
	}
	
	public String toString () {
		return "Rode noad : "+position.toString()+", type : "+type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode () {
		return position.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals (Object o) {
		if(this == o) {
			return true;
		}
		if(o == null) {
			return false;
		}
		if (o.getClass() != this.getClass()) {
			return false;
		}
		RoadNode rn = (RoadNode) o;
		return rn.position.equals(position) && rn.getType().equals(type);
	}

}
