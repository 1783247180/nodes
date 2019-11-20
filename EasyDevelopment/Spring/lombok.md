### lombok的使用

```xml
<dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
</dependency>
```

```java
package com.yy.pojo;

import lombok.Data;
//加入注解@Data可以自动生成get set tostring方法
@Data
public class Users {
    private Short userId;
    private String userName;
    private String password;
}
```

