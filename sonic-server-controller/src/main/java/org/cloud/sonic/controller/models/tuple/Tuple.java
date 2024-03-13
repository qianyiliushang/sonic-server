package org.cloud.sonic.controller.models.tuple;

import jakarta.websocket.Session;

/**
 * 用来保存浏览器到agent websocket session的数据结构
 *
 * @param browserSessionId 浏览器sessionId
 * @param browserSession    浏览器session
 * @param agentSessionId  代理sessionId
 * @param agentSession    代理session
 */

public record Tuple(String browserSessionId, Session browserSession, String agentSessionId, Session agentSession) {

}
