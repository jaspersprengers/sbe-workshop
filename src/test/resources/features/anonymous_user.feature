Feature: Legal structure retrieval by kvk number for anonymous users

  This feature covers the scenarios for an anonymous user, i.e., who is not authenticated and may or may not be an existing Rabo customer.

  The system receives a request containing a kvk (Kamer van Koophandel) number.
  This is looked up in the KvK portal (where it should exist) and checked in CRM (where is should NOT exist).
  A new company record is then created in CRM for the parent organisation and all its subsidiaries.
  The corresponding legal structure is returned, based on the data from the kvk portal and enriched with a relation ID for the parent company and any daughters.

  Background:
    When an anonymous user enter the system

  Scenario: a valid kvk number is unknown in CRM
    Given kvk has organisation Snackwereld (kvk 123) with subsidiaries Frituur Jos (kvk 456)
    When a request for kvk 123 is made
    Then the response contains company Snackwereld (id 000123) with daughters Frituur Jos (id 000456)

  Scenario: kvk is found in kvk portal and also known in CRM.

    For the anonymous flow, this is not permitted

    Given kvk has organisation Snackwereld (kvk 123) with no subsidiaries
    And crm has company Snackwereld (kvk 123) with no daughters
    When a request for kvk 123 is made
    Then the request is rejected with reason CRM already has a company with kvk: 123

  Scenario: kvk is not found in kvk portal.
    When a request for kvk 123 is made
    Then the request is rejected with reason No match for kvk number in kvk portal: 123

  Scenario: the same kvk is presented twice

    The call for 456 will fail because it has already been added as the child of 123

    Given kvk has organisation Snackwereld (kvk 123) with subsidiaries Frituur Jos (kvk 456)
    And kvk has organisation Frituur Jos (kvk 456) with no subsidiaries
    When a request for kvk 123 is made
    Then the response contains company Snackwereld (id 000123) with daughters Frituur Jos (id 000456)
    When a request for kvk 456 is made
    Then the request is rejected with reason CRM already has a company with kvk: 456
