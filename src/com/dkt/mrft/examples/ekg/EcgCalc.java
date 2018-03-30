package com.dkt.mrft.examples.ekg;

import java.io.PrintStream;

/*
 * EcgCalc.java
 *
 * See EcgLicense.txt for License terms.
 */
/**
 *
 * @author Mauricio Villarroel (m.villarroel@acm.og)
 */
public final class EcgCalc {

    /* C defines */
    private final double PI = 2.0 * Math.asin(1.0);
    private final int IA = 16807;
    private final long IM = 2147483647;
    private final double AM = (1.0 / IM);
    private final long IQ = 127773;
    private final int IR = 2836;
    private final int NTAB = 32;
    private final double NDIV = (1 + (IM - 1) / NTAB);
    private final double EPS = 1.2e-7;
    private final double RNMX = (1.0 - EPS);
    /**
     * ***************************************************************************
     * DEFINE PARAMETERS AS GLOBAL VARIABLES *
     * ***************************************************************************
     */
    //private String outfile ="ecgsyn.dat";
    // Order of extrema: [P Q R S T]
    private final double[] ti = new double[6];
    /* ti converted in radians             */
    private final double[] ai = new double[6];
    /* new calculated a                    */
    private final double[] bi = new double[6];
    /* new calculated b                    */

    private final int mstate = 3;
    /*  System state space dimension       */
    private final double xinitial = 1.0;
    /*  Initial x co-ordinate value        */
    private final double yinitial = 0.0;
    /*  Initial y co-ordinate value        */
    private final double zinitial = 0.04;
    /*  Initial z co-ordinate value        */
    private long rseed;
    private double h;
    private double[] rrpc;

    /*
     * Variables for static function ran1()
     */
    private long iy;
    private final long[] iv;

    /*
     * ECG Result Variables
     */
    /* Result Vectors*/
    private double[] ecgResultTime;
    private double[] ecgResultVoltage;
    private int[] ecgResultPeak;

    private int ecgResultNumRows;

    /* Object Variables */
    private final EcgParam paramOb;
    private final PrintStream ecgLog;

    /**
     * Creates a new instance of EcgCalc
     */
    public EcgCalc(EcgParam parameters) {
        paramOb = parameters;
        ecgLog = NullPrintStream.getInstance();

        /* variables for static function ranq() */
        iy = 0;
        iv = new long[NTAB];
    }

    public boolean calculateEcg() {
        boolean RetValue;

        ecgLog.println("Starting to calculate ECG....");

        RetValue = dorun();

        ecgLog.println("Finished calculating ECG table data.\n");

        return (RetValue);
    }

    public int getEcgResultNumRows() {
        return ecgResultNumRows;
    }

    public double getEcgResultTime(int index) {
        return ecgResultTime[index];
    }

    public double getEcgResultVoltage(int index) {
        return ecgResultVoltage[index];
    }

    public int getEcgResultPeak(int index) {
        return ecgResultPeak[index];
    }

    /*--------------------------------------------------------------------------*/
    /*    UNIFORM DEVIATES                                                      */
    /*--------------------------------------------------------------------------*/
    private double ran1() {
        final boolean flg = iy != 0;
        long k;
        double temp;

        if ((rseed <= 0) || !flg) {
            if (-(rseed) < 1) {
                rseed = 1;
            } else {
                rseed = -rseed;
            }

            for (int j = NTAB + 7; j >= 0; j--) {
                k = (rseed) / IQ;
                rseed = IA * (rseed - k * IQ) - IR * k;
                if (rseed < 0) {
                    rseed += IM;
                }
                if (j < NTAB) {
                    iv[j] = rseed;
                }
            }
            iy = iv[0];
        }

        k = (rseed) / IQ;
        rseed = IA * (rseed - k * IQ) - IR * k;
        if (rseed < 0) {
            rseed += IM;
        }

        int j = (int) (iy / NDIV);
        iy = iv[j];
        iv[j] = rseed;

        if ((temp = AM * iy) > RNMX) {
            return RNMX;
        } else {
            return temp;
        }
    }

    /*
     * FFT
     */
    private void ifft(double[] data, long nn, int isign) {
        final long n = nn << 1;
        long mmax;
        long m;
        long istep;
        long j = 1;
        double wtemp;
        double wr;
        double wpr;
        double wpi;
        double wi;
        double theta;
        double tempr;
        double tempi;
        double swap;

        for (long i = 1; i < n; i += 2) {
            if (j > i) {
                //SWAP(data[j],data[i]);
                swap = data[(int) j];
                data[(int) j] = data[(int) i];
                data[(int) i] = swap;
                //SWAP(data[j+1],data[i+1]);
                swap = data[(int) j + 1];
                data[(int) j + 1] = data[(int) i + 1];
                data[(int) i + 1] = swap;
            }
            m = n >> 1;
            while (m >= 2 && j > m) {
                j -= m;
                m >>= 1;
            }
            j += m;
        }
        mmax = 2;
        while (n > mmax) {
            istep = mmax << 1;
            theta = isign * (6.28318530717959 / mmax);
            wtemp = Math.sin(0.5 * theta);
            wpr = -2.0 * wtemp * wtemp;
            wpi = Math.sin(theta);
            wr = 1.0;
            wi = 0.0;
            for (m = 1; m < mmax; m += 2) {
                for (long i = m; i <= n; i += istep) {
                    j = i + mmax;
                    tempr = wr * data[(int) j] - wi * data[(int) j + 1];
                    tempi = wr * data[(int) j + 1] + wi * data[(int) j];
                    data[(int) j] = data[(int) i] - tempr;
                    data[(int) j + 1] = data[(int) i + 1] - tempi;
                    data[(int) i] += tempr;
                    data[(int) i + 1] += tempi;
                }
                wr = (wtemp = wr) * wpr - wi * wpi + wr;
                wi = wi * wpr + wtemp * wpi + wi;
            }
            mmax = istep;
        }
    }

    /*
     * STANDARD DEVIATION CALCULATOR
     */
    /* n-by-1 vector, calculate standard deviation */
    private double stdev(double[] x, int n) {
        final double mean;
        double add;
        double diff;
        double total;

        add = 0.0;
        for (int j = 1; j <= n; j++) {
            add += x[j];
        }

        mean = add / n;

        total = 0.0;
        for (int j = 1; j <= n; j++) {
            diff = x[j] - mean;
            total += diff * diff;
        }
        return (Math.sqrt(total / ((double) n - 1)));
    }

    /*
     * THE ANGULAR FREQUENCY
     */
    private double angfreq(double t) {
        int i = 1 + (int) Math.floor(t / h);
        return (2.0 * PI / rrpc[i]);
    }

    /*--------------------------------------------------------------------------*/
    /*    THE EXACT NONLINEAR DERIVATIVES                                       */
    /*--------------------------------------------------------------------------*/
    private void derivspqrst(double t0, double[] x, double[] dxdt) {
        final int k = 5;
        final double w0 = angfreq(t0);
        final double r0 = 1.0;
        final double x0 = 0.0;
        final double y0 = 0.0;
        final double a0 = 1.0 - Math.sqrt((x[1] - x0) * (x[1] - x0) 
                              + (x[2] - y0) * (x[2] - y0)) / r0;
        final double zbase = 0.005 * Math.sin(2.0 * PI * paramOb.getFHi() * t0);
        final double t = Math.atan2(x[2], x[1]);
        double dt;
        double dt2;
        
        dxdt[1] = a0 * (x[1] - x0) - w0 * (x[2] - y0);
        dxdt[2] = a0 * (x[2] - y0) + w0 * (x[1] - x0);
        dxdt[3] = 0.0;

        for (int i = 1; i <= k; i++) {
            dt = Math.IEEEremainder(t - ti[i], 2.0 * PI);
            dt2 = dt * dt;
            dxdt[3] += -ai[i] * dt * Math.exp(-0.5 * dt2 / (bi[i] * bi[i]));
        }
        
        dxdt[3] += -1.0 * (x[3] - zbase);
    }

    /*
     * RUNGA-KUTTA FOURTH ORDER INTEGRATION
     */
    private void rk4(double[] y, int n, double x, double h, double[] yout) {
        final double[] dydx = new double[n + 1];
        final double[] dym = new double[n + 1];
        final double[] dyt = new double[n + 1];
        final double[] yt = new double[n + 1];
        final double hh = h * 0.5;
        final double h6 = h / 6.0;
        final double xh = x + hh;

        derivspqrst(x, y, dydx);
        for (int i = 1; i <= n; i++) {
            yt[i] = y[i] + hh * dydx[i];
        }

        derivspqrst(xh, yt, dyt);
        for (int i = 1; i <= n; i++) {
            yt[i] = y[i] + hh * dyt[i];
        }

        derivspqrst(xh, yt, dym);
        for (int i = 1; i <= n; i++) {
            yt[i] = y[i] + h * dym[i];
            dym[i] += dyt[i];
        }

        derivspqrst(x + h, yt, dyt);
        for (int i = 1; i <= n; i++) {
            yout[i] = y[i] + h6 * (dydx[i] + dyt[i] + 2.0 * dym[i]);
        }
    }

    /*
     * GENERATE RR PROCESS
     */
    private void rrprocess(double[] rr, double flo, double fhi,
            double flostd, double fhistd, double lfhfratio,
            double hrmean, double hrstd, double sf, int n) {
        final double c1 = 2.0 * PI * flostd;
        final double c2 = 2.0 * PI * fhistd;
        final double w1 = 2.0 * PI * flo;
        final double w2 = 2.0 * PI * fhi;
        final double sig1 = lfhfratio;
        final double sig2 = 1.0;
        final double rrmean = 60.0 / hrmean;
        final double rrstd = 60.0 * hrstd / (hrmean * hrmean);
        final double xstd;
        final double ratio;
        final double df = sf / (double) n;
        final double[] w = new double[n + 1];
        final double[] Hw = new double[n + 1];
        final double[] Sw = new double[n + 1];
        final double[] ph0 = new double[(int) (n / 2 - 1 + 1)];
        final double[] ph = new double[n + 1];
        final double[] SwC = new double[(2 * n) + 1];
        
        for (int i = 1; i <= n; i++) {
            w[i] = (i - 1) * 2.0 * PI * df;
        }

        for (int i = 1; i <= n; i++) {
            //dw1 = w[i]-w1;
            //dw2 = w[i]-w2;
            Hw[i] = (sig1 * Math.exp(-0.5 * (Math.pow(w[i] - w1, 2) 
                  / Math.pow(c1, 2))) / Math.sqrt(2 * PI * c1 * c1))
                  + (sig2 * Math.exp(-0.5 * (Math.pow(w[i] - w2, 2) 
                  / Math.pow(c2, 2))) / Math.sqrt(2 * PI * c2 * c2));
        }

        for (int i = 1; i <= n / 2; i++) {
            Sw[i] = (sf / 2.0) * Math.sqrt(Hw[i]);
        }

        for (int i = n / 2 + 1; i <= n; i++) {
            Sw[i] = (sf / 2.0) * Math.sqrt(Hw[n - i + 1]);
        }

        /* randomise the phases */
        for (int i = 1; i <= n / 2 - 1; i++) {
            ph0[i] = 2.0 * PI * ran1();
        }

        ph[1] = 0.0;
        for (int i = 1; i <= n / 2 - 1; i++) {
            ph[i + 1] = ph0[i];
        }

        ph[n / 2 + 1] = 0.0;
        for (int i = 1; i <= n / 2 - 1; i++) {
            ph[n - i + 1] = -ph0[i];
        }

        /* make complex spectrum */
        for (int i = 1; i <= n; i++) {
            SwC[2 * i - 1] = Sw[i] * Math.cos(ph[i]);
        }

        for (int i = 1; i <= n; i++) {
            SwC[2 * i] = Sw[i] * Math.sin(ph[i]);
        }

        /* calculate inverse fft */
        ifft(SwC, n, -1);

        /* extract real part */
        for (int i = 1; i <= n; i++) {
            rr[i] = (1.0 / (double) n) * SwC[2 * i - 1];
        }

        xstd = stdev(rr, n);
        ratio = rrstd / xstd;

        for (int i = 1; i <= n; i++) {
            rr[i] *= ratio;
        }

        for (int i = 1; i <= n; i++) {
            rr[i] += rrmean;
        }

    }

    /*
     * DETECT PEAKS
     */
    private void detectpeaks(double[] ipeak, double[] x, double[] y, double[] z, int n) {
        int j1;
        int j2;
        int jmin;
        int jmax;
        int d;
        final double thetap1 = ti[1];
        final double thetap2 = ti[2];
        final double thetap3 = ti[3];
        final double thetap4 = ti[4];
        final double thetap5 = ti[5];
        double theta1;
        double theta2;
        double d1;
        double d2;
        double zmin;
        double zmax;

        for (int i = 1; i <= n; i++) {
            ipeak[i] = 0.0;
        }

        theta1 = Math.atan2(y[1], x[1]);

        for (int i = 1; i < n; i++) {
            theta2 = Math.atan2(y[i + 1], x[i + 1]);

            if ((theta1 <= thetap1) && (thetap1 <= theta2)) {
                d1 = thetap1 - theta1;
                d2 = theta2 - thetap1;
                if (d1 < d2) {
                    ipeak[i] = 1.0;
                } else {
                    ipeak[i + 1] = 1.0;
                }
            } else if ((theta1 <= thetap2) && (thetap2 <= theta2)) {
                d1 = thetap2 - theta1;
                d2 = theta2 - thetap2;
                if (d1 < d2) {
                    ipeak[i] = 2.0;
                } else {
                    ipeak[i + 1] = 2.0;
                }
            } else if ((theta1 <= thetap3) && (thetap3 <= theta2)) {
                d1 = thetap3 - theta1;
                d2 = theta2 - thetap3;
                if (d1 < d2) {
                    ipeak[i] = 3.0;
                } else {
                    ipeak[i + 1] = 3.0;
                }
            } else if ((theta1 <= thetap4) && (thetap4 <= theta2)) {
                d1 = thetap4 - theta1;
                d2 = theta2 - thetap4;
                if (d1 < d2) {
                    ipeak[i] = 4.0;
                } else {
                    ipeak[i + 1] = 4.0;
                }
            } else if ((theta1 <= thetap5) && (thetap5 <= theta2)) {
                d1 = thetap5 - theta1;
                d2 = theta2 - thetap5;
                if (d1 < d2) {
                    ipeak[i] = 5.0;
                } else {
                    ipeak[i + 1] = 5.0;
                }
            }
            theta1 = theta2;
        }

        /* correct the peaks */
        d = (int) Math.ceil(paramOb.getSfEcg() / 64);
        for (int i = 1; i <= n; i++) {
            if (ipeak[i] == 1 || ipeak[i] == 3 || ipeak[i] == 5) {

                j1 = (1 > (i - d) ? 1 : (i - d)); //MAX(1,i-d);
                j2 = (n < (i + d) ? n : (i + d)); //MIN(n,i+d);
                jmax = j1;
                zmax = z[j1];
                for (int j = j1 + 1; j <= j2; j++) {
                    if (z[j] > zmax) {
                        jmax = j;
                        zmax = z[j];
                    }
                }
                if (jmax != i) {
                    ipeak[jmax] = ipeak[i];
                    ipeak[i] = 0;
                }
            } else if (ipeak[i] == 2 || ipeak[i] == 4) {
                j1 = (1 > (i - d) ? 1 : (i - d));//MAX(1,i-d);
                j2 = (n < (i + d) ? n : (i + d)); //MIN(n,i+d);
                jmin = j1;
                zmin = z[j1];
                for (int j = j1 + 1; j <= j2; j++) {
                    if (z[j] < zmin) {
                        jmin = j;
                        zmin = z[j];
                    }
                }
                if (jmin != i) {
                    ipeak[jmin] = ipeak[i];
                    ipeak[i] = 0;
                }
            }
        }
    }

    /*
     * DORUN PART OF PROGRAM
     */
    private boolean dorun() {
        boolean RetValue = true;
        
        final int Nrr;
        final int Nt;
        final int Nts;
        final int q;
        final double[] x;
        final double tstep;
        double tecg;
        final double rrmean;
        final double hrfact;
        final double hrfact2;
        final double[] xt;
        final double[] yt;
        final double[] zt;
        final double[] xts;
        final double[] yts;
        final double[] zts;
        double timev;
        double zmin;
        double zmax;
        final double zrange;
        final double[] ipeak;

        // perform some checks on input values
        q = (int) Math.rint(paramOb.getSf() / paramOb.getSfEcg());

        /* convert angles from degrees to radians and copy a vector to ai*/
        for (int i = 1; i <= 5; i++) {
            ti[i] = paramOb.getTheta(i - 1) * PI / 180.0;
            ai[i] = paramOb.getA(i - 1);
        }

        /* adjust extrema parameters for mean heart rate */
        hrfact = Math.sqrt(paramOb.getHrMean() / 60);
        hrfact2 = Math.sqrt(hrfact);

        for (int i = 1; i <= 5; i++) {
            bi[i] = paramOb.getB(i - 1) * hrfact;
        }

        ti[1] *= hrfact2;
        ti[2] *= hrfact;
        ti[3] *= 1.0;
        ti[4] *= hrfact;
        ti[5] *= 1.0;

        /* declare state vector */
        //x=dvector(1,mstate);
        x = new double[4];

        ecgLog.println("Approximate number of heart beats: " + paramOb.getN());
        ecgLog.println("ECG sampling frequency: " + paramOb.getSfEcg() + " Hertz");
        ecgLog.println("Internal sampling frequency: " + paramOb.getSf() + " Hertz");
        ecgLog.println("Amplitude of additive uniformly distributed noise: " + paramOb.getANoise() + " mV");
        ecgLog.println("Heart rate mean: " + paramOb.getHrMean() + " beats per minute");
        ecgLog.println("Heart rate std: " + paramOb.getHrStd() + " beats per minute");
        ecgLog.println("Low frequency: " + paramOb.getFLo() + " Hertz");
        ecgLog.println("High frequency std: " + paramOb.getFHiStd() + " Hertz");
        ecgLog.println("Low frequency std: " + paramOb.getFLoStd() + " Hertz");
        ecgLog.println("High frequency: " + paramOb.getFHi() + " Hertz");
        ecgLog.println("LF/HF ratio: " + paramOb.getLfHfRatio());
        ecgLog.println("time step milliseconds: " + paramOb.getEcgAnimateInterval() + "\n");
        ecgLog.println("Order of Extrema:");
        ecgLog.println("      theta(radians)");
        ecgLog.println("P: [" + ti[1] + "\t]");
        ecgLog.println("Q: [" + ti[2] + "\t]");
        ecgLog.println("R: [" + ti[3] + "\t]");
        ecgLog.println("S: [" + ti[4] + "\t]");
        ecgLog.println("T: [" + ti[5] + "\t]\n");
        ecgLog.println("      a(calculated)");
        ecgLog.println("P: [" + ai[1] + "\t]");
        ecgLog.println("Q: [" + ai[2] + "\t]");
        ecgLog.println("R: [" + ai[3] + "\t]");
        ecgLog.println("S: [" + ai[4] + "\t]");
        ecgLog.println("T: [" + ai[5] + "\t]\n");
        ecgLog.println("      b(calculated)");
        ecgLog.println("P: [" + bi[1] + "\t]");
        ecgLog.println("Q: [" + bi[2] + "\t]");
        ecgLog.println("R: [" + bi[3] + "\t]");
        ecgLog.println("S: [" + bi[4] + "\t]");
        ecgLog.println("T: [" + bi[5] + "\t]\n");

        /* Initialise the vector */
        x[1] = xinitial;
        x[2] = yinitial;
        x[3] = zinitial;

        /* initialise seed */
        rseed = -paramOb.getSeed();

        /* calculate time scales */
        h = 1.0 / (double) paramOb.getSf();
        tstep = 1.0 / (double) paramOb.getSfEcg();

        /* calculate length of RR time series */
        rrmean = (60.0 / paramOb.getHrMean());
        Nrr = (int) Math.pow(2.0, Math.ceil(Math.log(paramOb.getN() * rrmean * paramOb.getSf()) / Math.log(2.0)));

        ecgLog.println("Using " + Nrr + " = 2^ " + (int) (Math.log(1.0 * Nrr) / Math.log(2.0)) + " samples for calculating RR intervals");

        /* create rrprocess with required spectrum */
        final double[] rr = new double[Nrr + 1];
        rrprocess(rr, paramOb.getFLo(), paramOb.getFHi(), paramOb.getFLoStd(),
                paramOb.getFHiStd(), paramOb.getLfHfRatio(), paramOb.getHrMean(),
                paramOb.getHrStd(), paramOb.getSf(), Nrr);

        /* create piecewise constant rr */
        rrpc = new double[(2 * Nrr) + 1];
        tecg = 0.0;
        int i = 1;
        int j = 1;
        while (i <= Nrr) {
            tecg += rr[j];
            j = (int) Math.rint(tecg / h);
            for (int k = i; k <= j; k++) {
                rrpc[k] = rr[i];
            }
            i = j + 1;
        }
        Nt = j;

        /* integrate dynamical system using fourth order Runge-Kutta*/
        xt = new double[Nt + 1];
        yt = new double[Nt + 1];
        zt = new double[Nt + 1];
        timev = 0.0;
        for (i = 1; i <= Nt; i++) {
            xt[i] = x[1];
            yt[i] = x[2];
            zt[i] = x[3];
            rk4(x, mstate, timev, h, x);
            timev += h;
        }

        /* downsample to ECG sampling frequency */
        xts = new double[Nt + 1];
        yts = new double[Nt + 1];
        zts = new double[Nt + 1];

        j = 0;
        for (i = 1; i <= Nt; i += q) {
            j++;
            xts[j] = xt[i];
            yts[j] = yt[i];
            zts[j] = zt[i];
        }
        Nts = j;

        /* do peak detection using angle */
        ipeak = new double[Nts + 1];
        detectpeaks(ipeak, xts, yts, zts, Nts);

        /* scale signal to lie between -0.4 and 1.2 mV */
        zmin = zts[1];
        zmax = zts[1];
        for (i = 2; i <= Nts; i++) {
            if (zts[i] < zmin) {
                zmin = zts[i];
            } else if (zts[i] > zmax) {
                zmax = zts[i];
            }
        }
        zrange = zmax - zmin;
        for (i = 1; i <= Nts; i++) {
            zts[i] = (zts[i] - zmin) * (1.6) / zrange - 0.4;
        }

        /* include additive uniformly distributed measurement noise */
        for (i = 1; i <= Nts; i++) {
            zts[i] += paramOb.getANoise() * (2.0 * ran1() - 1.0);
        }

        /*
         * insert into the ECG data table
         */
        ecgLog.println("Generating result matrix...");

        ecgResultNumRows = Nts;

        ecgResultTime = new double[ecgResultNumRows];
        ecgResultVoltage = new double[ecgResultNumRows];
        ecgResultPeak = new int[ecgResultNumRows];

        for (i = 1; i <= Nts; i++) {
            ecgResultTime[i - 1] = (i - 1) * tstep;
            ecgResultVoltage[i - 1] = zts[i];
            ecgResultPeak[i - 1] = (int) ipeak[i];
        }

        ecgLog.println("Finished generating result matrix.");

        return (RetValue);
    }
}
