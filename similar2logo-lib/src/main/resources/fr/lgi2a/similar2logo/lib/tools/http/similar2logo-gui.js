//Establish the WebSocket connection and set up event handlers
var webSocket = new WebSocket("ws://" + location.hostname + ":" + location.port + "/webSocket");

function initInterface() {
    $('#stopSimulation').prop('disabled', true);
    $('#pauseSimulation').prop('disabled', true);
}

function startSimulation() {
    $.get('start');
    $('#startSimulation').prop('disabled', true);
    $('#stopSimulation').prop('disabled', false);
    $('#pauseSimulation').prop('disabled', false);
}

function stopSimulation() {
    $.get('stop');
    $('#startSimulation').prop('disabled', false);
    $('#stopSimulation').prop('disabled', true);
    $('#pauseSimulation').prop('disabled', true);
}

function pauseSimulation() {
    $.get('pause');
    $('#startSimulation').prop('disabled', true);
    $('#stopSimulation').prop('disabled', false);
    $('#pauseSimulation').prop('disabled', false);
}

function updateNumericParameter(parameter) {
    var output = 'setParameter?' + parameter + '=' + $('#' + parameter).val();
    $.get(output);
}

function updateBooleanParameter(parameter) {
    var output = 'setParameter?' + parameter + '=' + $('#' + parameter).prop('checked');
    $.get(output);
}

function fullScreen() {
    document.getElementById('grid_canvas').webkitRequestFullScreen();
    document.getElementById('grid_canvas').height = screen.availHeight;
    document.getElementById('grid_canvas').width = screen.availHeight;
}

function drawCanvas(data) {
    var json = JSON.parse(data),
        canvas = document.getElementById('grid_canvas'),
        context = canvas.getContext('2d'),
        i = 0;
    context.clearRect(0, 0, canvas.width, canvas.height);
    if (json.hasOwnProperty('pheromones')) {
        for (i = 0; i < json.pheromones.length; i++) {
            var centerX = json.pheromones[i].x * canvas.width,
                centerY = json.pheromones[i].y * canvas.height,
                radius = 5;

            if (json.pheromones[i].v < 255) {
                var value = Math.floor(255 - (json.pheromones[i].v));
            } else {
                var value = 0;
            }
            context.fillStyle = "rgb(" + 255 + "," + value + "," + value + ")";

            context.beginPath();
            context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
            context.fill();
        }
    }
    if (json.hasOwnProperty('marks')) {
        for (i = 0; i < json.marks.length; i++) {
            var centerX = json.marks[i].x * canvas.width,
                centerY = json.marks[i].y * canvas.height,
                radius = 1;
            context.fillStyle = 'red';
            context.beginPath();
            context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
            context.fill();
        }
    }
    if (json.hasOwnProperty('agents')) {
        for (i = 0; i < json.agents.length; i++) {
            var centerX = json.agents[i].x * canvas.width,
                centerY = json.agents[i].y * canvas.height,
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