# Generate your test data using Scalacheck’s generators
## Gerbrand van Dieijen

The need for writing automated tests is now well accepted. What about generating your test-data? Most of you may have heard of Property-based-testing, yet, it’s used very rarely, at least in my experience. This session is for people who write unit-tests regularly and want start on property-based testing using Scalacheck.
We’ll start with some classical unit tests for a sample application. First, some of the tests are replaced by TableDrivenProperty test, removing many near duplicate unit-tests. Next, Scalacheck’s generators are used to generate test-data.

