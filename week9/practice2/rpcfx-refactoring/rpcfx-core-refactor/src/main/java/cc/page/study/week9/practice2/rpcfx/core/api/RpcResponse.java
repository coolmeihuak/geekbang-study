package cc.page.study.week9.practice2.rpcfx.core.api;

import lombok.Data;

@Data
public class RpcResponse {
    private Object result;
    private Boolean status;
    private Exception exception;
}
