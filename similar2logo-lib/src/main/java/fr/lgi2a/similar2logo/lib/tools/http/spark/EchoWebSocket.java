package fr.lgi2a.similar2logo.lib.tools.http.spark;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class EchoWebSocket {

    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    public static boolean wsLaunch = false;

    static ScheduledExecutorService timer = 
    	       Executors.newSingleThreadScheduledExecutor();
    
    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        System.out.println("Connected !!!");
        wsLaunch = true;
    }

    public static void sendJsonProbe(){
    	for (Session session : sessions) {
			try {
				session.getRemote().sendString(SparkHttpServer.jSONProbe.getOutput().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}

	@OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
        System.out.println("Disconnected !!!");
    }
	
	@OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        System.out.println("Got: " + message);
        session.getRemote().sendString(message);
    }
}