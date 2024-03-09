package org.cloud.sonic.controller.transport;

import jakarta.websocket.*;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.tools.BytesTool;

import java.nio.ByteBuffer;

@ClientEndpoint
@Slf4j
public class HubClient {

    @OnOpen
    public void onOpen(Session session) {
        log.info("hub server connect to agent");
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        //forward message to browser
        Session browser = BytesTool.sessionList.getBrowserSessionByClientSessionId(session.getId());
        BytesTool.send(browser, message);
    }

    @OnMessage
    public void onMessage(Session session, byte[] message) {
        Session browser = BytesTool.sessionList.getBrowserSessionByClientSessionId(session.getId());
        BytesTool.send(browser, message);
    }

//    @OnMessage
//    public void onMessage(Session session, ByteBuffer message){
//        Session browser = BytesTool.sessionList.getBrowserSessionByClientSessionId(session.getId());
//        BytesTool.send(session, message);
//    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("forward agent message to browser error", throwable);
    }

    @OnClose
    public void onClose(Session session) {
        BytesTool.sessionList.removeByClientSessionId(session.getId());
    }
}
