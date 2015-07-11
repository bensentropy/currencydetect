(ns currencydetect.utils)

(defn count-back [pos s]
  (if (neg? pos)
    (+ (count s) pos)
    pos))

(defn substring
  "Returns the substring of s beginning at start inclusive, and ending
  at end (defaults to length of string), exclusive. Negative arguments
  count from the end."
  ([#^String s start] (subs s (count-back start s) (count s)))
  ([#^String s start end]
   (subs s (count-back start s) (count-back end s))))

(defn substring-catch
  "Wrap substring in catch for any index errors and just return an empty string"
  [& args]
  (try (apply substring args)
       (catch Exception e
         "")))

(defn find-first
  "Find the first item in coll given a predicate function"
  [f coll]
  (first (drop-while (complement f) coll)))

(defn str-insert
  "Insert c in string s at index i."
  [s c i]
  (str (subs s 0 i) c (subs s i)))