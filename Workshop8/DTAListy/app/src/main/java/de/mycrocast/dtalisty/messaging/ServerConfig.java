package de.mycrocast.dtalisty.messaging;

public class ServerConfig {
    private final static String HTTP = "http://";
    private final static String REST_EXTENSION = "/rest/";

    private String ip;
    private String port;

    public ServerConfig(String ip, String port) {
        this.ip = ip;
        this.port = port;
    }

    public String createRestEndpoint(String beanExtension, String requestExtension) {
        return HTTP + this.ip + ":" + this.port + REST_EXTENSION + beanExtension + "/" + requestExtension;
    }
}
