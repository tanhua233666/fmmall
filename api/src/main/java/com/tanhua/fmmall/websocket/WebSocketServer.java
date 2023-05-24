package com.tanhua.fmmall.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint("/webSocket/{oid}")
public class WebSocketServer {

    private static ConcurrentHashMap<String,Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 前端发送请求建立websocket连接，就会执行@OnOpen
     */
    @OnOpen
    public void open(@PathParam("oid") String orderId, Session session){
        System.out.println("连接+1:" + orderId);
        sessionMap.put(orderId,session);
    }

    @OnClose
    public void close(@PathParam("oid") String orderId){
        System.out.println("连接销毁");
        sessionMap.remove(orderId);
    }

    public static void sendMsg(String orderId,String msg){
        Session session = sessionMap.get(orderId);
        try {
            session.getBasicRemote().sendText(msg);
            System.out.println("消息发送");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
