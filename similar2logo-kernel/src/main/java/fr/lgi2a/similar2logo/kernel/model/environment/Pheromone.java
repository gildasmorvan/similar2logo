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

/**
 * The object representing a pheromone.
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 */
public final class Pheromone {
	
	/**
	 * The string identifier of the pheromone.
	 */
	private final String identifier;
	
	/**
	 * The diffusion coefficient of the pheromone.
	 */
	private final double diffusionCoef;
	
	/**
	 * The evaporation coefficient of the pheromone.
	 */
	private final double evaporationCoef;
	
	/**
	 * The amount of the pheromone in the environment when initiating the simulation.
	 */
	private final double defaultValue;
	
	/**
	 * The minimal value of the pheromone can take 
	 */
	private final double minValue;
	
	/**
	 * The error message displayed when the first argument is null.
	 */
	private static final String ERROR_MESSAGE_IDENTIFIER = "The first argument cannot be null.";
	
	/**
	 * The error message displayed when the diffusion and evaporation coefs are inferior to 0.
	 */
	private static final String ERROR_MESSAGE_COEFS = "The diffusion and evaporation coefs cannot be inferior to 0.";
	
	/**
	 * Builds an instance of this class using a specific value for the level identifier.
	 * @param identifier The identifier of the pheromone. This value should be unique.
	 * @param diffusionCoef The diffusion coefficient of the pheromone.
	 * @param evaporationCoef The evaporation coefficient of the pheromone.
	 * @throws IllegalArgumentException If <code>identifier</code> is <code>null</code>.
	 */
	public Pheromone(
		String identifier,
		double diffusionCoef,
		double evaporationCoef
	) {
		if( identifier == null ){
			throw new IllegalArgumentException(ERROR_MESSAGE_IDENTIFIER);
		}
		this.identifier = identifier;
		if( diffusionCoef < 0 || evaporationCoef < 0 ){
			throw new IllegalArgumentException(ERROR_MESSAGE_COEFS);
		}
		this.diffusionCoef = diffusionCoef;
		this.evaporationCoef = evaporationCoef;
		this.defaultValue = 0;
		this.minValue = 0;
	}
	
	/**
	 * Builds an instance of this class using a specific value for the level identifier.
	 * @param identifier The identifier of the pheromone. This value should be unique.
	 * @param diffusionCoef The diffusion coefficient of the pheromone.
	 * @param evaporationCoef The evaporation coefficient of the pheromone.
	 * @param defaultValue The amount of the pheromone in the environment when initiating the simulation.
	 * @throws IllegalArgumentException If <code>identifier</code> is <code>null</code>.
	 */
	public Pheromone(
		String identifier,
		double diffusionCoef,
		double evaporationCoef,
		double defaultValue
	) {
		if( identifier == null ){
			throw new IllegalArgumentException(ERROR_MESSAGE_IDENTIFIER);
		}
		this.identifier = identifier;
		if( diffusionCoef < 0 || evaporationCoef < 0 || defaultValue < 0){
			throw new IllegalArgumentException(ERROR_MESSAGE_COEFS);
		}
		this.diffusionCoef = diffusionCoef;
		this.evaporationCoef = evaporationCoef;
		this.defaultValue = defaultValue;
		this.minValue = 0;
	}
	
	/**
	 * Builds an instance of this class using a specific value for the level identifier.
	 * @param identifier The identifier of the pheromone. This value should be unique.
	 * @param diffusionCoef The diffusion coefficient of the pheromone.
	 * @param evaporationCoef The evaporation coefficient of the pheromone.
	 * @param minValue The minimal value of the pheromone can take.
	 * @param defaultValue The amount of the pheromone in the environment when initiating the simulation.
	 * @throws IllegalArgumentException If <code>identifier</code> is <code>null</code>.
	 */
	public Pheromone(
		String identifier,
		double diffusionCoef,
		double evaporationCoef,
		double defaultValue, 
		double minValue
	) {
		if( identifier == null ){
			throw new IllegalArgumentException(ERROR_MESSAGE_IDENTIFIER);
		}
		this.identifier = identifier;
		if( diffusionCoef < 0 || evaporationCoef < 0 || defaultValue < 0){
			throw new IllegalArgumentException(ERROR_MESSAGE_COEFS);
		}
		this.diffusionCoef = diffusionCoef;
		this.evaporationCoef = evaporationCoef;
		this.defaultValue = defaultValue;
		this.minValue = minValue;
	}
	
	/**
	 * Gets a printable version of the pheromone identifier.
	 * 
	 * @return A string representation of the pheromone identifier.
	 */
	@Override
	public String toString() {
		return this.identifier;
	}
	
	/**
	 * Check if this pheromone identifier is equal to another pheromone identifier.
	 * @param o The other object used to check equality.
	 * @return <code>true</code> if the two objects are equal, <i>i.e.</i> if they are both 
	 * pheromone identifiers having the same string identifier.
	 */
	@Override
	public boolean equals( Object o ) {
		if( o == null ){
			return false;
		} else {
			if( ! ( o instanceof Pheromone )  ){
				return false;
			} else {
				Pheromone otherIdentifier = (Pheromone) o;
				return this.identifier.equals( otherIdentifier.identifier );
			}
		}
	}
	
	/**
	 * Gets the hash code of this object.
	 * @return The hash code of this object.
	 */
	@Override
	public int hashCode(){
		return this.identifier.hashCode();
	}

	/**
	 * @return the diffusion coefficient of the pheromone.
	 */
	public double getDiffusionCoef() {
		return diffusionCoef;
	}

	/**
	 * @return the evaporation coefficient of the pheromone.
	 */
	public double getEvaporationCoef() {
		return evaporationCoef;
	}

	/**
	 * @return the string identifier of the pheromone.
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * @return the amount of the pheromone in the environment when initiating the simulation.
	 */
	public double getDefaultValue() {
		return defaultValue;
	}
	
	/**
	 * @return the minimal value of the pheromone can take in the simulation
	 */
	public double getMinValue(){
		return this.minValue;
	}
}
