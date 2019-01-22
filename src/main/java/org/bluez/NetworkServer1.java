package org.bluez;

import org.freedesktop.dbus.DBusInterface;

public interface NetworkServer1 extends DBusInterface {
    void Register(String uuid, String bridge);
    void Unregister(String uuid);
}
