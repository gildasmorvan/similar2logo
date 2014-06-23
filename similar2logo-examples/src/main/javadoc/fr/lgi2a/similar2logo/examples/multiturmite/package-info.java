/**
 * Copyright or © or Copr. LGI2A
 * 
 * LGI2A - Laboratoire de Genie Informatique et d'Automatique de l'Artois - EA 3926 
 * Faculte des Sciences Appliquees
 * Technoparc Futura
 * 62400 - BETHUNE Cedex
 * http://www.lgi2a.univ-artois.fr/
 * 
 * Email: gildas.morvan@univ-artois.fr
 * 
 * Contributors:
 * 	Gildas MORVAN (creator of the IRM4MLS formalism)
 * 	Yoann KUBERA (designer, architect and developer of SIMILAR)
 * 
 * This software is a computer program whose purpose is to support the 
 * implementation of Logo-like simulations using the SIMILAR API.
 * This software defines an API to implement such simulations, and also 
 * provides usage examples.
 * 
 * This software is governed by the CeCILL-B license under French law and
 * abiding by the rules of distribution of free software.  You can  use, 
 * modify and/ or redistribute the software under the terms of the CeCILL-B
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". 
 * 
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability. 
 * 
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or 
 * data to be ensured and,  more generally, to use and operate it in the 
 * same conditions as regards security. 
 * 
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-B license and that you accept its terms.
 */
/**
 * A Logo simulation multiple Langton’s ants. The model is inspired by this paper: 
 * <a href="http://www.ifaamas.org/Proceedings/aamas2010/pdf/01%20Full%20Papers/11_04_FP_0210.pdf">
 * Fatès N. and V. Chevrier. How important are updating schemes in multi-agent systems?
 * An illustration on a multi-turmite model. Proceedings of AAMAS' 10, Toronto (Canada),
 * May 2010, p. 533-540
 * </a>
 * When conflicting turmite influences (removing or dropping a mark to the same location) are detected,
 * a specific policy is applied:
 * <ul>
 * <li> if the parameter dropMark is true, the dropping influence takes precedent over the removing one
 * and reciprocally. </li>
 * <li> if the parameter removeDirectionChange is true, direction changes are not taken into account. </li>
 * </ul>
 * <ul>
 * <li> {@link TwoTurmitesSimulationMain} defines a simple instance of the multi-turmite model with two
 * turtles located at (x,y) and (x,y+1) and heading north. This simulation results in a growing squares.
 * There are no collision in this simulation. Thus, parameters inverseMarkUpdate and removeDirectionChange
 * have no effect.</li>
 * <li> {@link FourTurmitesSimulationMain} defines an instance of the multi-turmite model with four turtles.
 * This simulation results different cyclic or environment-filling behaviors according to the values of
 * parameters inverseMarkUpdate and removeDirectionChange.</li>
 * </ul>
 * 
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 *
 */
package fr.lgi2a.similar2logo.examples.multiturmite;