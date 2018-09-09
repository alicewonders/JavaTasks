import java.util.Scanner;
import java.util.TreeMap;

public class Main {
    private static Double[] paramsD = null;
    private static Double[] params = null;
    private static Double[] rootsD = null;
    private static final Double E = 0.000001;
    private static final Integer delta = 1;
    private static final Double INFINITY = 99999999.0;
    private static final Integer SCALE = 100;

    private static Double myDoubleRound(Double value, Integer scale) {
        value = value * scale;
        int i = (int) Math.round(value);
        return (double)i / scale;
    }

    private static Double countDiscrOfDerivative() {
        Double derivativeDiscr;
        paramsD = new Double[4];
        paramsD[3] = 3.0;
        for (int i = 2; i > 0; i--) {
            paramsD[i] = i * params[i];
        }
        derivativeDiscr = paramsD[2] * paramsD[2] - 4 * paramsD[3] * paramsD[1];
        return derivativeDiscr;
    }

    private static void findingRootsOfDiscr() {
        rootsD = new Double[2];
        Double x1 = (-paramsD[2] + Math.sqrt(countDiscrOfDerivative())) / (2 * paramsD[3]);
        Double x2 = (-paramsD[2] - Math.sqrt(countDiscrOfDerivative())) / (2 * paramsD[3]);
        if (x1 < x2) {
            rootsD[0] = x1;
            rootsD[1] = x2;
        }
        else {
            rootsD[0] = x2;
            rootsD[1] = x1;
        }
    }

    private static Double countFunction(Integer value) {
        Double result;
        result = Math.pow(value, 3) + params[2] * Math.pow(value, 2) + params[1] * value + params[0];
        return result;
    }

    private static Double countFunction(Double value) {
        Double result;
        result = Math.pow(value, 3) + params[2] * Math.pow(value, 2) + params[1] * value + params[0];
        return result;
    }

    private static String signOfResult(Double result) {
        if (result < 0) {
            return "-";
        } else {
            return "+";
        }
    }

    private static Double[] findingSegment(Double c) {
        Double[] borders = new Double[2];
        TreeMap<Double, String> signForValue = new TreeMap<>();
        signForValue.put(0.0, signOfResult(countFunction(0)));
        int step = 0;
        if (c < 0) {
            step = step + delta;
            signForValue.put((double)step, signOfResult(countFunction(step)));
            while (signForValue.get(step - 1.0).equals(signForValue.get((double)step))) {
                step = step + delta;
                signForValue.put((double)step, signOfResult(countFunction(step)));
            }
            borders[0] = (double)(step - 1);
            borders[1] = (double)step;
        }
        else {
            step = step - delta;
            signForValue.put((double)step, signOfResult(countFunction(step)));
            while (signForValue.get((double)step).equals(signForValue.get((double)step + 1))) {
                step = step - delta;
                signForValue.put((double)step, signOfResult(countFunction(step)));
            }
            borders[0] = (double)step;
            borders[1] = (double)(step + 1);
        }
        return borders;
    }

    private static Double findingRootOfTheEquation (Double[] ab) {
        Double x, xn;
        Double a = ab[0], b = ab[1];
        Integer n = 1;
        xn = (ab[0] + ab[1]) / 2;
        while (Math.abs(xn) > E) {
            xn = (a + b) / 2;
            if (countFunction(xn) * countFunction(a) < 0) {
                b = xn;
            }
            else {
                a = xn;
                xn = countFunction(a);
            }
            n++;
        }
        System.out.print("Amount of steps: ");
        System.out.println(n);
        return myDoubleRound((a + b) / 2, SCALE);
    }

    public static void main(String[] args) {
        System.out.println("Please, enter a, b and c parameters in x^3 + ax^2 + bx + c:");
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String[] p = s.split(" ");
        params = new Double[3];
        Double x;
        int j = 0;
        for (int i = p.length - 1; i >= 0; i--) {
            params[i] = Double.parseDouble(p[j]);
            j++;
        }

        Double derivativeDiscr = countDiscrOfDerivative();
        if (derivativeDiscr  <= 0) {
            Double[] segment = findingSegment(params[0]);
            x = findingRootOfTheEquation(segment);
            System.out.println(x);
        }
        else {
            findingRootsOfDiscr();
            if (countFunction(rootsD[0]) > 0 && countFunction(rootsD[1]) > 0 ) {
                Double[] segment = {-INFINITY, rootsD[0]};
                x = findingRootOfTheEquation(segment);
                System.out.print("x1 = ");
                System.out.println(x);
            }
            if (countFunction(rootsD[0]) < 0 && countFunction(rootsD[1]) < 0) {
                Double[] segment = {rootsD[1], INFINITY};
                x = findingRootOfTheEquation(segment);
                System.out.print("x1 = ");
                System.out.println(x);
            }
            if (countFunction(rootsD[1]) == 0 && Math.abs(countFunction(rootsD[1])) < E) {
                Double[] segment = {-INFINITY, rootsD[0]};
                x = findingRootOfTheEquation(segment);
                System.out.print("x1,2 = ");
                System.out.print(rootsD[1] + ";");
                System.out.println("multiplicity root");
                System.out.println("x3 = " + x);
            }
            if (countFunction(rootsD[0]) == 0) {
                Double[] segment = {rootsD[1], INFINITY};
                x = findingRootOfTheEquation(segment);
                System.out.print("x1,2 = ");
                System.out.print(rootsD[0] + ";");
                System.out.println("multiplicity root");
                System.out.println("x3 = " + x);
            }
            if (countFunction(rootsD[0]) > 0 && countFunction(rootsD[1]) < 0) {
                Double[] segment = {-INFINITY, rootsD[0]};
                x = findingRootOfTheEquation(segment);
                System.out.println("x1 = " + x);
                segment[0] = rootsD[0];
                segment[1] = rootsD[1];
                x = findingRootOfTheEquation(segment);
                System.out.println("x2 = " + x);
                segment[0] = rootsD[1];
                segment[1] = INFINITY;
                x = findingRootOfTheEquation(segment);
                System.out.println("x3 = " + x);
            }
        }
    }
}
