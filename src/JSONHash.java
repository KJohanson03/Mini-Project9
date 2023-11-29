import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * JSON hashes/objects.
 */
public class JSONHash<K,V> {

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

    /**
   * The number of values currently stored in the hash table. We use this to
   * determine when to expand the hash table.
   */
  int size = 0;

  /**
   * The array that we use to store the ArrayList of key/value pairs. (We use an
   * array, rather than an ArrayList, because we want to control expansion and
   * ArrayLists of ArrayLists are just weird.)
   */
  Object[] buckets;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  // +-------------------------+-------------------------------------
  // | Standard object methods |
  // +-------------------------+

  /**
   * Convert to a string (e.g., for printing).
   */
  public String toString() {
    if (this.buckets == null) {
      return "{ }";
    }
    String holder = "";
    holder = holder.concat("{ ");
    for (int i = 0; i < this.buckets.length; i++) {
      @SuppressWarnings("unchecked")
      ArrayList<KVPair<JSONString,JSONValue>> alist = (ArrayList<KVPair<JSONString,JSONValue>>) this.buckets[i];
      if (alist != null) {
        for (KVPair<JSONString,JSONValue> pair : alist) {
          holder = holder.concat(pair.key.toString() + ":" + pair.value.toString() + ", ");
        } // for each pair in the bucket
      } // if the current bucket is not null
    } // for each bucket
    holder = holder.concat("}");
  } // toString()

  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    return true;        // STUB
  } // equals(Object)

  /**
   * Compute the hash code.
   */
  public int hashCode() {
    return 0;           // STUB
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

  } // writeJSON(PrintWriter)

  /**
   * Get the underlying value.
   */
  public Iterator<KVPair<JSONString,JSONValue>> getValue() {
    return this.iterator();
  } // getValue()

  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  /**
   * Get the value associated with a key.
   */
  public JSONValue get(JSONString key) {
      int index = find(key);
    @SuppressWarnings("unchecked")
    ArrayList<KVPair<JSONString,JSONValue>> alist = (ArrayList<KVPair<JSONString,JSONValue>>) buckets[index];
    if (alist == null) {
      throw new IndexOutOfBoundsException("Invalid key: " + key);
    } else {
      KVPair<JSONString,JSONValue> pair = alist.get(0);
      for (int i = 0; i < alist.size(); i++) {
        if (alist.get(i).key().equals(key)) {
          return alist.get(i).value();
        }
      }
      return pair.value();
    } // get
  } // get(JSONString)

  /**
   * Get all of the key/value pairs.
   */
  public Iterator<KVPair<JSONString,JSONValue>> iterator() {
    return null;        // STUB
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  @SuppressWarnings("unchecked")
  public void set(JSONString key, JSONValue value) {
    JSONValue result = null;
    // If there are too many entries, expand the table.
    if (this.size > (this.buckets.length * LOAD_FACTOR)) {
      expand();
    } // if there are too many entries

    if (containsKey(key)) {
      //throw new NullPointerException("Invalid Key");


      int index = find(key);
      ArrayList<KVPair<JSONString,JSONValue>> alist = (ArrayList<KVPair<JSONString,JSONValue>>) this.buckets[index];
      KVPair<JSONString,JSONValue> pair = new KVPair<>(key,value); 
      for (int i = 0; i < alist.size(); i++) {
        if (alist.get(i).key().equals(key)) {
          alist.set(i, pair);
          
        }
        else{
          alist.add(pair);
        }
      }
      
      
    }
    else{

    
    // Find out where the key belongs and put the pair there.
    int index = find(key);
    ArrayList<KVPair<JSONString,JSONValue>> alist = (ArrayList<KVPair<JSONString,JSONValue>>) this.buckets[index];
    // Special case: Nothing there yet
    if (alist == null) {
      alist = new ArrayList<KVPair<JSONString,JSONValue>>();
      this.buckets[index] = alist;
    }
    alist.add(new KVPair<JSONString,JSONValue>(key, value));
    ++this.size;
  }

  } // set(JSONString, JSONValue)

  /**
   * Find out how many key/value pairs are in the hash table.
   */
  public int size() {
    return 0;           // STUB
  } // size()

    /**
   * Find the index of the entry with a given key. If there is no such entry,
   * return the index of an entry we can use to store that key.
   */
  int find(JSONString key) {
    return Math.abs(key.hashCode()) % this.buckets.length;
  } // find(K)


  /**
   * Determine if the hash table contains a particular key.
   */
  @Override
  public boolean containsKey(JSONString key) {
    // STUB/HACK
    try {
      get(key);
      return true;
    } catch (Exception e) {
      return false;
    } // try/catch
  } // containsKey(K)


} // class JSONHash
