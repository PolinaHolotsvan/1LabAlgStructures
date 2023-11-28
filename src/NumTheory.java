import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

interface NumTheory {

    // 1) Обчислення функцій Ейлера та Мьобіуса. Знаходження найменшого спільного кратного набору чисел
    static BigInteger mobius(BigInteger num) {
        if (num.equals(Main.one))
            return Main.one;
        BigInteger count = Main.zero;
        for (BigInteger i = Main.one; i.compareTo(num) <= 0; i = i.add(Main.one)) {
            if (isPrime(i)) {
                if (num.remainder(i.multiply(i)).equals(Main.zero)) {
                    return Main.zero;
                }
                if (num.remainder(i).equals(Main.zero)) {
                    count = count.add(Main.one);
                }
            }

        }

        return count.mod(Main.two).equals(Main.zero) ? Main.one : BigInteger.valueOf(-1);
    }

    static boolean isPrime(BigInteger num) {
        if (num.equals(Main.one)) {
            return false;
        }
        for (BigInteger j = Main.two; j.compareTo(num) < 0; j = j.add(Main.one)) {
            if (num.remainder(j).equals(Main.zero)) {
                return false;
            }
        }
        return true;
    }

    static BigInteger euler(BigInteger num) {
        BigInteger s = Main.zero;

        for (BigInteger i = Main.one; i.compareTo(num) <= 0; i = i.add(Main.one)) {
            if (num.remainder(i).equals(Main.zero))
                s = s.add(mobius(i).multiply(num).divide(i));
        }
        return s;
    }

    static BigInteger gcd(BigInteger a, BigInteger b) {
        if (a.equals(Main.zero)) return b;
        return gcd(b.remainder(a), a);
    }

    // 2) Розв’язання системи лінійних порівнянь за модулем (китайська теорема про лишки).
    static BigInteger extendedEuclid(BigInteger a, BigInteger b) {

        BigInteger b0 = b,
                a0 = a,
                x0 = Main.zero,
                x1 = Main.one;
        BigInteger temp, quotient;


        if (b0.equals(Main.one))
            return Main.zero;

        while (a0.compareTo(Main.one) > 0) {
            quotient = a0.divide(b0);

            temp = b0;
            b0 = a0.mod(b0);

            a0 = temp;

            temp = x0;
            x0 = x1.subtract(quotient.multiply(x0));
            x1 = temp;
        }

        if (x1.compareTo(Main.zero) < 0)
            x1 = x1.add(b);

        return x1;
    }

    static BigInteger chineseRemainder(BigInteger[] c, BigInteger[] m) {

        BigInteger[] M = new BigInteger[m.length];
        BigInteger[] N = new BigInteger[m.length];

        BigInteger mProduct = Main.one;
        for (BigInteger i = Main.zero; i.compareTo(BigInteger.valueOf(m.length)) < 0; i = i.add(Main.one)) {
            mProduct = mProduct.multiply(m[i.intValue()]);
        }

        BigInteger x0 = Main.zero;
        for (BigInteger i = Main.zero; i.compareTo(BigInteger.valueOf(m.length)) < 0; i = i.add(Main.one)) {
            M[i.intValue()] = mProduct.divide(m[i.intValue()]);
            N[i.intValue()] = extendedEuclid(M[i.intValue()], m[i.intValue()]);
            x0 = x0.add(M[i.intValue()].multiply(N[i.intValue()]).multiply(c[i.intValue()]));
        }

        return x0.mod(mProduct);
    }


    // 3) Обчислення символів Лежандра та Якобі.
    static BigInteger legendre(BigInteger a, BigInteger p) {
        BigInteger modded = a.mod(p);
        if (p.compareTo(Main.three) < 0 || modded.equals(Main.zero))
            return Main.zero;

        if (isQuadraticResidue(a, p)) {
            return Main.one;

        }
        return Main.minusOne;
    }

    static boolean isQuadraticResidue(BigInteger a, BigInteger p) {
        for (BigInteger i = Main.one; i.compareTo(p) < 0; i = i.add(Main.one)) {
            if (i.multiply(i).mod(p).equals(a)) {
                return true;
            }
        }
        return false;
    }

    static BigInteger jacobi(BigInteger a, BigInteger m) {
        BigInteger result = Main.one;
        for (BigInteger p = Main.three; p.compareTo(m) <= 0; p = p.add(Main.one)) {
            if (m.remainder(p).equals(Main.one)) {
                result = result.multiply(legendre(a, p));
            }
        }
        return result;
    }

    // 4) Один алгоритм факторизації довгих цілих чисел на вибір: ро-алгоритм Полларда або алгоритм квадратичного решета.
    static BigInteger pollardRhoFactorization(BigInteger n) {
        BigInteger x = Main.two,
                y = Main.two,
                d = Main.one,
                c = Main.one;

        while (d.equals(Main.one) || d.equals(n)) {
            x = g(x, n, c);
            y = g(g(y, n, c), n, c);
            d = gcd(x.subtract(y).abs(), n);
            if (d.equals(n)) {
                c = c.add(Main.one);
            }
        }
        return d;
    }

    private static BigInteger g(BigInteger val, BigInteger n, BigInteger c) {
        return val.multiply(val).add(c).mod(n);
    }

    // 5) Один алгоритм знаходження дискретного логарифма на вибір: ро-алгоритм Полларда або алгоритм «великий крок – малий крок».
    static BigInteger babyGiantStep(BigInteger a, BigInteger b, BigInteger n) {
        BigInteger m = n.sqrt();
        if (!m.multiply(m).equals(n)) m = m.add(Main.one);

        BigInteger extM = a.modPow(m.negate(), n);
        BigInteger[] table = new BigInteger[m.intValue()];


        for (BigInteger j = Main.zero; j.compareTo(m) < 0; j = j.add(Main.one)) {
            table[j.intValue()] = a.modPow(j, n);
        }

        BigInteger y = b;

        for (BigInteger i = Main.zero; i.compareTo(m) < 0; i = i.add(Main.one)) {
            for (BigInteger j = Main.zero; j.compareTo(m) < 0; j = j.add(Main.one)) {
                if (y.equals(table[j.intValue()]))
                    return i.multiply(m).add(j);
            }

            y = y.multiply(extM).mod(n);

        }

        return null;
    }

    // 6) Алгоритм Чіпполи знаходження дискретного квадратного кореня.
    static BigInteger[] cipolla(BigInteger n, BigInteger p) {
        if (!legendre(n, p).equals(Main.one)) {
            throw new IllegalArgumentException("n is not a square modulo p");
        }

        BigInteger a = Main.two;
        BigInteger omega = a.multiply(a).subtract(n).mod(p);

        while (!legendre(omega, p).equals(Main.minusOne)) {
            a = a.add(Main.one);
            omega = a.multiply(a).subtract(n).mod(p);
        }

        BigInteger finalOmega = omega;

        BiFunction<BigInteger[], BigInteger[], BigInteger[]> mul = (BigInteger[] aa, BigInteger[] bb) -> new BigInteger[]
                {
                        aa[0].multiply(bb[0]).add(aa[1].multiply(bb[1]).multiply(finalOmega)).mod(p),
                        aa[0].multiply(bb[1]).add(bb[0].multiply(aa[1])).mod(p)
                };

        BigInteger[] x1 = {Main.one, Main.zero};
        BigInteger[] x2 = {a, Main.one};

        BigInteger nn = p.add(Main.one).shiftRight(1).mod(p);
        while (nn.compareTo(Main.zero) > 0) {
            if (nn.and(Main.one).equals(Main.one)) {
                x1 = mul.apply(x1, x2);
            }
            x2 = mul.apply(x2, x2);
            nn = nn.shiftRight(1);
        }

        return new BigInteger[]{x1[0], p.subtract(x1[0])};
    }


    // 7) Один алгоритм перевірки чисел на простоту на вибір: алгоритм Соловея-Штрассена або алгоритм Міллера-Рабіна.
    static boolean millerRabin(BigInteger n, BigInteger k) {
        Function<BigInteger, Boolean> evenCheck = (BigInteger num) ->
                num.mod(Main.two).equals(Main.zero);

        if (evenCheck.apply(n))
            return false;
        BigInteger m = n.subtract(Main.one).divide(Main.two),
                t = Main.one;

        while (evenCheck.apply(m)) {
            m = m.divide(Main.two);
            t = t.add(Main.one);
        }

        BigInteger a, u;
        Random rnd = new Random();
        for (BigInteger i = Main.one; i.compareTo(k) <= 0; i = i.add(Main.one)) {
            int random = rnd.nextInt(n.intValue() - 3) + 2;
            a = BigInteger.valueOf(random);
            u = a.modPow(m, n);

            if (!u.equals(Main.one) && !u.equals(n.subtract(Main.one))) {
                for (BigInteger j = Main.one; j.compareTo(t) < 0 && !u.equals(n.subtract(Main.one)); j = j.add(Main.one))
                    u = u.modPow(Main.two, n);

                if (!u.equals(n.subtract(Main.one)))
                    return false;
            }
        }

        return true;
    }

    static void printResult(String function, Object result, Object... params) {
        System.out.println(function + "(" + Arrays.toString(params) + ")=" + result.toString());
    }
}
