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
package fr.univ_artois.lgi2a.similar2logo.lib.tools.html;

import java.io.IOException;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.ResourceNotFoundException;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.SimilarWebConfig;
/**
 * The configuration options of Similar2Logo simulations using the HTML web interface.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 */
public class Similar2LogoWebConfig extends SimilarWebConfig {
	
	/**
	 * <code>true</code> if agent states are exported, <code>false</code> else.
	 */
	private boolean exportAgents;
	/**
	 * <code>true</code> if marks are exported, <code>false</code> else.
	 */
	private boolean exportMarks;
	/**
	 * <code>true</code> if pheromones are exported, <code>false</code> else.
	 */
	private boolean exportPheromones;
	

	
	/**
	 * Creates a default content for the configuration.
	 */
	public Similar2LogoWebConfig( ) {
		super();
		this.exportAgents = false;
		this.exportMarks = false;
		this.exportPheromones = false;
		try {
			this.setCustomHtmlBody( Similar2LogoWebConfig.class.getResourceAsStream("gridview.html") );
		} catch (IOException e) {
			throw new ResourceNotFoundException(e);
		}
	}
	
	/**
	 * Checks if the agents have to be exported to the HTML view.
	 * @return <code>true</code> if agent states are exported, <code>false</code> else.
	 */
	public boolean areAgentsExported( ){
		return this.exportAgents;
	}
	
	/**
	 * Checks if the marks have to be exported to the HTML view.
	 * @return <code>true</code> if marks are exported, <code>false</code> else.
	 */
	public boolean areMarksExported( ){
		return this.exportMarks;
	}
	
	/**
	 * Checks if the pheromones have to be exported to the HTML view.
	 * @return <code>true</code> if pheromones are exported, <code>false</code> else.
	 */
	public boolean arePheromonesExported( ){
		return this.exportPheromones;
	}

	/**
	 * Define if the agents have to be exported to the HTML view.
	 * @param exportAgents <code>true</code> if agent states are exported, <code>false</code> else.
	 * @return The updated configuration.
	 * @throws IllegalStateException Whenever the runner is already initialized and cannot be configured any more.
	 */
	public Similar2LogoWebConfig setExportAgents( boolean exportAgents ) throws IllegalStateException {
		if( ! this.initializationDone ){
			this.exportAgents = exportAgents;
		} else {
			throw new IllegalStateException( ERROR_MESSAGE );
		}
		return this;
	}

	/**
	 * Define if the marks have to be exported to the HTML view.
	 * @param exportMarks <code>true</code> if marks are exported, <code>false</code> else.
	 * @return The updated configuration.
	 * @throws IllegalStateException Whenever the runner is already initialized and cannot be configured any more.
	 */
	public Similar2LogoWebConfig setExportMarks( boolean exportMarks ) throws IllegalStateException {
		if( ! this.initializationDone ){
			this.exportMarks = exportMarks;
		} else {
			throw new IllegalStateException( ERROR_MESSAGE );
		}
		return this;
	}

	/**
	 * Define if the pheromones have to be exported to the HTML view.
	 * @param exportPheromones <code>true</code> if pheromones are exported, <code>false</code> else.
	 * @return The updated configuration.
	 * @throws IllegalStateException Whenever the runner is already initialized and cannot be configured any more.
	 */
	public Similar2LogoWebConfig setExportPheromones( boolean exportPheromones ) throws IllegalStateException {
		if( ! this.initializationDone ){
			this.exportPheromones = exportPheromones;
		} else {
			throw new IllegalStateException( ERROR_MESSAGE );
		}
		return this;
	}
	
}