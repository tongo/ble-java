package org.dbus;

import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.freedesktop.DBus.ObjectManager")
public interface InterfacesAddedSignal extends DBusInterface {
	
	@DBusMemberName("InterfacesAdded")
	class InterfacesAdded extends DBusSignal {
		private final Path objectPath;
		private final Map<String, Map<String, Variant>> interfacesAdded;

		public InterfacesAdded(String path, Path objectPath, Map<String, Map<String, Variant>> interfacesAdded) throws DBusException {
			super(path, objectPath, interfacesAdded);
			this.objectPath = objectPath;
			this.interfacesAdded = interfacesAdded;
		}
		
		public Path getObjectPath() {
			return objectPath;
		}
		
		public Map<String, Map<String, Variant>> getInterfacesAdded() {
			return interfacesAdded;
		}
	}
	
}


