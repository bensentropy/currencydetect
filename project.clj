(defproject currencydetect "0.1.2"
  :description "A libary for currency code extraction from natural language"
  :url "http://github.com/bensentropy/currencydetect"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [clojurewerkz/urly "1.0.0"]]
  :main ^:skip-aot currencydetect.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
