(ns currencydetect.utils-test
  (:require [clojure.test :refer :all]
            [currencydetect.utils :refer :all]
            ))

(deftest substring-test
  (testing "Test substring with negative indexes."
    (is (= (substring "test string" -3) "ing"))
    (is (= (substring "test string" 3) "t string"))
    (is (= (substring "test string" -3 -1) "in"))
    (is (= (substring "test string" 3 -1) "t strin"))))

(deftest substring-catch-test
  (testing "Test substring with negative indexes."
    (is (= (substring-catch "test string" -20) ""))))

(deftest find-first-test
  (testing "Extracting price from a range of string cases."
    (is (= (find-first #(= (first %) "test2")
                       [["test" 3] ["test2" 3]] ) ["test2" 3]))))

(deftest str-insert-test
  (testing "Extracting price from a range of string cases."
    (is (= (str-insert "test string" "." 3) "tes.t string"))
    (is (= (str-insert "test string" "." 0) ".test string"))))