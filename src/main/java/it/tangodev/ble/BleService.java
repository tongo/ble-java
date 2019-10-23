package it.tangodev.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bluez.GattService1;
import org.freedesktop.DBus.Properties;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * BleService class rapresent the service.
 * @author Tongo
 *
 */
public class BleService implements GattService1, Properties {
	
	private static final String GATT_SERVICE_INTERFACE = "org.bluez.GattService1";
	private static final String SERVICE_UUID_PROPERTY_KEY = "UUID";
	private static final String SERVICE_PRIMARY_PROPERTY_KEY = "Primary";
	private static final String SERVICE_CHARACTERISTIC_PROPERTY_KEY = "Characteristics";
	
	private String uuid = null;
	private Boolean primary = true;
	private List<BleCharacteristic> characteristics = new ArrayList<BleCharacteristic>();
	private String path = null;
	
	/**
	 * Remember that the path need to have this format APPLICATION/SERVICE
	 * @param path: Absolute path
	 * @param uuId: unique ID 128 bit in this format 13333333-3333-3333-3333-333333333001
	 * @param primary
	 */
	public BleService(String path, String uuId, Boolean primary) {
		this.path = path;
		this.uuid = uuId;
		this.primary = primary;
	}
	
	public void addCharacteristic(BleCharacteristic characteristic) {
		this.characteristics.add(characteristic);
	}
	
	public void removeCharacteristic(BleCharacteristic characteristic) {
		this.characteristics.remove(characteristic);
	}
	
	public List<BleCharacteristic> getCharacteristics() {
		return characteristics;
	}
	
	protected void export(DBusConnection dbusConnection) throws DBusException {
		for (BleCharacteristic characteristic : characteristics) {
			characteristic.export(dbusConnection);
		}
		dbusConnection.exportObject(this.getPath().toString(), this);
	}

	protected void unexport(DBusConnection dbusConnection) throws DBusException {
        for (BleCharacteristic characteristic : characteristics) {
            characteristic.unexport(dbusConnection);
        }
        dbusConnection.unExportObject(this.getPath().toString());
    }
	
	/**
	 * Return the Path (dbus class)
	 * @return
	 */
	public Path getPath() {
		return new Path(path);
	}
	
	/**
	 * Convert the list in array[]
	 * @return
	 */
	private Path[] getCharacteristicsPathArray() {
		Path[] pathArray = new Path[characteristics.size()];
		for (int i=0; i < characteristics.size(); i++) {
			pathArray[i] = characteristics.get(i).getPath();
		}
		return pathArray;
	}
	
	public Map<String, Map<String, Variant>> getProperties() {
		System.out.println("Service -> getServiceProperties");
		Map<String, Variant> serviceMap = new HashMap<String, Variant>();
		
		Variant<String> uuidProperty = new Variant<String>(this.uuid);
		serviceMap.put(SERVICE_UUID_PROPERTY_KEY, uuidProperty);
		
		Variant<Boolean> primaryProperty = new Variant<Boolean>(this.primary);
		serviceMap.put(SERVICE_PRIMARY_PROPERTY_KEY, primaryProperty);
		
		Variant<Path[]> characteristicsPat = new Variant<Path[]>(getCharacteristicsPathArray());
		serviceMap.put(SERVICE_CHARACTERISTIC_PROPERTY_KEY, characteristicsPat);
		
		Map<String, Map<String, Variant>> externalMap = new HashMap<String, Map<String, Variant>>();
		externalMap.put(GATT_SERVICE_INTERFACE, serviceMap);
		
		return externalMap;
	}
	
	public Boolean isPrimary() {
		return primary != null && primary;
	}

	public void setPrimary(Boolean primary) {
		this.primary = primary;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public void setCharacteristics(List<BleCharacteristic> characteristics) {
		this.characteristics = characteristics;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean isRemote() { return false; }

	@Override
	public <A> A Get(String interface_name, String property_name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> void Set(String interface_name, String property_name, A value) {
		// TODO Auto-generated method stub
	}

	@Override
	public Map<String, Variant> GetAll(String interfaceName) {
		if(GATT_SERVICE_INTERFACE.equals(interfaceName)) {
			return this.getProperties().get(GATT_SERVICE_INTERFACE);
		}
		throw new RuntimeException("Interfaccia sbagliata [interface_name=" + interfaceName + "]");
	}

}
