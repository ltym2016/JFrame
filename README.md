# JFrame
## 使用使用
### 第一步
```
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}

dependencies {
    implementation 'com.github.ltym2016:JFrame:1.0.0'
}
```

### 第二步
在项目的build.gradle里添加如下代码
```
apply plugin: 'kotlin-kapt'
kapt {
    generateStubs = true
}

android{
...
    dataBinding {
        enabled = true
    }

    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }
}
```

### 第三步
在Application里面初始化工具类
```
class MyApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // 初始化工具类
        Utils.init(this)
        // 初始化log日记
        LogUtils.newBuilder()
            .debug(Utils.isDebug())
            .tag("FUXI_LOG")
            .build()
        // 初始化请求Host地址
        HttpUtils.init(Utils.getStringFromConfig(R.string.host))
            .isDebug(Utils.isDebug())
    }
}
```
