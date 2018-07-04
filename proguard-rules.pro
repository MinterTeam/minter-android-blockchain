-dontobfuscate

-keep public class network.minter.blockchainapi.** { *; }
-keep public class network.minter.blockchainapi.MinterBlockChainApi { *; }


# Parceler library
-keep interface org.parceler.Parcel
-keep @org.parceler.Parcel class * { *; }
-keep class **$$Parcelable { *; }