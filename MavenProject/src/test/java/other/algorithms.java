package other;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

public class algorithms {

    @Test
    public void solution() {
        /**
         * 从谷歌code获取的获取输入的代码
         */
        Scanner in = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int t = in.nextInt();  // Scanner has functions to read ints, longs, strings, chars, etc.
        for (int i = 1; i <= t; ++i) {
            int n = in.nextInt();
            int m = in.nextInt();
            System.out.println("Case #" + i + ": " + beautiful(m));
        }
    }

    public int beautiful(int m) {
        for (int radix = 2; radix < m; radix++) {
            if (isBeautiful(m, radix)) {
                return radix;
            }
        }
        return m - 1;
    }

    public boolean isBeautiful(int m, int radix) {
        while (m > 0) {
            if (m % radix != 1) {
                return false;
            }
            m /= radix;
        }
        return true;
    }
}
