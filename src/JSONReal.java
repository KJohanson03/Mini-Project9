import java.io.PrintWriter;
import java.math.BigDecimal;

/**
 * JSON reals.
 */
public class JSONReal implements JSONValue{

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying double.
   */
  BigDecimal value;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new real given the underlying string.
   */
  public JSONReal(String str) {
    this.value = new BigDecimal(str);
  } // JSONReal(String)

  /**
   * Create a new real given a BigDecimal.
   */
  public JSONReal(BigDecimal value) {
    this.value = value;
  } // JSONReal(BigDecimal)

  /**
   * Create a new real given a double.
   */
  public JSONReal(double d) {
    this.value = BigDecimal.valueOf(d);
  } // JSONReal(double)

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
    if (other instanceof JSONReal) { 
      if ( (((JSONReal) other).getValue()).equals(this.value) ) {
        return true;
      } else {
        return false;
      } // else 
    } else {
      return false;
    } // else
                
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    if (this.value == null) {
      return 0;
    } else {
      return this.value.hashCode();
    } // else 
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   * @throws Exception
   */
  public void writeJSON(PrintWriter pen)  {


    if (this.value.compareTo(BigDecimal.ZERO) < 0 && (this.value.toString().charAt(1) == '0' )) { 
      //checks if has a leading zero or if its just a decimal
      if (this.value.toString().charAt(2) != '.' ) {
        return ;
      } // if 

    } // if 

    pen.println(this.value);

  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public BigDecimal getValue() {
    return this.value;
  } // getValue()

} // class JSONReal
