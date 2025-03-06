package ohmygod.project.text;

import java.util.Scanner;

public class Helloworld{
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 读取数组长度
        int[] scores = new int[scanner.nextInt()];

        // 循环给数组赋值
        for (int i = 0; i < scores.length; i++) {
            scores[i] = scanner.nextInt();
        }

        // 在这里计算数组scores的平均值和最大值
        double sum = 0;
        int max = scores[0];
        for (int i = 0; i < scores.length; i++) {
            sum += scores[i];
            if (scores[i] > max) {
                max = scores[i];
            }
        }
        double average = sum / scores.length;

        // 输出平均值和最大值
        System.out.println("平均值：" + average);
        System.out.println("最大值：" + max);

        scanner.close(); // 关闭Scanner对象
    }
}
