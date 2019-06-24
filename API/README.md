
## DataFornix-API SDK
![Download](https://api.bintray.com/packages/datafornix/dfapimodule/dfapimodule/images/download.svg)

![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)


This Data-Fornix-Mobile API SDK provides a set of methods for iOS applications to authenticate user, capture, save and retrieve data.

#### You can achieve the functionality with easy integration of following steps:
- In your project level `build.gradle` add this dependency:
```sh 
allprojects {
   repositories {
      jcenter()
       maven { url "https://dl.bintray.com/datafornix/dfapimodule" }
    }
 } 
```
- In your app level `build.gradle` add this dependency:
```sh
implementation 'dfapimodule:dfapimodule:1.0.0'
```
- Initialise `DataFornixSdk` object with your `apiToken`
```sh
DataFornixSdk.initializeSdk(YOUR_API_TOKEN)
```
- Call `all_api_calling` function from `DataFornixSdk` as example:
```sh
DataFornixSdk.sendImageForSelfieCheck(this,
    loggedInUserId, imagePath, object : ApiCallback<UploadAssetBasicResponse> {
        override fun onError(message: String) {
            showMessage(message)
        }

        override fun onException(throwable: Throwable) {
            showMessage(throwable.localizedMessage)
        }

        override fun onSuccess(result: UploadAssetBasicResponse) {
            showMessage(result.message)
        }
    })
```

#### By calling different different methods of`DataFornixSdk` class you will be calling all API's of this SDK and can get callbacks of the API response.