package fr.univ_artois.lgi2a.similar2logo.examples.projettut.model;

import fr.univ_artois.lgi2a.similar.extendedkernel.libs.abstractimpl.AbstractAgtDecisionModel;
import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;
import fr.univ_artois.lgi2a.similar.microkernel.SimulationTimeStamp;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.agents.turtle.TurtlePLSInLogo;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.influences.ChangeAcceleration;
import fr.univ_artois.lgi2a.similar2logo.kernel.model.levels.LogoSimulationLevelList;

public class AbstractLogoDecisionModel extends AbstractAgtDecisionModel {

	public AbstractLogoDecisionModel(LevelIdentifier levelIdentifier) {
		super(LogoSimulationLevelList.LOGO);
	}
	

}
