package cc.page.study.week9.practice2.rpcfx.core.exception;

import lombok.*;

/**
 * 简单封装服务端异常
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class RpcException extends RuntimeException {

    private String message;

    private Throwable cause;

    public RpcException(String message) {
        super(message);
        this.message = message;
    }

    public RpcException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
        this.cause = null;
    }
}
