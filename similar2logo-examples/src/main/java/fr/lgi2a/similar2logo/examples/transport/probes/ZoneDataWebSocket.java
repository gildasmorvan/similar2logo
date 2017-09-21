package fr.lgi2a.similar2logo.examples.transport.probes;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

/**
 * Web socket for getting the data of traffic of each zone
 * @author <a href="mailto:romainwindels@yahoo.fr">Romain Windels</a>
 *
 */
@WebSocket
public class ZoneDataWebSocket {
	
	/**
	 * The current sessions
	 */
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();
    
    /**
     * <code>true</code> if the server is launched
     */
    public static boolean wsLaunch = false;
    
    /**
     * Adds a user that connects to the server
     * @param session the current session
     */
    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        wsLaunch = true;
    }

    /**
     * Sends the JSON data to all users
     */
    public static void sendJsonProbe(String JSONData){
    	for (Session session : sessions) {
			session.getRemote().sendStringByFuture(JSONData);
    	}
	}

    /**
     * Removes an user that disconnects from the server
     * @param session current session of the user
     * @param statusCode disconnection code
     * @param reason Reason of the disconnection
     */
	@OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

}
