/*
 * Copyright (C) 2018 by MinterTeam
 * @link https://github.com/MinterTeam
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

package network.minter.blockchainapi.models;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchainapi.models.operational.OperationType;
import network.minter.blockchainapi.models.operational.Transaction;
import network.minter.blockchainapi.models.operational.TransactionSign;
import network.minter.blockchainapi.models.operational.TxConvertCoin;
import network.minter.blockchainapi.models.operational.TxSendCoin;
import network.minter.mintercore.MinterSDK;
import network.minter.mintercore.crypto.BytesData;
import network.minter.mintercore.crypto.MinterAddress;
import network.minter.mintercore.crypto.PrivateKey;
import network.minter.mintercore.crypto.PublicKey;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

/**
 * MinterWallet. 2018
 *
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
@RunWith(AndroidJUnit4.class)
public class TransactionTest {

    @Test
    public void testSignPredefinedTransaction() {
        final String validSign = "f873010101aae98a4d4e540000000000000094fe60014a6e9ac91618f5d1cab3fd58cded61ee99880de0b6b3a764000080801ca0ae0ee912484b9bf3bee785f4cbac118793799450e0de754667e2c18faa510301a04f1e4ed5fad4b489a1065dc1f5255b356ab9a2ce4b24dde35bcb9dc43aba019c";

        final PrivateKey senderPrivateKey = new PrivateKey(
                "b574d2a7151fcf0df573feae58015f85f6ebf38ea4b38c49196c6aceee27e189");
        final MinterAddress recipientAddress = new MinterAddress("Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99");
        final MinterAddress senderAddress = new MinterAddress("Mx887c5de2515e788abb422c3e483496e1b1f3dff4");
        final PublicKey senderPublicKey = senderPrivateKey.getPublicKey();
        assertEquals(senderAddress, senderPublicKey.toMinter());

        Transaction<TxSendCoin> tx = Transaction
                .newSendTransaction(new BigInteger("1"))
                .setCoin("MNT")
                .setTo(recipientAddress)
                .setValue(new BigDecimal(1))
                .build();

        TransactionSign sign = tx.sign(senderPrivateKey);
        assertNotNull(sign);
        assertEquals(validSign, sign.getTxSign());
    }

    @Test
    public void testSignConvertCoinTransaction() {
        final String validTx = "f869010102a0df8a4d4e54000000000000008a53505254455354000000880de0b6b3a764000080801ba09ea1259e0b94b0e136c54ddf9fe97aefb47c6208a307a692169f4f7c8606d24aa030fee0c7978dde0aa9ab814593ab3c99c1be3d0ce0ceeb607f25a29acf3a958c";
        final PrivateKey privateKey = new PrivateKey("b574d2a7151fcf0df573feae58015f85f6ebf38ea4b38c49196c6aceee27e189");
        final String fromCoin = "MNT";
        final String toCoin = "SPRTEST";
        final BigDecimal amount = new BigDecimal(1);

        Transaction<TxConvertCoin> tx = Transaction.newConvertCoinTransaction(new BigInteger("1"))
                .setFromCoin(fromCoin)
                .setToCoin(toCoin)
                .setAmount(amount)
                .build();

        TransactionSign sign = tx.sign(privateKey);
        assertNotNull(sign);
        assertEquals(validTx, sign.getTxSign());

    }

    @Test
    public void testSignRealTransaction() {
        final String validTx = "f873010101aae98a4d4e540000000000000094857ce4c4a55929bcbe43c7e386e4ede004e3fbfc8801c6bf526340000080801ca0fb7d1cf0ab1a444c18199cd74215c81dab4bbb95a30209ca2689fe4d8788da74a019bde5302bc9e5121c69434d535423b050292974195ed28dce7b4cb0cf3f2604";
        final MinterAddress address = new MinterAddress("Mx790053b45145188729465477876e0f04b5c5b1f9");
        final PrivateKey privateKey = new PrivateKey("7ec91ba19d0221a125aaedaddadb81b9b88bd536b92825147f9a7859864b4ccc");
        final PublicKey publicKey = new PublicKey("030ac3746fa6305cf1e0c6ebe92a1441d4802d36c7e820068f973bc6478107cd7d");
        final BytesData seed = new BytesData("f88392faf3c460b05bbd50278362d12b19dc8e0cf2ebd5c97cc9ce01b5db819c7364f4d72d3f2ef85c96328bcc49763ad31bf546f8d0e56009524fe3b9b01502");
        final String phrase = "royal park wage travel execute focus brother click twin stove drift margin";

        assertEquals(publicKey, privateKey.getPublicKey(true));
        assertEquals(address, publicKey.toMinter());


    }

    @Test
    public void testDecodeSendTransaction() {
        MinterAddress fromAddress = new MinterAddress("Mx887c5de2515e788abb422c3e483496e1b1f3dff4");
        MinterAddress toAddress = new MinterAddress("Mxfe60014a6e9ac91618f5d1cab3fd58cded61ee99");
        final String encodedTransaction = "Mxf873010101aae98a4d4e540000000000000094fe60014a6e9ac91618f5d1cab3fd58cded61ee99880de0b6b3a764000080801ca0ae0ee912484b9bf3bee785f4cbac118793799450e0de754667e2c18faa510301a04f1e4ed5fad4b489a1065dc1f5255b356ab9a2ce4b24dde35bcb9dc43aba019c";
        BigInteger nonce = new BigInteger("1");
        BigInteger gasPrice = new BigInteger("1");
        OperationType type = OperationType.SendCoin;
        long valueHuman = 1L;
        BigInteger value = new BigInteger("1").multiply(Transaction.VALUE_MUL);
        String coin = MinterSDK.DEFAULT_COIN;

        Transaction<TxSendCoin> tx = Transaction.fromEncoded(encodedTransaction, TxSendCoin.class);

        assertNotNull(tx);
        assertEquals(nonce, tx.getNonce());
        assertEquals(gasPrice, tx.getGasPrice());
        assertEquals(type, tx.getType());

        assertNotNull(tx.getData());
        assertTrue(tx.getData() instanceof TxSendCoin);
        assertEquals(valueHuman, tx.getData().getValue());
        assertEquals(value, tx.getData().getValueBigInteger());
        assertEquals(coin, tx.getData().getCoin());
        assertEquals(toAddress, tx.getData().getTo());
    }
}
