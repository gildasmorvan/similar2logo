package fr.lgi2a.similar2logo.lib.tools.http;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

@WebSocket
public class GridWebSocket {
	
	/**
	 * Contains the current sessions
	 */
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    
    /**
     * To know if the server is launch
     */
    public static boolean wsLaunch = false;
    
    /**
     * When a user connect on the server
     * @param session is a current session
     */
    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        wsLaunch = true;
    }

    /**
     * To send the JsonProbe at all users
     */
    public static void sendJsonProbe(){
    	for (Session session : sessions) {
			try {
				session.getRemote().sendString(SparkHttpServer.jSONProbe.getOutput());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
	}

    /**
     * When the user disconnect on the server
     * @param session is a current session of the user
     * @param statusCode is a code of disconnection
     * @param reason is the reason of this disconnection
     */
	@OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }
}