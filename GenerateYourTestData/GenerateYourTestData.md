---
marp: true
---
# Generate your test-data

* Gerbrand van Dieijen
* Programming for a few decades. Scala since around 2011.

---
# What is Property based testing
* Check properties about your software
  * What does my software do? What properties hold?
* Testing using large amount of data
  * Possibly random data
  * Or based on a large corpus
* Framework tries to find data that fails a check
<!--

What does my software actually do? What is supposed to do?
Not very different from TDD, or automatic tests in general but sligly more fundemental

What are properties ?
References:
https://hypothesis.works/articles/what-is-property-based-testing/
-->
---
# What is Fuzzing
* Very much related to property based testing
* Slightly less academic origin
* Using random data originally
* Checking the property: does it crash?
  
# What is Property Based Testing Not
* Proving correctness
  * Type-checking, dependent types, refinement types
  * Idris, Coq
* Testing pure functions only
  * Also for integration-tests too
  * Referential transparency is not a must
    * But it helps
    * And no global side-effects (or rollback after each test)
  

<!--
https://hypothesis.works/articles/referential-transparency/

For proving certain properties you could types
https://en.wikipedia.org/wiki/Refinement_type

https://www.idris-lang.org/

-->
---
# History
- 1999: First (popular) implementation: QuickCheck, by by John Hughes and Koen Claessen
- 2007: ScalaCheck, by Rickard Nilsson
- Also implementations for other programmming languages: C, PHP, Python

<!--
References:
https://github.com/steos/php-quickcheck/blob/master/README.md
--> 
  
# Basic example

```scala

```

---
# References
[Examples of writing mixed unit/property-based (ScalaTest with ScalaCheck) tests.](https://gist.github.com/davidallsopp/60d7474a1fe8dc9b1f2d)
