package it.tangodev.ble;

/**
 * Interface that describe the source of the data source of one Characteristic.
 * @author Tongo
 *
 */
public interface BleCharacteristicListener {
	byte[] getValue();
	void setValue(byte[] value);
}
