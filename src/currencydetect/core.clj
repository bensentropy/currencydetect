(ns currencydetect.core
  (:require [currencydetect.utils :as utils]
            [clojure.string :as str]
            [clojure.set :as s]
            [clojurewerkz.urly.core :refer [tld-of url-like]]
            [currencydetect.data :refer [tld-stats currency-symbols outlier-tlds
                                         world-gdp euro-gdp]]))

(defn tld->symbol
  "Pair's of tlds maped to respective currency codes"
  [exclude]
  (map #(vector (s/difference
                  (-> % second :tld) exclude) (-> % first name))
    currency-symbols))

(defn tld->ccode
  "Find currency code by tld and exclude set of tlds"
  [tld exclude]
  (second (utils/find-first #(contains? (first %) tld) (tld->symbol exclude))))


(defn symbol-max-gdp
  "Retrive the currency code for currency symbol ranked by the largest GDPs"
  [currency-symbol]
  (let [symbol-tlds (map :tld (filter #(= (:symbol %) currency-symbol)
                                      (vals currency-symbols)))
        tld-kws (->> symbol-tlds (apply s/union) (map keyword))
        max-tld (key (apply max-key #(-> % val :gdp)
                            (select-keys tld-stats tld-kws)))]
    (tld->ccode (name max-tld) #{})))

(defn extract-price [text]
  (let [price-str (-> (re-find #"[\d,\.]+" text)
                      (str/replace "," ".")
                      str/reverse)
        change #(if (or (= -1 %) (not= 2 %)) 0 %) ; modify if no decimal place is found
        decimal-pos (-> price-str (.indexOf ".") change)
        price (str/replace price-str "." "")]
    (bigdec (str/reverse (utils/str-insert price "." decimal-pos)))))

(defn detect-currency-code
  "Detect the currency cody for a given string"
  [text tld]
  (let [text (str/trim text)
        three (utils/substring-catch text 0 3)
        currency-symbol (re-find #"[^\d\s]*" text)
        symbol-count (count (filter #{currency-symbol}
                                    (map :symbol (vals currency-symbols))))
        decimal-mark (utils/substring-catch text -3 -2)]
    (cond
      ; if first three characters is currency code
      (contains? currency-symbols (keyword three)) three
      ; if symbol is one to one
      (= symbol-count 1) (-> (utils/find-first
                               #(= (-> % second :symbol) currency-symbol)
                               currency-symbols) first name)
      ; if tld maps to currency symbol
      (some? (tld->ccode tld outlier-tlds)) (tld->ccode tld outlier-tlds)
      (> symbol-count 1) (symbol-max-gdp currency-symbol)
      (= decimal-mark ",") "EUR"
      (= decimal-mark ".") "USD"
      ; if all fails revert to USD
      :else "USD")))

(defn parse-price
  ([text] (parse-price text nil))
  ([text url]
   (let [tld (some-> url url-like tld-of (str/split #"\.") last)]
    {:amount (extract-price text)
     :code (detect-currency-code text tld)
     :tld tld})))