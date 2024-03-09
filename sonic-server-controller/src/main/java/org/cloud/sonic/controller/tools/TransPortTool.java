package org.cloud.sonic.controller.tools;

import jakarta.websocket.ContainerProvider;
import jakarta.websocket.DeploymentException;
import jakarta.websocket.Session;
import jakarta.websocket.WebSocketContainer;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.models.tuple.Tuple;
import org.cloud.sonic.controller.transport.HubClient;

import java.io.IOException;
import java.net.URI;

@Slf4j
public class TransPortTool {
    public static void initTransport(String agentUri, Session session) {

        try {
            log.info("agentUri:{}",agentUri);
            URI uri = new URI(agentUri);
            connectToAgent(session, uri);
        } catch (Exception e) {
            log.error("create hub client failed:", e);
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(1000);
                    log.info("reconnect {} times,uri:{}", i, agentUri);
                    URI uri = new URI(agentUri);
                    connectToAgent(session, uri);
                    break;
                } catch (Exception ex) {
                    log.error("{} times connection failed，uri:{}", i, agentUri, ex);
                }
                Session agentSession = BytesTool.sessionList.getClientSessionByBrowserSessionId(
                        session.getId());
                if (agentSession == null) {
                    log.info("agentSession is null");
                    BytesTool.sessionList.removeByBrowserSessionId(session.getId());
                    throw new RuntimeException("agentSession is null");
                }
            }

        }
    }

    private static void connectToAgent(Session session, URI uri) throws DeploymentException, IOException {
        Session clientSession = null;
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxSessionIdleTimeout(600 * 1000L);
        clientSession = container.connectToServer(HubClient.class, uri);
        log.info("create hub client successful:wsURL:{},sessionId:{},{}", uri.toString(),
                clientSession.getId(), session.isOpen());
        Tuple sessionTuple = new Tuple(session.getId(), session,
                clientSession.getId(), clientSession);
        BytesTool.sessionList.add(sessionTuple);
    }


}
