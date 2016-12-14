package br.com.fgr.parquetestes.helpers;

public enum CommandsEnum {
    HARDWARE_SKU("ro.boot.hardware.sku"), FINGERPRINT("ro.bootimage.build.fingerprint"), DISPLAY(
            "ro.product.display"), RELEASE("ro.build.version.release"), SDK(
            "ro.build.version.sdk"), EMULATOR("ro.setupwizard.mode"), ERROR("");

    private String command;

    CommandsEnum(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public static CommandsEnum getType(String s) {
        switch (s) {
            case "ro.boot.hardware.sku":
                return HARDWARE_SKU;
            case "ro.bootimage.build.fingerprint":
                return FINGERPRINT;
            case "ro.product.display":
                return DISPLAY;
            case "ro.build.version.release":
                return RELEASE;
            case "ro.build.version.sdk":
                return SDK;
            case "ro.setupwizard.mode":
                return EMULATOR;
            default:
                return ERROR;
        }
    }
}
