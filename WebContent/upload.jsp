<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="site.css">
<title>File Upload</title>
</head>
<body>
	<div class="container">
		<form method="post" action="UploadServlet"
			enctype="multipart/form-data">
			<div class="row">
				<div class="col-md-3">
					<label for="apkFile.apk">Selecione o APK: </label>
				</div>
				<div class="col-md-3">
					<input type="file" name="apkFile.apk" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<label for="app.keystore">Selecione a chave do app: </label>
				</div>
				<div class="col-md-3">
					<input type="file" name="app.keystore" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<label for="pwd">Coloque aqui a senha da chave: </label>
				</div>
				<div class="col-md-3">
					<input type="password" name="pwd" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<label for="alias">Coloque aqui o alias da chave: </label>
				</div>
				<div class="col-md-3">
					<input type="text" name="alias" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<label for="scenario01.feature">Selecione o primeiro teste:
					</label>
				</div>
				<div class="col-md-3">
					<input type="file" name="scenario01.feature" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<label for="scenario02.feature">Selecione o segundo teste:
					</label>
				</div>
				<div class="col-md-3">
					<input type="file" name="scenario02.feature" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<label for="scenario03.feature">Selecione o terceiro teste:
					</label>
				</div>
				<div class="col-md-3">
					<input type="file" name="scenario03.feature" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-3">
					<label for="calabash_steps.rb">Selecione o aquivo dos
						passos: </label>
				</div>
				<div class="col-md-3">
					<input type="file" name="calabash_steps.rb" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-6">
					<div class="text-center">
						<input type="submit" class="text-center" value="Upload" />
					</div>
				</div>
			</div>
			<div id="devices"></div>
		</form>
		<!-- $("#devices").text(responseText); -->
	</div>
</body>
<script type="text/javascript" src="jquery-3.1.1.js"></script>
<script type="text/javascript">
	var init = function() {
		$.get("ListDevicesServlet", function(responseText) {
			var $table = $("<table>").appendTo($(devices));
			$("<thead>").appendTo($table).append($("<tr>")).append(
					$("<th colspan='6'>").text(
							"Dispositivos que serão testados"))
					.appendTo($table).append($("<tr>")).append(
							$("<th>").text("Serial")).append(
							$("<th>").text("Modelo do aparelho")).append(
							$("<th>").text("Nome do Aparelho")).append(
							$("<th>").text("Versão do SO")).append(
							$("<th>").text("Versão do SDK")).append(
							$("<th>").text("É emulador?"));
			$.each(JSON.parse(responseText), function(index, device) {
				$("<tr>").appendTo($table)
						.append($("<td>").text(device.serial)).append(
								$("<td>").text(device.sku)).append(
								$("<td>").text(device.display)).append(
								$("<td>").text(device.release)).append(
								$("<td>").text(device.version)).append(
								$("<td>").text(device.emulator));
			});
		});
	}
	$(document).ready(init);
</script>
</html>