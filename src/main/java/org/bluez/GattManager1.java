package org.bluez;

import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;

public interface GattManager1 extends DBusInterface {
	void RegisterApplication(DBusInterface application, Map<String, Variant> options);
	void UnregisterApplication(DBusInterface application);
}
