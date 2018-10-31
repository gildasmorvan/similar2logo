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
package fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle;

import java.util.Collection;
import java.util.Map;

import fr.univ_artois.lgi2a.similar.extendedkernel.agents.IAgtPerceptionModel;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar.microkernel.libs.abstractimpl.AbstractPerceivedData;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.environment.Mark;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

/**
 * Models the data perceived by a turtle using a {@link IAgtPerceptionModel}. 
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
@SuppressWarnings("rawtypes")
public class TurtlePerceivedData extends AbstractPerceivedData {

	/**
	 * The perceived turtles
	 */
	private final Collection<LocalPerceivedData<TurtlePLSInLogo>> turtles;
	
	/**
	 * The perceived marks
	 */
	private final Collection<LocalPerceivedData<Mark>> marks;
	
	/**
	 * The perceived pheromones
	 */
	private final Map<String,Collection<LocalPerceivedData<Double>>> pheromones;
	
	/**
	 * Builds a set of data perceived by a turtle in the Logo level.
	 * @param transitoryPeriodMin The lower bound of the transitory period for which these data were perceived. 
	 * @param transitoryPeriodMax The upper bound of the transitory period for which these data were perceived.
	 * @param turtles The perceived turtles.
	 * @param marks The perceived marks.
	 * @param pheromones The perceived pheromones.
	 * @throws IllegalArgumentException If an argument is <code>null</code>.
	 */
	public TurtlePerceivedData(
		SimulationTimeStamp transitoryPeriodMin,
		SimulationTimeStamp transitoryPeriodMax,
		Collection<LocalPerceivedData<TurtlePLSInLogo>> turtles,
		Collection<LocalPerceivedData<Mark>> marks,
		Map<String,Collection<LocalPerceivedData<Double>>> pheromones
	) {
		super(LogoSimulationLevelList.LOGO, transitoryPeriodMin, transitoryPeriodMax);
		this.turtles = turtles;
		this.marks = marks;
		this.pheromones = pheromones;
		
	}

	/**
	 * Models the data about a perceived entity (a {@link TurtlePLSInLogo}, 
	 * a {@link Mark} or a {@link TurtlePLSInLogo}) that depends on the topology
	 * of the environment. 
	 * 
	 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
	 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
	 * @param <E> represents the type of perceived data.
	 */
	public static class LocalPerceivedData<E>{
		
		/**
		 * The data about the perceived entity.
		 */
		private final E content;
		
		/**
		 * The distance to the perceived entity.
		 */
		private final double distanceTo;
		
		/**
		 * The direction of the perceived entity.
		 */
		private final double directionTo;

		/**
		 * @param content the data about a perceived entity.
		 * @param distanceTo the distance to the perceived entity.
		 * @param directionTo the direction of the perceived entity.
		 */
		public LocalPerceivedData(
			E content,
			double distanceTo,
			double directionTo
		) {
			this.distanceTo = distanceTo;
			this.directionTo = directionTo;
			this.content = content;
		}
		
		/**
		 * @return the distance to the perceived entity.
		 */
		public double getDistanceTo() {
			return distanceTo;
		}

		/**
		 * @return the direction to the perceived entity.
		 */
		public double getDirectionTo() {
			return directionTo;
		}

		/**
		 * @return the data about the perceived entity.
		 */
		public E getContent() {
			return content;
		}
	}

	/**
	 * @return the turtles
	 */
	public Collection<LocalPerceivedData<TurtlePLSInLogo>> getTurtles() {
		return turtles;
	}

	/**
	 * @return the marks
	 */
	public Collection<LocalPerceivedData<Mark>> getMarks() {
		return marks;
	}

	/**
	 * @return the pheromones
	 */
	public Map<String, Collection<LocalPerceivedData<Double>>> getPheromones() {
		return pheromones;
	}

}
