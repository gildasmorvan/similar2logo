package fr.univ_artois.lgi2a.similar2logo.kernel.model.environment;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;

/**
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ConnectedLogoEnvPLS extends LogoEnvPLS {

	/**
	 * Map of the connected levels
	 */
	private Map<Double, ConnectedLogoEnvPLS> connexions;

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
		Map<Double, ConnectedLogoEnvPLS> connexions,
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
		this.connexions = connexions;
		
	}
	
	/**
	 * Computes the positions of the neighbors of a patch.
	 * @param x The x coordinate of the patch.
	 * @param y The y coordinate of the patch.
	 * @param distance The maximal distance of neighbors.
	 * @return the positions of the patch neighbors.
	 */
	@Override
	public Collection<Point> getNeighbors(int x, int y, int distance) {
		ArrayDeque<Point> neighbors = new ArrayDeque<>();
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
				} else if(nx < 0 && ny >=0 && connexions.containsKey(WEST) ) {
					neighbors.add(
						connexions.get(WEST).patches[connexions.get(WEST).width+nx][ny]
					);
				} else if(ny < 0 && nx >=0 && connexions.containsKey(SOUTH)) {
					neighbors.add(
						connexions.get(SOUTH).patches[nx][connexions.get(SOUTH).height+ny]
					);
				} else if(nx >= width && ny >=0 && connexions.containsKey(EAST) ) {
					neighbors.add(
						connexions.get(EAST).patches[nx-width][ny]
					);
				} else if(ny >= height && nx >=0 && connexions.containsKey(NORTH) ) {
					neighbors.add(
						connexions.get(NORTH).patches[nx][ny-height]
					);
				} else if(nx < 0 && ny < 0 && connexions.containsKey(SOUTH_WEST) ) {
					neighbors.add(
						connexions.get(SOUTH_WEST).patches[connexions.get(SOUTH_WEST).width+nx][connexions.get(SOUTH_WEST).height+ny]
					);
				} else if(nx < 0 && ny >= height && connexions.containsKey(NORTH_WEST) ) {
					neighbors.add(
						connexions.get(NORTH_WEST).patches[connexions.get(NORTH_WEST).width+nx][ny-height]
					);
				} else if(nx >= width && ny >= height && connexions.containsKey(NORTH_EAST) ) {
					neighbors.add(
						connexions.get(NORTH_EAST).patches[nx-width][ny-height]
					);
				} else if(nx >= width && ny < 0 && connexions.containsKey(SOUTH_EAST) ) {
					neighbors.add(
						connexions.get(SOUTH_EAST).patches[nx-width][connexions.get(SOUTH_EAST).height+ny]
					);
				}
			}	
		}
		return neighbors;
	}
	
	/**
	 * @param connexion the direction of the connected level.
	 * @param position the position of the patch.
	 * @return the public local states of the turtles located in patch position in the given connexion.
	 */
	public Set<TurtlePLSInLogo> getTurtlesAt(Double connexion, Point2D position) {
		return connexions.get(connexion).getTurtlesAt(position);
	}
	
	/**
	 * @param loc1 the location of the first situated entity.
	 * @param loc2 the location of the second situated entity.
	 * @param connexion the direction of the connected level in which loc2 is situated.
	 * @return the distance between <code>loc1</code> and <code>loc2</code>
	 */
	public double getDistance(SituatedEntity loc1, SituatedEntity loc2, Double connexion) {		
		//TODO
		return getDistance(loc1.getLocation(), loc2.getLocation());
	}
	
}
