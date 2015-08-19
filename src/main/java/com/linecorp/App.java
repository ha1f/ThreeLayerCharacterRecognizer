package com.linecorp;

import com.linecorp.api.ReadFileApi;
import com.linecorp.service.PerceptronService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */

@EnableAutoConfiguration
@ComponentScan
@Controller
public class App implements CommandLineRunner {
	@Autowired
	PerceptronService perceptronService;

	@Autowired
	ReadFileApi readFileApi;

	//教師の入出力
	private List<List<List<Double>>> teacherList = new ArrayList<>();

	public boolean checkOutput(List<Double> teacherIn, List<Double> teacherOut) {
		return (perceptronService.getOutput(teacherIn) == Util.getMaxIndex(teacherOut));
	}

	public double checkAccuracy(List<List<List<Double>>> dataList) {
		int cnum = 0;
		int inum = 0;

		for (List<List<Double>> list : dataList) {
			if (checkOutput(list.get(0), list.get(1))) {
				cnum++;
			} else {
				inum++;
			}
		}
		return 100.0 * cnum / (cnum + inum);
	}

	public void test() {
		//ラムダ式だとaccuracy測定できない
		/*teacherList.stream().forEach(list -> checkOutput(list.get(0), list.get(1)));
		readFileApi.getData("mlp_test1.data.txt").stream().forEach(list -> checkOutput(list.get(0), list.get(1)));
		readFileApi.getData("mlp_test3.data.txt").stream().forEach(list -> checkOutput(list.get(0), list.get(1)));
		readFileApi.getData("mlp_test10.data.txt").stream().forEach(list -> checkOutput(list.get(0), list.get(1)));*/

		System.out.print("learnDataCheck: ");
		System.out.println("accuracy:" + checkAccuracy(teacherList) + "%");

		System.out.print("test1Check: ");
		System.out.println("accuracy:" + checkAccuracy(readFileApi.getData("mlp_test1.data.txt")) + "%");

		System.out.print("test3Check: ");
		System.out.println("accuracy:" + checkAccuracy(readFileApi.getData("mlp_test3.data.txt")) + "%");

		System.out.print("test10Check: ");
		System.out.println("accuracy:" + checkAccuracy(readFileApi.getData("mlp_test10.data.txt")) + "%");
	}

	@Override
	public void run(String... strings) throws Exception {
		//ここでteacherListを読み込み
		teacherList = readFileApi.getData("mlp_train.data");

		//読み込んだデータを元に学習開始
		perceptronService.create();

		int i = 0;
		Double diff = 100.0;
		while (diff > 5) {
			teacherList.forEach(perceptronService::learn);
			diff = perceptronService.getDiffSum(teacherList);
			Util.println(i + "回目：diff=" + diff);
			i++;
		}

		perceptronService.showAllData();

		test();
	}

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
