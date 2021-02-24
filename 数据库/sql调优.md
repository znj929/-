#### 查看sql执行计划

explain select * from table

table | type | possible_keys | key | key_len | ref | rows | Extra
````
table：哪个表

type：这个很重要，是说类型，all（全表扫描），const（读常量，最多一条记录匹配），eq_ref（走主键，一般就最多一条记录匹配），index（扫描全部索引），range（扫描部分索引）

possible_keys：显示可能使用的索引

key：实际使用的索引

key_len：使用索引的长度

ref：联合索引的哪一列被用了

rows：一共扫描和返回了多少行

extra：using filesort（需要额外进行排序），using temporary（mysql构建了临时表，比如排序的时候），using where（就是对索引扫出来的数据再次根据where来过滤出了结果）
````
