A Logo simulation of a Langton’s ant. See http://en.wikipedia.org/wiki/Langton's_ant for more information.

Java implementation:
	The main class of this simulation model is defined in the 'TurmiteSimulationMain.java' file, located in the 'examples/turmite/src/java/fr/lgi2a/similar2logo/examples/turmite' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution :
		java -cp "lib/*" fr.lgi2a.similar2logo.examples.turmite.TurmiteSimulationMain

Groovy implementation:
	The main Groovy script of this simulation model is defined in the 'GroovyTurmiteSimulation.groovy' file, located in the 'examples/turmite/src/groovy/fr/lgi2a/similar2logo/examples/turmite' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution:
		groovy -cp "lib/*" examples/boids/src/groovy/fr/lgi2a/similar2logo/examples/turmite/GroovyTurmiteSimulation

Python implementation
		The main Python script of this simulation model is defined in the 'TurmiteSimulation.py' file, located in the 'examples/turmite/src/python/fr/lgi2a/similar2logo/examples/turmite' directory of the distribution.
		To run this simulation, use the following command from the root directory of the distribution:
		jython  -J-cp "lib/*" examples/turmite/src/python/fr/turmite/examples/passive/TurmiteSimulation.py
		
Ruby implementation:
	The main Ruby script of this simulation model is defined in the 'RubyTurmiteSimulation.rb' file, located in the 'examples/turmite/src/ruby/fr/lgi2a/similar2logo/examples/turmite' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution:
		jruby examples/turmite/src/ruby/fr/lgi2a/similar2logo/examples/turmite/RubyBoidsSimulation.rb
	Note that to run a simulation written in Ruby, you must install JRuby (http://jruby.org) on your system and that to load needed Java libraries, you must change the the second line of the script according to the location of your Similar2Logo installation.
		Dir["/Users/morvan/Logiciels/similar2logo/similar2logo-distribution/target/similar2logo-distribution-0.9-SNAPSHOT-bin/lib/*.jar"].each { |jar| require jar }		