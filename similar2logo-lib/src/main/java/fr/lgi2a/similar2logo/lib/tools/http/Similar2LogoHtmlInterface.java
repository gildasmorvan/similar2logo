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
package fr.lgi2a.similar2logo.lib.tools.http;

import java.lang.reflect.Field;

import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;


/**
 * The HTML interface.
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 *
 */
public class Similar2LogoHtmlInterface {

	public static String defaultParametersInterface(ISimulationParameters parameters) {
		String output="<form class='form-inline' role='form' data-toggle='validator'>"
				+ "<table>";
		for(Field parameter : parameters.getClass().getFields()) {
			output+="<tr><td>";
			output+=displayParameter(parameters, parameter);
			output+="</td></tr>";
		}
		output+="</table></form>"
				+ "<script type='text/javascript'>"
				+ "function updateNumericParameter(parameter){"
				+ "var output='setParameter?'+parameter+'='+$('#'+parameter).val();"
				+ " $.get(output);\n"
				+ "}"
				+ "</script>"
				+ "<script type='text/javascript'>"
				+ "function updateBooleanParameter(parameter){\n"
				+ "var output='setParameter?'+parameter+'='+$('#'+parameter).prop('checked');"
				+ " $.get(output);"
				+ "}"
				+ "</script>"
				+"<script>"
				+ "$(document).ready(function(){"
				+ " $('[data-toggle=\"popover\"]').popover(); "
				+ "});"
				+ "</script>";
		return output;
	}
	
	public static String displayParameter(ISimulationParameters parameters, Field parameter) {
		String output="";
		if(
				!parameter.getName().equals("initialTime")
				&&!parameter.getName().equals("finalTime")
				&&!parameter.getName().equals("pheromones")
				&&parameter.getType().isPrimitive()
		) {
			output+="<div class='form-group'>"
					  +"<label  "
					  +"for='"
					  +parameter.getName()
					  +"'>"
					  +parameter.getAnnotation(Parameter.class).name()+"</label></td><td>";
			try {
				if(parameter.getType().equals(boolean.class)) {
					
					output+= "<input type='checkbox'  class='checkbox-inline'  id='"
				      +parameter.getName()
				      +"' data-toggle='popover' data-trigger='hover' data-placement='right' "
				      +"data-content='"+parameter.getAnnotation(Parameter.class).description()+"' " ;
					if(parameters.getClass().getField(parameter.getName()).get(parameters).equals(true)) {
						output+="checked";
					}
					output+=" onclick=\"updateBooleanParameter(\'"+parameter.getName()+"\')\">";
				} else {
				  output+="<input type='number' data-toggle='popover' data-trigger='hover' data-placement='right' "
				   +"data-content='"+parameter.getAnnotation(Parameter.class).description()+"' " ;
				   if(parameter.getType().equals(int.class)) {
					   output+= "step='1' "; 
				   } else{
					   output+= "step='0.01' ";  
				   }
				   output+= "maxlength='5' class='form-control  bfh-number text-right' id='"
				   +parameter.getName()
				   +"' value='"
				   +parameters.getClass().getField(parameter.getName()).get(parameters)
				   +"' onchange=\"updateNumericParameter(\'"+parameter.getName()+"\')\">";
				}
				output+="</div><br>";
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return output;
		
	}
	
	public static String defaultGridView(int frameRate) {
		return 	  "<style type='text/css'>"
				+ " canvas{height: 100%; width: auto;}"
				+ "</style>"
				+ "<canvas id='grid_canvas' class='center-block' width='500' height='500'></canvas>"
				+ "<script type='text/javascript'>"
				+ "$(document).ready(function () {"
				+ "function drawCanvas(){"
				+ " $.ajax({url: 'grid',dataType: 'text',success: function(data) {"
				+ " var json = JSON.parse(data);"
				+ "  var canvas = document.getElementById('grid_canvas');"
				+ "  var context = canvas.getContext('2d');"
				+ "  context.clearRect(0, 0, canvas.width, canvas.height);"
				+ " for (var i = 0; i < json.agents.length; i++) {"
				+ "  var centerX = json.agents[i].x*canvas.width;"
				+ "  var centerY = json.agents[i].y*canvas.height;"
				+ "  var radius = 1;"			
				+ "  context.fillStyle = 'blue';"
				+ "  context.beginPath();"
				+ "  context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);"
				+ "  context.fill();"
				+ " }"
				+ " for (var i = 0; i < json.marks.length; i++) {"
				+ "  var centerX = json.marks[i].x*canvas.width;"
				+ "  var centerY = json.marks[i].y*canvas.height;"
				+ "  var radius = 1;"			
				+ "  context.fillStyle = 'red';"
				+ "  context.beginPath();"
				+ "  context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);"
				+ "  context.fill();"
				+ " }"
				+ "}});"
				+ "}"
				+ "setInterval(function() {drawCanvas();}, "+frameRate+");});"
				+ "</script>";
		
	}

}
