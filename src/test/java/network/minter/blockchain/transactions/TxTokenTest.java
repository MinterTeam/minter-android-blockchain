/*
 * Copyright (C) by MinterTeam. 2021
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

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import network.minter.blockchain.models.operational.BlockchainID;
import network.minter.blockchain.models.operational.OperationInvalidDataException;
import network.minter.blockchain.models.operational.Transaction;
import network.minter.blockchain.models.operational.TransactionSign;
import network.minter.blockchain.models.operational.TxTokenBurn;
import network.minter.blockchain.models.operational.TxTokenCreate;
import network.minter.blockchain.models.operational.TxTokenMint;
import network.minter.blockchain.models.operational.TxTokenRecreate;
import network.minter.core.crypto.PrivateKey;

import static org.junit.Assert.assertEquals;

/**
 * minter-android-blockchain. 2021
 *
 * @author Eduard Maximovich (edward.vstock@gmail.com)
 */
public class TxTokenTest extends BaseTxTest {

    @Test
    public void testEncodeMint()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        String expectedTx = "f85c170201801c8ccb0389878678326eac900000808001b845f8431ba0dd78fa0dfb782d99d3f0983917ff8b73e571cbd56eb694bfd570368a31453abca07a6d86ed82e5de41ad27588a7e71b7030abb258c6992d38f4b995c1184ebf745";

        Transaction tx = new Transaction.Builder(new BigInteger("23"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .mintToken()
                .setCoinId(new BigInteger("3"))
                .setValue(new BigDecimal("2500"))
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecodeMint() {
        final String expectedTx = "f85c170201801c8ccb0389878678326eac900000808001b845f8431ba0dd78fa0dfb782d99d3f0983917ff8b73e571cbd56eb694bfd570368a31453abca07a6d86ed82e5de41ad27588a7e71b7030abb258c6992d38f4b995c1184ebf745";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("23"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxTokenMint data = tx.getData();
        assertEquals(new BigInteger("3"), data.getCoinId());
        assertEquals(new BigDecimal("2500"), data.getValue());
    }

    @Test
    public void testEncodeBurn()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        String expectedTx = "f85c180201801d8ccb0489878678326eac900000808001b845f8431ba0903a9bd68dfe002346698b111b2010e02a5fedc9094e0d4588659d28f38dca0ba03118723f4f6b32a068ab4981c666dd264ee7216c90e73d8d34129ed37b9b69b8";

        Transaction tx = new Transaction.Builder(new BigInteger("24"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .burnToken()
                .setCoinId(new BigInteger("4"))
                .setValue(new BigDecimal("2500"))
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecodeBurn() {
        final String expectedTx = "f85c180201801d8ccb0489878678326eac900000808001b845f8431ba0903a9bd68dfe002346698b111b2010e02a5fedc9094e0d4588659d28f38dca0ba03118723f4f6b32a068ab4981c666dd264ee7216c90e73d8d34129ed37b9b69b8";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("24"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxTokenBurn data = tx.getData();
        assertEquals(new BigInteger("4"), data.getCoinId());
        assertEquals(new BigDecimal("2500"), data.getValue());
    }

    @Test
    public void testEncodeCreate()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        String expectedTx = "f883130201801eb3f28e4255524e41424c4520544f4b454e8a4255524e41424c4500008a34f086f3b33b684000008a3f870857a3e0e38000008001808001b845f8431ba03803675da057cde3bf43a085d1c774a93f3f038e843626d42d26bcdc92310e1fa002c77de52d499f65d03d159672c88e1cd9262048c60904b5a80a5065aa37fce1";

        Transaction tx = new Transaction.Builder(new BigInteger("19"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .createToken()
                .setName("BURNABLE TOKEN")
                .setSymbol("BURNABLE")
                .setInitialAmount(new BigDecimal("250000"))
                .setMaxSupply(new BigDecimal("300000"))
                .setIsMintable(false)
                .setIsBurnable(true)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecodeCreate() {
        final String expectedTx = "f883130201801eb3f28e4255524e41424c4520544f4b454e8a4255524e41424c4500008a34f086f3b33b684000008a3f870857a3e0e38000008001808001b845f8431ba03803675da057cde3bf43a085d1c774a93f3f038e843626d42d26bcdc92310e1fa002c77de52d499f65d03d159672c88e1cd9262048c60904b5a80a5065aa37fce1";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("19"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxTokenCreate data = tx.getData();
        assertEquals("BURNABLE TOKEN", data.getName());
        assertEquals("BURNABLE", data.getSymbol());
        assertEquals(new BigDecimal("250000"), data.getInitialAmount());
        assertEquals(new BigDecimal("300000"), data.getMaxSupply());
        assertEquals(false, data.isMintable());
        assertEquals(true, data.isBurnable());
    }

    @Test
    public void testEncodeRecreate()
            throws OperationInvalidDataException {
        PrivateKey privateKey = new PrivateKey("474ad4c6517502f3f939e276ae619f494d586a9b6cae81d63f8287dda0aabd4f");
        String expectedTx = "f8901e0201801fb83ff83d994255524e41424c452026204d494e5441424c4520544f4b454e8a4255524e41424c4500008ad3c20dee1639f99c00008ad3c20dee1639f99c00000101808001b845f8431ca082c41369313d7d2c2fc1c455a2d1a0458da8f647ddbf45601f2edbef2299c5ffa06788aaeec5abff26d91f1d482903cb41fa57c34823e014aa711099f31b94a3be";

        Transaction tx = new Transaction.Builder(new BigInteger("30"))
                .setBlockchainId(BlockchainID.TestNet)
                .setGasCoinId(new BigInteger("0"))
                .setGasPrice(new BigInteger("1"))
                .recreateToken()
                .setName("BURNABLE & MINTABLE TOKEN")
                .setSymbol("BURNABLE")
                .setInitialAmount(new BigDecimal("999999"))
                .setMaxSupply(new BigDecimal("999999"))
                .setIsMintable(true)
                .setIsBurnable(true)
                .build();

        TransactionSign sign = tx.signSingle(privateKey);
        Assert.assertEquals(expectedTx, sign.getTxSign());
    }

    @Test
    public void testDecodeRecreate() {
        final String expectedTx = "f8901e0201801fb83ff83d994255524e41424c452026204d494e5441424c4520544f4b454e8a4255524e41424c4500008ad3c20dee1639f99c00008ad3c20dee1639f99c00000101808001b845f8431ca082c41369313d7d2c2fc1c455a2d1a0458da8f647ddbf45601f2edbef2299c5ffa06788aaeec5abff26d91f1d482903cb41fa57c34823e014aa711099f31b94a3be";

        Transaction tx = Transaction.fromEncoded(expectedTx);
        assertEquals(new BigInteger("30"), tx.getNonce());
        assertEquals(new BigInteger("1"), tx.getGasPrice());
        assertEquals(new BigInteger("0"), tx.getGasCoinId());

        TxTokenRecreate data = tx.getData();
        assertEquals("BURNABLE & MINTABLE TOKEN", data.getName());
        assertEquals("BURNABLE", data.getSymbol());
        assertEquals(new BigDecimal("999999"), data.getInitialAmount());
        assertEquals(new BigDecimal("999999"), data.getMaxSupply());
        assertEquals(true, data.isMintable());
        assertEquals(true, data.isBurnable());
    }
}
