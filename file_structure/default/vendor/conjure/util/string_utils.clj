(ns conjure.util.string-utils
  (:require [clojure.contrib.str-utils :as str-utils]
            [clj-record.util :as clj-record-util]))

(defn
#^{:doc "If the string's length does not equal total-length then this method returns a new string with length 
total-length by adding fill-char multiple times to the beginning of string. If string's length is already total-length,
then this method simply returns it."}
  prefill [string total-length fill-char]
  (let [base-string (if string string "")
        final-length (if total-length total-length 0)]
    (if (>= (. base-string length) final-length)
      base-string
      (str (str-utils/str-join "" 
        (map
          (fn [index] fill-char) 
          (range (- final-length (. base-string length))))) base-string))))
        
(defn
#^{:doc "Converts a keyword to it's string value. Basically, it just removes the ':' from the beginning."}
  str-keyword [keyword]
  (if (keyword? keyword)
    (. (str keyword) substring 1)
    (str keyword)))
    
(defn
#^{:doc "If string ends with the string ending, then remove ending and return the result. Otherwise, return string."}
  strip-ending [string ending]
  (if (and string ending (. string endsWith ending))
    (let [ending-index (- (. string length) (. ending length))]
      (. string substring 0 ending-index))
    string))