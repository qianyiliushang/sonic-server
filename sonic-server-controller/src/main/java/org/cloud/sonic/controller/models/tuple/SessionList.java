package org.cloud.sonic.controller.models.tuple;

import jakarta.websocket.Session;

import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class SessionList extends CopyOnWriteArrayList<Tuple> {
    public Session getBrowserSessionByClientSessionId(String sessionId) {
        Optional<Tuple> tuple = this.stream().filter(item -> item.clientSessionId().equals(sessionId)).findFirst();
        return tuple.map(Tuple::browserSession).orElse(null);
    }

    public Session getClientSessionByBrowserSessionId(String sessionId) {
        Optional<Tuple> tuple = this.stream().filter(item -> item.browserSessionId().equals(sessionId)).findFirst();
        return tuple.map(Tuple::clientSession).orElse(null);
    }

    public void removeByBrowserSessionId(String sessionId) {
        if (sessionId == null) {
            return;
        }
        Optional<Tuple> tuple = this.stream().filter(item -> item.browserSessionId().equals(sessionId)).findAny();
        if (tuple.isPresent()) {
            this.remove(tuple);
        }
    }

    public void removeByClientSessionId(String sessionId) {
        if (sessionId == null) {
            return;
        }
        Optional<Tuple> tuple = this.stream().filter(item -> item.clientSessionId().equals(sessionId)).findFirst();
        if (tuple.isPresent()) {
            this.remove(tuple);
        }
    }
}
