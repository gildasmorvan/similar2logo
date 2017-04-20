A Logo simulation of the Boids model. See http://en.wikipedia.org/wiki/Boids for more information.

Java implementation:
	The main class of this simulation model is defined in the 'BoidsSimulationMain.java' file, located in the 'examples/boids/src/java/fr/lgi2a/similar2logo/examples/boids' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution:
		java -cp "lib/*" fr.lgi2a.similar2logo.examples.boids.BoidsSimulationMain

Groovy implementation:
	The main Groovy script of this simulation model is defined in the 'GroovyBoidsSimulation.groovy' file, located in the 'examples/boids/src/groovy/fr/lgi2a/similar2logo/examples/boids' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution:
		groovy -cp "lib/*" examples/boids/src/groovy/fr/lgi2a/similar2logo/examples/boids/GroovyBoidsSimulation
		
Ruby implementation:
	The main Ruby script of this simulation model is defined in the 'RubyBoidsSimulation.rb' file, located in the 'examples/boids/src/ruby/fr/lgi2a/similar2logo/examples/boids' directory of the distribution.
	To run this simulation, use the following command from the root directory of the distribution:
		jruby examples/boids/src/ruby/fr/lgi2a/similar2logo/examples/boids/RubyBoidsSimulation.rb
	Note that to run a simulation written in Ruby, you must install JRuby (http://jruby.org) on your system and that to load needed Java libraries, you must change the the second line of the script according to the location of your Similar2Logo installation.
		Dir["/Users/morvan/Logiciels/similar2logo/similar2logo-distribution/target/similar2logo-distribution-0.9-SNAPSHOT-bin/lib/*.jar"].each { |jar| require jar }