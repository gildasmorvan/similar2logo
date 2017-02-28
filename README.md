# Similar2Logo

similar2logo is a Logo-like multiagent-based simulation environment based on the [SIMILAR](http://www.lgi2a.univ-artois.fr/~morvan/similar.html) API under the [CeCILL-B license](http://cecill.info). 
This software defines an API to implement such simulations and provides usage examples.

## Contents of the README

* [License](#license)

* [Contributors](#contributors)

* [Documentation](#documentation)

	* [The technical architecture of Similar2Logo](#architecture)

	* [Compiling and running Similar2Logo](#compile)

	* [Develop your own agent-based models](#develop)

		* [Basic structure of a Similar2Logo simulation](#structure)

		* [Examples](#examples)

			* [A first example with a passive turtle](#passive)

			* [Adding a decision module to the turtles: The following turtles model](#following)


## <a name="license"></a> License

Similar2Logo is distributed under the [CeCILL-B license](http://cecill.info). In a few words, "if the initial program is under CeCILL-B, you can distribute your program under any license that you want (without the need to distribute the source code) provided you properly mention the use that you did of the initial program" (from the [CeCILL FAQ](http://www.cecill.info/faq.en.html#differences) ).

See the file  [LICENSE.txt](https://forge.univ-artois.fr/gildas.morvan/similar2logo/blob/master/LICENSE.txt) for more information. 

## <a name="contributors"></a> Contributors

Jonathan JULIEN - [mail](mailto:julienjnthn@gmail.com) - developer.

Yoann KUBERA - [mail](mailto:yoann.kubera@gmail.com) - [homepage](http://yoannkubera.net/) - designer of the SIMILAR API.

Antoine LECOUTRE - [mail](mailto:Antoine-Lecoutre@outlook.com) - developer.

Stéphane MEILLIEZ - [mail](mailto:stephane.meilliez@gmail.com) - developer.

Gildas MORVAN - [mail](mailto:gildas.morvan@univ-artois.fr) - [homepage](http://www.lgi2a.univ-artois.fr/~morvan/) - designer, developer.

## <a name="documentation"></a> Documentation

To understand the philosophy of Similar2Logo, it may be interesting to first look at the [SIMILAR documentation](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/README.html) and to read the papers about the [influences/reaction model](http://www.aaai.org/Papers/ICMAS/1996/ICMAS96-009.pdf) and the [IRM4S (Influence/Reaction Principle for Multi-Agent Based Simulation) model](http://www.aamas-conference.org/Proceedings/aamas07/html/pdf/AAMAS07_0179_07a7765250ef7c3551a9eb0f13b75a58.pdf).


### <a name="architecture"></a> The technical architecture of Similar2Logo

The following scheme presents the technical architecture of Similar2Logo.

![technical architecture of Similar2Logo](src/main/doc/img/similar2logoArchitecture.png)

* Similar2Logo runs on a web server based on the [Spark framework](http://sparkjava.com). By default its uses the `8080`port.

* The engine of Similar is in charge of the execution of the simulation.

* Users can interact with the simulation using a HTML5/css/js GUI based on [Bootstrap](http://getbootstrap.com).

* Similar2Logo uses [jQuery](http://jquery.com) to control (start/pause/stop/quit) and change the parameters of the simulations.

* The web server will push the simulation data to the client using the [websocket protocol](https://en.m.wikipedia.org/wiki/WebSocket) in [JSON](http://www.json.org).


### <a name="compile"></a> Compiling and running Similar2Logo

#### Using the binary distribution

A binary distribution of Similar2Logo can be downloaded at [this address](http://www.lgi2a.univ-artois.fr/~morvan/similar.html). It contains all the needed libraries and some simulation examples. This is probably the easiest way to start using Similar2Logo.

#### Compiling Similar2Logo from the git repository.


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


#### Running Similar2Logo

When you launch a Similar2Logo simulation, your browser should open a page that looks like this.

![GUI of Similar2Logo. Boids example](src/main/doc/img/boids-example.png)


* You can change the parameters of the simulation using the panel on the left. When you hover on a parameter, a description of it should appear.

* You can control the simulation execution (start/stop/pause/quit) using the buttons on the upper right.

* The simulation will be displayed in the center of the web page. By default, it will display the agents, marks and pheromone fields but you can add the visualization you want, for instance, the prey/predator simulation will display the population of preys, predators and grass in a chart.

![GUI of Similar2Logo. predation example](src/main/doc/img/predation-example.png)


### <a name="develop"></a> Develop your own multiagent-based simulations

#### <a name="structure"></a> Basic structure of a Similar2Logo simulation

A typical Similar2Logo simulation will contain the following components:

* The parameters of the simulation in a class that inherits from [LogoSimulationParameters](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/LogoSimulationParameters.html).

* An environment. By default it is a 2D grid on which agents, marks and pheromones will be located and interact. It is implemented in the [LogoEnvPLS](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/environment/LogoEnvPLS.html) class. Following the IRM4S model, the environment has its own dynamics, which means that it can emit influences. By default, the environment emits to influences at each step:

	* [AgentPositionUpdate](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/AgentPositionUpdate.html) which updates the position of agents according to their dynamics (speed, acceleration and direction),

	* [PheromoneFieldUpdate](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/PheromoneFieldUpdate.html) which updates the pheromone fields.

* Agent models. In Similar2Logo, following the IRM4S model, an agent has

	* A state, defined by a class that inherits from [TurtlePLSInLogo](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/agents/turtle/TurtlePLSInLogo.html).

	* A perception model. By default, it is implemented in the [TurtlePerceptionModel](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/lib/agents/perception/TurtlePerceptionModel.html) class, but you can define your own perception model if needed.

	* A decision model that will defines how the agent produces influences. It is implemented in a class that inherits from [AbstractAgtDecisionModel](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/api/fr/lgi2a/similar/extendedkernel/libs/abstractimpl/AbstractAgtDecisionModel.html).

* A set of influences that an agent can produce. By default, the following influences can be used, but you can define your own influences if needed:

	* [ChangeAcceleration] (http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/ChangeAcceleration.html): an influence that aims at changing the acceleration of an agent.

	* [ChangeDirection](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/ChangeDirection.html): an influence that aims at changing the direction of an agent.

	* [ChangePosition](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/ChangePosition.html): an influence that aims at changing the position of an agent.

	* [ChangeSpeed](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/ChangeSpeed.html):  an influence that aims at changing the speed of an agent.

	* [DropMark](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/DropMark.html): an influence that aims at dropping a mark at a given location.

	* [EmitPheromone](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/EmitPheromone.html): an influence that aims at emitting a pheromone at given location.

	* [RemoveMark](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/RemoveMark.html): an influence that aims at removing a mark from the environment.

	* [RemoveMarks](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/RemoveMarks.html): an influence that aims at removing marks from the environment.

	* [Stop](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/influences/Stop.html): an influence that aims at stopping an agent.

	* [SystemInfluenceAddAgent](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/api/fr/lgi2a/similar/microkernel/influences/system/SystemInfluenceAddAgent.html): Adds an agent to the simulation.

	* [SystemInfluenceRemoveAgent](http://www.lgi2a.univ-artois.fr/~morvan/similar/docs/api/fr/lgi2a/similar/microkernel/influences/system/SystemInfluenceRemoveAgent.html): Removes an agent from the simulation.

* A reaction model which describes how influences are handled to compute the next simulation state. A default reaction model is implemented in the [LogoDefaultReactionModel](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/levels/LogoDefaultReactionModel.html), but you can define your own reaction model if needed.

* A simulation model that defines the initial state of the simulation. It is implemented in a class that inherits from [LogoSimulationModel](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/initializations/LogoSimulationModel.html).

* A Main class that run the web server.


#### <a name="examples"></a> Examples

In the following we comment the examples distributed with Similar2Logo. Each example introduces a specific feature of Similar2Logo.

* [A first example with a passive turtle](#passive)

* [Adding a decision module to the turtles: The following turtles model](#following)

##### <a name="passive"></a> A first example with a passive turtle

First we consider a simple example with a single passive agent. The example source code is located in the package `fr.lgi2a.similar2logo.examples.passive`. Following the structure of a SIMILAR simulation, it contains 2 packages:

* `fr.lgi2a.similar2logo.examples.passive.model` containing the class that defines the parameters of the model: `PassiveTurtleSimulationParameters`. This class inherits from `fr.lgi2a.similar2logo.kernel.model.LogoSimulationParameters`.
* `fr.lgi2a.similar2logo.examples.passive.initializations`, containing the simulation model class `PassiveTurtleSimulationModel`. This class inherits from `fr.lgi2a.similar2logo.kernel.initializations.LogoSimulationModel`.

#### The model parameters

The class [LogoSimulationParameters](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/model/LogoSimulationParameters.html) defines the generic parameters of a Logo-like simulation (environment size, topology, etc.). The class  `PassiveTurtleSimulationParameters` contains the parameters specific to the passive turtle model:


```
	/**
	 * The initial position of the turtle on the x axis.
	 */
	public double initialX;
	
	/**
	 * The initial position of the turtle on the y axis.
	 */
	public double initialY;
	
	/**
	 * The initial speed of the turtle.
	 */
	public double initialSpeed;
	
	/**
	 * The initial acceleration of the turtle.
	 */
	public double initialAcceleration;
	
	/**
	 * The initial direction of the turtle.
	 */
	public double initialDirection;

```

#### The simulation model

The class [LogoSimulationModel](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/kernel/initializations/LogoSimulationModel.html) defines a generic simulation model of a Similar2Logo simulation. The modeler must implement the method `generateAgents` to describe the initial state of the passive turtle of our simulation. 

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

#### The Main class

In the main class, the simulation model is instantiated as well as some probes to observe the simulation:

* `ProbeExceptionPrinter` prints on the standard output the trace of exceptions that are thrown during the execution of the simulation,
* `ProbeExecutionTracker` tracks the execution of the simulation and prints notification messages in a stream printer,
* `ProbeImageSwingJFrame` displays the content of the simulation in a [JFrame](http://docs.oracle.com/javase/7/docs/api/javax/swing/JFrame.html), using an image defined in a [GridSwingView](http://www.lgi2a.univ-artois.fr/~morvan/similar2logo/docs/api/fr/lgi2a/similar2logo/lib/probes/GridSwingView.html) probe,
* `LogoRealTimeMatcher` slows down the simulation so that its execution speed matches a specific factor of N steps per second.

Then the simulation model is run.


```
		// Create the parameters used in this simulation.
		PassiveTurtleSimulationParameters parameters = new PassiveTurtleSimulationParameters();
		parameters.initialTime = new SimulationTimeStamp( 0 );
		parameters.finalTime = new SimulationTimeStamp( 3000 );
		parameters.initialSpeed = 0.1;
		parameters.initialDirection = LogoEnvPLS.NORTH_EAST;
		parameters.xTorus = true;
		parameters.yTorus = true;
		parameters.gridHeight = 20;
		parameters.gridWidth = 40;
		// Register the parameters to the agent factories.
		TurtleFactory.setParameters( parameters );
		// Create the simulation engine that will run simulations
		ISimulationEngine engine = new EngineMonothreadedDefaultdisambiguation( );
		// Create the probes that will listen to the execution of the simulation.
		engine.addProbe( 
			"Error printer", 
			new ProbeExceptionPrinter( )
		);
		engine.addProbe(
			"Trace printer", 
			new ProbeExecutionTracker( System.err, false )
		);
		engine.addProbe(
			"Swing view",
			new ProbeImageSwingJFrame( 
				"Logo level",
				new GridSwingView(
					Color.WHITE,
					true
				)
			)
		);
		
		engine.addProbe(
			"Real time matcher", 
			new LogoRealTimeMatcher(100)
		);

		// Create the simulation model being used.
		PassiveTurtleSimulationModel simulationModel = new PassiveTurtleSimulationModel(
			parameters
		);
		// Run the simulation.
		engine.runNewSimulation( simulationModel );
```

### <a name="following"></a> Adding a decision module to the turtles: The following turtles model

We consider a model with agents that aim at following themselves. The example source code is located in the package `fr.lgi2a.similar2logo.examples.following`. Following the structure of a SIMILAR simulation, the decision module of the following turtles is located in the class `fr.lgi2a.similar2logo.examples.agents.FollowingTurtleDecisionModel`.

#### The behavior of turtles 

The decision module consists in changing the direction and speed of the turtle to follow the closest turtle.

```
	public void decide(
			SimulationTimeStamp timeLowerBound,
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
			double directionTo = castedPublicLocalState.getDirection();
			double distanceTo = Double.MAX_VALUE;
			TurtlePLSInLogo leader = castedPublicLocalState;
			for(LocalPerceivedData<TurtlePLSInLogo> turtle : castedPerceivedData.getTurtles()) {
				if(turtle.getDistanceTo() < distanceTo) {
					directionTo = turtle.getDirectionTo();
					distanceTo = turtle.getDistanceTo();
					leader = turtle.getContent();
				}
			}
			
			producedInfluences.add(
				new ChangeDirection(
					timeLowerBound,
					timeUpperBound,
					directionTo - castedPublicLocalState.getDirection(),
					castedPublicLocalState
				)
			);
			producedInfluences.add(
				new ChangeSpeed(
					timeLowerBound,
					timeUpperBound,
					leader.getSpeed() - castedPublicLocalState.getSpeed(),
					castedPublicLocalState
				)
			);
		}
	}

```

#### The parameters of the simulation

The parameter class contains the following parameters:

```
	/**
	 * The maximal initial speed of turtles.
	 */
	public double maxInitialSpeed;
	
	/**
	 * The perception angle of turtles.
	 */
	public double perceptionAngle;
	
	/**
	 * The perception distance of turtles.
	 */
	public double perceptionDistance;
	
	/**
	 * The number of agents in the simulation.
	 */
	public int nbOfAgents;
```

#### The simulation model

The simulation model is located in the class `fr.lgi2a.similar2logo.examples.following.initializations.FollowingAgentsSimulationModel` and consists in creating `nbOfAgents` turtles with the decision module `FollowingTurtleDecisionModel` at a random position in the environment. The random number generation is provided by the class `RandomValueFactory` which uses the Java [SecureRandom](http://docs.oracle.com/javase/7/docs/api/java/security/SecureRandom.html) implementation by default.

```
	protected AgentInitializationData generateAgents(
			ISimulationParameters parameters, Map<LevelIdentifier, ILevel> levels) {
		FollowingAgentsSimulationParameters castedParameters = (FollowingAgentsSimulationParameters) parameters;
		AgentInitializationData result = new AgentInitializationData();
		for(int i = 0; i < castedParameters.nbOfAgents; i++) {
			IAgent4Engine turtle = TurtleFactory.generate(
				new TurtlePerceptionModel(castedParameters.perceptionDistance, castedParameters.perceptionAngle, true, false, false),
				new FollowingTurtleDecisionModel(),
				new AgentCategory("follower", TurtleAgentCategory.CATEGORY),
				RandomValueFactory.getStrategy().randomDouble()*2*Math.PI,
				RandomValueFactory.getStrategy().randomDouble()*castedParameters.maxInitialSpeed,
				0,
				RandomValueFactory.getStrategy().randomDouble()*castedParameters.gridWidth,
				RandomValueFactory.getStrategy().randomDouble()*castedParameters.gridHeight
			);
			result.getAgents().add( turtle );
		}
		return result;
	}
```

#### The Main class

The main class is very similar to the previous example. Only the simulation model has been changed.

### Changing the default reaction model: the multi-turmite simulation

The goal of this example is to implement the multi-turmite model proposed by [N. Fatès](http://www.loria.fr/~fates/) and [V. Chevrier](http://www.loria.fr/~chevrier/) in [this paper](http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf). It extends the traditional [Langton's ant model](http://en.wikipedia.org/wiki/Langton%27s_ant) by specifying what happens when conflicting influences (removing or dropping a mark to the same location) are detected. The following policy is applied:

* if the parameter `dropMark` is `true`, the dropping influence takes precedent over the removing one and reciprocally.
* if the parameter `removeDirectionChange` is `true`, direction changes are not taken into account.

It allows to define 4 different reaction models according to these parameters.

#### The reaction model

In the previous example, the influence management relies on the default reaction model defined in the class `fr.lgi2a.similar2logo.kernel.model.levels.LogoDefaultReactionModel`. Now, we want to handle some influences manually. To do so, we have to define a class `fr.lgi2a.similar2logo.examples.multiturmite.model.level.MultiTurmiteReactionModel that inherits from `LogoDefaultReactionModel`. This class has then two attributes:

```
	/**
	 * <code>true</code> if direction changes are not taken into account
	 * when two turtles want to modify the same patch.
	 */
	private final boolean removeDirectionChange;

	/**
	 * <code>true</code> if the output of turtle actions is inversed
	 * when two turtles want to modify the same patch.
	 */
	private final boolean inverseMarkUpdate;

```

What we have to do is changing the behavior of the `makeRegularReaction` method. A generic stub of a specific reaction model is given below:

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

In this case, specific influences represents collisions between turtle decisions. We define a class `MultiTurmiteCollision` that explicitly represent possible collisions for each location.

```
public class MultiTurmiteCollision {

	private Set<DropMark> dropMarks ;
	private Set<RemoveMark> removeMarks;
	private Set<ChangeDirection> changeDirections;
	
	/**
	 * 
	 */
	public MultiTurmiteCollision() {
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
Then, it is easy to implement the reaction model if influences are colliding or not:
```
public void makeRegularReaction(SimulationTimeStamp transitoryTimeMin,
			SimulationTimeStamp transitoryTimeMax,
			ConsistentPublicLocalDynamicState consistentState,
			Set<IInfluence> regularInfluencesOftransitoryStateDynamics,
			InfluencesMap remainingInfluences) {
		Set<IInfluence> nonSpecificInfluences = new LinkedHashSet<IInfluence>();
		Map<Point2D,MultiTurmiteCollision> collisions = new LinkedHashMap<Point2D,MultiTurmiteCollision>();
		
		//Organize influences by location and type
		for(IInfluence influence : regularInfluencesOftransitoryStateDynamics) {
			if(influence.getCategory().equals(DropMark.CATEGORY)) {
				DropMark castedDropInfluence = (DropMark) influence;
				if(!collisions.containsKey(castedDropInfluence.getMark().getLocation())) {
					collisions.put(
						castedDropInfluence.getMark().getLocation(),
						new MultiTurmiteCollision()
					);
				} 
				collisions.get(castedDropInfluence.getMark().getLocation()).getDropMarks().add(castedDropInfluence);
	
			} else if(influence.getCategory().equals(RemoveMark.CATEGORY)) {
				RemoveMark castedRemoveInfluence = (RemoveMark) influence;
				if(!collisions.containsKey(castedRemoveInfluence.getMark().getLocation())) {
					collisions.put(
						castedRemoveInfluence.getMark().getLocation(),
						new MultiTurmiteCollision()
					);
				}
				collisions.get(castedRemoveInfluence.getMark().getLocation()).getRemoveMarks().add(castedRemoveInfluence);
			} else if(influence.getCategory().equals(ChangeDirection.CATEGORY)) {
				ChangeDirection castedChangeDirectionInfluence = (ChangeDirection) influence;
				if(!collisions.containsKey(castedChangeDirectionInfluence.getTarget().getLocation())) {
					collisions.put(
						castedChangeDirectionInfluence.getTarget().getLocation(),
						new MultiTurmiteCollision()
					);
				}
				collisions.get(castedChangeDirectionInfluence.getTarget().getLocation()).getChangeDirections().add(castedChangeDirectionInfluence);
			} else {
				nonSpecificInfluences.add(influence);
			}
		}
	
		for(Map.Entry<Point2D, MultiTurmiteCollision> collision : collisions.entrySet()) {
			if(collision.getValue().isColliding()) {
				if(collision.getValue().getDropMarks().size() > 1 && !this.inverseMarkUpdate) {
					nonSpecificInfluences.add(
						collision.getValue().getDropMarks().iterator().next()
					);
				} if(collision.getValue().getRemoveMarks().size() > 1 && !this.inverseMarkUpdate)  {
					nonSpecificInfluences.add(
						collision.getValue().getRemoveMarks().iterator().next()
					);
				}
				
				if(!this.removeDirectionChange) {
					nonSpecificInfluences.addAll(collision.getValue().getChangeDirections());
				}
			} else {
				nonSpecificInfluences.addAll(collision.getValue().getChangeDirections());
				nonSpecificInfluences.addAll(collision.getValue().getDropMarks());
				nonSpecificInfluences.addAll(collision.getValue().getRemoveMarks());
			}
		}
		
		super.makeRegularReaction(transitoryTimeMin, transitoryTimeMax, consistentState, nonSpecificInfluences, remainingInfluences);
	}
```
#### The simulation model

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


#### The Main class

The main class is very similar to the previous examples.

### Adding a pheromone: Heatbugs

"Heatbugs is an abstract model of the behavior of biologically-inspired agents that attempt to maintain an optimum temperature around themselves. It demonstrates how simple rules defining the behavior of agents can produce several different kinds of emergent behavior.

Heatbugs has been used as a demonstration model for many agent-based modeling toolkits." from [the Heatbugs page](http://ccl.northwestern.edu/netlogo/models/Heatbugs)  of the NetLogo documentation. 

This example illustrates how to add a pheromone field in a similar2logo simulation and how it can be used by turtles.

#### The parameters of the simulation

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

#### The decision model

