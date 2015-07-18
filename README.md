# Currency Detect - natural language currency code extraction

A library to detect currency symbols in natural language by use of simple heuristics in a rule based system.

The rules are as follows:

1. check if the first three characters of the string is a currency code.

2. check if the currency symbol has a one to one mapping from symbol to currency code i.e ฿ to THB

3. check if the TLD of the url where the text was found has a one to one mapping with a currency symbol (i.e .th to THB).
If more then one country uses the respective currency symbol select the currency with the largest GDP.
TLDs are excluded if that they are outside the 90% threshold band of a log transformed linear model of the two variables; GDP and Tld counts (i.e .me .tk).

4. if USD if text uses a decimal point or to EUR if the text has a decimal comma.

5. if all else fails revert to USD

These rules are currently a speculative approximation with much room for improvement, suggestions are welcome. 

### Resources

* http://research.domaintools.com/statistics/tld-counts/
* https://en.wikipedia.org/wiki/Decimal_mark
* http://www.xe.com/symbols.php
* http://data.worldbank.org/indicator/NY.GDP.MKTP.CD

## Installation

#### Leiningen

[![Clojars Project](http://clojars.org/currencydetect/latest-version.svg)](http://clojars.org/currencydetect)

## Usage

<pre>
(ns testproject.core
  (:require [currencydetect.core :refer [parse-price]]))

(parse-price "£20.00" "http://www.r.co.uk")
=>  {:amount 20.0M, :code "GBP", :tld "uk"}

(parse-price "NZD 10")
=>  {:amount 10M, :code "NZD", :tld nil}
</pre>


## License

Copyright © 2015 Ben Olsen
Distributed under the Apache License 2.0.
