package org.bluez;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;

public interface GattCharacteristic1 extends DBusInterface {
	byte[] ReadValue(Map<String, Variant> option);
	void WriteValue(byte[] value, Map<String, Variant> option);
	void StartNotify();
	void StopNotify();
}
