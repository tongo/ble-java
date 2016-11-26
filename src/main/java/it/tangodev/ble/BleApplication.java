package it.tangodev.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bluez.GattApplication1;
import org.bluez.GattManager1;
import org.bluez.LEAdvertisingManager1;
import org.freedesktop.DBus.Properties;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

public class BleApplication implements GattApplication1 {

	public static final String BLUEZ_DBUS_BUSNAME = "org.bluez";
	public static final String BLUEZ_ADAPTER_INTERFACE = "org.bluez.Adapter1";
	
	private List<BleService> servicesList = new ArrayList<BleService>();
	private String path = "/it/tangodev/openlaptimer";
	
	public BleApplication() {
		BleService service = new BleService();
		servicesList.add(service);
	}
	
	public void start() throws DBusException, InterruptedException {
		DBusConnection dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);
		
		this.export(dbusConnection);
		
		Properties adapterProperties = (Properties) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/org/bluez/hci0", Properties.class);
		adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Powered", new Variant<Boolean>(true));
		
		GattManager1 m = (GattManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/org/bluez/hci0", GattManager1.class);
		
		LEAdvertisingManager1 advManager = (LEAdvertisingManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/org/bluez/hci0", LEAdvertisingManager1.class);

		BleAdvertisement adv = new BleAdvertisement(BleAdvertisement.ADVERTISEMENT_TYPE_PERIPHERAL);
		for (BleService service : servicesList) {
			if(service.isPrimary()) {
				adv.addService(service);
			}
		}
		adv.export(dbusConnection);
		
		Map<String, Variant> advOptions = new HashMap<String, Variant>();
		advManager.RegisterAdvertisement(adv, advOptions);
		
		Map<String, Variant> appOptions = new HashMap<String, Variant>();
		m.RegisterApplication(this, appOptions);
	}
	
	public void export(DBusConnection dbusConnection) throws DBusException {
		for (BleService service : servicesList) {
			service.export(dbusConnection);
		}
		dbusConnection.exportObject(path, this);
	}
	
	public void addService(BleService service) {
		this.servicesList.add(service);
	}
	
	public void removeService(BleService service) {
		this.servicesList.remove(service);
	}
	
	@Override
	public boolean isRemote() { return false; }
	
	@Override
	public Map<Path, Map<String, Map<String, Variant>>> GetManagedObjects() {
		System.out.println("Application -> GetManagedObjects");
		
		Map<Path, Map<String, Map<String, Variant>>> response = new HashMap<Path, Map<String,Map<String,Variant>>>();
		for (BleService service : servicesList) {
			response.put(service.getPath(), service.getProperties());
			for (BleCharacteristic characteristic : service.getCharacteristics()) {
				response.put(characteristic.getPath(), characteristic.getProperties());
				// TODO foreach Description in Characteristic
			}
		}
		
		System.out.println(response);
		return response;
	}

}
