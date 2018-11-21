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
package fr.univ_artois.lgi2a.similar2logo.kernel.model.environment;

import static net.jafama.FastMath.*;

import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.AgentCategory;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractLocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;
import fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil;

/**
 * Models the public local state of a turtle agent.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
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
	 * The patch grid coordinates.
	 */
	private Point2D.Double[][] patches;
	
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
	public static final double NORTH_EAST = -PI/4;
	
	/**
	 * The east of the grid.
	 */
	public static final double EAST = -PI/2;
	
	/**
	 * The south east of the grid.
	 */
	public static final double SOUTH_EAST = -3*PI/4;
	
	/**
	 * The south of the grid.
	 */
	public static final double SOUTH = -PI;
	
	/**
	 * The south west of the grid.
	 */
	public static final double SOUTH_WEST = 3*PI/4;
	
	/**
	 * The west of the grid.
	 */
	public static final double WEST = PI/2;
	
	/**
	 * The north west of the grid.
	 */
	public static final double NORTH_WEST = PI/4;
	
	/**
	 * Builds an initialized instance of this class.
	 * @param gridWidth The width of the grid.
	 * @param gridHeight The height of the grid.
	 * @param xAxisTorus <code>true</code> if the environment
	 * is toroidal along the x axis.
	 * @param yAxisTorus <code>true</code> if the environment
	 * is toroidal along the y axis.
	 */
	public LogoEnvPLS(
		int gridWidth,
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
		this.pheromoneField = pheromoneField;
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
	public LogoEnvPLS(
		int gridWidth,
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
		marks = new Set[this.width][this.height];
		patches = new Point2D.Double[this.width][this.height];
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				turtlesInPatches[x][y] = new HashSet<>();
				marks[x][y] = new HashSet<>();
				patches[x][y] = new Point2D.Double(x, y);
			}
		}
		
	}
	
	/**
	 * Builds an initialized instance of this class for a given level.
	 * @param levelIdentifier the identifier of the level
	 * @param gridWidth The width of the grid.
	 * @param gridHeight The height of the grid.
	 * @param xAxisTorus <code>true</code> if the environment
	 * is toroidal along the x axis.
	 * @param yAxisTorus <code>true</code> if the environment
	 * is toroidal along the y axis.
	 * @param pheromones The set of pheromone fields in the environment.
	 */
	public LogoEnvPLS(
		LevelIdentifier levelIdentifier,
		int gridWidth,
		int gridHeight,
		boolean xAxisTorus,
		boolean yAxisTorus,
		Set<Pheromone> pheromones
	) {
		super(levelIdentifier);
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
		marks = new Set[this.width][this.height];
		patches = new Point2D.Double[this.width][this.height];
		for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				turtlesInPatches[x][y] = new HashSet<>();
				marks[x][y] = new HashSet<>();
				patches[x][y] = new Point2D.Double(x, y);
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
	public Collection<Point2D.Double> getNeighbors(int x, int y, int distance) {
		ArrayDeque<Point2D.Double> neighbors = new ArrayDeque<>();
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
					neighbors.add(patches[nx][ny]);
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
	public double getDirection(Point2D.Double from, Point2D.Double to) {
				
		double xtarget = to.x;
		double ytarget = to.y;
		if(this.xAxisTorus && abs(xtarget - from.x)*2 > this.width) {
			if(from.x > xtarget) {
				xtarget += this.width;
			} else {
				xtarget -= this.width;
			}
		}
		if(this.yAxisTorus && abs(ytarget - from.y)*2 > this.height) {
			if(from.y > ytarget) {
				ytarget += this.height;
			} else {
				ytarget -= this.height;
			}
		}
		return -atan2(xtarget-from.x, ytarget-from.y);
	}
	
	/**
	 * @param loc1 the location of the first point
	 * @param loc2 the location of the second point
	 * @return the distance between <code>loc1</code> and <code>loc2</code>
	 */
	public double getDistance(Point2D loc1, Point2D loc2) {
		double dx = abs(loc1.getX() - loc2.getX());
		double dy  = abs(loc1.getY() - loc2.getY());
		if(this.xAxisTorus && dx*2 > this.width) {
			dx = this.width - dx;
		}
		if(this.yAxisTorus && dy*2 > this.height) {
			dy = this.height - dy;
		}
		
		return hypot(dx,dy);
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
	 * @return the public local states of the turtles located in patch x,y.
	 */
	public Set<TurtlePLSInLogo> getTurtlesAt(double x, double y) {
		return turtlesInPatches[(int) x][(int) y];
	}
	
	/**
	 * @return the public local states of the turtles.
	*/
	public Set<TurtlePLSInLogo> getTurtles() {
		Set<TurtlePLSInLogo> turtles = new HashSet<>();
		for (Set<TurtlePLSInLogo>[] array : turtlesInPatches) {
			for (Set<TurtlePLSInLogo> set : array) {
				turtles.addAll(set);
			}
		}
		return turtles;
	}
	
	/**
	 * @param agentCategory the category of the agents to get.
	 * @return the public local states of the turtles of category agentCategory.
	*/
	public Set<TurtlePLSInLogo> getTurtles(AgentCategory agentCategory) {
		Set<TurtlePLSInLogo> turtles = new HashSet<>();
		for (Set<TurtlePLSInLogo>[] array : turtlesInPatches) {
			for (Set<TurtlePLSInLogo> set : array) {
				for(TurtlePLSInLogo turtle : set) {
					if(turtle.getCategoryOfAgent().isA(agentCategory)) {
						turtles.add(turtle);
					}
				}
			}
		}
		return turtles;
	}
	
	/**
	 * @return the marks in the environment as a Set.
	*/
	public Set<Mark> getMarksAsSet() {
		Set<Mark> marks = new HashSet<>();	
		for (Set<Mark>[] array : this.marks) {
			for (Set<Mark> set : array) {
				marks.addAll(set);
			}
		}
		return marks;
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
	 * @param x the x coordinate of the patch.
	 * @param y the y coordinate of the patch.
	 * @return the marks located in patch x,y.
	 */
	public Set<Mark> getMarksAt(double x, double y) {
		return marks[(int) x][(int) y];
	}
	
	/**
	 * @param pheromone a pheromone
	 * @param x the x coordinate of the patch.
	 * @param y the y coordinate of the patch.
	 * @return the value of pheromone in patch x,y.
	 */
	public double getPheromoneValueAt(Pheromone pheromone, int x, int y) {
		return pheromoneField.get(pheromone)[x][y];
	}
	
	/**
	 * @param pheromone a pheromone
	 * @param x the x coordinate of the patch.
	 * @param y the y coordinate of the patch.
	 * @return the value of pheromone in patch x,y.
	 */
	public double getPheromoneValueAt(Pheromone pheromone, double x, double y) {
		return pheromoneField.get(pheromone)[(int) x][(int) y];
	}
	
	/**
	 * @param pheromone a pheromone
	 * @return the values of pheromone.
	 */
	public double[][] getPheromoneValues(Pheromone pheromone) {
		return pheromoneField.get(pheromone);
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
	 * {@inheritDoc}
	 */
	@Override
	public Object clone() {
		 Set<TurtlePLSInLogo>[][] turtlesInPatches = new Set[this.width][this.height];
		 Set<Mark>[][] marks = new Set[this.width][this.height];
		 for(int x = 0; x < this.width; x++) {
			for(int y = 0; y < this.height; y++) {
				turtlesInPatches[x][y] = new HashSet();
				marks[x][y] = new HashSet();
				if (!this.marks[x][y].isEmpty()) {
					for(Mark mark: this.marks[x][y]) {
						marks[x][y].add((Mark) mark.clone());
					}
				}
			}
		 }
		Map<Pheromone, double[][]> pheromoneField  = new HashMap<>();
		for( Pheromone pheromone : this.pheromoneField.keySet()) {
			pheromoneField.put(pheromone, (double[][]) this.pheromoneField.get(pheromone).clone());
		}
		LogoEnvPLS env = new LogoEnvPLS(
			width,
			height,
			xAxisTorus,
			yAxisTorus,
			turtlesInPatches,
			marks,
			pheromoneField	
		);
		return env;
	}

	private static boolean equalsSetTable (Set<?>[][] set1, Set<?>[][] set2) {
		//If the lengths are different, the set tables can't be equal.
		if (!(set1.length == set2.length) && !(set1[0].length == set2[0].length) ) {
			return false;
		}
		//We check the content of each set at each position
		for (int i=0 ; i < set1.length; i++) {
			for (int j=0 ; j < set1[0].length; j++) {
				Set<?> s1 = set1[i][j];
				Set<?> s2 = set2[i][j];
				for (Object obj : s1) {
					if (!s2.contains(obj)) {
						return false;
					}
				}
				for (Object obj : s2) {
					if (!s1.contains(obj)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private static boolean equalsMapTable (Map<?, double[][]> map1, Map<?, double[][]> map2) {
		//We check the keys first
		if (!map1.keySet().equals(map2.keySet())) {
			return false;
		}
		//We check the value for each key
		for (Object o : map1.keySet()) {
			if ((map1.get(o).length != map2.get(o).length) || (map1.get(o)[0].length != map2.get(o)[0].length)) {
				return false;
			}
			for (int i = 0; i < map1.get(o).length; i++) {
				for (int j= 0; j < map1.get(o)[0].length; j++) {
					if (!MathUtil.areEqual(map1.get(o)[i][j], map2.get(o)[i][j])) {
						return false;
					}
				}
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + Arrays.deepHashCode(marks);
		result = prime * result + ((pheromoneField == null) ? 0 : pheromoneField.hashCode());
		result = prime * result + Arrays.deepHashCode(turtlesInPatches);
		result = prime * result + width;
		result = prime * result + (xAxisTorus ? 1231 : 1237);
		result = prime * result + (yAxisTorus ? 1231 : 1237);
		return result;
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
		LogoEnvPLS lep = (LogoEnvPLS) o;
		return ((this.width == lep.getWidth()) && (this.height == lep.getHeight()) && (this.xAxisTorus == lep.isxAxisTorus())
				&& (this.yAxisTorus == lep.isyAxisTorus()) 
				&& (equalsMapTable(this.pheromoneField, lep.getPheromoneField()))
				&& (equalsSetTable(this.turtlesInPatches, lep.getTurtlesInPatches())) 
				&& (equalsSetTable(this.marks, lep.getMarks())));
	}

	
}
