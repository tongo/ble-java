package org.dbus;

import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.freedesktop.DBus.Properties")
public interface PropertiesChangedSignal extends DBusInterface {

	@DBusMemberName("PropertiesChanged")
	public static class PropertiesChanged extends DBusSignal {
		private final String iface;
		private final Map<String, Variant> propertiesChanged;
		private final List<String> propertiesRemoved;

		public PropertiesChanged(String path, String iface, Map<String, Variant> propertiesChanged, List<String> propertiesRemoved) throws DBusException {
			super(path, iface, propertiesChanged, propertiesRemoved);
			this.iface = iface;
			this.propertiesChanged = propertiesChanged;
			this.propertiesRemoved = propertiesRemoved;
		}

		public String getIface() {
			return iface;
		}

		public Map<String, Variant> getPropertiesChanged() {
			return propertiesChanged;
		}

		public List<String> getPropertiesRemoved() {
			return propertiesRemoved;
		}
	}
}


