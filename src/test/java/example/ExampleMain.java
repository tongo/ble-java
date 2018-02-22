package example;

import it.tangodev.ble.BleApplication;
import it.tangodev.ble.BleApplicationListener;
import it.tangodev.ble.BleCharacteristic;
import it.tangodev.ble.BleCharacteristic.CharacteristicFlag;
import it.tangodev.ble.BleCharacteristicListener;
import it.tangodev.ble.BleService;

import java.util.ArrayList;
import java.util.List;

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
		BleApplicationListener appListener = new BleApplicationListener() {
			@Override
			public void deviceDisconnected() {
				System.out.println("Device disconnected");
			}
			
			@Override
			public void deviceConnected() {
				System.out.println("Device connected");
			}
		};
		app = new BleApplication("/tango", appListener);
		service = new BleService("/tango/s", "13333333-3333-3333-3333-333333333001", true);
		List<CharacteristicFlag> flags = new ArrayList<CharacteristicFlag>();
		flags.add(CharacteristicFlag.READ);
		flags.add(CharacteristicFlag.WRITE);
		flags.add(CharacteristicFlag.NOTIFY);
		
		characteristic = new BleCharacteristic("/tango/s/c", service, flags, "13333333-3333-3333-3333-333333333002", new BleCharacteristicListener() {
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
		
		ExampleCharacteristic exampleCharacteristic = new ExampleCharacteristic(service);
		service.addCharacteristic(exampleCharacteristic);
		app.start();
	}

	@Override
	public void run() {
		try {
			this.wait();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BleApplication getApp() {
		return app;
	}

	public static void main(String[] args) throws DBusException, InterruptedException {
		ExampleMain example = new ExampleMain();
		System.out.println("");
//		Thread t = new Thread(example);
//		t.start();
//		Thread.sleep(15000);
//		example.notifyBle("woooooo");
//		Thread.sleep(15000);
//		t.notify();

//		Thread.sleep(5000);
//		System.out.println("stopping application");
//		example.getApp().stop();
	}
	
}
