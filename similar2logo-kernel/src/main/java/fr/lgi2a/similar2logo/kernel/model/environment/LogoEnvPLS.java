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
package fr.lgi2a.similar2logo.kernel.model.environment;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import fr.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfEnvironment;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.lgi2a.similar2logo.kernel.tools.FastMath;

/**
 * Models the public local state of a turtle agent.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LogoEnvPLS extends AbstractLocalStateOfEnvironment implements Cloneable {

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
	 * The pheromone field associated to the grid.
	 */
	private Map<Pheromone, double[][]> pheromoneField;
	
	/**
	 * The turle sets associated to each patch in the grid.
	 */
	private final Set<TurtlePLSInLogo>[][] turtlesInPatches;
	
	/**
	 * The sets of marks associated to each patch in the grid.
	 */
	private final Set<Mark>[][] marks;
	
	/**
	 * The north of the grid.
	 */
	public static final double NORTH = 0;
	
	/**
	 * The north east of the grid.
	 */
	public static final double NORTH_EAST = -Math.PI/4;
	
	/**
	 * The east of the grid.
	 */
	public static final double EAST = -Math.PI/2;
	
	/**
	 * The south east of the grid.
	 */
	public static final double SOUTH_EAST = -3*Math.PI/4;
	
	/**
	 * The south of the grid.
	 */
	public static final double SOUTH = Math.PI;
	
	/**
	 * The south west of the grid.
	 */
	public static final double SOUTH_WEST = 3*Math.PI/4;
	
	/**
	 * The west of the grid.
	 */
	public static final double WEST = Math.PI/2;
	
	/**
	 * The north west of the grid.
	 */
	public static final double NORTH_WEST = Math.PI/4;
	
	/**
	 * Builds an initialized instance of this class.
	 * @param gridWidth The width of the grid.
	 * @param gridHeight The height of the grid.
	 * @param xAxisTorus <code>true</code> if the environment
	 * is toroidal along the x axis.
	 * @param yAxisTorus <code>true</code> if the environment
	 * is toroidal along the y axis.
	 */
	public LogoEnvPLS(int gridWidth,
		int gridHeight,
		boolean xAxisTorus,
		boolean yAxisTorus,
		Set<TurtlePLSInLogo>[][] turtlesInPatches,
		Set<Mark>[][] marks,
		Map<Pheromone, double[][]> pheromoneField
	) {
		super(LogoSimulationLevelList.LOGO);
		this.width = gridWidth;
		this.height = gridHeight;
		this.xAxisTorus = xAxisTorus;
		this.yAxisTorus = yAxisTorus;
		this.pheromoneField = new HashMap<>();
		this.turtlesInPatches = turtlesInPatches;
		this.marks = marks;
		
	}
	
	/**
	 * Builds an initialized instance of this class.
	 * @param gridWidth The width of the grid.
	 * @param gridHeight The height of the grid.
	 * @param xAxisTorus <code>true</code> if the environment
	 * is toroidal along the x axis.
	 * @param yAxisTorus <code>true</code> if the environment
	 * is toroidal along the y axis.
	 * @param pheromones The set of pheromone fields in the environment.
	 */
	public LogoEnvPLS(int gridWidth,
		int gridHeight,
		boolean xAxisTorus,
		boolean yAxisTorus,
		Set<Pheromone> pheromones
	) {
		super(LogoSimulationLevelList.LOGO);
		this.width = gridWidth;
		this.height = gridHeight;
		this.xAxisTorus = xAxisTorus;
		this.yAxisTorus = yAxisTorus;
		this.pheromoneField = new HashMap<>();
		for(Pheromone pheromone : pheromones) {
			this.pheromoneField.put(pheromone, new double[this.width][this.height]);
			for(int x = 0; x < this.width; x++) {
				for(int y = 0; y < this.height; y++) {
					this.pheromoneField.get(pheromone)[x][y] = pheromone.getDefaultValue();
				}
			}
		}
		turtlesInPatches = new Set[this.width][this.height];
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				turtlesInPatches[x][y] = new HashSet<>();
			}
		}
		marks = new Set[this.width][this.height];
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				marks[x][y] = new HashSet<>();
			}
		}
		
	}
	
	
	/**
	 * Computes the positions of the neighbors of a patch.
	 * @param x The x coordinate of the patch.
	 * @param y The y coordinate of the patch.
	 * @param distance The maximal distance of neighbors.
	 * @return the positions of the patch neighbors.
	 */
	public List<Position> getNeighbors(int x, int y, int distance) {
		List<Position> neighbors = new ArrayList<>();
		for(int dx=-distance; dx <=distance; dx++) {
			for(int dy=-distance; dy <=distance; dy++) {
				int nx = x + dx;
				int ny = y + dy;
				if(this.xAxisTorus) {
					nx = ( ( nx % this.width ) + this.width ) % this.width;
				}
				if(this.yAxisTorus) {
					ny =  ( ( ny % this.height ) + this.height ) % this.height;
				}
				if(nx >=0 && nx < this.width && ny >=0 && ny < this.height) {
					neighbors.add(new Position(nx, ny));
				}
			}	
		}
		return neighbors;
	}
	
	/**
	 * @param from the location of the first point
	 * @param to the location of the second point
	 * @return the direction from <code>from</code> to <code>to</code>
	 */
	public double getDirection(Point2D from, Point2D to) {
				
		if(this.getDistance( from, to ) <= Double.MIN_VALUE) {
			return 0;
		}
		double xtarget = to.getX();
		double ytarget = to.getY();
		if(this.xAxisTorus && Math.abs(xtarget - from.getX())*2 >= this.width) {
			if(from.getX() > xtarget) {
				xtarget += this.width;
			} else {
				xtarget -= this.width;
			}
		}
		if(this.yAxisTorus && Math.abs(ytarget - from.getY())*2 >= this.height) {
			if(from.getY() > ytarget) {
				ytarget += this.height;
			} else {
				ytarget -= this.height;
			}
		}
		return -FastMath.atan2(xtarget-from.getX(), ytarget-from.getY());
	}
	
	/**
	 * @param loc1 the location of the first point
	 * @param loc2 the location of the second point
	 * @return the distance between <code>loc1</code> and <code>loc2</code>
	 */
	public double getDistance(Point2D loc1, Point2D loc2) {
		double dx = Math.abs(loc1.getX() - loc2.getX());
		double dy  = Math.abs(loc1.getY() - loc2.getY());
		if(this.xAxisTorus && dx*2 > this.width) {
			dx = this.width - dx;
		}
		if(this.yAxisTorus && dy*2 > this.height) {
			dy = this.height - dy;
		}
		
		return Math.sqrt(dx*dx + dy*dy);
	}
	
	/**
	 * @param x the x coordinate of the patch.
	 * @param y the y coordinate of the patch.
	 * @return the public local states of the turtles located in patch x,y.
	 */
	public Set<TurtlePLSInLogo> getTurtlesAt(int x, int y) {
		return turtlesInPatches[x][y];
	}
	
	/**
	 * @param x the x coordinate of the patch.
	 * @param y the y coordinate of the patch.
	 * @return the marks located in patch x,y.
	 */
	public Set<Mark> getMarksAt(int x, int y) {
		return marks[x][y];
	}

	/**
	 * @return the pheromone field associated to the grid.
	 */
	public Map<Pheromone, double[][]> getPheromoneField() {
		return pheromoneField;
	}


	/**
	 * @param pheromoneField The pheromone field associated to the grid.
	 */
	public void setPheromoneField(Map<Pheromone, double[][]> pheromoneField) {
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
	 * @return the marks associated to the grid.
	 */
	public Set<Mark>[][] getMarks() {
		return marks;
	}


	/**
	 * @return the turtlesInPatches.
	 */
	public Set<TurtlePLSInLogo>[][] getTurtlesInPatches() {
		return turtlesInPatches;
	}
	
	/** 
	 * @see java.lang.Object#clone()
	 * @return a copy of an instance of this class.
	 */
	@Override
	public Object clone() {
		Map<Pheromone, double[][]> pheromoneField  = new HashMap<>();
		for( Entry<Pheromone, double[][]> pheromone : this.pheromoneField.entrySet()) {
			pheromoneField.put((Pheromone) pheromone.getKey(), (double[][]) pheromone.getValue().clone());
		}
		
		LogoEnvPLS env = new LogoEnvPLS(
			width,
			height,
			xAxisTorus,
			yAxisTorus,
			this.turtlesInPatches.clone(),
			this.marks.clone(),
			pheromoneField	
		);
		return env;
		
	}

}
