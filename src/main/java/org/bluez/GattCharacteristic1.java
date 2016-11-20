package org.bluez;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.Variant;

public interface GattCharacteristic1 extends DBusInterface {
//	@dbus.service.method(DBUS_PROP_IFACE,
//  in_signature='s',
//  out_signature='a{sv}')
//def GetAll(self, interface):
//if interface != GATT_CHRC_IFACE:
//raise InvalidArgsException()
	//
	//return self.get_properties()[GATT_CHRC_IFACE]

	
//
//@dbus.service.method(GATT_CHRC_IFACE,
//           in_signature='a{sv}',
//           out_signature='ay')
//def ReadValue(self, options):
//print('Default ReadValue called, returning error')
//raise NotSupportedException()
	public byte[] ReadValue(Map<String, Variant> option);
	
//@dbus.service.method(GATT_CHRC_IFACE, in_signature='aya{sv}')
//def WriteValue(self, value, options):
//print('Default WriteValue called, returning error')
//raise NotSupportedException()
	public void WriteValue(byte[] value, Map<String, Variant> option);
	
//@dbus.service.method(GATT_CHRC_IFACE)
//def StartNotify(self):
//print('Default StartNotify called, returning error')
//raise NotSupportedException()
	public void StartNotify();
	
//@dbus.service.method(GATT_CHRC_IFACE)
//def StopNotify(self):
//print('Default StopNotify called, returning error')
//raise NotSupportedException()
	public void StopNotify();
	
//@dbus.service.signal(DBUS_PROP_IFACE,
//            signature='sa{sv}as')
//def PropertiesChanged(self, interface, changed, invalidated):
//pass
	public void PropertiesChanged();
}
