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
package fr.lgi2a.similar2logo.lib.tools.html.view;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.Field;

import org.eclipse.jetty.util.log.Log;

import fr.lgi2a.similar.extendedkernel.simulationmodel.ISimulationParameters;
import fr.lgi2a.similar2logo.kernel.model.Parameter;
import fr.lgi2a.similar2logo.lib.tools.html.IHtmlInitializationData;
import fr.lgi2a.similar2logo.lib.tools.html.ResourceNotFoundException;
import spark.utils.IOUtils;

/**
 * The helper class generating the HTML interface used by the Similar2LogoHtmlRunner.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 */
public class Similar2LogoHtmlGenerator {
	
	/**
	 * The name of the files where the js and css libraries are located.
	 */
	protected static final String[] deployedResources = {
		"js/bootstrap.min.js",
		"css/bootstrap.min.css",
		"js/dygraph.min.js",
		"css/dygraph.css",
		"js/jquery-3.3.1.min.js",
		"js/similar2logo-gui.js",
		"css/similar2logo-gui.css"
	};	

	/**
	 * The object providing initialization data to this view.
	 */
	private IHtmlInitializationData initializationData;
	/**
	 * The body of the web GUI.
	 */
	private String htmlBody;
	
	/**
	 * Builds a HTML code generator where the body of the web GUI is manually defined.
	 * @param htmlBody The body of the web GUI.
	 * @param initializationData The object providing initialization data to this view.
	 */
	public Similar2LogoHtmlGenerator(
		String htmlBody,
		IHtmlInitializationData initializationData
	) {
		this.htmlBody = htmlBody;
		this.initializationData = initializationData;
	}

	/**
	 * Builds a HTML code generator where the body of the web GUI is empty.
	 * @param initializationData The object providing initialization data to this view.
	 */
	public Similar2LogoHtmlGenerator(
		IHtmlInitializationData initializationData
	) {
		this( "", initializationData );
	}
	
	/**
	 * Builds a HTML code generator where the body of the web GUI is obtained through an input stream.
	 * @param htmlBody The body of the web GUI.
	 * @param initializationData The object providing initialization data to this view.
	 */
	public Similar2LogoHtmlGenerator(
		InputStream htmlBody,
		IHtmlInitializationData initializationData
	) {
		this( Similar2LogoHtmlGenerator.getViewResource(htmlBody), initializationData );
	}
	
	/**
	 * Gets the URL of a static resource of the HTML view.
	 * @return the URL of a resource of the HTML view.
	 */
	public static String getViewResource(InputStream inputStream) {
		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(inputStream, writer);
		} catch (IOException e) {
			throw new ResourceNotFoundException(e);
			
		}
		return writer.toString();
	}
	
	/**
	 * Gets the HTML code of the canvas containing the grid view over the simulation.
	 * @return the canvas containing the grid view.
	 */
	public static String getGridView() {
		return Similar2LogoHtmlGenerator.getViewResource(
			Similar2LogoHtmlGenerator.class.getResourceAsStream("gridview.html")
		);
	}
	
	/**
	 * Generates the HTML code of the header of the GUI.
	 * @return the header of the web GUI.
	 */
	public String renderHtmlHeader( ) {
		
		StringBuilder output =  new StringBuilder();
		
		output	.append(Similar2LogoHtmlGenerator.getViewResource(
			Similar2LogoHtmlGenerator.class.getResourceAsStream("guiheader.html")
			)
		)
				.append("<h2 id='simulation-title'>"+ this.initializationData.getConfig().getSimulationName()+"</h2>")
				.append("<div class='row'>")
				.append("<div class='col-md-2 col-lg-2'>");
		renderParameters( this.initializationData.getSimulationParameters(), output );
		output	.append( "</div>")
				.append( "<div class='col-md-10 col-lg-10'>");
			
		return  output.toString();	
	}

	/**
	 * Generates the HTML code of the interface that allows users to modify the parameters of the simulation.
	 * @param parameters The parameters of the simulation.
	 * @param output the html interface that allows users to modify the parameters.
	 */
	public static void renderParameters(
			ISimulationParameters parameters,
			StringBuilder output
	) {
		output.append("<form data-toggle='validator'>");
		for(Field parameter : parameters.getClass().getFields()) {
			renderParameter(parameters, parameter,output);
		}
		output.append("</form>");
	}
	
	/**
	 * Generates the HTML code of the interface that allows users to modify a parameter of the simulation.
	 * @param parameters The parameters of the simulation.
	 * @param parameter The rendered form entry for the parameter.
	 * @param output the html interface that allows users to modify the parameters.
	 */
	private static void renderParameter(
		ISimulationParameters parameters, 
		Field parameter,
		StringBuilder output
	) {
		if(parameter.getAnnotation(Parameter.class) != null && parameter.getType().isPrimitive() ) {
			try {
				if(parameter.getType().equals(boolean.class)) {
					output	.append( "<div class='form-check form-check-sm'>")
						  	.append(	"<label class='form-check-label'>")
							.append(		"<input 	class='form-check-input' ")
							.append(					"type='checkbox' id='" + parameter.getName()+"' ")
							.append(					"data-toggle='popover' ")
							.append(					"data-trigger='hover' ")
							.append(					"data-placement='right' ")
							.append(						"data-content='"+parameter.getAnnotation(Parameter.class).description()+"' ");
					if(parameters.getClass().getField(parameter.getName()).get(parameters).equals(true)) {
						output	.append(				"checked ");
					}
					output	.append(					"onclick=\"updateBooleanParameter(\'"+parameter.getName()+"\')\" />")
							.append(		"<strong>")
							.append(			parameter.getAnnotation(Parameter.class).name())
							.append(		"</strong>")
							.append( 	"</label>")
							.append( "</div>");
				} else {
					output 	.append( "<div class='form-group form-group-sm row'>")
							.append(	"<label class='col-12 col-form-label' ")
							.append( 			"for='"+parameter.getName() + "' >")
							.append(		parameter.getAnnotation(Parameter.class).name())
							.append(	"</label>")
							.append( 	"<div class='col-12'>")
							.append(		"<input 	type='number' ")
							.append( 					"data-toggle='popover' ")
							.append( 					"data-trigger='hover' ")
							.append( 					"data-placement='right' ")
							.append(					"data-content='"+parameter.getAnnotation(Parameter.class).description()+"' ");
					if(parameter.getType().equals(int.class)) {
						output	.append( 				"step='1' "); 
					} else{
						output	.append(				"step='0.01' ");  
					}
					output	.append( 					"size='5' ")
							.append( 					"class='form-control bfh-number text-right' ")
							.append( 					"id='"+parameter.getName()+"' ")
							.append(					"value='" + parameters.getClass().getField(parameter.getName()).get(parameters)+"' ")
							.append(					"onchange=\"updateNumericParameter(\'"+parameter.getName()+"\')\" >")
							.append(	"</div>")
							.append( "</div>");
				}
			} catch (
					 IllegalArgumentException
				   | IllegalAccessException
				   | NoSuchFieldException
				   | SecurityException e
			) {
				Log.getRootLogger().warn(
	        			"The parameter "+ parameter + " cannot be found", e
	        		);
			}
		}
	}

	/**
	 * Generates the HTML code of the body of the GUI.
	 * @return the body of the web GUI.
	 */
	public String renderHtmlBody() {
		return this.htmlBody;
	}
	
	/**
	 * Generates the HTML code of the footer of the GUI.
	 * @return the footer of the web GUI.
	 */
	public String renderHtmlFooter() {
		return Similar2LogoHtmlGenerator.getViewResource(
				Similar2LogoHtmlGenerator.class.getResourceAsStream("guifooter.html")
		);
	}
}
