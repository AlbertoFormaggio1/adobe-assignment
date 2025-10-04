import java.nio.file.Paths;
import util.Utils;
import server.WebServer;
import routing.Router;
import routing.StaticFileHandler;
import routing.RequestContext;

public class Executable {
    public static int parseInputs(String args[]){
        // Check if argument is provided
        if(args.length == 0){
            throw new IllegalArgumentException("No port provided. The app must be executed as `java WebServer <port number>`");
        }

        int port = Integer.parseInt(args[0]);
        Utils.log("Parsed port: " + port);
        return port;
    }

    public static void main(String args[]){
        // Parsing input port
        int port = 0;
        try{
            port = Executable.parseInputs(args);
        }
        catch(NumberFormatException e){
            System.err.println("The port provided is not valid, please provide an integer.");
            System.exit(1);
        }

        // Create the RequestContext for this WebServer with the specified path
        RequestContext context = new RequestContext(Paths.get("files"));
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
