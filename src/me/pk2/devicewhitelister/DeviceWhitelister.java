package me.pk2.devicewhitelister;

import net.samuelcampos.usbdrivedetector.USBDeviceDetectorManager;
import net.samuelcampos.usbdrivedetector.USBStorageDevice;
import net.samuelcampos.usbdrivedetector.events.DeviceEventType;
import net.samuelcampos.usbdrivedetector.utils.OSUtils;
import net.samuelcampos.usbdrivedetector.utils.OSType;

import java.io.PrintStream;

public class DeviceWhitelister {
    private static String UUIDS = "822247FF,10F62FDB,2214AAA3";

    private static boolean checkUUID(USBStorageDevice device, String[] uuids) {
        for(String uuid : uuids)
            if(device.getUuid().equalsIgnoreCase(uuid))
                return true;
        return false;
    }

    public static void main(String[] args) {
        PrintStream errorStream = new PrintStream(System.err) {
            public void println(String line) {
                assert line != null;
                if(!line.startsWith("SLF4J"))
                    super.println(line);
            }
        };
        System.setErr(errorStream);

        System.out.println("DeviceWhitelister by: PK2_Stimpy#7089\n");

        String[] allowed = UUIDS.split(",");

        USBDeviceDetectorManager manager = new USBDeviceDetectorManager();
        for(String arg : args) {
            if (arg.equalsIgnoreCase("--show")) {
                System.out.printf("Allowed UUIDs(%d): %s\n", allowed.length, UUIDS);
                System.out.printf("-- Devices List(%s) --\n", manager.getRemovableDevices().size());
                manager.getRemovableDevices().forEach(System.out::println);
                return;
            }
        }

        /*
        List<USBStorageDevice> storages = manager.getRemovableDevices();
        storages.forEach(v -> {
            if(!checkUUID(v, allowed))
                System.out.println("INVALID UUID " + v.getUuid());
        });*/

        if(OSUtils.getOsType() != OSType.WINDOWS) {
            while(true) {
                manager.getRemovableDevices().forEach(device -> {
                    System.out.println("CHECK " + device.getUuid());
                    if (!DeviceWhitelister.checkUUID(device, allowed)) {
                        System.out.println(">> INVALID UUID " + device.getUuid());
                        System.out.print(">> UNMOUNTING UUID " + device.getUuid() + "... ");
                        try {
                            manager.unmountStorageDevice(device);
                            System.out.println("OK!");
                        }
                        catch (Exception exception) {
                            System.out.println("FAIL! " + exception.getMessage());
                        }
                    }
                });
    
                try {
                    Thread.sleep(1000);
                } catch(InterruptedException e) { e.printStackTrace(); }
            }
        } else {
            manager.addDriveListener((event) -> {
                if(event.getEventType() == DeviceEventType.REMOVED)
                    return;

                USBStorageDevice device = event.getStorageDevice();
                System.out.println("CHECK " + device.getUuid());
                if(!checkUUID(device, allowed)) {
                    System.out.println(">> INVALID UUID " + device.getUuid());
                    System.out.print(">> UNMOUNTING UUID " + device.getUuid() + "... ");

                    try {
                        manager.unmountStorageDevice(device);
                        System.out.println("OK!");
                    } catch (Exception exception) {
                        System.out.println("FAIL! " + exception.getMessage());
                    }
                }
            });
        }
    }
}
