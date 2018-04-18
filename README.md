![](src/main/doc/img/similar2logo-logo-small.png)

[![Download](https://img.shields.io/badge/download-latest%20binary%20distribution%20(v0.9)-blue.svg)](https://github.com/gildasmorvan/similar2logo/releases/download/v0.9/similar2logo-distribution-0.9-bin.zip)
[![Build Status](https://travis-ci.org/gildasmorvan/similar2logo.svg?branch=master)](https://travis-ci.org/gildasmorvan/similar2logo)
[![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=fr.lgi2a%3Asimilar2logo&metric=alert_status)](https://sonarcloud.io/dashboard?id=fr.lgi2a%3Asimilar2logo)
[![SonarCloud Bugs](https://sonarcloud.io/api/project_badges/measure?project=fr.lgi2a%3Asimilar2logo&metric=bugs)](https://sonarcloud.io/component_measures/metric/reliability_rating/list?id=fr.lgi2a%3Asimilar2logo)
[![SonarCloud Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=fr.lgi2a%3Asimilar2logo&metric=vulnerabilities)](https://sonarcloud.io/component_measures/metric/security_rating/list?id=fr.lgi2a%3Asimilar2logo)

Similar2Logo is a Logo-like **multiagent-based simulation environment** based on the [SIMILAR](http://www.lgi2a.univ-artois.fr/~morvan/similar.html) API and released under the [CeCILL-B license](http://cecill.info).

Similar2Logo is written in [Java](https://en.wikipedia.org/wiki/Java_(software_platform)). The GUI is based on web technologies ([HTML5](https://en.wikipedia.org/wiki/HTML5)/[CSS](https://en.wikipedia.org/wiki/Cascading_Style_Sheets)/[js](https://en.wikipedia.org/wiki/JavaScript)). Simulations can be developed in Java, [Groovy](https://en.wikipedia.org/wiki/Groovy_(programming_language)), [Ruby](https://en.wikipedia.org/wiki/Ruby_(programming_language)), [Kotlin](https://kotlinlang.org), [Python](https://www.python.org) or any [JVM language](https://en.wikipedia.org/wiki/List_of_JVM_languages).

The purpose of Similar2Logo is not to offer a fully integrated agent-based modeling environment such as [NetLogo](http://ccl.northwestern.edu/netlogo/), [Gama](http://gama-platform.org), [TurtleKit](http://www.madkit.net/turtlekit/) or [Repast](https://repast.github.io) but to explore the potential of

* the **influences/reaction model**, developed by the [SMILE](http://www.lirmm.fr/recherche/equipes/smile) team of [LIRMM](http://www.lirmm.fr) lab at [Université de Montpellier](http://www.umontpellier.fr),

* the [**interaction-oriented modeling**](http://www.cristal.univ-lille.fr/SMAC/projects/ioda/) approach developed by the [SMAC](http://www.cristal.univ-lille.fr/SMAC/) team of [CRISTAL](http://cristal.univ-lille.fr) lab at [Université de Lille](https://www.univ-lille.fr),

* [multi-level agent-based modeling](https://www.lgi2a.univ-artois.fr/~morvan/mlabm.html)

* **web technologies** to produce portable simulations.

To understand the philosophy of Similar2Logo, it might be interesting to first look at the [SIMILAR documentation](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/README.html) and read the papers about the [influences/reaction model](http://www.aaai.org/Papers/ICMAS/1996/ICMAS96-009.pdf), the [IRM4S (Influence/Reaction Principle for Multi-Agent Based Simulation) model](http://www.aamas-conference.org/Proceedings/aamas07/html/pdf/AAMAS07_0179_07a7765250ef7c3551a9eb0f13b75a58.pdf) and the [interaction-oriented modeling](https://hal.inria.fr/hal-00825534/document) approach.


# Contents of the README

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
        
        * [Adding user-defined influence, reaction model and GUI: The segregation model](#jsegregation)
        
        * [Adding a hidden state to the turtles and a pheromone field: The heatbugs model](#jheatbugs)

	* [Groovy examples](#gexamples)
    
        * [A first example with a passive turtle](#gpassive)

        * [Adding a user-defined decision model to the turtles: The boids model](#gboids)
        
        * [Dealing with marks: the turmite model](#gturmite)
        
        * [Adding user-defined influence, reaction model and GUI: The segregation model](#gsegregation)
        
    * [Ruby examples](#rexamples)
    
        * [A first example with a passive turtle](#rpassive)
        
        * [Adding a user-defined decision model to the turtles: The boids model](#rboids)
        
        * [Dealing with marks: the turmite model](#rturmite)
        
        * [Adding user-defined influence, reaction model and GUI: The segregation model](#rsegregation)
        
    * [Kotlin examples](#kexamples)
    
        * [A first example with a passive turtle](#kpassive)
        
        * [Adding a user-defined decision model to the turtles: The boids model](#kboids)
        
        * [Dealing with marks: the turmite model](#kturmite)
        
        * [Adding an interaction and a user-defined reaction model: The multiturmite model](#kmultiturmite)
        
        * [Adding user-defined influence, reaction model and GUI: The segregation model](#ksegregation)
        
    * [Python examples](#pexamples)    
        
        * [A first example with a passive turtle](#ppassive)

        * [Adding a user-defined decision model to the turtles: The boids model](#pboids)

        * [Dealing with marks: the turmite model](#pturmite)

        * [Adding an interaction and a user-defined reaction model: The multiturmite model](#pmultiturmite)

# <a name="license"></a> License

Similar2Logo is distributed under the [CeCILL-B license](http://cecill.info). In a few words, "if the initial program is under CeCILL-B, you can distribute your program under any license that you want (without the need to distribute the source code) provided you properly mention the use that you did of the initial program" (from the [CeCILL FAQ](http://www.cecill.info/faq.en.html#differences) ).

See the file  [LICENSE.txt](LICENSE.txt) for more information. 

# <a name="contributors"></a> Contributors

Jonathan JULIEN - [mail](mailto:julienjnthn@gmail.com) - developer.

Yoann KUBERA - [mail](mailto:yoann.kubera@gmail.com) - [homepage](http://yoannkubera.net/) - designer of the SIMILAR API, developer.

Antoine LECOUTRE - [mail](mailto:Antoine-Lecoutre@outlook.com) - developer.

Stéphane MEILLIEZ - [mail](mailto:stephane.meilliez@gmail.com) - developer.

Gildas MORVAN - [mail](mailto:gildas.morvan@univ-artois.fr) - [homepage](http://www.lgi2a.univ-artois.fr/~morvan/) - designer, main developer.

Romain WINDELS - [mail](mailto:romainwindels@yahoo.fr) - developer.

# <a name="architecture"></a> Technical architecture of Similar2Logo

The following scheme presents the technical architecture of Similar2Logo.

![technical architecture of Similar2Logo](src/main/doc/img/similar2logoArchitecture.png)

* Similar2Logo runs on a web server based on the [Spark framework](http://sparkjava.com). By default it uses the `8080` port.

* The engine of Similar executes and probes the simulation.

* Users can interact with the simulation using a web GUI based on [Bootstrap](http://getbootstrap.com).

* Similar2Logo uses [jQuery](http://jquery.com) to control (start/pause/stop/quit) and change the parameters of the simulations.

* Simulation data are pushed by the web server to the client using the [websocket protocol](https://en.m.wikipedia.org/wiki/WebSocket) in [JSON](http://www.json.org).


## The Similar engine

The engine of Similar encapsulates the algorithm that runs a simulation model (see this [paper](https://arxiv.org/pdf/1703.02399.pdf) for more information about this algorithm). The default implementation is mono-threaded and executes the simulation as fast as possible. To monitor, interact with or execute the simulation in an other mode (e.g. step by step or in real time), probes can be attached to the engine. 

![the Similar engine](src/main/doc/img/similarEngine.png)

# <a name="compile"></a> Compiling and running Similar2Logo

## Cloud demo

You can try a [demo of the predation example](https://similar2logo-predationdemo.appspot.com/) shipped with Similar2Logo from Google cloud. It allows you to run a simulation and change its parameters.

## Using the binary distribution

A binary distribution of Similar2Logo can be downloaded at [this address](https://github.com/gildasmorvan/similar2logo/releases/download/v0.9/similar2logo-distribution-0.9-bin.zip). It contains all the needed libraries and some simulation examples. It is probably the easiest way to start using Similar2Logo.

To run a simulation written in Java, use the following command from the root directory of the distribution:

```
java -cp "lib/*" fr.lgi2a.similar2logo.examples.boids.BoidsSimulationMain
```

To run a simulation written in Groovy, you must install Groovy on your system and use the following command from the root directory of the distribution:

```
groovy -cp "lib/*" examples/boids/src/groovy/fr/lgi2a/similar2logo/examples/boids/GroovyBoidsSimulation
```

To run a simulation written in Python, you must install Jython on your system and use the following command from the root directory of the distribution:
```
jython  -J-cp "lib/*" examples/boids/src/python/fr/lgi2a/examples/boids/BoidsSimulation.py
```

To run a simulation written in Ruby, you must install [JRuby](http://jruby.org) on your system and use the following command from the root directory of the distribution:
```
jruby -J-cp "lib/*" examples/boids/src/ruby/fr/lgi2a/similar2logo/examples/boids/RubyBoidsSimulation.rb
```

Other simulations can be performed using a different main class or script. The main class or script of each simulation example and the corresponding execution command are identified in the README file located in sub-directories of the `examples` directory of the distribution.

## Getting Similar2Logo from the git repository

### Compiling Similar2Logo with Maven

The Similar2Logo project  uses the [git version control system](https://git-scm.com) and is hosted on [Github](https://github.com). To compile Similar2Logo from the source you will need a [Java SE 8 SDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and the software project management tool [Maven](https://maven.apache.org).

To clone the Similar and Similar2Logo repositories, use the following commands:

```
git clone https://github.com/gildasmorvan/similar.git
git clone https://github.com/gildasmorvan/similar2logo.git
```

To compile and install Similar and Similar2Logo on your system, use the following commands:

```
cd similar
mvn install
cd ../similar2logo
mvn install
```
The Similar2Logo project is divided into several sub-modules

* `similar2logo-kernel` contains the kernel of the platform,

* `similar2logo-lib` contains some useful libraries, such as generic perception and decision models, environment, probes to visualize and interact with the simulations, a web server that controls the execution of simulations, a HTML5/css/js GUI and random number generation tools.

* `similar2logo-com` contains tools based on [Mecsyco](http://mecsyco.com) to couple Similar2Logo with other simulators. **Note**: this module is experimental and therefore, not included in the binary distribution of Similar2Logo. To use it, uncomment the line 174 of the `pom.xml` of the main project.

* `similar2logo-examples` contains simulation model examples written in Java and Groovy and, if needed, their associated GUIs. Each example provides a main class that can be used to run the corresponding simulation.

* `similar2logo-distribution` allows to produce the binary distribution of Similar2Logo using the [Maven Assembly Plugin](http://maven.apache.org/plugins/maven-assembly-plugin/).

### Executing the Groovy examples from jupyter notebook

We provide a [jupyer notebook](https://github.com/gildasmorvan/similar2logo/blob/master/similar2logo-examples/src/main/doc/groovy-examples.ipynb) to try Groovy examples.

The notebook is located in the `similar2logo-examples/src/main/doc` directory.

To run the examples you must install the [scijava-jupyter-kernel](https://github.com/scijava/scijava-jupyter-kernel) and [compile Similar2Logo with maven](https://github.com/gildasmorvan/similar2logo/blob/master/README.md#compiling-similar2logo-with-maven).

To run the kernel, compile Similar2Logo with maven (see above) then run the following commands from the Similar2Logo base directory:

```
cd similar2logo-examples/src/main/doc
jupyter notebook
```

Note that you have to restart the kernel before running a new example.

### Running Similar2Logo

When using the git repository version of Similar2Logo, running simulations is easier with a Java IDE supporting maven, such that the [eclipse framework](https://eclipse.org/downloads/). Indeed, such framework automates the identification of the required libraries, and running a simulation simply requires to identify the main class of the simulation and run it through the IDE.

When you launch a Similar2Logo simulation, your browser should open a page that looks like this.

![<p align="center">GUI of Similar2Logo. Boids example</p>](src/main/doc/img/boids-example.png)


* You can change the parameters of the simulation using the panel on the left. When you hover on a parameter, a description of it should appear.

* You can control the simulation execution (start/stop/pause/quit) using the buttons on the upper right.

* The simulation will be displayed in the center of the web page. By default, it will display the turtles, marks and pheromone fields but you can add the visualization you want, for instance, the prey/predator simulation will display the population of preys, predators and grass in a chart.

![<p align="center">GUI of Similar2Logo. predation example</p>](src/main/doc/img/predation-example.png)


# <a name="develop"></a> Develop your own multiagent-based simulations

## <a name="structure"></a> Basic structure of a Similar2Logo simulation

A typical Similar2Logo simulation will contain the following components:

* If the model is stochastic, a [**pseudorandom number generator**](https://en.wikipedia.org/wiki/Pseudorandom_number_generator). The `PRNG` class is a simple factory relying on the `RandomValuesGenerator` class to generate pseudorandom numbers and shuffle lists. 

    * The following methods can be used to generate random numbers of different types:
    
        * `PRNG.get().randomDouble()` returns a random double between 0 (included) and 1 (excluded).
        
        * `PRNG.get().randomDouble(double lowerBound, double higherBound)` returns a random double within the given range.
        
        * `PRNG.get().randomAngle()` returns a random angle between -pi (included) and pi (excluded).
        
        * `PRNG.get().randomBoolean()` returns a random boolean.
        
        * `PRNG.get().randomInt(int bound)` returns a random integer.
        
        * `PRNG.get().randomGaussian()` returns a random double between 0 and 1 following a Gaussian.
        
    * The `PRNG.get().shuffle(List<?> l)` method can be used to shuffle a list.
    
    * The random number generator algorithm used by default is [xoroshiro128+](https://en.wikipedia.org/wiki/Xoroshiro128%2B) but it is possible to change it via the `PRNG.set()` method. There are various ways use it.
    
        * The simplest one is to use the algorithms shipped with Similar2Logo:
        
            * `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.XORO))` to use [xoroshiro128+](https://en.wikipedia.org/wiki/Xoroshiro128%2B),
            
            * `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.XOR))` to use [xorshift128+](https://en.wikipedia.org/wiki/Xorshift),
            
            * `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.LIGHT))` to use [SplitMix64](http://xoroshiro.di.unimi.it/splitmix64.c),
            
            * `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.MT_64))` to use [MT19937-64 (Mersenne Twister)](https://en.wikipedia.org/wiki/Mersenne_Twister),
            
            * `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.WELL_1024))` to use [WELL_1024](https://en.wikipedia.org/wiki/Well_equidistributed_long-period_linear),
            
            * `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.PCG))` to use [PCG](http://www.pcg-random.org),
            
            * `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.JDK))` to use [JDK implemetation of LCG](https://en.wikipedia.org/wiki/Linear_congruential_generator).
            
        * By default, the seed of the random number generator is generated using a [SecureRandom](https://docs.oracle.com/javase/8/docs/api/java/security/SecureRandom.html) instance. To set a given seed, use, e.g., `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.XORO, 21))`.
        
        * If the random number generator is used in a multithreaded context it must be synchronized. It is done this way: `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.XORO, true))` or with a given seed: `PRNG.set(new RandomValuesGenerator(RandomValuesGenerator.XORO, 21, true))`. Note: using a random number generator in a multithreaded context leads to non replicable experiments.
        
        * You can use other random number generators, since they are defined in a class that extends `java.util.Random` or implements `org.apache.commons.math3.random.RandomGenerator`. E.g., `PRNG.set(new RandomValuesGenerator(new Well512a()))`. To use it in a multithreaded context: `PRNG.set(new RandomValuesGenerator(new Well512a(), true))`.

* The **parameters of the simulation**, extending the class `LogoSimulationParameters`.

* An **environment**. By default it is a 2D grid discretized into patches on which **turtles** (i.e., Similar2Logo agents), **marks** (i.e.,  passive objects) and [**pheromone fields**](https://en.wikipedia.org/wiki/Pheromone) are located and interact. It is implemented by  the `LogoEnvPLS` class. Following the influences/reaction model, the environment has its own dynamics, which means that it can emit influences. By default, the environment emits 2 influences at each step:

	* `AgentPositionUpdate` which updates the position of turtles according to their dynamics (speed, acceleration and direction),

	* `PheromoneFieldUpdate` which updates the pheromone fields.

* **Turtle models**. In Similar2Logo, following the IRM4S model, a turtle has

	* A **public state** (i.e., that can be perceived by other agents), defined by a class that inherits from `TurtlePLSInLogo`.

	* A **perception model**. 2 perceptions models are shipped with Similar2Logo: `ConeBasedPerceptionModel` (a cone based perception model) and `EmptyPerceptionModel` (a model that perceives nothing), but you can define your own perception model if needed.

	* A **decision model** that will defines how a turtle produces influences according to its state and perceptions. It is implemented in a class that inherits from `AbstractAgtDecisionModel`.
    
    * Possibly, a **hidden state** (i.e., that cannot be perceived by other agents) that inherits from `AbstractLocalStateOfAgent`.

* A set of **influences** that a turtle can emit. By default, the following influences can be used, but you may define your own influences if needed:

	* `ChangeAcceleration`: an influence that aims at changing the acceleration of a turtle.

	* `ChangeDirection`: an influence that aims at changing the direction of a turtle.

	* `ChangePosition`: an influence that aims at changing the position of a turtle.

	* `ChangeSpeed`:  an influence that aims at changing the speed of a turtle.

	* `DropMark`: an influence that aims at dropping a mark at a given location.

	* `EmitPheromone`: an influence that aims at emitting a pheromone at given location.

	* `RemoveMark`: an influence that aims at removing a mark from the environment.

	* `RemoveMarks`: an influence that aims at removing marks from the environment.

	* `Stop`: an influence that aims at stopping a turtle.

	* `SystemInfluenceAddAgent`: Adds a turtle to the simulation.

	* `SystemInfluenceRemoveAgent`: Removes a turtle from the simulation.

* A **reaction model** which describes how influences are handled to compute the next simulation state. A default reaction model is implemented in `LogoDefaultReactionModel`, but you may define your own reaction model if needed.

* A **simulation model** that defines the initial state of the simulation. It is implemented in a class that inherits from `AbstractLogoSimulationModel`.

* A **simulation engine**, i.e., the algorithm that execute the simulation. By default, the mono-threaded engine of Similar is used. 

* A set of **probes**, attached to the engine, that monitor the simulation. By default the following probes are launched:

    * `Slf4jExecutionTracker`, that tracks the execution of the simulation and prints notification messages,
    
    * `Slf4jExceptionPrinter`, that prints the trace of an exception that was thrown during the execution of the simulation,
    
    * `JSONProbe`, that send information about the location of turtles, marks and phermones in JSON format to a websocket.
    
    * `InteractiveSimulationProbe`, that allows to pause and resume the simulation.
    
* A **web server** that serves as an interface between the web GUI and the engine. Since the version 0.9 of Similar2Logo, the class `Similar2LogoHtmlRunner` is used to control and configure it.

The easiest way to understand how to develop a simulation is to have a look at the [Java](#jexamples), [Groovy](#gexamples) or [Ruby](#rexamples) examples shipped with Similar2Logo.


## <a name="jexamples"></a> Java Examples

In the following we comment the examples written in Java distributed with Similar2Logo. Each example introduces a specific feature.

* [A first example with a passive turtle](#jpassive)

* [Adding a user-defined decision model to the turtles: The boids model](#jboids)

* [Dealing with marks: the turmite model](#jturmite)

* [Adding an interaction and a user-defined reaction model: The multiturmite model](#jmultiturmite)

* [Adding user-defined influence, reaction model and GUI: The segregation model](#jsegregation)

* [Adding a hidden state to the turtles and a pheromone field: The heatbugs model](#jheatbugs)

### <a name="jpassive"></a> A first example with a passive turtle

First we consider a simple example with a single passive agent. The example source code is located in the package `fr.lgi2a.similar2logo.examples.passive`. It contains 3 classes:

* `PassiveTurtleSimulationParameters`, that defines the parameters of the model. This class inherits from `LogoSimulationParameters`.

* `PassiveTurtleSimulationModel`, that defines the simulation model, i.e, the initial state of the simulation. This class inherits from `AbstractLogoSimulationModel`.

* `PassiveTurtleSimulationMain`, the main class of the simulation.

#### Model parameters

The class `LogoSimulationParameters` defines the generic parameters of a Logo-like simulation (environment size, topology, etc.).

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

#### The simulation model

The class [AbstractLogoSimulationModel](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/initializations/AbstractLogoSimulationModel.html) defines a generic simulation model of a Similar2Logo simulation. We must implement the `generateAgents` method to describe the initial state of our passive turtle. 

```
	protected AgentInitializationData generateAgents(
			ISimulationParameters parameters, Map<LevelIdentifier, ILevel> levels) {
		PassiveTurtleSimulationParameters castedParameters = (PassiveTurtleSimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		
		IAgent4Engine turtle = TurtleFactory.generate(
			new EmptyPerceptionModel(),
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

Since the turtle does not need to perceive anything, as a perception module, we use the empty perception model `EmptyPerceptionModel`.

#### The Main class

In the main class, the simulation model is created and the HTML runner is launched and configured.  Here, only the turtles are displayed.

Finally, the probe `LogoRealTimeMatcher` is added to the runner to slow down the simulation so that its execution speed matches a specific factor of N steps per second.

The `main` method contains the following code:

```
		// Creation of the runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		// Creation of the model
		AbstractLogoSimulationModel model = new PassiveTurtleSimulationModel( new PassiveTurtleSimulationParameters() );
		// Configuration of the runner
		runner.getConfig().setExportAgents( true );
		// Initialize the runner
		runner.initializeRunner( model );
		// Add other probes to the engine
		runner.addProbe("Real time matcher", new LogoRealTimeMatcher(20));
		// Open the GUI.
		runner.showView( );
```



### <a name="jboids"></a> Adding a decision module to the turtles: The boids model

The [boids](https://en.wikipedia.org/wiki/Boids) (bird-oid) model has been invented by [Graig Reynolds](https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)) in 1986 to simulate flocking behavior of birds. It is based on 3 principles:
    
* separation: boids tend to avoid other boids that are too close,

* alignment: boids tend to align their velocity to boids that are not too close and not too far away,

* cohesion: bois tend to move towards boids that are too far away.

While these rules are essentially heuristic, they can be implemented defining three areas for each principle. 

* Boids change their orientation to get away from other boids in the repulsion area,

* Boids change their orientation and speed to match those of other boids in the orientation area,

* Boids change their orientation to get to other boids in the attraction area.

An implementation of such model is located in the package `fr.lgi2a.similar2logo.examples.boids`.

The model itself is defined in the package `fr.lgi2a.similar2logo.examples.boids.model` which contains 2 classes:

* `BoidsSimulationParameters`, that defines the parameters of the model. This class inherits from `LogoSimulationParameters`,

* `BoidDecisionModel`, that defines the decision model of the boids. This class inherits from `AbstractAgtDecisionModel`.

The simulation model and main class are located in the main package.


#### Model parameters

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
	   name = "repulsion weight", 
	   description = "the repulsion weight"
	)
	public double repulsionWeight;
	

	@Parameter(
	   name = "orientation weight", 
	   description = "the orientation weight"
	)
	public double orientationWeight;

	@Parameter(
       name = "attraction weight", 
	   description = "the attraction weight"
	)
	public double attractionWeight;
	
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

#### The behavior of the boids 

The decision model consists in changing the direction and speed of the boids according to the previously described rules.
To define a decision model, the modeler must define a class that extends `AbstractAgtDecisionModel` and implement the `decide` method.


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
			int nbOfTurtlesInOrientationArea = 0;
			MeanAngle meanAngle = new MeanAngle();
			for (LocalPerceivedData<TurtlePLSInLogo> perceivedTurtle : castedPerceivedData.getTurtles()) {
				if (perceivedTurtle.getDistanceTo() <= this.parameters.repulsionDistance) {
					meanAngle.add(
						castedPublicLocalState.getDirection()- perceivedTurtle.getDirectionTo(),
						parameters.repulsionWeight
					);
				} else if (perceivedTurtle.getDistanceTo() <= this.parameters.orientationDistance) {
					meanAngle.add(
						perceivedTurtle.getContent().getDirection() - castedPublicLocalState.getDirection(),
						parameters.orientationWeight
					);
					orientationSpeed+=perceivedTurtle.getContent().getSpeed() - castedPublicLocalState.getSpeed();
					nbOfTurtlesInOrientationArea++;
				} else if (perceivedTurtle.getDistanceTo() <= this.parameters.attractionDistance){
					meanAngle.add(
						perceivedTurtle.getDirectionTo()- castedPublicLocalState.getDirection(),
						parameters.attractionWeight
					);
				}
			}
			double dd = meanAngle.value();
			if (!MathUtil.areEqual(dd, 0)) {
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

#### The simulation model

In the simulation model defined in our example, boids are initially randomly located in the environment with a random orientation and speed.

```
	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * @param p The parameters of the simulation model.
	 * @return a new boid located at the center of the grid.
	 */
	private static IAgent4Engine generateBoid(BoidsSimulationParameters p) {
		return TurtleFactory.generate(
			new ConeBasedPerceptionModel(
				p.attractionDistance,p.perceptionAngle,true,false,false
			),
			new BoidDecisionModel(p),
			new AgentCategory("b", TurtleAgentCategory.CATEGORY),
			PRNG.get().randomAngle(),
			p.minInitialSpeed + PRNG.get().randomDouble()*(
				p.maxInitialSpeed-p.minInitialSpeed
			),
			0,
			PRNG.get().randomDouble()*p.gridWidth,
			PRNG.get().randomDouble()*p.gridHeight
		);
	}
```


#### The main class

In the main class, such as in the previous example, the simulation model is created and the HTML runner is launched and configured. 
The `main` method contains the following code:

```
		// Creation of the runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		// Creation of the model
		AbstractLogoSimulationModel model = new BoidsSimulationModel( new BoidsSimulationParameters() );
		// Configuration of the runner
		runner.getConfig().setExportAgents( true );
		// Initialize the runner
		runner.initializeRunner( model );
		// Open the GUI.
		runner.showView( );
```

The main class is very similar to the previous example. Only the simulation model has been changed.


### <a name="jturmite"></a> Dealing with marks: the turmite model

The [turmite model](https://en.wikipedia.org/wiki/Langton's_ant), developed by [Christopher Langton](https://en.wikipedia.org/wiki/Christopher_Langton) in 1986, is a very simple mono-agent model that exhibits an emergent behavior. It is based on 2 rules:

* If the turmite is on a patch that does not contain a mark, it turns right, drops a mark, and moves forward,

* If the turmite is on a patch that contains a mark, it turns left, removes the mark, and moves forward.

The example source code is located in the package `fr.lgi2a.similar2logo.examples.turmite`. It contains 3 classes:

* `TurmiteDecisionModel` that defines the decision model of the turmites,

* `TurmiteSimulationModel` that defines the simulation model,

* `TurmiteSimulationMain`, the main class of the simulation.


#### The decision model

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

#### The simulation model

The simulation model generates a turmite heading north at the location 10.5,10.5 with a speed of 1 and an acceleration of 0:

```
	@Override
	protected AgentInitializationData generateAgents(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		AgentInitializationData result = new AgentInitializationData();	
		IAgent4Engine turtle = TurtleFactory.generate(
			new ConeBasedPerceptionModel(0, 2*Math.PI, false, true, false),
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

#### The main class

In the main class, such as in the previous example, the simulation model is created and the HTML runner is launched and configured. 
The `main` method contains the following code:

```
		//Launch the HTML runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		runner.getConfig().setExportAgents( true );
		runner.getConfig().setExportMarks( true );
		runner.initializeRunner( new TurmiteSimulationModel(parameters) );
		runner.addProbe("Real time matcher", new LogoRealTimeMatcher(20));
		runner.showView( );
```

The main difference with the previous example is that in this case we want to observe turtles and marks.

### <a name="jmultiturmite"></a> Adding an interaction and a user-defined reaction model: The multiturmite model

The goal of this example is to implement the multiturmite model proposed by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [this paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf). It extends the traditional [Langton's ant model](http://en.wikipedia.org/wiki/Langton%27s_ant) by specifying what happens when conflicting influences (removing or dropping a mark to the same location) are detected. The following policy is applied:

* if the parameter `dropMark` is `true`, the dropping influence takes precedent over the removing one and reciprocally.

* if the parameter `removeDirectionChange` is `true`, direction changes are not taken into account.

It allows to define 4 different reaction models according to these parameters.

This model is located in the `fr.lgi2a.similar2logo.examples.multiturmite` package and contains at least 5 classes:

* `MultiTurmiteSimulationParameters`, that contains the parameters of the model,

* `TurmiteInteraction`, that defines an interaction between multiple turmites,

* `MultiTurmiteReactionModel`, that extends `LogoDefaultReactionModel` and defines the reaction model, i.e., the way influences are handled,

* `MultiTurmiteSimulationModel` that defines the simulation model,

* Different main classes that define a specific initial configuration of the simulation, in our case, based on the ones described by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [their paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf).

#### Model parameters

The model parameters are defined in the class `MultiTurmiteSimulationParameters`. It defines how influences are handled according to the previously defined policy, the number of turmites and their initial locations.

```
	@Parameter(
	   name = "remove direction change", 
	   description = "if checked, direction changes are not taken into account when two turtles want to modify the same patch"
	)
	public boolean removeDirectionChange;
	
	@Parameter(
	   name = "inverse mark update", 
	   description = "if checked, the output of turtle actions is inversed when two turtles want to modify the same patch"
	)
	public boolean inverseMarkUpdate;
	
	@Parameter(
	   name = "number of turmites", 
	   description = "the  number of turmites in the environment"
	)
	public int nbOfTurmites;
	
	@Parameter(
	   name = "initial locations", 
	   description = "the  initial locations of turmites"
	)
	public List<Point2D> initialLocations;
	
	@Parameter(
	   name = "initial directions", 
	   description = "the initial directions of turmites"
	)
	public List<Double> initialDirections;
```

#### The reaction model

In the previous example, the influence management relies on the default reaction model defined in the class `LogoDefaultReactionModel`. Now, we want to handle some influences manually. To do so, we have to define a class `MultiTurmiteReactionModel` that inherits from `LogoDefaultReactionModel`. This class has one attribute: the parameters of the simulation.

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
#### The simulation model

The simulation model of this example is located in the class `MultiTurmiteSimulationModel`.

Such as in the previous example, we have to redefine the method `generateAgents` to specify the initial population of agents of the simulation:

```
@Override
	protected AgentInitializationData generateAgents(
			ISimulationParameters simulationParameters,
			Map<LevelIdentifier, ILevel> levels) {
		AgentInitializationData result = new AgentInitializationData();	
		MultiTurmiteSimulationParameters castedSimulationParameters = (MultiTurmiteSimulationParameters) simulationParameters;
		if(castedSimulationParameters.initialLocations.isEmpty()) {
			for(int i = 0; i < castedSimulationParameters.nbOfTurmites; i++) {
				IAgent4Engine turtle = TurtleFactory.generate(
					new ConeBasedPerceptionModel(0, 2*Math.PI, false, true, false),
					new TurmiteDecisionModel(),
					new AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
					MultiTurmiteSimulationModel.randomDirection(),
					1,
					0,
					Math.floor(PRNG.get().randomDouble()*castedSimulationParameters.gridWidth),
					Math.floor(PRNG.get().randomDouble()*castedSimulationParameters.gridHeight)
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
					new ConeBasedPerceptionModel(0, 2*Math.PI, false, true, false),
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


#### The Main class

The main class contains the following code:
```
		// Creation of the runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		
		// Definition of the parameters
		MultiTurmiteSimulationParameters parameters = new MultiTurmiteSimulationParameters();
		parameters.initialTime = new SimulationTimeStamp( 0 );
		parameters.finalTime = new SimulationTimeStamp( 100000 );
		parameters.xTorus = true;
		parameters.yTorus = true;
		parameters.gridHeight = 50;
		parameters.gridWidth = 50;
		parameters.nbOfTurmites = 2;
		parameters.inverseMarkUpdate = false;
		parameters.removeDirectionChange = false;
		//Create a specific instance
		parameters.initialLocations.add(new Point2D.Double(Math.floor(parameters.gridWidth/2),Math.floor(parameters.gridHeight/2)));
		parameters.initialDirections.add(LogoEnvPLS.NORTH);
		parameters.initialLocations.add(new Point2D.Double(Math.floor(parameters.gridWidth/2),Math.floor(parameters.gridHeight/2) +1));
		parameters.initialDirections.add(LogoEnvPLS.NORTH);
		
		// Creation of the model
		AbstractLogoSimulationModel model = new MultiTurmiteSimulationModel( parameters );
		// Configuration of the runner
		runner.getConfig().setExportAgents( true );
		runner.getConfig().setExportMarks( true );
		// Initialize the runner
		runner.initializeRunner( model );
		// Add other probes to the engine
		runner.addProbe("Real time matcher", new LogoRealTimeMatcher(20));
		// Open the GUI.
		runner.showView( );
```

In this case, we create a specific instance of the multiturmite model with 2 turmites. This configuration described by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [their paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf) produces interesting and distinctive emergent behaviors according to the values of `dropMark` and `removeDirectionChange` parameters.

Such as in the previous example, we want to observe the turtles and the marks.


### <a name="jsegregation"></a> Adding user-defined influence, reaction model and GUI: The segregation model


The segregation model has been proposed by [Thomas Schelling](https://en.wikipedia.org/wiki/Thomas_Schelling) in 1971 in his famous paper [Dynamic Models of Segregation](https://www.stat.berkeley.edu/~aldous/157/Papers/Schelling_Seg_Models.pdf). The goal of this model is to show that segregation can occur even if it is not wanted by the agents.

In our implementation of this model, turtles are located in the grid and at each step, compute an happiness index based on the similarity of other agents in their neighborhood. If this index is below a value, called here similarity rate, the turtle wants to move to an other location.

The segregation simulation source code is located in the package `fr.lgi2a.similar2logo.examples.segregation`. It contains

* a `model` package that describes the model. It is composed of 4 classes

    * `SegregationSimulationParameters` that extends `LogoSimulationParameters`, that contains the parameters of the simulation.
    
    * `Move` that extends `RegularInfluence`, representing a model-specific influence, emitted by an agent who wants to move to another location.
    
    * `SegregationAgentDecisionModel` that extends `AbstractAgtDecisionModel`, representing the decision model of our turtles.
    
    * `SegregationReactionModel` that extends `LogoDefaultReactionModel`, representing the model-specific reaction model. It defines how `Move` influences are handled.
    
* a class `SegregationSimulationModel` that extends `AbstractLogoSimulationModel`, representing the simulation model, i.e., the initial state of the simulation.

* the main class of the simulation `SegregationSimulationMain`.

* a HTML file `segregationgui.html`, that contains the GUI of the simulation.

#### Model parameters

The model parameters are defined in the class `SegregationSimulationParameters`. It contains the following parameters:

```
	@Parameter(
	   name = "similarity rate", 
	   description = "the rate of same-color turtles that each turtle wants among its neighbors"
	)
	public double similarityRate;
	
	@Parameter(
	   name = "vacancy rate", 
	   description = "the rate of vacant settling places"
	)
	public double vacancyRate;
	
	@Parameter(
	   name = "perception distance", 
	   description = "the perception distance of agents"
	)
	public double perceptionDistance;
```

The constructor defines the default values of the simulation parameters:

```
	public SegregationSimulationParameters() {
		super();
		this.similarityRate = 3.0/8;
		this.vacancyRate = 0.05;
		this.perceptionDistance = Math.sqrt(2);
		this.initialTime = new SimulationTimeStamp( 0 );
		this.finalTime = new SimulationTimeStamp( 500 );
		this.xTorus = true;
		this.yTorus = true;
		this.gridHeight = 50;
		this.gridWidth = 50;
	}
```

#### Model-specific influence

We define an influence called `Move` that is emitted by an agent who wants to move to another location. It is defined by a  unique identifier, here "move", and the state of the turtle that wants to move.

```
public class Move extends RegularInfluence {

	public static final String CATEGORY = "move";
	

	private final TurtlePLSInLogo target;
	
	public Move(SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound,
			TurtlePLSInLogo target) {
		super(CATEGORY, LogoSimulationLevelList.LOGO, timeLowerBound, timeUpperBound);
		this.target = target;
	}

	public TurtlePLSInLogo getTarget() {
		return target;
	}
```

#### Decision model

The decision model computes a happiness index based on the rate of turtles of different categories in its neighborhood. If the index is below the parameter `similarityRate`, the turtle emits a `Move` influence.

```
	@Override
	public void decide(SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		double similarityRate = 0;
		TurtlePLSInLogo castedPublicLocalState = (TurtlePLSInLogo) publicLocalState;
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		
		for(LocalPerceivedData<TurtlePLSInLogo> perceivedTurtle : castedPerceivedData.getTurtles()) {
			TurtlePLSInLogo castedPerceivedTurtle = (TurtlePLSInLogo) perceivedTurtle.getContent();
			if(castedPerceivedTurtle.getCategoryOfAgent().isA(castedPublicLocalState.getCategoryOfAgent())) {
				similarityRate++;
			}
		}
		if(castedPerceivedData.getTurtles().size() > 0 ) {
			similarityRate/= castedPerceivedData.getTurtles().size();
		}

		if(similarityRate < this.parameters.similarityRate) {
			producedInfluences.add(
					new Move(
						timeLowerBound,
						timeUpperBound,
						castedPublicLocalState
					)
				);
		}
```

#### Reaction model

The reaction model handles the `Move` influences emitted by unhappy turtles. First, it identifies vacant places and moves the turtles that have emitted a `Move` influence. Note that if there is not enough vacant places, not all turtle wishes can be fulfilled.

```
	@Override
	public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		LogoEnvPLS environment = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		List<IInfluence> specificInfluences = new ArrayList<IInfluence>();
		List<Point2D> vacantPlaces = new ArrayList<Point2D>();
		specificInfluences.addAll(regularInfluencesOftransitoryStateDynamics);
		Collections.shuffle(specificInfluences);
		//Identify vacant places
		LogoEnvPLS castedEnvState = (LogoEnvPLS) consistentState.getPublicLocalStateOfEnvironment();
		for(int x = 0; x < castedEnvState.getWidth(); x++) {
			for(int y = 0; y < castedEnvState.getHeight(); y++) {
				if(castedEnvState.getTurtlesAt(x, y).isEmpty()) {
					vacantPlaces.add(
						new Point2D.Double(x,y)
					);
				}
			}
		}
		Collections.shuffle(vacantPlaces);
		//move agents
		int i = 0;
		for(IInfluence influence : specificInfluences) {
			if(influence.getCategory().equals(Move.CATEGORY)) {
				Move castedInfluence = (Move) influence;
				environment.getTurtlesInPatches()[(int) Math.floor(castedInfluence.getTarget().getLocation().getX())][(int) Math.floor(castedInfluence.getTarget().getLocation().getY())].remove(castedInfluence.getTarget());
				environment.getTurtlesInPatches()[(int) Math.floor(vacantPlaces.get(i).getX())][(int) Math.floor(vacantPlaces.get(i).getY())].add(castedInfluence.getTarget());
				
				castedInfluence.getTarget().setLocation(
					vacantPlaces.get(i)
				);
				i++;
			}
			if(i >= vacantPlaces.size()) {
				break;
			}
		}
```


#### Simulation model

The simulation model generates the Logo level using the user-defined reaction model and a simple periodic time model.

```
    @Override
	protected List<ILevel> generateLevels(
			ISimulationParameters simulationParameters) {
		ExtendedLevel logo = new ExtendedLevel(
				simulationParameters.getInitialTime(), 
				LogoSimulationLevelList.LOGO, 
				new PeriodicTimeModel( 
					1, 
					0, 
					simulationParameters.getInitialTime()
				),
				new SegregationReactionModel()
			);
		List<ILevel> levelList = new LinkedList<ILevel>();
		levelList.add(logo);
		return levelList;
	}
```

It also generates turtles of 2 different types (a and b) randomly in the grid with respect to the vacancy rate parameter.

```
	@Override
	protected AgentInitializationData generateAgents(
		ISimulationParameters parameters, 
		Map<LevelIdentifier, ILevel> levels
	) {
		SegregationSimulationParameters castedParameters = (SegregationSimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		String t;
		for(int x = 0; x < castedParameters.gridWidth; x++) {
			for(int y = 0; y < castedParameters.gridHeight; y++) {
				if(PRNG.get().randomDouble() >= castedParameters.vacancyRate) {
					if(PRNG.get().randomBoolean()) {
						t = "a";
					} else {
						t = "b";
					}
					IAgent4Engine turtle = TurtleFactory.generate(
						new ConeBasedPerceptionModel(castedParameters.perceptionDistance, 2*Math.PI, true, false, false),
						new SegregationAgentDecisionModel(castedParameters),
						new AgentCategory(t, TurtleAgentCategory.CATEGORY),
						0,
						0,
						0,
						x,
						y
					);
					result.getAgents().add( turtle );
				}
			}
		}
		
		return result;
	}
```

#### <a name="segregationgui"></a> HTML GUI

The HTML GUI specifies how turtles are displayed in the grid.Turtles of type a are colored in blue and turtles of type b are colored in red.

```
<canvas id='grid_canvas' class='center-block' width='400' height='400'></canvas>
<script type='text/javascript'>
    drawCanvas = function (data) {
        var json = JSON.parse(data),
            canvas = document.getElementById('grid_canvas'),
            context = canvas.getContext('2d');
        context.clearRect(0, 0, canvas.width, canvas.height);
        for (var i = 0; i < json.agents.length; i++) {
            var centerX = json.agents[i].x * canvas.width;
            var centerY = json.agents[i].y * canvas.height;
            var radius = 2;
            if (json.agents[i].t == 'a') {
                context.fillStyle = 'red';
            } else {
                context.fillStyle = 'blue';
            }
            context.beginPath();
            context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
            context.fill();
        }
    }
</script>
```

#### Main class

The main method of the Main class simply launches and configures the HTML runner with the above described GUI.

```
		// Creation of the runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		// Creation of the model
		AbstractLogoSimulationModel model = new SegregationSimulationModel( new SegregationSimulationParameters() );
		// Configuration of the runner
		runner.getConfig().setCustomHtmlBody( SegregationSimulationMain.class.getResourceAsStream("segregationgui.html") );
		runner.getConfig().setExportAgents( true );
		// Initialize the runner
		runner.initializeRunner( model );
		// Open the GUI.
		runner.showView( );
```

### <a name="jheatbugs"></a> Adding a hidden state to the turtles and a pheromone field: The heatbugs model

"Heatbugs is an abstract model of the behavior of biologically-inspired agents that attempt to maintain an optimum temperature around themselves. It demonstrates how simple rules defining the behavior of agents can produce several different kinds of emergent behavior.

Heatbugs has been used as a demonstration model for many agent-based modeling toolkits." from [the Heatbugs page](http://ccl.northwestern.edu/netlogo/models/Heatbugs)  of the NetLogo documentation. 

This example illustrates how to add a hidden state to the turtles and a pheromone field to a similar2logo simulation and how it can be used by turtles.

The simulation is located in the package `fr.lgi2a.similar2logo.examples.heatbugs`.

The model itself is defined in the package `fr.lgi2a.similar2logo.examples.heatbugs.model` which contains

* the `HeatBugsSimulationParameters` class that extends `LogoSimulationParameters` and defines the parameters of the model. 

* the `agents` package that defines a heat bug turtle. It contains 4 classes:
    
    * `HeatBugCategory`, which defines the category of a heat bug turtle,
        
    * `HeatBugHLS` that extends `AbstractLocalStateOfAgent` and represents the hidden state of a heat bug,
        
    * `HeatBugDecisionModel` that extends `AbstractAgtDecisionModel`, which defines the decision model of a heat bug,
        
    * `HeatBugFactory`, the factory that creates a new heat bug.
        
* the `HeatBugsSimulationModel` class that extends `AbstractLogoSimulationModel` and defines the simulation model of the heatbugs simulation.
    
* the main class of the simulation `HeatBugsSimulationMain`

#### Model parameters

First, we define the parameters of the Heatbugs simulation in the class `HeatBugsSimulationParameters`. It contains the following parameters:

```
	@Parameter(
	   name = "number of bugs", 
	   description = "the number of bugs in the simulation"
	)
	public int nbOfBugs;
	
	@Parameter(
	   name = "evaporation rate", 
	   description = "the percentage of the world's heat that evaporates each cycle"
	)
	public double evaporationRate;
	
	@Parameter(
	   name = "diffusion rate", 
	   description = "How much heat a patch (a spot in the world) diffuses to its neighbors"
	)
	public double diffusionRate;
	
	@Parameter(
	   name = "min optimal temperature", 
	   description = "the minimum ideal temperatures for heatbugs"
	)
	public double minOptimalTemperature;
	
	@Parameter(
	   name = "max optimal temperature", 
	   description = "the maximum ideal temperatures for heatbugs"
	)
	public double maxOptimalTemperature;
	
	@Parameter(
	   name = "min output heat", 
	   description = "the minimum heat that heatbugs generate each cycle"
	)
	public double minOutputHeat;
	
	@Parameter(
	   name = "max output heat", 
	   description = "the maximum heat that heatbugs generate each cycle"
	)
	public double maxOutputHeat;
	
	@Parameter(
	   name = "random move probability", 
	   description = "the chance that a bug will make a random move even if it would prefer to stay where it is"
	)
	public double randomMoveProbability;
	
	@Parameter(
	   name = "unhappiness", 
	   description = "the relative difference between real and optimal temperature that triggers moves"
	)
	public double unhappiness;
```

The parameters `evaporationRate` and `diffusionRate` relate to a pheromone field which is instantiated in the constructor of `HeatBugsSimulationParameters` that also defines the default values of the parameters.

```
	public HeatBugsSimulationParameters() {
		super();
		this.nbOfBugs = 20;
		this.evaporationRate = 0.1;
		this.diffusionRate = 0.1;
		this.maxOptimalTemperature = 25;
		this.minOptimalTemperature = 10;
		this.maxOutputHeat = 3;
		this.minOutputHeat = 1;
		this.randomMoveProbability = 0.1;
		this.unhappiness = 0.1;
		this.finalTime = new SimulationTimeStamp( 30000 );
		this.gridWidth = 100;
		this.gridHeight = 100;
		this.xTorus = true;
		this.yTorus = true;
		this.pheromones.add(
			new Pheromone("heat", this.diffusionRate, this.evaporationRate)
		);
	}
```

#### Heat bug model

The model of a heat bug turtle is defined by several classes

##### Category of a heat bug

It defines the category, i.e., type of a heat bug turtle.

```
public class HeatBugCategory {
	
	public static final AgentCategory CATEGORY = new AgentCategory("heat bug", TurtleAgentCategory.CATEGORY);
	
	protected HeatBugCategory() {}	
}
```

##### Hidden state of a heat bug

The hidden state describes the state of the heat bug which is not visible by other heat bugs.

```
public class HeatBugHLS extends AbstractLocalStateOfAgent {

	private final double optimalTemperature;
	
	private final double outputHeat;
	
	private final double unhappiness;
	
	private final double randomMoveProbability;
	
	public HeatBugHLS(
		IAgent4Engine owner,
		double optimalTemperature,
		double outputHeat,
		double unhappiness,
		double randomMoveProbability
	) {
		super(
			LogoSimulationLevelList.LOGO,
			owner
		);
		this.optimalTemperature = optimalTemperature;
		this.outputHeat = outputHeat;
		this.unhappiness = unhappiness;
		this.randomMoveProbability = randomMoveProbability;
	}
    
    //Getter and setters
}
```

##### Decision model

The decision model of a heat bug defines how it moves according to the heat (defined as a pheromone field) it feels and how it raises the heat around it.

```
	@Override
	public void decide(SimulationTimeStamp timeLowerBound,
			SimulationTimeStamp timeUpperBound, IGlobalState globalState,
			ILocalStateOfAgent publicLocalState,
			ILocalStateOfAgent privateLocalState, IPerceivedData perceivedData,
			InfluencesMap producedInfluences) {
		TurtlePLSInLogo castedPLS = (TurtlePLSInLogo) publicLocalState;
		HeatBugHLS castedHLS = (HeatBugHLS) privateLocalState;
		
		TurtlePerceivedData castedPerceivedData = (TurtlePerceivedData) perceivedData;
		
		double bestValue;
		
		double bestDirection = castedPLS.getDirection();
		
		double diff = 0;
		
		for(LocalPerceivedData<Double> pheromone : castedPerceivedData.getPheromones().get("heat")) {
			if(MathUtil.areEqual(pheromone.getDistanceTo(), 0)) {
				diff = (pheromone.getContent() - castedHLS.getOptimalTemperature())/castedHLS.getOptimalTemperature();
				break;
			}
		}
		
		if(diff > castedHLS.getUnhappiness()) {
			//If the current patch is too hot
			bestValue = Double.MAX_VALUE;
			for(LocalPerceivedData<Double> pheromone : castedPerceivedData.getPheromones().get("heat")) {
				if(pheromone.getContent() < bestValue) {
					bestValue = pheromone.getContent();
					bestDirection = pheromone.getDirectionTo();
				}
			}
			producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					bestDirection - castedPLS.getDirection(),
					castedPLS
				)
			);
			if(MathUtil.areEqual(castedPLS.getSpeed(), 0)) {
				producedInfluences.add(
					new ChangeSpeed(
						timeLowerBound,
						timeUpperBound,
						1,
						castedPLS
					)
				);
			}
		} else if(diff < -castedHLS.getUnhappiness()) {
			//If the current patch is too cool
			bestValue = -Double.MAX_VALUE;
			for(LocalPerceivedData<Double> pheromone : castedPerceivedData.getPheromones().get("heat")) {
				if(pheromone.getContent() > bestValue) {
					bestValue = pheromone.getContent();
					bestDirection = pheromone.getDirectionTo();
				}
			}
			producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					bestDirection - castedPLS.getDirection(),
					castedPLS
				)
			);
			if(MathUtil.areEqual(castedPLS.getSpeed(), 0)) {
				producedInfluences.add(
					new ChangeSpeed(
						timeLowerBound,
						timeUpperBound,
						1,
						castedPLS
					)
				);
			}
		} else {
			// If the turtle is on the best patch
			if(castedHLS.getRandomMoveProbability() > PRNG.get().randomDouble()) {
				producedInfluences.add(
					new ChangeDirection(
						timeLowerBound,
						timeUpperBound,
						PRNG.get().randomDouble()*2*Math.PI,
						castedPLS
					)
				);
				if(MathUtil.areEqual(castedPLS.getSpeed(), 0)) {
					producedInfluences.add(
						new ChangeSpeed(
							timeLowerBound,
							timeUpperBound,
							1,
							castedPLS
						)
					);
				}
			} else if(!MathUtil.areEqual(castedPLS.getSpeed(), 0)) {
				producedInfluences.add(
					new Stop(
						timeLowerBound,
						timeUpperBound,
						castedPLS
					)
				);
			}
		}
		producedInfluences.add(
			new EmitPheromone(
				timeLowerBound,
				timeUpperBound,
				castedPLS.getLocation(),
				HeatBugsSimulationParameters.HEAT_FIELD_ID,
				castedHLS.getOutputHeat()
			)
		);
	}
```

##### Heat bug factory

Since heat bug turtles have a hidden state, we cannot use the default turtle factory. We have to define how a heat bug is generated.

```
public class HeatBugFactory {
    
    protected HeatBugFactory() {}
     
 	public static ExtendedAgent generate(
 			AbstractAgtPerceptionModel turtlePerceptionModel,
 			AbstractAgtDecisionModel turtleDecisionModel,
 			AgentCategory category,
 			double initialDirection,
 			double initialSpeed,
 			double initialAcceleration,
 			double initialX,
 			double initialY,
 			double optimalTemperature,
 			double outputHeat,
 			double unhappiness,
 			double randomMoveProbability
 	){
 		if( ! category.isA(HeatBugCategory.CATEGORY) ) {
 			throw new IllegalArgumentException( "Only turtle agents are accepted." );
 		}
 		ExtendedAgent turtle = new ExtendedAgent( category );
 		// Defines the revision model of the global state.
 		turtle.specifyGlobalStateRevisionModel(
 			new IdentityAgtGlobalStateRevisionModel( )
 		);
 		
 		//Defines the behavior of the turtle.
 		turtle.specifyBehaviorForLevel(
 			LogoSimulationLevelList.LOGO, 
 			turtlePerceptionModel, 
 			turtleDecisionModel
 		);
 		
 		// Define the initial global state of the turtle.
 		turtle.initializeGlobalState( new EmptyGlobalState( ) );
 		turtle.includeNewLevel(
			LogoSimulationLevelList.LOGO,
			new TurtlePLSInLogo( 
				turtle, 
				initialX,
				initialY, 
				initialSpeed,
				initialAcceleration,
				initialDirection
			),
			new HeatBugHLS(
				turtle,
				optimalTemperature,
				outputHeat,
				unhappiness,
				randomMoveProbability
			)
		);
 		
 		return turtle;
 	}
```

#### Simulation model

The simulation model generates heat bugs randomly in the environment.

```
	@Override
	protected AgentInitializationData generateAgents(
			ISimulationParameters parameters, Map<LevelIdentifier, ILevel> levels) {
		HeatBugsSimulationParameters castedParameters = (HeatBugsSimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		for(int i = 0; i < castedParameters.nbOfBugs; i++) {
			IAgent4Engine turtle = HeatBugFactory.generate(
				new ConeBasedPerceptionModel(1, 2*Math.PI, false, false, true),
				new HeatBugDecisionModel(),
				HeatBugCategory.CATEGORY,
				PRNG.get().randomDouble()*2*Math.PI,
				0,
	 			0,
	 			Math.floor(PRNG.get().randomDouble()*castedParameters.gridWidth) + 0.5,
	 			Math.floor(PRNG.get().randomDouble()*castedParameters.gridHeight) + 0.5,
				castedParameters.minOptimalTemperature +
				PRNG.get().randomDouble()*(
						castedParameters.maxOptimalTemperature	- castedParameters.minOptimalTemperature
				),
				castedParameters.minOutputHeat +
				PRNG.get().randomDouble()*(
						castedParameters.maxOutputHeat	- castedParameters.minOutputHeat
				),
	 			castedParameters.unhappiness,
	 			castedParameters.randomMoveProbability
				
			);
			result.getAgents().add( turtle );
		}
		
		return result;
	}
```

#### Main class

As usual, the main method of the Main class launches and configure the HTML runner. In this case, we want to display the turtles and the pheromone field.

```
		// Creation of the runner
		Similar2LogoHtmlRunner runner = new Similar2LogoHtmlRunner( );
		// Creation of the model
		AbstractLogoSimulationModel model = new HeatBugsSimulationModel( new HeatBugsSimulationParameters() );
		// Configuration of the runner
		runner.getConfig().setExportAgents( true );
		runner.getConfig().setExportPheromones( true );
		// Initialize the runner
		runner.initializeRunner( model );
		// Open the GUI.
		runner.showView( );
```


## <a name="gexamples"></a> Groovy Examples

In the following we comment the examples written in Groovy distributed with Similar2Logo. Each example introduces a specific feature.

* [A first example with a passive turtle](#gpassive)

* [Adding a user-defined decision model to the turtles: The boids model](#gboids)

* [Dealing with marks: the turmite model](#gturmite)

* [Adding user-defined influence, reaction model and GUI: The segregation model](#gsegregation)

### <a name="gpassive"></a> A first example with a passive turtle

First we consider a simple example with a single passive agent. The example source code is located in the package `fr.lgi2a.similar2logo.examples.passive`. It contains 1 groovy script.

Foremost, we define the parameters of the model by creating an object that inherits from `LogoSimulationParameters`, that contains the generic parameters of a Logo-like simulation (environment size, topology, etc.).

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

Then, we define the simulation model i.e, the initial state of the simulation from the `AbstractLogoSimulationModel` class. We must implement the `generateAgents` method to describe the initial state of our passive turtle.

```
def simulationModel = new AbstractLogoSimulationModel(simulationParameters) {
	protected AgentInitializationData generateAgents(
		ISimulationParameters p,
		Map<LevelIdentifier, ILevel> levels
	) {
		AgentInitializationData result = new AgentInitializationData()
		IAgent4Engine turtle = TurtleFactory.generate(
			new EmptyPerceptionModel(),
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

Then we launch and configure the HTML runner. Here, only the turtles are displayed. Finally, the probe `LogoRealTimeMatcher` is added to the server to slow down the simulation so that its execution speed matches a specific factor of N steps per second.

```
def runner = new Similar2LogoHtmlRunner( )
runner.config.exportAgents = true
runner.initializeRunner simulationModel
runner.showView( )
runner.addProbe "Real time matcher", new LogoRealTimeMatcher(20)
```

### <a name="gboids"></a> Adding a user-defined decision module to the turtles: The boids model

The [boids](https://en.wikipedia.org/wiki/Boids) (bird-oid) model has been invented by [Craig Reynolds](https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)) in 1986 to simulate the flocking behavior of birds. It is based on 3 principles:
    
* separation: boids tend to avoid other boids that are too close,

* alignment: boids tend to align their velocity to boids that are not too close and not too far away,

* cohesion: bois tend to move towards boids that are too far away.

While these rules are essentially heuristic, they can be implemented defining three areas (repulsion, orientation, attraction) for each principle. 

* Boids change their orientation to get away from other boids in the repulsion area,

* Boids change their orientation and speed to match those of other boids in the orientation area,

* Boids change their orientation to get to other boids in the attraction area.

An implementation of such model is located in the package `fr.lgi2a.similar2logo.examples.boids` which contains 1 groovy script called `GroovyBoidsSimulation`.

#### Model parameters

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

#### Decision model

The decision model consists in changing the direction and speed of the boids according to the previously described rules.
To define a decision model, the modeler must define an object that extends `AbstractAgtDecisionModel` and implement the `decide` method.


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
			def meanAngle = new MeanAngle(), sc=0, n = 0																		
			pd.turtles.each{ boid ->
				switch(boid.distanceTo) {
					case {it <= parameters.repulsionDistance}:											//the repulsion area
						meanAngle.add(pls.direction - boid.directionTo, parameters.repulsionWeight)
						break
					case {it > parameters.repulsionDistance && it <= parameters.orientationDistance}:		//the orientation area
						meanAngle.add(boid.content.direction - pls.direction,parameters.orientationWeight)
						sc+=boid.content.speed - pls.speed
						n++
						break
					case {it > parameters.orientationDistance && it <= parameters.attractionDistance}:		//the attraction area
						meanAngle.add(boid.directionTo- pls.direction, parameters.attractionWeight)
						break
				}
			}
			def oc = meanAngle.value()
			if (!MathUtil.areEqual(oc, 0)) {
				if(abs(oc) > parameters.maxAngle) oc = signum(oc)*parameters.maxAngle
				i.add new ChangeDirection(s, ns, oc, pls)
			}
			if (n > 0) i.add new ChangeSpeed(s, ns, sc/n, pls)
		}
	}
}
```

#### The simulation model

In the simulation model defined in our example, boids are initially located at the center of the environment with a random orientation and speed.


```
def simulationModel = new AbstractLogoSimulationModel(parameters) {
	protected AgentInitializationData generateAgents(
		ISimulationParameters p,
		Map<LevelIdentifier, ILevel> l
	) {
		def result = new AgentInitializationData()
		p.nbOfAgents.times {
			result.agents.add TurtleFactory.generate(
				new ConeBasedPerceptionModel(p.attractionDistance,p.perceptionAngle,true,false,false),
				decisionModel,
				new AgentCategory("b", TurtleAgentCategory.CATEGORY),
				PRNG.get().randomAngle(),
				p.minInitialSpeed + PRNG.get().randomDouble()*(p.maxInitialSpeed-p.minInitialSpeed),
				0,
				p.gridWidth/2,
				p.gridHeight/2
			)
		}
		return result
	}
}
```

#### Launch the HTML runner

Finally, we launch and configure the HTML runner as in the previous example.

```
def runner = new Similar2LogoHtmlRunner( )
runner.config.exportAgents = true
runner.initializeRunner simulationModel
runner.showView( )
```

### <a name="gturmite"></a> Dealing with marks: the turmite model

The [turmite model](https://en.wikipedia.org/wiki/Langton's_ant), developed by [Christopher Langton](https://en.wikipedia.org/wiki/Christopher_Langton) in 1986, is a very simple mono-agent model that exhibits an emergent behavior. It is based on 2 rules:

* If the turmite is on a patch that does not contain a mark, it turns right, drops a mark, and moves forward,

* If the turmite is on a patch that contains a mark, it turns left, removes the mark, and moves forward.

The example source code is located in the package `fr.lgi2a.similar2logo.examples.turmite`. It contains 1 Groovy script called `GroovyTurmiteSimulation`.

#### Model parameters

First we define the simulation parameters. Here we only need to specify the final step of the simulation:

```
def parameters = new LogoSimulationParameters(												
	finalTime: new SimulationTimeStamp(100000)
)
```
#### The decision model

The decision model implements the above described rules :

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
		if(pd.marks.empty) i.with {
			add new ChangeDirection(s, ns, PI/2, pls)
			add new DropMark(s, ns, new Mark((Point2D) pls.location.clone(), null))
		} else i.with {
			add new ChangeDirection(s, ns, -PI/2, pls)
			add new RemoveMark(s, ns,pd.marks.iterator().next().content)
		}
	}
}
```

#### The simulation model

The simulation model generates a turmite heading north at the location 10.5,10.5 with a speed of 1 and an acceleration of 0:

```
def simulationModel = new AbstractLogoSimulationModel(parameters) {
	protected AgentInitializationData generateAgents(
		ISimulationParameters simulationParameters,
		Map<LevelIdentifier, ILevel> levels
	) {
		def turmite = TurtleFactory.generate(									
			new ConeBasedPerceptionModel(0, 2*Math.PI, false, true, false),
			decisionModel,
			new AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
			LogoEnvPLS.NORTH,
			1,
			0,
			10.5, 10.5
		),
			result = new AgentInitializationData()
		result.agents.add turmite
		return result
	}
}
```


#### Launch the HTML runner

```
def runner = new Similar2LogoHtmlRunner( )
runner.config.exportAgents = true
runner.config.exportMarks = true
runner.initializeRunner simulationModel
runner.addProbe "Real time matcher", new LogoRealTimeMatcher(20)
runner.showView( )	
```

The main difference with the previous example is that in this case we want to observe turtles and marks.

### <a name="gsegregation"></a> Adding user-defined influence, reaction model and GUI: The segregation model

The segregation model has been proposed by [Thomas Schelling](https://en.wikipedia.org/wiki/Thomas_Schelling) in 1971 in his famous paper [Dynamic Models of Segregation](https://www.stat.berkeley.edu/~aldous/157/Papers/Schelling_Seg_Models.pdf). The goal of this model is to show that segregation can occur even if it is not wanted by the agents.

In our implementation of this model, turtles are located in the grid and at each step, compute an happiness index based on the similarity of other agents in their neighborhood. If this index is below a value, called here similarity rate, the turtle wants to move to an other location.

#### Model parameters

We define the following parameters and their default values.

```
def parameters = new LogoSimulationParameters() {

	@Parameter(
		name = "similarity rate",
		description = "the rate of same-color turtles that each turtle wants among its neighbors"
	 )
	 public double similarityRate = 3.0/8
	 
	 @Parameter(name = "vacancy rate", description = "the rate of vacant settling places")
	 public double vacancyRate = 0.05
	 
	 @Parameter(name = "perception distance", description = "the perception distance of agents")
	 public double perceptionDistance = sqrt(2)
}
```
#### Model-specific influence

We define an influence called `Move` that is emitted by an agent who wants to move to another location. It is defined by a  unique identifier, here "move", and the state of the turtle that wants to move.

```
class Move extends RegularInfluence {																	
	def target																							
	static final def CATEGORY = "move"
    
	Move(SimulationTimeStamp s, SimulationTimeStamp ns, TurtlePLSInLogo target) {
		super(CATEGORY, LOGO, s, ns)
		this.target = target																		
	}
}
```

#### Decision model

The decision model computes a happiness index based on the rate of turtles of different categories in its neighborhood. If the index is below the parameter `similarityRate`, the turtle emits a `Move` influence.

```
def decisionModel = new AbstractAgtDecisionModel(LOGO) {												
	void decide(
		SimulationTimeStamp s,
		SimulationTimeStamp ns,
		IGlobalState gs,
		ILocalStateOfAgent pls,
		ILocalStateOfAgent prls,
		IPerceivedData pd,
		InfluencesMap i	
	) {
		def sr = 0
		pd.turtles.each{ agent -> if(agent.content.categoryOfAgent.isA(pls.categoryOfAgent)) sr++ }
		if(!pd.turtles.empty) sr/= pd.turtles.size()
		if(sr < parameters.similarityRate) i.add new Move(s, ns, pls)
	}
}
```

#### Reaction model

The reaction model handles the `Move` influences emitted by unhappy turtles. First, it identifies vacant places and moves the turtles that have emitted a `Move` influence. Note that if there is not enough vacant places, not all turtle wishes can be fulfilled.

```
def reactionModel = new LogoDefaultReactionModel() {
    //redefine the reaction function for regular influences
    public void makeRegularReaction(
        SimulationTimeStamp s,
        SimulationTimeStamp ns,
        ConsistentPublicLocalDynamicState cs,
        Set<IInfluence> influences, 
        InfluencesMap remainingInfluences
    ) {
        def e = cs.publicLocalStateOfEnvironment,
            li = [], //the list of influences
            vacant = [] //the list of vacant housings	
        li.addAll influences //create the list of influences
        PRNG.get().shuffle li //shuffle the list of influences
        for(x in 0..<e.width) for(y in 0..<e.height)
            if(e.getTurtlesAt(x, y).empty) vacant.add new Point2D.Double(x,y)
        PRNG.get().shuffle vacant
        def n = 0 
        li.any{ i ->
            if(i.category == Move.CATEGORY) {
                e.turtlesInPatches[(int) i.target.location.x][(int) i.target.location.y].remove i.target
                e.turtlesInPatches[(int) vacant[n].x][(int) vacant[n].y].add i.target
                i.target.setLocation(vacant[n])
                if(++n >= vacant.size()) return true
            }
        }
    }
}
```

#### Simulation model

The simulation model generates the Logo level using the user-defined reaction model and a simple periodic time model. It also generates turtles of 2 different types (a and b) randomly in the grid with respect to the vacancy rate parameter.

```
def simulationModel = new AbstractLogoSimulationModel(parameters) {
    
    List<ILevel> generateLevels(ISimulationParameters p) {
        def logo = new ExtendedLevel(
            p.initialTime,
            LOGO,
            new PeriodicTimeModel(1,0, p.initialTime),
            reactionModel
        )
        def levelList = []
        levelList.add logo
        return levelList
    }

    AgentInitializationData generateAgents(ISimulationParameters p, Map<LevelIdentifier, ILevel> l) {
        def result = new AgentInitializationData()
        for(x in 0..<p.gridWidth) for(y in 0..<p.gridHeight)
            if(PRNG.get().randomDouble() >= p.vacancyRate) result.agents.add TurtleFactory.generate(
                new ConeBasedPerceptionModel(p.perceptionDistance, 2*PI, true, false, false),
                decisionModel,
                new AgentCategory(PRNG.get().randomBoolean() ? "a" :"b", TurtleAgentCategory.CATEGORY),
                0, 0, 0, x,y
            )
        return result
    }
}
```

#### HTML GUI

The GUI is defined in a variable called `segregationgui`.

```
def segregationgui = '''
    <canvas id='grid_canvas' class='center-block' width='400' height='400'></canvas>
    <script type='text/javascript'>
        drawCanvas = function (data) {
            var json = JSON.parse(data),
                canvas = document.getElementById('grid_canvas'),
                context = canvas.getContext('2d');
            context.clearRect(0, 0, canvas.width, canvas.height);
            for (var i = 0; i < json.agents.length; i++) {
                var centerX = json.agents[i].x * canvas.width;
                var centerY = json.agents[i].y * canvas.height;
                var radius = 2;
                if (json.agents[i].t == 'a') {
                    context.fillStyle = 'red';
                } else {
                    context.fillStyle = 'blue';
                }
                context.beginPath();
                context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
                context.fill();
            }
        }
    </script>'''
```

#### Launch the HTML runner

Finally, we launche the web server with the above described GUI.

```
def runner = new Similar2LogoHtmlRunner( )
runner.config.exportAgents = true
runner.config.setCustomHtmlBodyFromString segregationgui
runner.initializeRunner simulationModel
runner.showView( )	
```


## <a name="rexamples"></a> Ruby Examples

In the following we comment the examples written in Ruby distributed with Similar2Logo. Each example introduces a specific feature.

* [A first example with a passive turtle](#rpassive)

* [Adding a user-defined decision model to the turtles: The boids model](#rboids)

* [Dealing with marks: the turmite model](#rturmite)

* [Adding user-defined influence, reaction model and GUI: The segregation model](#rsegregation)

Note that to load needed Java libraries, you must specify where they are located in your Ruby scripts or classes according to the location of your Similar2Logo installation. E.g.,

```
require 'java'
Dir["/Users/morvan/Logiciels/similar2logo/similar2logo-distribution/target/similar2logo-distribution-0.9-SNAPSHOT-bin/lib/*.jar"].each { |jar| require jar }
```

To import needed Java classes, you must use the `java_import` statement. E.g., to import `AbstractLogoSimulationModel`, add the following line to your script or class

```
java_import 'fr.lgi2a.similar2logo.kernel.initializations.AbstractLogoSimulationModel'
```

### <a name="rpassive"></a> A first example with a passive turtle

First we consider a simple example with a single passive agent. The example source code is located in the package `fr.lgi2a.similar2logo.examples.passive`. It contains 1 Ruby script.

Foremost, we define the parameter class of the model by creating an object that inherits from `LogoSimulationParameters`, that contains the generic parameters of a Logo-like simulation (environment size, topology, etc.).

```
class PassiveSimulationParameters < LogoSimulationParameters
  
  def initialX; 10 end
  
  def initialY; 10 end
  
  def initialSpeed; 1 end
  
  def initialAcceleration; 0 end
  
  def initialDirection; LogoEnvPLS::NORTH end
  
end
```

Contrary to Java and Groovy simulations, it is not (yet) possible to change the value of the parameters in the GUI.

Then, we define the simulation model model, i.e, the initial state of the simulation from the `AbstractLogoSimulationModel` class. We must implement the `generateAgents` method to describe the initial state of our passive turtle.

```
class PassiveSimulationModel < AbstractLogoSimulationModel
  def generateAgents(p, levels)
    result =  AgentInitializationData.new
    turtle = TurtleFactory::generate(
      EmptyPerceptionModel.new,
      PassiveTurtleDecisionModel.new,
      AgentCategory.new("passive", TurtleAgentCategory::CATEGORY),
      p.initialDirection,
      p.initialSpeed,
      p.initialAcceleration,
      p.initialX,
      p.initialY
    )
    result.agents.add(turtle)
    return result
  end
end
```

We can now instanciate the parameters and the simulation model.

```
simulationParameters = PassiveSimulationParameters.new
simulationModel = PassiveSimulationModel.new(simulationParameters)
```
Then we launch and configure the HTML runner. Here, only the turtles are displayed. Finally, the probe `LogoRealTimeMatcher` is added to the server to slow down the simulation so that its execution speed matches a specific factor of N steps per second.

```
runner = Similar2LogoHtmlRunner.new
runner.config.setExportAgents(true)
runner.initializeRunner(simulationModel)
runner.showView
runner.addProbe("Real time matcher", LogoRealTimeMatcher.new(20))
```

### <a name="rboids"></a> Adding a user-defined decision module to the turtles: The boids model

The [boids](https://en.wikipedia.org/wiki/Boids) (bird-oid) model has been invented by [Craig Reynolds](https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)) in 1986 to simulate the flocking behavior of birds. It is based on 3 principles:
    
* separation: boids tend to avoid other boids that are too close,

* alignment: boids tend to align their velocity to boids that are not too close and not too far away,

* cohesion: bois tend to move towards boids that are too far away.

While these rules are essentially heuristic, they can be implemented defining three areas (repulsion, orientation, attraction) for each principle. 

* Boids change their orientation to get away from other boids in the repulsion area,

* Boids change their orientation and speed to match those of other boids in the orientation area,

* Boids change their orientation to get to other boids in the attraction area.

An implementation of such model is located in the package `fr.lgi2a.similar2logo.examples.boids` which contains 1 Ruby script called `RubyBoidsSimulation`.

#### Model parameters

The model parameters and their default values are defined as in the previous example.

```
class BoidsSimulationParameters < LogoSimulationParameters
  
  attr_accessor :repulsionDistance, :attractionDistance, :orientationDistance, :repulsionWeight, :orientationWeight, :attractionWeight, :maxInitialSpeed, :minInitialSpeed, :perceptionAngle, :nbOfAgents, :maxAngle
  
  def initialize
    
    @repulsionDistance = 6
  
    @attractionDistance = 14
  
    @orientationDistance  = 10
    
    @repulsionWeight = 1
    
    @orientationWeight = 1
    
    @attractionWeight = 1
  
    @maxInitialSpeed = 2
  
    @minInitialSpeed = 1
  
    @perceptionAngle = Math::PI
  
    @nbOfAgents = 200
 
    @maxAngle = Math::PI/8
  end
  
end
```

#### Decision model

The decision model consists in changing the direction and speed of the boids according to the previously described rules.
To define a decision model, the modeler must define an object that extends `AbstractAgtDecisionModel` and implement the `decide` method.


```
class BoidDecisionModel < AbstractAgtDecisionModel
  
  def initialize(parameters)
    super(LogoSimulationLevelList::LOGO)
    @parameters = parameters
  end
  
  def decide(
    timeLowerBound,
    timeUpperBound,
    globalState,
    publicLocalState,
    privateLocalState,
    perceivedData,
    producedInfluences
  )
    if !perceivedData.getTurtles.empty?
      meanAngle = MeanAngle.new
      orientationSpeed = 0
      nbOfTurtlesInOrientationArea = 0
      perceivedData.getTurtles.each do |perceivedTurtle|
        if perceivedTurtle  != publicLocalState
          if perceivedTurtle.getDistanceTo <= @parameters.repulsionDistance
            meanAngle.add(publicLocalState.getDirection - perceivedTurtle.getDirectionTo, @parameters.repulsionWeight)
          elsif perceivedTurtle.getDistanceTo <= @parameters.orientationDistance
            meanAngle.add(perceivedTurtle.getContent.getDirection - publicLocalState.getDirection, @parameters.orientationWeight)
            orientationSpeed+=perceivedTurtle.getContent.getSpeed - publicLocalState.getSpeed
            nbOfTurtlesInOrientationArea+=1
          elsif perceivedTurtle.getDistanceTo <= @parameters.attractionDistance
            meanAngle.add(perceivedTurtle.getDirectionTo- publicLocalState.getDirection, @parameters.attractionWeight)
          end
        end
      end
      dd = meanAngle.value
      if !MathUtil::areEqual(dd, 0)
        if dd > @parameters.maxAngle
          dd = @parameters.maxAngle
        elsif dd<-@parameters.maxAngle
          dd = -@parameters.maxAngle
        end
        producedInfluences.add(
          ChangeDirection.new(
           timeLowerBound,
           timeUpperBound,
           dd,
           publicLocalState
         )
       )
     end
     if nbOfTurtlesInOrientationArea > 0
        orientationSpeed /= nbOfTurtlesInOrientationArea
        producedInfluences.add(
          ChangeSpeed.new(
            timeLowerBound,
            timeUpperBound,
            orientationSpeed,
            publicLocalState
          )
        )
     end
    end
  end
end
```

#### The simulation model

In the simulation model defined in our example, boids are initially located at the center of the environment with a random orientation and speed.


```
class BoidsSimulationModel < AbstractLogoSimulationModel
  def generateAgents(p, levels)
     result =  AgentInitializationData.new
     p.nbOfAgents.times do
      result.getAgents.add(
        TurtleFactory::generate(
         ConeBasedPerceptionModel.new(p.attractionDistance,p.perceptionAngle,true,false,false),
         BoidDecisionModel.new(p),
         AgentCategory.new("b", TurtleAgentCategory::CATEGORY),
         Math::PI-PRNG::get.randomDouble*2*Math::PI,
         p.minInitialSpeed + PRNG::get.randomDouble*(
           p.maxInitialSpeed-p.minInitialSpeed
         ),
         0,
         p.gridWidth/2,
         p.gridHeight/2
       )
      )
    end
    return result
  end
end
```

#### Launch the HTML runner

Finally, we launch and configure the HTML runner as in the previous example.

```
runner = Similar2LogoHtmlRunner.new
runner.config.setExportAgents(true)
runner.initializeRunner(BoidsSimulationModel.new(BoidsSimulationParameters.new))
runner.showView
```

### <a name="rturmite"></a> Dealing with marks: the turmite model

The [turmite model](https://en.wikipedia.org/wiki/Langton's_ant), developed by [Christopher Langton](https://en.wikipedia.org/wiki/Christopher_Langton) in 1986, is a very simple mono-agent model that exhibits an emergent behavior. It is based on 2 rules:

* If the turmite is on a patch that does not contain a mark, it turns right, drops a mark, and moves forward,

* If the turmite is on a patch that contains a mark, it turns left, removes the mark, and moves forward.

The example source code is located in the package `fr.lgi2a.similar2logo.examples.turmite`. It contains 1 Ruby script called `RubyTurmiteSimulation`.

#### The decision model

The decision model implements the above described rules :

```
class TurmiteDecisionModel < AbstractAgtDecisionModel
  
  def initialize
    super(LogoSimulationLevelList::LOGO)
  end
  
  def decide(
      timeLowerBound,
      timeUpperBound,
      globalState,
      publicLocalState,
      privateLocalState,
      perceivedData,
      producedInfluences
    )
    if perceivedData.getMarks.empty?
      producedInfluences.add(
        ChangeDirection.new(
          timeLowerBound,
          timeUpperBound,
          Math::PI/2,
          publicLocalState
        )
      )
      producedInfluences.add(
        DropMark.new(
          timeLowerBound,
          timeUpperBound,
          Mark.new(
            publicLocalState.getLocation.clone,
            nil
          )
        )
      )
    else
      producedInfluences.add(
        ChangeDirection.new(
          timeLowerBound,
          timeUpperBound,
          -Math::PI/2,
          publicLocalState
        )
      )
      producedInfluences.add(
        RemoveMark.new(
          timeLowerBound,
          timeUpperBound,
          perceivedData.getMarks.iterator.next.getContent
        )
      )
    end
  end
end
```

#### The simulation model

The simulation model generates a turmite heading north at the location 10.5,10.5 with a speed of 1 and an acceleration of 0:

```
class TurmiteSimulationModel < AbstractLogoSimulationModel
  def generateAgents(p, levels)
      result =  AgentInitializationData.new
      turtle = TurtleFactory::generate(
        ConeBasedPerceptionModel.new(0, 2*Math::PI, false, true, false),
        TurmiteDecisionModel.new,
        AgentCategory.new("turmite", TurtleAgentCategory::CATEGORY),
        LogoEnvPLS::NORTH,
        1,
        0,
        10.5,
        10.5
      )
      result.agents.add(turtle)
      return result
    end
end
```


#### Launch the HTML runner

```
runner = Similar2LogoHtmlRunner.new
runner.config.setExportAgents(true)
runner.config.setExportMarks( true )
runner.initializeRunner(TurmiteSimulationModel.new(LogoSimulationParameters.new))
runner.showView
runner.addProbe("Real time matcher", LogoRealTimeMatcher.new(20))
```

The main difference with the previous example is that in this case we want to observe turtles and marks.

### <a name="rsegregation"></a> Adding user-defined influence, reaction model and GUI: The segregation model

The segregation model has been proposed by [Thomas Schelling](https://en.wikipedia.org/wiki/Thomas_Schelling) in 1971 in his famous paper [Dynamic Models of Segregation](https://www.stat.berkeley.edu/~aldous/157/Papers/Schelling_Seg_Models.pdf). The goal of this model is to show that segregation can occur even if it is not wanted by the agents.

In our implementation of this model, turtles are located in the grid and at each step, compute an happiness index based on the similarity of other agents in their neighborhood. If this index is below a value, called here similarity rate, the turtle wants to move to an other location.

#### Model parameters

We define the following parameters and their default values.

```
class SegregationSimulationParameters < LogoSimulationParameters
  
  attr_accessor :similarityRate, :vacancyRate, :perceptionDistance
  def initialize
    
    @similarityRate = 3.0/8
  
    @vacancyRate = 0.05
  
    @perceptionDistance = Math::sqrt(2)
  end
  
end
```

#### Model-specific influence

We define an influence called `Move` that is emitted by an agent who wants to move to another location. It is defined by a  unique identifier, here "move", and the state of the turtle that wants to move.

```
class Move < RegularInfluence
  
  attr_accessor :target
  def initialize( s, ns, target)
    super("move", LogoSimulationLevelList::LOGO, s, ns)
    @target = target
  end
  
end
```

#### Decision model

The decision model computes a happiness index based on the rate of turtles of different categories in its neighborhood. If the index is below the parameter `similarityRate`, the turtle emits a `Move` influence.

```
class SegregationAgentDecisionModel < AbstractAgtDecisionModel
  
  def initialize(parameters)
    super(LogoSimulationLevelList::LOGO)
    @parameters = parameters
  end
  
  def decide(
    timeLowerBound,
    timeUpperBound,
    globalState,
    publicLocalState,
    privateLocalState,
    perceivedData,
    producedInfluences
  )
    sr = 0.0
    perceivedData.turtles.each do |perceivedTurtle|
      if perceivedTurtle.content.categoryOfAgent.isA(publicLocalState.categoryOfAgent)
        sr+=1.0
      end
    end
    if !perceivedData.turtles.empty
      sr/= perceivedData.turtles.size
    end
    if sr < @parameters.similarityRate
      producedInfluences.add(Move.new(timeLowerBound, timeUpperBound, publicLocalState))
    end
  end
end
```

#### Reaction model

The reaction model handles the `Move` influences emitted by unhappy turtles. First, it identifies vacant places and moves the turtles that have emitted a `Move` influence. Note that if there is not enough vacant places, not all turtle wishes can be fulfilled.

```
class SegregationReactionModel < LogoDefaultReactionModel
  def makeRegularReaction(
      transitoryTimeMin,
      transitoryTimeMax,
      consistentState,
      regularInfluencesOftransitoryStateDynamics,
      remainingInfluences
    )
    if regularInfluencesOftransitoryStateDynamics.size > 2
      specificInfluences = ArrayList.new
      vacantPlaces = ArrayList.new
      specificInfluences.addAll(regularInfluencesOftransitoryStateDynamics)
      PRNG::get.shuffle(specificInfluences)
      for x in 0..consistentState.publicLocalStateOfEnvironment.width()-1
        for y in 0..consistentState.publicLocalStateOfEnvironment.height()-1
          if consistentState.publicLocalStateOfEnvironment.getTurtlesAt(x, y).empty?
            vacantPlaces.add(Point2D::Double.new(x,y))
          end
        end
      end
      PRNG::get.shuffle(vacantPlaces)
      i = 0
      specificInfluences.each do |influence|
        if influence.getCategory() == "move"
          consistentState.publicLocalStateOfEnvironment.turtlesInPatches[influence.target.location.x.floor][influence.target.location.y.floor].clear()
          consistentState.publicLocalStateOfEnvironment.turtlesInPatches[vacantPlaces[i].x.floor][vacantPlaces[i].y.floor].add(influence.target)
          influence.target.setLocation(vacantPlaces[i])
          i+=1      
        end
        if i >= vacantPlaces.size
          break
        end
      end
    end
  end 
end
```


#### Simulation model

The simulation model generates the Logo level using the user-defined reaction model and a simple periodic time model. It also generates turtles of 2 different types (a and b) randomly in the grid with respect to the vacancy rate parameter.

```
class SegregationSimulationModel < AbstractLogoSimulationModel
  def generateLevels(p)
    logo = ExtendedLevel.new(
      p.initialTime,
      LogoSimulationLevelList::LOGO,
      PeriodicTimeModel.new(1,0, p.initialTime),
      SegregationReactionModel.new
    )
    levelList = LinkedList.new
    levelList.add(logo)
    return levelList
  end
  def generateAgents(p, levels)
     result =  AgentInitializationData.new
     t = ""
    for x in 0...p.gridWidth-1
      for y in 0..p.gridHeight-1
        if PRNG::get.randomDouble >= p.vacancyRate
          if PRNG::get.randomBoolean
            t = "a"
          else
            t = "b"
          end
          turtle = TurtleFactory.generate(
            ConeBasedPerceptionModel.new(p.perceptionDistance, 2*Math::PI, true, false, false),
            SegregationAgentDecisionModel.new(p),
            AgentCategory.new(t, TurtleAgentCategory::CATEGORY),
            0,
            0,
            0,
            x,
            y
          )
          result.agents.add( turtle )
        end  
      end
    end
    return result
  end
end
```
#### HTML GUI

The GUI is defined in a variable called `segregationgui`.

```
segregationgui = %{
    <canvas id='grid_canvas' class='center-block' width='400' height='400'></canvas>
    <script type='text/javascript'>
        drawCanvas = function (data) {
            var json = JSON.parse(data),
                canvas = document.getElementById('grid_canvas'),
                context = canvas.getContext('2d');
            context.clearRect(0, 0, canvas.width, canvas.height);
            for (var i = 0; i < json.agents.length; i++) {
                var centerX = json.agents[i].x * canvas.width;
                var centerY = json.agents[i].y * canvas.height;
                var radius = 2;
                if (json.agents[i].t == 'a') {
                    context.fillStyle = 'red';
                } else {
                    context.fillStyle = 'blue';
                }
                context.beginPath();
                context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
                context.fill();
            }
        }
</script>}
```
#### Launch the HTML runner

Finally, we launche the web server with the above described GUI.

```
runner = Similar2LogoHtmlRunner.new
runner.config.setCustomHtmlBodyFromString(segregationgui)
runner.config.setExportAgents(true)
runner.initializeRunner(SegregationSimulationModel.new(SegregationSimulationParameters.new))
runner.showView
```

## <a name="kexamples"></a> Kotlin Examples

In the following we comment the examples written in Kotlin distributed with Similar2Logo. Each example introduces a specific feature.

* [A first example with a passive turtle](#kpassive)

* [Adding a user-defined decision model to the turtles: The boids model](#kboids)

* [Dealing with marks: the turmite model](#kturmite)

* [Adding an interaction and a user-defined reaction model: The multiturmite model](#kmultiturmite)

* [Adding user-defined influence, reaction model and GUI: The segregation model](#ksegregation)

### <a name="kpassive"></a> A first example with a passive turtle

First we consider a simple example with a single passive agent. The example source code is located in the package `fr.lgi2a.similar2logo.examples.passive`. It contains 3 classes:

* `PassiveTurtleSimulationParameters`, that defines the parameters of the model. This class inherits from `LogoSimulationParameters`.

* `PassiveTurtleSimulationModel`, that defines the simulation model, i.e, the initial state of the simulation. This class inherits from `AbstractLogoSimulationModel`.

* `PassiveTurtleSimulationMain`, the main class of the simulation.

#### Model parameters

The class `LogoSimulationParameters` defines the generic parameters of a Logo-like simulation (environment size, topology, etc.).

The class  `PassiveTurtleSimulationParameters` contains the parameters specific to this model.

```
class PassiveTurtleSimulationParameters : LogoSimulationParameters() {
	
	@Parameter(
	   name = "initial x", 
	   description = "the initial position of the turtle on the x axis"
	)
	var  initialX = 10.0;
	
	@Parameter(
	   name = "initial y", 
	   description = "the initial position of the turtle on the y axis"
	)
	var initialY = 10.0
	
	@Parameter(
	   name = "initial speed", 
	   description = "the initial speed of the turtle"
	)
	var initialSpeed = 0.1
	
	@Parameter(
	   name = "initial acceleration", 
	   description = "the initial acceleration of the turtle"
	)
	var initialAcceleration = 0.0
	
	@Parameter(
	   name = "initial direction", 
	   description = "the initial direction of the turtle"
	)
	var initialDirection = LogoEnvPLS.NORTH
}
```

#### The simulation model

The class [AbstractLogoSimulationModel](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/initializations/AbstractLogoSimulationModel.html) defines a generic simulation model of a Similar2Logo simulation. We must implement the `generateAgents` function to describe the initial state of our passive turtle. 

```
class PassiveTurtleSimulationModel(parameters : LogoSimulationParameters)  : AbstractLogoSimulationModel(parameters) {
	
	 override fun generateAgents(
	     parameters : ISimulationParameters,
		 levels : Map<LevelIdentifier, ILevel> 
	 ) : AgentInitializationData { 
         var castedParameters : PassiveTurtleSimulationParameters = parameters as PassiveTurtleSimulationParameters
		 var result = AgentInitializationData()
		 var turtle = TurtleFactory.generate(
			EmptyPerceptionModel(),
			PassiveTurtleDecisionModel(),
			AgentCategory("passive", TurtleAgentCategory.CATEGORY),
			castedParameters.initialDirection,
			castedParameters.initialSpeed,
			castedParameters.initialAcceleration,
			castedParameters.initialX,
			castedParameters.initialY
		)
		result.getAgents().add(turtle)
        return result
    }
		
}
```

Note that it is not necessary to define any class related to our turtle. Since it is passive, we use a predefined decision model called `PassiveTurtleDecisionModel`.

Since the turtle does not need to perceive anything, as a perception module, we use the empty perception model `EmptyPerceptionModel`.

#### The Main class

In the main class, the simulation model is created and the HTML runner is launched and configured.  Here, only the turtles are displayed.

Finally, the probe `LogoRealTimeMatcher` is added to the runner to slow down the simulation so that its execution speed matches a specific factor of N steps per second.

The `main` function contains the following code:

```
fun main(args: Array<String>) {
	var runner = Similar2LogoHtmlRunner()
	// Configuration of the runner
	runner.getConfig().setExportAgents(true)
	// Creation of the model
	var model = PassiveTurtleSimulationModel(PassiveTurtleSimulationParameters())
	// Initialize the runner with the model
	runner.initializeRunner(model)
	// Add other probes to the engine
	runner.addProbe("Real time matcher", LogoRealTimeMatcher(20.0))
	// Open the GUI.
	runner.showView()
}
```

### <a name="kboids"></a> Adding a user-defined decision module to the turtles: The boids model

The [boids](https://en.wikipedia.org/wiki/Boids) (bird-oid) model has been invented by [Graig Reynolds](https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)) in 1986 to simulate flocking behavior of birds. It is based on 3 principles:
    
* separation: boids tend to avoid other boids that are too close,

* alignment: boids tend to align their velocity to boids that are not too close and not too far away,

* cohesion: bois tend to move towards boids that are too far away.

While these rules are essentially heuristic, they can be implemented defining three areas for each principle. 

* Boids change their orientation to get away from other boids in the repulsion area,

* Boids change their orientation and speed to match those of other boids in the orientation area,

* Boids change their orientation to get to other boids in the attraction area.

An implementation of such model is located in the package `fr.lgi2a.similar2logo.examples.boids` which contains 4 classes:

* `BoidsSimulationParameters`, that defines the parameters of the model. This class inherits from `LogoSimulationParameters`,

* `BoidDecisionModel`, that defines the decision model of the boids. This class inherits from `AbstractAgtDecisionModel`.

* `BoidsSimulationModel`, that defines the simulation model. This class inherits from `AbstractLogoSimulationModel`.

* `BoidsSimulationMain`, the main class.


#### Model parameters

The `BoidsSimulationParameters` class contains the following parameters:

```
class BoidsSimulationParameters : LogoSimulationParameters() {


	@Parameter(
			name = "repulsion distance",
			description = "the repulsion distance"
	)
	var repulsionDistance = 6.0

	@Parameter(
			name = "orientation distance",
			description = "the orientation distance"
	)
	var orientationDistance = 10.0

	@Parameter(
			name = "attraction distance",
			description = "the attraction distance"
	)
	var attractionDistance = 14.0

	@Parameter(
			name = "repulsion weight",
			description = "the repulsion weight"
	)
	var repulsionWeight = 1.0

	@Parameter(
			name = "orientation weight",
			description = "the orientation weight"
	)
	var orientationWeight = 1.0

	@Parameter(
			name = "attraction weight",
			description = "the attraction weight"
	)
	var attractionWeight = 1.0

	@Parameter(
			name = "maximal initial speed",
			description = "the maximal initial speed of boids"
	)
	var maxInitialSpeed = 2.0

	@Parameter(
			name = "minimal initial speed",
			description = "the minimal initial speed of boids"
	)
	var minInitialSpeed = 1.0

	@Parameter(
			name = "perception angle",
			description = "the perception angle of the boids in rad"
	)
	var perceptionAngle = Math.PI

	@Parameter(
			name = "number of agents",
			description = "the number of agents in the simulation"
	)
	var nbOfAgents = 200

	@Parameter(
			name = "max angular speed",
			description = "the maximal angular speed of the boids in rad/step"
	)
	var maxAngle = Math.PI / 8

}
```

#### The behavior of the boids 

The decision model consists in changing the direction and speed of the boids according to the previously described rules.
To define a decision model, the modeler must define a class that extends `AbstractAgtDecisionModel` and implement the `decide` function.


```
class BoidDecisionModel(parameters: BoidsSimulationParameters) : AbstractAgtDecisionModel(LogoSimulationLevelList.LOGO) {

	var parameters = parameters

	override fun decide(
			timeLowerBound: SimulationTimeStamp,
			timeUpperBound: SimulationTimeStamp,
			globalState: IGlobalState,
			publicLocalState: ILocalStateOfAgent,
			privateLocalState: ILocalStateOfAgent,
			perceivedData: IPerceivedData,
			producedInfluences: InfluencesMap
	) {
		var castedPublicLocalState = publicLocalState as TurtlePLSInLogo
		var castedPerceivedData = perceivedData as TurtlePerceivedData
		if (!castedPerceivedData.getTurtles().isEmpty()) {
			var orientationSpeed = 0.0
			var nbOfTurtlesInOrientationArea = 0
			var meanAngle = MeanAngle()
			for (perceivedTurtle in castedPerceivedData.getTurtles()) {
				if (perceivedTurtle.getDistanceTo() <= this.parameters.repulsionDistance) {
					meanAngle.add(
							castedPublicLocalState.getDirection() - perceivedTurtle.getDirectionTo(),
							parameters.repulsionWeight
					)
				} else if (perceivedTurtle.getDistanceTo() <= this.parameters.orientationDistance) {
					meanAngle.add(
							perceivedTurtle.getContent().getDirection() - castedPublicLocalState.getDirection(),
							parameters.orientationWeight
					)
					orientationSpeed += perceivedTurtle.getContent().getSpeed() - castedPublicLocalState.getSpeed()
					nbOfTurtlesInOrientationArea++
				} else if (perceivedTurtle.getDistanceTo() <= this.parameters.attractionDistance) {
					meanAngle.add(
							perceivedTurtle.getDirectionTo() - castedPublicLocalState.getDirection(),
							parameters.attractionWeight
					)
				}
			}
			var dd = meanAngle.value()
			if (!MathUtil.areEqual(dd, 0.0)) {
				if (dd > parameters.maxAngle) {
					dd = parameters.maxAngle
				} else if (dd < -parameters.maxAngle) {
					dd = -parameters.maxAngle
				}
				producedInfluences.add(
						ChangeDirection(
								timeLowerBound,
								timeUpperBound,
								dd,
								castedPublicLocalState
						)
				)
			}
			if (nbOfTurtlesInOrientationArea > 0) {
				orientationSpeed /= nbOfTurtlesInOrientationArea;
				producedInfluences.add(
						ChangeSpeed(
								timeLowerBound,
								timeUpperBound,
								orientationSpeed,
								castedPublicLocalState
						)
				)
			}
		}
	}

}
```

#### The simulation model

In the simulation model defined in our example, boids are initially randomly located in the environment with a random orientation and speed.

```
class BoidsSimulationModel(parameters: LogoSimulationParameters) : AbstractLogoSimulationModel(parameters) {

	override fun generateAgents(
			parameters: ISimulationParameters,
			levels: Map<LevelIdentifier, ILevel>
	): AgentInitializationData {
		var castedParameters = parameters as BoidsSimulationParameters
		var result = AgentInitializationData()
		for (i in 0..castedParameters.nbOfAgents) {
			result.getAgents().add(generateBoid(castedParameters))
		}
		return result
	}

	fun generateBoid(p: BoidsSimulationParameters): IAgent4Engine {
		return TurtleFactory.generate(
				ConeBasedPerceptionModel(
						p.attractionDistance, p.perceptionAngle, true, false, false
				),
				BoidDecisionModel(p),
				AgentCategory("b", TurtleAgentCategory.CATEGORY),
				PRNG.get().randomAngle(),
				p.minInitialSpeed + PRNG.get().randomDouble() * (
						p.maxInitialSpeed - p.minInitialSpeed
						),
				0.0,
				PRNG.get().randomDouble() * p.gridWidth,
				PRNG.get().randomDouble() * p.gridHeight
		)
	}

}
```


#### The main class

In the main class, such as in the previous example, the simulation model is created and the HTML runner is launched and configured. 
The `main` function contains the following code:

```
fun main(args: Array<String>) {
	var runner = Similar2LogoHtmlRunner()
	// Configuration of the runner
	runner.getConfig().setExportAgents(true)
	// Creation of the model
	var model = BoidsSimulationModel(BoidsSimulationParameters())
	// Initialize the runner with the model
	runner.initializeRunner(model)
	// Add other probes to the engine
	// Open the GUI.
	runner.showView()
}
```

The main class is very similar to the previous example. Only the simulation model has been changed.


### <a name="kturmite"></a> Dealing with marks: the turmite model

The [turmite model](https://en.wikipedia.org/wiki/Langton's_ant), developed by [Christopher Langton](https://en.wikipedia.org/wiki/Christopher_Langton) in 1986, is a very simple mono-agent model that exhibits an emergent behavior. It is based on 2 rules:

* If the turmite is on a patch that does not contain a mark, it turns right, drops a mark, and moves forward,

* If the turmite is on a patch that contains a mark, it turns left, removes the mark, and moves forward.

The example source code is located in the package `fr.lgi2a.similar2logo.examples.turmite`. It contains 3 classes:

* `TurmiteDecisionModel` that defines the decision model of the turmites,

* `TurmiteSimulationModel` that defines the simulation model,

* `TurmiteSimulationMain`, the main class of the simulation.


#### The decision model

The decision model implements the above described rules :

```
class TurmiteDecisionModel : AbstractAgtDecisionModel(LogoSimulationLevelList.LOGO) {

	override fun decide(
			timeLowerBound: SimulationTimeStamp,
			timeUpperBound: SimulationTimeStamp,
			globalState: IGlobalState,
			publicLocalState: ILocalStateOfAgent,
			privateLocalState: ILocalStateOfAgent,
			perceivedData: IPerceivedData,
			producedInfluences: InfluencesMap
	) {
		var castedPublicLocalState = publicLocalState as TurtlePLSInLogo
		var castedPerceivedData = perceivedData as TurtlePerceivedData
		if(castedPerceivedData.getMarks().isEmpty()) {
			producedInfluences.add(
				ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					Math.PI/2,
					castedPublicLocalState
				)
			)
			producedInfluences.add(
				DropMark(
					timeLowerBound,
					timeUpperBound,
					Mark<Any>(
						castedPublicLocalState.getLocation().clone() as Point2D,
						null
					)
				)
			)
		} else {
			producedInfluences.add(
				ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					-Math.PI/2,
					castedPublicLocalState
				)
			)
			
			producedInfluences.add(
				RemoveMark(
					timeLowerBound,
					timeUpperBound,
					castedPerceivedData.getMarks().iterator().next().getContent()
				)
			)
		}
	}

}
```

#### The simulation model

The simulation model generates a turmite heading north at the location 10.5,10.5 with a speed of 1 and an acceleration of 0:

```
class TurmiteSimulationModel(parameters: LogoSimulationParameters) : AbstractLogoSimulationModel(parameters) {

	override fun generateAgents(
			parameters: ISimulationParameters,
			levels: Map<LevelIdentifier, ILevel>
	): AgentInitializationData {
		var result = AgentInitializationData()
		var turtle = TurtleFactory.generate(
				ConeBasedPerceptionModel(0.0, 2 * Math.PI, false, true, false),
				TurmiteDecisionModel(),
				AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
				LogoEnvPLS.NORTH,
				1.0,
				0.0,
				10.5,
				10.5
		)
		result.getAgents().add(turtle)
		return result
	}

}
```

#### The main class

In the main function, such as in the previous example, the simulation model is created and the HTML runner is launched and configured. 
The `main` function contains the following code:

```
fun main(args: Array<String>) {
	var runner = Similar2LogoHtmlRunner()
	// Configuration of the runner
	runner.getConfig().setExportAgents(true)
	runner.getConfig().setExportMarks(true)
	// Creation of the model
	var model = TurmiteSimulationModel(LogoSimulationParameters())
	// Initialize the runner with the model
	runner.initializeRunner(model)
	// Add other probes to the engine
	runner.addProbe("Real time matcher", LogoRealTimeMatcher(20.0))
	// Open the GUI.
	runner.showView()
}
```

The main difference with the previous example is that in this case we want to observe turtles and marks.

### <a name="kmultiturmite"></a> Adding an interaction and a user-defined reaction model: The multiturmite model

The goal of this example is to implement the multiturmite model proposed by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [this paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf). It extends the traditional [Langton's ant model](http://en.wikipedia.org/wiki/Langton%27s_ant) by specifying what happens when conflicting influences (removing or dropping a mark to the same location) are detected. The following policy is applied:

* if the parameter `dropMark` is `true`, the dropping influence takes precedent over the removing one and reciprocally.

* if the parameter `removeDirectionChange` is `true`, direction changes are not taken into account.

It allows to define 4 different reaction models according to these parameters.

This model is located in the `fr.lgi2a.similar2logo.examples.multiturmite` package and contains at least 5 classes:

* `MultiTurmiteSimulationParameters`, that contains the parameters of the model,

* `TurmiteInteraction`, that defines an interaction between multiple turmites,

* `MultiTurmiteReactionModel`, that extends `LogoDefaultReactionModel` and defines the reaction model, i.e., the way influences are handled,

* `MultiTurmiteSimulationModel` that defines the simulation model,

* Different main classes that define a specific initial configuration of the simulation, in our case, based on the ones described by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [their paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf).

#### Model parameters

The model parameters are defined in the class `MultiTurmiteSimulationParameters`. It defines how influences are handled according to the previously defined policy, the number of turmites and their initial locations.

```
class MultiTurmiteSimulationParameters : LogoSimulationParameters() {

	@Parameter(
			name = "remove direction change",
			description = "if checked, direction changes are not taken into account when two turtles want to modify the same patch"
	)
	var removeDirectionChange = true

	@Parameter(
			name = "inverse mark update",
			description = "if checked, the output of turtle actions is inversed when two turtles want to modify the same patch"
	)
	var inverseMarkUpdate = true

	@Parameter(
			name = "number of turmites",
			description = "the  number of turmites in the environment"
	)
	var nbOfTurmites = 2

	@Parameter(
			name = "initial locations",
			description = "the  initial locations of turmites"
	)
	var initialLocations = ArrayList<Point2D>()

	@Parameter(
			name = "initial directions",
			description = "the initial directions of turmites"
	)
	var initialDirections = ArrayList<Double>()

}
```

#### The reaction model

In the previous example, the influence management relies on the default reaction model defined in the class `LogoDefaultReactionModel`. Now, we want to handle some influences manually. To do so, we have to define a class `MultiTurmiteReactionModel` that inherits from `LogoDefaultReactionModel`. This class has one property: the parameters of the simulation.

What we have to do is to change the behavior of the `makeRegularReaction` function. A generic stub of a specific reaction model is given below:

```
override fun makeRegularReaction(
			transitoryTimeMin: SimulationTimeStamp,
			transitoryTimeMax: SimulationTimeStamp,
			consistentState: ConsistentPublicLocalDynamicState,
			regularInfluencesOftransitoryStateDynamics: Set<IInfluence>,
			remainingInfluences: InfluencesMap
	) {
		var nonSpecificInfluences = LinkedHashSet<IInfluence>()
		
        //Management of specific influences
        
		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, nonSpecificInfluences, remainingInfluences)
	}
```

The idea is to identify the influences that do not trigger a generic reaction and manage them separately. Non specific influences are handled by the regular reaction.

In this case, specific influences represents collisions between turtle decisions. We define a class `TurmiteInteraction` that explicitly represent possible collisions for each location.

```
class TurmiteInteraction {
	
	var dropMarks = LinkedHashSet<DropMark>()
	var removeMarks = LinkedHashSet<RemoveMark>()
	var changeDirections = LinkedHashSet<ChangeDirection>()
	
	fun isColliding(): Boolean {
		return removeMarks.size > 1|| dropMarks.size > 1;
	}
	
}
```

Then, it is easy to implement the reaction model whether the influences are colliding or not:

```
class MultiTurmiteReactionModel(parameters: MultiTurmiteSimulationParameters) : LogoDefaultReactionModel() {

	var parameters = parameters

	override fun makeRegularReaction(
			transitoryTimeMin: SimulationTimeStamp,
			transitoryTimeMax: SimulationTimeStamp,
			consistentState: ConsistentPublicLocalDynamicState,
			regularInfluencesOftransitoryStateDynamics: Set<IInfluence>,
			remainingInfluences: InfluencesMap
	) {
		var nonSpecificInfluences = LinkedHashSet<IInfluence>()
		var collisions = LinkedHashMap<Point2D, TurmiteInteraction>()

		//Organize influences by location and type
		for (influence in regularInfluencesOftransitoryStateDynamics) {
			if (influence.category.equals(DropMark.CATEGORY)) {
				var castedDropInfluence = influence as DropMark
				if (!collisions.containsKey(castedDropInfluence.mark.location)) {
					collisions.put(
							castedDropInfluence.mark.location,
							TurmiteInteraction()
					)
				}
				collisions.get(castedDropInfluence.mark.location)?.dropMarks?.add(castedDropInfluence)

			} else if (influence.category.equals(RemoveMark.CATEGORY)) {
				var castedRemoveInfluence = influence as RemoveMark
				if (!collisions.containsKey(castedRemoveInfluence.mark.location)) {
					collisions.put(
							castedRemoveInfluence.mark.location,
							TurmiteInteraction()
					)
				}
				collisions.get(castedRemoveInfluence.mark.location)?.removeMarks?.add(castedRemoveInfluence)
			} else if (influence.category.equals(ChangeDirection.CATEGORY)) {
				var castedChangeDirectionInfluence = influence as ChangeDirection
				if (!collisions.containsKey(castedChangeDirectionInfluence.target.location)) {
					collisions.put(
							castedChangeDirectionInfluence.target.location,
							TurmiteInteraction()
					)
				}
				collisions.get(castedChangeDirectionInfluence.target.location)?.changeDirections?.add(castedChangeDirectionInfluence)
			} else {
				nonSpecificInfluences.add(influence)
			}
		}

		for (collision in collisions.values) {
			if (collision.isColliding()) {
				if (!collision.dropMarks.isEmpty() && !this.parameters.inverseMarkUpdate) {
					nonSpecificInfluences.add(
							collision.dropMarks.iterator().next()
					)
				}
				if (!collision.removeMarks.isEmpty() && !this.parameters.inverseMarkUpdate) {
					nonSpecificInfluences.add(
							collision.removeMarks.iterator().next()
					)
				}
				if (!this.parameters.removeDirectionChange) {
					nonSpecificInfluences.addAll(collision.changeDirections)
				}
			} else {
				nonSpecificInfluences.addAll(collision.changeDirections)
				if (!collision.dropMarks.isEmpty()) {
					nonSpecificInfluences.add(collision.dropMarks.iterator().next())
				}
				if (!collision.removeMarks.isEmpty()) {
					nonSpecificInfluences.add(collision.removeMarks.iterator().next())
				}
			}
		}

		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, nonSpecificInfluences, remainingInfluences)
	}
}

```
#### The simulation model

The simulation model of this example is located in the class `MultiTurmiteSimulationModel`.

Such as in the previous example, we have to redefine the function `generateAgents` to specify the initial population of agents of the simulation. However, contrary to the previous examples, we have to redefine the function `generateLevels` to specify the reaction model we use:

```
class MultiTurmiteSimulationModel(parameters: LogoSimulationParameters) : AbstractLogoSimulationModel(parameters) {

	
	override fun generateLevels(
			simulationParameters: ISimulationParameters
	): List<ILevel> {
		var logo = ExtendedLevel(
				simulationParameters.initialTime,
				LogoSimulationLevelList.LOGO,
				PeriodicTimeModel(
						1,
						0,
						simulationParameters.initialTime
				),
				MultiTurmiteReactionModel(simulationParameters as MultiTurmiteSimulationParameters)
		)
		var levelList = LinkedList<ILevel>()
		levelList.add(logo)
		return levelList
	}
	
	override fun generateAgents(
			parameters: ISimulationParameters,
			levels: Map<LevelIdentifier, ILevel>
	): AgentInitializationData {
		var result = AgentInitializationData()
	    var castedSimulationParameters = simulationParameters as MultiTurmiteSimulationParameters
		if(castedSimulationParameters.initialLocations.isEmpty()) {
			for(i in 1..castedSimulationParameters.nbOfTurmites) {
				var turtle = TurtleFactory.generate(
					ConeBasedPerceptionModel(0.0, 2*Math.PI, false, true, false),
					TurmiteDecisionModel(),
					AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
					randomDirection(),
					1.0,
					0.0,
					Math.floor(PRNG.get().randomDouble()*castedSimulationParameters.gridWidth),
					Math.floor(PRNG.get().randomDouble()*castedSimulationParameters.gridHeight)
				)
				result.getAgents().add( turtle )
			}
		} else {
			if(
				castedSimulationParameters.nbOfTurmites != castedSimulationParameters.initialDirections.size ||
				castedSimulationParameters.nbOfTurmites != castedSimulationParameters.initialLocations.size
			) {
				throw UnsupportedOperationException("Inital locations and directions must be specified for each turmite")
			}
			for(i in 0..castedSimulationParameters.nbOfTurmites-1) {
				var turtle = TurtleFactory.generate(
					ConeBasedPerceptionModel(0.0, 2*Math.PI, false, true, false),
					TurmiteDecisionModel(),
					AgentCategory("turmite", TurtleAgentCategory.CATEGORY),
					castedSimulationParameters.initialDirections[i],
					1.0,
					0.0,
					castedSimulationParameters.initialLocations[i].x,
					castedSimulationParameters.initialLocations[i].y
				)
				result.getAgents().add( turtle )
			}
		}
		return result
	}
	
	fun randomDirection(): Double {
		var rand = PRNG.get().randomDouble()
		if(rand < 0.25) {
			return LogoEnvPLS.NORTH
		} else if ( rand < 0.5 ) {
			return LogoEnvPLS.WEST
		} else if ( rand < 0.75 ) {
			return LogoEnvPLS.SOUTH
		}
		return LogoEnvPLS.EAST
	}

}
```


#### The Main file

The main file contains the following code:
```
fun main(args: Array<String>) {
	var runner = Similar2LogoHtmlRunner()
	// Configuration of the runner
	runner.config.setExportAgents(true)
	runner.config.setExportMarks(true)
	// Creation of the model
	var parameters = MultiTurmiteSimulationParameters()
	parameters.apply {
		nbOfTurmites = 2
		initialLocations.add(
				Point2D.Double(
						Math.floor(parameters.gridWidth / 2.0),
						Math.floor(parameters.gridHeight / 2.0)
				)
		)
		parameters.initialDirections.add(LogoEnvPLS.NORTH)
		parameters.initialLocations.add(
				Point2D.Double(
						Math.floor(parameters.gridWidth / 2.0),
						Math.floor(parameters.gridHeight / 2.0) + 1
				)
		)
		parameters.initialDirections.add(LogoEnvPLS.NORTH)
	}
	var model = MultiTurmiteSimulationModel(parameters)
	// Initialize the runner with the model
	runner.initializeRunner(model)
	// Add other probes to the engine
	runner.addProbe("Real time matcher", LogoRealTimeMatcher(20.0))
	// Open the GUI.
	runner.showView()
}
```

In this case, we create a specific instance of the multiturmite model with 2 turmites. This configuration described by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [their paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf) produces interesting and distinctive emergent behaviors according to the values of `dropMark` and `removeDirectionChange` parameters.

Such as in the previous example, we want to observe the turtles and the marks.

### <a name="ksegregation"></a> Adding user-defined influence, reaction model and GUI: The segregation model


The segregation model has been proposed by [Thomas Schelling](https://en.wikipedia.org/wiki/Thomas_Schelling) in 1971 in his famous paper [Dynamic Models of Segregation](https://www.stat.berkeley.edu/~aldous/157/Papers/Schelling_Seg_Models.pdf). The goal of this model is to show that segregation can occur even if it is not wanted by the agents.

In our implementation of this model, turtles are located in the grid and at each step, compute an happiness index based on the similarity of other agents in their neighborhood. If this index is below a value, called here similarity rate, the turtle wants to move to an other location.

The segregation simulation source code is located in the package `fr.lgi2a.similar2logo.examples.segregation`. It contains 6 classes

* `SegregationSimulationParameters` that extends `LogoSimulationParameters`, that contains the parameters of the simulation.
    
* `Move` that extends `RegularInfluence`, representing a model-specific influence, emitted by an agent who wants to move to another location.
    
* `SegregationAgentDecisionModel` that extends `AbstractAgtDecisionModel`, representing the decision model of our turtles.
    
* `SegregationReactionModel` that extends `LogoDefaultReactionModel`, representing the model-specific reaction model. It defines how `Move` influences are handled.
    
* a class `SegregationSimulationModel` that extends `AbstractLogoSimulationModel`, representing the simulation model, i.e., the initial state of the simulation.

* the main class of the simulation `SegregationSimulationMain`.

* a HTML file `segregationgui.html`, that contains the GUI of the simulation.

#### Model parameters

The model parameters are defined in the class `SegregationSimulationParameters`. It contains the following parameters:

```
class SegregationSimulationParameters : LogoSimulationParameters() {

	@Parameter(
			name = "similarity rate",
			description = "the rate of same-color turtles that each turtle wants among its neighbors"
	)
	var similarityRate = 3.0 / 8

	@Parameter(
			name = "vacancy rate",
			description = "the rate of vacant settling places"
	)
	var vacancyRate = 0.05

	@Parameter(
			name = "perception distance",
			description = "the perception distance of agents"
	)
	var perceptionDistance = Math.sqrt(2.0)

}
```

#### Model-specific influence

We define an influence called `Move` that is emitted by an agent who wants to move to another location. It is defined by a  unique identifier, here "move", and the state of the turtle that wants to move.

```
class Move(
		timeLowerBound: SimulationTimeStamp,
		timeUpperBound: SimulationTimeStamp,
		target: TurtlePLSInLogo) :
		RegularInfluence("move", LogoSimulationLevelList.LOGO, timeLowerBound, timeUpperBound) {

	var target = target

}
```

#### Decision model

The decision model computes a happiness index based on the rate of turtles of different categories in its neighborhood. If the index is below the parameter `similarityRate`, the turtle emits a `Move` influence.

```
class SegregationAgentDecisionModel(parameters: SegregationSimulationParameters) : AbstractAgtDecisionModel(LogoSimulationLevelList.LOGO) {

	var parameters = parameters

	override fun decide(
			timeLowerBound: SimulationTimeStamp,
			timeUpperBound: SimulationTimeStamp,
			globalState: IGlobalState,
			publicLocalState: ILocalStateOfAgent,
			privateLocalState: ILocalStateOfAgent,
			perceivedData: IPerceivedData,
			producedInfluences: InfluencesMap
	) {
		var similarityRate = 0.0
		var castedPublicLocalState = publicLocalState as TurtlePLSInLogo
		var castedPerceivedData = perceivedData as TurtlePerceivedData
		for (perceivedTurtle in castedPerceivedData.getTurtles()) {
			var castedPerceivedTurtle = perceivedTurtle.getContent() as TurtlePLSInLogo
			if (castedPerceivedTurtle.getCategoryOfAgent().isA(castedPublicLocalState.getCategoryOfAgent())) {
				similarityRate++
			}
		}
		if (!castedPerceivedData.getTurtles().isEmpty()) {
			similarityRate /= castedPerceivedData.getTurtles().size
		}
		if (similarityRate < this.parameters.similarityRate) {
			producedInfluences.add(Move(timeLowerBound, timeUpperBound, castedPublicLocalState))
		}
	}

}
```

#### Reaction model

The reaction model handles the `Move` influences emitted by unhappy turtles. First, it identifies vacant places and moves the turtles that have emitted a `Move` influence. Note that if there is not enough vacant places, not all turtle wishes can be fulfilled.

```
class SegregationReactionModel : LogoDefaultReactionModel() {

	override fun makeRegularReaction(
			transitoryTimeMin: SimulationTimeStamp,
			transitoryTimeMax: SimulationTimeStamp,
			consistentState: ConsistentPublicLocalDynamicState,
			regularInfluencesOftransitoryStateDynamics: Set<IInfluence>,
			remainingInfluences: InfluencesMap
	) {
		//If there there is at least an agent that wants to move
		if (regularInfluencesOftransitoryStateDynamics.size > 2) {
			var specificInfluences = ArrayList<IInfluence>()
			var vacantPlaces = ArrayList<Point2D>()
			specificInfluences.addAll(regularInfluencesOftransitoryStateDynamics)
			PRNG.get().shuffle(specificInfluences)
			//Identify vacant places
			var castedEnvState = consistentState.getPublicLocalStateOfEnvironment() as LogoEnvPLS
			for (x in 0..castedEnvState.getWidth()-1) {
				for (y in 0..castedEnvState.getHeight()-1) {
					if (castedEnvState.getTurtlesAt(x, y).isEmpty()) {
						vacantPlaces.add(
								Point2D.Double(x.toDouble(), y.toDouble())
						)
					}
				}
			}
			PRNG.get().shuffle(vacantPlaces)
			//move agents
			var i = 0
			for (influence in specificInfluences) {
				if (influence.getCategory().equals("move")) {
					var castedInfluence = influence as Move
					castedEnvState.getTurtlesInPatches()[Math.floor(castedInfluence.target.getLocation().getX()).toInt()][Math.floor(castedInfluence.target.getLocation().getY()).toInt()].clear()
					castedEnvState.getTurtlesInPatches()[Math.floor(vacantPlaces.get(i).getX()).toInt()][Math.floor(vacantPlaces.get(i).getY()).toInt()].add(castedInfluence.target)
					castedInfluence.target.setLocation(vacantPlaces.get(i))
					i++
				}
				if (i >= vacantPlaces.size) {
					break
				}
			}
		}
	}
}
```


#### Simulation model

The simulation model generates the Logo level using the user-defined reaction model and a simple periodic time model. It also generates turtles of 2 different types (a and b) randomly in the grid with respect to the vacancy rate parameter.


```
class SegregationSimulationModel(parameters: LogoSimulationParameters) : AbstractLogoSimulationModel(parameters) {


	override fun generateLevels(
			simulationParameters: ISimulationParameters
	): List<ILevel> {
		var logo = ExtendedLevel(
				simulationParameters.getInitialTime(),
				LogoSimulationLevelList.LOGO,
				PeriodicTimeModel(
						1,
						0,
						simulationParameters.getInitialTime()
				),
				SegregationReactionModel()
		)
		var levelList = LinkedList<ILevel>()
		levelList.add(logo)
		return levelList
	}

	override fun generateAgents(
			parameters: ISimulationParameters,
			levels: Map<LevelIdentifier, ILevel>
	): AgentInitializationData {
		var castedParameters = parameters as SegregationSimulationParameters
		var result = AgentInitializationData()
		var t: String
		for (x in 0..castedParameters.gridWidth-1) {
			for (y in 0..castedParameters.gridHeight-1) {
				if (PRNG.get().randomDouble() >= castedParameters.vacancyRate) {
					if (PRNG.get().randomBoolean()) {
						t = "a"
					} else {
						t = "b"
					}
					var turtle = TurtleFactory.generate(
							ConeBasedPerceptionModel(castedParameters.perceptionDistance, 2 * Math.PI, true, false, false),
							SegregationAgentDecisionModel(castedParameters),
							AgentCategory(t, TurtleAgentCategory.CATEGORY),
							0.0,
							0.0,
							0.0,
							x.toDouble(),
							y.toDouble()
					);
					result.getAgents().add(turtle)
				}
			}
		}
		return result
	}

}
```

#### <a name="segregationgui"></a> HTML GUI

The HTML GUI specifies how turtles are displayed in the grid.Turtles of type a are colored in blue and turtles of type b are colored in red.

```
<canvas id='grid_canvas' class='center-block' width='400' height='400'></canvas>
<script type='text/javascript'>
    drawCanvas = function (data) {
        var json = JSON.parse(data),
            canvas = document.getElementById('grid_canvas'),
            context = canvas.getContext('2d');
        context.clearRect(0, 0, canvas.width, canvas.height);
        for (var i = 0; i < json.agents.length; i++) {
            var centerX = json.agents[i].x * canvas.width;
            var centerY = json.agents[i].y * canvas.height;
            var radius = 2;
            if (json.agents[i].t == 'a') {
                context.fillStyle = 'red';
            } else {
                context.fillStyle = 'blue';
            }
            context.beginPath();
            context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
            context.fill();
        }
    }
</script>
```

#### Main class

The main function simply launches and configures the HTML runner with the above described GUI.

```
fun main(args: Array<String>) {
	var runner = Similar2LogoHtmlRunner()
	runner.getConfig().setCustomHtmlBodyFromString(
			SegregationSimulationMain::class.java.getResource("segregationgui.html").readText()
	)
	// Configuration of the runner
	runner.getConfig().setExportAgents(true)
	// Creation of the model
	var model = SegregationSimulationModel(SegregationSimulationParameters())
	// Initialize the runner with the model
	runner.initializeRunner(model)
	// Add other probes to the engine
	// Open the GUI.
	runner.showView()
}
```

## <a name="pexamples"></a> Python Examples

In the following we comment the examples written in Python distributed with Similar2Logo. Each example introduces a specific feature.

* [A first example with a passive turtle](#ppassive)

* [Adding a user-defined decision model to the turtles: The boids model](#pboids)

* [Dealing with marks: the turmite model](#pturmite)

* [Adding an interaction and a user-defined reaction model: The multiturmite model](#pmultiturmite)

### <a name="ppassive"></a> A first example with a passive turtle

First we consider a simple example with a single passive agent. The example source code is located in the package `fr.lgi2a.similar2logo.examples.passive`. It contains 1 python script.

Foremost, we define the parameters of the model by creating an object that inherits from `LogoSimulationParameters`, that contains the generic parameters of a Logo-like simulation (environment size, topology, etc.).

```
class PassiveTurtleSimulationParameters(LogoSimulationParameters): 

    def __init__(self):
        self.initialX = 10.0
        self.initialY = 10.0
        self.initialSpeed = 0.1
        self.initialAcceleration = 0.0
        self.initialDirection = LogoEnvPLS.NORTH
```

Then, we define the simulation model i.e, the initial state of the simulation from the `AbstractLogoSimulationModel` class. We must implement the `generateAgents` method to describe the initial state of our passive turtle.

```
class PassiveTurtleSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(PassiveTurtleSimulationModel, self).__init__(parameters)
    
    def generateAgents(self, parameters, levels):
        result = AgentInitializationData()
        turtle = TurtleFactory.generate(
            EmptyPerceptionModel(),
            PassiveTurtleDecisionModel(),
            AgentCategory('passive', [TurtleAgentCategory.CATEGORY]),
            parameters.initialDirection,
            parameters.initialSpeed,
            parameters.initialAcceleration,
            parameters.initialX,
            parameters.initialY
        )
        result.agents.add(turtle)
        return result
```

Then we launch and configure the HTML runner. Here, only the turtles are displayed. Finally, the probe `LogoRealTimeMatcher` is added to the server to slow down the simulation so that its execution speed matches a specific factor of N steps per second.

```
runner = Similar2LogoHtmlRunner()
runner.config.setExportAgents(True)
model = PassiveTurtleSimulationModel(PassiveTurtleSimulationParameters())
runner.initializeRunner(model)
runner.addProbe('Real time matcher', LogoRealTimeMatcher(20.0))
runner.showView()
```

<a name="pboids"></a> Adding a user-defined decision module to the turtles: The boids model

The [boids](https://en.wikipedia.org/wiki/Boids) (bird-oid) model has been invented by [Craig Reynolds](https://en.wikipedia.org/wiki/Craig_Reynolds_(computer_graphics)) in 1986 to simulate the flocking behavior of birds. It is based on 3 principles:
    
* separation: boids tend to avoid other boids that are too close,

* alignment: boids tend to align their velocity to boids that are not too close and not too far away,

* cohesion: bois tend to move towards boids that are too far away.

While these rules are essentially heuristic, they can be implemented defining three areas (repulsion, orientation, attraction) for each principle. 

* Boids change their orientation to get away from other boids in the repulsion area,

* Boids change their orientation and speed to match those of other boids in the orientation area,

* Boids change their orientation to get to other boids in the attraction area.

An implementation of such model is located in the package `fr.lgi2a.similar2logo.examples.boids` which contains 1 python script called `BoidsSimulation.py`.

#### Model parameters

The model parameters and their default values are defined as in the previous example.

```
class BoidsSimulationParameters(LogoSimulationParameters): 

    def __init__(self):
        self.repulsionDistance = 6.0
        self.orientationDistance = 10.0
        self.attractionDistance = 14.0
        self.repulsionWeight = 1.0
        self.orientationWeight = 1.0
        self.attractionWeight = 1.0
        self.maxInitialSpeed = 2.0
        self.minInitialSpeed = 1.0
        self.perceptionAngle = math.pi
        self.nbOfAgents = 200
        self.maxAngle = math.pi / 8
```

#### Decision model

The decision model consists in changing the direction and speed of the boids according to the previously described rules.
To define a decision model, the modeler must define a class that extends `AbstractAgtDecisionModel` and implement the `decide` method.


```
class BoidDecisionModel(AbstractAgtDecisionModel):
    
    def __init__(self, parameters):
        self.parameters = parameters
        super(BoidDecisionModel, self).__init__(LogoSimulationLevelList.LOGO)
    
    def decide(self, timeLowerBound, timeUpperBound, globalState, publicLocalState, privateLocalState, perceivedData, producedInfluences):
        if not perceivedData.turtles.isEmpty():
            orientationSpeed = 0.0
            nbOfTurtlesInOrientationArea = 0
            meanAngle = MeanAngle()
            for perceivedTurtle in perceivedData.turtles:
                if perceivedTurtle.distanceTo <= self.parameters.repulsionDistance:
                    meanAngle.add(
                        publicLocalState.direction - perceivedTurtle.directionTo,
                        self.parameters.repulsionWeight
                    )
                elif perceivedTurtle.distanceTo <= self.parameters.orientationDistance:
                    meanAngle.add(
                        perceivedTurtle.content.direction - publicLocalState.direction,
                        self.parameters.orientationWeight
                    )
                    orientationSpeed += perceivedTurtle.content.speed - publicLocalState.speed
                    nbOfTurtlesInOrientationArea+=1
                elif perceivedTurtle.distanceTo <= self.parameters.attractionDistance:
                    meanAngle.add(
                        perceivedTurtle.directionTo - publicLocalState.direction,
                        self.parameters.attractionWeight
                    )
   
            dd = meanAngle.value()
            if not MathUtil.areEqual(dd, 0.0):
                if dd > self.parameters.maxAngle:
                    dd = self.parameters.maxAngle
                elif dd < -self.parameters.maxAngle:
                    dd = -self.parameters.maxAngle

                producedInfluences.add(
                    ChangeDirection(
                        timeLowerBound,
                        timeUpperBound,
                        dd,
                        publicLocalState
                    )
                )
                
            if  nbOfTurtlesInOrientationArea > 0:
                orientationSpeed /= nbOfTurtlesInOrientationArea;
                producedInfluences.add(
                    ChangeSpeed(
                        timeLowerBound,
                        timeUpperBound,
                        orientationSpeed,
                        publicLocalState
                    )
                )
```

#### The simulation model

In the simulation model defined in our example, boids are initially located at the center of the environment with a random orientation and speed.


```
class BoidsSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(BoidsSimulationModel, self).__init__(parameters)
    
    def generateBoid(self, p):
        return TurtleFactory.generate(
            ConeBasedPerceptionModel(
                p.attractionDistance, p.perceptionAngle, True, False, False
            ),
            BoidDecisionModel(p),
            AgentCategory("b", [TurtleAgentCategory.CATEGORY]),
            PRNG.get().randomAngle(),
            p.minInitialSpeed + PRNG.get().randomDouble() * (
                p.maxInitialSpeed - p.minInitialSpeed
            ),
            0.0,
            PRNG.get().randomDouble() * p.gridWidth,
            PRNG.get().randomDouble() * p.gridHeight
        )
        
    def generateAgents(self, parameters, levels):
        result = AgentInitializationData()
        for i in range(0, parameters.nbOfAgents):
            result.agents.add(self.generateBoid(parameters))
        return result
```

#### Launch the HTML runner

Finally, we launch and configure the HTML runner as in the previous example.

```
runner = Similar2LogoHtmlRunner()
runner.config.setExportAgents(True)
model = BoidsSimulationModel(BoidsSimulationParameters())
runner.initializeRunner(model)
runner.showView()
```

### <a name="pturmite"></a> Dealing with marks: the turmite model

The [turmite model](https://en.wikipedia.org/wiki/Langton's_ant), developed by [Christopher Langton](https://en.wikipedia.org/wiki/Christopher_Langton) in 1986, is a very simple mono-agent model that exhibits an emergent behavior. It is based on 2 rules:

* If the turmite is on a patch that does not contain a mark, it turns right, drops a mark, and moves forward,

* If the turmite is on a patch that contains a mark, it turns left, removes the mark, and moves forward.

The example source code is located in the package `fr.lgi2a.similar2logo.examples.turmite`. It contains 1 python script called `TurmiteSimulation.py`.

#### Model parameters

First we define the simulation parameters. Here we only need to specify the final step of the simulation:

```
def parameters = new LogoSimulationParameters(												
	finalTime: new SimulationTimeStamp(100000)
)
```
#### The decision model

The decision model implements the above described rules :

```
class TurmiteDecisionModel(AbstractAgtDecisionModel):
    
    def __init__(self):
        super(TurmiteDecisionModel, self).__init__(LogoSimulationLevelList.LOGO)
    
    def decide(self, timeLowerBound, timeUpperBound, globalState, publicLocalState, privateLocalState, perceivedData, producedInfluences):
        if perceivedData.marks.isEmpty():
            producedInfluences.add(
                ChangeDirection(
                    timeLowerBound,
                    timeUpperBound,
                    math.pi/2,
                    publicLocalState
                )
            )
            producedInfluences.add(
                DropMark(
                    timeLowerBound,
                    timeUpperBound,
                    Mark(
                        publicLocalState.location.clone(),
                        None
                    )
                )
            )
        else:
            producedInfluences.add(
                ChangeDirection(
                    timeLowerBound,
                    timeUpperBound,
                    -math.pi/2,
                    publicLocalState
                )
            )
            
            producedInfluences.add(
                RemoveMark(
                    timeLowerBound,
                    timeUpperBound,
                    perceivedData.marks.iterator().next().content
                )
            )
```

#### The simulation model

The simulation model generates a turmite heading north at the location 10.5,10.5 with a speed of 1 and an acceleration of 0:

```
class TurmiteSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(TurmiteSimulationModel, self).__init__(parameters)
    
    def generateAgents(self, parameters, levels):
        result = AgentInitializationData()
        turtle = TurtleFactory.generate(
            ConeBasedPerceptionModel(0.0, 2 * math.pi, False, True, False),
            TurmiteDecisionModel(),
            AgentCategory('turmite', [TurtleAgentCategory.CATEGORY]),
            LogoEnvPLS.NORTH,
            1.0,
            0.0,
            10.5,
            10.5
        )
        result.agents.add(turtle)
        return result
```


#### Launch the HTML runner

```
runner = Similar2LogoHtmlRunner()
runner.config.setExportAgents(True)
runner.config.setExportMarks(True)
model = TurmiteSimulationModel(LogoSimulationParameters())
runner.initializeRunner(model)
runner.addProbe('Real time matcher', LogoRealTimeMatcher(20.0))
runner.showView()
```

The main difference with the previous example is that in this case we want to observe turtles and marks.

### <a name="pmultiturmite"></a> Adding an interaction and a user-defined reaction model: The multiturmite model

The goal of this example is to implement the multiturmite model proposed by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [this paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf). It extends the traditional [Langton's ant model](http://en.wikipedia.org/wiki/Langton%27s_ant) by specifying what happens when conflicting influences (removing or dropping a mark to the same location) are detected. The following policy is applied:

* if the parameter `dropMark` is `True`, the dropping influence takes precedent over the removing one and reciprocally.

* if the parameter `removeDirectionChange` is `True`, direction changes are not taken into account.

It allows to define 4 different reaction models according to these parameters. 

The example source code is located in the package `fr.lgi2a.similar2logo.examples.multiturmite`. It contains 1 python script called `MultiTurmiteSimulation.py`.

#### Model parameters

The model parameters are defined in the class `MultiTurmiteSimulationParameters`. It defines how influences are handled according to the previously defined policy, the number of turmites and their initial locations and directions.

```
class MultiTurmiteSimulationParameters(LogoSimulationParameters): 

    def __init__(self):
        self.removeDirectionChange = False
        self.inverseMarkUpdate = False
        self.nbOfTurmites = 4
        self.initialLocations = [
            Point2D.Double(
                math.floor(self.gridWidth / 2.0),
                math.floor(self.gridHeight / 2.0)
            ),
            Point2D.Double(
                math.floor(self.gridWidth / 2.0),
                math.floor(self.gridHeight / 2.0) + 1
            ),
            Point2D.Double(
                math.floor(self.gridWidth / 2.0) + 10,
                math.floor(self.gridHeight / 2.0)
            ),
            Point2D.Double(
                math.floor(self.gridWidth / 2.0) + 10,
                math.floor(self.gridHeight / 2.0) + 1
            )
        ]
        self.initialDirections = [
            LogoEnvPLS.NORTH,
            LogoEnvPLS.SOUTH,
            LogoEnvPLS.NORTH,
            LogoEnvPLS.SOUTH
        ]
```

In this case, we create a specific instance of the multiturmite model with 4 turmites. This configuration described by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [their paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf) produces interesting and distinctive emergent behaviors according to the values of `dropMark` and `removeDirectionChange` parameters.


#### The reaction model

In the previous example, the influence management relies on the default reaction model defined in the class `LogoDefaultReactionModel`. Now, we want to handle some influences manually. To do so, we have to define a class `MultiTurmiteReactionModel` that inherits from `LogoDefaultReactionModel`. This class has one attribute: the parameters of the simulation.

The idea is to identify the influences that do not trigger a generic reaction and manage them separately. Non specific influences are handled by the regular reaction.

In this case, specific influences represents collisions between turtle decisions. We define a class `TurmiteInteraction` that explicitly represent possible collisions for each location.

```
class TurmiteInteraction:
    
    def __init__(self):
        self.dropMarks = LinkedHashSet()
        self.removeMarks = LinkedHashSet()
        self.changeDirections = LinkedHashSet()
    
    def isColliding(self):
        return self.removeMarks.size() > 1 or self.dropMarks.size() > 1
```

Then, it is easy to implement the reaction model whether the influences are colliding or not. To do so we redefine the `makeRegularReaction` method.

```
class MultiTurmiteReactionModel(LogoDefaultReactionModel):
        
        def __init__(self,parameters):
            self.parameters = parameters

        def makeRegularReaction(
            self,
            transitoryTimeMin,
            transitoryTimeMax,
            consistentState,
            regularInfluencesOftransitoryStateDynamics,
            remainingInfluences
        ):
            nonSpecificInfluences = LinkedHashSet()
            collisions = {}
            
            for influence in regularInfluencesOftransitoryStateDynamics:
                if influence.category == DropMark.CATEGORY:
                    if not influence.mark.location in collisions:
                        collisions[influence.mark.location] = TurmiteInteraction()
                    collisions[influence.mark.location].dropMarks.add(influence)
                elif influence.category == RemoveMark.CATEGORY:
                    if not influence.mark.location in collisions:
                        collisions[influence.mark.location] = TurmiteInteraction()
                    collisions[influence.mark.location].removeMarks.add(influence)
                elif influence.category == ChangeDirection.CATEGORY:
                    if not influence.target.location in collisions:
                        collisions[influence.target.location] = TurmiteInteraction()
                    collisions[influence.target.location].changeDirections.add(influence)
                else:
                    nonSpecificInfluences.add(influence)

            for collision in collisions.values():
                if collision.isColliding():
                    if not collision.dropMarks.isEmpty() and not self.parameters.inverseMarkUpdate:
                        nonSpecificInfluences.add(
                            collision.dropMarks.iterator().next()
                        )
                    if not collision.removeMarks.isEmpty() and not self.parameters.inverseMarkUpdate:
                        nonSpecificInfluences.add(
                            collision.removeMarks.iterator().next()
                        )        
                    if not self.parameters.removeDirectionChange:
                        nonSpecificInfluences.addAll(collision.changeDirections)
                else:
                    nonSpecificInfluences.addAll(collision.changeDirections)
                    if not collision.dropMarks.isEmpty():
                        nonSpecificInfluences.add(
                            collision.dropMarks.iterator().next()
                        )
                    if not collision.removeMarks.isEmpty():
                        nonSpecificInfluences.add(
                            collision.removeMarks.iterator().next()
                        )
            super(MultiTurmiteReactionModel, self).makeRegularReaction(
                transitoryTimeMin,
                transitoryTimeMax,
                consistentState,
                nonSpecificInfluences,
                remainingInfluences
            )
```
#### The simulation model

The simulation model of this example is located in the class `MultiTurmiteSimulationModel`.

Such as in the previous example, we have to redefine the function `generateAgents` to specify the initial population of agents of the simulation. However, contrary to the previous examples, we have to redefine the function `generateLevels` to specify the reaction model we use:

```
class MultiTurmiteSimulationModel(AbstractLogoSimulationModel):
    
    def __init__(self, parameters):
        super(MultiTurmiteSimulationModel, self).__init__(parameters)
    
    def randomDirection(self):
        rand = PRNG.get().randomDouble()
        if rand < 0.25:
            return LogoEnvPLS.NORTH
        elif rand < 0.5:
            return LogoEnvPLS.WEST
        elif  rand < 0.75:
            return LogoEnvPLS.SOUTH
        return LogoEnvPLS.EAST
    
    
    def generateLevels(self, simulationParameters):
        logo = ExtendedLevel(
            simulationParameters.initialTime,
            LogoSimulationLevelList.LOGO,
            PeriodicTimeModel(
                1,
                0,
                simulationParameters.initialTime
            ),
            MultiTurmiteReactionModel(simulationParameters)
        )
        levelList = []
        levelList.append(logo)
        return levelList
```


#### Launch the HTML runner

```
runner = Similar2LogoHtmlRunner()
runner.config.setExportAgents(True)
runner.config.setExportMarks(True)
model = MultiTurmiteSimulationModel(MultiTurmiteSimulationParameters())
runner.initializeRunner(model)
runner.addProbe('Real time matcher', LogoRealTimeMatcher(20.0))
runner.showView()    
```

Such as in the previous example, we want to observe the turtles and the marks.