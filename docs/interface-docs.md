# RocketMQ Http Proxy 接口文档

#### 发送消息

URI: /rmqpxy/send

METHOD: POST

| 参数 | 类型 | 是否必填 | 说明 |
| --- | --- | --- | --- |
|message | object | 必填 | 消息对象 |

参数样例：
```$xslt
{
    "message" : {
        "topic" : "topicA",
        "flag" : 0,
        "properties" : {
            "p1" : "val1"
        },
        "transactionId" : "",
        "body" : "this is body"
    },
    "queue" : {
        "id" : 0,
        "brokerName" : "broker-1"
    },
    "group" : "producer-group",
    "username" : "",
    "password" : ""
}
```
