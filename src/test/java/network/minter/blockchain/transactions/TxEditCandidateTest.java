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
    public void testPK() {
        String mnem = "original expand list pencil blade ivory express achieve inside stool apple truck";
        PrivateKey pk = PrivateKey.fromMnemonic(mnem);


        System.out.println(pk.toHexString());
        System.out.println(pk.getPublicKey(false).toHexString());
        System.out.println(pk.getPublicKey(true).toHexString());
        System.out.println(pk.getPublicKey().toMinter().toHexString());
    }


    @Test
    public void testEncodeSingle() throws OperationInvalidDataException {
        //original expand list pencil blade ivory express achieve inside stool apple truck
        PrivateKey privateKey = PrivateKey.fromMnemonic("original expand list pencil blade ivory express achieve inside stool apple truck");
        MinterAddress address = privateKey.getPublicKey().toMinter();
        String validTx = "f8a80102018a4d4e54000000000000000eb84df84ba00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a439442516215b2dd72187d3ef6adb19fc3aabbbced239442516215b2dd72187d3ef6adb19fc3aabbbced23808001b845f8431ba005b789fdca2d6a08ae47d62d60cfd442aad3eb7ed110373aba348ec687c15a28a035a7f223ab8d2675419f036445aac07b537882b1f3e10dd9e4d06cdb5ac44a38";

        BigInteger nonce = new BigInteger("1");
        Transaction tx = new Transaction.Builder(nonce)
                .setGasCoin("MNT")
                .setBlockchainId(BlockchainID.TestNet)
                .editCandidate()
                .setPublicKey(new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43"))
                .setRewardAddress(address)
                .setOwnerAddress(address)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        assertEquals(validTx, sign.getTxSign());
    }

    @Test
    public void testDecodeSingle() {
        PrivateKey privateKey = PrivateKey.fromMnemonic("original expand list pencil blade ivory express achieve inside stool apple truck");
        MinterAddress address = privateKey.getPublicKey().toMinter();
        String validTx = "f8a80102018a4d4e54000000000000000eb84df84ba00eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a439442516215b2dd72187d3ef6adb19fc3aabbbced239442516215b2dd72187d3ef6adb19fc3aabbbced23808001b845f8431ba005b789fdca2d6a08ae47d62d60cfd442aad3eb7ed110373aba348ec687c15a28a035a7f223ab8d2675419f036445aac07b537882b1f3e10dd9e4d06cdb5ac44a38";

        BigInteger nonce = new BigInteger("1");
        Transaction tx = Transaction.fromEncoded(validTx);
        assertNotNull(tx);
        assertEquals(nonce, tx.getNonce());

        TxEditCandidate data = tx.getData();

        MinterPublicKey pubKey = new MinterPublicKey("Mp0eb98ea04ae466d8d38f490db3c99b3996a90e24243952ce9822c6dc1e2c1a43");
        assertEquals(pubKey, data.getPubKey());
        assertEquals(address, data.getOwnerAddress());
        assertEquals(address, data.getRewardAddress());

    }
}
