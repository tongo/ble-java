<p align="center">
  <img src="ble-java-logo.png" alt="ble-java logo"/>
</p>

**ble-java** is a java library for building **BLE** GATT peripherals role application in JAVA.

**ble-java** is based on BlueZ, the Linux Bluetooth stack implemented on D-Bus.

# Features
* Create GATT Services
* Create GATT Characteristic
* Customize the Peripheral name
* JAVA library with minimal JNI interfaces to BlueZ over D-Bus

# Dependencies
1. Java 8 or better
2. BlueZ 5.43 or better
3. libunixsocket-java (```apt-get install libsocket-java```) 
4. d-bus Java library `libdbus-java`

Raspbian install:
```
sudo apt-get install libdbus-java
```
you may also have to do run
```
sudo ldconfig
```

# Install
Clone the repository and build with Gradle:
```
./gradlew build
```

# Example
`MainExample.java` in `src/test/java/example` demonstrates a sample main that 
creates a BLE Application with one Service and 2 Characteristics.

Before running the example, copy ```example.conf``` to ```/etc/dbus-1/system.d/```

To run from command line: ````sudo ./gradlew runExample````

Press ctrl-c to stop service.

# BlueZ compatibility
Tested with BlueZ 5.46 on Raspbian distribution.

**ble-java** usees the GattManager and LEAdvertising that was marked "Experimental" since 5.47. You need to enable the
 Experimental features with the `--experimental` parameter in the BlueZ startup service.

For Raspbian and Ubuntu `/lib/systemd/system/bluetooth.service`

```
...
bluetoohd --experimental
...
```

If you are using BlueZ 5.48, the ```--experimental``` tag may not be needed for LEAdvertising features. But we have not tried
this yet.

For more info about BlueZ see [http://www.bluez.org](http://www.bluez.org).

# Help
If you need help you can write me an email or you can open a new issue.
