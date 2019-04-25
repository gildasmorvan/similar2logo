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
package fr.univ_artois.lgi2a.similar2logo.lib.tools.web.view;

import java.io.IOException;
import java.io.InputStream;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.IHtmlInitializationData;
import fr.univ_artois.lgi2a.similar.extendedkernel.libs.web.view.SimilarHtmlGenerator;
import spark.utils.IOUtils;

/**
 * The helper class generating the HTML interface used by the Similar2LogoWebRunner.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com">Antoine Lecoutre</a>
 */
public class Similar2LogoHtmlGenerator extends SimilarHtmlGenerator {
	
	
	static {
		addTextResource("js/dygraph.min.js", Similar2LogoHtmlGenerator.class);
		addTextResource("css/dygraph.css", Similar2LogoHtmlGenerator.class);
		addTextResource("js/similar2logo-gui.js", Similar2LogoHtmlGenerator.class);
	}
	
	/**
	 * Builds a HTML code generator where the body of the web GUI is manually defined.
	 * @param htmlBody The body of the web GUI.
	 * @param initializationData The object providing initialization data to this view.
	 */
	public Similar2LogoHtmlGenerator(
		String htmlBody,
		IHtmlInitializationData initializationData
	) {
		super(htmlBody, initializationData);
	}

	/**
	 * Builds a HTML code generator where the body of the web GUI is empty.
	 * @param initializationData The object providing initialization data to this view.
	 * @throws IOException 
	 */
	public Similar2LogoHtmlGenerator(
		IHtmlInitializationData initializationData
	) throws IOException {
		this(Similar2LogoHtmlGenerator.class.getResourceAsStream("gridview.html"), initializationData );
	}
	
	/**
	 * Builds a HTML code generator where the body of the web GUI is obtained through an input stream.
	 * @param htmlBody The body of the web GUI.
	 * @param initializationData The object providing initialization data to this view.
	 * @throws IOException 
	 */
	public Similar2LogoHtmlGenerator(
		InputStream htmlBody,
		IHtmlInitializationData initializationData
	) throws IOException {
		this(IOUtils.toString(htmlBody), initializationData );
	}
	
	
	/**
	 * Generates the HTML code of the header of the GUI.
	 * @return the header of the web GUI.
	 * @throws IOException 
	 */
	@Override
	public String renderHtmlHeader( ) throws IOException {
		
		StringBuilder output =  new StringBuilder();
		
		output	.append(IOUtils.toString(
			SimilarHtmlGenerator.class.getResourceAsStream("guiheader.html")
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
}
