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
package fr.lgi2a.similar2logo.examples.circle.tools;

import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.lib.tools.http.Similar2LogoWebApp;
import fr.lgi2a.similar2logo.lib.tools.http.SimilarHttpServer;

/**
 * A http server that allow to control and visualize segregation simulations.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public class CircleHttpServer extends SimilarHttpServer {

	/**
	 * 
	 * Builds an instance of this Http server.
	 * 
	 * @param model The Simulation model.
	 */
	public CircleHttpServer(LogoSimulationModel model) {
		super(
			model,
			new Similar2LogoWebApp(getBody(model)),
			true,
			false,
			false
		);
		
		this.getSimilarHttpHandler().getEngine().addProbe(
				"Population printing",
				new CirclePopulationProbe()
			);
		
	}
	
	private static String getBody(LogoSimulationModel model) {
		return "<h2>Circle simulation</h2>"
		+ "<style type='text/css'>"
		+ "h2,h4{text-align:center;}"
		+ "#chart_div {width:100%;height:auto;}"
		+ "canvas{width:auto;height:100%;margin:auto;}"
		+ "</style>"
		+ "<div class='row'>"
		+ "<div class='col-md-4'>"
		+ Similar2LogoWebApp.displayParameters(model.getSimulationParameters())
		+ "<div class='checkbox col-sm-3 col-md-4 col-lg-4'><label><input type='checkbox'  id='clear'"
		+ " data-toggle='popover' data-trigger='hover' data-placement='right' "
		+"  data-content='Check to clear grid at each step.' checked><strong>clear</strong></label></div>"
		+ "</div>"
		+ "<div class='col-md-8'>"
		+ "<canvas id='grid_canvas' onclick='clearCanvas();' ondblclick='fullScreen();' class='center-block' width='400' height='400'></canvas>"
		+ "</div>"
		+ "</div>"
		+ "<script type='text/javascript'>"
		+ " function clearCanvas() {"
		+ "  var canvas = document.getElementById('grid_canvas');"
		+ "  var context = canvas.getContext('2d');"
		+ "  context.clearRect(0, 0, canvas.width, canvas.height);"
		+ " }"
		+ "</script>"
		+ "<script type='text/javascript'>"
		+ " function fullScreen() {"
		+ "  document.getElementById('grid_canvas').webkitRequestFullScreen();"
		+ "  document.getElementById('grid_canvas').height = screen.availHeight;"
		+ "  document.getElementById('grid_canvas').width = screen.availHeight;"
		+ " }"
		+ "</script>"
		+ "<script type='text/javascript'>"
		+ "$(document).ready(function () {"
		+ "function drawCanvas(){"
		+ " $.ajax({url: 'grid',dataType: 'text',success: function(data) {"
		+ "\n"
		+ " var json = JSON.parse(data);"
		+ " var canvas = document.getElementById('grid_canvas');"
		+ " var context = canvas.getContext('2d');"
		+ " if($('#clear').prop('checked')) {"
		+ "  context.clearRect(0, 0, canvas.width, canvas.height);"
		+ " }"
		+ " for (var i = 0; i < json.agents.length; i++) {"
		+ "  var centerX = json.agents[i].x*canvas.width;"
		+ "  var centerY = json.agents[i].y*canvas.height;"
		+ "  var radius = 1;"			
		+ "  if(json.agents[i].t=='l'){context.fillStyle = 'red';}"
		+ "  else {context.fillStyle = 'blue';}"
		+ "  context.beginPath();"
		+ "  context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);"
		+ "  context.fill();"
		+ "\n"
		+" }"
		+ "}});"
		+ "\n"
		+ "}"
		+ "setInterval(function() {drawCanvas();}, 20);});"
		+ "</script>";
	}
}
