package it.tangodev.ble;

/**
 * Interface that describe the source of the data source of one Characteristic.
 * @author Tongo
 *
 */
public interface BleCharacteristicListener {
	public byte[] getValue(String devicePath);
	public void setValue(String devicePath, int offset, byte[] value);
}
