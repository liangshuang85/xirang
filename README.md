## 息壤

### 配置

将 [docs/application-sample.yaml](docs%2Fapplication-sample.yaml) 复制为 `config/application.yaml`，修改相应配置

### 模块

- `common`模块：常量、模型、转换器、异常等
- `core`模块：DAO层、业务层
- `rest`模块：REST接口

### 分层

- `Manager`层只做单表操作和少量的关联表操作
- `Service`层可以组合多个Manager使用，同时也可以调用`Mapper`
- `Facade`层可以组合多个Service使用，同时也可以调用`Mapper`
- `Controller`可以调用`Manager`、`Service`和`Facade`，不允许调用`Mapper`

### 关于项目版本号和数据脚本版本号

- 采用[语义化版本](https://semver.org/lang/zh-CN/)管理版本号
- 二者的版本号不完全一致，只有主版本号和次版本号相同，修订号不一定相同
