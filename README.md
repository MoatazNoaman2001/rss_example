# RSS Feed Reader - NBC News World

A modern Android RSS feed reader application built with **Clean Architecture**, **Jetpack Compose**, and **Dependency Injection** using **Hilt**.

## 🏗️ Architecture

This project follows **Clean Architecture** principles with three distinct layers:

### **1. Data Layer** (`data/`)
- **Models**: XML parsing models using TikXML (`RssFeed`, `RssChannel`, `RssItem`)
- **API Service**: Retrofit interface for fetching RSS feeds
- **Repository Implementation**: Converts data models to domain models

### **2. Domain Layer** (`domain/`)
- **Models**: Business logic models (`NewsArticle`)
- **Repository Interface**: Defines contract for data operations
- **Use Cases**: Business logic encapsulation (`GetRssFeedUseCase`)
- **Result Wrapper**: Type-safe result handling

### **3. Presentation Layer** (`presentation/`)
- **ViewModel**: State management with `StateFlow`
- **UI State**: Sealed classes for different UI states
- **Compose UI**: Modern declarative UI with Material 3

## 🛠️ Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Networking**: Retrofit + OkHttp
- **XML Parsing**: TikXML
- **Image Loading**: Coil
- **Async**: Kotlin Coroutines & Flow
- **Build**: Gradle with Kotlin DSL

## 📦 Dependencies

```kotlin
// Compose - Modern UI toolkit
implementation("androidx.compose:compose-bom:2024.02.00")
implementation("androidx.compose.material3:material3")
implementation("androidx.activity:activity-compose:1.8.2")

// Hilt - Dependency Injection
implementation("com.google.dagger:hilt-android:2.50")
kapt("com.google.dagger:hilt-compiler:2.50")

// Retrofit - Network calls
implementation("com.squareup.retrofit2:retrofit:2.9.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")

// TikXML - XML parsing
implementation("com.tickaroo.tikxml:retrofit-converter:0.8.13")

// Coil - Image loading
implementation("io.coil-kt:coil-compose:2.5.0")

// Coroutines - Async operations
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
```

## 🏛️ Project Structure

```
com.example.rssexample/
├── data/
│   ├── model/
│   │   └── RssChannel.kt          # XML data models
│   ├── remote/
│   │   └── RssApiService.kt       # Retrofit API interface
│   └── repository/
│       └── RssRepositoryImpl.kt   # Repository implementation
│
├── domain/
│   ├── model/
│   │   └── NewsArticle.kt         # Domain model
│   ├── repository/
│   │   └── RssRepository.kt       # Repository interface
│   ├── usecase/
│   │   └── GetRssFeedUseCase.kt   # Business logic
│   └── util/
│       └── Result.kt              # Result wrapper
│
├── presentation/
│   └── feed/
│       ├── FeedUiState.kt         # UI state sealed class
│       ├── RssFeedViewModel.kt    # ViewModel with StateFlow
│       ├── RssFeedScreen.kt       # Main screen scaffold
│       ├── RssFeedComponents.kt   # Common UI components
│       └── NewsArticleCard.kt     # Article card components
│
├── di/
│   ├── NetworkModule.kt           # Network dependencies
│   └── RepositoryModule.kt        # Repository bindings
│
├── ui/theme/                      # Compose theme
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
│
├── MainActivity.kt                # Entry point
└── RssApplication.kt              # Application class with Hilt
```

## ✨ Features

- ✅ **Clean Architecture** - Separation of concerns with clear layer boundaries
- ✅ **Dependency Injection** - Hilt for providing dependencies
- ✅ **Modern UI** - Jetpack Compose with Material 3 design
- ✅ **Reactive UI** - StateFlow for state management
- ✅ **Error Handling** - Proper error states and retry functionality
- ✅ **Loading States** - Visual feedback during data fetching
- ✅ **Image Loading** - Async image loading with Coil
- ✅ **Deep Links** - Open articles in browser
- ✅ **Type Safety** - Result wrapper for safe error handling
- ✅ **XML Parsing** - Robust RSS feed parsing with TikXML

## 🚀 How It Works

### Data Flow:

1. **UI Layer** → ViewModel initializes and calls `loadFeed()`
2. **ViewModel** → Invokes `GetRssFeedUseCase`
3. **Use Case** → Calls repository interface
4. **Repository** → Fetches from API, transforms data
5. **API Service** → Makes HTTP request via Retrofit
6. **XML Parser** → TikXML converts XML to Kotlin objects
7. **Data Transformation** → Maps data models to domain models
8. **Result Emission** → StateFlow emits UI state
9. **UI Rendering** → Compose recomposes with new state

### State Management:

```kotlin
sealed class FeedUiState {
    object Initial : FeedUiState()
    object Loading : FeedUiState()
    data class Success(val articles: List<NewsArticle>) : FeedUiState()
    data class Error(val message: String) : FeedUiState()
}
```

## 🎨 UI Components

- **TopAppBar**: Material 3 app bar with title
- **LazyColumn**: Efficient scrollable list
- **NewsArticleCard**: Card with image, title, description, and date
- **LoadingView**: Circular progress with message
- **ErrorView**: Error message with retry button
- **AsyncImage**: Coil image loading with placeholder

## 🔧 Build Instructions

1. **Clone the repository**
2. **Open in Android Studio**
3. **Sync Gradle** - Let Gradle download dependencies
4. **Run the app** - Click the Run button or use `Shift + F10`

## 📱 RSS Feed Source

- **URL**: `https://feeds.nbcnews.com/nbcnews/public/world`
- **Source**: NBC News - World News
- **Format**: RSS 2.0 XML

## 🔐 Permissions

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

## 🧪 Testing

The architecture supports easy testing:

- **Data Layer**: Mock API responses
- **Domain Layer**: Test use cases with fake repositories
- **Presentation Layer**: Test ViewModels with test dispatchers

## 🎯 Best Practices Implemented

1. **Single Responsibility** - Each class has one clear purpose
2. **Dependency Inversion** - Depend on abstractions, not concrete implementations
3. **Separation of Concerns** - Clear layer boundaries
4. **Immutability** - Data classes with `val` properties
5. **Null Safety** - Proper handling of nullable types
6. **Coroutine Scoping** - ViewModelScope for proper lifecycle management
7. **Resource Management** - Proper cleanup with coroutines
8. **Modern Android** - Latest Jetpack libraries and best practices

## 📈 Potential Enhancements

- Add Room database for offline caching
- Implement pull-to-refresh
- Add article search functionality
- Support multiple RSS feeds
- Add article bookmarking
- Implement dark/light theme toggle
- Add share functionality
- Improve date formatting
- Add unit and UI tests

## 📄 License

This project is for educational purposes.

---

**Built with ❤️ using Clean Architecture principles**

# rss_example
