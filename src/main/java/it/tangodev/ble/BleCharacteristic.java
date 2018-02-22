package it.tangodev.ble;

import it.tangodev.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bluez.GattCharacteristic1;
import org.dbus.PropertiesChangedSignal.PropertiesChanged;
import org.freedesktop.DBus.Properties;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.UInt16;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

/**
 * BleCharacteristic represent a single peripheral's value that can be read, write or notified.
 * @author Tongo
 *
 */
//@SuppressWarnings("rawtypes")
public class BleCharacteristic implements GattCharacteristic1, Properties {

	private static final String GATT_CHARACTERISTIC_INTERFACE = "org.bluez.GattCharacteristic1";
	private static final String CHARACTERISTIC_SERVICE_PROPERTY_KEY = "Service";
	private static final String CHARACTERISTIC_UUID_PROPERTY_KEY = "UUID";
	private static final String CHARACTERISTIC_FLAGS_PROPERTY_KEY = "Flags";
	private static final String CHARACTERISTIC_DESCRIPTORS_PROPERTY_KEY = "Descriptors";
	public static final String CHARACTERISTIC_VALUE_PROPERTY_KEY = "Value";
	
	private BleService service = null;
	protected String uuid = null;
	private List<String> flags = new ArrayList<String>();;
	protected String path = null;
	private boolean isNotifying = false;
	protected BleCharacteristicListener listener;
	
	/**
	 * A flag indicate the operation allowed on a single characteristic.
	 * @author Tongo
	 *
	 */
	public enum CharacteristicFlag {
		READ("read"),
		WRITE("write"),
		NOTIFY("notify");
		
		private String flag;
		
		CharacteristicFlag(String flag) {
			this.flag = flag;
		}
		
		public static CharacteristicFlag fromString(String flag) {
			for (CharacteristicFlag t : CharacteristicFlag.values()) {
				if (flag.equalsIgnoreCase(t.flag)) { return t; }
			}
			throw new RuntimeException("Specified Characteristic Flag not valid [" + flag + "]");
		}
		
		@Override
		public String toString() {
			return this.flag;
		}
	}
	
	/**
	 * 
	 * @param service: The service that contains the Characteristic
	 */
	public BleCharacteristic(BleService service) {
		this.service = service;
	}
	
	/**
	 * 
	 * @param path: The absolute path, APPLICATION/SERVICE/CHARACTERISTIC
	 * @param service
	 * @param flags
	 * @param uuId
	 * @param listener: who can provide the data
	 */
	public BleCharacteristic(String path, BleService service, List<CharacteristicFlag> flags, String uuId, BleCharacteristicListener listener) {
		this.path = path;
		this.service = service;
		this.uuid = uuId;
		setFlags(flags);
		this.listener = listener;
	}
	
	public void setFlags(List<CharacteristicFlag> flags) {
		for (CharacteristicFlag characteristicFlag : flags) {
			this.flags.add(characteristicFlag.toString());
		}
	}
	
	protected void export(DBusConnection dbusConnection) throws DBusException {
		dbusConnection.exportObject(this.getPath().toString(), this);
	}

	protected void unexport(DBusConnection dBusConnection) throws DBusException {
		dBusConnection.unExportObject(this.getPath().toString());
	}
	
	/**
	 * Return the Path (dbus class)
	 * @return
	 */
	public Path getPath() {
		return new Path(path);
	}
	
	public Map<String, Map<String, Variant>> getProperties() {
		System.out.println("Characteristic -> getCharacteristicProperties");
		
		Map<String, Variant> characteristicMap = new HashMap<String, Variant>();
		
		Variant<Path> servicePathProperty = new Variant<Path>(service.getPath());
		characteristicMap.put(CHARACTERISTIC_SERVICE_PROPERTY_KEY, servicePathProperty);
		
		Variant<String> uuidProperty = new Variant<String>(this.uuid);
		characteristicMap.put(CHARACTERISTIC_UUID_PROPERTY_KEY, uuidProperty);
		
		Variant<String[]> flagsProperty = new Variant<String[]>(Utils.getStringArrayFromList(this.flags));
		characteristicMap.put(CHARACTERISTIC_FLAGS_PROPERTY_KEY, flagsProperty);
		
		// TODO manage Descriptors
		Variant<Path[]> descriptorsPatProperty = new Variant<Path[]>(new Path[0]);
		characteristicMap.put(CHARACTERISTIC_DESCRIPTORS_PROPERTY_KEY, descriptorsPatProperty);
		
		Map<String, Map<String, Variant>> externalMap = new HashMap<String, Map<String, Variant>>();
		externalMap.put(GATT_CHARACTERISTIC_INTERFACE, characteristicMap);
		
		return externalMap;
	}
	
	/**
	 * Call this method to send a notification to a central.
	 */
	public void sendNotification() {
		try {
			DBusConnection dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);
			
			Variant<byte[]> signalValueVariant = new Variant<byte[]>(listener.getValue());
			Map<String, Variant> signalValue = new HashMap<String, Variant>();
			signalValue.put(BleCharacteristic.CHARACTERISTIC_VALUE_PROPERTY_KEY, signalValueVariant);
			
			PropertiesChanged signal = new PropertiesChanged(this.getPath().toString(), GATT_CHARACTERISTIC_INTERFACE, signalValue, new ArrayList<String>());
			dbusConnection.sendSignal(signal);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean isRemote() {
		return false;
	}

	/**
	 * This method is called when the central request the Characteristic's value.
	 */
	@Override
	public byte[] ReadValue(Map<String, Variant> option) {
		System.out.println("Characteristic Read option[" + option + "]");
		int offset = 0;
		if(option.get("offset") != null) {
			Variant<UInt16> voffset = option.get("offset");
			offset = (voffset.getValue() != null) ? voffset.getValue().intValue() : offset;
		}
		
		byte[] valueBytes = listener.getValue();
		byte[] slice = Arrays.copyOfRange(valueBytes, offset, valueBytes.length);
		return slice;
	}

	/**
	 * This method is called when the central want to write the Characteristic's value.
	 */
	@Override
	public void WriteValue(byte[] value, Map<String, Variant> option) {
		System.out.println("Characteristic Write option[" + option + "]");
		listener.setValue(value);
	}

	@Override
	public void StartNotify() {
		if(isNotifying) {
			System.out.println("Characteristic already notifying");
			return;
		}
		this.isNotifying = true;
	}

	@Override
	public void StopNotify() {
		if(!isNotifying) {
			System.out.println("Characteristic already not notifying");
			return;
		}
		this.isNotifying = false;
	}
	
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
		if(GATT_CHARACTERISTIC_INTERFACE.equals(interfaceName)) {
			return this.getProperties().get(GATT_CHARACTERISTIC_INTERFACE);
		}
		throw new RuntimeException("Interfaccia sbagliata [interface_name=" + interfaceName + "]");
	}
	
}
