package it.tangodev.utils;


import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;

import java.util.Map;

/**
 * Consolidates the information about the local Bluetooth Adapter returned by BlueZ
 * Provides Java-friendly getters for the BlueZ D-Bus mappings of Variant values
 */
public class BleAdapter {
    private final Map<String, Variant> fields;
    private final Path path;

    /**
     * Based on the map stored in the D-Bus Managed object org.bluez.Adapter1
     * @param path path to the Adapter in the D-Bus i.e. /org/bluez/hci0
     * @param value map of Adapter Properties storied as Variant types
     */
    public BleAdapter(Path path, Map<String, Variant> value) {
        this.path = path;
        this.fields = value;
    }

    public String getPath() {
        return path.toString();
    }

    public String getAddress() {
        return fields.get("Address").toString();
    }

    public String getName() {
        return fields.get("Name").toString();
    }

    public String getAlias() {
        return fields.get("Alias").toString();
    }
}
