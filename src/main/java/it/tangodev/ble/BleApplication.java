package it.tangodev.ble;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bluez.GattApplication1;
import org.bluez.GattManager1;
import org.bluez.LEAdvertisingManager1;
import org.freedesktop.DBus.Properties;
import org.freedesktop.dbus.DBusConnection;
import org.freedesktop.dbus.Path;
import org.freedesktop.dbus.Variant;
import org.freedesktop.dbus.exceptions.DBusException;

public class BleApplication implements GattApplication1 {

	public static final String BLUEZ_DBUS_BUSNAME = "org.bluez";
	public static final String BLUEZ_ADAPTER_INTERFACE = "org.bluez.Adapter1";
	
	private List<BleService> servicesList = new ArrayList<BleService>();
	private String path = "/it/tangodev/openlaptimer";
	
	public BleApplication() {
		BleService service = new BleService();
		servicesList.add(service);
	}
	
	public void start() throws DBusException, InterruptedException {
		
		DBusConnection dbusConnection = DBusConnection.getConnection(DBusConnection.SYSTEM);
		
		this.export(dbusConnection);
		
		Properties adapterProperties = (Properties) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/org/bluez/hci0", Properties.class);
		adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Powered", new Variant<Boolean>(true));
		
		GattManager1 m = (GattManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/org/bluez/hci0", GattManager1.class);
		
		LEAdvertisingManager1 advManager = (LEAdvertisingManager1) dbusConnection.getRemoteObject(BLUEZ_DBUS_BUSNAME, "/org/bluez/hci0", LEAdvertisingManager1.class);

		BleAdvertisement adv = new BleAdvertisement(BleAdvertisement.ADVERTISEMENT_TYPE_PERIPHERAL);
		for (BleService service : servicesList) {
			if(service.isPrimary()) {
				adv.addService(service);
			}
		}
		adv.export(dbusConnection);
		
//		 # Setup the advertisement
//	        self.service_ad = Advertisement(self, 'peripheral')
//	        for service in self.services:
//	            if service.primary:
//	                print('Advertising service ', service.uuid)
//	                self.service_ad.add_service_uuid(service.uuid)
//	                self.service_ad.ad_type = service.type
//	                if service.service_data is not None:
//	                    print('Adding service data: ',
//	                          service.uuid,
//	                          service.service_data)
//	                    self.service_ad.add_service_data(service.uuid,
//	                                                     service.service_data)
//	                                                     
		
//		Thread.sleep(5000);
//		adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Discoverable", new Variant<Boolean>(false));
//		Thread.sleep(5000);
//		adapterProperties.Set(BLUEZ_ADAPTER_INTERFACE, "Pairable", new Variant<Boolean>(true));
//		Thread.sleep(5000);
//		System.out.println(adapterProperties.GetAll(BLUEZ_ADAPTER_INTERFACE));
		
		Map<String, Variant> advOptions = new HashMap<String, Variant>();
		advManager.RegisterAdvertisement(adv, advOptions);
		
		Map<String, Variant> appOptions = new HashMap<String, Variant>();
		m.RegisterApplication(this, appOptions);
		
		
		/*
		def start(self):
	        """Start the application.
	        This function performs the following steps to start the BLE peripheral
	        application:
	        1. Registers the Bluetooth adapter and turns it on.
	        2. Gets the Bluez D-Bus advertising manager interface.
	        3. Gets the Bluez D-Bus gatt manager interface.
	        4. Creates an advertisement with the primary application service.
	        5. Registers the advertisement with the Bluez advertising manager.
	        6. Registers the application with the Bluez gatt manager.
	        7. Runs the program loop
	        The application must first have had a service added to it.
	        :Example:
	        >>> app = peripheral.Application()
	        >>> app.add_service(your_service)
	        >>> app.start()
	        It is good practice to put the ``app.start()`` in a
	        ``try-except-finally`` block to enable keyboard interrupts.
	        """
	        # Register the Bluetooth adapter
	        self.dongle.powered('on')

	        # Setup the advertising manager
	        print('setup ad_manager')
	        self.ad_manager = tools.get_advert_manager_interface()

	        # Setup the service manager
	        print('setup service_manager')
	        self.service_manager = tools.get_gatt_manager_interface()

	        # Setup the advertisement
	        self.service_ad = Advertisement(self, 'peripheral')
	        for service in self.services:
	            if service.primary:
	                print('Advertising service ', service.uuid)
	                self.service_ad.add_service_uuid(service.uuid)
	                self.service_ad.ad_type = service.type
	                if service.service_data is not None:
	                    print('Adding service data: ',
	                          service.uuid,
	                          service.service_data)
	                    self.service_ad.add_service_data(service.uuid,
	                                                     service.service_data)

	        # Register the advertisement
	        print('Register Adver', self.service_ad.get_path())
	        self.ad_manager.RegisterAdvertisement(
	            self.service_ad.get_path(), {},
	            reply_handler=register_ad_cb,
	            error_handler=register_ad_error_cb)

	        # Register the application
	        print('Register Application ', self.get_path())
	        self.service_manager.RegisterApplication(
	            self.get_path(), {},
	            reply_handler=register_service_cb,
	            error_handler=register_service_error_cb)
	        try:
	            # Run the mainloop
	            self.mainloop.run()
	        except KeyboardInterrupt:
	            print('Closing Mainloop')
	            self.mainloop.quit()
	            */
	
	}
	
	public void export(DBusConnection dbusConnection) throws DBusException {
		for (BleService service : servicesList) {
			service.export(dbusConnection);
		}
		dbusConnection.exportObject(path, this);
	}
	
	public void addService(BleService service) {
		this.servicesList.add(service);
	}
	
	public void removeService(BleService service) {
		this.servicesList.remove(service);
	}
	
	@Override
	public boolean isRemote() { return false; }

//	a{oa{sa{sv}}}
//	array {
//		path array {
//			string array {
//				string variant
//			}
//		}
//	}
	
	@Override
	public Map<Path, Map<String, Map<String, Variant>>> GetManagedObjects() {
		System.out.println("Application -> GetManagedObjects");
		
		Map<Path, Map<String, Map<String, Variant>>> response = new HashMap<Path, Map<String,Map<String,Variant>>>();
		// TODO per ogni servizio dell'applicazione
					// TODO le property come van trattate??
					// TODO per ogni caratterstica
						// TODO le property come van trattate??
		
		for (BleService service : servicesList) {
			// TODO prelevare la lista di property del servizio (è un metodo apposito)
			response.put(service.getPath(), service.getProperties());
			
			for (BleCharacteristic characteristic : service.getCharacteristics()) {
				response.put(characteristic.getPath(), characteristic.getProperties());
			}
		}
		
		
//		for service in self.services:
//            response[service.get_path()] = service.get_properties()
//            chrcs = service.get_characteristics()
//            for chrc in chrcs:
//                response[chrc.get_path()] = chrc.get_properties()
//                descs = chrc.get_descriptors()
//                for desc in descs:
//                    response[desc.get_path()] = desc.get_properties()
		
		System.out.println(response);
		return response;
	}

}
