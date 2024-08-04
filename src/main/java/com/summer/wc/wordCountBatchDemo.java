package com.summer.wc;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.AggregateOperator;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.FlatMapOperator;
import org.apache.flink.api.java.operators.UnsortedGrouping;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * @author summer
 * @project_name LearningFlink
 * @create_time 2024/7/23 13:56
 * @description
 */
public class wordCountBatchDemo {
    public static void main(String[] args) throws Exception {
        //1. 获取执行环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        //2. 读取数据
        DataSource<String> lineDS = env.readTextFile("./input/words.txt");
        //3. 处理数据
        FlatMapOperator<String, Tuple2<String, Long>> wordAndOne = lineDS.flatMap(new FlatMapFunction<String, Tuple2<String, Long>>() {
            @Override
            public void flatMap(String value, Collector<Tuple2<String, Long>> out) throws Exception {
                // 3.1 切分
                String[] words = value.split(" ");
                for (String word : words) {
                    // 3.2 转换
                    out.collect(Tuple2.of(word, 1L));
                }
            }
        });
        // 3.3 分组
        UnsortedGrouping<Tuple2<String, Long>> oneAndOneUG = wordAndOne.groupBy(0);
        // 3.4 求和
        AggregateOperator<Tuple2<String, Long>> result = oneAndOneUG.sum(1);

        //4. 输出
        result.print();
    }
}
