# Release notes

## 0.5.0
 - BREAKING:
    - New api methods, some fields are removed/partly moved to another place due blockchain api has been changed.
 - Fixed conversion from double to BigDecimal - now only through the string. Don't do this: `new BigDecimal(0.1d)` - it leads too much garbage
 - Added support for new types of transaction:
    - Create multisig address (transaction for creating multisignature address)
    - Multisend (single transaction for sending coins to multiple addresses)
    - Edit candidate (transaction for editing existing candidate)
 - Added network "status" endpoint to repositories


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