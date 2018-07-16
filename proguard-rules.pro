-dontobfuscate

-keep public class network.minter.blockchainapi.** { *; }
-keep public enum network.minter.blockchainapi.** { *; }
-keepclassmembers enum * { *; }
-keep public class network.minter.blockchainapi.MinterBlockChainApi { *; }


# Parceler library
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }