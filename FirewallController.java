import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;
import javafx.application.Platform;
import java.util.List;
import java.util.Random;

import javax.swing.text.html.ListView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class FirewallController {

    @FXML private TextField directionField;
    @FXML private TextField protocolField;
    @FXML private TextField portField;
    @FXML private TextField sourceIpField;
    @FXML private TextField destinationIpField;
    @FXML private TextField actionField;
    @FXML private TextArea logArea;
    @FXML private ListView<String> rulesList;

    private Task<Void> packetSimulationTask;

    @FXML
    private void loadRules() {
        try {
            List<Packet> rules = DbManager.loadRules();

            ObservableList<String> items = FXCollections.observableArrayList();
            logArea.appendText("\n----- Loaded Rules @ " + new java.util.Date() + " -----\n");

            for (Packet p : rules) {
                String ruleStr = String.format("%s %s %d %s -> %s (%s)",
                    p.getDirection(), p.getProtocol(), p.getPort(),
                    p.getSourceIp(), p.getDestinationIp(), p.getAction());
                items.add(ruleStr);
                logArea.appendText(ruleStr + "\n");
            }

            rulesList.setItems(items);
        } catch (Exception e) {
            logArea.appendText("Error loading rules: " + e.getMessage() + "\n");
        }
        
   
}

    }

    @FXML
    private void addRule() {
        try {
            String direction = directionField.getText();
            String protocol = protocolField.getText();
            String portText = portField.getText();
            int port = portText.equals("*") ? -1 : Integer.parseInt(portText);

            String sourceIp = sourceIpField.getText();
            String destinationIp = destinationIpField.getText();
            String action = actionField.getText();

            DbManager.insertRule(direction, protocol, port, sourceIp, destinationIp, action);
            logArea.appendText("Rule added to database.\n");
            loadRules();
        } catch (Exception e) {
            logArea.appendText("Error adding rule: " + e.getMessage() + "\n");
        }
    }

    private void startPacketSimulation() {
        packetSimulationTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                Random rand = new Random();
                int packetsProcessed = 0;
                long startTime = System.currentTimeMillis();
                while (!isCancelled()) {
                    String direction = rand.nextBoolean() ? "IN" : "OUT";
                    String protocol = rand.nextBoolean() ? "TCP" : "UDP";
                    int port = rand.nextInt(65535);
                    String sourceIp = String.format("%d.%d.%d.%d", rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
                    String destIp = String.format("%d.%d.%d.%d", rand.nextInt(256), rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));

                    List<Packet> rules = DbManager.loadRules();
                    boolean allowed = isPacketAllowed(direction, protocol, port, sourceIp, destIp, rules);

                    String logEntry = String.format("%s: %s %s:%d -> %s (%s)\n",
                        new java.util.Date(), allowed ? "ALLOW" : "BLOCK",
                        sourceIp, port, destIp, protocol);
                    Platform.runLater(() -> logArea.appendText(logEntry));

                    packetsProcessed++;
                    long elapsed = System.currentTimeMillis() - startTime;
                    double throughput = packetsProcessed / (elapsed / 1000.0);
                    Platform.runLater(() -> logArea.appendText(String.format("Throughput: %.2f pkt/s\n", throughput)));

                    Thread.sleep(100);
                }
                return null;
            }
        };
        new Thread(packetSimulationTask).start();
    }

    private boolean isPacketAllowed(String direction, String protocol, int port, String sourceIp, String destIp, List<Packet> rules) {
        // You can update this logic as per your firewall rule checks
        return true;
    }

    @FXML
    private void startSimulation() {
        startPacketSimulation();
    }

    @FXML
    private void stopSimulation() {
        if (packetSimulationTask != null) {
            packetSimulationTask.cancel();
            logArea.appendText("Simulation stopped.\n");
        }
    }
}
