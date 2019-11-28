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
import network.minter.blockchain.models.operational.CheckTransaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterCheck;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;
import network.minter.core.util.FastByteComparisons;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2018
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class CheckTransactionTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncodeDecodeNumericNonce() {
        PrivateKey privateKey = PrivateKey.fromMnemonic("december wedding engage learn plate lion phone lemon hill grocery effort dismiss");
        MinterAddress address = new MinterAddress("Mx5f0b55330e289490efa54c92e2120d6ebb6514ca");

        CheckTransaction check = new CheckTransaction.Builder(new BigInteger("128"), "hello")
                .setChainId(BlockchainID.TestNet)
                .setCoin("MNT")
                .setGasCoin("MNT")
                .setDueBlock(new BigInteger("999999999"))
                .setValue("128")
                .build();
        TransactionSign sign = check.sign(privateKey);

        MinterCheck checkData = new MinterCheck(sign.getTxSign());

        final String validCheckData = "Mcf8b08331323802843b9ac9ff8a4d4e54000000000000008906f05b59d3b20000008a4d4e5400000000000000b841b59c9a11ee79a5dbe6e40383a5db5a90960b452e5fddc63cc8f3d092ebf7e39303340d8f42bda3b55a681b9ece3229f9cf718d717ef0c2cb818c52a9b93f27d9001ca0afe5f4c59f1a1f64bd2d7bb97f0fc0cbb9cf1b40d12dc59f948dc419bbad51f8a05033b98e743a9d2af329e890933ea585785573d3a40f52aaa76858083d68654e";
        CheckTransaction validCheck = CheckTransaction.fromEncoded(validCheckData);

        CheckTransaction decoded = CheckTransaction.fromEncoded(checkData.toString());
        assertEquals(validCheck.getNonce(), decoded.getNonce());
        assertEquals("MNT", decoded.getGasCoin());
        assertEquals(validCheck.getGasCoin(), decoded.getGasCoin());
        assertEquals(validCheck.getChainId(), decoded.getChainId());
        assertEquals(validCheck.getCoin(), decoded.getCoin());
        assertEquals(validCheck.getDueBlock(), decoded.getDueBlock());
        assertEquals(validCheck.getValue(), decoded.getValue());

        assertEquals("Invalid lock in decoded check", validCheck.getLock(), decoded.getLock());
        assertEquals("Invalid lock in raw check", validCheck.getLock(), check.getLock());


        assertTrue(validCheck.getSignature().equals(decoded.getSignature()));
        assertTrue(validCheck.getSignature().equals(check.getSignature()));


        assertEquals(validCheckData, sign.getTxSign());
    }

    @Test
    public void testSignCheck() {
        PrivateKey privateKey = PrivateKey.fromMnemonic("december wedding engage learn plate lion phone lemon hill grocery effort dismiss");
        MinterAddress address = new MinterAddress("Mx5f0b55330e289490efa54c92e2120d6ebb6514ca");
        String pass = "hello";
        String validCheck = "Mcf8b08331323802843b9ac9ff8a4d4e54000000000000008906f05b59d3b20000008a4d4e5400000000000000b841b59c9a11ee79a5dbe6e40383a5db5a90960b452e5fddc63cc8f3d092ebf7e39303340d8f42bda3b55a681b9ece3229f9cf718d717ef0c2cb818c52a9b93f27d9001ca0afe5f4c59f1a1f64bd2d7bb97f0fc0cbb9cf1b40d12dc59f948dc419bbad51f8a05033b98e743a9d2af329e890933ea585785573d3a40f52aaa76858083d68654e";

        CheckTransaction check = new CheckTransaction.Builder(new BigInteger("128"), pass)
                .setChainId(BlockchainID.TestNet)
                .setGasCoin("MNT")
                .setCoin("MNT")
                .setDueBlock(new BigInteger("999999999"))
                .setValue("128")
                .build();

        TransactionSign sign = check.sign(privateKey);

        CheckTransaction decoded = CheckTransaction.fromEncoded(sign.getTxSign());
        assertEquals(new BigInteger("128"), decoded.getNonceNumeric());
        assertTrue(FastByteComparisons.equal("128".getBytes(), decoded.getNonce().getBytes()));
        assertEquals(BlockchainID.TestNet, decoded.getChainId());
        assertEquals("MNT", decoded.getCoin());
        assertEquals("MNT", decoded.getGasCoin());
        assertEquals(new BigInteger("999999999"), decoded.getDueBlock());
        assertEquals(new BigDecimal("128"), decoded.getValue());

        CheckTransaction validDec = CheckTransaction.fromEncoded(validCheck);

        assertEquals(validCheck, sign.getTxSign());

        BytesData proof = CheckTransaction.makeProof(address, pass);
        System.out.println(proof.toHexString());

    }
}
