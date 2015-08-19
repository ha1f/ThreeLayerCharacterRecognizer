package com.linecorp.domain;

import com.linecorp.Util;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by ST20400 on 2015/08/09.
 * @author Tomoki Yamaguchi
 */

@Data
public class Perceptron {
    private List<Double> wList = new ArrayList<>();
    private boolean lockFlag = false;
    private Double lockValue = 0.0;

    public Double getOutput(List<Double> inputData) {
        if (lockFlag) {
            return lockValue;
        }

        double tmp = 0.0;
        int index = 0;
        for (Double wi: wList) {
            tmp = tmp + wi * inputData.get(index);
            index++;
        }

        return Util.sigmoid(tmp);
    }
}
