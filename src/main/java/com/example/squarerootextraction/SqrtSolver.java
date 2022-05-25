package com.example.squarerootextraction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SqrtSolver {

    public static BigDecimal sqrt(BigDecimal value, int precision){
        if (value.equals(BigDecimal.valueOf(0))) return BigDecimal.valueOf(0);
        BigDecimal x = value.divide(new BigDecimal(2.0), 10, RoundingMode.HALF_UP);
        int iterations = 0;
        BigDecimal eps = BigDecimal.valueOf(Math.pow(0.1, precision));
        while ((x.multiply(x).subtract(value)).abs().compareTo(eps) > 0 && iterations != 1000) {
            x = BigDecimal.valueOf(0.5).multiply(x.add(value.divide(x, 10, RoundingMode.HALF_UP)));
            iterations++;
        }
        return x;
    }

    public static Complex sqrt(Complex value, int precision){
        BigDecimal a = value.getReal(), b = value.getImaginary();
        BigDecimal real = sqrt(((sqrt(a.multiply(a).add(b.multiply(b)),precision).add(a)).divide(BigDecimal.valueOf(2))), precision);
        BigDecimal imaginary = sqrt(((sqrt(a.multiply(a).add(b.multiply(b)),precision).subtract(a)).divide(BigDecimal.valueOf(2))), precision);
        return new Complex(real, signum(b).multiply(imaginary));
    }


    private static BigDecimal signum(BigDecimal value){
        if (value.compareTo(BigDecimal.valueOf(0)) > 0) return BigDecimal.valueOf(1);
        else return BigDecimal.valueOf(-1);
    }
}
