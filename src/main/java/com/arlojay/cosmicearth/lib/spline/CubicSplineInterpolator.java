package com.arlojay.cosmicearth.lib.spline;

public class CubicSplineInterpolator extends SplineInterpolator<CubicSplineInterpolator> {
    private final double[] h;  // Differences between x values
    private final double[] a;  // a coefficients (same as y values)
    private final double[] b;  // b coefficients
    private final double[] c;  // c coefficients
    private final double[] d;  // d coefficients

    public CubicSplineInterpolator(SplinePoint[] points) {
        super(points);
        int n = points.length - 1;
        h = new double[n];
        a = new double[n + 1];
        b = new double[n];
        c = new double[n + 1];
        d = new double[n];

        for (int i = 0; i <= n; i++) {
            a[i] = points[i].y;
        }

        for (int i = 0; i < n; i++) {
            h[i] = points[i + 1].x - points[i].x;
        }

        calculateCoefficients(n);
    }

    private void calculateCoefficients(int n) {
        double[] alpha = new double[n];
        double[] l = new double[n + 1];
        double[] mu = new double[n + 1];
        double[] z = new double[n + 1];

        for (int i = 1; i < n; i++) {
            alpha[i] = (3.0 / h[i]) * (a[i + 1] - a[i]) - (3.0 / h[i - 1]) * (a[i] - a[i - 1]);
        }

        l[0] = 1.0;
        mu[0] = z[0] = 0.0;

        for (int i = 1; i < n; i++) {
            l[i] = 2.0 * (points[i + 1].x - points[i - 1].x) - h[i - 1] * mu[i - 1];
            mu[i] = h[i] / l[i];
            z[i] = (alpha[i] - h[i - 1] * z[i - 1]) / l[i];
        }

        l[n] = 1.0;
        z[n] = c[n] = 0.0;

        for (int j = n - 1; j >= 0; j--) {
            c[j] = z[j] - mu[j] * c[j + 1];
            b[j] = (a[j + 1] - a[j]) / h[j] - h[j] * (c[j + 1] + 2.0 * c[j]) / 3.0;
            d[j] = (c[j + 1] - c[j]) / (3.0 * h[j]);
        }
    }

    @Override
    public double interpolate(double x) {
        int n = points.length - 1;
        int i = findSegment(x, n);

        double dx = x - points[i].x;
        return a[i] + b[i] * dx + c[i] * dx * dx + d[i] * dx * dx * dx;
    }

    private int findSegment(double x, int n) {
        if (x <= points[0].x) {
            return 0;
        }
        if (x >= points[n].x) {
            return n - 1;
        }

        int low = 0;
        int high = n;
        while (low <= high) {
            int mid = (low + high) / 2;
            if (x < points[mid].x) {
                high = mid - 1;
            } else if (x > points[mid + 1].x) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return low;
    }

    @Override
    protected CubicSplineInterpolator makeNew(SplinePoint[] points) {
        return new CubicSplineInterpolator(points);
    }
}