Apica Loadtest plugin for TeamCity uses Maven to build the project. Also, you'll need JDK 8 or later.

Usage:
======

The build runner will appear as "Apica Loadtest" in the list of build runners in Jenkins.

You will need to know the following values in order to insert an Apica Loadtest build runner:

- the ALT API endpoint that the plugin will communicate with during the load test. Example: https://api-ltp.apicasystem.com/v1 . You can find this information under the 'API' menu of the Apica Loadtest Portal
- the ALT web base URL such as https://loadtest.apicasystem.com
- your LTP authentication token: you can find this under Setup -> User Settings within the Loadtest Portal. Locate and copy the value of "LTP API Auth Ticket"
- the preset name which includes the load test parameters such as duration, user count etc. You can save your presets under New Test in LTP. Copy the name of the preset as it is, e.g. "my Apica plugin preset"
- the load test file name: the name of the load test file that you would like to execute, e.g. "mytest.class" or "test_package.zip"

You'll see various exception messages in case the values cannot be validated. If that happens then review the values and retest.

Thresholds:
===========

Optionally you can enter thresholds to mark a test as failed. E.g. if the average response time was below 5000ms then you can mark the test as failed.
If you don't set any thresholds then the test will be marked as failed in case of an exception otherwise it will pass.

Execution:
==========

You'll be able to follow the job progress in the usual Jenkins build messages. The job status is reported every 10 seconds.

Statistics:
===========

You can view a set of job statistics under the Apica Load Test Summary tab in a tabular format. 
You can also view a trend chart under the Apica Load Test Trends tab which shows a Continuous Integration page from the Loadtest Portal
