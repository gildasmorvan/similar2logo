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
	$.get( 'stop' );
	$('#startSimulation').prop('disabled', false);
	$('#stopSimulation').prop('disabled', true);
	$('#pauseSimulation').prop('disabled', true);
}

function pauseSimulation() {
	$.get( 'pause' );
	$('#startSimulation').prop('disabled', true);
	$('#stopSimulation').prop('disabled', false);
	$('#pauseSimulation').prop('disabled', false);
}

function updateNumericParameter(parameter) {
	var output = 'setParameter?' + parameter + '=' + $('#' + parameter).val();
	$.get(output);
}

function updateBooleanParameter(parameter){
	var output = 'setParameter?' + parameter + '=' + $('#' + parameter).prop('checked');
	$.get(output);
}

function fullScreen() {
	document.getElementById('grid_canvas').webkitRequestFullScreen();
	document.getElementById('grid_canvas').height = screen.availHeight;
	document.getElementById('grid_canvas').width = screen.availHeight;
}

//$(document).ready(
//	function(){
//		$('[data-toggle=\popover\]').popover();
//	}
//);
$(function(){
   $('[data-toggle=\popover\]').popover(); 
});
$(document).ready(
	function () {
		function drawCanvas() {
			$.ajax({url: 'grid', dataType: 'text', success: function(data) {
				var json = JSON.parse(data),
                    canvas = document.getElementById('grid_canvas'),
				    context = canvas.getContext('2d');
				context.clearRect(0, 0, canvas.width, canvas.height);
				for (var i = 0; i < json.agents.length; i++) {
					var centerX = json.agents[i].x*canvas.width,
					    centerY = json.agents[i].y*canvas.height,
					    radius = 1;			
					context.fillStyle = 'blue';
					context.beginPath();
					context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
					context.fill();
				}
				for (var i = 0; i < json.marks.length; i++) {
					var centerX = json.marks[i].x*canvas.width,
                        centerY = json.marks[i].y*canvas.height,
                        radius = 1;			
					context.fillStyle = 'red';
					context.beginPath();
					context.arc(centerX, centerY, radius, 0, 2 * Math.PI, false);
					context.fill();
				}
			}});
		}
		setInterval(function() {drawCanvas();}, 20);
	}
);