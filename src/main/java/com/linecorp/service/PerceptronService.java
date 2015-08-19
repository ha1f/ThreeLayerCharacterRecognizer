package com.linecorp.service;

import com.linecorp.Util;
import com.linecorp.domain.Perceptron;
import com.linecorp.repository.PerceptronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Tomoki Yamaguchi
 */
@Service
public class PerceptronService {
    //@Autowired
    //PerceptronRepository firstPerceptronRepository;

    private final int middleNum = 40;
    private final int inputNum = 81 + 1;
    private final int outNum = 10;

    PerceptronRepository middlePerceptronRepository;
    PerceptronRepository lastPerceptronRepository;

    public Double getDiffSum(List<List<List<Double>>> allList) {
        Double sum = 0.0;
        for (List<List<Double>> list:allList ) {
            sum = sum + getDiff(list.get(0), list.get(1));
        }
        return sum;
    }

    public Double getDiff(List<Double> teacherIn, List<Double> teacherOut) {
        Double diff = 0.0;

        List<Double> middleOut = middlePerceptronRepository.getOutput(teacherIn);
        List<Double> lastOut = lastPerceptronRepository.getOutput(middleOut);

        int index = 0;
        for (Double cell: lastOut) {
            diff = diff + (cell - teacherOut.get(index)) * (cell - teacherOut.get(index));
            index++;
        }

        return diff;
    }

    public int getOutput(List<Double> input) {
        //答えがteacher.get(1)
        List<Double> middleOut = middlePerceptronRepository.getOutput(input);
        List<Double> lastOut = lastPerceptronRepository.getOutput(middleOut);

        Double max = 0.0;
        int index = 0;
        int maxindex = 0;
        for (Double cell: lastOut) {
            if (cell > max) {
                maxindex = index;
                max = cell;
            }
            index++;
        }
        //System.out.println(maxindex);

        return maxindex;
    }

    public void showAllData() {
        Util.println("middle");
        middlePerceptronRepository.printwList();
        Util.println("last");
        lastPerceptronRepository.printwList();
    }

    public void create() {
        middlePerceptronRepository = new PerceptronRepository();
        lastPerceptronRepository = new PerceptronRepository();

        middlePerceptronRepository.create(middleNum);
        lastPerceptronRepository.create(outNum);

        //firstPerceptronRepository.initWithRnd(inputNum);
        middlePerceptronRepository.initWithRnd(inputNum);
        lastPerceptronRepository.initWithRnd(middleNum);

        //firstPerceptronRepository.lockOutput(0, -1.0);
        middlePerceptronRepository.lockOutput(0, -1.0);
    }

    public void learn(List<List<Double>> teacher) {
        //答えがteacher.get(1)
        List<Double> middleOut = middlePerceptronRepository.getOutput(teacher.get(0));
        middlePerceptronRepository.learn2(teacher.get(0), lastPerceptronRepository.learn(middleOut, teacher.get(1)));
    }

}
