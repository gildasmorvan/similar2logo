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
package fr.lgi2a.similar2logo.examples.transport.exploration.data;

import java.util.HashSet;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;
import fr.lgi2a.similar2logo.lib.exploration.tools.SimulationData;

/**
 * Simulation data for the transport simulation exploration
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class SimulationDataTransport extends SimulationData implements Cloneable {
	
	/**
	 * The number of cars by section.
	 */
	private int[][] numberCars;
	
	/**
	 * The mean frequency by section
	 */
	private double[][] meanFrequencies;

	public SimulationDataTransport(SimulationTimeStamp startTime, int m, int n) {
		super(startTime,0);
		numberCars = new int[m][n];
		meanFrequencies = new double[m][n];
	}
	
	/**
	 * Gives the matrix of the number of cars
	 * @return the matrix of the number of cars
	 */
	public int[][] getNumberCars () {
		return numberCars;
	}
	
	/**
	 * Set the matrix of the number of cars
	 * @param matrix the new matrix with the number of car
	 * @exception Exception if the matrix doesn't have the good size
	 */
	public void setNumberCars (int[][] matrix) throws Exception {
		if (matrix.length != numberCars.length || matrix[0].length != numberCars[0].length)
			throw new Exception ("Size doesn't match.");
		this.numberCars = matrix;
	}
	
	/**
	 * Gives the matrix of the mean frequencies
	 * @return the matrix of the mean frequencies
	 */
	public double[][] getMeanFrequencies () {
		return this.meanFrequencies;
	}
	
	/**
	 * Set the matrix of the mean frequencies
	 * @param matrix the new matrix of the mean frequencies
	 * @exception Exception if the matrix doesn't have the good size.
	 */
	public void setMeanFrequencies (double[][] matrix) throws Exception {
		if (matrix.length != meanFrequencies.length || matrix[0].length != meanFrequencies[0].length)
			throw new Exception ("Size doesn't match.");
		this.meanFrequencies = matrix;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Object clone () {
		SimulationDataTransport sdt = new SimulationDataTransport(currentTime, numberCars.length, numberCars[0].length);
		sdt.agents = new HashSet<>();
		for (TurtlePLSInLogo turtle : agents) {
			sdt.agents.add((TurtlePLSInLogo) turtle.clone());
		}
		sdt.environment = (LogoEnvPLS) this.environment.clone();
		sdt.currentTime = new SimulationTimeStamp(currentTime.getIdentifier());
		sdt.endTime = new SimulationTimeStamp(endTime.getIdentifier());
		int[][] cars = new int[numberCars.length][numberCars[0].length];
		double[][] frequencies = new double[meanFrequencies.length][meanFrequencies[0].length];
		for (int i = 0 ; i < numberCars.length; i++) {
			for (int j = 0; j < numberCars[0].length; j++) {
				cars[i][j] = numberCars[i][j];
				frequencies[i][j] = meanFrequencies[i][j];
			}
		}
		try {
			sdt.setNumberCars(cars);
			sdt.setMeanFrequencies(frequencies);
		} catch (Exception e) {
			System.out.println("I don't understand...");
		}
		return sdt;
	}
	
	public String getData() {
		String res = "";
		for (int i=0; i < numberCars.length; i++) {
			for (int j=0; j < numberCars[0].length; j++) {
				res += i+" "+j+" "+numberCars[i][j]+" "+meanFrequencies[i][j]+"\n";
			}
		}
		return res;
	}

}
