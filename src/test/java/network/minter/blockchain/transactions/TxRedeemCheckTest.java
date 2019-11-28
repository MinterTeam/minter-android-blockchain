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
import network.minter.core.crypto.BytesData;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.PrivateKey;
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
        PrivateKey privateKey = PrivateKey.fromMnemonic("december wedding engage learn plate lion phone lemon hill grocery effort dismiss");
        String validTx = "f901540102018a4d4e540000000000000009b8f9f8f7b8b2f8b08331323802843b9ac9ff8a4d4e54000000000000008906f05b59d3b20000008a4d4e5400000000000000b841b59c9a11ee79a5dbe6e40383a5db5a90960b452e5fddc63cc8f3d092ebf7e39303340d8f42bda3b55a681b9ece3229f9cf718d717ef0c2cb818c52a9b93f27d9001ca0afe5f4c59f1a1f64bd2d7bb97f0fc0cbb9cf1b40d12dc59f948dc419bbad51f8a05033b98e743a9d2af329e890933ea585785573d3a40f52aaa76858083d68654eb841133824027bddf75120c93cf183f5ff18beea9c350203eb7af02bcbbbca5e282201efe7e4eac2494de85b762296dd4b7ea7879b238a6dd8b012838ee6fc04d51501808001b845f8431ca0d00ee903c7859ec891a983ff70b4167843140a62a5d346df29baf344c9feedd6a0323f997418d719e3a4d5b1da8854e60cb9f70674d12763214c374f9bd53d47ec";
        String validCheck = "Mcf8b08331323802843b9ac9ff8a4d4e54000000000000008906f05b59d3b20000008a4d4e5400000000000000b841b59c9a11ee79a5dbe6e40383a5db5a90960b452e5fddc63cc8f3d092ebf7e39303340d8f42bda3b55a681b9ece3229f9cf718d717ef0c2cb818c52a9b93f27d9001ca0afe5f4c59f1a1f64bd2d7bb97f0fc0cbb9cf1b40d12dc59f948dc419bbad51f8a05033b98e743a9d2af329e890933ea585785573d3a40f52aaa76858083d68654e";
        String validProof = "133824027bddf75120c93cf183f5ff18beea9c350203eb7af02bcbbbca5e282201efe7e4eac2494de85b762296dd4b7ea7879b238a6dd8b012838ee6fc04d51501";

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
        String validCheck = "Mcf8b08331323802843b9ac9ff8a4d4e54000000000000008906f05b59d3b20000008a4d4e5400000000000000b841b59c9a11ee79a5dbe6e40383a5db5a90960b452e5fddc63cc8f3d092ebf7e39303340d8f42bda3b55a681b9ece3229f9cf718d717ef0c2cb818c52a9b93f27d9001ca0afe5f4c59f1a1f64bd2d7bb97f0fc0cbb9cf1b40d12dc59f948dc419bbad51f8a05033b98e743a9d2af329e890933ea585785573d3a40f52aaa76858083d68654e";
        String validProof = "133824027bddf75120c93cf183f5ff18beea9c350203eb7af02bcbbbca5e282201efe7e4eac2494de85b762296dd4b7ea7879b238a6dd8b012838ee6fc04d51501";
        String pass = "hello";
        MinterAddress address = new MinterAddress("Mx5f0b55330e289490efa54c92e2120d6ebb6514ca");

        Transaction tx = Transaction.fromEncoded("f901540102018a4d4e540000000000000009b8f9f8f7b8b2f8b08331323802843b9ac9ff8a4d4e54000000000000008906f05b59d3b20000008a4d4e5400000000000000b841b59c9a11ee79a5dbe6e40383a5db5a90960b452e5fddc63cc8f3d092ebf7e39303340d8f42bda3b55a681b9ece3229f9cf718d717ef0c2cb818c52a9b93f27d9001ca0afe5f4c59f1a1f64bd2d7bb97f0fc0cbb9cf1b40d12dc59f948dc419bbad51f8a05033b98e743a9d2af329e890933ea585785573d3a40f52aaa76858083d68654eb841133824027bddf75120c93cf183f5ff18beea9c350203eb7af02bcbbbca5e282201efe7e4eac2494de85b762296dd4b7ea7879b238a6dd8b012838ee6fc04d51501808001b845f8431ca0d00ee903c7859ec891a983ff70b4167843140a62a5d346df29baf344c9feedd6a0323f997418d719e3a4d5b1da8854e60cb9f70674d12763214c374f9bd53d47ec");

        assertEquals(new BigInteger("1"), tx.getNonce());
        assertEquals(BlockchainID.TestNet, tx.getBlockchainId());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals("MNT", tx.getGasCoin());

        TxRedeemCheck data = tx.getData();

        assertEquals(validProof, data.getProof().toHexString());
        assertEquals(validCheck, data.getRawCheck().toString());

        CheckTransaction check = data.getDecodedCheck();
        assertEquals("MNT", check.getCoin());
        assertEquals(new BigDecimal("128"), check.getValue());
        assertEquals(new BigInteger("999999999"), check.getDueBlock());

        assertEquals(CheckTransaction.makeProof(address, pass), new BytesData(validProof));
    }
}
