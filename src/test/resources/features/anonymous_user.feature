Feature: Creating a new record in CRM for anonymous users

  This feature covers the scenarios for an anonymous user, someone who is not authenticated, and may or may not be an existing Rabo customer.

  The system receives a request containing a CIN.
  This is looked up in the Chamber of Commerce Company Portal (CP), where it should exist, and checked in CRM, where is should NOT exist.
  A new company record is then created in the CRM for the organisation.
  The corresponding legal structure is returned, based on the data from the CP and enriched with a relation ID.

  Scenario: CIN is found in CP and not in CRM

  For anonymous users, we expect that their CIN is unknown yet.

    Given CP has an organisation "Snackwereld" with CIN 123
    When an anonymous user applies with CIN 123
    Then the response is a relation with a valid id, name Snackwereld and CIN 123

  Scenario: CIN is found in CP and CRM

  For anonymous users, we do not expect their CIN to be found in CRM. If it is, the request is rejected.

    Given CP has an organisation "Snackwereld" with CIN 123 and foundation date 1980-01-01
    And CRM has a company "Snackwereld" with relation id 007 and CIN 123
    When an anonymous user applies with CIN 123
    Then the request is rejected with reason CRM already has a company with CIN: 123

  Scenario: CIN is not found in Company Portal
    When an anonymous user applies with CIN 123
    Then the request is rejected with reason No match for CIN in company portal: 123
