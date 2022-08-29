# Kafka JDBC Connector Demo

## 简介

Kafka JDBC Sink connector 可以从kafka的topics中导出数据到任何具有JDBC驱动的关系型数据库 (Mysql, Oracle, PostgreSQL, SQLite, SQL server, Opengauss...)。

![JDBC sink flow char](https://raw.githubusercontent.com/a525076133/kafka-images/main/sink.png)

## 安装流程

### 安装kafak

#### macos

kafka官网下载

[kafka官网]:https://kafka.apache.org
[下载链接]:<https://dlcdn.apache.org/kafka/3.2.1/kafka_2.13-3.2.1.tgz>

下载完成后并解压

![unzip](https://raw.githubusercontent.com/a525076133/kafka-images/main/kafka-unzip.png)

![unzip-content](https://raw.githubusercontent.com/a525076133/kafka-images/main/unzip.png)

#### window



#### Linux



### 数据库驱动

下载相应数据库的JDBC驱动，以opengauss为例。

[opengauss官网]:<https://www.opengauss.org/zh/download.html>

![download jdbc](https://raw.githubusercontent.com/a525076133/kafka-images/main/opengauss-jdbc.png)

下载完成后解压，得到相应jar包。

![jars](https://raw.githubusercontent.com/a525076133/kafka-images/main/jdbc-jar.png)

复制jar包到/your-kafka-path/libs下

如：`/usr/local/Cellar/kafka_2.12-3.2.1/libs`

![copy-jar](https://raw.githubusercontent.com/a525076133/kafka-images/main/copy-jar.png)

### Kafka JDBC Connector

下载JDBC Connector

[Confluent JDBC Connector]:<https://www.confluent.io/hub/confluentinc/kafka-connect-jdbc?_ga=2.140576818.1269349503.1660542741-1354758524.1659497119>

![confluent-jdbc-connector](https://raw.githubusercontent.com/a525076133/kafka-images/main/jdbc-connector.png)

下载完成后解压到任意位置，需要记住，后续需要使用。

![confluent-unzip](https://raw.githubusercontent.com/a525076133/kafka-images/main/confluent-unzip.png)

## 修改配置文件

#### config/server.properties

编辑kafka目录下的`config/server.properties`文件。

修改或添加`listeners=PLAINTEXT://0.0.0.0:9092`，否则将无法监听外网请求。

修改或添加`advertised.listeners=PLAINTEXT://your.host.name:9092`，your.host.name为本机的ip地址，否则外部网络将无法访问您的kafka。

保存修改并退出。

#### config/connect-distributed.properties

编辑kafka目录下的`config/connect-distributed.properties`文件。

添加或修改`plugin.path=your.confluent-kafka-connect-jdbc.path`,`your.confluent-kafka-connect-jdbc.path`为您存放confluent-kafka-connect-jdbc文件夹的位置。如`plugin.path=/usr/local/Cellar/confluentinc-kafka-connect-jdbc-10.5.2`

保存修改并退出。

## 启动程序

打开cmd/terminal并cd到kafka目录下。

如：`cd /usr/local/Cellar/kafka_2.12-3.2.1`

1) 启动zookeeper

`sh bin/zookeeper-server-start.sh config/zookeeper.properties`

2) 启动kafka

`sh bin/kafka-server-start.sh config/server.properties`

输入jps查看zookeeper和kafka启动情况

![zk_and_kafka_status](https://raw.githubusercontent.com/a525076133/kafka-images/main/zk_and_kafka_start.png)

若能查看到这两个进程，表明启动成功。否则请结合日志查找原因。

3) 启动Kafka Connect Worker

`sh bin/connect-distributed.sh config/connect-distributed.properties`

前台启动看到类似下图的消息，表明启动成功。

![connect_distributed](https://raw.githubusercontent.com/a525076133/kafka-images/main/worker_status.png)

通过jps可以查看到ConnectDistributed进程运行。

![ConnectDistributed_jps](https://raw.githubusercontent.com/a525076133/kafka-images/main/connect_distributed_jps.png)

4) 启动JDBC Sink Connector

**方式一**：使用在 `终端/cmd` 启动JDBC sink connector

在终端输入

```bash
curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" -d '{
    "name":"connector-name",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:opengauss://localhost:15432/OpengaussDB",
        "connection.user":"user",
        "connection.password":"password",
        "topics":"test_topic",
        "insert.mode": "insert",
        "table.name.format":"table_name_${topic}",
        "auto.create":true,
        "dialect.name": "PostgreSqlDatabaseDialect"
    }
}'
```

**方式二**：创建json文件配置JDBC Sink Connector

创建json文件，名为`test.json`，内容如：

```json
{
    "name":"connector-name",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:opengauss://localhost:15432/OpengaussDB",
        "connection.user":"user",
        "connection.password":"password",
        "topics":"test_topic",
        "insert.mode": "insert",
        "table.name.format":"table_name_${topic}",
        "auto.create":true,
        "dialect.name": "PostgreSqlDatabaseDialect"
    }
}
```

在 `终端/cmd` 发送 `curl -X POST http://localhost:8083/connectors -H "Content-Type: application/json" -d @test.json`。

**方式三**：使用Postman等接口测试工具

![postman](https://raw.githubusercontent.com/a525076133/kafka-images/main/postman.png)

设置好参数后发送post请求。

出现类似如下的消息表示Connector启动成功。

```json
{
  "name": "connector-name",
  "config": {
    "connector.class": "io.confluent.connect.jdbc.JdbcSinkConnector",
    "connection.url": "jdbc:opengauss://localhost:15432/sink",
    "connection.user": "user1",
    "connection.password": "Enmo@123",
    "topics": "test",
    "insert.mode": "insert",
    "table.name.format": "tableName_${topic}",
    "auto.create": "true",
    "dialect.name": "PostgreSqlDatabaseDialect",
    "name": "connector-name"
  },
  "tasks": [],
  "type": "sink"
}
```

## 参数说明

所有参数和说明请参考下述链接。

https://docs.confluent.io/kafka-connectors/jdbc/current/sink-connector/sink_config_options.html

下面介绍本例使用的参数和一些常见参数。

```json
"connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector"
```

connector class，此例使用confluent包，所以使用confluent包下的JDBCSinkConnector。

```json
"connection.url":"jdbc:opengauss://localhost:15432/OpengaussDB"
```

 数据库connection url，参数值为要连接的数据库connection url。

```json
"connection.user":"user"
```

数据库用户名。

```json
"connection.password":"password"
```

数据库密码。

```json
"topics":"test_topic1, test_topic2, test_topic3"
```

连接kafka的topic，可指定多个，用逗号分隔。

```json
"topics.regex":"topic-.*"
```

topic的正则表达式，如`topic-.*`可以匹配所有前缀为`topic-`的topics。

不支持多个正则表达式，若有多个需要启动另外的connector。

在一个connector配置中，`topics`和`topics.regex`只能使用一个。

```json
"table.name.format":"table_name_${topic}"
```

目标表名称的字符串格式，占位符'${topic}'表示原topic名。

```json
"auto.create":true
```

当目标表不存在时，是否自动创建表。默认为false。当该属性为false或不配置时，需要手动建表，表结构与source端协商，字段名相同且类型兼容时才能正常接收到数据。

```json
"dialect.name": "PostgreSqlDatabaseDialect"
```

应用于此connector的数据库方言，默认为空。Connector会根据 JDBC 连接 URL 自动确定。若想覆盖并使用特定方言，使用此选项。

> 有效值：[, Db2DatabaseDialect, MySqlDatabaseDialect, SybaseDatabaseDialect, GenericDatabaseDialect, OracleDatabaseDialect, SqlServerDatabaseDialect, PostgreSqlDatabaseDialect, SqliteDatabaseDialect, DerbyDatabaseDialect, SapHanaDatabaseDialect, MockDatabaseDialect, VerticaDatabaseDialect]

___此例使用的OpenGauss数据库，是基于PostgreSQL内核的。自动确定方言时不会选择PostgreSqlDatabaseDialect，执行某些操作时会出现错误。若使用OpenGauss数据库建议手动设置该项。___

```json
"insert.mode": "insert"
```

插入方式。"insert", "upsert", "update"，三个值可选。

> "insert"：使用标准SQL insert语句（默认）。

> "upsert":  使用相应的upsert语义。当使用upsert时必须添加pk.mode和pk.fields属性。

> "update":使用相应的update语义，例如update。

```json
"pk.mode":"record_value"
```

  指定主键模式。

>  none: 不使用（默认）

>  kafka: 使用Apache Kafka coordinates作为主键，详情请查看官方文档。

>  record_key: 使用record key的字段作为主键，可以是基本数据类型或struct。

>  record_value: 使用record value的字段作为主键, 必须是struct.

```json
"pk.fields":"id"
```

The runtime interpretation of this config depends on the pk.mode

用逗号分隔的主键字段名列表。该配置运行时解释取决于pk.mode的值。

> none: 此模式下没有字段用作主键。

> kafka: 详情请查看官方文档。

> record_key: 如果为空，key struct的所有字段将被作为主键。否则提取所输入的字段。

> record_value: 如果为空，value struct的所有字段将被作为主键。否则提取所输入的字段

_例如，假设有如下配置：_

```json
"pk.mode":"record_value",

"pk.fields":"id"
```

_则表示，使用record value中的"id"字段作为主键。_

```json
"delete.enabled"=true
```

是否删除值为null的记录。使用该配置时，要求`pk.mode=record_key`。

```json
"fields.whitelist"="id,temperature"
```

使用白名单中的字段，用逗号分隔。如果为空， record value的所有字段将被使用。默认为空。

**`transforms`等参数将在案例中介绍。**

## java web应用

主要包括controller、pojo、service、util、resource包以及主启动类

### web结构及主要方法与参数介绍

#### resource包

存放web应用的配置文件，本应用中存放连接kafka的配置文件：

![image-20220824171016987](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220824171016987.png)

```yaml
sink端kafka服务器的IP地址和端口号
bootstrap-servers: 172.16.0.69:9092
数据发送到topic时的序列化方式，数据格式为String，因此选择
StringSerializer
value-serializer: Xxx.Xxx.StringSerializer
```

#### controller包

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

##### 参数配置

以上参数可以根据需要选择配置，关于参数配置做以下说明：

1. 参数无需全部配置，根据需要选择配置即可
2. 当配置了`"topics"`属性后，又配置了随机生成topic，则随机生成topic不会生效
3. 随机生成topic时，如果没有配置`"topic_quantity"`和`"topic_prefix"`，则会使用默认前缀"topic_"，并生成随机数量的topic(范围可在util包下的工具类中修改)
4. web应用启动后，使用相同的`"topic_prefix"`重复请求，只会生成一组topic，之后的请求都会使用这组topic。只有修改`"topic_prefix"`后，才会重新生成一组新的topic
5. `"time"`与`"frequency"`至少需要配置一个，否则无法发送数据

#### service包

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

#### pojo包

存放数据库数据对象：Schema、Filed、Payload、JsonDemo、LogMessage、SendRequest

##### Schema，Filed，Payload，JsonDemo

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

##### SendRequest

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

##### LogMessage

日志对象，每一次请求结束后生成日志对象，并输出日志到控制台

![image-20220829120447970](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829120447970.png)

#### util包

存放获得数据库对象、生成随机数据并赋值、随机生成topic等方法的工具类

![image-20220829120603049](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829120603049.png)

![image-20220829120614402](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829120614402.png)

![image-20220829120637830](https://raw.githubusercontent.com/a525076133/kafka-images/main/image-20220829120637830.png)

### 启动java web应用（Windows下）

以下操作均在win11 21H2版本下完成，web应用默认监听8080端口

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



## 案例

### 案例1：Sink Connector的简单使用

对于一个Sink Connector，至少应该包含以下参数。

```json
"connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
"connection.url":"jdbc:opengauss://localhost:15432/OpengaussDB",
"connection.user":"user",
"connection.password":"password",
"topics":"test",
"table.name.format":"tableName"
```

由于没有设置`auto.create`，需要手动建表。

假设与source端协商如下表结构，并有如下配置。

```postgresql
CREATE TABLE "test" (
	"id" INT NOT NULL,
	"longitude" REAL NULL,
	"latitude" REAL NULL,
	"temperature" REAL NULL,
	"humidity" REAL NULL,
	"time" TIME NULL,
	"string_time" TEXT NULL,
	"randomString" TEXT NULL
);
```

```json
{
    "name":"connector-name",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:opengauss://localhost:15432/sink",
        "connection.user":"user1",
        "connection.password":"password",
        "topics":"topic",
        "table.name.format":"test"
    }
}
```

source端发送数据，启动Sink Connector。

` 终端/cmd`出现类似如下信息。

![demo1 terminal](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo1_terminal.jpg)

在数据库中查询，可以发现数据已保存到表中。

![demo1 result](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo1_result.jpg)

### 案例2：自动建表

该案例展示如何自动建表，无需提前创建。

添加两个参数，

`"auto.create":true`

` "dialect.name": "PostgreSqlDatabaseDialect"`

```json
{
    "name":"connector-name",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:opengauss://localhost:15432/sink",
        "connection.user":"user1",
        "connection.password":"password",
        "topics":"topic",
        "table.name.format":"test",
      
        "auto.create":true,
        "dialect.name": "PostgreSqlDatabaseDialect"
    }
}
```

启动Sink Connector。

在` 终端/cmd`可以看到，表被自动创建。

![demo2 terminal](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo2_terminal.jpg)

![demo2 result](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo2%20result.jpg)

在数据库中查询，可以发现数据已保存到表中。

### 案例3：添加新的列

该案例展示如何使用transform在原有数据基础上，添加新的列。

现介绍几个新的参数。

```json
"transforms": "InsertField"
```

transform的名字，可以使用多个transform，用逗号分隔。

```json
"transforms.InsertField.static.field": "new_filed"
```

静态数据字段的字段名称。

除了static.field，还可插入4种类型的字段。

>offset.field：Kafka offset字段名。
>
>partition.field：Kafka partition字段名。
>
>timestamp.field：record时间戳字段名。
>
>topic.field：Kafka topic字段名。

以“!”为后缀可使该字段为必填字段，或者以“?”为后缀使其为可选字段（默认）。

```json
"transforms.InsertField.static.value": "static_value"
```

默认静态字段值。

该案例展示，在之前数据基础上，新增一列名为"new_filed"的静态字段，默认值为"default_value"，一列"insert_time"的timestamp字段。

***注意，使用该功能时必须配置"auto.evolve":true，否则将不能正确执行***

```json
{
    "name":"connector-name",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:opengauss://localhost:15432/sink",
        "connection.user":"user1",
        "connection.password":"password",
        "topics":"topic",
        "table.name.format":"test",
      
        "auto.evolve":true,
        "transforms": "insertTS,InsertField",
        "transforms.insertTS.type": "org.apache.kafka.connect.transforms.InsertField$Value",
        "transforms.InsertField.type": "org.apache.kafka.connect.transforms.InsertField$Value",
        "transforms.insertTS.timestamp.field":"insert_time",
        "transforms.InsertField.static.field": "new_filed",
        "transforms.InsertField.static.value": "default_value"
    }
}
```

![demo3 result](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo3_result.jpg)

查询数据库，可以看到数据已经导入成功，并且新增了上述两列。

### 案例4：正则匹配topics

**4.1**

该案例展示如何使用正则表达式匹配topics。

假设有如下配置。

```json
{
    "name":"connector-name",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:opengauss://localhost:15432/sink",
        "connection.user":"user1",
        "connection.password":"password",
        "topics.regex":"topic-.*-aaaa",
        "table.name.format":"${topic}",
        "auto.create":true,
        "dialect.name": "PostgreSqlDatabaseDialect",
    }
}
```

`"topics.regex":"topic-.*-aaaa"`可以匹配所有以`topic-`为前缀`-aaaa`为后缀的topics。

使用`"table.name.format":"${topic}"`匹配table名为topic名的table。

source端向`topic-test1-aaaa`和`topic-test2-aaaa`注入数据。

启动Sink Connector。

查询数据库，可以看到自动创建了两个表，并且属于已经写入表中。

![demo4-tables](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo4-tables.png)

![demo4-result](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo4-result.png)

**4.2**

对于一些有固定前后缀的topics，在创建表时不需要这些前后缀，可以使用transform来替换掉。

`RegexRouter`使用 Re2j 正则表达式来替换字符串以更新topics名。

以下配置可以去除前缀`topic-`和`-aaaa`后缀，即只留下中间部分。

```json
"transforms":"renameTopic",
"transforms.renameTopic.type":"org.apache.kafka.connect.transforms.RegexRouter",
"transforms.renameTopic.regex":"topic-(.*)-aaaa",
"transforms.renameTopic.replacement":"$1"
```

假设有两个topics：`topic-test1-aaaa`和`topic-test2-aaaa`。

使用以下配置可以自动创建test1表和test2表。

```json
{
    "name":"connector-name",
    "config":{
        "connector.class":"io.confluent.connect.jdbc.JdbcSinkConnector",
        "connection.url":"jdbc:opengauss://localhost:15432/sink",
        "connection.user":"user1",
        "connection.password":"Enmo@123",
        "topics.regex":"topic-.*-aaaa",
        "table.name.format":"${topic}",
        "auto.create":true,
        "dialect.name": "PostgreSqlDatabaseDialect",

        "transforms":"renameTopic",
        "transforms.renameTopic.type":"org.apache.kafka.connect.transforms.RegexRouter",
        "transforms.renameTopic.regex":"topic-(.*)-aaaa",
        "transforms.renameTopic.replacement":"$1"
    }
}
```

查看表和表中数据。

![demo4.2-table](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo4.2-table.jpg)

![demo4.2-result](https://raw.githubusercontent.com/a525076133/kafka-images/main/demo4.2-result.png)

***注意：使用RegexRouter并不会在kafka中新建topic，只是将原topic名映射成其他名字，kafka中的topics仍是原topics。***

![topics-router](https://raw.githubusercontent.com/a525076133/kafka-images/main/topics-router.jpg)

查询topics列表，可以发现并没有新的topics。

有关RegexRouter的更多用法请参考：

`https://docs.confluent.io/platform/current/connect/transforms/regexrouter.html`

Transform的更多功能，例如：修改某字段类型等和参数详情请参考：

`https://docs.confluent.io/platform/current/connect/transforms/overview.html`

## 常用指令

**获取当前所有connector名称**

> `curl -X GET http://localhost:8083/connectors/`

**删除connector**

> 删除名为connector-name的connector。

> `curl -X DELETE http://localhost:8083/connectors/connector-name`

**查看topics列表**

> 首先cd到kafka目录下。

> `sh bin/kafka-topics.sh --list --bootstrap-server localhost:9092`

**删除topic**

> 删除名为test的topic。

> `sh bin/kafka-topics.sh --bootstrap-server localhost:9092 --delete --topic test`

