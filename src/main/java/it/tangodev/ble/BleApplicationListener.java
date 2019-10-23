package it.tangodev.ble;

public interface BleApplicationListener {
	public void deviceConnected(String path, String address);
	public void deviceDisconnected(String path);
}
