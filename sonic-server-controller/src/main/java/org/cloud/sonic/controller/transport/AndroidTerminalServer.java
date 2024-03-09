package org.cloud.sonic.controller.transport;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.config.WsEndpointConfigure;
import org.cloud.sonic.controller.tools.BytesTool;
import org.cloud.sonic.controller.tools.TransPortTool;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@ServerEndpoint(value = "/android/terminal/{agentKey}/{udId}/{token}/{agentIp}/{agentPort}", configurator = WsEndpointConfigure.class)
public class AndroidTerminalServer {

    @OnOpen
    public void onOpen(Session session, @PathParam("agentKey") String agentKey, @PathParam("udId") String udId,
                       @PathParam("token") String token, @PathParam("agentIp") String agentIp,
                       @PathParam("agentPort") Integer agentPort) {
        String agentUri = String.format("ws://%s:%s/websockets/android/terminal/%s/%s/%s", agentIp, agentPort, agentKey, udId, token);
        TransPortTool.initTransport(agentUri, session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        //forward browser message to agent
        Session client = BytesTool.sessionList.getClientSessionByBrowserSessionId(session.getId());
        BytesTool.send(client, message);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        Session client = BytesTool.sessionList.getClientSessionByBrowserSessionId(session.getId());
        BytesTool.send(client, message);
    }

    @OnClose
    public void onClose(Session session) {
        BytesTool.sessionList.removeByBrowserSessionId(session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("forward message from browser to agent error", throwable);
    }
}
