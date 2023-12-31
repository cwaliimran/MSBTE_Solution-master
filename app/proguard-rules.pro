# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontwarn com.facebook.ads.internal.**
-keeppackagenames com.facebook.*
-keep public class com.facebook.ads.** {*;}
-keep public class com.facebook.ads.**
{ public protected *; }
# Keep filenames and line numbers for stack traces
-keepattributes SourceFile,LineNumberTable
# Keep JavascriptInterface for WebView bridge
-keepattributes JavascriptInterface
# Sometimes keepattributes is not enough to keep annotations
-keep class android.webkit.JavascriptInterface {
   *;
}
# Keep all classes in Unity Ads package
-keep class com.unity3d.ads.** {
   *;
}
# Keep all classes in Unity Services package
-keep class com.unity3d.services.** {
   *;
}
-dontwarn com.google.ar.core.**
-dontwarn com.unity3d.services.**
-dontwarn com.ironsource.adapters.unityads.**
-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** { *;
}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-dontwarn com.facebook.ads.internal.**
-keeppackagenames com.facebook.*
-keep public class com.facebook.ads.** {*;}
-keep public class com.facebook.ads.**
{ public protected *; }


     # Google
        -dontwarn com.google.android.gms.common.GoogleApiAvailabilityLight
        -dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient
        -dontwarn com.google.android.gms.ads.identifier.AdvertisingIdClient$Info
         -dontwarn com.google.ar.core.**
        -keep class com.google.firebase.example.fireeats.java.model.** { *; }


-keep class com.shockwave.**

        -keep class myadapter.*
        -keep class com.firebase.*
        -keep class model.*
        -keep public class com.msbte.modelanswerpaper.** {
          public protected *;
          public class myadapter.*;
          public class model.*;
        }

        -keepclassmembers class * {
            @android.webkit.JavascriptInterface <methods>;
        }

        -keepattributes JavascriptInterface
        -keepattributes *Annotation*

        -dontwarn com.razorpay.**
        -keep class com.razorpay.** {*;}

        -optimizations !method/inlining/*

        -keepclasseswithmembers class * {
          public void onPayment*(...);
        }

        # Razorpay Payment Gateway
        -keep class com.razorpay.** { *; }

        # Firebase Features
        -keepattributes Signature
        -keepattributes *Annotation*
        -keep class com.google.firebase.** { *; }
        -keep class io.grpc.** { *; }
        -keep class com.google.android.gms.** { *; }
        -dontwarn com.google.firebase.**
        -dontwarn io.grpc.**
        -dontwarn com.google.android.gms.**
