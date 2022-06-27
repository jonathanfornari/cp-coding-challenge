# cp-coding-challenge

* Java 11 Maven Project
* Part1 and Part2 included on respective packages
* Part 1 -> Known issue: unable to deal with circular dependency on properties parsing, for instance if FOO = ${BAR} and BAR = ${FOO}
* Part 1 -> This solution supports dynamically adding and/or editing custom cases, just create a new folder under test/resouces/custom-test-cases that should include an input.properties file and a expected_assertion.properties, the code will run all the custom tests cases automatically and also assert every expected parsed property result provided on the expected_assertion.properties

