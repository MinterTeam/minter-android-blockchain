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
    minterBlockchainSDK = "0.11.1"
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
MinterBlockChainApi.initialize();
```

Or it's HIGHLY RECOMMENDED to use you own node instead of Minter's.
```java
MinterBlockChainApi.initialize("https://your-node.local");
```

### 2. Creating and signing transactions

Transactions API uses **Builder** pattern, so it so easy to handle it.

All transactions requires a valid **nonce** value. Nonce - is a number of transaction. To get valid transaction number, you should get current number via `BlockChainAccountRepository#getTransactionCount` and increment it:

```java
// init object with your Minter address
MinterAddress myAddress = new MinterAddress("Mxccc3fc91a3d47dc1ee26d62611a09831f0214d62");

// get account repository from SDK singleton object
BlockChainAccountRepository repo = MinterBlockChainApi.getInstance().account();

// send request
repo.getTransactionCount(myAddress).enqueue(new Callback<BCResult<CountableData>>() {
    @Override
    public void onResponse(Call<BCResult<CountableData>> call, Response<BCResult<CountableData>> response) {
        BigInteger txCount = response.body().result.count;

        // use this incremented value as nonce to your transaction
        BigInteger nonce = txCount.add(new BigInteger("1"));
    }

    @Override
    public void onFailure(Call<BCResult<CountableData>> call, Throwable t) {
    }
})
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
Transaction tx = new Transaction.Builder(new BigInteger("1"))
    // optional: available for all transactions, but not useful for some transactions
    .setGasCoin("MNT")
    // here you should select what transaction you are trying to create, builder will select exact type
    .sendCoin()
    // required: coin to send
    .setCoin(coin)
    // required: value to send
    .setValue(10D)
    // required: recipient address
    .setTo(toAddress)
    // finally, build object
    .build();
```

Sign transaction using your private key
```java
TransactionSign sign = tx.sign(privateKey);

// get transaction hash - this hash you'll send to blockchain
String signedTransaction = sign.getTxSign();
```


So, it's easy, isn't? :)

For more transaction types see `OperationType` and class `Transaction.Builder`

Now we'll send transaction to blockchain.

#### 2.2 Send "send" transaction to the Minter blockchain

To send transaction to blockchain, we need to get `BlockChainAccountRepository` from `MinterBlockChainApi`

```java
BlockChainAccountRepository accountRepo = MinterBlockChainApi.getInstance().account();
```

To send transaction, you just need to call http request
```java
TransactionSign sign = ...
accountRepo.sendTransaction(sign).enqueue(new Callback<BCResult<TransactionSendResult>>() {
    @Override
    public void onResponse(Call<BCResult<TransactionSendResult>> call, Response<BCResult<TransactionSendResult>> response) {
        // handle send result
    }

    @Override
    public void onFailure(Call<BCResult<TransactionSendResult>> call, Throwable t) {
       // handle send error
    }
})
```

That's all!


## Documentation

Javadoc available in code and in *.jar file at the bintray

## Build
TODO

## Tests
TODO

## Changelog

See [Release notes](RELEASE.md)


## License

This software is released under the [MIT](LICENSE.txt) License.

© 2018 MinterTeam <edward.vstock@gmail.com>, All rights reserved.