package org.bluez;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;

public interface Adapter1 extends DBusInterface {
	public void StartDiscovery();
	public void SetDiscoveryFilter(Map<String, Variant> properties);
	public void StopDiscovery();
	public void RemoveDevice(DBusInterface device);
}
