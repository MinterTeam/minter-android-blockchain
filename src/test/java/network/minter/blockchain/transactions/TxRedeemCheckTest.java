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
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxRedeemCheck;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static network.minter.core.MinterSDK.DEFAULT_COIN_ID;
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
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        String validTx = "f901350101018009b8e4f8e2b89df89b843130303201830f423f80880de0b6b3a764000080b8412b326337a6f1fc5617a3f9b32b0949cdf6761db0129d6507de155c21513b6a0334deb6d0bb4662426d4472716cde0b8258f47c99a12f93a05b2e732c4caaa9fa011ba0bdbd9d7d63b157fc232d5d859d13916e85e076632614013902b838c02e294428a06c031b2115e2c7c68c8808f84bba0cd8be5d882104b5a5c8355aa36008354e39b8413d02668333291917face5bbdc6c5bb6c2020479b720b3ee345b095a79a913409136a09c192b9483f0ae973cf6c86a71a9b440e7bdcb9437489463b93e15382a300808001b845f8431ba07020bc3b709ca547d0eeffb4baf0bd897dcfb4adabfed6113f1f1e9048335271a02af056405d1fe8feff5004cf693de523645c6001bd9ba4a5d41a838ed3fd040e";
        String validCheck = "Mcf89b843130303201830f423f80880de0b6b3a764000080b8412b326337a6f1fc5617a3f9b32b0949cdf6761db0129d6507de155c21513b6a0334deb6d0bb4662426d4472716cde0b8258f47c99a12f93a05b2e732c4caaa9fa011ba0bdbd9d7d63b157fc232d5d859d13916e85e076632614013902b838c02e294428a06c031b2115e2c7c68c8808f84bba0cd8be5d882104b5a5c8355aa36008354e39";
        String validProof = "3d02668333291917face5bbdc6c5bb6c2020479b720b3ee345b095a79a913409136a09c192b9483f0ae973cf6c86a71a9b440e7bdcb9437489463b93e15382a300";

        Transaction tx = new Transaction.Builder(new BigInteger("1"))
                .setBlockchainId(BlockchainID.MainNet)
                .setGasPrice(new BigInteger("1"))
                .setGasCoinId(DEFAULT_COIN_ID)
                .redeemCheck()
                .setProof(validProof)
                .setRawCheck(validCheck)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testDecode() {
        String validCheck = "Mcf89b843130303201830f423f80880de0b6b3a764000080b8412b326337a6f1fc5617a3f9b32b0949cdf6761db0129d6507de155c21513b6a0334deb6d0bb4662426d4472716cde0b8258f47c99a12f93a05b2e732c4caaa9fa011ba0bdbd9d7d63b157fc232d5d859d13916e85e076632614013902b838c02e294428a06c031b2115e2c7c68c8808f84bba0cd8be5d882104b5a5c8355aa36008354e39";
        String validProof = "3d02668333291917face5bbdc6c5bb6c2020479b720b3ee345b095a79a913409136a09c192b9483f0ae973cf6c86a71a9b440e7bdcb9437489463b93e15382a300";
        String pass = "pass";
        MinterAddress address = new MinterAddress("Mx67691076548b20234461ff6fd2bc9c64393eb8fc");

        Transaction tx = Transaction.fromEncoded("f901350101018009b8e4f8e2b89df89b843130303201830f423f80880de0b6b3a764000080b8412b326337a6f1fc5617a3f9b32b0949cdf6761db0129d6507de155c21513b6a0334deb6d0bb4662426d4472716cde0b8258f47c99a12f93a05b2e732c4caaa9fa011ba0bdbd9d7d63b157fc232d5d859d13916e85e076632614013902b838c02e294428a06c031b2115e2c7c68c8808f84bba0cd8be5d882104b5a5c8355aa36008354e39b8413d02668333291917face5bbdc6c5bb6c2020479b720b3ee345b095a79a913409136a09c192b9483f0ae973cf6c86a71a9b440e7bdcb9437489463b93e15382a300808001b845f8431ba07020bc3b709ca547d0eeffb4baf0bd897dcfb4adabfed6113f1f1e9048335271a02af056405d1fe8feff5004cf693de523645c6001bd9ba4a5d41a838ed3fd040e");

        assertEquals(new BigInteger("1"), tx.getNonce());
        assertEquals(BlockchainID.MainNet, tx.getBlockchainId());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(DEFAULT_COIN_ID, tx.getGasCoinId());

        TxRedeemCheck data = tx.getData();

        assertEquals(validProof, data.getProof().toHexString());
        assertEquals(validCheck, data.getRawCheck().toString());

        CheckTransaction check = data.getDecodedCheck();
        assertEquals(DEFAULT_COIN_ID, check.getCoinId());
        assertEquals(new BigDecimal("1"), check.getValue());
        assertEquals(new BigInteger("999999"), check.getDueBlock());
    }
}
