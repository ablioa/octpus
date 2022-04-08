# 动态业务模型转换
## 1. 目标
- 解决在业务流程冲从**固定的业务模型对象**转换为**目标系统业务对象**的问题。
- 以配置方式进行。
- 提供到json报文，支持普通http接口对接请求。
- 提供到DTO或其他接口目标对象(如wsl参数)转换，通过POJO兼容目标接口。
- 

## 2. 配置结构定义
### 2.1 模型定义
```
// 源模型
subjects:{
    "domainType":"com.octpus.PolicyDTO",
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
    "domainType":"com.octpus.PolicyDTO",
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
### 2.3 字段
通过映射关系可以对映射字段名称、类型和具体数值的转换。

# 类型转换
模型字段本身在模型转换过程中也需要转换时，可以指定
```
1. 编码值映射
2. 脚本映射
3. Bean值映射
```

## 开发接口
 Json报文映射，不需要为输出接口提供特别的模型定义。
```java

```

Bean映射，从固定模型转换为其他领域业务模型
```java

```