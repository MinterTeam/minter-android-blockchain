-dontobfuscate

-keep public class network.minter.blockchain.** { *; }
-keep public enum network.minter.blockchain.** { *; }
-keepclassmembers enum * { *; }
-keep public class network.minter.blockchain.MinterBlockChainApi { *; }


# Parceler library
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }