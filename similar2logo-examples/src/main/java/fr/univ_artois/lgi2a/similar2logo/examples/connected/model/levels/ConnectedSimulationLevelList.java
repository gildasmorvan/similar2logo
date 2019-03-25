package fr.univ_artois.lgi2a.similar2logo.examples.connected.model.levels;

import fr.univ_artois.lgi2a.similar.microkernel.LevelIdentifier;

public class ConnectedSimulationLevelList  {

	/**
     * This constructor is unused since this class only defines static values.
     */
	private ConnectedSimulationLevelList() {
		//Does nothing
	}
	
	/**
	 * The identifiers of the "LOGO" levels.
	 */
	public static final LevelIdentifier[][] LOGO = {
		{ 
			new LevelIdentifier( "LOGO0" ),
			new LevelIdentifier( "LOGO1" ),
			new LevelIdentifier( "LOGO2" )
		},{
			new LevelIdentifier( "LOGO3" ),
			new LevelIdentifier( "LOGO4" ),
			new LevelIdentifier( "LOGO5" )
		},{
			new LevelIdentifier( "LOGO6" ),
			new LevelIdentifier( "LOGO7" ),
			new LevelIdentifier( "LOGO8" )
		}
	};
	
}
