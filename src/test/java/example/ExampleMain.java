package example;

import java.util.ArrayList;
import java.util.List;

import it.tangodev.ble.BleApplication;
import it.tangodev.ble.BleCharacteristic;
import it.tangodev.ble.CharacteristicListener;
import it.tangodev.ble.BleCharacteristic.CharacteristicFlag;
import it.tangodev.ble.BleService;

import org.freedesktop.dbus.exceptions.DBusException;

public class ExampleMain implements Runnable {
	
	protected String valueString = "Ciao ciao";
	BleApplication app;
	BleService service;
	BleCharacteristic characteristic;
	
	public void notifyBle(String value) {
		this.valueString = value;
		characteristic.sendNotification();
	}
	
	public ExampleMain() throws DBusException, InterruptedException {
		app = new BleApplication("/tango");
		service = new BleService("/tango/s", "13333333-3333-3333-3333-333333333001", true);
		List<CharacteristicFlag> flags = new ArrayList<CharacteristicFlag>();
		flags.add(CharacteristicFlag.READ);
		flags.add(CharacteristicFlag.WRITE);
		flags.add(CharacteristicFlag.NOTIFY);
		
		characteristic = new BleCharacteristic("/tango/s/c", service, flags, "13333333-3333-3333-3333-333333333002", new CharacteristicListener() {
			@Override
			public void setValue(byte[] value) {
				try {
					valueString = new String(value, "UTF8");
				} catch(Exception e) {
					System.out.println("");
				}
			}
			
			@Override
			public byte[] getValue() {
				try {
					return valueString.getBytes("UTF8");
				} catch(Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		service.addCharacteristic(characteristic);
		app.addService(service);
	}

	@Override
	public void run() {
		try {
			app.start();
			this.wait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws DBusException, InterruptedException {
		ExampleMain example = new ExampleMain();
		Thread t = new Thread(example);
		t.start();
		Thread.sleep(15000);
		example.notifyBle("woooooo");
		Thread.sleep(15000);
		t.notify();
	}
	
}
