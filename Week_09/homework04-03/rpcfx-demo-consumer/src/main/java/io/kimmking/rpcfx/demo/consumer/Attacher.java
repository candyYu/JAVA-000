package io.kimmking.rpcfx.demo.consumer;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * Created by candy on 2020/12/22.
 */
public class Attacher {

    public static void main(String[] args) throws AttachNotSupportedException, IOException, AgentLoadException, AgentInitializationException {
        // 传入目标 JVM pid
        VirtualMachine vm = VirtualMachine.attach("73920");
        vm.loadAgent("/Volumes/Transcend/project/own/JAVA-000/Week_09/homework04-03/rpcfx-demo-consumer/src/main/java/operation-server.jar");
    }


}
