/*
 * MIT License
 *
 * Copyright (c) 2016 Federico Vera <https://github.com/dktcoding>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.dkt.mrft.funcs;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import net.objecthunter.exp4j.function.Function;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class FunctionsRandom {
    private static final int INDEX_RAND   = 0;
    private static final int INDEX_RAND2  = 1;
    private static final int INDEX_GAUSS  = 2;
    private static final int INDEX_GAUSS2 = 3;

    private static final Function[] builtinFunctions = new Function[4];

    static {
        builtinFunctions[INDEX_RAND] = new Function("rand", 0) {
            @Override
            public double apply(double... args) {
                return ThreadLocalRandom.current().nextDouble();
            }
        };
        builtinFunctions[INDEX_RAND2] = new Function("rand2", 2) {
            @Override
            public double apply(double... args) {
                final double a = args[0];
                final double b = args[1];
                return ThreadLocalRandom.current().nextDouble() * (b - a) + a;
            }
        };
        builtinFunctions[INDEX_GAUSS] = new Function("gaussian", 0) {
            @Override
            public double apply(double... args) {
                return ThreadLocalRandom.current().nextGaussian();
            }
        };
        builtinFunctions[INDEX_GAUSS2] = new Function("gaussian2", 2) {
            @Override
            public double apply(double... args) {
                final double a = args[0];
                final double b = args[1];
                return ThreadLocalRandom.current().nextGaussian() * b + a;
            }
        };
    }
    
    public static Function[] getFunctions() {
        return Arrays.copyOf(builtinFunctions, builtinFunctions.length);
    }

    public static Function getBuiltinFunctions(final String name) {
        switch(name) {
            case "rand":
                return builtinFunctions[INDEX_RAND];
            case "rand2":
                return builtinFunctions[INDEX_RAND2];
            case "gaussian":
                return builtinFunctions[INDEX_GAUSS];
            case "gaussian2":
                return builtinFunctions[INDEX_GAUSS2];
            default:
                return null;
        }
    }
}
