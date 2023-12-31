<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f0f8f7; /* Verde suave pastel */
	margin: 0;
	padding: 0;
}

h1 {
	background-color: #5bc0be; /* Color de encabezado */
	color: white;
	padding: 20px;
	margin: 0;
}

h2 {
	text-align: center;
}

label {
	display: block;
	margin: 10px 0;
}

textarea {
	width: 60%;
	padding: 10px;
}

button {
	background-color: #5bc0be; /* Color de bot�n */
	color: white;
	border: none;
	padding: 10px 20px;
	margin: 10px 0;
	cursor: pointer;
}

button:hover {
	background-color: #449b98; /* Color de bot�n al pasar el rat�n */
}

select {
	padding: 10px;
	width: 100%;
	margin: 10px 0;
}
</style>
</head>
    <%
String usuario = (String) request.getSession().getAttribute("sesion");
if (usuario == null) {
	
	%>
	<script type="text/javascript">
	    window.close();
	</script>
	<%
	}

	%>
	
	<%
String identificador = request.getParameter("id");
%>
<body>
	<h1>Detalle y soluci�n de Ticket</h1>
	<h2>
		Ticket No:
		<%=identificador%></h2>

<a href="AtencionTicket.jsp">Regresar</a>
	<form id="frm_atender">
		<input name="ticket" id="ticket" value="<%=identificador%>"
			hidden="true"> <input name="usuario" id="usuario"
			value="<%=usuario%>" hidden="true">
		<button name="acciones" value="Atender" id="acciones" type="button"
			onclick="enviarFormulario()">Atender Ticket</button>
		<!-- Cambio el id y name -->
	</form>
	<div id="resultado"></div>

	<script>
		function enviarFormulario() {

			var ticket = encodeURIComponent(document.getElementById("ticket").value);
			var usuario = encodeURIComponent(document.getElementById("usuario").value);
			var atenderButtonValue = encodeURIComponent(document
					.getElementById("acciones").value);
			var url = "Estados?ticket=" + ticket + "&usuario=" + usuario
					+ "&acciones=" + atenderButtonValue;

			var xmlhttp = new XMLHttpRequest();
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState === 4 && xmlhttp.status === 200) {
					document.getElementById("resultado").innerHTML = xmlhttp.responseText;
				}
			};
			xmlhttp.open("POST", url, true);
			xmlhttp.send();
		}
	</script>


	<form action="Estados" method="get">
		<input name="ticket" id="ticket" value="<%=identificador%>"
			hidden="true"> <input name="usuario" id="usuario"
			value="<%=usuario%>" hidden="true"> 

		<label>Ingrese su comentario</label>
		<textarea rows="5" cols="15" name="comentario"></textarea>
		<br>
		<button name="acciones" id="acciones" value="Finalizar">Finalizar</button>
	</form>


	<form action="Estados" method="get">
		<label>Indique area a trasladar ticket</label> 
		<select name="area" id="area">
			<option value="1">Contabilidad</option>
			<option value="2">Cobros</option>
			<option value="3">Prestaciones</option>
		</select> 
		<label>Motivo de traslado</label>
		<textarea rows="5" cols="15" name="motivo_traslado" id="motivo_traslado"></textarea>
		<input name="ticket" id="ticket" value="<%=identificador%>" hidden="true">
		<input name="usuario" id="usuario" value="<%=usuario%>" hidden="true"> 
		<br>

		<button name="acciones" id="acciones" value="Traslado">Trasladar</button>
	</form>
</body>
</html>
