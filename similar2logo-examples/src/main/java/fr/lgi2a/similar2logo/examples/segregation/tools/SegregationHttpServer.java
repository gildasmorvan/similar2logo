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
package fr.lgi2a.similar2logo.examples.segregation.tools;

import java.io.File;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar2logo.examples.segregation.probes.SegregationAgentDrawer;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.lib.probes.GridPngView;
import fr.lgi2a.similar2logo.lib.tools.http.SimilarHttpServer;

/**
 * A http server that allow to control and visualize segregation simulations.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class SegregationHttpServer extends SimilarHttpServer {

	/**
	 * 
	 * Builds an instance of this Http server.
	 * 
	 * @param engine The simulation engine used to simulate the model.
	 * @param model The Simulation model.
	 */
	public SegregationHttpServer(ISimulationEngine engine, LogoSimulationModel model) {
		super(engine, model, true, false);
		
		engine.addProbe(
				"Png export",
				new GridPngView(
					null,
					new SegregationAgentDrawer(),
					null,
					null,
					null,
					new File("results/grid.png"),
					new File("results/grid_tmp.png")
				)
			);
		
		this.getSimilarHttpHandler()
				.setHtmlBody(
						"<h2>Segregation simulation</h2><style type='text/css'> #grid_img{display: block; margin: auto;} h2{text-align:center;}   </style> <div><img id='grid_img' src='grid.png' alt='' height='400px' width='400px' onerror='displaylastImage()'></div> <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'> <script src='http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js'></script> <script src='http://cdnjs.cloudflare.com/ajax/libs/dygraph/1.1.1/dygraph-combined.js'></script> <script type='text/javascript'>$(document).ready(function () { setInterval(function() {$('#grid_img').attr('src', 'grid.png');}, 10);});</script> <script type='text/javascript'> function displaylastImage() {$('#grid_img').attr('src', 'grid_tmp.png');}</script>");
	}
}
