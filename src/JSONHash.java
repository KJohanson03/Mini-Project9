import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * JSON hashes/objects.
 */
public class JSONHash<K,V> implements JSONValue{

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The load factor for expanding the table.
   * CONSTANT
   */
  static final double LOAD_FACTOR = 0.7;

    /**
   * Our helpful random number generator, used primarily when expanding the size
   * of the table..
   */
  Random rand;

  /**
   * 
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

  /**
   * Create a new hash table.
   */
  public JSONHash() {
    this.rand = new Random();
    this.clear();

  } // ChainedHashTable


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
          holder = holder.concat(pair.key().toString() + ": " + pair.value().toString() + ", ");
        } // for each pair in the bucket
      } // if the current bucket is not null
    } // for each bucket
    holder = holder.substring(0, holder.length() - 2);
    holder = holder.concat(" }");
    return holder;
  } // toString()


  /**
   * Compare to another object.
   */
  public boolean equals(Object other) {
    if (!(other instanceof JSONHash)) {
       new Exception("not a JSONHash");
    } 
      @SuppressWarnings("unchecked")
      JSONHash<K,V> newOther = (JSONHash<K,V>) other;
    
    for (int i = 0; i < this.buckets.length; i++) {
      @SuppressWarnings("unchecked")
      ArrayList<KVPair<JSONString,JSONValue>> alist = (ArrayList<KVPair<JSONString,JSONValue>>) this.buckets[i];
      if (alist != null) {
        for (KVPair<JSONString,JSONValue> pair : alist) {
          // position of the bucket where the key could be 
          int bucketPos = newOther.find(pair.key());

          @SuppressWarnings("unchecked")
          // array list at compared JSONHash
          ArrayList<KVPair<JSONString,JSONValue>> blist = (ArrayList<KVPair<JSONString,JSONValue>>) newOther.buckets[bucketPos];

          // iterates through the bucket to see if key value matches up 
          for (int b = 0; b < blist.size(); b++) {
            if ( blist.get(b) == pair.value()) {
              return true;
            } // if 
          } // for 
        } // for each pair in the bucket
      } // if the current bucket is not null
    } // for each bucket 
    
    return false;
  } // equals(Object)  


  /**
   * Compute the hash code.
   */
  public int hashCode() {
    if (this.buckets == null){
      return 0;
    } else{
      return this.buckets.hashCode();
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
  public Iterator<KVPair<JSONString,JSONValue>> getValue() {
    return this.iterator();
  } // getValue()

  // +-------------------+-------------------------------------------
  // | Hashtable methods |
  // +-------------------+

  /**
   * Get the value associated with a key.
   * if key is not found return the value at index 0 
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
        return new Iterator<KVPair<JSONString,JSONValue>>() { /** The position in the underlying array. */ 
        int pos = 0;
        int aPos = 0;
        
      public boolean hasNext() {
        if (buckets[pos] != null) {
          @SuppressWarnings("unchecked")
          ArrayList<KVPair<JSONString,JSONValue>> alist = (ArrayList<KVPair<JSONString,JSONValue>>) buckets[pos];
          if (aPos < alist.size() - 1 && alist.get(aPos + 1) != null) {
            return true;
          } else {
            int temp = aPos;
            aPos = 0;
            pos++;
            boolean returnBool =  hasNext();
            aPos = temp;
            pos--;
            return returnBool;
          } // else
        } else if (pos < buckets.length - 1) {
            int temp = aPos;
            aPos = 0;
            pos++;
            boolean returnBool = hasNext();
            aPos = temp;
            pos--;
            return returnBool;
        } else {
          return false;
        } // else
      } // hasNext()
    
      public KVPair<JSONString, JSONValue> next() {
        if (buckets[pos] != null) {
          @SuppressWarnings("unchecked")
          ArrayList<KVPair<JSONString,JSONValue>> alist = (ArrayList<KVPair<JSONString,JSONValue>>) buckets[pos];
          if (aPos < alist.size() - 1) {
            KVPair<JSONString,JSONValue> pair = alist.get(aPos);
            aPos++;
            return pair;
          } else {
            aPos = 0;
            pos++;
            return next();
          } // else
        } else if (pos < buckets.length - 1) {
            aPos = 0;
            pos++;
            return next();
        } else {
          throw new IndexOutOfBoundsException("No no no");
        } // else 
      } // next()
    };
  } // iterator()

  /**
   * Set the value associated with a key.
   */
  @SuppressWarnings("unchecked")
  public void set(JSONString key, JSONValue value) {
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
    return this.size;
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
  public boolean containsKey(JSONString key) {
    // STUB/HACK
    try {
      get(key);
      return true;
    } catch (Exception e) {
      return false;
    } // try/catch
  } // containsKey(K)

  /**
   * Expand the size of the table.
   */
  void expand() {
  // Figure out the size of the new table
  int newSize = 2 * this.buckets.length + rand.nextInt(10);
  // Remember the old table
  Object[] oldBuckets = this.buckets;
  // Create a new table of that size.
  this.buckets = new Object[newSize];
  // Move all buckets from the old table to their appropriate
  // location in the new table.
  for (int i = 0; i < oldBuckets.length; i++) {
    @SuppressWarnings("unchecked")
    ArrayList<KVPair<JSONString,JSONValue>> alist = (ArrayList<KVPair<JSONString,JSONValue>>) oldBuckets[i];
    if (alist != null) {
      for (KVPair<JSONString,JSONValue> pair : alist) {
        this.set(pair.key(), pair.value());
      } // for each pair in the arraylist
    } // if 
  } // for

} // expand()




  /**
   * Clear the whole table.
   */
  public void clear() {
    this.buckets = new Object[41];
    this.size = 0;
  } // clear()



} // class JSONHash
