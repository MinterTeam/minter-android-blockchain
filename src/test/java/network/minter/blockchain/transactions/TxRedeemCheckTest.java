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
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxRedeemCheck;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.crypto.UnsignedBytesData;
import network.minter.core.internal.exceptions.NativeLoadException;

import static org.junit.Assert.assertEquals;

public class TxRedeemCheckTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEncode() throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("64e27afaab363f21eec05291084367f6f1297a7b280d69d672febecda94a09ea");
        String validTx = "f901470102018a4d4e540000000000000009b8ecf8eab8a5f8a38334383002830f423f8a4d4e5400000000000000888ac7230489e80000b841d184caa333fe636288fc68d99dea2c8af5f7db4569a0bb91e03214e7e238f89d2b21f4d2b730ef590fd8de72bd43eb5c6265664df5aa3610ef6c71538d9295ee001ba08bd966fc5a093024a243e62cdc8131969152d21ee9220bc0d95044f54e3dd485a033bc4e03da3ea8a2cd2bd149d16c022ee604298575380db8548b4fd6672a9195b841da021d4f84728e0d3d312a18ec84c21768e0caa12a53cb0a1452771f72b0d1a91770ae139fd6c23bcf8cec50f5f2e733eabb8482cf29ee540e56c6639aac469600808001b845f8431ca054ab4e282049e698be637e02378c08aa6af86a55d1749065db207ce61c77d49aa00355674c7fd492c9763ad8808e3da560f9b3ec1459fd1bb365f2403ac77ac783";
        String validCheck = "Mcf8a38334383002830f423f8a4d4e5400000000000000888ac7230489e80000b841d184caa333fe636288fc68d99dea2c8af5f7db4569a0bb91e03214e7e238f89d2b21f4d2b730ef590fd8de72bd43eb5c6265664df5aa3610ef6c71538d9295ee001ba08bd966fc5a093024a243e62cdc8131969152d21ee9220bc0d95044f54e3dd485a033bc4e03da3ea8a2cd2bd149d16c022ee604298575380db8548b4fd6672a9195";
        String validProof = "da021d4f84728e0d3d312a18ec84c21768e0caa12a53cb0a1452771f72b0d1a91770ae139fd6c23bcf8cec50f5f2e733eabb8482cf29ee540e56c6639aac469600";

        Transaction tx = new Transaction.Builder(new BigInteger("1"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasPrice(new BigInteger("1"))
                .setGasCoin("MNT")
                .redeemCheck()
                .setProof(validProof)
                .setRawCheck(validCheck)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testDecode() {
        String validCheck = "Mcf8a38334383002830f423f8a4d4e5400000000000000888ac7230489e80000b841d184caa333fe636288fc68d99dea2c8af5f7db4569a0bb91e03214e7e238f89d2b21f4d2b730ef590fd8de72bd43eb5c6265664df5aa3610ef6c71538d9295ee001ba08bd966fc5a093024a243e62cdc8131969152d21ee9220bc0d95044f54e3dd485a033bc4e03da3ea8a2cd2bd149d16c022ee604298575380db8548b4fd6672a9195";
        String validProof = "da021d4f84728e0d3d312a18ec84c21768e0caa12a53cb0a1452771f72b0d1a91770ae139fd6c23bcf8cec50f5f2e733eabb8482cf29ee540e56c6639aac469600";
        String pass = "pass";
        MinterAddress address = new MinterAddress("Mxa7bc33954f1ce855ed1a8c768fdd32ed927def47");

        Transaction tx = Transaction.fromEncoded("f901470102018a4d4e540000000000000009b8ecf8eab8a5f8a38334383002830f423f8a4d4e5400000000000000888ac7230489e80000b841d184caa333fe636288fc68d99dea2c8af5f7db4569a0bb91e03214e7e238f89d2b21f4d2b730ef590fd8de72bd43eb5c6265664df5aa3610ef6c71538d9295ee001ba08bd966fc5a093024a243e62cdc8131969152d21ee9220bc0d95044f54e3dd485a033bc4e03da3ea8a2cd2bd149d16c022ee604298575380db8548b4fd6672a9195b841da021d4f84728e0d3d312a18ec84c21768e0caa12a53cb0a1452771f72b0d1a91770ae139fd6c23bcf8cec50f5f2e733eabb8482cf29ee540e56c6639aac469600808001b845f8431ca054ab4e282049e698be637e02378c08aa6af86a55d1749065db207ce61c77d49aa00355674c7fd492c9763ad8808e3da560f9b3ec1459fd1bb365f2403ac77ac783");

        assertEquals(new BigInteger("1"), tx.getNonce());
        assertEquals(BlockchainID.TestNet, tx.getBlockchainId());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals("MNT", tx.getGasCoin());

        TxRedeemCheck data = tx.getData();

        assertEquals(validProof, data.getProof().toHexString());
        assertEquals(validCheck, data.getRawCheck().toString());

        CheckTransaction check = data.getDecodedCheck();
        assertEquals("MNT", check.getCoin());
        assertEquals(new BigDecimal("10"), check.getValue());
        assertEquals(new BigInteger("999999"), check.getDueBlock());

        assertEquals(CheckTransaction.makeProof(address, pass), new UnsignedBytesData(validProof));
    }
}
