见package：cc.page.study.week11.practice9

postman调用post请求：
* http://localhost:9300/order
``` json 
{
    "itemId": "erwerwer",
    "count": 10
}
```
* 先将对象序列化，然后 jedis.publish 将消息发送到 redis 
* jedis.subscribe 订阅消息，将消息反序列化为 java 对象，调用 service 方法完成订单异步处理