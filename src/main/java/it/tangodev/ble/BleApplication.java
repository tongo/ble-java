package it.tangodev.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.tangodev.utils.BleAdapter;
import org.bluez.GattApplication1;
import org.bluez.GattManager1;
import org.bluez.LEAdvertisingManager1;
import org.dbus.ObjectManager;
import org.dbus.InterfacesAddedSignal.InterfacesAdded;
import org.dbus.InterfacesRomovedSignal.InterfacesRemoved;
import org.freedesktop.DBus;
import org.freedesktop.DBus.Properties;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.DBusSigHandler;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * BleApplication class is the starting point of the entire Peripheral service's structure.
 * It is responsible of the service's publishment and and the advertisement.
 * @author Tongo
 *
 */
public class BleApplication implements GattApplication1 {

	public static final String DBUS_BUSNAME = "org.freedesktop.DBus";
	public static final String BLUEZ_DBUS_BUSNAME = "org.bluez";
	public static final String BLUEZ_DEVICE_INTERFACE = "org.bluez.Device1";
	public static final String BLUEZ_ADAPTER_INTERFACE = "org.bluez.Adapter1";
	public static final String BLUEZ_GATT_INTERFACE = "org.bluez.GattManager1";
	public static final String BLUEZ_LE_ADV_INTERFACE = "org.bluez.LEAdvertisingManager1";
	public static final String ADDRESS = "Address";

	private List<BleService> servicesList = new ArrayList<BleService>();
	private String path = null;
	private BleAdapter bleAdapter;
	private BleService advService;
	private BleAdvertisement adv;
	private String adapterAlias;
	
	private boolean hasDeviceConnected = false;
	
	private DBusSigHandler<InterfacesAdded> interfacesAddedSignalHandler;
	private DBusSigHandler<InterfacesRemoved> interfacesRemovedSignalHandler;
	private BleApplicationListener listener;
	
	/**
	 * In order to create a BleApplication you need to pass a path.
	 * The bluezero standard structure is:
	 * APPLICAZION
	 * 	SERVICE
	 * 		CHARACTERISTIC-1
	 * 		CHARACTERISTIC-2
	 * 
	 * Since bluez 5.43, the advertisement is able to run only ONE service.
	 * @param path
	 */
	public BleApplication(String path, BleApplicationListener listener) {
		this.path = path;
		this.listener = listener;
	}
	
	/**
	 * First of all the method power-on the adapter.
	 * Then publish the service with their characteristic and start the advertisement (only primary service can advertise).
	 * @throws DBusException
	 * @throws InterruptedException
	 */
	public void start() throws DBusException, InterruptedException {
		DBusConnection dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);

		bleAdapter = findAdapterPath();
		if (bleAdapter == null) {
			throw new RuntimeException("No BLE adapter found");
		}

		this.export(dbusConnection);

		Properties adapterProperties = (Properties) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, bleAdapter.getPath(), Properties.class);
		adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Powered", new Variant<Boolean>(true));
		if(adapterAlias != null) {
			adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Alias", new Variant<String>(adapterAlias));
		}

		GattManager1 gattManager = (GattManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, bleAdapter.getPath(), GattManager1.class);

		LEAdvertisingManager1 advManager = (LEAdvertisingManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, bleAdapter.getPath(), LEAdvertisingManager1.class);

		String advPath = path + "/advertisement";
		adv = new BleAdvertisement(BleAdvertisement.ADVERTISEMENT_TYPE_PERIPHERAL, advPath);
		for (BleService service : servicesList) {
			if(service.isPrimary()) {
				advService = service;
				adv.addService(service);
				break;
			}
		}
		adv.export(dbusConnection);
		
		Map<String, Variant> advOptions = new HashMap<String, Variant>();
		advManager.RegisterAdvertisement(adv, advOptions);
		
		Map<String, Variant> appOptions = new HashMap<String, Variant>();
		gattManager.RegisterApplication(this, appOptions);
		
		initInterfacesHandler();
	}
	
	/**
	 * Stop the advertisement and unpublish the service.
	 * @throws DBusException
	 * @throws InterruptedException
	 */
	public void stop() throws DBusException, InterruptedException {
		if (bleAdapter == null) {
			return;
		}
		DBusConnection dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);
		GattManager1 gattManager = (GattManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, bleAdapter.getPath(), GattManager1.class);
		LEAdvertisingManager1 advManager = (LEAdvertisingManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, bleAdapter.getPath(), LEAdvertisingManager1.class);

		if (adv != null) {
			advManager.UnregisterAdvertisement(adv);
		}
		gattManager.UnregisterApplication(this);
		
		dbusConnection.removeSigHandler(InterfacesAdded.class, interfacesAddedSignalHandler);
		dbusConnection.removeSigHandler(InterfacesRemoved.class, interfacesRemovedSignalHandler);
		dbusConnection.disconnect();
	}
	
	protected void initInterfacesHandler() throws DBusException {
		DBusConnection dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);
		DBus dbus = dbusConnection.getRemoteObject(DBUS_BUSNAME, "/or/freedesktop/DBus", DBus.class);
		String bluezDbusBusName = dbus.GetNameOwner(BLUEZ_DBUS_BUSNAME);
		ObjectManager bluezObjectManager = (ObjectManager) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/", ObjectManager.class);
		
		interfacesAddedSignalHandler = new DBusSigHandler<InterfacesAdded>() {
			@Override
			public void handle(InterfacesAdded signal) {
				Map<String, Variant> iamap = signal.getInterfacesAdded().get(BLUEZ_DEVICE_INTERFACE);
				if (iamap != null) {
					Variant<String> address = iamap.get(ADDRESS);
					String path = signal.getObjectPath().toString();
					hasDeviceConnected = true;
					if (listener != null) {
						listener.deviceConnected(path, address.getValue());
					}
				}
			}
		};

		interfacesRemovedSignalHandler = new DBusSigHandler<InterfacesRemoved>() {
			@Override
			public void handle(InterfacesRemoved signal) {
				List<String> irlist = signal.getInterfacesRemoved();
				for (String ir : irlist) {
					if (BLUEZ_DEVICE_INTERFACE.equals(ir)) {
						String path = signal.getObjectPath().toString();
						hasDeviceConnected = false;
						if (listener != null) {
							listener.deviceDisconnected(path);
						}
					}
				}
			}
		};

		dbusConnection.addSigHandler(InterfacesAdded.class, bluezDbusBusName, bluezObjectManager, interfacesAddedSignalHandler);
		dbusConnection.addSigHandler(InterfacesRemoved.class, bluezDbusBusName, bluezObjectManager, interfacesRemovedSignalHandler);
	}
	
	/**
	 * Set the alias name of the peripheral. This name is visible by the central that discover s peripheral.
	 * This must set before start to take effect.
	 * @param alias
	 */
	public void setAdapterAlias(String alias) {
		adapterAlias = alias;
	}
	
	public void addService(BleService service) {
		this.servicesList.add(service);
	}
	
	public void removeService(BleService service) {
		this.servicesList.remove(service);
	}
	
	public List<BleService> getServicesList() {
		return servicesList;
	}

	public boolean hasDeviceConnected() {
		return hasDeviceConnected;
	}

	/**
	 * Search for a Adapter that has GattManager1 and LEAdvertisement1 interfaces, otherwise return null.
	 * @return BleAdapter based on the map stored in the D-Bus Managed object org.bluez.Adapter1
	 * @throws DBusException if there is an error communicating with BlueZ over D-Bus
	 */
	public static BleAdapter findAdapterPath() throws DBusException {
		DBusConnection dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);
		ObjectManager bluezObjectManager = dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/", ObjectManager.class);
		if (bluezObjectManager == null) {
			return null;
		}

		Map<Path, Map<String, Map<String, Variant>>> bluezManagedObject = bluezObjectManager.GetManagedObjects();
		if (bluezManagedObject == null) {
			return null;
		}

		for (Path path : bluezManagedObject.keySet()) {
			Map<String, Map<String, Variant>> value = bluezManagedObject.get(path);
			boolean hasGattManager = false;
			boolean hasAdvManager = false;

			for (Map.Entry<String, Map<String, Variant>> entry : value.entrySet()) {
				if (entry.getKey().equals(BLUEZ_GATT_INTERFACE)) {
					hasGattManager = true;
				}
				if (entry.getKey().equals(BLUEZ_LE_ADV_INTERFACE)) {
					hasAdvManager = true;
				}
				if (hasGattManager && hasAdvManager) {
					return new BleAdapter(path, value.get("org.bluez.Adapter1"));
				}

			}
		}
		
		return null;
	}
	
	/**
	 * Export the application in Dbus system.
	 * @param dbusConnection
	 * @throws DBusException
	 */
	private void export(DBusConnection dbusConnection) throws DBusException {
		for (BleService service : servicesList) {
			service.export(dbusConnection);
		}
		dbusConnection.exportObject(path, this);
	}
	
	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public Map<Path, Map<String, Map<String, Variant>>> GetManagedObjects() {
		System.out.println("Application -> GetManagedObjects");

		Map<Path, Map<String, Map<String, Variant>>> response = new HashMap<Path, Map<String, Map<String, Variant>>>();
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

	public BleAdapter getBleAdapter() {
		return bleAdapter;
	}

}