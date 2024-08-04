package com.summer.wc;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;


/**
 * @author summer
 * @project_name LearningFlink
 * @create_time 2024/7/28 15:12
 * @description
 */
public class wordCountStreamDemo {
    public static void main(String[] args) throws Exception {
        // 1. 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 2. 读取文件
        DataStreamSource<String> dataStreamSource = env.readTextFile("input/words.txt");
        // 3. 文件分割、转换、分组、聚合、打印输出
        dataStreamSource.flatMap((String value, Collector<Tuple2<String, Integer>> out) -> {
                    String[] wordSplit = value.split(" ");
                    for (String word : wordSplit) {
                        out.collect(Tuple2.of(word, 1));
                    }
                })
                .setParallelism(2)
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(0)
                .sum(1)
                .print();
        env.execute();
    }
}
