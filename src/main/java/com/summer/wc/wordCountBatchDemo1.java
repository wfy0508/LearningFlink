package com.summer.wc;

import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author summer
 * @project_name LearningFlink
 * @create_time 2024/7/23 13:56
 * @description new FlatMapFunction改lambda表达式和算子链式计算
 */
public class wordCountBatchDemo1 {
    public static void main(String[] args) throws Exception {
        //1. 获取执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //2. 读取数据
        DataSource<String> lineDS = env.readTextFile("./input/words.txt");
        //3. 处理数据
        lineDS.flatMap((String value, Collector<Tuple2<String, Integer>> out) -> {
                    String[] s = value.split(" ");
                    for (String string : value.split(" ")) {
                        out.collect(Tuple2.of(string, 1));
                    }
                })
                .setParallelism(2)
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .groupBy(0)
                .sum(1)
                .print();
    }
}
