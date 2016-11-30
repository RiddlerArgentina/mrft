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
import net.objecthunter.exp4j.function.Function;

/**
 *
 * @author Federico Vera {@literal <fedevera at unc.edu.ar>}
 */
public class FunctionsMisc {
    private static final int INDEX_SINC   = 0;
    private static final int INDEX_PI  = 1;
    private static final int INDEX_E = 2;
    private static final int INDEX_R2D = 3;
    private static final int INDEX_D2R = 4;
    private static final int INDEX_F2C = 5;
    private static final int INDEX_C2F = 6;

    private static final Function[] builtinFunctions = new Function[7];

    static {
        builtinFunctions[INDEX_SINC] = new Function("sinc") {
            @Override
            public double apply(double... args) {
                final double x = args[0];
                return x == 0 ? 1 : Math.sin(x) / x;
            }
        };
        builtinFunctions[INDEX_PI] = new Function("pi", 0) {
            @Override
            public double apply(double... args) {
                return Math.PI;
            }
        };
        builtinFunctions[INDEX_E] = new Function("e", 0) {
            @Override
            public double apply(double... args) {
                return Math.E;
            }
        };
        builtinFunctions[INDEX_R2D] = new Function("r2d", 1) {
            @Override
            public double apply(double... args) {
                final double r = args[0];
                return r * 180. / Math.PI;
            }
        };
        builtinFunctions[INDEX_D2R] = new Function("d2r", 1) {
            @Override
            public double apply(double... args) {
                final double d = args[0];
                return d * Math.PI / 180;
            }
        };
        builtinFunctions[INDEX_F2C] = new Function("f2c", 1) {
            @Override
            public double apply(double... args) {
                final double f = args[0];
                return (f - 32) / 1.8;
            }
        };
        builtinFunctions[INDEX_C2F] = new Function("c2f", 1) {
            @Override
            public double apply(double... args) {
                final double c = args[0];
                return 1.8 * c + 32;
            }
        };
    }
    
    public static Function[] getFunctions() {
        return Arrays.copyOf(builtinFunctions, builtinFunctions.length);
    }

    public static Function getBuiltinFunctions(final String name) {
        switch(name) {
            case "sinc":
                return builtinFunctions[INDEX_SINC];
            case "pi":
                return builtinFunctions[INDEX_PI];
            case "e":
                return builtinFunctions[INDEX_E];
            case "r2d":
                return builtinFunctions[INDEX_R2D];
            case "d2r":
                return builtinFunctions[INDEX_D2R];
            case "f2c":
                return builtinFunctions[INDEX_F2C];
            case "c2f":
                return builtinFunctions[INDEX_C2F];
            default:
                return null;
        }
    }
}
