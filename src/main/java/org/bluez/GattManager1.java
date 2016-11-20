package org.bluez;

import java.util.Map;
import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;


public interface GattManager1 extends DBusInterface {
	public void RegisterApplication(DBusInterface application, Map<String, Variant> options);
	public void UnregisterApplication(DBusInterface application);
}
