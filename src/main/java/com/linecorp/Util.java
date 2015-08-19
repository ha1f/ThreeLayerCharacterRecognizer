package com.linecorp;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Created by ST20400 on 2015/08/11.
 *
 */
@Component
public class Util {
    static final boolean debugFlag = true;

    static public int getMaxIndex(List<Double> list) {
        Double max = list.get(0);
        int index = 0;
        int maxindex = 0;
        for (Double cell: list) {
            if (cell > max) {
                maxindex = index;
                max = cell;
            }
            index++;
        }
        return maxindex;
    }

    static public Double sigmoid(Double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    //sigmoidの微分
    static public Double sigmoid2(Double x) {
        return sigmoid(sigmoid(x));
    }

    static public void println(Object any) {
        if (debugFlag) {
           System.out.println(any);
        }
    }

    static public void print(Object any) {
        if (debugFlag) {
            System.out.print(any);
        }
    }

}
