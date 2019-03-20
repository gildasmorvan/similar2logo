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
 * implementation of multi-agent-based simulations using the formerly named
 * IRM4MLS meta-model. This software defines an API to implement such 
 * simulations, and also provides usage examples.
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
package fr.univ_artois.lgi2a.similar2logo.examples.fire;

import static spark.Spark.webSocket;

import java.io.IOException;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.ResourceNotFoundException;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.model.FireForestParameters;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.probes.FireProbe;
import fr.univ_artois.lgi2a.similar2logo.examples.fire.probes.JSONFireProbe;
import fr.univ_artois.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel;
import fr.univ_artois.lgi2a.similar2logo.lib.probes.LogoRealTimeMatcher;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.Similar2LogoWebRunner;
import fr.univ_artois.lgi2a.similar2logo.lib.tools.web.view.GridWebSocket;

/**
 * The main class of the "Fire" simulation.
 * 
 * @author <a xavier_szkudlarek@univ-artois.fr target="_blank">Szkudlarek
 *         Xavier</a>
 * 
 *
 */
public final class FireForestMain {

	/**
	 * Private Constructor to prevent class instantiation.
	 */
	private FireForestMain() {
	}

	/**
	 * The main method of the simulation.
	 * 
	 * @param args
	 *            The command line arguments.
	 */
	public static void main(String[] args) {

		webSocket("/grid", GridWebSocket.class);

		Similar2LogoWebRunner runner = new Similar2LogoWebRunner();
		runner.getConfig().setExportAgents(false);
		runner.getConfig().setExportMarks(false);
		runner.getConfig().setExportPheromones(false);

		try {
			runner.getConfig().setCustomHtmlBody(
					FireForestMain.class.getResourceAsStream("firegui.html"));
		} catch (IOException e) {
			throw new ResourceNotFoundException(e);
		}

		AbstractLogoSimulationModel model = new FireForestSimulationModel(
				new FireForestParameters());
		runner.initializeRunner(model);
		runner.addProbe("display", new JSONFireProbe());
		runner.addProbe("Real time matcher", new LogoRealTimeMatcher(5));
		runner.addProbe("Fire statistics", new FireProbe());
		runner.showView();
	}
}
