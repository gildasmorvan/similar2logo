# Similar2Logo

Similar2Logo is a Logo-like multiagent-based simulation environment based on the [SIMILAR](http://www.lgi2a.univ-artois.fr/~morvan/similar.html) API and released under the [CeCILL-B license](http://cecill.info).

Similar2Logo is written in [Java](https://en.wikipedia.org/wiki/Java_(software_platform)). GUIs are based on web technologies ([HTML5](https://en.wikipedia.org/wiki/HTML5)/[CSS](https://en.wikipedia.org/wiki/Cascading_Style_Sheets)/[js](https://en.wikipedia.org/wiki/JavaScript)). Simulations can be developed in Java, [Groovy](https://en.wikipedia.org/wiki/Groovy_(programming_language)) or any [JVM language](https://en.wikipedia.org/wiki/List_of_JVM_languages).

The purpose of Similar2Logo is not to offer a fully integrated agent-based modeling environment such as [NetLogo](http://ccl.northwestern.edu/netlogo/), [Gama](http://gama-platform.org), [TurtleKit](http://www.madkit.net/turtlekit/) or [Repast](https://repast.github.io) but to explore the potential of

* the influences/reaction model, developed by the [SMILE](http://www.lirmm.fr/recherche/equipes/smile) team of [LIRMM](http://www.lirmm.fr) lab at [Université de Montpellier](http://www.umontpellier.fr),

* the [interaction-oriented modeling](http://www.cristal.univ-lille.fr/SMAC/projects/ioda/) approach developed by the [SMAC](http://www.cristal.univ-lille.fr/SMAC/) team of [CRISTAL](http://cristal.univ-lille.fr) lab at [Université de Lille](https://www.univ-lille.fr),

* web technologies to produce portable simulations.

To understand the philosophy of Similar2Logo, it may be interesting to first look at the [SIMILAR documentation](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/README.html) and read the papers about the [influences/reaction model](http://www.aaai.org/Papers/ICMAS/1996/ICMAS96-009.pdf), the [IRM4S (Influence/Reaction Principle for Multi-Agent Based Simulation) model](http://www.aamas-conference.org/Proceedings/aamas07/html/pdf/AAMAS07_0179_07a7765250ef7c3551a9eb0f13b75a58.pdf) and the [interaction-oriented modeling](https://hal.inria.fr/hal-00825534/document) approach.


## Contents of the README

* [License](#license)

* [Contributors](#contributors)

* [Technical architecture of Similar2Logo](#architecture)

* [Compiling and running Similar2Logo](#compile)

* [Develop your own agent-based models](#develop)

	* [Basic structure of a Similar2Logo simulation](#structure)

	* [Java examples](#jexamples)

		* [A first example with a passive turtle](#jpassive)

        * [Adding a user-defined decision model to the turtles: The boids model](#jboids)

        * [Dealing with marks: the turmite model](#jturmite)

        * [Adding an interaction and a user-defined reaction model: The multiturmite model](#jmultiturmite)

	* [Groovy examples](#gexamples)
    
        * [A first example with a passive turtle](#gpassive)

        * [Adding a user-defined decision model to the turtles: The boids model](#gboids)
        
        * [Dealing with marks: the turmite model](#gturmite)


## <a name="license"></a> License

Similar2Logo is distributed under the [CeCILL-B license](http://cecill.info). In a few words, "if the initial program is under CeCILL-B, you can distribute your program under any license that you want (without the need to distribute the source code) provided you properly mention the use that you did of the initial program" (from the [CeCILL FAQ](http://www.cecill.info/faq.en.html#differences) ).

See the file  [LICENSE.txt](https://forge.univ-artois.fr/gildas.morvan/similar2logo/blob/master/LICENSE.txt) for more information. 

## <a name="contributors"></a> Contributors

Jonathan JULIEN - [mail](mailto:julienjnthn@gmail.com) - developer.

Yoann KUBERA - [mail](mailto:yoann.kubera@gmail.com) - [homepage](http://yoannkubera.net/) - designer of the SIMILAR API.

Antoine LECOUTRE - [mail](mailto:Antoine-Lecoutre@outlook.com) - developer.

Stéphane MEILLIEZ - [mail](mailto:stephane.meilliez@gmail.com) - developer.

Gildas MORVAN - [mail](mailto:gildas.morvan@univ-artois.fr) - [homepage](http://www.lgi2a.univ-artois.fr/~morvan/) - designer, developer.

## <a name="architecture"></a> Technical architecture of Similar2Logo

The following scheme presents the technical architecture of Similar2Logo.

![technical architecture of Similar2Logo](src/main/doc/img/similar2logoArchitecture.png)

* Similar2Logo runs on a web server based on the [Spark framework](http://sparkjava.com). By default its uses the `8080` port.

* The engine of Similar executes and probes the simulation.

* Users can interact with the simulation using a web GUI based on [Bootstrap](http://getbootstrap.com).

* Similar2Logo uses [jQuery](http://jquery.com) to control (start/pause/stop/quit) and change the parameters of the simulations.

* The web server will push the simulation data to the client using the [websocket protocol](https://en.m.wikipedia.org/wiki/WebSocket) in [JSON](http://www.json.org).


## <a name="compile"></a> Compiling and running Similar2Logo

### Using the binary distribution

A binary distribution of Similar2Logo can be downloaded at [this address](http://www.lgi2a.univ-artois.fr/~morvan/similar.html). It contains all the needed libraries and some simulation examples. It is probably the easiest way to start using Similar2Logo.

### Compiling Similar2Logo from the git repository with Maven.

The Similar2Logo project  uses the [git version control system](https://git-scm.com) and is hosted on the [forge of Université d'Artois](https://forge.univ-artois.fr). To compile Similar2Logo from the source you will need a [Java SE 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and the a software project management tool [Maven](https://maven.apache.org).

To clone the Similar and Similar2Logo repositories, use the following commands:

```
git clone https://forge.univ-artois.fr/yoann.kubera/similar.git
git clone https://forge.univ-artois.fr/gildas.morvan/similar2logo.git
```

To compile and install Similar and Similar2Logo on your system, use the following commands:

```
cd similar
mvn clean install
cd ../similar2logo
mvn clean install
```
The Similar2Logo project is divided into several sub-projects

* `similar2logo-kernel` contains the kernel of the platform,

* `similar2logo-lib`contains some useful libraries, such as generic perception and decision models, environment, probes to visualize and interact with the simulations, a web server that controls the execution of simulations, a HTML5/css/js GUI and random number generation tools.

* `similar2logo-com` contains tools based on [Mecsyco](http://mecsyco.com) to couple Similar2Logo with other simulators.

* `similar2logo-examples` contains simulation model examples written in Java and Groovy and eventually their associated GUIs.

* `similar2logo-distribution` allows to produce the binary distribution of Similar2Logo using the [Maven Assembly Plugin](http://maven.apache.org/plugins/maven-assembly-plugin/).


### Running Similar2Logo

When you launch a Similar2Logo simulation, your browser should open a page that looks like this.

![GUI of Similar2Logo. Boids example](src/main/doc/img/boids-example.png)


* You can change the parameters of the simulation using the panel on the left. When you hover on a parameter, a description of it should appear.

* You can control the simulation execution (start/stop/pause/quit) using the buttons on the upper right.

* The simulation will be displayed in the center of the web page. By default, it will display the turtles, marks and pheromone fields but you can add the visualization you want, for instance, the prey/predator simulation will display the population of preys, predators and grass in a chart.

![GUI of Similar2Logo. predation example](src/main/doc/img/predation-example.png)


## <a name="develop"></a> Develop your own multiagent-based simulations

### <a name="structure"></a> Basic structure of a Similar2Logo simulation

A typical Similar2Logo simulation will contain the following components:

* The parameters of the simulation, extending the class `LogoSimulationParameters`.

* An environment. By default it is a 2D grid on which turtles (i.e., Similar2logo agents), marks (i.e. passive objects) and [pheromones](https://en.wikipedia.org/wiki/Pheromone) are located and interact. It is implemented by  the `LogoEnvPLS` class. Following the influences/reaction model, the environment has its own dynamics, which means that it can emit influences. By default, the environment emits 2 influences at each step:

	* [AgentPositionUpdate](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/AgentPositionUpdate.html) which updates the position of turtles according to their dynamics (speed, acceleration and direction),

	* [PheromoneFieldUpdate](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/PheromoneFieldUpdate.html) which updates the pheromone fields.

* Turtle models. In Similar2Logo, following the IRM4S model, a turtle has

	* A state, defined by a class that inherits from `TurtlePLSInLogo`.

	* A perception model. By default, it is implemented in the `TurtlePerceptionModel` class, but you can define your own perception model if needed.

	* A decision model that will defines how a turtle produces influences according to its state and perceptions. It is implemented in a class that inherits from [AbstractAgtDecisionModel](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/api/fr/lgi2a/similar/extendedkernel/libs/abstractimpl/AbstractAgtDecisionModel.html).

* A set of influences that a turtle can produce. By default, the following influences can be used, but you may define your own influences if needed:

	* [ChangeAcceleration](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/ChangeAcceleration.html): an influence that aims at changing the acceleration of a turtle.

	* [ChangeDirection](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/ChangeDirection.html): an influence that aims at changing the direction of a turtle.

	* [ChangePosition](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/ChangePosition.html): an influence that aims at changing the position of a turtle.

	* [ChangeSpeed](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/ChangeSpeed.html):  an influence that aims at changing the speed of a turtle.

	* [DropMark](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/DropMark.html): an influence that aims at dropping a mark at a given location.

	* [EmitPheromone](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/EmitPheromone.html): an influence that aims at emitting a pheromone at given location.

	* [RemoveMark](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/RemoveMark.html): an influence that aims at removing a mark from the environment.

	* [RemoveMarks](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/RemoveMarks.html): an influence that aims at removing marks from the environment.

	* [Stop](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/Stop.html): an influence that aims at stopping a turtle.

	* [SystemInfluenceAddAgent](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/api/fr/lgi2a/similar/microkernel/influences/system/SystemInfluenceAddAgent.html): Adds a turtle to the simulation.

	* [SystemInfluenceRemoveAgent](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/api/fr/lgi2a/similar/microkernel/influences/system/SystemInfluenceRemoveAgent.html): Removes a turtle from the simulation.

* A reaction model which describes how influences are handled to compute the next simulation state. A default reaction model is implemented in `LogoDefaultReactionModel`, but you may define your own reaction model if needed.

* A simulation model that defines the initial state of the simulation. It is implemented in a class that inherits from `LogoSimulationModel`.

* A simulation engine, i.e., the algorithm that execute the simulation. By default, the engine of SIMILAR is used. 

* A set of probes, attached to the engine, that monitor the simulation. By default the following probes are launched:

    * `ProbeExecutionTracker`, that tracks the execution of the simulation and prints notification messages,
    
    * `ProbeExceptionPrinter`, that prints the trace of an exception that was thrown during the execution of the simulation,
    
    * `JSONProbe`, that prints information about the location of turtles, marks and phermones in a given target (in our case, a websocket) in the JSON format.
    
    * `InteractiveSimulationProbe`, that allows to pause and resume the simulation.
    
* A web server that serves as an interface between the web GUI and the engine. Since the version 0.7 of Similar2Logo, the `SparkHttpServer` is used.


### <a name="jexamples"></a> Java Examples

In the following we comment the examples written in Java distributed with Similar2Logo. Each example introduces a specific feature.

* [A first example with a passive turtle](#jpassive)

* [Adding a user-defined decision model to the turtles: The boids model](#jboids)

* [Dealing with marks: the turmite model](#jturmite)

* [Adding an interaction and a user-defined reaction model: The multiturmite model](#jmultiturmite)

#### <a name="jpassive"></a> A first example with a passive turtle

First we consider a simple example with a single passive agent. The example source code is located in the package `fr.lgi2a.similar2logo.examples.passive`. It contains 3 classes:

* `PassiveTurtleSimulationParameters`, that defines the parameters of the model. This class inherits from `fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters`.

* `PassiveTurtleSimulationModel`, that defines the simulation model, i.e, the initial state of the simulation. This class inherits from `fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel`.

* `PassiveTurtleSimulationMain`, the main class of the simulation.

##### Model parameters

The class [LogoSimulationParameters](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/LogoSimulationParameters.html) defines the generic parameters of a Logo-like simulation (environment size, topology, etc.).

The class  `PassiveTurtleSimulationParameters` contains the parameters specific to this model.

```
	@Parameter(
	   name = "initial x", 
	   description = "the initial position of the turtle on the x axis"
	)
	public double initialX;
	
	@Parameter(
	   name = "initial y", 
	   description = "the initial position of the turtle on the y axis"
	)
	public double initialY;
	
	@Parameter(
	   name = "initial speed", 
	   description = "the initial speed of the turtle"
	)
	public double initialSpeed;
	
	@Parameter(
	   name = "initial acceleration", 
	   description = "the initial acceleration of the turtle"
	)
	public double initialAcceleration;
	
	@Parameter(
	   name = "initial direction", 
	   description = "the initial direction of the turtle"
	)
	public double initialDirection;

```

Note that each parameter is prefixed with the `@Parameter` annotation. This annotation is mandatory to be able to change the value of the parameters in the GUI.

The default constructor of the `PassiveTurtleSimulationParameters` defines the default values of the simulation parameters.

```
    public PassiveTurtleSimulationParameters() {
		super();
		this.initialX = 10;
		this.initialY = 10;
		this.initialAcceleration = 0;
		this.initialDirection = LogoEnvPLS.NORTH;
		this.initialSpeed = 0.1;
		this.xTorus = true;
		this.yTorus = true;
		this.gridHeight = 20;
		this.gridWidth = 20;
		this.initialTime = new SimulationTimeStamp( 0 );
		this.finalTime = new SimulationTimeStamp( 3000 );
	}
```

##### The simulation model

The class [LogoSimulationModel](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/initializations/LogoSimulationModel.html) defines a generic simulation model of a Similar2Logo simulation. We must implement the `generateAgents` method to describe the initial state of our passive turtle. 

```
	protected AgentInitializationData generateAgents(
			ISimulationParameters parameters, Map<LevelIdentifier, ILevel> levels) {
		PassiveTurtleSimulationParameters castedParameters = (PassiveTurtleSimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		
		IAgent4Engine turtle = TurtleFactory.generate(
			new TurtlePerceptionModel(0, Double.MIN_VALUE, false, false, false),
			new PassiveTurtleDecisionModel(),
			new AgentCategory("passive", TurtleAgentCategory.CATEGORY),
			castedParameters.initialDirection,
			castedParameters.initialSpeed,
			castedParameters.initialAcceleration,
			castedParameters.initialX,
			castedParameters.initialY
		);
		result.getAgents().add( turtle );
		return result;
	}

```

Note that it is not necessary to define any class related to our turtle. Since it is passive, we use a predefined decision model called `PassiveTurtleDecisionModel`.

As a perception module, we use the generic perception model `TurtlePerceptionModel` with a perception distance of `0` and a perception angle of `Double.MIN_VALUE`.

##### The Main class

In the main class, the simulation model is created and the web server is run. The three booleans in the constructor specify if the turtles, marks and pheromones will be diplayed in the GUI. Here, only the turtles are displayed.

Finally, the probe `LogoRealTimeMatcher` is added to the server to slow down the simulation so that its execution speed matches a specific factor of N steps per second.

The `main` method contains the following code:

```
	SparkHttpServer http = new SparkHttpServer(new PassiveTurtleSimulationModel(new PassiveTurtleSimulationParameters()), true, false, false);
	
	http.getEngine().addProbe("Real time matcher", new LogoRealTimeMatcher(20));
```



#### <a name="jboids"></a> Adding a decision module to the turtles: The boids model

The [boids](https://en.wikipedia.org/wiki/Boids) (bird-oid) model has been invented by [https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)](https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)) in 1986 to simulate flocking behavior of birds. It is based on 3 principles:
    
* separation: boids tend to avoid other boids that are too close,

* alignment: boids tend to align their velocity to boids that are not too close and not too far away,

* cohesion: bois tend to move towards boids that are too far away.

While these rules are essentially heuristic, they can be implemented defining three areas for each principle. 

* Boids change their orientation to get away from other boids in the repulsion area,

* Boids change their orientation and speed to match those of other boids in the orientation area,

* Boids change their orientation to get to other boids in the attraction area.

An implementation of such model is located in the package `fr.lgi2a.similar2logo.examples.boids`.

The model itself is defined in the package `fr.lgi2a.similar2logo.examples.boids.model` which contains 2 classes:

* `BoidsSimulationParameters`, that defines the parameters of the model. This class inherits from `fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters`,

* `BoidDecisionModel`, that defines the decision model of the boids. This class inherits from `fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel`.

The simulation model and main class are located in the main package.


##### Model parameters

The `BoidsSimulationParameters` class contains the following parameters:

```
	@Parameter(
	   name = "repulsion distance", 
	   description = "the repulsion distance"
	)
	public double repulsionDistance;
	
	@Parameter(
       name = "attraction distance", 
	   description = "the attraction distance"
	)
	public double attractionDistance;
	
	@Parameter(
	   name = "orientation distance", 
	   description = "the orientation distance"
	)
	public double orientationDistance;
	
	@Parameter(
	   name = "maximal initial speed", 
	   description = "the maximal initial speed of boids"
	)
	public double maxInitialSpeed;
	
	@Parameter(
		name = "minimal initial speed", 
		description = "the minimal initial speed of boids"
	)
	public double minInitialSpeed;
	
	@Parameter(
	   name = "perception angle", 
	   description = "the perception angle of the boids in rad"
	)
	public double perceptionAngle;
	
	@Parameter(
	   name = "number of agents", 
	   description = "the number of agents in the simulation"
	)
	public int nbOfAgents;

	@Parameter(
	   name = "max angular speed", 
	   description = "the maximal angular speed of the boids in rad/step"
	)
	public double maxAngle;
```

##### The behavior of the boids 

The decision model consists in changing the direction and speed of the boids according to the previously described rules.
To define a decision model, the modeller must define a class that extends `fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel` and implement the `decide` method.


```
	@Override
	public void decide(SimulationTimeStamp timeLowerBound,
		SimulationTimeStamp timeUpperBound,
		IGlobalState globalState,
		ILocalStateOfAgent publicLocalState,
		ILocalStateOfAgent privateLocalState,
		IPerceivedData perceivedData,
		InfluencesMap producedInfluences
	) {
		
		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		
		if(!castedPerceivedData.getTurtles().isEmpty()) {
			double orientationSpeed = 0;
			double sinAngle = 0;
			double cosAngle = 0;
			int nbOfTurtlesInOrientationArea = 0;
			for (LocalPerceivedData<TurtlePLSInLogo> perceivedTurtle : castedPerceivedData.getTurtles()) {
				if (!perceivedTurtle.equals(publicLocalState)) {
					if (perceivedTurtle.getDistanceTo() <= this.parameters.repulsionDistance) {
						sinAngle+=Math.sin(castedPublicLocalState.getDirection()- perceivedTurtle.getDirectionTo());
						cosAngle+=Math.cos(castedPublicLocalState.getDirection()- perceivedTurtle.getDirectionTo());
					} else if (perceivedTurtle.getDistanceTo() <= this.parameters.orientationDistance) {
						sinAngle+=Math.sin(perceivedTurtle.getContent().getDirection() - castedPublicLocalState.getDirection());
						cosAngle+=Math.cos(perceivedTurtle.getContent().getDirection() - castedPublicLocalState.getDirection());
						orientationSpeed+=perceivedTurtle.getContent().getSpeed() - castedPublicLocalState.getSpeed();
						nbOfTurtlesInOrientationArea++;
					} else if (perceivedTurtle.getDistanceTo() <= this.parameters.attractionDistance){
						sinAngle+=Math.sin(perceivedTurtle.getDirectionTo()- castedPublicLocalState.getDirection());
						cosAngle+=Math.cos(perceivedTurtle.getDirectionTo()- castedPublicLocalState.getDirection());
					}
				}
			}
			sinAngle /= castedPerceivedData.getTurtles().size();
			cosAngle /= castedPerceivedData.getTurtles().size();
			double dd = Math.atan2(sinAngle, cosAngle);
			if (dd != 0) {
				if(dd > parameters.maxAngle) {
					dd = parameters.maxAngle;
				}else if(dd<-parameters.maxAngle) {
					dd = -parameters.maxAngle;
				}
				producedInfluences.add(
					new ChangeDirection(
						timeLowerBound,
						timeUpperBound,
						dd,
						castedPublicLocalState
					)
				);
			}
			if (nbOfTurtlesInOrientationArea > 0) {
				orientationSpeed /= nbOfTurtlesInOrientationArea;
				producedInfluences.add(
					new ChangeSpeed(
						timeLowerBound,			
						timeUpperBound,
						orientationSpeed,
						castedPublicLocalState
					)
				);
			}
		}
	}	

```

##### The simulation model

In the simulation model defined in our example, boids are initially located at the center of the environment with a random orientation and speed.

```
	@Override
	protected AgentInitializationData generateAgents(
		ISimulationParameters parameters, Map<LevelIdentifier, ILevel> levels
	) {
		BoidsSimulationParameters castedParameters = (BoidsSimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		for(int i = 0; i < castedParameters.nbOfAgents; i++) {
			result.getAgents().add(generateBoid(castedParameters));
		}
		return result;
	}
	
	private static IAgent4Engine generateBoid(BoidsSimulationParameters p) {
		return TurtleFactory.generate(
			new TurtlePerceptionModel(
				p.attractionDistance,p.perceptionAngle,true,false,false
			),
			new BoidDecisionModel(p),
			new AgentCategory("b", TurtleAgentCategory.CATEGORY),
			Math.PI-RandomValueFactory.getStrategy().randomDouble()*2*Math.PI,
			p.minInitialSpeed + RandomValueFactory.getStrategy().randomDouble()*(
				p.maxInitialSpeed-p.minInitialSpeed
			),
			0,
			p.gridWidth/2,
			p.gridHeight/2
		);
	}
```

We use the `fr.lgi2a.similar2logo.lib.tools.RandomValueFactory` class to generate random numbers which uses the Java [SecureRandom](http://docs.oracle.com/javase/7/docs/api/java/security/SecureRandom.html) implementation by default.


##### The main class

In the main class, such as in the previous example, the simulation model is created and the web server is run. 
The `main` method contains the following code:

```
        SparkHttpServer http = new SparkHttpServer(new BoidsSimulationModel(new BoidsSimulationParameters()), true, false, false);
```

The main class is very similar to the previous example. Only the simulation model has been changed.


#### <a name="jturmite"></a> Dealing with marks: the turmite model

The [turmite model](https://en.wikipedia.org/wiki/Langton's_ant), developed by [Christopher Langton](https://en.wikipedia.org/wiki/Christopher_Langton) in 1986, is a very simple mono-agent model that exhibits an emergent behavior. It is based on 2 rules:

* If the turmite is on a patch that does not contain a mark, it turns right, drops a mark, and moves forward,

* If the turmite is on a patch that contains a mark, it turns left, removes the mark, and moves forward.

The example source code is located in the package `fr.lgi2a.similar2logo.examples.turmite`. It contains 3 classes:

* `TurmiteDecisionModel` that defines the decision model of the turmites,

* `TurmiteSimulationModel` that defines the simulation model,

* `TurmiteSimulationMain`, the main class of the simulation.


##### The decision model

The decision model implements the above described rules :

```
	@Override
	public void decide(SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		
		if(castedPerceivedData.getMarks().isEmpty()) {
			producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					Math.PI/2,
					castedPublicLocalState
				)
			);
			producedInfluences.add(
				new DropMark(
					timeLowerBound,
					timeUpperBound,
					new Mark<Object>(
						(Point2D) castedPublicLocalState.getLocation().clone(),
						null
					)
				)
			);
		} else {
			producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					-Math.PI/2,
					castedPublicLocalState
				)
			);
			
			producedInfluences.add(
				new RemoveMark(
					timeLowerBound,
					timeUpperBound,
					castedPerceivedData.getMarks().iterator().next().getContent()
				)
			);
		}
		

	}
```

##### The simulation model

The simulation model generates a turmite at a given location, with a speed of 1:

```
	@Override
	protected AgentInitializationData generateAgents(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		AgentInitializationData result = new AgentInitializationData();	
		IAgent4Engine turtle = TurtleFactory.generate(
			new TurtlePerceptionModel(0, Double.MIN_VALUE, false, true, false),
			new TurmiteDecisionModel(),
			new AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
			LogoEnvPLS.NORTH,
			1,
			0,
			10.5,
			10.5
		);
		result.getAgents().add( turtle );
		return result;	
	}
```

##### The main class

In the main class, such as in the previous example, the simulation model is created and the web server is run. 
The `main` method contains the following code:

```
    SparkHttpServer http = new SparkHttpServer(new TurmiteSimulationModel(parameters), true, true, false);
```

The main difference with the previous example is that in this case we want to observe turtles and marks.

#### <a name="jmultiturmite"></a> Adding an interaction and a user-defined reaction model: The multiturmite model

The goal of this example is to implement the multiturmite model proposed by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [this paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf). It extends the traditional [Langton's ant model](http://en.wikipedia.org/wiki/Langton%27s_ant) by specifying what happens when conflicting influences (removing or dropping a mark to the same location) are detected. The following policy is applied:

* if the parameter `dropMark` is `true`, the dropping influence takes precedent over the removing one and reciprocally.

* if the parameter `removeDirectionChange` is `true`, direction changes are not taken into account.

It allows to define 4 different reaction models according to these parameters.

This model is located in the `fr.lgi2a.similar2logo.examples.multiturmite` package and contains at least 5 classes:

* `MultiTurmiteSimulationParameters`, that contains the parameters of the model,

* `TurmiteInteraction`, that defines an interaction between multiple turmites,

* `MultiTurmiteReactionModel`, that extends `fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel` and defines the reaction model, i.e., the way influences are handled,

* `MultiTurmiteSimulationModel` that defines the simulation model,

* Different main classes that define a specific initial configuration of the simulation, in our case, based on the ones described by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [their paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf).

##### Model parameters

The model parameters are defined in the class `fr.lgi2a.similar2logo.examples.multiturmite.model.MultiTurmiteSimulationParameters`. It defines how influences are handled according to the previously defined policy, the number of turmites and their initial locations.

```
	@Parameter(
	   name = "remove direction change", 
	   description = "if checked, direction changes are not taken into account when two turtles want to modify the same patch"
	)
	public boolean removeDirectionChange;
	
	/**
	 * <code>true</code> if the output of turtle actions is inversed
	 * when two turtles want to modify the same patch.
	 */
	@Parameter(
	   name = "inverse mark update", 
	   description = "if checked, the output of turtle actions is inversed when two turtles want to modify the same patch"
	)
	public boolean inverseMarkUpdate;
	
	/**
	 * The number of turmites in the environment.
	 */
	@Parameter(
	   name = "number of turmites", 
	   description = "the  number of turmites in the environment"
	)
	public int nbOfTurmites;
	
	/**
	 * The initial locations of turmites. If it is empty,
	 * turmites are placed randomly.
	 */
	@Parameter(
	   name = "initial locations", 
	   description = "the  initial locations of turmites"
	)
	public List<Point2D> initialLocations;
	
	/**
	 * The initial directions of turmites. If it is empty,
	 * turmites head randomly.
	 */
	@Parameter(
	   name = "initial directions", 
	   description = "the initial directions of turmites"
	)
	public List<Double> initialDirections;
```

##### The reaction model

In the previous example, the influence management relies on the default reaction model defined in the class `fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel`. Now, we want to handle some influences manually. To do so, we have to define a class `fr.lgi2a.similar2logo.examples.multiturmite.model.level.MultiTurmiteReactionModel` that inherits from `LogoDefaultReactionModel`. This class has then one attribute: the parameters of the simulation.

```
    private MultiTurmiteSimulationParameters parameters

```

What we have to do is to change the behavior of the `makeRegularReaction` method. A generic stub of a specific reaction model is given below:

```
public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		Set<IInfluence> nonSpecificInfluences = new LinkedHashSet<IInfluence>();


		//Management of specific influences
		
		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, nonSpecificInfluences, remainingInfluences);
	}
```

The idea is to identify the influences that do not trigger a generic reaction and manage them separately. Non specific influences are handled by the regular reaction.

In this case, specific influences represents collisions between turtle decisions. We define a class `TurmiteInteraction` that explicitly represent possible collisions for each location.

```
public class TurmiteInteraction {

	private Set<DropMark> dropMarks ;
	private Set<RemoveMark> removeMarks;
	private Set<ChangeDirection> changeDirections;
	
	/**
	 * 
	 */
	public TurmiteInteraction() {
		dropMarks = new LinkedHashSet<DropMark>();
		removeMarks = new LinkedHashSet<RemoveMark>();
		changeDirections = new LinkedHashSet<ChangeDirection>();
	}
	/**
	 * 
	 * @return <code>true</code> if there is a collision
	 */
	public boolean isColliding() {
		return removeMarks.size() > 1|| dropMarks.size() > 1;
	}

	//Getters and setters
}
```

Then, it is easy to implement the reaction model whether the influences are colliding or not:

```
	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		Set<IInfluence> nonSpecificInfluences = new LinkedHashSet<IInfluence>();
		Map<Point2D,TurmiteInteraction> collisions = new LinkedHashMap<Point2D,TurmiteInteraction>();
		
		//Organize influences by location and type
		for(IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
			if(influence.getCategory().equals(DropMark.CATEGORY)) {
				DropMark castedDropInfluence = (DropMark) influence;
				if(!collisions.containsKey(castedDropInfluence.getMark().getLocation())) {
					collisions.put(
						castedDropInfluence.getMark().getLocation(),
						new TurmiteInteraction()
					);
				} 
				collisions.get(castedDropInfluence.getMark().getLocation()).getDropMarks().add(castedDropInfluence);
	
			} else if(influence.getCategory().equals(RemoveMark.CATEGORY)) {
				RemoveMark castedRemoveInfluence = (RemoveMark) influence;
				if(!collisions.containsKey(castedRemoveInfluence.getMark().getLocation())) {
					collisions.put(
						castedRemoveInfluence.getMark().getLocation(),
						new TurmiteInteraction()
					);
				}
				collisions.get(castedRemoveInfluence.getMark().getLocation()).getRemoveMarks().add(castedRemoveInfluence);
			} else if(influence.getCategory().equals(ChangeDirection.CATEGORY)) {
				ChangeDirection castedChangeDirectionInfluence = (ChangeDirection) influence;
				if(!collisions.containsKey(castedChangeDirectionInfluence.getTarget().getLocation())) {
					collisions.put(
						castedChangeDirectionInfluence.getTarget().getLocation(),
						new TurmiteInteraction()
					);
				}
				collisions.get(castedChangeDirectionInfluence.getTarget().getLocation()).getChangeDirections().add(castedChangeDirectionInfluence);
			} else {
				nonSpecificInfluences.add(influence);
			}
		}
	
		for(Map.Entry<Point2D, TurmiteInteraction> collision : collisions.entrySet()) {
			if(collision.getValue().isColliding()) {
				if(!collision.getValue().getDropMarks().isEmpty() && !this.parameters.inverseMarkUpdate) {
					nonSpecificInfluences.add(
						collision.getValue().getDropMarks().iterator().next()
					);
				} if(!collision.getValue().getRemoveMarks().isEmpty() && !this.parameters.inverseMarkUpdate)  {
					nonSpecificInfluences.add(
						collision.getValue().getRemoveMarks().iterator().next()
					);
				}
				
				if(!this.parameters.removeDirectionChange) {
					nonSpecificInfluences.addAll(collision.getValue().getChangeDirections());
				}
			} else {
				nonSpecificInfluences.addAll(collision.getValue().getChangeDirections());
				if(!collision.getValue().getDropMarks().isEmpty()) {
					nonSpecificInfluences.add(collision.getValue().getDropMarks().iterator().next());
				}
				if(!collision.getValue().getRemoveMarks().isEmpty()) {
					nonSpecificInfluences.add(collision.getValue().getRemoveMarks().iterator().next());
				}
			}
		}
		
		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, nonSpecificInfluences, remainingInfluences);
	}
```
##### The simulation model

The simulation model of this example is located in the class `fr.lgi2a.similar2logo.examples.multiturmite.initializations.MultiTurmiteSimulationModel`.

Such as in the previous example, we have to redefine the method `generateAgents` to specify the initial population of agents of the simulation:

```
	protected AgentInitializationData generateAgents(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		AgentInitializationData result = new AgentInitializationData();	
		MultiTurmiteSimulationParameters castedSimulationParameters = (MultiTurmiteSimulationParameters) simulationParameters;
		if(castedSimulationParameters.initialLocations.isEmpty()) {
			for(int i = 0; i < castedSimulationParameters.nbOfTurmites; i++) {
				IAgent4Engine turtle = TurtleFactory.generate(
					new TurtlePerceptionModel(0, Double.MIN_VALUE, false, true, false),
					new TurmiteDecisionModel(),
					new AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
					MultiTurmiteSimulationModel.randomDirection(),
					1,
					0,
					Math.floor(RandomValueFactory.getStrategy().randomDouble()*castedSimulationParameters.gridWidth),
					Math.floor(RandomValueFactory.getStrategy().randomDouble()*castedSimulationParameters.gridHeight)
				);
				result.getAgents().add( turtle );
			}
		} else {
			if(
				castedSimulationParameters.nbOfTurmites != castedSimulationParameters.initialDirections.size() ||
				castedSimulationParameters.nbOfTurmites != castedSimulationParameters.initialLocations.size()
			) {
				throw new UnsupportedOperationException("Inital locations and directions must be specified for each turmite");
			}
			for(int i = 0; i < castedSimulationParameters.nbOfTurmites; i++) {
				IAgent4Engine turtle = TurtleFactory.generate(
					new TurtlePerceptionModel(0, Double.MIN_VALUE, false, true, false),
					new TurmiteDecisionModel(),
					new AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
					castedSimulationParameters.initialDirections.get(i),
					1,
					0,
					castedSimulationParameters.initialLocations.get(i).getX(),
					castedSimulationParameters.initialLocations.get(i).getY()
				);
				result.getAgents().add( turtle );
			}
		}
		return result;	
	}
```

However, contrary to the previous examples, we have to redefine the method `generateLevels` to specify the reaction model we use:
```
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters) {
		MultiTurmiteSimulationParameters castedSimulationParameters = (MultiTurmiteSimulationParameters) simulationParameters;
		ExtendedLevel logo = new ExtendedLevel(
				castedSimulationParameters.getInitialTime(), 
				LogoSimulationLevelList.LOGO, 
				new PeriodicTimeModel( 
					1, 
					0, 
					castedSimulationParameters.getInitialTime()
				),
				new MultiTurmiteReactionModel(castedSimulationParameters)
			);
		List<ILevel> levelList = new LinkedList<ILevel>();
		levelList.add(logo);
		return levelList;
	}
```


##### The Main class

The main class contains the following code:
```
		MultiTurmiteSimulationParameters parameters = new MultiTurmiteSimulationParameters();
		parameters.initialTime = new SimulationTimeStamp( 0 );
		parameters.finalTime = new SimulationTimeStamp( 1000000 );
		parameters.xTorus = true;
		parameters.yTorus = true;
		parameters.gridHeight = 60;
		parameters.gridWidth = 60;
		parameters.nbOfTurmites = 4;
		parameters.inverseMarkUpdate = true;
		parameters.removeDirectionChange = false;
		
		//Create a specific instance
		parameters.initialLocations.add(new Point2D.Double(Math.floor(parameters.gridWidth/2),Math.floor(parameters.gridHeight/2)));
		parameters.initialDirections.add(LogoEnvPLS.NORTH);
		parameters.initialLocations.add(new Point2D.Double(Math.floor(parameters.gridWidth/2),Math.floor(parameters.gridHeight/2) + 1));
		parameters.initialDirections.add(LogoEnvPLS.SOUTH);
		parameters.initialLocations.add(new Point2D.Double(Math.floor(parameters.gridWidth/2) + 10,Math.floor(parameters.gridHeight/2)));
		parameters.initialDirections.add(LogoEnvPLS.NORTH);
		parameters.initialLocations.add(new Point2D.Double(Math.floor(parameters.gridWidth/2) + 10,Math.floor(parameters.gridHeight/2) + 1));
		parameters.initialDirections.add(LogoEnvPLS.SOUTH);
		
		MultiTurmiteSimulationModel simulationModel = new MultiTurmiteSimulationModel(
			parameters
		);
		
		SparkHttpServer http = new SparkHttpServer(simulationModel, true, true, false);
```

In this case, we create a specific instance of the multiturmite model with 4 turmites. This configuration described by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [their paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf) produces an intersting and distinctive emergent behaviors according to the values of `dropMark` and `removeDirectionChange` parameters.

Such as in the previous example, we want to observe the turtles and the marks.

#### Adding a pheromone: Heatbugs

"Heatbugs is an abstract model of the behavior of biologically-inspired agents that attempt to maintain an optimum temperature around themselves. It demonstrates how simple rules defining the behavior of agents can produce several different kinds of emergent behavior.

Heatbugs has been used as a demonstration model for many agent-based modeling toolkits." from [the Heatbugs page](http://ccl.northwestern.edu/netlogo/models/Heatbugs)  of the NetLogo documentation. 

This example illustrates how to add a pheromone field in a similar2logo simulation and how it can be used by turtles.

##### The parameters of the simulation

First, we define the parameters of Heatbugs in the class `HeatBugsSimulationParameters`:

```
	/**
	 * The number of bugs in the environment.
	 */
	public int nbOfBugs;
	
	/**
	 * The percentage of the world's heat that evaporates each cycle.
	 * A lower number means a world which cools slowly, a higher number
	 * is a world which cools quickly.
	 */
	public double evaporationRate;
	
	/**
	 * How much heat a patch (a spot in the world) diffuses to its neighbors.
	 * A higher number means that heat diffuses through the world quickly.
	 * A lower number means that patches retain more of their heat.
	 */
	public double diffusionRate;
	
	/**
	 * The minimum ideal temperatures for heatbugs. Each bug is given an ideal temperature
	 * between the min and max ideal temperature.
	 */
	public double minOptimalTemperature;
	
	/**
	 * The maximum ideal temperatures for heatbugs. Each bug is given an ideal temperature
	 * between the min and max ideal temperature.
	 */
	public double maxOptimalTemperature;
	
	/**
	 * The minimum heat that heatbugs generate each cycle. Each bug is given a
	 * output-heat value between the min and max output heat.
	 */
	public double minOutputHeat;
	
	/**
	 * The maximum heat that heatbugs generate each cycle. Each bug is given a
	 * output-heat value between the min and max output heat.
	 */
	public double maxOutputHeat;
	
	/**
	 * The chance that a bug will make a random move even if it would prefer to
	 * stay where it is (because no more ideal patch is available).
	 */
	public double randomMoveProbability;
	
	/**
	 * The relative difference between real and optimal temperature that triggers moves.
	 */
	public double unhappiness;
	
```

The parameters `evaporationRate` and `diffusionRate`relate to a pheromone field. It is instantiated in the constructor of `HeatBugsSimulationParameters`:

```
	public HeatBugsSimulationParameters() {
		super();

		//Default values for parameters…

		this.pheromones.add(
			new Pheromone("heat", this.diffusionRate, this.evaporationRate)
		);
	}
```

##### The decision model



### <a name="gexamples"></a> Groovy Examples

In the following we comment the examples written in Groovy distributed with Similar2Logo. Each example introduces a specific feature.

* [A first example with a passive turtle](#gpassive)

* [Adding a user-defined decision model to the turtles: The boids model](#gboids)

* [Dealing with marks: the turmite model](#gturmite)

#### <a name="gpassive"></a> A first example with a passive turtle

First we consider a simple example with a single passive agent. The example source code is located in the package `fr.lgi2a.similar2logo.examples.passive`. It contains 1 groovy script.

Foremost, we define the parameters of the model by creating an object that inherits from `fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters`, that contains the generic parameters of a Logo-like simulation (environment size, topology, etc.).

```
def simulationParameters = new LogoSimulationParameters() {

	@Parameter(
		name = "initial x",
		description = "the initial position of the turtle on the x axis"
	 )
	 public double initialX = 0
	 
	 @Parameter(
		name = "initial y",
		description = "the initial position of the turtle on the y axis"
	 )
	 public double initialY = 0
	 
	 @Parameter(
		name = "initial speed",
		description = "the initial speed of the turtle"
	 )
	 public double initialSpeed = 0.1
	 
	 @Parameter(
		name = "initial acceleration",
		description = "the initial acceleration of the turtle"
	 )
	 public double initialAcceleration = 0
	 
	 @Parameter(
		name = "initial direction",
		description = "the initial direction of the turtle"
	 )
	 public double initialDirection = LogoEnvPLS.NORTH

}
```
Note that each parameter is prefixed with the `@Parameter` annotation. This annotation is mandatory to be able to change the value of the parameters in the GUI.

Then, we define the simulation model i.e, the initial state of the simulation from `fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel` class. We must implement the `generateAgents` method to describe the initial state of our passive turtle.

```
def simulationModel = new LogoSimulationModel(simulationParameters) {
	protected AgentInitializationData generateAgents(
		ISimulationParameters p,
		Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData()
		IAgent4Engine turtle = TurtleFactory.generate(
			new TurtlePerceptionModel(0, Double.MIN_VALUE, false, false, false),
			new PassiveTurtleDecisionModel(),
			new AgentCategory("passive", TurtleAgentCategory.CATEGORY),
			p.initialDirection,
			p.initialSpeed,
			p.initialAcceleration,
			p.initialX,
			p.initialY
		)
		result.agents.add turtle
		return result
	}
}
```

Then we launch the web server. The three booleans in the constructor specify if the turtles, marks and pheromones will be diplayed in the GUI. Here, only the turtles are displayed.

```
def http = new SparkHttpServer(simulationModel, true, false, false)
```

Finally, the probe `LogoRealTimeMatcher` is added to the server to slow down the simulation so that its execution speed matches a specific factor of N steps per second.

```
http.engine.addProbe "Real time matcher", new LogoRealTimeMatcher(20)
```

#### <a name="gboids"></a> Adding a user-defined decision module to the turtles: The boids model

The [boids](https://en.wikipedia.org/wiki/Boids) (bird-oid) model has been invented by [https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)](https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)) in 1986 to simulate flocking behavior of birds. It is based on 3 principles:
    
* separation: boids tend to avoid other boids that are too close,

* alignment: boids tend to align their velocity to boids that are not too close and not too far away,

* cohesion: bois tend to move towards boids that are too far away.

While these rules are essentially heuristic, they can be implemented defining three areas for each principle. 

* Boids change their orientation to get away from other boids in the repulsion area,

* Boids change their orientation and speed to match those of other boids in the orientation area,

* Boids change their orientation to get to other boids in the attraction area.

An implementation of such model is located in the package `fr.lgi2a.similar2logo.examples.boids` which contains 1 groovy script called `GroovyBoidsSimulation`.

##### Model parameters

The model parameters and their default values are defined as in the previous example.

```
def parameters = new LogoSimulationParameters() {

	 @Parameter(name = "repulsion distance", description = "the repulsion distance")
	 public double repulsionDistance = 6
	 
	 @Parameter(name = "attraction distance", description = "the attraction distance")
	 public double attractionDistance = 14
	 
	 @Parameter(name = "orientation distance", description = "the orientation distance")
	 public double orientationDistance = 10
	 
	 @Parameter(name = "maximal initial speed", description = "the maximal initial speed")
	 public double maxInitialSpeed = 2
	 
	 @Parameter(name = "minimal initial speed", description = "the minimal initial speed")
	 public double minInitialSpeed = 1
	 
	 @Parameter(name = "perception angle", description = "the perception angle in rad")
	 public double perceptionAngle = PI
	 
	 @Parameter(name = "number of agents", description = "the number of boids in the simulation")
	 public int nbOfAgents = 200
	
	 @Parameter(name = "max angular speed", description = "the maximal angular speed in rad/step")
	 public double maxAngle = PI/8
}
```

##### The decision model

The decision model consists in changing the direction and speed of the boids according to the previously described rules.
To define a decision model, the modeller must define an object that extends `fr.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel` and implement the `decide` method.


```
def decisionModel = new AbstractAgtDecisionModel(LogoSimulationLevelList.LOGO) {
	void decide(																						
		SimulationTimeStamp s,																			
		SimulationTimeStamp ns,																			
		IGlobalState gs,																				
		ILocalStateOfAgent pls,																			
		ILocalStateOfAgent prls,																		
		IPerceivedData pd,																				
		InfluencesMap i																					
	) {	
		if(!pd.turtles.empty) {	
			def sc = 0, 																				
				sinoc = 0, 																				
				cosoc = 0, 																				
				n = 0																					
			pd.turtles.each{ boid ->																	
				switch(boid.distanceTo) {																
					case {it <= parameters.repulsionDistance}:											
						sinoc+=sin(pls.direction - boid.directionTo)
						cosoc+=cos(pls.direction - boid.directionTo)
						break
					case {it > parameters.repulsionDistance && it <= parameters.orientationDistance}:
						sinoc+=sin(boid.content.direction - pls.direction)
						cosoc+=cos(boid.content.direction - pls.direction)
						sc+=boid.content.speed - pls.speed
						n++
						break
					case {it > parameters.orientationDistance && it <= parameters.attractionDistance}:
						sinoc+=sin(boid.directionTo- pls.direction)
						cosoc+=cos(boid.directionTo- pls.direction)
						break
				}
			}
			def oc = atan2(sinoc/pd.turtles.size(), cosoc/pd.turtles.size())							
			if (oc != 0) {
				if(abs(oc) > parameters.maxAngle) oc = signum(oc)*parameters.maxAngle					
				i.add new ChangeDirection(s, ns, oc, pls)												
			}
			if (n > 0) i.add new ChangeSpeed(s, ns, sc/n, pls)											
		}
	}
}
```

##### The simulation model

In the simulation model defined in our example, boids are initially located at the center of the environment with a random orientation and speed.


```
def simulationModel = new LogoSimulationModel(parameters) {												//defines the initial state of the simulation
	protected AgentInitializationData generateAgents(													//generates the boids
		ISimulationParameters p,
		Map<LevelIdentifier, ILevel> l
	) {
		def result = new AgentInitializationData()
		p.nbOfAgents.times {																			//for each boid to be generated
			result.agents.add TurtleFactory.generate(													//generates the boid
				new TurtlePerceptionModel(p.attractionDistance,p.perceptionAngle,true,false,false),		//defines the perception model of the boid
				decisionModel,																			//defines the decision model of the boid
				new AgentCategory("b", TurtleAgentCategory.CATEGORY),									//defines the category of the boid
				rand.randomAngle(),																		//defines initial the orientation of the boid
				p.minInitialSpeed + rand.randomDouble()*(p.maxInitialSpeed-p.minInitialSpeed),			//defines the initial speed of the boid
				0,																						//defines the initial acceleration of the boid
				p.gridWidth/2,																			//defines the initial x position of the boid
				p.gridHeight/2																			//defines the initial y position of the boid
			)
		}
		return result
	}
}
```

##### Launch the web server

Finally, we launch the web server as in the previous example.

```
new SparkHttpServer(simulationModel, true, false, false)
```

#### <a name="gturmite"></a> Dealing with marks: the turmite model

The [turmite model](https://en.wikipedia.org/wiki/Langton's_ant), developed by [Christopher Langton](https://en.wikipedia.org/wiki/Christopher_Langton) in 1986, is a very simple mono-agent model that exhibits an emergent behavior. It is based on 2 rules:

* If the turmite is on a patch that does not contain a mark, it turns right, drops a mark, and moves forward,

* If the turmite is on a patch that contains a mark, it turns left, removes the mark, and moves forward.

The example source code is located in the package `fr.lgi2a.similar2logo.examples.turmite`. It contains 1 Groovy script called `GroovyTurmiteSimulation`.

##### Model parameters

First we define the simulation parameters. Here we only need to specify the final step of the simulation:

```
def parameters = new LogoSimulationParameters(												
	finalTime: new SimulationTimeStamp(100000)
)
```
##### The decision model

The decision model implements the above described rules :

```
def decisionModel = new AbstractAgtDecisionModel(LogoSimulationLevelList.LOGO) {			
	void decide(
		SimulationTimeStamp s,																//the current simulation step
		SimulationTimeStamp ns,																//the next simulation step
		IGlobalState gs,																	//the global state of the agent
		ILocalStateOfAgent pls,																//the public local state of the agent
		ILocalStateOfAgent prls,															//the private local state of the agent
		IPerceivedData pd,																	//the data perceived by the agent
		InfluencesMap i																		//the influences produced by the agent
	) {	
		if(pd.marks.empty) i.with {															//if the agent perceives no mark
			add new ChangeDirection(s, ns, PI/2, pls)										//it turns pi/2 rad
			add new DropMark(s, ns, new Mark((Point2D) pls.location.clone(), null))			//and drops a mark
		} else i.with {																		//if the agent perceives a mark
			add new ChangeDirection(s, ns, -PI/2, pls)										//it turns -pi/2 rad
			add new RemoveMark(s, ns,pd.marks.iterator().next().content)					//and removes the mark
		}
	}
}
```

##### The simulation model

The simulation model generates a turmite at a given location, with a speed of 1:

```
def simulationModel = new LogoSimulationModel(parameters) {									//defines the initial state of the simulation
	protected AgentInitializationData generateAgents(										//generates the agents
		ISimulationParameters simulationParameters,											//the parameters of the simulation
		Map<LevelIdentifier, ILevel> levels													//the levels of the simulation
	) {
		def turmite = TurtleFactory.generate(												//creates a new turmite agent										
			new TurtlePerceptionModel(0, Double.MIN_VALUE, false, true, false),				//a perception model that allows to perceive marks
			decisionModel,																	//the turmite decision model
			new AgentCategory("turmite", TurtleAgentCategory.CATEGORY),						//the turmite category
			LogoEnvPLS.NORTH,																//heading north
			1,																				//a speed of 1
			0,																				//an acceleration of 0
			10.5, 10.5																		//located at 10.5, 10.5
		),
			result = new AgentInitializationData()											//creates the agent initialization data
		result.agents.add turmite															//adds the turmite agent
		return result
	}
}
```


##### Launch the web server

```
   new SparkHttpServer(simulationModel, true, true, false)
```

The main difference with the previous example is that in this case we want to observe turtles and marks.