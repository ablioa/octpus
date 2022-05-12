# 动态业务模型转换
## 1. 目标
- 解决在业务流程冲从**固定的业务模型对象**转换为**目标系统业务对象**的问题。
- 以配置方式进行,实现动态模型转换。
- 提供到json报文，支持普通http接口对接请求。
- 提供到DTO或其他接口目标对象(如wsl参数)转换，通过POJO兼容目标接口。

## 2. 配置结构定义
为实现转换的自动化和动态特性，通过配置文件模板定义模型的转换的方式，由转换器执行转换规则实现从源模型到目标模型的转换。
### 1. 概念
- **转换规则** 指定源业务对象到目标业务对象的映射关系的配置文件，由开发人员手工配置或由相关代码自动生成。
- **转换器** 用于执行转换规则，将指定的源业务对象转换为目标业务对象。
- **映射规则** 转换器在执行转换规则的过程中对模型字段值的转换进行定制化处理。

### 2.1 转换规则
源模型定义，指定来源对象中需要进行转换的的字段和取值路径，字段的类型通过是有转换规则自动提取。
```
// 源模型
subjects:{
    "domainType":"org.fnd.modulator.PolicyDTO",
    "items":{
        {
            "mid":"xx",
            "path":"policy.insurance.address.zipcode"
        },
        ...
    }
}

// 目标模型
objects:{
    "domainType":"org.fnd.modulator.PolicyDTO",
    "items":{
        {
            "mid":"xx",
            "path":"policy.insurance.address.zipcode"
        },
        ...
    }
}
```
### 2.1 映射关系定义
源模型和目标模型之间通过映射mid标识对应关系，确定从数据流向。
### 2.2 字段
通过映射关系可以对映射字段名称、类型和具体数值的转换。

# 类型转换
模型字段本身在模型转换过程中也需要转换时，可以指定
1. 编码值映射
```
   "converter": {
     "method": "C0001",
     "table": {
       "table": {
         "A001": "0001",
         "A002": "0002",
         "A003": "0003"
       },
       "defaultValue": "0001"
     }
```

2. 脚本映射
目标字段通过执行规则脚本实现，转换逻辑在外部groovy脚本中实现。
```
  "converter": {
    "method": "C0002",
    "groovy": "return 'CD'+input;"
  }
```
3. Bean值映射（待扩展使用，暂不支持。）

## 开发接口
 Json报文映射，不需要为输出接口提供特别的模型定义。
```java
    @DisplayName("Bean对象映射测试")
    @Test
    public void mapBeanObject(XlsTemplate domainObject) throws Exception {
        SystemMapping mapperPool = new SystemMapping();
        
        // 根据规则代码计算取得对应的转换规则
        MapModel mapper = mapperPool.getMapper("M07");
        Assert.notNull(mapper,"映射模板未定义。");

        // 构造转换上下文
        MapContext context = new MapContext(mapper);
        
        // 转换后的输出对象
        Object result = context.traverse(domainObject);
        
    }
```

Bean映射，从固定模型转换为其他领域业务模型
```java
    @DisplayName("Bean对象映射测试")
    @Test
    public void mapBeanObject(XlsTemplate domainObject) throws Exception {
        SystemMapping mapperPool = new SystemMapping();
        
        // 根据规则代码计算取得对应的转换规则
        MapModel mapper = mapperPool.getMapper("M07");
        Assert.notNull(mapper,"映射模板未定义。");

        // 构造转换上下文
        BeanMapContext context = new BeanMapContext(mapper);
        
        // 转换后的输出对象
        Object result = context.traverse(domainObject);
        
    }
```

## 问题
+ 递归型数据处理
+ 特殊字段处理
+ 异构数据处理