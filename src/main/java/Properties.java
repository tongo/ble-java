/*
 * Copyright (C) 2016 Keith M. Hughes
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */



import java.util.List;
import java.util.Map;

import org.freedesktop.dbus.DBusInterface;
import org.freedesktop.dbus.DBusInterfaceName;
import org.freedesktop.dbus.DBusMemberName;
import org.freedesktop.dbus.DBusSignal;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * The BlueZ object for the DBus Properties interface. It allows properties to
 * be read or written on DBus objects.
 * 
 * <p>
 * The DBus Java libraries included a Properties interface, but did not provide
 * the signals that the Properties interface supports.
 * 
 * @author Keith M. Hughes
 */
@DBusInterfaceName("org.freedesktop.DBus.Properties")
public interface Properties extends DBusInterface {

	/**
	 * Get the value of a property from a given DBus interface.
	 * 
	 * @param <A>
	 *            the type of the value in the variant
	 * @param interfaceName
	 *            the name of the DBus interface
	 * @param propertyName
	 *            the name of the property
	 * 
	 * @return the variant giving the value
	 */
	<A> Variant<A> Get(String interfaceName, String propertyName);

	/**
	 * Get the value of all properties from a given DBus interface.
	 * 
	 * @param interfaceName
	 *            the name of the DBus interface
	 * 
	 * @return a map of property name to the variant giving the value
	 */
	Map<String, Variant> GetAll(String interfaceName);

	/**
	 * Set the value of a property for a given DBus interface.
	 * 
	 * @param interfaceName
	 *            the name of the interface
	 * @param propertyName
	 *            the name of the property
	 * @param value
	 *            a variant giving the new value for the property
	 */
	void Set(String interfaceName, String propertyName, Variant value);

	/**
	 * A DBus signal for properties being changed on a DBus object.
	 * 
	 * @author keith
	 *
	 */
	@DBusMemberName("PropertiesChanged")
	public static class PropertiesChanged extends DBusSignal {

		/**
		 * The name of the interface whose properties changed.
		 */
		private final String iface;

		/**
		 * The properties that changed, mapped by property name o new value.
		 */
		private final Map<String, Variant> propertiesChanged;

		/**
		 * A list of the property names that are removed.
		 */
		private final List<String> propertiesRemoved;

		/**
		 * Construct a new PropertiesChanged signal.
		 * 
		 * @param path
		 *            the DBus object path to the object that sent the signal
		 * @param iface
		 *            the DBus interface the signal has been raised for
		 * @param propertiesChanged
		 *            a map of just the changed properties, key is the property
		 *            name, value is the new value
		 * @param propertiesRemoved
		 *            a list of the names of properties that have been removed
		 * 
		 * @throws DBusException
		 *             something inside DBus broke
		 */
		public PropertiesChanged(String path, String iface, Map<String, Variant> propertiesChanged,
				List<String> propertiesRemoved) throws DBusException {
			super(path, iface, propertiesChanged, propertiesRemoved);
			this.iface = iface;
			this.propertiesChanged = propertiesChanged;
			this.propertiesRemoved = propertiesRemoved;
		}

		/**
		 * Get the name of the DBus interface that the properties changed on.
		 * 
		 * @return the name of the DBus interface
		 */
		public String getIface() {
			return iface;
		}

		/**
		 * Get the properties that changed and their new values.
		 * 
		 * @return map of property names to their new values.
		 */
		public Map<String, Variant> getPropertiesChanged() {
			return propertiesChanged;
		}

		/**
		 * Get the properties that have been removed.
		 * 
		 * @return the names of the removed properties.
		 */
		public List<String> getPropertiesRemoved() {
			return propertiesRemoved;
		}
	}
}
