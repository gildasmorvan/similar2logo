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
package fr.lgi2a.similar2logo.examples.predation.exploration.treatment;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;

import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.IntArrayVector;

import fr.lgi2a.similar2logo.examples.predation.exploration.data.SimulationDataPreyPredator;
import fr.lgi2a.similar2logo.lib.exploration.ExplorationSimulationModel;
import fr.lgi2a.similar2logo.lib.exploration.treatment.ITreatment;

/**
 * Treatment for the prey predator simulation.
 * Works with R and a clustering algorithm.
 * @author <a href="mailto:romainwindels@yahoo.fr>Romain Windels</a>
 */
public class PreyPredatorExplorationTreatment implements ITreatment {
	
	/**
	 * The number of centroids and the number of clones
	 */
	private int k,n;
	
	/**
	 * Create a new PreyPredatorExplorationTreatment
	 * @param k the number of centroid
	 * @param n the number of clone
	 */
	public PreyPredatorExplorationTreatment(int k, int n) {
		this.k =k;
		this.n = n;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ExplorationSimulationModel> treatSimulations(List<ExplorationSimulationModel> currentSimulations) {
		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	    // create a Renjin engine:
	    ScriptEngine engine = factory.getScriptEngine();
	    try {
	    	List<ExplorationSimulationModel> toKeep = new ArrayList<>();
	    	List<Double> donnees = new ArrayList<>();
	    	/*for (int i=0; i < currentSimulations.size(); i++) {
	    		SimulationDataPreyPredator data = (SimulationDataPreyPredator) currentSimulations.get(i).getData();
	    		donnees.add((double) data.getId());
	    	}*/
	    	for (int i=0 ; i < (currentSimulations.size()); i++) {
	    		SimulationDataPreyPredator data = (SimulationDataPreyPredator) currentSimulations.get(i).getData();
	    		donnees.add((double) data.getNbOfPreys());
	    	}
	    	for (int i=0 ; i < (currentSimulations.size()); i++) {
	    		SimulationDataPreyPredator data = (SimulationDataPreyPredator) currentSimulations.get(i).getData();
	    		donnees.add((double) data.getNbOfPredators());
	    	}
	    	for (int i=0 ; i < (currentSimulations.size()); i++) {
	    		SimulationDataPreyPredator data = (SimulationDataPreyPredator) currentSimulations.get(i).getData();
	    		donnees.add(data.getNbOfGrass());
	    	}
	    	/*for (int i=0 ; i < (currentSimulations.size()); i++) {
	    		SimulationDataPreyPredator data = (SimulationDataPreyPredator) currentSimulations.get(i).getData();
	    		donnees.add((double) data.getNbOfPreys() - data.getLastNbOfPreys());
	    	}
	    	for (int i=0 ; i < (currentSimulations.size()); i++) {
	    		SimulationDataPreyPredator data = (SimulationDataPreyPredator) currentSimulations.get(i).getData();
	    		donnees.add((double) data.getNbOfPredators() - data.getLastNbOfPredators());
	    	}*/
	    	org.renjin.sexp.DoubleVector res = new org.renjin.sexp.DoubleArrayVector(donnees);
	    	engine.put("k",k);
	    	engine.put("datapp",res);
	    	engine.eval("dim(datapp) <- c("+currentSimulations.size()+",3)");
	    	//engine.eval("print(datapp)");
	    	IntArrayVector resVector = (IntArrayVector) engine.eval(new FileReader(new File("./R/kmeans.r")));
	    	//engine.eval("print(observations_to_keep)");
	    	for(int i = 0; i < resVector.length(); i++) {
	    		int pos = (int) resVector.getElementAsSEXP(i).asReal();
	    		currentSimulations.get(pos-1).getData().setId(i);
	    		for (int j=0 ; j <n; j++) {
	    			toKeep.add(currentSimulations.get(pos-1).makeCopy(currentSimulations.get(pos-1).getData()));
	    		}
	    	}
	    	return toKeep;
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	    return null;
	}

}
