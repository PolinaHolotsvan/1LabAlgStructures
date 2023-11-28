import java.math.BigInteger;
import java.util.Arrays;

public class Main {

    final static BigInteger zero = BigInteger.ZERO;
    final static BigInteger one = BigInteger.ONE;
    final static BigInteger minusOne = BigInteger.valueOf(-1);
    final static BigInteger two = BigInteger.valueOf(2);
    final static BigInteger three = BigInteger.valueOf(3);


    public static void main(String[] args) {
        BigInteger mobius1 = BigInteger.valueOf(17),
                mobius2 = BigInteger.valueOf(25),
                mobius3 = BigInteger.valueOf(6);
        NumTheory.printResult("mobius", NumTheory.mobius(mobius1), mobius1);
        NumTheory.printResult("mobius", NumTheory.mobius(mobius2), mobius2);
        NumTheory.printResult("mobius", NumTheory.mobius(mobius3), mobius3);


        BigInteger euler1 = BigInteger.valueOf(15);
        NumTheory.printResult("euler", NumTheory.euler(euler1), euler1);


        BigInteger[] chineseC = new BigInteger[]{
                BigInteger.valueOf(2),
                BigInteger.valueOf(3),
                BigInteger.valueOf(1)
        },
                chineseM = new BigInteger[]{
                        BigInteger.valueOf(3),
                        BigInteger.valueOf(4),
                        BigInteger.valueOf(5)
                };
        NumTheory.printResult("chineseRemainder", NumTheory.chineseRemainder(chineseC, chineseM),
                Arrays.toString(chineseC), Arrays.toString(chineseM));


        BigInteger jacobiA = BigInteger.valueOf(9),
                jacobiM = BigInteger.valueOf(17);
        NumTheory.printResult("jacobi", NumTheory.jacobi(jacobiA, jacobiM), jacobiA, jacobiM);


        BigInteger pollard = BigInteger.valueOf(8051);
        NumTheory.printResult("pollardRhoFactorization", NumTheory.pollardRhoFactorization(pollard), pollard);


        BigInteger cipollaN = BigInteger.valueOf(10),
                cipollaP = BigInteger.valueOf(13);
        NumTheory.printResult("cipolla",
                Arrays.toString(NumTheory.cipolla(cipollaN, cipollaP)), cipollaN, cipollaP);


        BigInteger stepA = BigInteger.valueOf(17),
                stepB = BigInteger.valueOf(438),
                stepN = BigInteger.valueOf(509);
        NumTheory.printResult("babyGiantStep",
                NumTheory.babyGiantStep(stepA, stepB, stepN), stepA, stepB, stepN);


        BigInteger millerRabinN = BigInteger.valueOf(13),
                millerRabinK = BigInteger.valueOf(4);
        NumTheory.printResult("millerRabin",
                NumTheory.millerRabin(millerRabinN, millerRabinK), millerRabinN, millerRabinK);

        System.out.println("\nrsa:");
        BigInteger rsaMessage = BigInteger.valueOf(111111);
        Cryptography.rsa(rsaMessage);


        System.out.println("\nelgamal:");
        BigInteger elgamalN = new BigInteger("115792089237316195423570985008687907852837564279074904382605163141518161494337"),
                elgamalQ = two.pow(256).subtract(two.pow(32)).subtract(two.pow(9))
                        .subtract(two.pow(8)).subtract(two.pow(7)).subtract(two.pow(6))
                        .subtract(two.pow(4)).subtract(one),
                elgamalA = zero,
                elgamalB = BigInteger.valueOf(7),
                elgamalM = BigInteger.valueOf(333);

        BigPoint elgamalP = new BigPoint(
                new BigInteger("55066263022277343669578718895168534326250603453777594175500187360389116729240"),
                new BigInteger("32670510020758816978083085130507043184471273380659243275938904335757337482424")
        );

        Cryptography.elgamal(elgamalA, elgamalB, elgamalQ, elgamalN, elgamalP, elgamalM);
    }
}



