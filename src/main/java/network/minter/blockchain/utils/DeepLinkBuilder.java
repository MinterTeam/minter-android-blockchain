/*
 * Copyright (C) by MinterTeam. 2020
 * @link <a href="https://github.com/MinterTeam">Org Github</a>
 * @link <a href="https://github.com/edwardstock">Maintainer Github</a>
 *
 * The MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package network.minter.blockchain.utils;

import java.math.BigInteger;

import network.minter.blockchain.models.operational.CheckTransaction;
import network.minter.blockchain.models.operational.ExternalTransaction;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TxRedeemCheck;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.PrivateKey;

public class DeepLinkBuilder {
    public static final String BIP_WALLET_MAINNET = "https://bip.to";
    public static final String BIP_WALLET_TESTNET = "https://testnet.bip.to";
    public static String BIP_WALLET_URL = BIP_WALLET_MAINNET;
    private ExternalTransaction mExternalTransaction;
    private String mBaseUrl = BIP_WALLET_URL;
    private String mCheckPass = null;

    public DeepLinkBuilder(Transaction transaction) {
        this(new ExternalTransaction(transaction));
    }

    public DeepLinkBuilder(CheckTransaction checkTransaction, PrivateKey pk, String passphrase) {
        TxRedeemCheck redeemCheck = new TxRedeemCheck();
        redeemCheck.setRawCheck(checkTransaction.sign(pk).getTxSign());
        redeemCheck.setProof(CheckTransaction.makeProof(pk.getPublicKey().toMinter(), passphrase).toHexString());
        mExternalTransaction = new ExternalTransaction.Builder()
                .setData(redeemCheck)
                .setGasCoin(checkTransaction.getGasCoin())
                .setGasPrice(BigInteger.ONE)
                .setNonce(BigInteger.ZERO)
                .build();
    }

    public DeepLinkBuilder(CheckTransaction checkTransaction, PrivateKey pk) {
        TxRedeemCheck redeemCheck = new TxRedeemCheck();
        redeemCheck.setRawCheck(checkTransaction.sign(pk).getTxSign());
        mExternalTransaction = new ExternalTransaction.Builder()
                .setData(redeemCheck)
                .setGasCoin(checkTransaction.getGasCoin())
                .setGasPrice(BigInteger.ONE)
                .setNonce(BigInteger.ZERO)
                .build();
    }

    public DeepLinkBuilder(ExternalTransaction.Builder externalTransactionBuilder) {
        mExternalTransaction = externalTransactionBuilder.build();
    }

    public DeepLinkBuilder(ExternalTransaction externalTransaction) {
        mExternalTransaction = externalTransaction;
    }

    public DeepLinkBuilder(String baseUrl, Transaction transaction) {
        this(baseUrl, new ExternalTransaction(transaction));
    }

    public DeepLinkBuilder(String baseUrl, ExternalTransaction.Builder externalTransactionBuilder) {
        this(baseUrl, externalTransactionBuilder.build());
    }

    public DeepLinkBuilder(String baseUrl, ExternalTransaction externalTransaction) {
        mBaseUrl = baseUrl;
        mExternalTransaction = externalTransaction;
    }

    public static Transaction decodeData(byte[] b64TxData) {
        BytesData txData = Base64UrlSafe.decode(b64TxData);
        return Transaction.fromEncoded(txData.toHexString());
    }

    public String build() {
        BytesData enc = Base64UrlSafe.encode(mExternalTransaction.encode().getBytes());
        String out = mBaseUrl + "/tx/" + enc.stringValue();
        if (mCheckPass != null) {
            out += "?p=" + Base64UrlSafe.encodeString(mCheckPass);
        }

        return out;
    }

    public DeepLinkBuilder setCheckPassword(String password) {
        mCheckPass = password;
        return this;
    }

    public String getBaseUrl() {
        return mBaseUrl;
    }

    public DeepLinkBuilder setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return this;
    }

    public Transaction getTransaction() throws OperationInvalidDataException {
        return mExternalTransaction.toTransaction();
    }
}
