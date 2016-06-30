package fr.lgi2a.similar2logo.examples.mecsyco.predation.tools;

import mecsycoscholar.application.ode.model.Equation;

public class LinearGrowthEquation extends Equation {

	private String symbol;
	
	public LinearGrowthEquation(String symbol, double X, double birthRate, double step) {
		super(new String[] { symbol }, new double[] { X }, new String[] { "birthRate" }, new double[] { birthRate }, step);
		this.symbol = symbol;
	}

	@Override
	public void dynamics() {
		double dx = (getParameters("birthRate") * getVariable(symbol));
		setVariable(symbol, dx * time_step);	
	}

}
