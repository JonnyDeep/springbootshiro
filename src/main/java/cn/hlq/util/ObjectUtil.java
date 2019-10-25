package cn.hlq.util;

import java.io.*;

public class ObjectUtil {

    public static byte[] Serializable(Object o) throws Exception {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        }catch (Exception ex){
            throw new Exception("序列化失败");
        }
        return bytes;
    }

    public static Object UnSerializable(byte[] bytes) throws Exception {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        }catch (IOException ex){
            throw new Exception("序列化失败");
        }catch (ClassNotFoundException ex){
            throw new Exception("没有该对象");
        }

        return obj;
    }

}
