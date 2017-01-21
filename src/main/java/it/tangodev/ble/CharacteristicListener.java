package it.tangodev.ble;

/**
 * Interface that describe the source of the data source of one Characteristic.
 * @author Tongo
 *
 */
public interface CharacteristicListener {
	public byte[] getValue();
	public void setValue(byte[] value);
}
