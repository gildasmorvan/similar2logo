# How to use the spark server to run Similar2Logo simulations  ?

## Creating the web server

The `SparkHttpServer` has 3 constructors :

* A simple constructor requiring only the simulation model and 3 boolean parameters to export respectively the agents, marks and pheromones. For example : 
			
```
SparkHttpServer http = new SparkHttpServer(
	new SimulationModel(new SimulationParameters()),
	true,
	false,
	false
);
```

* A constructor with the same parameters as above and the url of the Html body of the GUI. For example :

```
SparkHttpServer http = new SparkHttpServer(
	new SimulationModel(new SimulationParameters()),
	true,
	true,
	true,
	SimulationMain.class.getResource("body.html")
);
```

* A constructor with the same parameters as above and the Html body of the GUI as a `String`. For example :

```
SparkHttpServer http = new SparkHttpServer(
	new SimulationModel(new SimulationParameters()),
	true,
	true,
	true,
	"<h2>Simulation</h2>"
	+ "<style type='text/css'>"
	+ "h2,h4{text-align:center;}"
	+ "#chart_div {width:100%;height:auto;}"
	+ "canvas{width:auto;height:100%;margin:auto;}"
	+ "</style>"
	+ "<div class='row'>"
	+ "<div class='col-md-4'>"
	.....
);
```

## Adding a probe

To add a probe we use this code :

```
http.getEngine().addProbe(identifier, probe);
```

* http is a name of your `SparkHttpServer`,

* identifier is a string where is a name of the probe,

* probe is a probe class.