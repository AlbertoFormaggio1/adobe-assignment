import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import util.Utils;
import server.WebServer;
import routing.Router;
import routing.StaticFileHandler;
import routing.RequestContext;

public class Executable {
    public static void main(String args[]){
        if (args == null || args.length != 2) {
            throw new IllegalArgumentException("Usage: java Executable <port> \"<path to server folder>\"");
        }

        // 1) Port must be a number
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Port must be a number.", e);
        }

        Path inputPath = Paths.get(args[1]).toAbsolutePath().normalize();
        if (!Files.isDirectory(inputPath)) {
            throw new IllegalArgumentException("Doc root does not exist or is not a directory: " + inputPath);
        }

        // Create the RequestContext for this WebServer with the specified path
        RequestContext context = new RequestContext(inputPath);
        StaticFileHandler fileHandler = new StaticFileHandler();
        // Create also the router
        Router router = new Router(fileHandler, context);
        Utils.log("Router initialized with docRoot: " + context.docRoot());

        WebServer server = new WebServer(port, router);

        // Shutdown correctly the server when closing the app, otherwise it'll keep spinning
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop, "shutdown"));

        server.start();

        try{
            // Make the web server wait indefinitely until the user doesn't stop the application using CTRL+C
            server.await();
        } catch(Exception e){
            Utils.log("Fatal exception raised. Shutting down server");
            server.stop();
            System.exit(1);
        }
    }
}
