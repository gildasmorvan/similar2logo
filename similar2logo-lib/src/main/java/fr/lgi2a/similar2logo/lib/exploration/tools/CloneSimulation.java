package fr.lgi2a.similar2logo.lib.exploration.tools;

import java.util.HashSet;
import java.util.Set;

import fr.lgi2a.similar.microkernel.agents.IAgent4Engine;
import fr.lgi2a.similar.microkernel.environment.IEnvironment4Engine;
import fr.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.lgi2a.similar2logo.kernel.model.environment.LogoEnvPLS;

public class CloneSimulation {

	public static Set<IAgent4Engine> cloneAgents (SimulationData data) {
		Set<IAgent4Engine> newAgents = new HashSet<>();
		Set<TurtlePLSInLogo> oldAgents = data.getAgents();
		for (TurtlePLSInLogo a : oldAgents) {
			newAgents.add(generateClonedTurtle ((TurtlePLSInLogo) a.clone()));
		}
		return newAgents;
	}
	
	public static IEnvironment4Engine cloneEnvironment (SimulationData data) {
		LogoEnvPLS oldEnvironment = data.getEnvironment();
		IEnvironment4Engine ev = (IEnvironment4Engine) oldEnvironment.clone();
		System.out.println(ev.toString());
		return (IEnvironment4Engine) oldEnvironment.clone();
	}
	
	private static IAgent4Engine generateClonedTurtle (TurtlePLSInLogo turtle) {
		return turtle.getOwner();
	}
}
