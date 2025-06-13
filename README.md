Automated Firewall 

A desktop-based firewall simulation tool using JavaFX and Oracle SQL.

## ✨ Features
- Add, view, and simulate firewall rules
- Connects to Oracle DB (`firewall_rules1` table)
- Real-time packet generation and matching
- Logging and rule display using `ListView` and `TextArea`

## 📦 Tech Stack
- JavaFX
- Oracle SQL (JDBC)
- Multithreading (simulation)
- FXML UI

## 📁 Files
- `Main.java` — app launcher
- `FirewallController.java` — main logic
- `Packet.java` — model class
- `DbManager.java` — database handler
- `firewall.fxml` — UI layout

## 🧪 Future Ideas
- Export logs
- Use `ComboBox` for better input validation
- Add chart to display blocked vs allowed packets
