package org.bluez;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;

public interface GattApplication1 extends DBusInterface {
	public Map<Path, Map<String, Map<String, Variant>>> GetManagedObjects();
}
