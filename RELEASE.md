# Release notes

## 0.11.1
 - Fixed human values calculation

## 0.11.0
 - Fixes, updated core sdk
 - Added method to get unsigned transaction hash `Transaction#getUnsignedTxHash`
 - Added method to sign tx with a signature created outside `Transaction#signExternal`

## 0.10.0
 - Added check decoding from **Mc[data]**
 - More check tests
 - Fixed check signing

## 0.9.5
 - Fixed gas coin length

## 0.9.4 
 - Fixed decoding empty string to numeric

## 0.9.3
 - Updated code SDK to interpret RLP empty object[] as empty char[]

## 0.9.2
 - Throwing exception (instead of returning null) if encoded transaction has invalid data length

## 0.9.1
 - Fixed coin creation fee calculator

## 0.9.0
 - Added some getters and constructor to `Transaction.Builder` to create it from `ExternalTransaction`
 - Removed `double` setters and getters where using BigDecimals or BigIntegers, because `double` value have a poor accuracy
 - Instead of `double` values, added `String` setters to convert it to `BigDecimal`
 - Renamed `TxEditCandidateTransaction` to `TxEditCandidate`

## 0.8.0
 - Added ability to create simple transaction-based structure encoded with RLP to share transaction data
 - `TxSendCoin#getValue()` not returns `BigDecimal` instead of `double`. For double value, added method `TxSendCoin#getValueDouble()`
 - Added empty constructors for operation data objects. It quite breaks the builder logic but gives you ability to use this data for your own implementations. Also, #build() method now will throw exception if object were constructed without or with null `Transaction`
 - Migrate to androidx

## 0.7.5
 - Updated core SDK

## 0.7.4
 - Fixed Multisend address encoding

## 0.7.3
 - Now `MinterPublicKey` and `MinterAddress` encodes with fixed bytes length

## 0.7.2
 - Updated blockchain SDK, re-verified tests

## 0.7.1
 - Fixed check

## 0.7.0
 - BREAKING CHANGES:
    - Fixed RLP decoding/encoding for uint64_t-like values, `nonce` for example
    - In most places `byte[]` was replaced with `char[]`, `BytesData` to `BytesData`

## 0.6.2
 - MainNet urls, cleanup

## 0.6.1
 - Update commissions
 - Fix serialization for Validator

## 0.6.0
 - BREAKING:
    - Added "chainId" to transactions. Now all transactions requires this field to determine correct network

## 0.5.6
 - Changed `stake` to `value` in delegate/unbound transactions

## 0.5.5
 - Fixed sending transaction: now blockchain requires 0x before transaction sign

## 0.5.4
 - Removed `transactionCount` method and moved this info to balance model `Balance.txCount`

## 0.5.3
 - New balance api endpoint (from /balance/{address} to /address/?address=Mx...

## 0.5.2
 - Added `getApiService()` to modify api client

## 0.5.1
 - Added gas price methods, see BlockChainBlockRepository
 - Opened gas price field and setter for transaction, it uses in new api
 - Added ability to create non-singleton instances, for using in cases when need to connect to multiple nodes


## 0.5.0
 - BREAKING:
    - New api methods, some fields are removed/partly moved to another place due blockchain api has been changed.
 - Fixed conversion from double to BigDecimal - now only through the string. Don't do this: `new BigDecimal(0.1d)` - it leads too much garbage
 - Added support for new types of transaction:
    - Create multisig address (transaction for creating multisignature address)
    - Multisend (single transaction for sending coins to multiple addresses)
    - Edit candidate (transaction for editing existing candidate)
 - Added missing endpoints and repositories
 - More tests (still without mocks. Soon...)


## 0.4.0
 - BREAKING: 
    - Added Min/MaxValueToBuy to Sell/SellAll/Buy coin model and it's all required
    - Renamed some methods for more consistency

## 0.3.1
 - Added minter check creation support
 - Updated core library

## 0.3.0
 - Multiple signatures support
 - BREAKING CHANGES:
    - Old transactions will not works with new signing algorithm
    - Deprecated `Transaction#sign(PrivateKey pk)`.
   Use `Transaction#signSingle(PrivateKey pk)`
   or for multiple signs: `Transaction#signMulti(MinterAddress, List<PrivateKey>)`
    - removed `Class<>` argument from `Transaction#fromEncoded`.
        Old signature: `Transaction#fromEncoded(String hexString, Class<T extends Operation> cls)`,
     new: `Transaction#fromEncoded(String hexString)`
 - Reduced android dependencies, more pure java for JRE support (in future)
 - Custom logger

## 0.2.0
 - BREAKING CHANGES:
    - refactored blockchain api result codes, now they are is valid and representative
 - Removed android support dependencies, replaced with guava (proguard included)

## 0.1.4
 - https base minter node url

## 0.1.3
 - Package dependencies fix

## 0.1.2
 - Updated core sdk to creating private key from mnemonic phrase directly

## 0.1.1
 - Added signed transaction commission calculation endpoint.
 - Target api 28