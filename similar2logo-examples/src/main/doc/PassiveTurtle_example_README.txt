A Logo simulation with a passive turtle.

Java implementation:
	The main class of this simulation model is defined in the 'PassiveSimulationMain.java' file, located in the 'examples/passive/src/java/fr/lgi2a/similar2logo/examples/passive' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution:
		java -cp "lib/*" fr.lgi2a.similar2logo.examples.passive.PassiveSimulationMain

Groovy implementation:
	The main Groovy script of this simulation model is defined in the 'GroovyPassiveSimulation.groovy' file, located in the 'examples/passive/src/groovy/fr/lgi2a/similar2logo/examples/passive' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution:
		groovy -cp "lib/*" examples/passive/src/groovy/fr/lgi2a/similar2logo/examples/passive/GroovyPassiveSimulation

Python implementation
		The main Python script of this simulation model is defined in the 'PassiveTurtleSimulation.py' file, located in the 'examples/boids/src/python/fr/lgi2a/similar2logo/examples/passive' directory of the distribution.
		To run this simulation, use the following command from the root directory of the distribution:
		jython  -J-cp "lib/*" examples/passive/src/python/fr/passive/examples/passive/PassiveTurtleSimulation.py
				
		
Ruby implementation:
	The main Ruby script of this simulation model is defined in the 'RubyPassiveSimulation.rb' file, located in the 'examples/passive/src/ruby/fr/lgi2a/similar2logo/examples/passive' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution:
		jruby examples/passive/src/ruby/fr/lgi2a/similar2logo/examples/passive/RubyPassiveSimulation.rb
	Note that to run a simulation written in Ruby, you must install JRuby (http://jruby.org) on your system and that to load needed Java libraries, you must change the the second line of the script according to the location of your Similar2Logo installation.
		Dir["/Users/morvan/Logiciels/similar2logo/similar2logo-distribution/target/similar2logo-distribution-0.9-SNAPSHOT-bin/lib/*.jar"].each { |jar| require jar }