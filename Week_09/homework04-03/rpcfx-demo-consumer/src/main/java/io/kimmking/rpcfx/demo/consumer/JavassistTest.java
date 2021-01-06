package io.kimmking.rpcfx.demo.consumer;

import javassist.*;

import java.io.IOException;

/**
 * Created by candy on 2020/12/22.
 */
public class JavassistTest {

    public static void main(String[] args) throws NotFoundException, CannotCompileException, IllegalAccessException, InstantiationException, IOException {
        ClassPool cp = ClassPool.getDefault();
        CtClass cc = cp.get("io.kimmking.rpcfx.demo.consumer.Base");
        CtMethod m = cc.getDeclaredMethod("process");
        m.insertBefore("{ System.out.println(\"start\"); }");
        m.insertAfter("{ System.out.println(\"end\"); }");
        Class c = cc.toClass();
        cc.writeFile("/Volumes/Transcend/project/own/JAVA-000/Week_09/homework04-03/rpcfx-demo-consumer/target/classes");
        Base h = (Base)c.newInstance();
        h.process();
    }
}

