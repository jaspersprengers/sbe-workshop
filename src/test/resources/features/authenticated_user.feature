Feature: Relation retrieval by CIN number for authenticated users

  This feature covers the scenarios for authenticated users, i.e. an existing Rabo customer.

  The system receives a request containing a CIN (Kamer van Koophandel) number.
  This is looked up in the CP and in the CRM, where it should exist.

  If the organisation that is returned from the CP contains subsidiaries, these should also be matched in CRM.

  This feature is a validation step to ensure that the CP does not supply data unknown in CRM.

  Scenario: CIN is found in CP and not in CRM

  For existing users, their CIN must be known in CRM.

    Given CP has an organisation "Snackwereld" with CIN 123 and foundation date 2000-01-01
    When an authenticated user applies with CIN 123
    Then the request is rejected with reason No match for CIN in CRM: 123

  Scenario Outline: CIN is found in CP and CRM, with acceptable mismatches in name

    If a name in CRM is only slightly different from the company portal, we return the one from CRM.

    Given CP has an organisation "<name in portal>" with CIN 123 and foundation date 1980-01-01
    And CRM has a company "<name in crm>" with relation id 007, CIN 123 and status CUSTOMER
    When an authenticated user applies with CIN 123
    Then the response is a relation with id 007, name <name in crm>, CIN 123, foundation year 1980 and status CUSTOMER
    Examples:
      | name in portal | name in crm    |
      | Snackwereld    | Snackwereld    |
      | Snackwereld    | Snackwereld BV |
      | Snackwereld    | snackwereld    |

  Scenario: CIN is found in CP and CRM, with unacceptable mismatch in name

    Given CP has an organisation "Snackwereld" with CIN 123 and foundation date 1980-01-01
    And CRM has a company "Smulwereld" with relation id 007, CIN 123 and status PROSPECT
    When an authenticated user applies with CIN 123
    Then the request is rejected with reason Names do not match: CRM: Smulwereld / company portal: Snackwereld
