package org.dbus;

import java.util.List;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.exceptions.DBusException;

@DBusInterfaceName("org.freedesktop.DBus.ObjectManager")
public interface InterfacesRomovedSignal extends DBusInterface {
	
	@DBusMemberName("InterfacesRemoved")
	public static class InterfacesRemoved extends DBusSignal {
		
		private final Path objectPath;
		private List<String> interfacesRemoved;
		
		public InterfacesRemoved(String path, Path objectPath, List<String> interfacesRemoved) throws DBusException {
			super(path, objectPath, interfacesRemoved);

			this.objectPath = objectPath;
			this.interfacesRemoved = interfacesRemoved;
		}
		
		public Path getObjectPath() {
			return objectPath;
		}
		
		public List<String> getInterfacesRemoved() {
			return interfacesRemoved;
		}
	}
	
}


