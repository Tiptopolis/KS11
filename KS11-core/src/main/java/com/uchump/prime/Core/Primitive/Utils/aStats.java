package com.uchump.prime.Core.Primitive.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.ToDoubleFunction;
import java.util.stream.DoubleStream;

import com.uchump.prime.Core.Primitive.Struct.aMap;
import com.uchump.prime.Core.Utils.StringUtils;

public class aStats {

	public aMap<String, Number> out = new aMap<String, Number>();
	private Number[] values;

	public aStats(Number... N) {
		List<Double> numbers = new ArrayList<>();
		this.values = N;
		for (Number n : N)
			numbers.add(n.doubleValue());
		this.out.put("Sum: ", statistic(numbers, s -> s.sum()));
		this.out.put("Max: ", statistic(numbers, s -> s.max().getAsDouble()));
		this.out.put("Min: ", statistic(numbers, s -> s.min().getAsDouble()));
		this.out.put("Average: ", statistic(numbers, s -> s.average().getAsDouble()));
		this.out.put("Count: ", statistic(numbers, s -> s.count()));
	}

	private static double statistic(List<Double> numbers, ToDoubleFunction<DoubleStream> function) {
		return function.applyAsDouble(numbers.stream().mapToDouble(Double::doubleValue));
	}

	public String toLog() {
		String vals = StringUtils.resolveVarargsToCSV(this.values);
		String log = "[STATS]\n";
		log += "[IN]: " + vals + "\n";
		log += "[OUT]: " + this.out.toLog();
		return log;
	}

}
