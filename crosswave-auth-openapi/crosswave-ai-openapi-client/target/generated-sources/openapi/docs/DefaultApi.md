# DefaultApi

All URIs are relative to *http://localhost:8080*

| Method | HTTP request | Description |
| ------------- | ------------- | ------------- |
| [**getToken**](DefaultApi.md#getToken) | **POST** /auth/v1/get-token | Вход в систему |
| [**login**](DefaultApi.md#login) | **POST** /auth/v1/login | Чтение данных о пользователе по токену |
| [**register**](DefaultApi.md#register) | **POST** /auth/v1/register | Регистрация пользователя |


<a id="getToken"></a>
# **getToken**
> TokenResponseDto getToken(getTokenDto)

Вход в систему

Авторизация пользователя по логину и паролю.

### Example
```kotlin
// Import classes:
//import org.xxavierr404.crosswave.auth.client.infrastructure.*
//import org.xxavierr404.crosswave.auth.client.models.*

val apiInstance = DefaultApi()
val getTokenDto : GetTokenDto =  // GetTokenDto | 
try {
    val result : TokenResponseDto = apiInstance.getToken(getTokenDto)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#getToken")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#getToken")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **getTokenDto** | [**GetTokenDto**](GetTokenDto.md)|  | |

### Return type

[**TokenResponseDto**](TokenResponseDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="login"></a>
# **login**
> UserDataDto login(loginRequestDto)

Чтение данных о пользователе по токену

### Example
```kotlin
// Import classes:
//import org.xxavierr404.crosswave.auth.client.infrastructure.*
//import org.xxavierr404.crosswave.auth.client.models.*

val apiInstance = DefaultApi()
val loginRequestDto : LoginRequestDto =  // LoginRequestDto | 
try {
    val result : UserDataDto = apiInstance.login(loginRequestDto)
    println(result)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#login")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#login")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **loginRequestDto** | [**LoginRequestDto**](LoginRequestDto.md)|  | |

### Return type

[**UserDataDto**](UserDataDto.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a id="register"></a>
# **register**
> register(registerDto)

Регистрация пользователя

Создание нового пользователя.

### Example
```kotlin
// Import classes:
//import org.xxavierr404.crosswave.auth.client.infrastructure.*
//import org.xxavierr404.crosswave.auth.client.models.*

val apiInstance = DefaultApi()
val registerDto : RegisterDto =  // RegisterDto | 
try {
    apiInstance.register(registerDto)
} catch (e: ClientException) {
    println("4xx response calling DefaultApi#register")
    e.printStackTrace()
} catch (e: ServerException) {
    println("5xx response calling DefaultApi#register")
    e.printStackTrace()
}
```

### Parameters
| Name | Type | Description  | Notes |
| ------------- | ------------- | ------------- | ------------- |
| **registerDto** | [**RegisterDto**](RegisterDto.md)|  | |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: Not defined

