<p align="center">
  <img src="ble-java-logo.png" alt="ble-java logo"/>
</p>

ble-java is a java library for building BLE GATT peripherals role application in JAVA.
ble-java is based on BlueZ, the linux Bluetooth stack.

#Features
* Create GATT Services
* Create GATT Characteristic
* Customize the Peripheral name
* Pure JAVA library

#How to use it
ble-java is based on BlueZ and manage it over dbus protocol.
1. You need to have BlueZ installed
2. You need to have libdbus-java, the d-bus JAVA library
Raspbian example:
```sh
sudo apt-get install -y
```

#Example
You could see the main `MainExample.java` in `src/test/java/example`.
It's a sample main that create a BLE Application with one Service and 2 Characteristic.

#BlueZ compatibility
Until now is tested with BlueZ 5.46 on Raspbian distribution.
ble-java use the GattManager and LEAdvertising that was marked "Experimental" since 5.47, so you have to enable the Experimental features with the "--experimental" parameter in the BlueZ startup service.

Example for Raspbian `/lib/systemd/system/bluetooth.service`

```sh
...
bluetoohd --experimental
...
```

In the BlueZ 5.48 seem to be removed the experimental tag on the LEAdvertising features, but I never tried this version.

#Help
If you need help you can write me an email or you can open a new issue.
