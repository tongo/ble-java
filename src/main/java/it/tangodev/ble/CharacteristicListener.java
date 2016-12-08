package it.tangodev.ble;

public interface CharacteristicListener {
	public byte[] getValue();
	public void setValue(byte[] value);
}
