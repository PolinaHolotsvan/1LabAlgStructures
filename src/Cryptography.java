import java.math.BigInteger;
import java.util.Random;

interface Cryptography {
    // 8) Криптосистема RSA або криптосистема Рабіна (на вибір).
    static void rsa(BigInteger message) {
        BigInteger p = BigInteger.valueOf(3557), q = BigInteger.valueOf(2579),
                n = p.multiply(q),
                phi = (p.subtract(Main.one)).multiply(q.subtract(Main.one)),
                exp = Main.two;

        while (!NumTheory.gcd(exp, phi).equals(Main.one)) {
            exp = exp.add(Main.one);
        }

        BigInteger secretExp = NumTheory.extendedEuclid(exp, phi);

        BigInteger encryption = message.modPow(exp, n);
        System.out.println("Message encryption: " + encryption);

        BigInteger decryption = encryption.modPow(secretExp, n);
        System.out.println("Message decryption: " + decryption);
    }

    // 9) Криптосистема Ель-Гамаля над еліптичними кривими.

    static BigPoint pointAddition(BigPoint point1, BigPoint point2, BigInteger q, BigInteger a) {
        if (point1.x.compareTo(point2.x) == 0 && !(point1.y.compareTo(point2.y) == 0))
            return new BigPoint(Main.zero, Main.zero);

        BigInteger lambda;
        if (point1.x.compareTo(point2.x) == 0)
            lambda = (Main.three.multiply(point1.x).multiply(point2.x).add(a)).multiply((Main.two.multiply(point1.y)).modInverse(q));
        else lambda = (point2.y.subtract(point1.y)).multiply((point2.x.subtract(point1.x)).modInverse(q));

        BigInteger x3 = lambda.multiply(lambda).subtract(point1.x).subtract(point2.x).mod(q),
                y3 = lambda.multiply(point1.x.subtract(x3)).subtract(point1.y).mod(q);

        return new BigPoint(x3, y3);
    }

    static BigPoint doubleAndAdd(BigPoint point, BigInteger q, BigInteger k, BigInteger a) {
        String kBit = Integer.toBinaryString(k.intValue() - 1);

        BigPoint pointR, pointA;
        pointR = pointA = point;

        for (var bit : kBit.toCharArray()) {
            if (bit == '1')
                pointR = pointAddition(pointR, pointA, q, a);
            else
                pointA = pointAddition(pointA, pointA, q, a);
        }

        return pointR;
    }

    static int getRandom(BigInteger n) {
        Random rnd = new Random();
        int nInt = n.intValue();

        if (nInt > 0)
            return rnd.nextInt(nInt - 1) + 1;

        return rnd.nextInt() + 1;
    }

    static void elgamal(BigInteger a, BigInteger b, BigInteger q, BigInteger n, BigPoint p, BigInteger m) {
        BigInteger k = BigInteger.valueOf(getRandom(n));

        BigPoint y = doubleAndAdd(p, q, k, a);

        BigInteger r = BigInteger.valueOf(getRandom(n));

        BigPoint g, h, mAlice;

        mAlice = doubleAndAdd(p, q, m, a);
        System.out.println("Alice`s M point: \n" + mAlice.toString());

        g = doubleAndAdd(p, q, r, a);
        h = pointAddition(mAlice, doubleAndAdd(y, q, r, a), q, a);
        System.out.println("Alice`s ciphertext: \n" + g.toString() + "\n" + h.toString());

        BigPoint s, mBob;

        s = doubleAndAdd(g, q, k, a);
        s.y = s.y.negate();

        mBob = pointAddition(s, h, q, a);
        System.out.println("Bob`s M point: \n" + mBob.toString());
    }
}
