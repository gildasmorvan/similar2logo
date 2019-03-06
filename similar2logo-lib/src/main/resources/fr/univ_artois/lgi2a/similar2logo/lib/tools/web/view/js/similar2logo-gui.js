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
 * The javascript library of the Similar2Logo GUI
 * 
 * @author <a href="http://www.lgi2a.univ-artois.net/~morvan" target="_blank">Gildas Morvan</a>
 * @author <a href="http://www.yoannkubera.net" target="_blank">Yoann Kubera</a>
 * @author <a href="mailto:Antoine-Lecoutre@outlook.com>Antoine Lecoutre</a>
 *
 */


/**
 * Establishes the WebSocket connection and set up event handlers 
 */
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/webSocket");

/**
 * Displays the grid canvas in full screen.
 */
function fullScreen() {
    document.getElementById('grid_canvas').webkitRequestFullScreen();
    document.getElementById('grid_canvas').height = screen.availHeight;
    document.getElementById('grid_canvas').width = screen.availHeight;
}

/**
 * Displays the simulation data in a canvas identified as <code>grid_canvas</code>
 * 
 * @param data the simulation data.
 */
function drawCanvas(data) {
    var json = JSON.parse(data),
        canvas = document.getElementById('grid_canvas'),
        context = canvas.getContext('2d'),
        value,
        radius,
        centerX,
        centerY,
        i;
    context.clearRect(0, 0, canvas.width, canvas.height);
    if (json.hasOwnProperty('pheromones')) {
        for (i = 0; i < json.pheromones.length; i++) {
            centerX = json.pheromones[i].x * canvas.width;
            centerY = json.pheromones[i].y * canvas.height;
            radius = 5;

            if (json.pheromones[i].v < 255) {
                value = Math.floor(255 - (json.pheromones[i].v));
            } else {
                value = 0;
            }
            context.fillStyle = "rgb(" + 255 + "," + value + "," + value + ")";

            context.beginPath();
            context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
            context.fill();
        }
    }
    if (json.hasOwnProperty('marks')) {
        for (i = 0; i < json.marks.length; i++) {
            centerX = json.marks[i].x * canvas.width;
            centerY = json.marks[i].y * canvas.height;
            radius = 1;
            context.fillStyle = 'red';
            context.beginPath();
            context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
            context.fill();
        }
    }
    if (json.hasOwnProperty('agents')) {
        for (i = 0; i < json.agents.length; i++) {
            centerX = json.agents[i].x * canvas.width;
            centerY = json.agents[i].y * canvas.height;
            radius = 1;
            context.fillStyle = 'blue';
            context.beginPath();
            context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
            context.fill();
        }
    }

}

$(function () {
    $('[data-toggle=\popover\]').popover();
    if (document.getElementById('grid_canvas') !== null) {
        webSocket.onmessage = function (event) {
            drawCanvas(event.data);
        }
    }
});