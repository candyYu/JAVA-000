package io.kimmking.rpcfx.api;

/**
 * Created by candy on 2020/12/17.
 */
public class RpcfxException extends Exception {

    public RpcfxException(Exception e) {
        super(e.getCause());
    }

    public RpcfxException(String msg){
        super(msg);
    }
}
