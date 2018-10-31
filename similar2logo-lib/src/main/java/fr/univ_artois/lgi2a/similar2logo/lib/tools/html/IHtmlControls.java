package fr.univ_artois.lgi2a.similar2logo.lib.tools.html;

/**
 * The parent interface of all the classes applying modifications to the HTML 
 * web view of Similar2Logo.
 * 
 * @author <a href="http://www.lgi2a.univ-artois.fr/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 */
public interface IHtmlControls {
	/**
	 * Sets whether the start button is enabled or not in the view.
	 * @param active 	<code>true</code> if the start button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	void setStartButtonState(boolean active);

	/**
	 * Sets whether the pause button is enabled or not in the view.
	 * @param active 	<code>true</code> if the pause button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	void setPauseButtonState(boolean active);

	/**
	 * Sets whether the abort button is enabled or not in the view.
	 * @param active 	<code>true</code> if the pause button has to be interactive. 
	 * 					<code>false</code> if it has to be deactivated in the view.
	 */
	void setAbortButtonState(boolean active);
	
	/**
	 * Requests the view to shut down.
	 */
	void shutDownView();
}