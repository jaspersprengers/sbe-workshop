# From Brainwave to Feature

* The implementation is a rapid prototype, but the features and the test code are already robust, and we're using the models of the published APIs.
* We define an acceptable mismatch as having a Levenshtein distance of 3 or less. This algorithm can be swapped for something more sophisticated later.
* Setup of mock data (E.g. CrmMockClient) and verification of results is deliberately minimal. You only include the values that are essential to the flow. Perhaps the company portal requires the name of a director, but that value is not referenced in any business rule. You use a hardcoded default value in the backing code, but don't mention it in the feature file.
* Every test starts with a clean slate. Only the data that you explicitly store in CP or CRM can be looked up.

## Let's have a good laugh

Imagine the following specification:
```text
"For each user that starts the journey, we want to align the company details for a given CIN.
Authenticated users should be handled a little differently from anonymous users. 
If you’re authenticated, we should have your company details on record, so whatever we get back from the CP should already be present in CRM.
If it's not, raise an error. If there is an acceptable mismatch between CRM and the CP, the portal is leading, but large differences should raise an error.
If you're anonymous, just create a record in CRM and return the new relation Id."
```

Can you write Gherkin features and scenarios for all the possible cases?

```gherkin 
Feature: Align Company Details
As a user
I want to align the company details for a given CIN
So that the company details are consistent across systems

Scenario: Authenticated User with Matching Company Details
Given an authenticated user starts the journey
And the company details in CRM match with the CP
When the user aligns the company details for a given CIN
Then no error should be raised
And the company details should remain the same

Scenario: Authenticated User with Acceptable Mismatch in Company Details
Given an authenticated user starts the journey
And there is an acceptable mismatch between the company details in CRM and the CP
When the user aligns the company details for a given CIN
Then no error should be raised
And the company details should be updated to match the CP

Scenario: Authenticated User with Large Mismatch in Company Details
Given an authenticated user starts the journey
And there is a large mismatch between the company details in CRM and the CP
When the user aligns the company details for a given CIN
Then an error should be raised
And the company details should not be updated

Scenario: Anonymous User Aligning Company Details
Given an anonymous user starts the journey
When the user aligns the company details for a given CIN
Then a new record should be created in CRM
And the new relation Id should be returned
```
