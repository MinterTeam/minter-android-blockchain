Minter Android Blockchain API SDK
=================================
[![Download](https://api.bintray.com/packages/minterteam/android/minter-android-blockchain-testnet/images/download.svg) ](https://bintray.com/minterteam/android/minter-android-blockchain-testnet/_latestVersion)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE.txt)


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
    minterBlockchainSDK = "1.0.1"
}

dependencies {
    // for testnet use suffix "-testnet"
    implementation "network.minter.android:minter-android-blockchain-testnet:${minterBlockchainSDK}"

    // for main net
    implementation "network.minter.android:minter-android-blockchain:${minterBlockchainSDK}"
}
```

## Usage / Examples
### 1. Initialize SDK

Use our nodes
```java
MinterBlockChainSDK.initialize();
```

Or it's HIGHLY RECOMMENDED to use you own node instead of Minter's.
```java
MinterBlockChainSDK.initialize("https://your-node.local");
```

### 2. Creating and signing transactions

Transactions API uses **Builder** pattern, so it so easy to handle it.

```java
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import network.minter.blockchain.MinterBlockChainSDK;
import network.minter.blockchain.repo.NodeTransactionRepository;
class MyClass {
    
    void myMethod() {
        Transaction tx = new Transaction.Builder(new BigInteger("1"))
                .setBlockchainId(BlockchainID.MainNet)
                .setGasCoinId(DEFAULT_COIN_ID)
                .sendCoin()
                .setCoinId(DEFAULT_COIN_ID)
                .setValue("0.012345")
                .setTo(toAddress)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
       
        NodeTransactionRepository txRepo = MinterBlockChainSDK.getInstance().transactions();
        txRepo.sendTransaction(sign)
            .observeOn(Schedulers.io())
            .subscribeOn(Scheduler.io())
            .subscribe(sendResult -> {
               System.out.println(sendResult.txHash.toString());
            }, throwable -> {
               // handle error
            });
    }
}
```

#### 2.1 Create "Send" transaction
Calculate nonce (see above)
```java
// Calculated nonce
BigInteger nonce = ...
```

Create your private key to sign tx with it:
```java
PrivateKey privateKey = new PrivateKey("4c9a495b52aeaa839e53c3eb2f2d6650d892277bde58a24bb6a396f2bb31aa37");
```

or if you know only mnemonic phrase, you can do this:
```java
final PrivateKey privateKey = PrivateKey.fromMnemonic("your phrase must contains twenty words et cetera ...");
```

Create transaction builder and build transaction:
```java
Transaction tx = new Transaction.Builder(nonce)
    // by default it depends on what sdk build type you used: with or without suffix "-testnet"
    .setBlockchainId(BlockchainID.MainNet)
    // optional: available for all transactions, but not useful for some transactions
    .setGasCoinId(DEFAULT_COIN_ID)
    // here you should select what transaction you are trying to create, builder will select exact type
    .sendCoin()
    // required: coin to send represented by it's ID
    .setCoinId(DEFAULT_COIN_ID)
    // value to send
    .setValue("0.012345")
    // required: recipient address
    .setTo(toAddress)
    // finally, build object
    .build();
```

Sign transaction using your private key
```java
TransactionSign sign = tx.sign(privateKey);
```

For more transaction types see `OperationType` and class `Transaction.Builder`

Now we'll send transaction to blockchain.

#### 2.2 Send "send" transaction to the Minter blockchain

To send transaction to blockchain, we need to get `NodeTransactionRepository` from `MinterBlockChainSDK`

```java
NodeTransactionRepository accountRepo = MinterBlockChainSDK.getInstance().transactions();
```

To send transaction, you just need to call http request
```java
TransactionSign sign = ...

NodeTransactionRepository txRepo = MinterBlockChainSDK.getInstance().transactions();
txRepo.sendTransaction(sign)
    .observeOn(Schedulers.io())
    .subscribeOn(Scheduler.io())
    .subscribe(sendResult -> {
        System.out.println(sendResult.txHash.toString());
    }, throwable -> {
        // handle error
    });
```

That's all!


## Documentation

Javadoc available in code and in *.jar file at the bintray

## Build
To create local artifacts that you can find in your home `~/.m2` directory, just run:
```bash
bash project_root/publish_local.sh
```

## Tests

To run unit tests, you must build bip39 and secp256k1 with host target
See: [bip39](https://github.com/edwardstock/bip3x) and [secp256k1-java](https://github.com/edwardstock/native-secp256k1-java)

All these test can be runned only with testnet configuration, don't use `gradlew test` directly
```bash
cd project_root
./gradlew testNetTestDebugUnitTest -PnativeLibPath=/path/to/native/libs
```

## Changelog

See [Release notes](RELEASE.md)


## License

This software is released under the [MIT](LICENSE.txt) License.

© 2018 MinterTeam <edward.vstock@gmail.com>, All rights reserved.
