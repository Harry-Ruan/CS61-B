package gh2;

import edu.princeton.cs.algs4.*;

public class GuitarHero {
    public static void main(String[] args) {
        // 1. 定义键盘字符串（共 37 个字符）e
        String keyboard = "q2we4r5ty7u8i9op-[-zxdcfvgbnjmk,.;/' ";

        // 2. 创建一个包含 37 根吉他弦的数组
        GuitarString[] strings = new GuitarString[37];

        // 3. 用 for 循环初始化这 37 根弦，计算每根弦的频率
        for (int i = 0; i < 37; i++) {
            // 使用公式：f = 440 * 2^((i - 24) / 12)
            double frequency = 440.0 * Math.pow(2.0, (i - 24) / 12.0);
            strings[i] = new GuitarString(frequency);
        }

        // 4. 游戏主循环（这里以标准 stdDraw 或键盘监听为例，具体取决于你们课程的库，比如 StdDraw）
        while (true) {
            // 检查用户是否敲击了键盘
            if (StdDraw.hasNextKeyTyped()) {
                // 读取用户按下的键
                char key = StdDraw.nextKeyTyped();

                // 寻找这个键在字符串中的位置
                int index = keyboard.indexOf(key);

                // 安全检查：如果 index 是 -1，说明按了不相关的键，直接忽略不处理
                if (index != -1) {
                    strings[index].pluck();
                }
            }

            // 5. 合成声音并播放（这部分通常是把所有弦的 sample 叠加起来）
            double sample = 0.0;
            for (int i = 0; i < 37; i++) {
                sample += strings[i].sample();
            }

            // 播放这个采样点（例如使用 StdAudio）
            StdAudio.play(sample);

            // 推进所有弦的前进状态（tic）
            for (int i = 0; i < 37; i++) {
                strings[i].tic();
            }
        }
    }
}
