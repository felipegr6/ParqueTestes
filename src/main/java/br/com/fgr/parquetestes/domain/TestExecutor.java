package br.com.fgr.parquetestes.domain;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;

public class TestExecutor {
    private String path;
    private OnCompletedOperation callback;

    public TestExecutor(String path, OnCompletedOperation callback) {
        this.path = path;
        this.callback = callback;
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

            String c1[] = new String[]{"/bin/bash", "-c",
                    String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec chmod +x gen.sh)", path)};
            Process p1 = Runtime.getRuntime().exec(c1);
            p1.waitFor();

            String c2[] = new String[]{"/bin/bash", "-c", String
                    .format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec chmod +x move.sh)", path)};
            Process p2 = Runtime.getRuntime().exec(c2);
            p2.waitFor();

            String c3[] = new String[]{"/bin/bash", "-c",
                    String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec chmod +x run.sh)", path)};
            Process p3 = Runtime.getRuntime().exec(c3);
            p3.waitFor();

            callback.onSuccess("Create scripts");
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    public void genSkeleton() {
        try {
            String commands[] = new String[]{"/bin/bash", "-c",
                    String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec .//gen.sh)", path)};
            Process p = Runtime.getRuntime().exec(commands);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            callback.onSuccess(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    public void changeFilesLocation() {
        try {
            String commands[] = new String[]{"/bin/bash", "-c",
                    String.format("(cd ../standalone/deployments/ParqueTestes.war/%s && exec .//move.sh)", path)};
            Process p = Runtime.getRuntime().exec(commands);
            p.waitFor();

            callback.onSuccess("Change files location.");
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    public void resignApk() {
        try {
            String commands[] = new String[]{"/bin/bash", "-c",
                    String.format(
                            "(cd ../standalone/deployments/ParqueTestes.war/%s && exec calabash-android resign apkFile.apk apkFile.apk)",
                            path)};
            Process p = Runtime.getRuntime().exec(commands);
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            callback.onSuccess(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }

    public void openEmulator() {
        // /home/felipe/Android/Sdk/tools/emulator -netdelay none -netspeed full
        // -avd Device_Kitkat_API_19
    }

    public void runTests(String pathScript) {
        // calabash-android run <out.apk> --verbose
        try {
            List<Device> devices = Device.getDevices();
            for (Device d : devices) {
                StringBuffer sb = new StringBuffer();
                String commands[] = new String[]{"/bin/bash", "-c",
                        String.format(
                                "(cd ../standalone/deployments/ParqueTestes.war/%s && export ADB_DEVICE_ARG=%s && exec .//run.sh)",
                                path, d.getSerial())};
                Process p = Runtime.getRuntime().exec(commands);
                p.waitFor();
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                System.out.println(sb.toString());
                List<String> testContent = Arrays.asList(sb.toString());
                Path testFile = Paths.get(String.format("%s/%s.log", pathScript, d.getSerial()));
                if (Files.exists(testFile, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
                    Files.write(testFile, testContent, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
                } else {
                    Files.write(testFile, testContent, Charset.forName("UTF-8"));
                }
                callback.onSuccess(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
    }
}
