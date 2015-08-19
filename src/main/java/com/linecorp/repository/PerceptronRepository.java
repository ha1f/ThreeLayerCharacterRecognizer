package com.linecorp.repository;

import com.linecorp.Util;
import com.linecorp.domain.Perceptron;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by ST20400 on 2015/08/09.
 *
 */
@Repository
public class PerceptronRepository {
    private List<Perceptron> perceptronList = new ArrayList<>();

    //学習係数
    private final double lcoef = 0.01;

    public void lockOutput(int index, Double value) {
        perceptronList.get(index).setLockFlag(true);
        perceptronList.get(index).setLockValue(value);
    }

    public void create(int num) {
        perceptronList = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            perceptronList.add(new Perceptron());
        }
    }

    public List<Double> learn(List<Double> teacherIn, List<Double> teacherOut) {
        List<Double> sigmaK = new ArrayList<>();
        List<Double> sigmaI = new ArrayList<>();

        int index = 0;
        for (Perceptron perceptron: perceptronList) {
            Double sum = perceptron.getOutput(teacherIn);
            sigmaI.add((teacherOut.get(index) - perceptron.getOutput(teacherIn)) * Util.sigmoid2(sum));

            int index2 = 0;
            for (Double wi: perceptron.getWList()) {
                Double diff = lcoef * sigmaI.get(index) * teacherIn.get(index2);
                perceptron.getWList().set(index2, wi + diff);
                index2++;
            }
            index++;
        }

        //とりまリストを生成、ここから変更していく
        perceptronList.get(0).getWList().stream().forEach(wi -> sigmaK.add(wi * sigmaI.get(0)));

        for (int i = 1; i < 10; i++) {
            int index2 = 0;
            for (Double wi: perceptronList.get(i).getWList()) {
                sigmaK.set(index2, sigmaK.get(index2) + sigmaI.get(i) * wi);
                index2++;
            }
        }

        return sigmaK; //正確にはsigmaK/sigmoid2(sum2)
    }

    public void learn2(List<Double> teacherIn, List<Double> sigmaK) {
        int index = 0;
        for (Perceptron perceptron: perceptronList) {
            int index2 = 0;
            Double sum = 0.0;
            for (Double wi: perceptron.getWList()) {
                sum = sum + wi * teacherIn.get(index2);
                index2++;
            }

            index2 = 0;
            for (Double wi: perceptron.getWList()) {
                Double diff = lcoef * Util.sigmoid2(sum) * teacherIn.get(index2) * sigmaK.get(index);
                perceptron.getWList().set(index2, wi + diff);
                index2++;
            }
            index++;
        }
    }

    //全パーセプトロンに入力を流し込んで、出力のListを得る
    public List<Double> getOutput(List<Double> inputData) {
        int index = 0;
        List<Double> outputList = new ArrayList<>();
        for (Perceptron perceptron: perceptronList) {
            outputList.add(perceptron.getOutput(inputData));
            index++;
        }
        return outputList;
    }

    //全パーセプトロンの初期化を呼び出す
    //numは入力の数
    public void initWithRnd(int num) {
        for (Perceptron perceptron : perceptronList) {
            Random random = new Random();

            List<Double> wList = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                wList.add((random.nextDouble() / 2 + 0.1) / 100.0);
            }
            perceptron.setWList(wList);
        }
    }

    //全パーセプトロンのwListを取得してprint
    public void printwList() {
        perceptronList.stream().forEach(perceptron -> Util.println(perceptron.getWList()));

    }
}