Feature: Legal structure retrieval by kvk number for authenticated users

  This feature covers the scenarios for authenticated users, i.e. existing Rabo customer.

  A kvk (Kamer van Koophandel) number for an organisation is presented to the system.
  This is looked up against the kvk portal and in the CRM, where it should exist.

  If the organisation returned from the kvk portal contains subsidiaries, these should also be matched.

  This feature is a validation step to ensure that the kvk portal does not supply data unknown in CRM.

  Background:
    When an authenticated user enter the system

  Scenario: one organisation with one subsidiary and exact name match
    Given kvk has organisation Snackwereld (kvk 123) with subsidiaries Frituur Jos (kvk 456)
    And crm has company Snackwereld (kvk 123) with daughters Frituur Jos (kvk 456)
    When a request for kvk 123 is made
    Then the response contains company Snackwereld (id 000123) with daughters Frituur Jos (id 000456)

  Scenario: one organisation with no subsidiary and different name
    Given kvk has organisation Snackwereld (kvk 123) with no subsidiaries
    And crm has company Monde du snack (kvk 123) with no daughters
    When a request for kvk 123 is made
    Then the response contains company Monde du snack (id 000123) with no daughters
    
  Scenario: organisation unknown in kvk portal
    Given kvk has organisation Snackwereld (kvk 123) with subsidiaries Frituur Jos (kvk 456)
    When a request for kvk 345 is made
    Then the request is rejected with reason No match for kvk number in kvk portal: 345

  Scenario: organisation unknown in CRM
    Given kvk has organisation Snackwereld (kvk 123) with no subsidiaries
    When a request for kvk 123 is made
    Then the request is rejected with reason No match for kvk in CRM: 123

  Scenario: organisation has subsidiaries unknown in CRM
    Given kvk has organisation Snackwereld (kvk 123) with subsidiaries Frituur Jos (kvk 456) and Snackbar Lea (kvk 234)
    And crm has company Snackwereld (kvk 123) with daughters Frituur Jos (kvk 456)
    When a request for kvk 123 is made
    Then the request is rejected with reason kvk structure contains entries unknown in CRM: [234]
    
  Scenario: CRM has child company not present in organisation from kvk portal
    Given kvk has organisation Snackwereld (kvk 123) with subsidiaries Frituur Jos (kvk 456)
    And crm has company Snackwereld (kvk 123) with daughters Snackbar Lea (kvk 567) and Frituur Jos (kvk 456)
    When a request for kvk 123 is made
    Then the response contains company Snackwereld (id 000123) with daughters Snackbar Lea (id 000567) and Frituur Jos (id 000456)

