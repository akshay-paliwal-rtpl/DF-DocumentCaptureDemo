# Document Scanner SDK

[![N|Solid](https://cldup.com/dTxpPi9lDf.thumb.png)](https://nodesource.com/products/nsolid)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

## This SDK based on OpenCV which is been created to scan, capture and crop the document.

- Capture Document from Front and Back
- Crop with Auto-Edge-Detection

Sole purpose of this SDK is to capture the snapshot of document via scan mode, even you get the functionality of auto-edge-detection, so that you can crop the images more accurately.

## You can achieve the functionality with easy integration of following steps:
- In your app level build.gradle add this dependency:
```sh
implementation 'documentscanner:documentscanner:1.0.0'
```
- In your project level build.gradle add this dependency:
```sh 
allprojects {
   repositories {
      jcenter()
       maven {
            url "https://dl.bintray.com/vikashkumar/documentscanner" 
       }
    }
 } 
```
- Now in your project activity create DocumentCaptureInstance object: 
```sh
private lateinit var documentCaptureInstance: DocumentCaptureInstance
```
- Initialize this object with your sdkToken 
```sh
documentCaptureInstance = DocumentCaptureInstance(this, "ranosys:po5KAZjv7zFIU8NFHn9LRTIvEn0ciSq6")
```

### You can customize the color and theme of the SDK's view according to your projects user-experience.
### Just access the properties mentioned in the SDK. Have a look at the sample below:

```sh
val config = DocumentCaptureConfig()
config.isDocumentBackCaptureRequired = true
config.cameraScreenBackgroundColor =
       ContextCompat.getColor(this, R.color.black)
config.tutorialScreenBackgroundColor =
       ContextCompat.getColor(this, R.color.colorPrimaryDark)
config.cropScreenBackgroundColor =
       ContextCompat.getColor(this, R.color.black)
```
- Call captureDocument function from documentCaptureInstance
```sh
documentCaptureInstance.captureDocument(
       config,
       object : DocumentCaptureInstance.DocumentCaptureListener {
           override fun onDocumentCaptureSuccess(path: List<String>) {
               if (path.isNotEmpty() && path.size == 1) {
                   image_front.setImageBitmap(BitmapFactory.decodeFile(path[0]))
               } else if (path.isNotEmpty() && path.size == 2) {
                   image_front.setImageBitmap(BitmapFactory.decodeFile(path[0]))
                   image_back.setImageBitmap(BitmapFactory.decodeFile(path[1]))
               }
           }
           override fun onDocumentCaptureFailure(error: DocumentCaptureInstance.DocumentCaptureError) {
               Toast.makeText(this@Demo, error.message, Toast.LENGTH_SHORT).show()
           }
       })
```
- Override onActivityResult:
```sh
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
   super.onActivityResult(requestCode, resultCode, data)
   if (resultCode == Activity.RESULT_OK && requestCode == DfDCConstants.REQUEST_CODE_DOCUMENT_CAPTURE_INSTANCE) {
       if (::documentCaptureInstance.isInitialized) {
           documentCaptureInstance.onDocumentCaptureResult(requestCode, resultCode, data)
       }
   }
}
```
