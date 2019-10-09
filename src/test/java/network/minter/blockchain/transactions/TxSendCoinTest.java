/*
 * Copyright (C) by MinterTeam. 2019
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

package network.minter.blockchain.transactions;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxSendCoin;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.internal.exceptions.NativeLoadException;
import network.minter.core.util.RLPBoxed;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * MinterWallet. 2018
 * @author Eduard Maximovich <edward.vstock@gmail.com>
 */
public class TxSendCoinTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

	public void testSome() {
		BigInteger nonce = new BigInteger("128");
		char[] encoded = RLPBoxed.encode(new Object[]{nonce});
		UnsignedBytesData bd = new UnsignedBytesData(encoded);

		assertEquals("c28180", bd.toHexString());
	}

	@Test
	public void testDecode128Nonce()
			throws OperationInvalidDataException {
		PrivateKey privateKey = PrivateKey.fromMnemonic("body attitude enable enjoy swift wise example hammer trap saddle bike lobster");
		MinterAddress toAddress = privateKey.getPublicKey().toMinter();

		assertEquals(new MinterAddress("Mxb445feaf3eb747ac52426b054aa42b0b3d913e1f"), toAddress);

		BigInteger nonce = new BigInteger("128");
		String coin = MinterSDK.DEFAULT_COIN;
		String gasCoin = MinterSDK.DEFAULT_COIN;

		Transaction tx = new Transaction.Builder(nonce)
				.setGasCoin(gasCoin)
				.setGasPrice(new BigInteger("1"))
				.setBlockchainId(BlockchainID.TestNet)
				.sendCoin()
				.setCoin(coin)
				.setValue(new BigDecimal("1"))
				.setTo(toAddress)
				.build();
		TransactionSign sign = tx.signSingle(privateKey);

		String txraw = "f885818002018a4d4e540000000000000001aae98a4d4e540000000000000094b445feaf3eb747ac52426b054aa42b0b3d913e1f880de0b6b3a7640000808001b845f8431ba0c446cf0f2c8be0cb4ad7018fbd37289bdfcd458f39678488889b6ea52757718ca00de61c8890149e71a83546a3edda40fc393b13655a5dfe85711cd26e02aea51d";

		assertEquals(txraw, sign.getTxSign());

		Transaction decoded = Transaction.fromEncoded(txraw);

		assertEquals(new BigInteger("128"), decoded.getNonce());

	}

    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("df1f236d0396cc43147e44206c341a65573326e907d033690e31a21323c03a9f");
        MinterAddress toAddress = new MinterAddress("Mxee81347211c72524338f9680072af90744333146");
        MinterAddress from = new MinterAddress("Mxe176cbf6b307c61c5939a517fd0c09a6f999f1d2");
	    final String encodedTransaction = "f8880102018a4d4e540000000000000001aae98a4d4e540000000000000094ee81347211c72524338f9680072af90744333146880de0b6b3a764000084746573748001b845f8431ba016c8d27b1038823f87fba01eb97c9b16614e62b64904d923f168e296acbb384ca0754daff93835277e46fcd718d5d0295dda1153183f257db6146c15975099fcf7";
        BigInteger nonce = new BigInteger("1");
        String valueHuman = "1";
        String coin = "MNT";
        String gasCoin = "MNT";
        byte[] payload = "test".getBytes();

        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin(gasCoin)
                .setPayload(payload)
                .sendCoin()
                .setCoin(coin)
                .setValue(valueHuman)
                .setTo(toAddress)
                .build();

        assertNotNull(tx);
        TransactionSign sign = tx.signSingle(privateKey);
        assertNotNull(sign);
        assertEquals(encodedTransaction, sign.getTxSign());
        assertEquals(from, privateKey.getPublicKey().toMinter());


    }

    @Test
    public void testDecodeSingle() {
        PrivateKey privateKey = new PrivateKey("df1f236d0396cc43147e44206c341a65573326e907d033690e31a21323c03a9f");
        MinterAddress toAddress = new MinterAddress("Mxee81347211c72524338f9680072af90744333146");
	    final String encodedTransaction = "f8880102018a4d4e540000000000000001aae98a4d4e540000000000000094ee81347211c72524338f9680072af90744333146880de0b6b3a764000084746573748001b845f8431ba016c8d27b1038823f87fba01eb97c9b16614e62b64904d923f168e296acbb384ca0754daff93835277e46fcd718d5d0295dda1153183f257db6146c15975099fcf7";
        BigInteger nonce = new BigInteger("1");
        BigDecimal valueHuman = new BigDecimal("1");
        String coin = "MNT";
        String gasCoin = "MNT";
        String payload = "test";

        Transaction transaction = Transaction.fromEncoded(encodedTransaction);
        assertNotNull(transaction);
        assertEquals(toAddress, transaction.<TxSendCoin>getData().getTo());
        assertEquals(nonce, transaction.getNonce());
        assertEquals(valueHuman, transaction.<TxSendCoin>getData().getValue());
        assertEquals(coin, transaction.<TxSendCoin>getData().getCoin());
        assertEquals(gasCoin, transaction.getGasCoin());
	    assertEquals(payload, transaction.getPayload().stringValue());

        TransactionSign sign = transaction.signSingle(privateKey);
        assertEquals(encodedTransaction, sign.getTxSign());
    }
}
