package fr.lgi2a.similar2logo.lib.tools.http.spark;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class EchoWebSocket {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    static ScheduledExecutorService timer = 
    	       Executors.newSingleThreadScheduledExecutor();
    
    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        System.out.println("Connected !!!");
        timer.scheduleAtFixedRate(
                () -> sendJsonProbe(session)
				,0,50,TimeUnit.MILLISECONDS);
    }

    private void sendJsonProbe(Session session){
    	try {
			session.getRemote().sendString(SparkHttpServer.jSONProbe.getOutput().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
        System.out.println("Disconnected !!!");
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
    	session.getRemote().sendString(message);
    }
}