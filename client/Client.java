import org.jcp.xml.dsig.internal.dom.Utils;

public class Client {
    public static int parseInputs(String args[]){
        // Check if argument is provided
        if(args.length == 0){
            throw new IllegalArgumentException("No port provided. The app must be executed as `java WebServer <port number>`");
        }

        int port = Integer.parseInt(args[0]);
        Utils.log("Parsed port: " + port);
        return port;
    }

    public static void Main(String[] args){
        // Parsing input port
        int port = 0;
        try{
            port = Executable.parseInputs(args);
        }
        catch(NumberFormatException e){
            System.err.println("The port provided is not valid, please provide an integer.");
            System.exit(1);
        }

        
    }
}
