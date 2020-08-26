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
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;
import network.minter.core.util.FastByteComparisons;

import static junit.framework.TestCase.assertTrue;
import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
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
    public void testSignCheck() {
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        MinterAddress address = new MinterAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc");
        String pass = "pass";
        String validCheck = "Mcf89a8334383001830f423f80888ac7230489e8000080b84191ea56636b6667bb9da14bd412d492b90b9ae29799a90d0d69a637f3894c8ba246aae2b466fe76acab0a65cc6791ab2ae29d155b56efe84a929e089a22e15615001ba0d88c6543ac5d791428d46f8625c0af8e908fde3ec339e3d77ecb585e7c507ea8a021debf60dd96497d430b3cd92dac7bf22a00b4518f39d084c1dc50ba4c8b0d3b";
        String validProof = "7afddf8a86013784a056a3fa7ce3b5b07e259870e686c5f3df4eb656f8e6800c732bd8cb13fbdca0100433d98a760a8736249a41ad34b8af445b807ba53974f500";


        CheckTransaction check = new CheckTransaction.Builder(new BigInteger("480"), pass)
                .setChainId(BlockchainID.MainNet)
                .setGasCoin(DEFAULT_COIN_ID)
                .setCoinId(DEFAULT_COIN_ID)
                .setDueBlock(new BigInteger("999999"))
                .setValue("10")
                .build();

        TransactionSign sign = check.sign(privateKey);

        CheckTransaction decoded = CheckTransaction.fromEncoded(sign.getTxSign());
        assertEquals(new BigInteger("480"), decoded.getNonceNumeric());
        assertTrue(FastByteComparisons.equal("480".getBytes(), decoded.getNonce().getBytes()));
        assertEquals(BlockchainID.MainNet, decoded.getChainId());
        assertEquals(DEFAULT_COIN_ID, decoded.getCoinId());
        assertEquals(DEFAULT_COIN_ID, decoded.getGasCoinId());
        assertEquals(new BigInteger("999999"), decoded.getDueBlock());
        assertEquals(new BigDecimal("10"), decoded.getValue());

        CheckTransaction validDec = CheckTransaction.fromEncoded(validCheck);

        assertEquals(validCheck, sign.getTxSign());

        BytesData proof = CheckTransaction.makeProof(address, pass);
        assertEquals(validProof, proof.toHexString());
        System.out.println(proof.toHexString());

    }
}
