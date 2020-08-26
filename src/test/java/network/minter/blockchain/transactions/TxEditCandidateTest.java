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
import network.minter.core.crypto.PrivateKey;
import network.minter.core.internal.exceptions.NativeLoadException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * minter-android-blockchain. 2019
 * @author Eduard Maximovich [edward.vstock@gmail.com]
 */
public class TxEditCandidateTest {

    static {
        try {
            MinterSDK.initialize();
        } catch (NativeLoadException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        //original expand list pencil blade ivory express achieve inside stool apple truck
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        MinterAddress address = new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31d");
        MinterPublicKey pubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0");
        MinterPublicKey nPubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe1");
        String validTx = "f8d4100101800eb883f881a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe194d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d808001b845f8431ca06a9ce263674f403e2e612ac7055933c662db6c2db199635de985a69b9c0032baa03f4e2cd2bb33f89a10d4fdd4024f1767bfa94e87da47e075b4d5cbcaf519f66b";

        BigInteger nonce = new BigInteger("16");
        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoinId(MinterSDK.DEFAULT_COIN_ID)
                .setBlockchainId(BlockchainID.MainNet)
                .editCandidate()
                .setPublicKey(pubKey)
                .setNewPublicKey(nPubKey)
                .setRewardAddress(address)
                .setOwnerAddress(address)
                .setControlAddress(address)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testDecodeSingle() {
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        MinterAddress address = new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31d");
        MinterPublicKey pubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0");
        MinterPublicKey nPubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe1");
        String validTx = "f8d4100101800eb883f881a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe194d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d808001b845f8431ca06a9ce263674f403e2e612ac7055933c662db6c2db199635de985a69b9c0032baa03f4e2cd2bb33f89a10d4fdd4024f1767bfa94e87da47e075b4d5cbcaf519f66b";

        BigInteger nonce = new BigInteger("16");
        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);
        assertEquals(nonce, tx.getNonce());

        TxEditCandidate data = tx.getData();


        assertEquals(pubKey, data.getPublicKey());
        assertEquals(nPubKey, data.getNewPublicKey());
        assertEquals(address, data.getOwnerAddress());
        assertEquals(address, data.getRewardAddress());
        assertEquals(address, data.getControlAddress());

    }


    @Test
    public void testEncodeSingleWONewPubKey() throws OperationInvalidDataException {
        //original expand list pencil blade ivory express achieve inside stool apple truck
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        MinterAddress address = new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31d");
        MinterPublicKey pubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0");
        String validTx = "f8b4100101800eb863f861a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe08094d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d808001b845f8431ba07f5ca6bfef9b876677a328b892382d57383621d4a7b057a2516bb21372c2b585a059b3293bf705d973096680b85f60bf10e27f23b515f9ac429d0261ffb1222cb9";

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

        TransactionSign sign = tx.signSingle(privateKey);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testDecodeSingleWONewPubKey() {
        PrivateKey privateKey = new PrivateKey("4daf02f92bf760b53d3c725d6bcc0da8e55d27ba5350c78d3a88f873e502bd6e");
        MinterAddress address = new MinterAddress("Mxd82558ea00eb81d35f2654953598f5d51737d31d");
        MinterPublicKey pubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe0");
        MinterPublicKey nPubKey = new MinterPublicKey("Mp0208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe1");
        String validTx = "f8b4100101800eb863f861a00208f8a2bd535f65ecbe4b057b3b3c5fbfef6003b0713dc37b697b1d19153fe08094d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d94d82558ea00eb81d35f2654953598f5d51737d31d808001b845f8431ba07f5ca6bfef9b876677a328b892382d57383621d4a7b057a2516bb21372c2b585a059b3293bf705d973096680b85f60bf10e27f23b515f9ac429d0261ffb1222cb9";

        BigInteger nonce = new BigInteger("16");
        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);
        assertEquals(nonce, tx.getNonce());

        TxEditCandidate data = tx.getData();


        assertEquals(pubKey, data.getPublicKey());
        assertEquals(null, data.getNewPublicKey());
        assertEquals(address, data.getOwnerAddress());
        assertEquals(address, data.getRewardAddress());
        assertEquals(address, data.getControlAddress());

    }
}
