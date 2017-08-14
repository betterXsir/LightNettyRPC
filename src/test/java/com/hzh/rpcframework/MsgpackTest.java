package com.hzh.rpcframework;

import com.hzh.rpcframework.entity.MessageReq;
import com.hzh.rpcframework.entity.MessageResp;
import org.msgpack.MessagePack;
import org.msgpack.annotation.Message;

import java.io.Serializable;
import java.util.ArrayList;

public class MsgpackTest {
    public static void main(String[] args) {
        MessagePack pack = new MessagePack();
        User user = new User(1, "name", "password");
        MessageResp msg = new MessageResp();

        msg.setStatus("io.netty.buffer.ByteBuf");
        msg.setMessageId(2);
        msg.setResult("write");
        try {
            System.out.println("单个对象使用Msgpack");
            System.out.println("序列化前: " + msg.toString());
            // 序列化
            byte[] bytes = pack.write(msg);
            // 反序列化
            MessageResp s = pack.read(bytes, MessageResp.class);
            System.out.println("反序列化: " + s.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        //ArrayList
        try {
            System.out.println("ArrayList使用Msgpack");
            ArrayList<User> list = new ArrayList<User>();
            list.add(user);
            System.out.println("序列化前: " + list.get(0).getId());
            // 序列化
            byte[] bytesList = pack.write(list);
            // 反序列化
            @SuppressWarnings("unchecked")
            ArrayList<User> lists = pack.read(bytesList, ArrayList.class);
            System.out.println("反序列化: " + lists.get(0).getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
        //Vector
//        try {
//            System.out.println("Vector使用Msgpack");
//            Vector<User> list = new Vector<User>();
//            list.add(user);
//            System.out.println("序列化前: " + list.get(0).getId());
//            // 序列化
//            byte[] bytesList = pack.write(list);
//            // 反序列化
//            @SuppressWarnings("unchecked")
//            Vector<User> lists = pack.read(bytesList, Vector.class);
//            System.out.println("反序列化: " + lists.get(0).getId());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println();
        //Map
//        try {
//            System.out.println("Map使用Msgpack");
//            Map<String, User> map = new HashMap<String, User>();
//            map.put("user", user);
//            System.out.println("序列化前: " + map.get("user"));
//            // 序列化
//            byte[] bytesList = pack.write(map);
//            // 反序列化
//            @SuppressWarnings("unchecked")
//            Map<String, User> maps = pack.read(bytesList, Map.class);
//            System.out.println("反序列化: " + maps.get("user"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    @Message
    static class User implements Serializable{
        private static final long serialVersionUID = -5848295770696335660L;
        private int id;
        private String name;
        private transient String password;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }
        public User(int id, String name, String password) {
            this.id = id;
            this.name = name;
            this.password = password;
        }

        @Override
        public String toString() {
            return "User [id=" + id + ", name=" + name + ", password=" + password
                    + "]";
        }
        public User() {

        }
    }
}
