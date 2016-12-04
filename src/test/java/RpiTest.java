import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.tangodev.ble.BleApplication;
import it.tangodev.ble.BleCharacteristic;
import it.tangodev.utils.Utils;

import org.dbus.ObjectManager;
import org.dbus.PropertiesChangedSignal.PropertiesChanged;
import org.freedesktop.DBus;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

public class RpiTest {
	
	public static void main(String[] args) throws DBusException, InterruptedException {
		
		
		
		DBusConnection dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);
//		DBus dbus = dbusConnection.getRemoteObject("org.freedesktop.DBus", "/or/freedesktop/DBus", DBus.class);
//		String bluezDbusBusName = dbus.GetNameOwner("org.bluez");
//		Introspectable i = (Introspectable) dbusConnection.getRemoteObject("org.bluez", "/org/bluez/hci0", Introspectable.class);
//		System.out.println(i.Introspect());
		
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
		
//		String characteristicPath = "/it/tangodev/openlaptimer/service/characteristic";
//		
		byte[] value = new byte[2];
		value[0] = 'a';
		value[1] = 'a';
//		
//		Variant<byte[]> signalValueVariant = new Variant<byte[]>(value);
//		Map<String, Variant> signalValue = new HashMap<String, Variant>();
//		signalValue.put(BleCharacteristic.CHARACTERISTIC_VALUE_PROPERTY_KEY, signalValueVariant);
//		
//		PropertiesChanged signal = new PropertiesChanged(characteristicPath, signalValue, new ArrayList<String>());
//		dbusConnection.sendSignal(signal);
		
		BleCharacteristic characteristic = application.getServicesList().get(0).getCharacteristics().get(0);
		characteristic.StartNotify();
		characteristic.sendNotification(value);
		
		System.out.println("FINE");
	}

}
