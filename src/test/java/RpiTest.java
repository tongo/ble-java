import it.tangodev.ble.BleApplication;

import org.freedesktop.dbus.exceptions.DBusException;

public class RpiTest {
	
	public static void main(String[] args) throws DBusException, InterruptedException {
		
//		DBus dbus = dbusConnection.getRemoteObject("org.freedesktop.DBus", "/or/freedesktop/DBus", DBus.class);
//		String bluezDbusBusName = dbus.GetNameOwner("org.bluez");
//		Introspectable i = (Introspectable) dbusConnection.getRemoteObject("org.bluez", "/org/bluez/hci0", Introspectable.class);
//		System.out.println(i.Introspect());
		
//		ObjectManager bluezObjectManager = (ObjectManager) dbusConnection.getRemoteObject("org.bluez", "/", ObjectManager.class);
		
//		Properties adapterProperties = (Properties) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/org/bluez/hci0", Properties.class);
//		adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Powered", new Variant<Boolean>(true));
//		Thread.sleep(5000);
//		adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Discoverable", new Variant<Boolean>(false));
//		Thread.sleep(5000);
//		adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Pairable", new Variant<Boolean>(true));
//		Thread.sleep(5000);
//		System.out.println(adapterProperties.GetAll(BLUEZ_ADAPTER_INTERFACE));
		
//		GattManager1 m = (GattManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/org/bluez/hci0", GattManager1.class);
		
		BleApplication application = new BleApplication();
//		application.export(dbusConnection);
//		
//		Map<String, Variant> appOptions = new HashMap<String, Variant>();
//		m.RegisterApplication(application, appOptions);
		
		
		application.start();
		System.out.println("FINE");
	}

}
