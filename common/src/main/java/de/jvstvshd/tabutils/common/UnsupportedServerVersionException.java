package de.jvstvshd.tabutils.common;

public class UnsupportedServerVersionException extends RuntimeException {

    public UnsupportedServerVersionException() {
        super("The server version is not supported by TabUtils.");
    }

    public UnsupportedServerVersionException(Throwable cause) {
        super("The server version is not supported by TabUtils.", cause);
    }
}
