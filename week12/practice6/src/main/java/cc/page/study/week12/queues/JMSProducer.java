package cc.page.study.week12.queues;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JMSProducer {

    private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认用户名
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD; // 默认密码
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL; // 默认连接地址
    private static final int SENDNUM = 10; // 定义发送的消息数量

    public static void main(String[] args) {
        ConnectionFactory connectionFactory; // 连接工厂，用来生产Connection
        Connection connection = null; // 连接
        Session session; // 会话，接收或者发送消息的线程
        Destination destination; // 消息的目的地
        MessageProducer messageProducer; // 消息发送者

        // 实例化连接工厂
        connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWORD, BROKEURL);
        try {
            connection = connectionFactory.createConnection(); // 通过连接工厂获取连接
            connection.start();  // 启动连接
            session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE); // 获取Session
            destination = session.createQueue("FirstQueue1"); // 创建消息队列，名为FirstQueue1
            messageProducer = session.createProducer(destination); // 创建消息生产者
            sendMessage(session, messageProducer); // 发送消息
            session.commit(); // 因为上面加了事务Boolean.TRUE表示有事务，所以要commit
        } catch (JMSException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if(connection != null){
                try {
                    connection.close();
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendMessage(Session session, MessageProducer messageProducer) throws JMSException {
        for(int i = 0; i < JMSProducer.SENDNUM; i++) {
            TextMessage message = session.createTextMessage("ActiveMQ 发送的消息" + i);
            System.out.println("ActiveMQ 发送的消息" + i);
            messageProducer.send(message);
        }
    }
}
