package it.tangodev.ble;

import it.tangodev.utils.Utils;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bluez.GattCharacteristic1;
import org.freedesktop.DBus.Properties;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

//@SuppressWarnings("rawtypes")
public class BleCharacteristic implements GattCharacteristic1, Properties {

	private static final String GATT_CHARACTERISTIC_INTERFACE = "org.bluez.GattCharacteristic1";
	private static final String CHARACTERISTIC_SERVICE_PROPERTY_KEY = "Service";
	private static final String CHARACTERISTIC_UUID_PROPERTY_KEY = "UUID";
	private static final String CHARACTERISTIC_FLAGS_PROPERTY_KEY = "Flags";
	private static final String CHARACTERISTIC_DESCRIPTORS_PROPERTY_KEY = "Descriptors";
	
	private BleService service = null;
	private String uuid = "0000180d-0000-1000-8000-00805f9b34fb";
	private List<String> flags;
	private String path = "/it/tangodev/openlaptimer/service/characteristic";
	
	private String value = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer luctus pharetra purus id elementum. Praesent at placerat ante, quis ornare orci. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla hendrerit commodo mattis. Mauris interdum sem at tincidunt tincidunt. Interdum et malesuada fames ac ante ipsum primis in faucibus. Integer non interdum odio, eget dapibus nisl. Aenean sodales tortor ac orci tincidunt, ac finibus arcu efficitur. Vivamus ut urna nunc. Integer nec arcu id eros gravida volutpat" +
			"Donec sit amet hendrerit diam, non volutpat tortor. Pellentesque viverra enim vel dictum consectetur. Curabitur hendrerit laoreet libero, nec blandit lectus tempor at. In pharetra libero sit amet nunc luctus, pellentesque pellentesque massa accumsan. Integer laoreet et ligula et hendrerit. Proin quis iaculis ex. Suspendisse a turpis rutrum, elementum metus varius, iaculis diam. Interdum et malesuada fames ac ante ipsum primis in faucibus. Phasellus orci turpis, ullamcorper ullamcorper convallis id, pellentesque sed augue. Sed eget ante nec ante lobortis luctus. Cras a tellus dictum turpis rhoncus pellentesque. Fusce commodo justo sed hendrerit facilisis."+
			"Nulla tristique efficitur massa, sed feugiat lorem maximus eget. Nunc vel leo urna. Nulla pellentesque massa sed enim aliquam, sed scelerisque est ultrices. Nam accumsan, eros eget elementum semper, nunc metus tristique leo, non efficitur ante elit non erat. Maecenas egestas lorem eget sollicitudin fermentum. In vitae nibh vitae metus commodo facilisis. Phasellus suscipit sem orci, a cursus libero pharetra nec."+
			"Etiam vehicula dapibus tellus in imperdiet. Sed ac dui est. Nunc ultrices porttitor magna, sit amet euismod mauris dapibus nec. Curabitur tempus ut sem quis ultrices. Pellentesque faucibus quam non posuere lacinia. Proin at eros vehicula, eleifend risus vitae, mollis magna. Phasellus vel tristique nunc. Maecenas a faucibus nisl. Suspendisse fringilla accumsan diam, eget vestibulum eros. Fusce commodo, lorem sed aliquam malesuada, risus elit euismod ipsum, et bibendum eros est sit amet sapien. Suspendisse potenti. Phasellus venenatis tortor neque, vitae tincidunt nisl bibendum at. Fusce eget massa tempus, tempus sapien quis, vulputate erat. Pellentesque cursus aliquam tincidunt. Mauris aliquam metus ante, non aliquam ex sodales aliquam. Cras pellentesque sem neque, at euismod elit consequat ut.";
	
	public BleCharacteristic(BleService service) {
		flags = new ArrayList<String>();
		flags.add("read");
		flags.add("write");
		this.service = service;
	}
	
	public void export(DBusConnection dbusConnection) throws DBusException {
		dbusConnection.exportObject(this.getPath().toString(), this);
	}
	
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
	
	@Override
	public boolean isRemote() {
		return false;
	}

	@Override
	public byte[] ReadValue(Map<String, Variant> option) {
		System.out.println("Characteristic Read option[" + option + "]");
		
		byte[] b = value.getBytes(Charset.forName("ASCII"));
		return b;
	}

	@Override
	public void WriteValue(byte[] value, Map<String, Variant> option) {
		System.out.println("Characteristic Write option[" + option + "]");
		this.value = new String(value, Charset.forName("ASCII"));
	}

	@Override
	public void StartNotify() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void StopNotify() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void PropertiesChanged() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public <A> A Get(String arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <A> void Set(String arg0, String arg1, A arg2) {
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
