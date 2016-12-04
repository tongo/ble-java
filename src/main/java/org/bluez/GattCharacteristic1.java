package org.bluez;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;

public interface GattCharacteristic1 extends DBusInterface {
	public byte[] ReadValue(Map<String, Variant> option);
	public void WriteValue(byte[] value, Map<String, Variant> option);
	public void StartNotify();
	public void StopNotify();
}
