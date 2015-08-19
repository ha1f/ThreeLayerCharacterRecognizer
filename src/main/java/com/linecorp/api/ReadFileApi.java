package com.linecorp.api;

import com.linecorp.Util;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ST20400 on 2015/08/10.
 *
 */
@Component
public class ReadFileApi {
    private final List<List<List<Double>>> teacherList = new ArrayList<>();

    public List<List<List<Double>>> getData(String filepath) {
        try {
            File file = new File(filepath);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String line;

            List<List<Double>> tmp = new ArrayList<>();
            List<Double> tmpIn = new ArrayList<>();
            List<Double> tmpOut = new ArrayList<>();
            tmpIn.add(-1.0);

            //tmpInの1個目は絶対に-1、その続きから入れていく

            //System.out.println("Read start.");
            while ((line = bufferedReader.readLine()) != null) {
                if (line.equals("")) {
                    if (tmpOut.size() > 1) {
                        tmp.add(tmpIn);
                        tmp.add(tmpOut);
                        teacherList.add(tmp);
                        tmpIn = new ArrayList<>();
                        tmpIn.add(-1.0);
                        tmpOut = new ArrayList<>();
                        tmp = new ArrayList<>();
                    }
                } else {
                    if (tmpIn.size() < 80) {
                        //Arrays.stream(line.split(" ", 0)).forEach(cell -> tmpIn.add(Double.valueOf(cell)));

                        for (String cell: line.split(" ", 0)) {
                            tmpIn.add(Double.valueOf(cell));
                        }
                    } else {
                        for (String cell: line.split(" ", 0)) {
                            tmpOut.add(Double.valueOf(cell));
                        }
                    }
                }
            }
            if (tmpOut.size() > 1) {
                tmp.add(tmpIn);
                tmp.add(tmpOut);
                teacherList.add(tmp);
            }



            fileReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            Util.println(e);
        }

        //System.out.println("Read finished.");

        return teacherList;
    }
}
