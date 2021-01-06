package io.kimmking.rpcfx.demo.consumer;


import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by candy on 2020/12/21.
 */
public class SimpleGeneric {

    public static void main(String[] args) throws Exception {
        //读取类文件
        ClassReader classReader = new ClassReader("io.kimmking.rpcfx.demo.consumer.Base");
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        //处理，通过classVisitor修改类
        ClassVisitor classVisitor = new SimpleClassVisitor(classWriter);
        classReader.accept(classVisitor, ClassReader.SKIP_DEBUG);
        byte[] data = classWriter.toByteArray();
        //保存新的字节码文件
        //target/classes/io/kimmking/rpcfx/demo/consumer/
        File file = new File("target/classes/io/kimmking/rpcfx/demo/consumer/Base.class");
        FileOutputStream outputStream = new FileOutputStream(file);
        outputStream.write(data);
        outputStream.close();
        System.out.println("generator new class file success.");

        Base base = new Base();
        base.process();
    }


}
