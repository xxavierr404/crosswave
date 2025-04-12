# DefaultApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**predictGenre**](DefaultApi.md#predictGenre) | **POST** /suggest/v1/predict-genre | Получить самый вероятный жанр трека |
| [**suggestTracks**](DefaultApi.md#suggestTracks) | **GET** /suggest/v1/tracks | Получить рекомендации треков |
| [**suggestUsers**](DefaultApi.md#suggestUsers) | **GET** /suggest/v1/friends | Получить рекомендации пользователей |


<a id="predictGenre"></a>
# **predictGenre**
> GenrePredictionDto predictGenre(file)

Получить самый вероятный жанр трека

### Example
```kotlin
// Import classes:
//import org.xxavierr404.crosswave.ai.client.infrastructure.*
//import org.xxavierr404.crosswave.ai.client.models.*

val apiInstance = DefaultApi()
val file : kotlin.ByteArray = BINARY_DATA_HERE // kotlin.ByteArray | 
try {
    val result : GenrePredictionDto = apiInstance.predictGenre(file)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#predictGenre")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#predictGenre")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **file** | **kotlin.ByteArray**|  | |

### Return type

[**GenrePredictionDto**](GenrePredictionDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: application/json

<a id="suggestTracks"></a>
# **suggestTracks**
> kotlin.collections.List&lt;java.util.UUID&gt; suggestTracks(xUserId)

Получить рекомендации треков

### Example
```kotlin
// Import classes:
//import org.xxavierr404.crosswave.ai.client.infrastructure.*
//import org.xxavierr404.crosswave.ai.client.models.*

val apiInstance = DefaultApi()
val xUserId : kotlin.String = xUserId_example // kotlin.String | 
try {
    val result : kotlin.collections.List<java.util.UUID> = apiInstance.suggestTracks(xUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#suggestTracks")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#suggestTracks")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **xUserId** | **kotlin.String**|  | |

### Return type

[**kotlin.collections.List&lt;java.util.UUID&gt;**](java.util.UUID.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

<a id="suggestUsers"></a>
# **suggestUsers**
> kotlin.collections.List&lt;java.util.UUID&gt; suggestUsers(xUserId)

Получить рекомендации пользователей

### Example
```kotlin
// Import classes:
//import org.xxavierr404.crosswave.ai.client.infrastructure.*
//import org.xxavierr404.crosswave.ai.client.models.*

val apiInstance = DefaultApi()
val xUserId : kotlin.String = xUserId_example // kotlin.String | 
try {
    val result : kotlin.collections.List<java.util.UUID> = apiInstance.suggestUsers(xUserId)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#suggestUsers")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#suggestUsers")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **xUserId** | **kotlin.String**|  | |

### Return type

[**kotlin.collections.List&lt;java.util.UUID&gt;**](java.util.UUID.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json

