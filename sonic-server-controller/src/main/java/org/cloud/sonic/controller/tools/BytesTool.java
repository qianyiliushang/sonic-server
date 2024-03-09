/*
 *   sonic-server  Sonic Cloud Real Machine Platform.
 *   Copyright (C) 2022 SonicCloudOrg
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published
 *   by the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.cloud.sonic.controller.tools;

import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.cloud.sonic.controller.models.tuple.SessionList;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BytesTool {
    public static Map<Integer, Session> agentSessionMap = new HashMap<>();
    public static final SessionList sessionList = new SessionList();

    public static final ThreadPoolTaskExecutor excutor = (ThreadPoolTaskExecutor) SpringTool.getBean("messageThreadPool");

    public static void sendText(Session session, String message) {
        if (session == null || !session.isOpen()) {
            return;
        }
        synchronized (session) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IllegalStateException | IOException e) {
                log.error("WebSocket send msg failed...");
            }
        }
    }

    public static void send(Session session, String message) {
        if (session == null || !session.isOpen()) {
            return;
        }
        excutor.submit(() -> {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("transform message failed", e);
            }
        });
    }

    public static void send(Session session, byte[] message) {
        if (session == null || !session.isOpen()) {
            return;
        }
        excutor.submit(() -> {
            try {
                session.getBasicRemote().sendBinary(ByteBuffer.wrap(message));
            } catch (IOException e) {
                log.error("transform byte message error ", e);
            }
        });
    }

    public static void send(Session session, ByteBuffer message) {
        if (session == null || !session.isOpen()) {
            return;
        }
        excutor.submit(() -> {
            try {
                session.getBasicRemote().sendBinary(message);
            } catch (IOException e) {
                log.error("transform byte message error ", e);
            }
        });
    }
}
