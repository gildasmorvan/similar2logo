package fr.univ_artois.lgi2a.similar2logo.kernel.model.environment;

import static fr.univ_artois.lgi2a.similar2logo.kernel.tools.MathUtil.*;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.environment.ILocalStateOfEnvironment;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;

/**
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ConnectedLogoEnvPLS extends LogoEnvPLS {

	/**
	 * Map of the connected levels
	 */
	private Map<Double, ConnectedLogoEnvPLS> connections;

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
	public ConnectedLogoEnvPLS(
		LevelIdentifier levelIdentifier,
		int gridWidth,
		int gridHeight,
		boolean xAxisTorus,
		boolean yAxisTorus,
		Set<Pheromone> pheromones
	) {
		
		super(
			levelIdentifier,
			gridWidth,
			gridHeight,
			xAxisTorus,
			yAxisTorus,
			pheromones
		);
		this.connections = new HashMap<>();
		
	}
	
	/**
	 * Computes the positions of the neighbors of a patch.
	 * @param x The x coordinate of the patch.
	 * @param y The y coordinate of the patch.
	 * @param distance The maximal distance of neighbors.
	 * @return the positions of the patch neighbors.
	 */
	public Map<Double,Point> getMultiLevelNeighbors(int x, int y, int distance) {
		HashMap<Double,Point> neighbors = new HashMap<>();
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
					neighbors.put(Double.NaN, patches[nx][ny]);
				} else if(nx < 0 && ny >=0 && connections.containsKey(WEST) ) {
					neighbors.put(
						WEST, connections.get(WEST).patches[connections.get(WEST).width+nx][ny]
					);
				} else if(ny < 0 && nx >=0 && connections.containsKey(SOUTH)) {
					neighbors.put(
						SOUTH, connections.get(SOUTH).patches[nx][connections.get(SOUTH).height+ny]
					);
				} else if(nx >= width && ny >=0 && connections.containsKey(EAST) ) {
					neighbors.put(
						EAST, connections.get(EAST).patches[nx-width][ny]
					);
				} else if(ny >= height && nx >=0 && connections.containsKey(NORTH) ) {
					neighbors.put(
						NORTH, connections.get(NORTH).patches[nx][ny-height]
					);
				} else if(nx < 0 && ny < 0 && connections.containsKey(SOUTH_WEST) ) {
					neighbors.put(
						SOUTH_WEST, connections.get(SOUTH_WEST).patches[connections.get(SOUTH_WEST).width+nx][connections.get(SOUTH_WEST).height+ny]
					);
				} else if(nx < 0 && ny >= height && connections.containsKey(NORTH_WEST) ) {
					neighbors.put(
						NORTH_WEST, connections.get(NORTH_WEST).patches[connections.get(NORTH_WEST).width+nx][ny-height]
					);
				} else if(nx >= width && ny >= height && connections.containsKey(NORTH_EAST) ) {
					neighbors.put(
						NORTH_EAST,
						connections.get(NORTH_EAST).patches[nx-width][ny-height]
					);
				} else if(nx >= width && ny < 0 && connections.containsKey(SOUTH_EAST) ) {
					neighbors.put(
						SOUTH_EAST,
						connections.get(SOUTH_EAST).patches[nx-width][connections.get(SOUTH_EAST).height+ny]
					);
				}
			}	
		}
		return neighbors;
	}
	
	/**
	 * @param connection the direction of the connected level.
	 * @param position the position of the patch.
	 * @return the public local states of the turtles located in patch position in the given connection.
	 */
	@SuppressWarnings("rawtypes")
	public Set<Mark> getMarksAt(Double connection, Point2D position) {
		if(connection.isNaN()) {
			return getMarksAt(position);
		}
		return connections.get(connection).getMarksAt(position);
	}
	
	/**
	 * @param connection the direction of the connected level.
	 * @param position the position of the patch.
	 * @return the public local states of the turtles located in patch position in the given connection.
	 */
	public Set<TurtlePLSInLogo> getTurtlesAt(Double connection, Point2D position) {
		if(connection.isNaN()) {
			return getTurtlesAt(position);
		}
		return connections.get(connection).getTurtlesAt(position);
	}
	
	/**
	 * @param loc1 the location of the first situated entity.
	 * @param loc2 the location of the second situated entity.
	 * @param connection the direction of the connected level in which loc2 is situated.
	 * @return the distance between <code>loc1</code> and <code>loc2</code>
	 */
	public double getDistance(Point2D loc1, Point2D loc2, Double connection) {		
		
		if(connection.isNaN()) {
			return getDistance(loc1, loc2);
		} else if(!connections.containsKey(connection)) {
			return Double.POSITIVE_INFINITY;
		}
		if(areEqual(NORTH, connection)) {
			return getDistance(
				loc1,
				new Point2D.Double(
					loc2.getX(),
					height-loc1.getY()+loc2.getY()
				)
			);
		} else if(areEqual(SOUTH, connection)) {
			return getDistance(
				loc1,
				new Point2D.Double(
					loc2.getX(),
					loc1.getY()+connections.get(connection).height-loc2.getY()
				)
			);
		} else if(areEqual(EAST, connection)) {
			return getDistance(
				loc1,
				new Point2D.Double(
					width-loc1.getX()+loc2.getX(),
					loc2.getY()
				)
			);
		} else if(areEqual(WEST, connection)) {
			return getDistance(
				loc1,
				new Point2D.Double(
					loc1.getX()+connections.get(connection).width-loc2.getX(),
					loc2.getY()
				)
			);
		} else if(areEqual(NORTH_EAST, connection)) {
			return getDistance(
				loc1,
				new Point2D.Double(
					width-loc1.getX()+loc2.getX(),
					height-loc1.getY()+loc2.getY()
				)
			);
		} else if(areEqual(NORTH_WEST, connection)) {
			return getDistance(
				loc1,
				new Point2D.Double(
					loc1.getX()+connections.get(connection).width-loc2.getX(),
					height-loc1.getY()+loc2.getY()
				)
			);
		} else if(areEqual(SOUTH_EAST, connection)) {
			return getDistance(
				loc1,
				new Point2D.Double(
					width-loc1.getX()+loc2.getX(),
					loc1.getY()+connections.get(connection).height-loc2.getY()
				)
			);
		} else if(areEqual(SOUTH_WEST, connection)) {
			return getDistance(
				loc1,
				new Point2D.Double(
					loc1.getX()+connections.get(connection).width-loc2.getX(),
					loc1.getY()+connections.get(connection).height-loc2.getY()
				)
			);
		}
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * @param loc1 the location of the first situated entity.
	 * @param loc2 the location of the second situated entity.
	 * @param connection the direction of the connected level in which loc2 is situated.
	 * @return the distance between <code>loc1</code> and <code>loc2</code>
	 */
	public double getDistance(SituatedEntity loc1, SituatedEntity loc2, Double connection) {		
		
		return getDistance(loc1.getLocation(), loc2.getLocation(), connection);
	}
	
	/**
	 * @param loc1 the location of the first situated entity.
	 * @param loc2 the location of the second situated entity.
	 * @param connection the direction of the connected level in which loc2 is situated.
	 * @return the distance between <code>loc1</code> and <code>loc2</code>
	 */
	public double getDirection(Point2D loc1, Point2D loc2, Double connection) {		
		if(!connections.containsKey(connection)) {
			return Double.POSITIVE_INFINITY;
		}
		if(areEqual(NORTH, connection)) {
			return getDirection(
				loc1,
				new Point2D.Double(
					loc2.getX(),
					height-loc1.getY()+loc2.getY()
				)
			);
		} else if(areEqual(SOUTH, connection)) {
			return getDirection(
				loc1,
				new Point2D.Double(
					loc2.getX(),
					loc1.getY()+connections.get(connection).height-loc2.getY()
				)
			);
		} else if(areEqual(EAST, connection)) {
			return getDirection(
				loc1,
				new Point2D.Double(
					width-loc1.getX()+loc2.getX(),
					loc2.getY()
				)
			);
		} else if(areEqual(WEST, connection)) {
			return getDirection(
				loc1,
				new Point2D.Double(
					loc1.getX()+connections.get(connection).width-loc2.getX(),
					loc2.getY()
				)
			);
		} else if(areEqual(NORTH_EAST, connection)) {
			return getDirection(
				loc1,
				new Point2D.Double(
					width-loc1.getX()+loc2.getX(),
					height-loc1.getY()+loc2.getY()
				)
			);
		} else if(areEqual(NORTH_WEST, connection)) {
			return getDirection(
				loc1,
				new Point2D.Double(
					loc1.getX()+connections.get(connection).width-loc2.getX(),
					height-loc1.getY()+loc2.getY()
				)
			);
		} else if(areEqual(SOUTH_EAST, connection)) {
			return getDirection(
				loc1,
				new Point2D.Double(
					width-loc1.getX()+loc2.getX(),
					loc1.getY()+connections.get(connection).height-loc2.getY()
				)
			);
		} else if(areEqual(SOUTH_WEST, connection)) {
			return getDirection(
				loc1,
				new Point2D.Double(
					loc1.getX()+connections.get(connection).width-loc2.getX(),
					loc1.getY()+connections.get(connection).height-loc2.getY()
				)
			);
		}
		return Double.POSITIVE_INFINITY;
	}
	
	/**
	 * @param loc1 the location of the first situated entity.
	 * @param loc2 the location of the second situated entity.
	 * @param connection the direction of the connected level in which loc2 is situated.
	 * @return the distance between <code>loc1</code> and <code>loc2</code>
	 */
	public double getDirection(SituatedEntity loc1, SituatedEntity loc2, Double connection) {		
		return getDirection(loc1.getLocation(), loc2.getLocation(), connection);
	}

	/**
	 * @return the connections
	 */
	public Map<Double, ConnectedLogoEnvPLS> getConnections() {
		return connections;
	}
	
	/**
	 * @param the direction of the connection.
	 * @return the connection
	 */
	public ConnectedLogoEnvPLS getConnection(Double direction) {
		return connections.getOrDefault(direction, this);
	}
	
	/**
	 * @param the direction of the connection.
	 * @return the connection
	 */
	public ConnectedLogoEnvPLS addConnection(Double direction, ILocalStateOfEnvironment environment) {
		return connections.put(direction, (ConnectedLogoEnvPLS) environment);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((connections == null) ? 0 : connections.hashCode());
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		ConnectedLogoEnvPLS other = (ConnectedLogoEnvPLS) obj;
		if (connections == null) {
			if (other.connections != null) {
				return false;
			}
		} else if (!connections.equals(other.connections)) {
			return false;
		}
		return true;
	}
	
	
	
}

