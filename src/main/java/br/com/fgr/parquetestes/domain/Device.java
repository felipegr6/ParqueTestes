package br.com.fgr.parquetestes.domain;

import br.com.fgr.parquetestes.helpers.CommandsEnum;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Device {
    private String serial;
    private String sku;
    private transient String fingerPrint;
    private String display;
    private String release;
    private int version;
    private String emulator;

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public void setVersion(String version) {
        this.version = Integer.parseInt(version);
    }

    public void setEmulator(boolean emulator) {
        this.emulator = emulator ? "SIM" : "NÃO";
    }

    public String getSerial() {
        return serial;
    }

    public String getSku() {
        return sku;
    }

    public String getFingerPrint() {
        return fingerPrint;
    }

    public String getDisplay() {
        return display;
    }

    public String getRelease() {
        return release;
    }

    public int getVersion() {
        return version;
    }

    public String isEmulator() {
        return emulator;
    }

    public static List<Device> getDevices() {
        List<Device> devices = new ArrayList<>();
        try {
            Process p = Runtime.getRuntime().exec("adb devices");
            p.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && !line.contains("List")) {
                    Device d = new Device();
                    d.setSerial(line.split("\t")[0]);

                    String prefixCommand = String.format("adb -s %s shell getprop", d.getSerial());
                    String commands[] = new String[] {
                        String.format("%s %s", prefixCommand, CommandsEnum.DISPLAY.getCommand()),
                        String.format("%s %s", prefixCommand, CommandsEnum.EMULATOR.getCommand()),
                        String.format("%s %s", prefixCommand,
                            CommandsEnum.FINGERPRINT.getCommand()),
                        String.format("%s %s", prefixCommand,
                            CommandsEnum.HARDWARE_SKU.getCommand()),
                        String.format("%s %s", prefixCommand, CommandsEnum.RELEASE.getCommand()),
                        String.format("%s %s", prefixCommand, CommandsEnum.SDK.getCommand())};

                    for (String s : commands) {
                        Process p2 = Runtime.getRuntime().exec(new String[] {"/bin/bash", "-c", s});
                        BufferedReader r2 =
                            new BufferedReader(new InputStreamReader(p2.getInputStream()));
                        String l2 = "";
                        while ((l2 = r2.readLine()) != null) {
                            switch (CommandsEnum
                                .getType(
                                    s.replace(
                                        String.format("adb -s %s shell getprop ", d.getSerial()),
                                        ""))) {
                                case DISPLAY:
                                    d.setDisplay(l2);
                                    break;
                                case EMULATOR:
                                    d.setEmulator(l2.equals("EMULATOR"));
                                    break;
                                case FINGERPRINT:
                                    d.setFingerPrint(l2);
                                    break;
                                case HARDWARE_SKU:
                                    d.setSku(l2);
                                    break;
                                case RELEASE:
                                    d.setRelease(l2);
                                    break;
                                case SDK:
                                    d.setVersion(l2);
                                    break;
                                default:
                            }
                        }
                    }
                    devices.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return devices;
    }
}
