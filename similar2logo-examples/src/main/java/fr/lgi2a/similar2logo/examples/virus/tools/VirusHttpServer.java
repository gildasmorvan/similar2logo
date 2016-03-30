package fr.lgi2a.similar2logo.examples.virus.tools;

import java.io.FileNotFoundException;

import fr.lgi2a.similar.microkernel.ISimulationEngine;
import fr.lgi2a.similar2logo.examples.virus.probes.ProbePrintingPopulation;
import fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel;
import fr.lgi2a.similar2logo.lib.tools.http.SimilarHttpServer;

public class VirusHttpServer extends SimilarHttpServer {

	public VirusHttpServer(ISimulationEngine engine, LogoSimulationModel model) {
		super(engine, model);
			try {
				engine.addProbe("Population printing", new ProbePrintingPopulation());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			this.getSimilarHttpHandler().setHtmlUserCode("<style type='text/css'>         #chart_div {             position: relative;             left: 10px;             right: 10px;             top: 40px;             bottom: 10px;         }     </style> <div id='chart_div'></div> <link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css' integrity='sha384-1q8mTJOASx8j1Au+a5WDVnPi2lkFfwwEAa8hDDdjZlpLegxhjVME1fgjWPGmkzs7' crossorigin='anonymous'> <script src='http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js'></script> <script src='http://cdnjs.cloudflare.com/ajax/libs/dygraph/1.1.1/dygraph-combined.js'></script> <script type='text/javascript'>     $(document).ready(function () {         g = new Dygraph(document.getElementById('chart_div'),'result.txt',         {             showRoller: false,             customBars: false,             title: 'Population dynamics',             labels: ['Time', 'nbOfAgents', 'nbOfInfectedAgents', 'nbOfImmuneAgents', 'nbOfNeverInfectedAgents'],             legend: 'follow',             labelsSeparateLines: true         });         setInterval(function() {             g.updateOptions( { 'file': 'result.txt' } );         }, 1000);     }); </script>");
			}
}
