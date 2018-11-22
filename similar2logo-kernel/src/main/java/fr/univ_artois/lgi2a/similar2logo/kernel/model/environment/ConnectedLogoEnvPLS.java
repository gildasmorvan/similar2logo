package fr.univ_artois.lgi2a.similar2logo.kernel.model.environment;

import java.awt.Point;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;

/**
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
public class ConnectedLogoEnvPLS extends LogoEnvPLS {

	/**
	 * Map of the connected levels
	 */
	private Map<Double, LevelIdentifier> connexions;

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
		Map<Double, LevelIdentifier> connexions,
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
				if(this.isxAxisTorus()) {
					nx = ( ( nx % this.getWidth() ) + this.getWidth() ) % this.getWidth();
				}
				if(this.isyAxisTorus()) {
					ny =  ( ( ny % this.getHeight() ) + this.getHeight() ) % this.getHeight();
				}
				if(nx >=0 && nx < this.getWidth() && ny >=0 && ny < this.getHeight()) {
					neighbors.add(new Point(nx, ny));
				} else if(connexions.containsKey(SOUTH) && nx < 0) {
					
				}
			}	
		}
		return neighbors;
	}
	
}
