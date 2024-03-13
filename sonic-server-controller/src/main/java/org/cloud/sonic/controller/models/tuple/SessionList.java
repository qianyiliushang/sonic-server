package org.cloud.sonic.controller.models.tuple;

import jakarta.websocket.Session;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SessionList extends CopyOnWriteArrayList<Tuple> {
    public Session getBrowserSessionByClientSessionId(String sessionId) {
        Optional<Tuple> tuple = this.stream().filter(item -> item.agentSessionId().equals(sessionId)).findFirst();
        return tuple.map(Tuple::browserSession).orElse(null);
    }

    public Session getClientSessionByBrowserSessionId(String sessionId) {
        Optional<Tuple> tuple = this.stream().filter(item -> item.browserSessionId().equals(sessionId)).findFirst();
        return tuple.map(Tuple::agentSession).orElse(null);
    }

    public void removeByBrowserSessionId(String sessionId) {
        if (sessionId == null) {
            return;
        }
        Optional<Tuple> tuple = this.stream().filter(item -> item.browserSessionId().equals(sessionId)).findAny();
        if (tuple.isPresent()) {
            closeSession(tuple.get());
            this.remove(tuple);
        }
    }

    public void removeByClientSessionId(String sessionId) {
        if (sessionId == null) {
            return;
        }
        Optional<Tuple> tuple = this.stream().filter(item -> item.agentSessionId().equals(sessionId)).findFirst();
        if (tuple.isPresent()) {
            closeSession(tuple.get());
            this.remove(tuple);
        }
    }

    private void closeSession(Tuple tuple) {

        try( Session agentSession = tuple.agentSession();Session browserSession = tuple.browserSession()){
            if (agentSession.isOpen()){
                log.info("close agent session:{}", tuple.agentSessionId());
            }
            if (browserSession.isOpen()){
                log.info("close browser session:{}", tuple.browserSessionId());
            }
        } catch (IOException ioException) {
            log.error("close session failed:{}", ioException.getMessage());
        }
    }
}
