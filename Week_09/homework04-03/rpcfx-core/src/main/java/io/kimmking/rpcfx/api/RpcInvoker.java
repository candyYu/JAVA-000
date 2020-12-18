package io.kimmking.rpcfx.api;

/**
 * Created by candy on 2020/12/17.
 */
public abstract class RpcInvoker {

    public abstract Object invoke(Object instance, String method, Object[] params);

}
