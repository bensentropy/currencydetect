(ns currencydetect.core-test
  (:require [clojure.test :refer :all]
            [currencydetect.core :refer :all]))

(deftest extract-price-test
  (testing "Extracting price from a range of string cases."
    (is (= (extract-price "USD 12.00") 12.0M))
    (is (= (extract-price "20") 20.0M))
    (is (= (extract-price "$10,001.00") 10001.0M))
    (is (= (extract-price "$10.001,00") 10001.0M))
    (is (= (extract-price "$1,0.0.01,00") 10001.0M))
    (is (= (extract-price "£234,234.0") 234234.0M))))

(deftest symbol-max-gdp-test
  (testing "Currency code of largest country is returned for currency symbol"
    (is (= (symbol-max-gdp "$") "USD"))
    (is (= (symbol-max-gdp "€") "EUR"))
    (is (= (symbol-max-gdp "£") "GBP"))
    (is (= (symbol-max-gdp "฿") "THB"))))

(deftest tld->symbol-test
  (testing "That currency set is exluded"
    (is (empty? (ffirst (tld->symbol #{"aw"}))))))

(deftest tld->ccode-test
  (testing "That tld maps to currency code"
    (is (= (tld->ccode "nz" #{}) "NZD"))))

; integration tests

(deftest detect-currency-test
  (testing "That tld maps to currency code"
    (is (= (detect-currency-code "£165.00" "") "GBP"))
    (is (= (detect-currency-code "$165.00" "tv") "USD"))
    (is (= (detect-currency-code "$165.00", "nz") "NZD"))
    (is (= (detect-currency-code "NZD$ 34.34" "") "NZD"))
    (is (= (detect-currency-code "NZD$ 34.34" "") "NZD"))
    (is (= (detect-currency-code "2345,33" "") "EUR"))
    (is (= (detect-currency-code "TT$2" "") "TTD"))
    (is (= (detect-currency-code "1" "") "USD"))
    (is (= (detect-currency-code "2345.33" "") "USD"))))

(deftest parse-price-test
  (testing ""
    (is (= (parse-price "20.0" "http://www.r.co.uk")
           {:amount 20.0M, :code "GBP" :tld "uk"}))
    (is (= (parse-price "30,00" "http://www.r.dj")
           {:amount 30.0M, :code "EUR" :tld "dj"}))
    (is (= (parse-price "$40") {:amount 40.0M, :code "USD" :tld nil}))))