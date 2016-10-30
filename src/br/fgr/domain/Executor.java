package br.fgr.domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;

public class Executor {

	private String path;

	public Executor(String path) {
		this.path = path;
		System.out.println(path);
	}

	// TODO: Example, remove when finishes.
	public void execute() {
		try {
			System.out.println(path);
			StringBuffer sb = new StringBuffer();
			Process p = Runtime.getRuntime().exec(String.format("pwd", path));
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createScripts(String pathScript, String location, String alias, String password) {
		try {
			// --------------------------------------------------------------------------------
			List<String> genContent = Arrays.asList("echo -ne '\\n' | calabash-android gen",
					"rm -f features/my_first.feature");
			Path genFile = Paths.get(pathScript + "/gen.sh");
			Files.write(genFile, genContent, Charset.forName("UTF-8"));

			StringBuffer sb1 = new StringBuffer();
			sb1.append("mv scenario01.feature features/scenario01.feature");
			sb1.append(" | ");
			sb1.append("mv scenario02.feature features/scenario02.feature");
			sb1.append(" | ");
			sb1.append("mv scenario03.feature features/scenario03.feature");
			sb1.append(" | ");
			sb1.append("mv calabash_steps.rb features/step_definitions/calabash_steps.rb");
			List<String> moveContent = Arrays.asList(sb1.toString());
			Path moveFile = Paths.get(pathScript + "/move.sh");
			Files.write(moveFile, moveContent, Charset.forName("UTF-8"));

			List<String> setupContent = Arrays
					.asList(new Gson().toJson(new CalabashSettings(location, password, alias)));
			Path setupFile = Paths.get(pathScript + "/.calabash_settings");
			Files.write(setupFile, setupContent, Charset.forName("UTF-8"));

			List<String> runContent = Arrays.asList("calabash-android run apkFile.apk --verbose");
			Path runFile = Paths.get(pathScript + "/run.sh");
			Files.write(runFile, runContent, Charset.forName("UTF-8"));

			// --------------------------------------------------------------------------------

			String c1[] = new String[] { "/bin/bash", "-c",
					String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec chmod +x gen.sh)", path) };
			Process p1 = Runtime.getRuntime().exec(c1);
			p1.waitFor();

			String c2[] = new String[] { "/bin/bash", "-c", String
					.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec chmod +x move.sh)", path) };
			Process p2 = Runtime.getRuntime().exec(c2);
			p2.waitFor();

			String c3[] = new String[] { "/bin/bash", "-c",
					String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec chmod +x run.sh)", path) };
			Process p3 = Runtime.getRuntime().exec(c3);
			p3.waitFor();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void genSkeleton() {
		try {
			String commands[] = new String[] { "/bin/bash", "-c",
					String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec .//gen.sh)", path) };
			Process p = Runtime.getRuntime().exec(commands);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void changeFilesLocation() {
		try {
			String commands[] = new String[] { "/bin/bash", "-c",
					String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec .//move.sh)", path) };
			Process p = Runtime.getRuntime().exec(commands);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void resignApk() {
		try {
			String commands[] = new String[] { "/bin/bash", "-c",
					String.format(
							"(cd ../standalone/deployments/ParqueTestes.war/%s && exec calabash-android resign apkFile.apk apkFile.apk)",
							path) };
			for (String s : commands)
				System.out.println(s);
			Process p = Runtime.getRuntime().exec(commands);
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openEmulator() {
		// /home/felipe/Android/Sdk/tools/emulator -netdelay none -netspeed full
		// -avd Device_Kitkat_API_19
	}

	public void runTests() {
		// calabash-android run <out.apk> --verbose
		try {
			StringBuffer sb = new StringBuffer();
			String commands[] = new String[] { "/bin/bash", "-c",
					String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec .//run.sh)", path) };
			Process p = Runtime.getRuntime().exec(commands);
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			System.out.println(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
