# Distributed Systems & Network Programming Foundations in Java

Welcome to my **Distributed Systems Exploration Repository**. This project contains a collection of practical templates and implementations focused on network programming, concurrency models, and distributed communication paradigms. I uploaded this codebase to demonstrate my hands-on understanding of network architectures and active learning workflow.

---

## 💡 The Philosophy: Active Learning in Distributed Environments

* **An Active Learning Journey:** This repository represents a structured environment for understanding how machines communicate over networks, handle concurrent data, and manage state in decentralized systems.
* **Intentionally Work-in-Progress:** The focus of this template is on breaking down fundamental networking blocks (like sockets, byte streams, and message passing) from scratch. Some modules reflect different iterations and stages of my learning curve.
* **Exploratory & Experimental:** Rather than using high-level black-box frameworks, this project dives straight into the low-level and structural abstractions of Java’s networking capabilities.

---

## 📂 Repository Structure & Key Components

The codebase is organized into highly focused packages within `src/main/java/` tackling specific distributed systems challenges:

### 📡 Networking & Client-Server Protocols
* **`echo` / `daytime` / `greeter` / `zitat`** – Implementing standard internet protocols and custom core client-server architectures over TCP/UDP Sockets.
* **`netcat`** – Custom network utility implementation for reading from and writing to network connections.
* **`webclient`** – Handling HTTP-like request/response cycles and parsing network streams manually.

### 🧵 Concurrency & Architectural Patterns
* **`actor`** – Implementing or exploring the **Actor Model** for concurrent, thread-safe, and asynchronous message-passing communication without mutable shared state.
* **`stream` / `io` / `inout`** – Mastering non-blocking or blocking I/O stream manipulation, data serialization, and network throughput pipelines.

### 📊 Data & Structural Utilities
* **`messdaten`** – Processing and parsing raw metrics or sensor data received over network layers.
* **`fpinjava` / `list` / `tuple`** – Functional programming primitives utilized to write safer, side-effect-free data transformation logic across network boundaries.

---

## 🧠 Core Engineering Principles Demonstrated

By working through this distributed systems template, I have developed a strong foundation in:

1. **Network Layer I/O:** Understanding the mechanics of Sockets, byte streams, buffer handling, and the lifecycle of network connections.
2. **Asynchronous & Concurrent Design:** Moving away from standard multi-threading bottlenecks by utilizing paradigms like the Actor Model to prevent race conditions.
3. **Protocol Engineering:** Designing and implementing structured application-layer communication flows between independent runtimes.

---

*Feel free to browse through the packages to see how I approach networking and concurrency principles within the Java ecosystem!*
