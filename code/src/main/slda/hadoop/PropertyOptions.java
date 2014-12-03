package slda.hadoop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * This class acts as an adapter to a property file and command-line options in order to parse
 * key-value configuration and provides simpler interface for getting these properties. The user can parse
 * properies from a provided property file, or directly pass these properties in the command line. To
 * use this class, provide either or both of these command line options:
 *  -p [path to a property file]: full path to the property file
 *  -P key=value: multiple intances of key-value pairs
 * 
 * -P has higher precedence over -p
 * 
 * @author Khoa
 *
 */
public class PropertyOptions {
	private final static Logger LOG = Logger.getLogger(PropertyOptions.class);
	
	public static final String PROPERTY_FILE = "p";
	public static final String PROPERTY_OPTION = "P";
	private final SimpleOptions opts;
	private final Properties props;

	public PropertyOptions() {
		opts = new SimpleOptions("p:P:");
		props = new Properties();
	}
	
	/**
	 * Parse a list of arguments into their key-value properties map.
	 * @param args
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public PropertyOptions parse(String[] args) throws FileNotFoundException, IOException {
		opts.parse(args);
		if(opts.hasOption(PROPERTY_FILE)) {
			LOG.info("Reading property file at: " + opts.getOption(PROPERTY_FILE));
			props.load(new FileInputStream(opts.getOption(PROPERTY_FILE)));
			LOG.debug("Loaded " + props.size() + " properties from property file");
		}
		if(opts.hasOption(PROPERTY_OPTION)) {
			for(String prop: opts.getOptionValues(PROPERTY_OPTION)) {
				String[] keyval = prop.split("=", 2);
		        if (keyval.length == 2) {
		          props.put(keyval[0], keyval[1]);
		        }
			}
			LOG.debug("Loaded " + props.size() + " properties from both property file and command line");
		}
		
		return this;
	}
	
	/**
	 * Determine if the property map contains a key
	 * @param key
	 * @return
	 */
	public boolean containsKey(String key) {
		return props.containsKey(key);
	}
	
	/**
	 * Get a value of a key
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return props.getProperty(key);
	}
	
	/**
	 * Get a value of a key. If the key's not there, use the provided default value.
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getProperty(String key, String defaultValue) {
		return containsKey(key) ? getProperty(key) : defaultValue;
	}
	
	/**
	 * Get the set of all available keys
	 * @return
	 */
	public Set<Object> keySet() {
		return props.keySet();
	}
}
