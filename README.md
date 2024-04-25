# From Brainwave to Feature

This project accompanies a talk of the same name I delivered at Rabobank's Engineer's Week on 16/4/2024.
It covered how you get from a fairly messy specification to usable gherkin scenarios.

## The initial version
For each user that starts the journey, we want to align the corporate details for a given CIN. Authenticated users should be handled a little differently from anonymous users. If you're logged in, we should have your company details on record, so whatever we get back from the external API should already be present in CRM. If it's not, raise an error. If there is an acceptable mismatch between Siebel and the Corporation portal, the portal is leading, but large differences should raise an error. If you're unauthenticated, just create a new record in Siebel and return the new Siebel Id.

This is not great:
* "A little differently" is vague, as is "acceptable mismatch".
* Many pairs of terms seem to refer to the same concept: "corporate details" and "company details", "Siebel" and "CRM", "external API" and "Corporation portal".

## Clean up part 1: get rid of the synonyms

There are three guidelines you can use to favour one term over the other:
* Use the term that everybody uses: ubiquitous.
* Don’t use an implementation/vendor term when a generic term will do. Siebel/CRM; Oracle/Database
* Use a more precise term over a broad term. User is better than stakeholder, and loan applicant is better than user.

But realize that these rules may also be at odds with each other. Siebel is more precise than CRM, but in a functional specification, such an implementation detail is not relevant.

"For each user that starts the journey, we want to align the corporate details for a given CIN. Authenticated users should be handled a little differently from anonymous users. If you're logged in, we should have your company details on record, so whatever we get back from the external API should already be present in CRM. If it's not, raise an error. If there is an acceptable mismatch between Siebel and the Corporation portal, the portal is leading, but large differences should raise an error. If you're unauthenticated, just create a new record in Siebel and return the new Siebel Id."

## Separate the features in bullet points
Sync details for authenticated users:
* Look up CIN in CP
* Look up CIN in CRM
* Not found in CRM? Error.
* Details mismatch significant? Error
* Overwrite CRM state with CP details

Create record for anonymous users:
* Look up CIN in CP
* Create record in CRM with CP details
* Return the new relation Id.

## Don't forget about the edge cases
* What if the CIN is not found in the CP?
* What if an existing user accesses the portal anonymously? We should raise an error.

## About this prototype project

* The implementation is a rapid prototype written in Kotlin, but the features and the test code are already robust, and we're using the models of the published APIs.
* What counts as an acceptable mismatch can get complicated. For the time being we'll define it as having a Levenshtein distance of 3 or less. This algorithm can be swapped for something more sophisticated later.
* Setup of mock data (E.g. CrmMockClient) and verification of results is deliberately minimal. You only include the values that are essential to the flow. Perhaps the company portal requires the name of a director, but that value is not referenced in any business rule. You use a hardcoded default value in the backing code, but don't mention it in the feature file. 
* In general: the features try to cover all possible execution branches, but without specifying or validating every detail or flavour. For that purpose we have openapi and wsdl files. 
* Every test starts with a clean slate. Only the data that you explicitly store in CP or CRM will yield a positive response. 

## Let's have a good laugh with github copilot

Imagine the following specification:
```text
"For each user that starts the journey, we want to align the company details for a given CIN.
Authenticated users should be handled a little differently from anonymous users. 
If you’re authenticated, we should have your company details on record, so whatever we get back from the CP should already be present in CRM.
If it's not, raise an error. If there is an acceptable mismatch between CRM and the CP, the portal is leading, but large differences should raise an error.
If you're anonymous, just create a record in CRM and return the new relation Id."
```

Can you write Gherkin features and scenarios for all the possible cases?

This is what Copilot came up with:

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
