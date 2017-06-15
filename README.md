# KZRxDownload

## 效果图
![imgge](https://github.com/zongkaili/KZRxDownload_kotlin/blob/master/screen/img1.jpg)
![imgge](https://github.com/zongkaili/KZRxDownload_kotlin/blob/master/screen/img2.jpg)
![gif](https://github.com/zongkaili/KZRxDownload_kotlin/blob/master/screen/device-2017-06-15-141737.gif)

## 包含技术
kotlin
Facebook Fresco
Tencent Bugly
Vector矢量图
Eventbus
Butterknife
Kotterknife
Rxpermissions2
Rxjava2

## 使用方式

### 准备工作

1.添加Gradle依赖[ ![Download](https://api.bintray.com/packages/ssseasonnn/android/RxDownload2/images/download.svg) ](https://bintray.com/ssseasonnn/android/RxDownload2/_latestVersion)

```groovy
	dependencies{
         compile 'zlc.season:rxdownload2:2.0.2'
	}
```

2.配置权限

```xml
 	<!-- 在XML中设置权限 -->
	<uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
```

> **注意: Android 6.0 以上还必须申请运行时权限, 如果遇到不能下载, 请先检查权限**

2.butterknife在绑定view和data上的使用

在根目录的gradle配置文件中引用butterknife的gradle插件：
```groovy
    dependencies {
           classpath "com.android.tools.build:gradle:2.2.3"
           classpath 'com.jakewharton:butterknife-gradle-plugin:8.5.1'
       }
    }
```

让所有的子项目中能找到 jcenter 的中央仓库：
```groovy
    allprojects {
       repositories {
           jcenter()
       }
    }
```
在需要引用到 butterknife 的子项目中，引入 butterknife 的插件支持,比如：
```groovy
    apply plugin: 'com.android.application'
    apply plugin: 'com.jakewharton.butterknife'
```
添加库依赖：
```groovy
   compile "com.jakewharton:butterknife:8.4.0
```
在android{}中添加dataBinding的支持配置：
```groovy
    dataBinding {
        enabled = true
    }
```
Data Binding 布局中的使用:
```xml
    <layout
        xmlns:android="http://schemas.android.com/apk/res/android"
        xmlns:app="http://schemas.android.com/apk/res-auto">
    
        <data>
            <import type="com.kelly.kzrxdownload.MainActivity"/>
    
            <variable
                name="presenter"
                type="MainActivity.Presenter"/>
        </data>
        
        <!--此处为项目页面的根布局-->
    </layout>
```
*注：Presenter为MainActivity中的public内部类，比如其中可定义view操作后的点击方法*
布局中如下使用
```xml
    android:onClick="@{(v)->presenter.onClick(v)}"
```
*即：改view点击后，执行variable标签中指定的内部类Presenter中的onClick方法*

在activity中如下设置来代替setContentView(R.layout.activity_main):
```java
     ActivityMainBinding binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
     binding.contentMain.setPresenter(new Presenter());
```
