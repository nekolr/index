## 动态数据源
- `AbstractRoutingDataSource` 有两个属性 `targetDataSources` 和 `defaultTargetDataSource`，在 Spring 容器初始化过程中根据配置文件信息手动创建 `DataSource`，分别给这两个属性赋值，这样在 Spring 初始化完毕后容器中就加载了所有的 `DataSource`。
- 实现 `AbstractRoutingDataSource` 的 `determineCurrentLookupKey` 方法，该方法提供一个数据源的名称，这里实现为获取当前线程使用的数据源名称。
- 创建多数据源注解，在 DAO 层使用该注解，注解提供一个属性值表示使用的数据源名称。
- 创建多数据源切面，提供 `@Before` 和 `@After` 方法，切点设置为多数据源注解，`@Before` 方法实现设置当前线程使用的数据源名称，`@After` 方法实现清除设置的当前线程使用的数据源名称。

  通过上述过程，就可以在 DAO 层调用每个方法之前，先根据注解提供的值来设置当前线程使用的数据源名称，`AbstractRoutingDataSource` 根据这个名称找到该数据源返回。在使用完数据源之后，清空设置的值，这样下次使用时，找不到设置的值就会返回默认的数据源。  
  
```java
/**
* 根据 determineCurrentLookupKey 返回的值来查找数据源，如果没找到，使用默认的数据源
*/
protected DataSource determineTargetDataSource() {
  Assert.notNull(this.resolvedDataSources, "DataSource router not initialized");
  Object lookupKey = determineCurrentLookupKey();
  DataSource dataSource = this.resolvedDataSources.get(lookupKey);
  if (dataSource == null && (this.lenientFallback || lookupKey == null)) {
    dataSource = this.resolvedDefaultDataSource;
  }
  if (dataSource == null) {
    throw new IllegalStateException("Cannot determine target DataSource for lookup key [" + lookupKey + "]");
  }
  return dataSource;
}
```
## Selenium
将人的操作编写为程序代码，然后 Selenium 就通过驱动向浏览器发送命令，从而模拟人的行为。
