package fr.lgi2a.similar2logo.examples.virus.probes;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

/**
 * The Http handler of the probe.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan"
 *         target="_blank">Gildas Morvan</a>
 *
 */
public class ProbeHttpHandler implements HttpHandler {

	private String mainPage = "<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n<title>Result</title>\n<style type=\"text/css\">\n#chart_div { position: absolute; left: 10px; right: 10px; top: 40px; bottom: 10px; }\n</style>\n</head>\n<body>\n<div id=\"chart_div\"></div>\n<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css\" integrity=\"sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7\" crossorigin=\"anonymous\">\n<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script>\n<script src=\"http://cdnjs.cloudflare.com/ajax/libs/dygraph/1.1.1/dygraph-combined.js\"></script>\n<script type=\"text/javascript\">\ng = new Dygraph(document.getElementById(\"chart_div\"),\n\"http://localhost:8080/result.txt\",\n{\nshowRoller: false,\ncustomBars: false,\ntitle: \"Population dynamics\",\nlabels: [\"Time\", \"nbOfAgents\", \"nbOfInfectedAgents\", \"nbOfImmuneAgents\", \"nbOfNeverInfectedAgents\"],\nlegend: \"follow\",\nlabelsSeparateLines: true } );\nwindow.intervalId = setInterval(function() {\nlocation.reload();\n},\n4000);\n</script>\n</body>\n</html>";

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handle(HttpExchange t) throws IOException {
		OutputStream os = t.getResponseBody();

		String fileName = t.getRequestURI().getPath();
		
		Headers h = t.getResponseHeaders();
		byte[] response;
		if (fileName.endsWith("/")) {
			response = mainPage.getBytes();
			h.add("Content-Type", "text/html");
		} else {

			h.add("Content-Type", "text");
			if (Paths.get("results"+fileName).toFile().exists()) {
				response = Files.readAllBytes(Paths.get("results"+fileName));
			} else {
				response = new String("Error 404").getBytes();

			}
		}

		t.sendResponseHeaders(200, response.length);
		os.write(response);
		os.close();
	}

}
