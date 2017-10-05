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
package fr.lgi2a.similar2logo.examples.transport.model;

import java.io.BufferedReader;
import java.io.FileReader;

import org.json.JSONObject;

/**
 * Class for generate data for the parameters planning
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class TransportSimulationParametersGenerator {
	
	/**
	 * Gives the JSON with the default static parameters
	 * @return the JSON of the static default parameters
	 */
	public static JSONObject staticParametersByDefaultJSON () {
		JSONObject staticParameters = new JSONObject();
		try {
			staticParameters.put("speedFrequencyPerson", 14.4);
			staticParameters.put("speedFrequencyCar", 1.4);
			staticParameters.put("speedFrequencyTram", 2.4);
			staticParameters.put("speedFrequencyTrain", 1);
			staticParameters.put("carCapacity", 5);
			staticParameters.put("tramwayCapacity", 240);
			staticParameters.put("trainCapacity", 500);
			staticParameters.put("probaBecomeCar", 0.0025);
			staticParameters.put("probaBecomePerson",0.0025);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return staticParameters;
	}
	
	
	
	/**
	 * Gives the JSON with the static parameters from a file
	 * All the static parameters must be here
	 * The format must be the following for each line : name of the parameter + space + value of the parameter
	 * @param path the file where are the parameters
	 * @return the JSON with the static parameters
	 */
	public static JSONObject staticParametersFromFileJSON (String path) {
		JSONObject staticParameters = new JSONObject ();
		try {
			FileReader fr = new FileReader (path);
			BufferedReader br = new BufferedReader (fr);
			String l;
			while ((l = br.readLine()) != null) {
				String[] p = l.split(" ");
				if (p[0].equals("probaBecomeCar") || p[0].equals("probaBecomePerson"))
					staticParameters.put(p[0],Double.parseDouble(p[1]));
				else
					staticParameters.put(p[0], Integer.parseInt(p[1]));
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return staticParameters;
	}
	
	/**
	 * Gives the JSON with the default variable parameters
	 * @return the JSON of the default variable parameters
	 */
	public static JSONObject variableParametersByDefaultJSON () {
		JSONObject variableParameters = new JSONObject();
		try {
			variableParameters.put("probaCreatePerson", 0.001);
			variableParameters.put("probaCreateCar", 0.008);
			variableParameters.put("probaCreateTram", 0.005);
			variableParameters.put("probaCreateTrain", 0.005);
			variableParameters.put("probaLeaveHome", 0.0001);
			variableParameters.put("probaTakeTransport", 0.3);
			variableParameters.put("probaGoToSchool", 1);
			variableParameters.put("probaGoToShop",1);
			variableParameters.put("probaGoToRestaurant", 1);
			variableParameters.put("probaGoToDoctor",1);
			variableParameters.put("probaGoToBank",1);
			variableParameters.put("probaLeaveTownByTrain", 0.1);
			variableParameters.put("probaLeaveTownByTram", 0.1);
			variableParameters.put("probaLeaveTownByRoad", 0.1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return variableParameters;
	}
	
	/**
	 * Gives the JSON of the variable parameters from a file
	 * All the static parameters must be here
	 * The format must be the following for each line : name of the parameter + space + value of the parameter
	 * @param path the path where the file with the parameters is
	 * @return the JSON with the variable parameters
	 */
	public static JSONObject variableParametersFromFileJSON (String path) {
		JSONObject variableParameters = new JSONObject();
		try {
			FileReader fr = new FileReader (path);
			BufferedReader br = new BufferedReader (fr);
			String s;
			while ((s = br.readLine()) != null) {
				String[] p = s.split(" ");
				variableParameters.put(p[0], Double.parseDouble(p[1]));
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return variableParameters;
	}
	
	/**
	 * Gives the JSON with parameters for each hours from the static, variable and factors of each hour
	 * The parameters are the same for each zone of the map.
	 * @param staticParameters the JSON with the static parameters
	 * @param variableParameters the JSON with the variable parameters
	 * @param hourFactors the path toward the file where are the factors
	 * @return a JSON with all the hour parameters
	 */
	public static JSONObject parametersHourJSON (JSONObject staticParameters, JSONObject variableParameters, String hourFactors) {
		JSONObject hourParameters = new JSONObject();
		try {
			FileReader fr = new FileReader (hourFactors);
			BufferedReader br = new BufferedReader(fr);
			hourParameters.put("zone", false);
			hourParameters.put("staticParameters", staticParameters);
			JSONObject newVariableParam = new JSONObject();
			for (int i = 0; i < 24; i++) {
				String[] factors = br.readLine().split(";");
				JSONObject vph = new JSONObject();
				vph.put("probaCreatePerson", variableParameters.getDouble("probaCreatePerson")*Double.parseDouble(factors[0]));
				vph.put("probaCreateCar", variableParameters.getDouble("probaCreateCar")*Double.parseDouble(factors[1]));
				vph.put("probaCreateTram", variableParameters.getDouble("probaCreateTram")*Double.parseDouble(factors[2]));
				vph.put("probaCreateTrain", variableParameters.getDouble("probaCreateTrain")*Double.parseDouble(factors[3]));
				vph.put("probaLeaveHome", variableParameters.getDouble("probaLeaveHome")*Double.parseDouble(factors[4]));
				vph.put("probaTakeTransport", variableParameters.getDouble("probaTakeTransport")*Double.parseDouble(factors[5]));
				vph.put("probaGoToSchool", variableParameters.getDouble("probaGoToSchool")*Double.parseDouble(factors[6]));
				vph.put("probaGoToShop", variableParameters.getDouble("probaGoToShop")*Double.parseDouble(factors[7]));
				vph.put("probaGoToRestaurant", variableParameters.getDouble("probaGoToRestaurant")*Double.parseDouble(factors[8]));
				vph.put("probaGoToDoctor", variableParameters.getDouble("probaGoToDoctor")*Double.parseDouble(factors[9]));
				vph.put("probaGoToBank", variableParameters.getDouble("probaGoToBank")*Double.parseDouble(factors[10]));
				vph.put("probaLeaveTownByTrain", variableParameters.getDouble("probaLeaveTownByTrain")*Double.parseDouble(factors[11]));
				vph.put("probaLeaveTownByTram", variableParameters.getDouble("probaLeaveTownByTram")*Double.parseDouble(factors[12]));
				vph.put("probaLeaveTownByRoad", variableParameters.getDouble("probaLeaveTownByRoad")*Double.parseDouble(factors[13]));
				newVariableParam.put(Integer.toString(i), vph);
			}
			hourParameters.put("variableParameters",newVariableParam);
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hourParameters;
	}
	
	/**
	 * Gives the JSON of the parameters following the hour and the zone of the map
	 * @param staticParameters the JSON with the static parameters
	 * @param variableParameters the JSON with the variable parameters
	 * @param hourFactors the hour factors
	 * @param zoneFactors the zone factors
	 * @return the JSON with all the parameters
	 */
	public static JSONObject parametersByZoneJSON (JSONObject staticParameters, JSONObject variableParameters, 
			String hourFactors, String zoneFactors) {
		JSONObject zoneParameters = new JSONObject();
		try {
			FileReader fr2 = new FileReader (zoneFactors);
			BufferedReader br2 = new BufferedReader (fr2);
			zoneParameters.put("staticParameters", staticParameters);
			zoneParameters.put("zone", true);
			int x = Integer.parseInt(br2.readLine());
			int y = Integer.parseInt(br2.readLine());
			JSONObject variable = new JSONObject();
			for (int i =0; i < x; i++) {
				for (int j=0; j < y; j++) {
					JSONObject zoneVP = new JSONObject();
					zoneVP.put("x", i);
					zoneVP.put("y", j);
					String s = br2.readLine();
					String[] p = s.split(";");
					FileReader fr1 = new FileReader (hourFactors);
					BufferedReader br1 = new BufferedReader (fr1);
					for (int k = 0; k < 24; k++) {
						JSONObject hp = new JSONObject();
						String s2 = br1.readLine();
						String[] p2 = s2.split(";");
						hp.put("probaCreatePerson", variableParameters.getDouble("probaCreatePerson")*Double.parseDouble(p[0])
								*Double.parseDouble(p2[0]));
						hp.put("probaCreateCar", variableParameters.getDouble("probaCreateCar")*Double.parseDouble(p[1])
								*Double.parseDouble(p2[1]));
						hp.put("probaCreateTram", variableParameters.getDouble("probaCreateTram")*Double.parseDouble(p[2])
								*Double.parseDouble(p2[2]));
						hp.put("probaCreateTrain", variableParameters.getDouble("probaCreateTrain")*Double.parseDouble(p[3])
								*Double.parseDouble(p2[3]));
						hp.put("probaLeaveHome", variableParameters.getDouble("probaLeaveHome")*Double.parseDouble(p[4])
								*Double.parseDouble(p2[4]));
						hp.put("probaTakeTransport", variableParameters.getDouble("probaTakeTransport")*Double.parseDouble(p[5])
								*Double.parseDouble(p2[5]));
						hp.put("probaGoToSchool", variableParameters.getDouble("probaGoToSchool")*Double.parseDouble(p[6])
								*Double.parseDouble(p2[6]));
						hp.put("probaGoToShop", variableParameters.getDouble("probaGoToShop")*Double.parseDouble(p[7])
								*Double.parseDouble(p2[7]));
						hp.put("probaGoToRestaurant", variableParameters.getDouble("probaGoToRestaurant")*Double.parseDouble(p[8])
								*Double.parseDouble(p2[8]));
						hp.put("probaGoToDoctor", variableParameters.getDouble("probaGoToDoctor")*Double.parseDouble(p[9])
								*Double.parseDouble(p2[9]));
						hp.put("probaGoToBank", variableParameters.getDouble("probaGoToBank")*Double.parseDouble(p[10])
								*Double.parseDouble(p2[10]));
						hp.put("probaLeaveTownByTrain", variableParameters.getDouble("probaLeaveTownByTrain")*Double.parseDouble(p[11])
								*Double.parseDouble(p2[11]));
						hp.put("probaLeaveTownByTram", variableParameters.getDouble("probaLeaveTownByTram")*Double.parseDouble(p[12])
								*Double.parseDouble(p2[12]));
						hp.put("probaLeaveTownByRoad", variableParameters.getDouble("probaLeaveTownByRoad")*Double.parseDouble(p[13])
								*Double.parseDouble(p2[13]));
						zoneVP.put(String.valueOf(k), hp);
					}
					br1.close();
					fr1.close();
					variable.put(String.valueOf(i+j*x), zoneVP);
				}
			}
			zoneParameters.put("variableParameters", variable);
			br2.close();
			fr2.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return zoneParameters;
	}
	
	/**
	 * Allows to edit start parameters.
	 * Allows to changes the number of each agent and the type of reaction.
	 * The parameters must be "name" + "space" + "value" for each line.
	 * @param path the path where is the 
	 * @return
	 */
	public static TransportSimulationParameters startParameters (String path) {
		TransportSimulationParameters tsp = new TransportSimulationParameters();
		try {
			FileReader fr = new FileReader(path);
			BufferedReader br = new BufferedReader (fr);
			String s;
			while ((s = br.readLine()) != null) {
				String[] p = s.split(" ");
				if (p[0].equals("nbrPersons")) {
					tsp.nbrPersons = Integer.parseInt(p[1]);
				} else if (p[0].equals("nbrCars")) {
					tsp.nbrCars = Integer.parseInt(p[1]);
				} else if (p[0].equals("nbrTramways")) {
					tsp.nbrTramways = Integer.parseInt(p[1]);
				} else if (p[0].equals("carReactionOnly")) {
					tsp.carReactionOnly = Boolean.parseBoolean(p[1]);
				}
			}
			br.close();
			fr.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tsp;
	}

}
