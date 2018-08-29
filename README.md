Minter Android Blockchain API SDK
=================================
[![Download](https://api.bintray.com/packages/minterteam/android/minter-android-blockchain-testnet/images/download.svg?version=0.1.1) ](https://bintray.com/minterteam/android/minter-android-blockchain-testnet/0.1.1/link)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)


Minter blockchain sdk library
-----------------------------------------------------------------

## Setup

Gradle
root build.gradle
```groovy
allprojects {
    repositories {
       // ... some repos
        maven { url "https://dl.bintray.com/minterteam/android" }
    }
}
```

project build.gradle
```groovy

ext {
    minterSdkVersion = "0.1.1"
}

dependencies {
    // for testnet use suffix "-testnet"
    implementation "network.minter.android:minter-android-blockchain-testnet:${minterSdkVersion}"

    // for main net
    implementation "network.minter.android:minter-android-blockchain:${minterSdkVersion}"
}
```

## Basic Usage
### Initialize it
```java

MinterBlockChainApi.initialize();
```

### Usage
Look at [tests](src/tests/java/network/minter/blockchain) to see how to create and sign transactions

## Docs
TODO (tests and javadocs available for now)

# Build
TODO