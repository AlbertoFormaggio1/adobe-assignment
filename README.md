# Adobe Assignment — Java Web Server

A compact HTTP/1.1 server written in Java. It serves static files from a document root (default: `files`) and implements **GET** and **HEAD**. Other methods are recognized but return **405 Method Not Allowed**. Security-minded path resolution prevents directory traversal (e.g., `../`).

---

## ✨ Features

- **HTTP/1.1** request parsing (request line + headers)
- Methods: **GET** (body), **HEAD** (no body)
- Static file serving (`/` maps to `index.html`)
- Basic MIME detection with fallbacks
- Status codes: `200`, `403`, `404`, `405`, `500`, `505`

---

## 🛠️ Build & Run

Compile **everything** into `./out`, then start the server:

```bash
# Compile everything into ./out
find . -name "*.java" -print0 | xargs -0 javac -encoding UTF-8 -d out

# Run the server (replace package if Executable is in a package)
java -cp out Executable 8080
```

- Only HTTP/1.1 is supported; others return 505.
- Responses are streamed; large files don’t load fully into memory.

Also tests are provided using JUnit 5
