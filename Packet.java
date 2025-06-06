public class Packet {
    private String direction;
    private String protocol;
    private int port;
    private String sourceIp;
    private String destinationIp;
    private String action;

    public Packet(String direction, String protocol, int port, String sourceIp, String destinationIp, String action) {
        this.direction = direction;
        this.protocol = protocol;
        this.port = port;
        this.sourceIp = sourceIp;
        this.destinationIp = destinationIp;
        this.action = action;
    }

    // Getters and setters (optional, but good practice)
    public String getDirection() { return direction; }
    public String getProtocol() { return protocol; }
    public int getPort() { return port; }
    public String getSourceIp() { return sourceIp; }
    public String getDestinationIp() { return destinationIp; }
    public String getAction() { return action; }
}
