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

import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxEditCandidate;
import network.minter.core.MinterSDK;
import network.minter.core.crypto.MinterAddress;
import network.minter.core.crypto.MinterPublicKey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxEditCandidateTest extends BaseTxTest {


    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        //original expand list pencil blade ivory express achieve inside stool apple truck
        MinterAddress address = new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31d");
        MinterPublicKey pubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0");
        MinterPublicKey nPubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe1");
        String validTx = "f8b3100101800eb862f860a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe094d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d808001b845f8431ba021c0f2da522422607325e32fa3915ea29d23559f0e20464da688bb45b04a59a8a06e235dc9fe780dfa4cb349062041be95d7bc656c7ff52a571507de7989c4a8b1";

        BigInteger nonce = new BigInteger("16");
        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .editCandidate()
                .setPublicKey(pubKey)
                .setRewardAddress(address)
                .setOwnerAddress(address)
                .setControlAddress(address)
                .build();

        TransactionSign sign = tx.signSingle(UNIT_KEY);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testDecodeSingle() {
        MinterAddress address = new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31d");
        MinterPublicKey pubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0");
        MinterPublicKey nPubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe1");
        String validTx = "f8b3100101800eb862f860a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe094d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d808001b845f8431ba021c0f2da522422607325e32fa3915ea29d23559f0e20464da688bb45b04a59a8a06e235dc9fe780dfa4cb349062041be95d7bc656c7ff52a571507de7989c4a8b1";

        BigInteger nonce = new BigInteger("16");
        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);
        assertEquals(nonce, tx.getNonce());

        TxEditCandidate data = tx.getData();


        assertEquals(pubKey, data.getPublicKey());
        assertEquals(address, data.getOwnerAddress());
        assertEquals(address, data.getRewardAddress());
        assertEquals(address, data.getControlAddress());

    }
}
