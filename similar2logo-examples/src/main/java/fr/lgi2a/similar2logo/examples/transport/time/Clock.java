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
package fr.lgi2a.similar2logo.examples.transport.time;

import fr.lgi2a.similar.microkernel.SimulationTimeStamp;

/**
 * Clock of the transport simulation.
 * It enables to get the time in the simulation
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
public class Clock {
	
	/**
	 * The day when the simulations started.
	 * 0 for Monday to 6 for Sunday
	 */
	private int startDay;
	
	/**
	 * The hour when the simulation started.
	 * We use a 24 hours clock
	 */
	private int startHour;
	
	/**
	 * The number of step do in one second
	 */
	private int stepBySecond;
	
	/**
	 * Constructor of the clock
	 * @param startDay the start day of the simulation
	 * @param startHour the start hour of the simulation
	 * @param step the number of step in on second
	 */
	public Clock (int startDay, int startHour, int step) {
		this.startDay = startDay;
		this.startHour = startHour;
		this.stepBySecond = step;
	}
	
	/**
	 * Gives the complete time in the form of a string
	 * @param sts the current simulation time stamp
	 * @return the current time as a string
	 */
	public String getTime (SimulationTimeStamp sts) {
		String res = "";
		return res+dayToString(getDay(sts))+", "+getHour(sts)+" h, "+getMinute(sts)+" min, "+getSecond(sts)+" s";
	}
	
	/**
	 * Gives the day
	 * @param sts the current simulation time stamp
	 * @return the current day
	 */
	public int getDay (SimulationTimeStamp sts) {
		double time = sts.getIdentifier() + startDay*86400*stepBySecond + startHour*stepBySecond*3600;
		return (int) (time/(86400*stepBySecond))%7;
	}
	
	/**
	 * Gives the hour
	 * @param sts the current simulation time stamp
	 * @return the current hour
	 */
	public int getHour (SimulationTimeStamp sts) {
		double time = sts.getIdentifier() + startHour*(stepBySecond*3600);
		return (int) ((time%(86400*stepBySecond))/(stepBySecond*3600))%24;
	}
	
	/**
	 * Gives the minute
	 * @param sts the current simulation time stamp
	 * @return the current minute
	 */
	public int getMinute (SimulationTimeStamp sts) {
		double time = sts.getIdentifier();
		return (int) (time%(86400*stepBySecond)%(stepBySecond*3600))/(stepBySecond*60)%60;
	}
	
	/**
	 * Gives the second
	 * @param sts the current simulation time stamp
	 * @return the current second (each second lasts 4 steps)
	 */
	public int getSecond (SimulationTimeStamp sts) {
		double time = sts.getIdentifier();
		return (int) (time%(86400*stepBySecond)%(stepBySecond*3600)%(stepBySecond*60))/stepBySecond%60;
	}
	
	/**
	 * Gives the day in string following its number
	 * @param day the number of the day
	 * @return the string of the day
	 */
	private String dayToString (int day) {
		switch (day) {
			case 0: return "Monday";
			case 1 : return "Tuesday";
			case 2 : return "Wednesday";
			case 3 : return "Thrusday";
			case 4 : return "Friday";
			case 5 : return "Saturday";
			default : return "Sunday";
		}
	}
}
