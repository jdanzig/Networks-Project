import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class HTTPServer
{
        public String[] dirInspect() { //need to inspect www folder on run
		String[] dirStructure = {};
		return dirStructure;
        }

        public static String[] parseReq (ArrayList req) { //need to parse incoming HTTP requests, make sure they're formatted, and respond
		String reqType, reqHost, reqObj;
		String firstLine = (String) req.get(0);
		String [] splitReq = firstLine.split(" ");
		reqType = splitReq[0];
		reqObj = splitReq[1];
		reqHost = (String)req.get(1);
		System.out.printf("The request is for %s of type %s from host: %s", reqObj, reqType, reqHost);
		String [] parsedReq= {reqObj, reqType, reqHost};
		return parsedReq;
        }

        public static void main(String[] args) {
            int port;
            String input;
            ArrayList req = new ArrayList();
            if (args.length!=1)
            {
                System.out.println("Please pass a port number in the form '--port=x'");
                return;
            }
            port = Integer.parseInt((args[0].split("="))[1]);
            try {
                System.out.printf("Starting server. Port: %d \n", port);
            }
            catch (Exception x) {
                System.out.print ("Exception");
            }
            try (ServerSocket serverS = new ServerSocket (port);
                        Socket clientS = serverS.accept();
                        PrintWriter echoOut = new PrintWriter(clientS.getOutputStream(), true);
                        BufferedReader echoIn = new BufferedReader(new InputStreamReader(clientS.getInputStream()));)
            {
                while ((input = echoIn.readLine()) != null) {
                    System.out.println(input);
                    echoOut.println(input);
                    req.add(input);
                    if (input=="\n") { //hit a newline char. this is the gap between header and the actual HTTP request if the syntax is right
                        input = echoIn.readLine();
                        req.add(input); //add the request body
                        parseReq(req);
                    }
                }
            }
            catch (Exception x) {
                System.out.printf ("Exception: %s", x);
            }


        }

}
