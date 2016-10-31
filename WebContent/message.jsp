<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<title>File Upload</title>
</head>
<body onload="getMessages();">
	<div class="container">
		<div>
			<div class="text-center">
				<h2>${requestScope.message}</h2>
			</div>
		</div>
		<div>
			<form>
				<div class="text-center">
					<input type="button" onClick="postMessage()"
						value="Começar os testes" />
				</div>
			</form>
		</div>
		<div id="content"></div>
	</div>
</body>
<script type="text/javascript" src="jquery-3.1.1.js"></script>
<script type="text/javascript">
	function postMessage() {
		var xmlhttp = new XMLHttpRequest();
		var dir = '${dir}';
		var alias = '${alias}';
		var password = '${password}';
		$.post()
		xmlhttp.open("POST", "TestServlet", false);
		xmlhttp.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded");
		xmlhttp
				.send("dir=" + dir + "&alias=" + alias + "&password="
						+ password);
	};
	var messagesWaiting = false;
	function getMessages() {
		if (!messagesWaiting) {
			messagesWaiting = true;
			var xmlhttp = new XMLHttpRequest();
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					messagesWaiting = false;
					$(content).append("<p>" + xmlhttp.responseText + "</p>");
					getMessages();
				}
			}
			xmlhttp.open("GET", "TestServlet", true);
			xmlhttp.send();
		}
	}
</script>
</html>