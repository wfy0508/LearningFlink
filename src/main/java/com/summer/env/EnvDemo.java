package com.summer.env;

import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.util.Collector;

/**
 * @author summer
 * @project_name LearningFlink
 * @create_time 2024/8/4 19:03
 * @description
 */
public class EnvDemo {
    public static void main(String[] args) throws Exception {
        // 0. 创建新的配置
        Configuration conf = new Configuration();
        conf.set(RestOptions.BIND_PORT, "8082");
        // 1. 获取执行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment(conf);
        //env.setRuntimeMode(RuntimeExecutionMode.BATCH);
        env.setRuntimeMode(RuntimeExecutionMode.STREAMING);
        // 2. 数据操作
        env.socketTextStream("localhost", 9999)
                .flatMap((String lines, Collector<Tuple2<String, Integer>> out) -> {
                    String[] split = lines.split(" ");
                    for (String s : split) {
                        out.collect(Tuple2.of(s, 1));
                    }
                })
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(0)
                .sum(1)
                .print();
        // 3. 开启任务
        env.execute();
    }
}
