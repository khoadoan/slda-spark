package slda.hadoop;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.mutable.MutableInt;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;

public class SimpleCounter<T extends TaskInputOutputContext<?, ?, ?, ?>, E extends Enum<?>> {
	private Map<Enum<?>, MutableInt> counters;
	private T context;
	private Set<Enum<?>> primaries;
	long primaryCounter = 0;
	private String group;
	private int recordsBasedUpdatePeriod = 10000;
	private long timeout = -1;
	private long previousTimer = 0;
	
	/**
	 * @param context Hadoop Job Context
	 * @param primaries the primary counter that is used for determining the update period
	 * @param group the group name represented by this counter
	 * @param recordsBasedUpdatePeriod the number of records after which we update the counters
	 */
	public SimpleCounter(T context, String group, int recordsBasedUpdatePeriod, E... primaries) {
		Class<Enum<?>> e = (Class<Enum<?>>) primaries[0].getDeclaringClass();
		//Enumerate all the enum values
		Enum<?>[] names = e.getEnumConstants();
		counters = new HashMap<Enum<?>, MutableInt>(names.length);
		for(Enum<?> name: names) {
			counters.put(name, new MutableInt(0));
		}

		this.context = context;
		this.primaries = new HashSet<Enum<?>>(primaries.length);
		for(E primary: primaries) {
			this.primaries.add(primary);
		}
		this.group = group;
		this.recordsBasedUpdatePeriod = recordsBasedUpdatePeriod;
	}
	
	public SimpleCounter(T context, String group, E... primaries) {
		this(context, group, 10000, primaries);
	}
	
	/**
	 * Set the timer of update in seconds
	 * @param timeout
	 * @return
	 */
	public SimpleCounter setTimeout(int timeout) {
		this.timeout = timeout * 1000;
		return this;
	}
	
	public void increment(E name) {
		MutableInt value = counters.get(name);
		if(value == null) {
			value = new MutableInt();
			counters.put(name, value);
		}
		
		if(primaries.contains(name))
			primaryCounter++;
		
		value.increment();
		//Timeout takes precedence
		if(timeout >= 0 && (System.currentTimeMillis() - previousTimer > timeout)) {
			push();
			//reset the timer
			previousTimer = System.currentTimeMillis();
		} else if(primaryCounter > recordsBasedUpdatePeriod) {
			push();
		}
	}
	
	public void push() {
		for(Enum<?> name: counters.keySet()) {
			MutableInt value = counters.get(name);
			if(value.intValue() > 0) {
				context.getCounter(group, name.name()).increment(value.intValue());
				value.setValue(0);
			}
		}
		
		primaryCounter = 0;
	}
}
