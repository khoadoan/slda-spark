package slda.hadoop.inference;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;

import org.apache.hadoop.io.Writable;

import edu.umd.cloud9.util.map.HMapII;

/**
 * A representation for a document (aka. Image). This is different from "document" definition 
 * of SLDA. The document here is equivalent to an Image
 * 
 * @author Khoa
 *
 */
public class Document implements Writable, Cloneable, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 752244298258266755L;

  /**
   * 
   */
  private int[] content = null;
  private HMapII termCount = null;

  /**
   * @deprecated
   */
  private double[] gamma = null;

  /**
   * Define the number of distinct words in this document
   */
  //private int numberOfWords = 0;

  /**
   * Creates a <code>LDADocument</code> object from a byte array.
   * 
   * @param bytes raw serialized representation
   * @return a newly-created <code>LDADocument</code> object
   * @throws IOException
   */
  public static Document create(byte[] bytes) throws IOException {
    return create(new DataInputStream(new ByteArrayInputStream(bytes)));
  }

  /**
   * Creates a <code>LDADocument</code> object from a <code>DataInput</code>.
   * 
   * @param in source for reading the serialized representation
   * @return a newly-created <code>LDADocument</code> object
   * @throws IOException
   */
  public static Document create(DataInput in) throws IOException {
    Document m = new Document();
    m.readFields(in);

    return m;
  }

  public Document() {
  }

  public Document(int[] document) {
    setDocument(document);
  }

  /**
   * @deprecated
   * @param document
   * @param gamma
   */
  public Document(int[] document, double[] gamma) {
    this(document);
    this.gamma = gamma;
  }

  /**
   * @deprecated
   * @param document
   * @param numberOfTopics
   */
  public Document(int[] document, int numberOfTopics) {
    this(document, new double[numberOfTopics]);
  }

  public int[] getContent() {
    return this.content;
  }

  /**
   * @deprecated
   * @return
   */
  public double[] getGamma() {
    return gamma;
  }
  
  public int getTermCount(int termID) {
	  return termCount.get(termID);
  }

  /**
   * @deprecated
   * @return
   */
  public int getNumberOfTopics() {
    if (gamma == null) {
      return 0;
    } else {
      return gamma.length;
    }
  }

  /**
   * Get the total number of words in this document, not necessarily distinct.
   * 
   * @return the total number of words in this document, not necessarily distinct.
   */
  public int getNumberOfWords() {
    return content.length;
  }

  /**
   * Deserializes the LDADocument.
   * 
   * @param in source for raw byte representation
   */
  public void readFields(DataInput in) throws IOException {
    int numEntries = in.readInt();
    if (numEntries <= 0) {
      content = null;
    } else {
      content = new int[numEntries];
      for (int i = 0; i < numEntries; i++) {
        content[i] = in.readInt();
      }
    }

    int numTopics = in.readInt();
    if (numTopics <= 0) {
      gamma = null;
    } else {
      gamma = new double[numTopics];
      for (int i = 0; i < numTopics; i++) {
        gamma[i] = in.readDouble();
      }
    }
  }

  /**
   * Returns the serialized representation of this object as a byte array.
   * 
   * @return byte array representing the serialized representation of this object
   * @throws IOException
   */
  public byte[] serialize() throws IOException {
    ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    DataOutputStream dataOut = new DataOutputStream(bytesOut);
    write(dataOut);

    return bytesOut.toByteArray();
  }

  public void setDocument(int[] document) {
    content = document;
    if(content != null) {
    	termCount = new HMapII();
    	for(int termID: this.content) {
    		if(termCount.containsKey(termID)) {
    			termCount.put(termID, termCount.get(termID) + 1);
    		} else {
    			termCount.put(termID, 1);
    		}
    	}
    }
  }

  /**
   * @deprecated
   * @param gamma
   */
  public void setGamma(double[] gamma) {
    this.gamma = gamma;
  }

  @Override
  public String toString() {
    StringBuilder document = new StringBuilder("content:\t");
    if (content == null) {
      document.append("null");
    } else {
      for(int i=0; i<content.length; i++) {
        document.append(i);
        document.append(":");
        document.append(content[i]);
        document.append(" ");
      }
    }
    document.append("\ngamma:\t");
    if (gamma == null) {
      document.append("null");
    } else {
      for (double value : gamma) {
        document.append(value);
        document.append(" ");
      }
    }

    return document.toString();
  }

  /**
   * Serializes the map.
   * 
   * @param out where to write the raw byte representation
   */
  public void write(DataOutput out) throws IOException {
    // Write out the number of entries in the map.
    if (content == null) {
      out.writeInt(0);
    } else {
      out.writeInt(content.length);
      for (int i=0; i<content.length; i++) {
        out.writeInt(content[i]);
      }
    }

    // Write out the gamma values for this document.
    if (gamma == null) {
      out.writeInt(0);
    } else {
      out.writeInt(gamma.length);
      for (double value : gamma) {
        // TODO: change it to double and also in read method
        out.writeDouble(value);
      }
    }
  }
}