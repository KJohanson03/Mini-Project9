import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * JSON arrays.
 */
public class JSONArray {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The underlying array.
   */
  ArrayList<JSONValue> values;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Build a new array.
   */
  public JSONArray() {
    this.values = new ArrayList<JSONValue>();
  } // JSONArray() 

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    if (this.values.get(0) == null) {
        return ("[ ]");
    } else {
    String holder = "[ ";
    for (int i = 0; i < this.values.size() - 1; i++){
        holder = holder.concat(this.values.get(i).toString() + ", ");
    }
    holder = holder.concat(this.values.get(this.values.size() - 1).toString() + " ]");
    return holder;
    }
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if (!(other instanceof JSONArray)) {
        return false;
    } else {
        JSONArray temp = (JSONArray) other;
        for (int i = 0; i < this.values.size(); i++) {
            if (!(this.values.get(i).equals(temp.values.get(i)))) {
                return false;
            }
        }
        return true;
    }
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    if (this.values == null){
      return 0;
    } else{
      return this.values.hashCode();
    } // else 
  } // hashCode()

  // +--------------------+------------------------------------------
  // | Additional methods |
  // +--------------------+

  /**
   * Write the value as JSON.
   */
  public void writeJSON(PrintWriter pen) {
    pen.print(this.toString());
  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public ArrayList<JSONValue> getValue() {
    return this.values;
  } // getValue()

  // +---------------+-----------------------------------------------
  // | Array methods |
  // +---------------+

  /**
   * Add a value to the end of the array.
   */
  public void add(JSONValue value) {
    this.values.add(value);
  } // add(JSONValue)

  /**
   * Get the value at a particular index.
   */
  public JSONValue get(int index) throws IndexOutOfBoundsException {
    return this.values.get(index);
  } // get(int)

  /**
   * Get the iterator for the elements.
   */
  public Iterator<JSONValue> iterator() {
    return this.values.iterator();
  } // iterator()

  /**
   * Set the value at a particular index.
   */
  public void set(int index, JSONValue value) throws IndexOutOfBoundsException {
    this.values.set(index, value);
  } // set(int, JSONValue)

  /**
   * Determine how many values are in the array.
   */
  public int size() {
    return this.values.size();
  } // size()
} // class JSONArray
