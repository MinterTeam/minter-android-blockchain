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

import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.CheckTransaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.internal.exceptions.NativeLoadException;

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
        PrivateKey privateKey = new PrivateKey("64e27afaab363f21eec05291084367f6f1297a7b280d69d672febecda94a09ea");
        MinterAddress address = new MinterAddress("Mxa7bc33954f1ce855ed1a8c768fdd32ed927def47");
        String pass = "pass";
	    String validCheck = "Mcf8a00102830f423f8a4d4e5400000000000000888ac7230489e80000b8419200e3c947484ced3268eebd1810d640ac0d6c6a099e4d87e074bab6a5751a324540e1e53907a10c9fb73f944490a737034de4a8bae96e707b5acbf8015dd8cb001ba0cbbc87bc7018f2c3bcaea67968713389addc3bf72f698b8b44ffddc384fca230a07ff35524aaca365fdac2eb25d29e9ba8431484fcb2b890d6d940d2527daeca22";
        String validProof = "da021d4f84728e0d3d312a18ec84c21768e0caa12a53cb0a1452771f72b0d1a91770ae139fd6c23bcf8cec50f5f2e733eabb8482cf29ee540e56c6639aac469600";

        CheckTransaction check = new CheckTransaction.Builder(new BigInteger("1"), pass)
                .setCoin("MNT")
		        .setChainId(BlockchainID.TestNet)
                .setDueBlock(new BigInteger("999999"))
                .setValue(10d)
                .build();

        TransactionSign sign = check.sign(privateKey);

        assertEquals(validCheck, sign.getTxSign());

	    UnsignedBytesData proof = CheckTransaction.makeProof(address, pass);
        assertEquals(validProof, proof.toHexString());

    }
}
