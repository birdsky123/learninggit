# 金融助手 - Kotlin Android应用

一个使用Kotlin和Jetpack Compose开发的现代化金融管理Android应用。

## 功能特性

- 📱 现代化UI设计，使用Material Design 3
- 💰 账户资产概览
- 📈 实时股票行情展示
- 🏦 理财产品推荐
- 🔍 搜索和通知功能
- 🌙 支持深色模式

## 技术栈

- **语言**: Kotlin
- **UI框架**: Jetpack Compose
- **架构模式**: MVVM
- **构建工具**: Gradle
- **最低SDK**: Android API 21+

## 项目结构

```
app/src/main/java/com/example/myapplication/
├── MainActivity.kt              # 主活动
├── model/                       # 数据模型
│   └── Models.kt               # 股票和理财产品模型
├── navigation/                  # 导航组件
│   └── BottomNavItem.kt        # 底部导航项
├── screens/                     # 界面屏幕
│   ├── HomeScreen.kt           # 首页
│   ├── MarketScreen.kt         # 市场页面
│   ├── TradingScreen.kt        # 交易页面
│   ├── WealthScreen.kt         # 财富页面
│   └── ProfileScreen.kt        # 个人资料页面
└── ui/theme/                    # 主题和样式
    ├── Color.kt                # 颜色定义
    ├── Theme.kt                # 主题配置
    └── Type.kt                 # 字体样式
```

## 主要界面

### 首页 (HomeScreen)
- 账户总资产概览
- 热门股票行情
- 推荐理财产品

### 市场页面 (MarketScreen)
- 股票市场数据
- 市场趋势分析

### 交易页面 (TradingScreen)
- 股票买卖功能
- 交易记录

### 财富页面 (WealthScreen)
- 投资组合管理
- 收益分析

### 个人资料页面 (ProfileScreen)
- 用户信息设置
- 偏好配置

## 开发环境要求

- Android Studio Hedgehog | 2023.1.1 或更高版本
- Kotlin 1.9.0 或更高版本
- Android SDK 34 或更高版本
- Gradle 8.0 或更高版本

## 构建和运行

1. 克隆项目到本地
```bash
git clone https://github.com/birdsky123/kotlin_learning.git
cd kotlin_learning
```

2. 在Android Studio中打开项目

3. 同步Gradle依赖

4. 连接Android设备或启动模拟器

5. 点击运行按钮或使用快捷键 `Shift + F10`

## 预览功能

项目包含多个Composable函数的预览，可以在Android Studio中直接查看UI效果：
- 首页完整预览
- 深色模式预览
- 各个组件的独立预览

## 贡献

欢迎提交Issue和Pull Request来改进这个项目！

## 许可证

本项目采用MIT许可证。

## 联系方式

- GitHub: [@birdsky123](https://github.com/birdsky123)

---

*这是一个学习项目，展示了现代Android开发的最佳实践。*
