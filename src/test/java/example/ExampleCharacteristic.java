package example;

import it.tangodev.ble.BleCharacteristic;
import it.tangodev.ble.BleService;
import it.tangodev.ble.BleCharacteristicListener;

import java.util.ArrayList;
import java.util.List;

import org.bluez.GattCharacteristic1;

public class ExampleCharacteristic extends BleCharacteristic implements GattCharacteristic1 {
	
	String exampleValue = "value";
	
	public ExampleCharacteristic(BleService service) {
		super(service);
		List<CharacteristicFlag> flags = new ArrayList<CharacteristicFlag>();
		flags.add(CharacteristicFlag.READ);
		flags.add(CharacteristicFlag.WRITE);
		flags.add(CharacteristicFlag.NOTIFY);
		setFlags(flags);
		
		this.path = "/tango/s/ec";
		this.uuid = "13333333-3333-3333-3333-333333333003";
		
		this.listener = new BleCharacteristicListener() {
			@Override
			public void setValue(String devicePath, int offset, byte[] value) {
				try {
					exampleValue = new String(value, "UTF8");
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public byte[] getValue(String devicePath) {
				try {
					return exampleValue.getBytes("UTF8");
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

}
