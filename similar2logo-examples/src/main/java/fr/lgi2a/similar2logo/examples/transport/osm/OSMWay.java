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
package fr.lgi2a.similar2logo.examples.transport.osm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static fr.lgi2a.similar2logo.examples.transport.osm.OSMConstants.*;

/**
 * Way data from the OSM data.
 * 
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 */
public class OSMWay {
	
	/**
	 * The nodes contain in the way
	 */
	private List<String> nodes;
	
	/**
	 * The tags associate to the way
	 */
	private Map<String,String> tag;
	
	/**
	 * Constructor of the OSM way data
	 */
	public OSMWay () {
		this.nodes = new ArrayList<String>();
		this.tag = new HashMap<>();
	}
	
	/**
	 * Adds a node
	 * @param ref the id of the node
	 */
	public void addNode (String ref) {
		this.nodes.add(ref);
	}

	/**
	 * Adds a tag
	 * @param k the key of the tag
	 * @param v the value of the tag
	 */
	public void addTag (String k, String v) {
		this.tag.put(k, v);
	}
	
	/**
	 * @return the nodes
	 */
	public List<String> getNodes () {
		return this.nodes;
	}
	
	/**
	 * @return the tags
	 */
	public Map<String,String> getTags () {
		return this.tag;
	}
	
	/**
	 * Indicates if the way belongs to a railway
	 * @return <code>true</code> if the way belongs to a railway <code>false</code> else
	 */
	public boolean isRailway () {
		return (tag.keySet().contains(RAILWAY) && tag.get(RAILWAY).equals(RAIL));
	}
	
	/**
	 * Indicates if the way belongs to a highway
	 * @return <code>true</code> if the way belongs to a highway <code>false</code> else
	 */
	public boolean isHighway () {
		return (this.tag.containsKey(HIGHWAY) && (tag.get(HIGHWAY).equals(RESIDENTIAL) || tag.get(HIGHWAY).equals(TERTIARY)
				|| tag.get(HIGHWAY).equals(SECONDARY) || tag.get(HIGHWAY).equals(SECONDARY_LINK)));
	}
	
	/**
	 * Indicates if the way belongs to a tramway.
	 * @return <code>true</code> if the way belong to a tramway <code>false</code> else
	 */
	public boolean isTramway () {
		for (String t : tag.keySet()) {
			if (t.equals(RAILWAY) && tag.get(t).equals(TRAM)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return <code>true</code> if the road is residential <code>false</code> else
	 */
	public boolean isResidentialRoad () {
		return this.tag.containsKey(HIGHWAY) 
			&& this.tag.get(HIGHWAY).equals(RESIDENTIAL);
	}
	
	/**
	 * @return <code>true</code> if the road is tertiary <code>false</code> else
	 */
	public boolean isTertiaryRoad () {
		return this.tag.containsKey(HIGHWAY) 
			&& this.tag.get(HIGHWAY).equals(TERTIARY);
	}
	
	/**
	 * @return <code>true</code> if the road is secondary <code>false</code> else
	 */
	public boolean isSecondaryRoad () {
		return this.tag.containsKey(HIGHWAY) 
			&& (this.tag.get(HIGHWAY).equals(SECONDARY) 
			 || this.tag.get(HIGHWAY).equals(SECONDARY_LINK));
	}
	

}
