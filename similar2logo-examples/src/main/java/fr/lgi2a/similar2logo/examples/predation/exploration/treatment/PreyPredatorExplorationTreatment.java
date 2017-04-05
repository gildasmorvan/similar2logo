package fr.lgi2a.similar2logo.examples.predation.exploration.treatment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;

import org.renjin.script.RenjinScriptEngineFactory;
import org.renjin.sexp.*;

import fr.lgi2a.similar2logo.examples.predation.exploration.data.SimulationDataPreyPredator;
import fr.lgi2a.similar2logo.lib.exploration.ExplorationSimulationModel;
import fr.lgi2a.similar2logo.lib.exploration.treatment.ITreatment;

public class PreyPredatorExplorationTreatment implements ITreatment {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void treatSimulations(List<ExplorationSimulationModel> currentSimulations) {
		RenjinScriptEngineFactory factory = new RenjinScriptEngineFactory();
	    // create a Renjin engine:
	    ScriptEngine engine = factory.getScriptEngine();
	    try {
	    	List<ExplorationSimulationModel> toKeep = new ArrayList<>();
	    	String preys = "c(";
	    	String predators = "c(";
	    	String grass = "c(";
	    	for (int i=0 ; i < (currentSimulations.size() -1); i++) {
	    		SimulationDataPreyPredator data = (SimulationDataPreyPredator) currentSimulations.get(i).getData();
	    		preys += data.getNbrOfPreys()+",";
	    		predators += data.getNbOfPredator()+",";
	    		grass += data.getNbOfGrass()+",";
	    	}
	    	SimulationDataPreyPredator lastData = (SimulationDataPreyPredator) currentSimulations.get(currentSimulations.size()-1).getData();
	    	preys += lastData.getNbrOfPreys()+")";
	    	predators += lastData.getNbOfPredator()+")";
	    	grass += lastData.getNbOfGrass()+")";
	    	System.out.println(preys);
	    	System.out.println(predators);
	    	System.out.println(grass);
	    	//Part to redo when the clustering will be made
	    	/*ListVector res = (ListVector) engine.eval(new java.io.FileReader(
	    			new File(PreyPredatorExplorationTreatment.class.getResource("k-means_clustering.r").getPath())));
	    	System.out.println("cluster - type: "+ res.get("cluster").getTypeName() + ", dim: "+ ((IntArrayVector) res.get("cluster")).getElementAsInt(0));
	    	System.out.println("centers - type: "+ res.get("centers").getTypeName() + ", dim: "+ (res.get("centers").getAttributes().getDim()));
	    	for(int i = 0; i < res.get("centers").length(); i++) {
	    		double dav = ((DoubleArrayVector) res.get("centers")).get(i);
	    		toKeep.add(currentSimulations.get((int) dav));
	    	}*/
	    	
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	}

}
