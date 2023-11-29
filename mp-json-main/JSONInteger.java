import java.io.PrintWriter;
import java.math.BigInteger;

/**
 * JSON integers.
 */
public class JSONInteger {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying integer.
   */
  BigInteger value;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new integer given the underlying string.
   */
  public JSONInteger(String str) {
    this.value = new BigInteger(str);
  } // JSONInteger(String)

  /**
   * Create a new integer given a BigInteger.
   */
  public JSONInteger(BigInteger value) {
    this.value = value;
  } // JSONInteger(BigInteger)

  /**
   * Create a new integer given an integer or long.
   */
  public JSONInteger(long l) {
    this.value = BigInteger.valueOf(l);
  } // JSONInteger(long)

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    return this.value.toString();
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if (!(other instanceof JSONInteger)) {
        return false;
    } else {
        JSONInteger temp = (JSONInteger) other;
        return temp.value.equals(this.value);
    }
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    if (this.value == null){
      return 0; 
    }
    else {
      return this.value.hashCode();
    }
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) throws Exception{
    if ((this.value.compareTo(BigInteger.ZERO) < 0) && (this.value.toString().charAt(1) != '0')) {
        pen.println(this.toString());
    }
    else {
        throw new Exception();
    }

    
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public BigInteger getValue() {
    return this.value;
  } // getValue()

} // class JSONInteger
