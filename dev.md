# java web应用

主要包括controller、pojo、service、util、resource包以及主启动类

## web结构及主要方法与参数介绍

### resource包

存放web应用的配置文件，本应用中存放连接kafka的配置文件：

![image-20220824171016987](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220824171016987.png)

```yaml
# sink端kafka服务器的IP地址和端口号
bootstrap-servers: 172.16.0.69:9092
# 数据发送到topic时的序列化方式，数据格式为String，因此选择 StringSerializer
value-serializer: Xxx.Xxx.StringSerializer
```

### controller包

对REST请求进行响应，从请求体中获取参数，调用service层方法进行处理

![image-20220826141059708](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220826141059708.png)

以POST方式发送请求，请求命令为：

![image-20220829115219302](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829115219302.png)

并携带JSON格式请求体SendRequest，可选择的参数如下：

```json
{
 发送时长，单位为S（秒）
 "time":
 与time属性一起配置时表示每秒发送的数据量，单位为条/S（秒）；单独配置时表示发送的数据量
 "frequency":
 指定数据发送到kafka中的哪些topic，可写多个topic(数组形式)，如果指定的topic不存在，会自动创建topic
 "topics":
 随机生成topic，指定生成的topic数量
 "topic_quantity":
 随机生成topic，指定生成的topic前缀
 "topic_prefix":
}
```

#### 参数配置

以上参数可以根据需要选择配置，关于参数配置做以下说明：

1. 参数无需全部配置，根据需要选择配置即可
2. 当配置了`"topics"`属性后，又配置了随机生成topic，则随机生成topic不会生效
3. 随机生成topic时，如果没有配置`"topic_quantity"`和`"topic_prefix"`，则会使用默认前缀"topic_"，并生成随机数量的topic(范围可在util包下的工具类中修改)
4. web应用启动后，使用相同的`"topic_prefix"`重复请求，只会生成一组topic，之后的请求都会使用这组topic。只有修改`"topic_prefix"`后，才会重新生成一组新的topic
5. `"time"`与`"frequency"`至少需要配置一个，否则无法发送数据
6. 当指定了`"time"`属性，而未指定`"frequency"`属性时，将会在发送时间内一直发送数据

### service包

调用生成随机数据的方法，按照不同方式将数据发送到指定topic

![sendByTime](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829115423431.png)

![sendByFrequency](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829115446639.png)

![sendDataCircularly](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829115549307.png)

![send](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829115718820.png)

```java
//将得到的数据库数据对象jsonDemo转换成json格式的字符串
//数据中会随机插入null，因此配置参数SerializerFeature.WriteMapNullValue，不忽略null
message = JSONObject.toJSONString(jsonDemo, SerializerFeature.WriteMapNullValue);
```

### pojo包

存放数据库数据对象：Schema、Filed、Payload、JsonDemo、LogMessage、SendRequest

#### Schema，Filed，Payload，JsonDemo

开启schema嵌入json选项后，数据库数据的json格式如下：

field：数据库数据的字段名及数据类型

payload：数据字段的实际值

```json
{
  "schema": {
    "type": "struct",
    "fields": [
      {
        "type": "int32",
        "optional": false,
        "field": "id"
      },
      {
        "type": "double",
        "optional": true,
        "field": "humidity"
      },
      {
        "type": "string",
        "optional": true,
        "field": "stringtime"
      },
    ],
    "optional": false,
    "name": "demo_table"
  },
  "payload": {
    "id": 100,
    "humidity": 25.700237994363352,
    "stringtime": "1970-01-16 02:30:59",
  }
}
```

#### SendRequest

向web应用发送请求时携带的请求体对象

```json
{
 发送时长，单位为S（秒）
 "time":
 与time属性一起配置时表示每秒发送的数据量，单位为条/S（秒）；单独配置时表示发送的数据量
 "frequency":
 指定数据发送到kafka中的哪些topic，可写多个topic(数组形式)，如果指定的topic不存在，会自动创建topic
 "topics":
 随机生成topic，指定生成的topic数量
 "topic_quantity":
 随机生成topic，指定生成的topic前缀
 "topic_prefix":
}
```

#### LogMessage

日志对象，每一次请求结束后生成日志对象，并输出日志到控制台

![image-20220829120447970](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829120447970.png)

### util包

存放获得数据库对象、生成随机数据并赋值、随机生成topic等方法的工具类

![image-20220829120603049](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829120603049.png)

![image-20220829120614402](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829120614402.png)

![image-20220829120637830](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829120637830.png)

使用Kafka提供的AdminClient类型对象创建Topic：

![image-20220831165550573](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220831165550573.png)

AdminClient使用完之后可以手动关闭，如果没有关闭，在一段时间没有收到请求后，服务器会自动关闭空闲的AdminClient，这个最大空闲时间可以通过以下参数配置：

```properties
#可以配置在broker和客户端，broker和客户端都会关闭空闲太久的连接，默认值为540000ms
connections.max.idle.ms
```

 

### Config包

存放Kafka配置相关，实现配置文件读取，返回操作Topic的对象

![image-20220830174852830](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220830174852830.png)

KafkaProperties类被@ConfigurationProperties注解标记，会将配置文件中的属性值赋给被标记类中的属性

![image-20220831161042313](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220831161042313.png)

使用注解开启配置文件文件与java bean的绑定：

```java
@EnableConfigurationProperties(KafkaProperties.class)
```

之后，SpringBoot会自动读取resource包下的application前缀的配置文件，并使用其中的属性创建一个KafkaProperties

使用KafkaProperties得到操作Kafka中Topic的对象：AdminClient

![image-20220831163233931](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220831163233931.png)

## 编译

```bash
mvn package
```

### 使用jar包启动

进入到jar包所在的路径，例如：

`D:\Users\fgdth\IdeaProjects\kafka-test\target`

右键空白处选择在终端中打开，输入命令：

`java -jar kafka-test-0.0.1-SNAPSHOT.jar`

出现以下界面说明启动成功

![命令行启动web](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220824160206446.png)

### 在IDE中启动

以IntelliJ IDEA为例：

1.使用pom.xml文件将web项目导入IDEA中，找到项目主启动类并运行即可：

![主启动类](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220824154439142.png)

出现以下界面说明启动成功：

![idea启动web界面](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220824160439062.png)

## 使用web应用发送数据

启动web应用后，可以通过向web发送请求实现数据的生成，并发送到指定的topic中，sink可以从topic中读取数据并写入数据库

### 通过类postman工具向web发送请求

以下操作以ApiFox为例

启动apifox，并新建项目：

![image-20220824162939489](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220824162939489.png)

选择快捷请求：

![image-20220824163021219](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220824163021219.png)

通过配置请求体的参数，可以使用不同的方式向topic中发送数据

#### 指定发送时间以及单位时间数据量（随机topic）

在地址栏输入命令：

  `localhost:8080/send`

选择post请求方式，并配置请求体，例如：

![image-20220829151057595](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829151057595.png)

出现以下界面说明请求成功：

![image-20220829151127377](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829151127377.png)

指定topic时，将参数`"topics"`配置进请求体即可，例如：

![image-20220829152647310](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829152647310.png)

#### 指定发送数据量（随机topic）

在地址栏输入命令：

`localhost:8080/send`

选择post请求方式，并配置请求体，例如：

![image-20220829152341850](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829152341850.png)

出现以下界面说明请求成功：

![image-20220829152408179](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829152408179.png)

指定topic时，将参数`"topics"`配置进请求体即可
