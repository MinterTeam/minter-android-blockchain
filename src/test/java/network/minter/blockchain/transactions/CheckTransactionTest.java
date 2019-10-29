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
import network.minter.core.crypto.MinterCheck;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.internal.exceptions.NativeLoadException;

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

        final String validCheckData = "Mcf8a58331323802843b9ac9ff8a4d4e54000000000000008906f05b59d3b2000000b8412374f8a8a53c3efe9d8617ad2c0ea8532c71babba55c72e23fff581c96987a233a0ff89affaccbac423b04e1176c4b4fbfbc642c2ef76d2d1f0aafd2250de771011ba05afa84a3971fb430fd3594a0d573a3fa9618299e970912d4754e02c7bf61b343a00e771ed1425dade278f81dd59aed48fccc976294ecbc34b76bce115fa93cea68";
        CheckTransaction validCheck = CheckTransaction.fromEncoded(validCheckData);

        CheckTransaction check = new CheckTransaction.Builder(new BigInteger("128"), "hello")
                .setChainId(BlockchainID.TestNet)
                .setCoin("MNT")
                .setDueBlock(new BigInteger("999999999"))
                .setValue("128")
                .build();
        TransactionSign sign = check.sign(privateKey);

        MinterCheck checkData = new MinterCheck(sign.getTxSign());

        CheckTransaction decoded = CheckTransaction.fromEncoded(checkData.toString());
        assertEquals(validCheck.getNonce(), decoded.getNonce());
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
        PrivateKey privateKey = new PrivateKey("64e27afaab363f21eec05291084367f6f1297a7b280d69d672febecda94a09ea");
        MinterAddress address = new MinterAddress("Mxa7bc33954f1ce855ed1a8c768fdd32ed927def47");
        String pass = "pass";
        String validCheck = "Mcf8a38334383002830f423f8a4d4e5400000000000000888ac7230489e80000b841d184caa333fe636288fc68d99dea2c8af5f7db4569a0bb91e03214e7e238f89d2b21f4d2b730ef590fd8de72bd43eb5c6265664df5aa3610ef6c71538d9295ee001ba08bd966fc5a093024a243e62cdc8131969152d21ee9220bc0d95044f54e3dd485a033bc4e03da3ea8a2cd2bd149d16c022ee604298575380db8548b4fd6672a9195";
        String validProof = "da021d4f84728e0d3d312a18ec84c21768e0caa12a53cb0a1452771f72b0d1a91770ae139fd6c23bcf8cec50f5f2e733eabb8482cf29ee540e56c6639aac469600";

        CheckTransaction check = new CheckTransaction.Builder(new BigInteger("480"), pass)
                .setCoin("MNT")
		        .setChainId(BlockchainID.TestNet)
                .setDueBlock(new BigInteger("999999"))
                .setValue("10")
                .build();

        TransactionSign sign = check.sign(privateKey);

        assertEquals(validCheck, sign.getTxSign());

	    UnsignedBytesData proof = CheckTransaction.makeProof(address, pass);
        assertEquals(validProof, proof.toHexString());

    }
}
