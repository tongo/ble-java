package org.bluez;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;

public interface Media1 extends DBusInterface {
	void RegisterEndpoint(DBusInterface endpoint, Map<String, Variant> properties);
	void UnregisterEndpoint(DBusInterface endpoint);
	void RegisterPlayer(DBusInterface player, Map<String, Variant> properties);
	void UnregisterPlayer(DBusInterface player);
}
