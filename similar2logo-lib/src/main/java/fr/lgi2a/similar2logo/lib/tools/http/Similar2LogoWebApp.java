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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;

import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.kernel.model.Parameter;
import spark.utils.IOUtils;


/**
 * The HTML interface.
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 *
 */
public class Similar2LogoWebApp {
	
	/**
	 * The name of the files where the js and css libraries are located.
	 */
	public static final String[] deployedResources = {
		"js/bootstrap.min.js",
		"css/bootstrap.min.css",
		"js/dygraph-combined.js",
		"js/jquery-3.1.1.min.js",
		"js/similar2logo-gui.js",
		"css/similar2logo-gui.css"
	};
	
	/**
	 * The body of the web GUI.
	 */
	private String htmlBody;

	/**
	 * @param htmlBody The body of the web GUI.
	 */
	public Similar2LogoWebApp(String htmlBody) {
		this.htmlBody = htmlBody;
	}
	
	public Similar2LogoWebApp() {
		this.htmlBody = "";
	}
	
	/**
	 * @param htmlBody The URL of the body of the web GUI.
	 */
	public Similar2LogoWebApp(InputStream htmlBody) {
		this.htmlBody = getAppResource(htmlBody);
	}

	/**
	 * @param parameters The parameters of the simulation.
	 * @return the html interface that allows users to modify the parameters.
	 */
	public static String displayParameters(ISimulationParameters parameters) {	
		String output="<form data-toggle='validator'>";
		for(Field parameter : parameters.getClass().getFields()) {
			output+=displayParameter(parameters, parameter);
		}
		output+="</form>";
		return output;
	}
	
	/**
	 * @param parameters The parameters of the simulation.
	 * @param parameter The displayed parameter.
	 * @return the html gui of the parameter.
	 */
	private static String displayParameter(ISimulationParameters parameters, Field parameter) {
		String output="";
		if(
				!parameter.getName().equals("initialTime")
				&&!parameter.getName().equals("finalTime")
				&&!parameter.getName().equals("pheromones")
				&&parameter.getType().isPrimitive()
		) {
			try {
				if(parameter.getType().equals(boolean.class)) {
					output += "<div class='form-check form-check-sm'>"
							+	"<label class='form-check-label'>"
							+		"<input 	class='form-check-input' "
							+					"type='checkbox' id='" + parameter.getName()+"' "
							+					"data-toggle='popover' "
							+					"data-trigger='hover' "
							+					"data-placement='right' "
						+						"data-content='"+parameter.getAnnotation(Parameter.class).description()+"' ";
					if(parameters.getClass().getField(parameter.getName()).get(parameters).equals(true)) {
						output+=				"checked ";
					}
					output+=					"onclick=\"updateBooleanParameter(\'"+parameter.getName()+"\')\" />"
							+		"<strong>"
							+			parameter.getAnnotation(Parameter.class).name()
							+		"</strong>"
							+ 	"</label>"
							+ "</div>";
				} else {
					output += "<div class='form-group form-group-sm row'>"
							+	"<label class='col-12 col-form-label' "
							+ 			"for='"+parameter.getName() + "' >"
							+		parameter.getAnnotation(Parameter.class).name()
							+	"</label>"
							+ 	"<div class='col-12'>"
							+		"<input 	type='number' "
							+ 					"data-toggle='popover' "
							+ 					"data-trigger='hover' "
							+ 					"data-placement='right' "
							+					"data-content='"+parameter.getAnnotation(Parameter.class).description()+"' ";
					if(parameter.getType().equals(int.class)) {
						output+= 				"step='1' "; 
					} else{
						output+= 				"step='0.01' ";  
					}
					output+= 					"size='5' "
							+ 					"class='form-control bfh-number text-right' "
							+ 					"id='"+parameter.getName()+"' "
							+					"value='" + parameters.getClass().getField(parameter.getName()).get(parameters)+"' "
							+					"onchange=\"updateNumericParameter(\'"+parameter.getName()+"\')\" >"
							+	"</div>";
					output += "</div>";
				}
			} catch (Exception e) {}
		}
		return output;
		
	}
	
	/**
	 * @return the url of a resource of the web app.
	 */
	public static String getAppResource(InputStream inputStream) {
			StringWriter writer = new StringWriter();
			try {
				IOUtils.copy(inputStream, writer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return writer.toString();
	}
	
	/**
	 * @return the canvas containing the grid view.
	 */
	public static String getGridView() {
		return getAppResource(
				Similar2LogoWebApp.class.getResourceAsStream("gridview.html")
		);
	}
	
	/**
	 * @param model 
	 * @return the header of the web GUI.
	 */
	public static String getHtmlHeader(LogoSimulationModel model) {
		return getAppResource(
				Similar2LogoWebApp.class.getResourceAsStream("guiheader.html")
			)
			+"<h2 id='simulation-title'>"+model.getClass().getSimpleName().split("SimulationModel")[0]+"</h2>"
			+ "<div class='row'>"
			+ "<div class='col-md-4'>"
			+ Similar2LogoWebApp.displayParameters(model.getSimulationParameters())
			+ "</div>"
			+ "<div class='col-md-8'>";
	}
	
	/**
	 * @return the footer of the web GUI.
	 */
	public static String getHtmlFooter() {
		return getAppResource(
				Similar2LogoWebApp.class.getResourceAsStream("guifooter.html")
		);
	}

	/**
	 * @return the body of the web GUI.
	 */
	public String getHtmlBody() {
		return htmlBody;
	}

	/**
	 * @param htmlBody The body of the web GUI.
	 */
	public void setHtmlBody(String htmlBody) {
		this.htmlBody = htmlBody;
	}


}
