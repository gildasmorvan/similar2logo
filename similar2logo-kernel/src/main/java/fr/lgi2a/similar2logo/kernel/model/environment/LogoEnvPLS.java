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
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
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
package fr.lgi2a.similar2logo.kernel.model.environment;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfEnvironment;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Models the public local state of a turtle agent.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class LogoEnvPLS extends AbstractLocalStateOfEnvironment{

	/**
	 * The width of the grid of the environment.
	 */
	private final int width;
	/**
	 * The height of the grid of the environment.
	 */
	private final int height;
	/**
	 * <code>true</code> if the grid is a torus along the x axis.
	 */
	private final boolean xAxisTorus;
	/**
	 * <code>true</code> if the grid is a torus along the y axis.
	 */
	private final boolean yAxisTorus;
	
	/**
	 * The grid of patches.
	 */
	private final Patch[][] grid;
	
	/**
	 * The pheromone field associated to the grid.
	 */
	private Map<String, double[][]> pheromoneField;
	
	
	/**
	 * Builds an initialized instance of this class.
	 * @param level The level for which this local state was defined.
	 */
	public LogoEnvPLS(int gridWidth,
			int gridHeight,
			boolean xAxisTorus,
			boolean yAxisTorus,
			Set<Pheromone> pheromones) {
		super(LogoSimulationLevelList.LOGO);
		this.grid = new Patch[gridWidth][gridHeight];
		this.width = gridWidth;
		this.height = gridHeight;
		this.xAxisTorus = xAxisTorus;
		this.yAxisTorus = yAxisTorus;
		this.pheromoneField = new LinkedHashMap<String, double[][]>();
		for(Pheromone pheromone : pheromones) {
			this.pheromoneField.put(pheromone.getIdentifier(), new double[this.width][this.height]);
			Arrays.fill(this.pheromoneField.get(pheromone.getIdentifier()), pheromone.getDefaultValue());
		}
		
	}


	/**
	 * @return the pheromone field associated to the grid.
	 */
	public Map<String, double[][]> getPheromoneField() {
		return pheromoneField;
	}


	/**
	 * @param pheromoneField The pheromone field associated to the grid.
	 */
	public void setPheromoneField(Map<String, double[][]> pheromoneField) {
		this.pheromoneField = pheromoneField;
	}


	/**
	 * @return the width of the grid of the environment.
	 */
	public int getWidth() {
		return width;
	}


	/**
	 * @return the height of the grid of the environment.
	 */
	public int getHeight() {
		return height;
	}


	/**
	 * @return the <code>true</code> if the grid is a torus along the x axis.
	 */
	public boolean isxAxisTorus() {
		return xAxisTorus;
	}


	/**
	 * @return <code>true</code> if the grid is a torus along the y axis.
	 */
	public boolean isyAxisTorus() {
		return yAxisTorus;
	}


	/**
	 * @return the grid of patches.
	 */
	public Patch[][] getGrid() {
		return grid;
	}

}
