Feature: Synchronizing company details for authenticated users

  This feature covers the scenarios for authenticated users, i.e. existing Rabo customers.

  The system receives a request containing a CIN (Company Identification Number).
  This is looked up in the CP and in the CRM, where it should exist.

  This feature ensures that the CP does not supply data unknown in CRM.

  Scenario Outline: CIN is found in CP and CRM, with exactly similar details, or an acceptable difference

  If a name in CRM is only slightly different from the company portal, we return the one from CRM.

    Given CP has an organisation "<name in portal>" with CIN 123
    And CRM has a company "<name in crm>" with relation id 007 and CIN 123
    When an authenticated user applies with CIN 123
    Then the response is a relation with id 007, name <name in crm> and CIN 123
    Examples:
      | name in portal | name in crm    |
      | Snackwereld    | Snackwereld    |
      | Snackwereld    | Snackwereld BV |
      | Snackwereld    | snackwereld    |


  Scenario: CIN is found in CP and not in CRM

  For existing users, their CIN must be known in CRM.

    Given CP has an organisation "Snackwereld" with CIN 123
    When an authenticated user applies with CIN 123
    Then the request is rejected with reason No match for CIN in CRM: 123


  Scenario: CIN is found in CP and CRM, with unacceptable mismatch in name

    Given CP has an organisation "Snackwereld" with CIN 123
    And CRM has a company "Smulwereld" with relation id 007 and CIN 123
    When an authenticated user applies with CIN 123
    Then the request is rejected with reason Names do not match: CRM: Smulwereld / company portal: Snackwereld

  Scenario: CIN is not found in Company Portal for an authenticated user
    When an authenticated user applies with CIN 123
    Then the request is rejected with reason No match for CIN in company portal: 123
