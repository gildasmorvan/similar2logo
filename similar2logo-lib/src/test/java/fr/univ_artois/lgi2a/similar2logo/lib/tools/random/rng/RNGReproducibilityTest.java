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
package fr.univ_artois.lgi2a.similar2logo.lib.tools.random.rng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

import fr.univ_artois.lgi2a.similar2logo.lib.tools.random.rng.LightRNG;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.random.rng.XoRoRNG;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.random.rng.XorRNG;
import junit.framework.TestCase;

public class RNGReproducibilityTest extends TestCase {
	
	private Map<Rng,List<Random>> randomMap;
	
	private enum Rng {
		Light,
		Xoro,
		Xor,
//		MT,
		LG;
		
	}
	
	private static final int N_RAND=100;
	
	private static final int N_REP=100;
	
	private static final long SEED=21;

	@Before
	public void setUp() throws Exception {
		randomMap = new HashMap<>();
		for(Rng r : Rng.values()) {
			List<Random> random = new ArrayList<>();
			switch(r) {
			case Light:
				for(int i = 0; i < N_RAND; i++) {
					Random lr = new LightRNG();
					lr.setSeed(SEED);
					random.add(lr);
				}
				break;
			case Xoro:
				for(int i = 0; i < N_RAND; i++) {
					Random lr = new XoRoRNG();
					lr.setSeed(SEED);
					random.add(lr);
				}
				break;
			case Xor:
				for(int i = 0; i < N_RAND; i++) {
					Random lr = new XorRNG();
					lr.setSeed(SEED);
					random.add(lr);
				}
				break;
//			case MT:
//				for(int i = 0; i < N_RAND; i++) {
//					Random lr = new SynchronizedMersenneTwister();
//					lr.setSeed(SEED);
//					random.add(lr);
//				}
//				break;
			case LG:
				for(int i = 0; i < N_RAND; i++) {
					Random lr = new Random();
					lr.setSeed(SEED);
					random.add(lr);
				}
				break;
			}
			randomMap.put(r, random );
		}
		
	}

	@Test
	public void testReproducibility() {
		for(Entry<Rng, List<Random>> randomEntry : randomMap.entrySet()) {
			for(int i = 0; i < N_REP; i++) {
				long lastValue=0;
				boolean init = false;
				for(Random random : randomEntry.getValue()) {
					long value = random.nextLong();
					if(init) {
						assertEquals(value, lastValue);
					} else {
						init=true;
					}
					lastValue = value;
				}
			}
		}
	}

}
