Feature: A CIN is unknown to the company portal

  If a given  CIN is not found in the company portal, the request is rejected.
  Functionally, it does not matter whether it is an unknown CIN, or the portal is down
  The logic is identical for authenticated and anonymous users

  Scenario: CIN is not found in Company Portal for an anonymous user
    When an anonymous user applies with CIN 123
    Then the request is rejected with reason No match for CIN in company portal: 123

  Scenario: CIN is not found in Company Portal for an authenticated user
    When an authenticated user applies with CIN 123
    Then the request is rejected with reason No match for CIN in company portal: 123
