package cc.page.study.week9.practice2.rpcfx.core.api;

import lombok.Data;

@Data
public class RpcRequest {
  private String serviceClass;
  private String method;
  private Object[] params;
}
