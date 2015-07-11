# Currency Detect - a natural language currency code extraction

A library to detect currency symbols in natural language by use of simple heuristics in rule based system.

The rules are as follows:

1. Check if the first three characters of the string is a currency code.

2. Check if the currency symbol has a one to one mapping from symbol to currency code i.e ฿ to THB

3. Check if the TLD of the url where the text was found has a one to one mapping to currency symbol. i.e .th to THB
If more then one country uses the respective currency symbol select the currency with the largest GDP.
TlDs are excluded if that fit out side the normal bands 85% bands of a log transformed linear model i.e .me

4. If all fails revert to the USD if text uses a decimal point or to EUR if the text has a decimal comma.

This rules are currently a speculative approximation with much room for improvement, suggestions are welcome. 


### Resources

* http://research.domaintools.com/statistics/tld-counts/
* https://en.wikipedia.org/wiki/Decimal_mark
* http://www.xe.com/symbols.php

## Installation


## Usage

<pre>
(ns testproject.core
  (:require [currencydetect.core :refer [parse-price]))

(parse-price "£20.00" "http://www.r.co.uk")
=>  {:amount 20.0, :code "GBP", :probability 0.8}

(parse-price "£20.0" "http://www.r.co.uk")
=>  {:amount 20.0, :code "GBP", :probability 0.8}



</pre>


## License

Copyright © 2015 Ben Olsen
Distributed under the Apache License 2.0.
